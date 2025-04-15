package ru.cooper.island.services.lifecycle;

import ru.cooper.island.config.SimulationConfig;
import ru.cooper.island.core.model.map.IslandCell;
import ru.cooper.island.core.model.map.IslandGrid;
import ru.cooper.island.services.initialization.WorldInitializer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * Класс, управляющий жизненным циклом симуляции.
 * <p>
 * Основные задачи:
 * <ul>
 *     <li>Периодический вызов {@link #executeSimulationStep()} через {@link ScheduledExecutorService}</li>
 *     <li>Запуск потока-исполнителя {@code taskExecutor}, который обрабатывает животных и растения</li>
 *     <li>Ожидание завершения всех задач, обновление UI по callbacks</li>
 *     <li>Остановка при достижении максимального числа шагов симуляции</li>
 * </ul>
 */
public class WorldLifeCycle {

    /**
     * Текущее число шагов симуляции, начиная с 1.
     */
    private final AtomicInteger simulationStepNumber = new AtomicInteger(0);

    /**
     * Планировщик шагов симуляции (однопоточный, чтобы не гоняться за синхронизацией).
     */
    private ScheduledExecutorService scheduler;

    /**
     * Пул потоков для обработки задач (животные, растения).
     */
    private ExecutorService taskExecutor;

    /**
     * Сетка острова, по которой идёт симуляция.
     */
    private final IslandGrid islandGrid;

    /**
     * Ссылка на будущую задачу (шаг симуляции).
     */
    private ScheduledFuture<?> currentTask;

    /**
     * Callback, вызываемый для обновления UI после каждого шага.
     */
    private Consumer<IslandGrid> uiUpdateCallback;

    /**
     * Продолжительность одного шага симуляции (мс).
     */
    private int stepDuration;

    /**
     * Создаёт новый жизненный цикл, инициализируя поля из {@link WorldInitializer}.
     * <p>
     * Кроме того, пытается получить значение {@code STEP_DURATION} из {@link SimulationConfig}
     * (через reflection), если оно задано.
     *
     * @param worldInitializer объект, содержащий {@code IslandGrid}.
     */
    public WorldLifeCycle(WorldInitializer worldInitializer) {
        this.islandGrid = worldInitializer.getIslandGrid();
        this.stepDuration = SimulationConfig.STEP_DURATION;
    }

    /**
     * Устанавливает callback, который будет вызван после каждого шага симуляции для обновления UI.
     *
     * @param callback callback, принимающий {@link IslandGrid}.
     */
    public void setUIUpdateCallback(Consumer<IslandGrid> callback) {
        this.uiUpdateCallback = callback;
    }

    /**
     * Устанавливает длительность одного шага (мс). Если симуляция уже запущена,
     * останавливает текущую задачу и перезапускает с новым интервалом.
     *
     * @param duration новая длительность шага в миллисекундах
     */
    public void setStepDuration(int duration) {
        this.stepDuration = duration;

        // Если симуляция уже идёт, пересоздаём расписание с новой задержкой
        if (scheduler != null && !scheduler.isShutdown() && currentTask != null) {
            currentTask.cancel(false);
            currentTask = scheduler.scheduleWithFixedDelay(
                    createSimulationTask(),
                    0,
                    stepDuration,
                    TimeUnit.MILLISECONDS
            );
        }
    }

    /**
     * Возвращает текущий номер шага симуляции.
     *
     * @return значение {@link #simulationStepNumber}.
     */
    public int getSimulationStepNumber() {
        return simulationStepNumber.get();
    }

    /**
     * Выполняет один «шаг» симуляции:
     * <ul>
     *     <li>Увеличивает счётчик шагов</li>
     *     <li>Запускает задачи для каждой клетки (животные, растения)</li>
     *     <li>Ждёт окончания всех задач</li>
     *     <li>Запускает callback {@code uiUpdateCallback}, если задан</li>
     *     <li>Останавливает симуляцию при достижении {@code NUMBER_OF_SIMULATION_STEPS}</li>
     * </ul>
     */
    private void executeSimulationStep() {
        // Увеличиваем счётчик
        simulationStepNumber.incrementAndGet();

        List<Future<?>> futures = new ArrayList<>();
        for (int y = 0; y < islandGrid.getHeight(); y++) {
            for (int x = 0; x < islandGrid.getWidth(); x++) {
                IslandCell cell = islandGrid.getLocation(y, x);
                if (cell != null) {
                    // Собираем все задачи (животные + растения)
                    futures.addAll(cell.startSimulationTasks(taskExecutor));
                }
            }
        }

        // Ждём выполнения всех задач
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                // Логирование ошибки в задаче
                System.err.println("Ошибка при ожидании задачи симуляции: " + e.getMessage());
            }
        }

        // Обновляем UI
        if (uiUpdateCallback != null) {
            uiUpdateCallback.accept(islandGrid);
        }

        // Проверяем, не вышли ли за предел шага
        int maxSteps = 100; // значение по умолчанию
        try {
            java.lang.reflect.Field stepsField = SimulationConfig.class.getDeclaredField("NUMBER_OF_SIMULATION_STEPS");
            stepsField.setAccessible(true);
            maxSteps = (int) stepsField.get(null);
        } catch (Exception e) {
            System.err.println("Не удалось получить NUMBER_OF_SIMULATION_STEPS: " + e.getMessage());
        }

        if (simulationStepNumber.get() >= maxSteps) {
            stop();
        }
    }

    /**
     * Обёртка для создания задачи, которая периодически вызывает {@link #executeSimulationStep()}.
     *
     * @return {@link Runnable}, вызывающий {@link #executeSimulationStep()}.
     */
    private Runnable createSimulationTask() {
        return this::executeSimulationStep;
    }

    /**
     * Запускает симуляцию, создавая планировщик и пул потоков.
     * Если симуляция уже была запущена и не остановлена, повторный вызов не создаёт новые ресурсы.
     */
    public void start() {
        // Создаём planScheduler и пул потоков, если ещё не созданы или были остановлены
        if (scheduler == null || scheduler.isShutdown()) {
            scheduler = Executors.newSingleThreadScheduledExecutor();
            taskExecutor = Executors.newWorkStealingPool();

            currentTask = scheduler.scheduleWithFixedDelay(
                    createSimulationTask(),
                    0,
                    stepDuration,
                    TimeUnit.MILLISECONDS
            );
        }
    }

    /**
     * Останавливает планировщик и пул потоков.
     * <p>
     * Дожидается окончания задач (1 сек), если не вышло — останавливает жёстко {@code shutdownNow()}.
     * После этого вызывает финальное обновление UI.
     */
    public void stop() {
        if (scheduler != null) {
            scheduler.shutdown();
            taskExecutor.shutdown();

            try {
                if (!taskExecutor.awaitTermination(1, TimeUnit.SECONDS)) {
                    taskExecutor.shutdownNow();
                }
            } catch (InterruptedException e) {
                taskExecutor.shutdownNow();
                Thread.currentThread().interrupt();
            }

            // Финальный callback UI
            if (uiUpdateCallback != null) {
                uiUpdateCallback.accept(islandGrid);
            }
        }
    }
}
