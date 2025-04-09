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

public class WorldLifeCycle {
    private final AtomicInteger simulationStepNumber = new AtomicInteger(0);
    private ScheduledExecutorService scheduler;
    private ExecutorService taskExecutor;
    private final IslandGrid islandGrid;
    private ScheduledFuture<?> currentTask;
    private Consumer<IslandGrid> uiUpdateCallback;
    private int stepDuration = 500; // Дефолтное значение, если не задано в SimulationConfig

    public WorldLifeCycle(WorldInitializer worldInitializer) {
        this.islandGrid = worldInitializer.islandGrid;

        // Пытаемся получить значение из SimulationConfig если оно есть
        try {
            java.lang.reflect.Field stepDurationField = SimulationConfig.class.getDeclaredField("STEP_DURATION");
            stepDurationField.setAccessible(true);
            this.stepDuration = (int) stepDurationField.get(null);
        } catch (Exception ignored) {
            // Используем дефолтное значение
        }
    }

    public void setUIUpdateCallback(Consumer<IslandGrid> callback) {
        this.uiUpdateCallback = callback;
    }

    public void setStepDuration(int duration) {
        this.stepDuration = duration;
        // If simulation is running, restart with new duration
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

    public int getSimulationStepNumber() {
        return simulationStepNumber.get();
    }

    private void executeSimulationStep() {
        simulationStepNumber.incrementAndGet();

        // For console output (can be disabled in GUI mode)
        islandGrid.output();

        List<Future<?>> futures = new ArrayList<>();
        for (int y = 0; y < islandGrid.getHeight(); y++) {
            for (int x = 0; x < islandGrid.getWidth(); x++) {
                IslandCell cell = islandGrid.getLocation(y, x);
                if (cell != null) {
                    futures.addAll(cell.startSimulationTasks(taskExecutor));
                }
            }
        }

        // Wait for all tasks to complete
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                System.err.println("Error waiting for simulation task: " + e.getMessage());
            }
        }

        // Update UI if callback is registered
        if (uiUpdateCallback != null) {
            uiUpdateCallback.accept(islandGrid);
        }

        // Check if we've reached the max steps
        int maxSteps = 100; // Дефолтное значение
        try {
            java.lang.reflect.Field stepsField = SimulationConfig.class.getDeclaredField("NUMBER_OF_SIMULATION_STEPS");
            stepsField.setAccessible(true);
            maxSteps = (int) stepsField.get(null);
        } catch (Exception ignored) {
            // Используем дефолтное значение
        }

        if (simulationStepNumber.get() >= maxSteps) {
            stop();
        }
    }

    private Runnable createSimulationTask() {
        return this::executeSimulationStep;
    }

    public void start() {
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

            // Final UI update
            if (uiUpdateCallback != null) {
                uiUpdateCallback.accept(islandGrid);
            }
        }
    }
}
