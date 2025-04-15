package ru.cooper.island.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import ru.cooper.island.config.SimulationConfig;
import ru.cooper.island.core.model.Animal;
import ru.cooper.island.core.model.map.IslandCell;
import ru.cooper.island.core.model.map.IslandGrid;
import ru.cooper.island.services.initialization.WorldInitializer;
import ru.cooper.island.services.lifecycle.WorldLifeCycle;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Главный контроллер JavaFX-приложения "Остров животных".
 * <p>
 * Отвечает за:
 * <ul>
 *     <li>Запуск симуляции</li>
 *     <li>Останов симуляции</li>
 *     <li>Сброс (полное обновление) симуляции</li>
 *     <li>Обновление UI (отображение животных и растений в сетке)</li>
 * </ul>
 * Все действия с UI происходят внутри JavaFX Application Thread.
 */
public class MainController {

    @FXML
    private GridPane islandGridPane;

    @FXML
    private Label statusLabel;

    @FXML
    private Button startButton;

    @FXML
    private Button stopButton;

    @FXML
    private Button resetButton;

    @FXML
    private Slider speedSlider;

    private WorldLifeCycle worldLifeCycle;

    /**
     * Map, связывающая имя животного (String) с цветом (Color),
     * чтобы клетка с доминирующим животным красилась соответствующим образом.
     */
    private final Map<String, Color> animalColors = new HashMap<>();

    /**
     * Флаг, указывающий, запущена ли симуляция.
     */
    private final AtomicBoolean simulationRunning = new AtomicBoolean(false);

    /**
     * Размер одной клетки в пикселях.
     */
    private static final int CELL_SIZE = 50;

    /**
     * Инициализация контроллера.
     * <p>
     * Вызывается автоматически после загрузки FXML.
     * В этом методе:
     * <ul>
     *     <li>Инициализируется карта цветов животных</li>
     *     <li>Вызывается {@link #resetSimulation()} для установки начального состояния</li>
     *     <li>Устанавливается слушатель для слайдера скорости</li>
     * </ul>
     */
    @FXML
    public void initialize() {
        initializeAnimalColors();
        resetSimulation();

        // Слушатель изменения скорости, чтобы в реальном времени обновлять шаг симуляции
        speedSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (worldLifeCycle != null) {
                worldLifeCycle.setStepDuration(newVal.intValue());
            }
        });
    }

    /**
     * Заполняет {@link #animalColors} цветами для различных животных.
     */
    private void initializeAnimalColors() {
        animalColors.put("Wolf", Color.DARKGRAY);
        animalColors.put("Fox", Color.ORANGE);
        animalColors.put("Bear", Color.BROWN);
        animalColors.put("Eagle", Color.DARKBLUE);
        animalColors.put("Horse", Color.SADDLEBROWN);
        animalColors.put("Deer", Color.BURLYWOOD);
        animalColors.put("Rabbit", Color.LIGHTGRAY);
        animalColors.put("Mouse", Color.LIGHTGREY);
        animalColors.put("Goat", Color.BEIGE);
        animalColors.put("Sheep", Color.WHITE);
        animalColors.put("Boar", Color.DARKRED);
        animalColors.put("Buffalo", Color.DARKSLATEGRAY);
        animalColors.put("Duck", Color.YELLOW);
        animalColors.put("Caterpillar", Color.GREENYELLOW);
        animalColors.put("Snake", Color.GREEN);
    }

    /**
     * Обработчик кнопки "Start".
     * <p>
     * Запускает симуляцию, если она ещё не запущена. Блокирует кнопку Start,
     * разблокирует кнопку Stop и отключает кнопку Reset.
     */
    @FXML
    public void handleStartSimulation() {
        if (!simulationRunning.get()) {
            simulationRunning.set(true);
            startButton.setDisable(true);
            stopButton.setDisable(false);
            resetButton.setDisable(true);

            int stepDuration = (int) speedSlider.getValue();
            worldLifeCycle.setStepDuration(stepDuration);

            // Запускаем симуляцию в отдельном потоке
            new Thread(() -> {
                worldLifeCycle.setUIUpdateCallback(this::updateUI);
                worldLifeCycle.start();
            }).start();

            statusLabel.setText("Simulation running");
        }
    }

    /**
     * Обработчик кнопки "Stop".
     * <p>
     * Останавливает симуляцию, если она была запущена.
     * Разблокирует кнопку Start, блокирует Stop и разблокирует Reset.
     */
    @FXML
    public void handleStopSimulation() {
        if (simulationRunning.get()) {
            worldLifeCycle.stop();
            simulationRunning.set(false);

            startButton.setDisable(false);
            stopButton.setDisable(true);
            resetButton.setDisable(false);

            statusLabel.setText("Simulation stopped");
        }
    }

    /**
     * Обработчик кнопки "Reset".
     * <p>
     * Полностью пересоздаёт мир и обновляет интерфейс. Останавливает текущую симуляцию.
     */
    @FXML
    public void handleResetSimulation() {
        resetSimulation();
        statusLabel.setText("Simulation reset");
    }

    /**
     * Полный сброс симуляции:
     * <ul>
     *     <li>Сбрасывает флаг {@link #simulationRunning}</li>
     *     <li>Создаёт {@link WorldInitializer} и {@link WorldLifeCycle}</li>
     *     <li>Вызывает {@link #initializeGridUI(IslandGrid)} для перерисовки интерфейса</li>
     * </ul>
     */
    private void resetSimulation() {
        simulationRunning.set(false);
        startButton.setDisable(false);
        stopButton.setDisable(true);
        resetButton.setDisable(false);

        // Берём напрямую
        int gridHeight = SimulationConfig.FIELD_TO_SIZE_Y;
        int gridWidth  = SimulationConfig.FIELD_TO_SIZE_X;

        // Инициализируем мир и жизненный цикл
        WorldInitializer worldInitializer = new WorldInitializer(gridHeight, gridWidth);
        worldLifeCycle   = new WorldLifeCycle(worldInitializer);

        // Перерисовываем UI
        initializeGridUI(worldInitializer.getIslandGrid());
    }

    /**
     * Создаёт видимую сетку в UI (GridPane) на основе данных {@link IslandGrid}.
     *
     * @param islandGrid объект с данными о клетках.
     */
    private void initializeGridUI(IslandGrid islandGrid) {
        if (islandGridPane != null) {
            islandGridPane.getChildren().clear();
            islandGridPane.getRowConstraints().clear();
            islandGridPane.getColumnConstraints().clear();

            for (int y = 0; y < islandGrid.getHeight(); y++) {
                for (int x = 0; x < islandGrid.getWidth(); x++) {
                    IslandCell cell = islandGrid.getLocation(y, x);
                    if (cell != null) {
                        StackPane cellPane = createCellPane(cell);
                        islandGridPane.add(cellPane, x, y);
                    }
                }
            }
        }
    }

    /**
     * Создаёт {@link StackPane} для отображения одной клетки (фон + текст).
     *
     * @param cell клетка острова
     * @return {@link StackPane}, отображающий клетку.
     */
    private StackPane createCellPane(IslandCell cell) {
        StackPane stackPane = new StackPane();
        stackPane.setPrefSize(CELL_SIZE, CELL_SIZE);

        Rectangle background = new Rectangle(CELL_SIZE, CELL_SIZE);
        background.setFill(Color.FORESTGREEN);
        background.setStroke(Color.BLACK);
        background.setStrokeWidth(0.5);

        Text cellInfo = new Text();
        updateCellInfo(cellInfo, cell);

        stackPane.getChildren().addAll(background, cellInfo);
        return stackPane;
    }

    /**
     * Обновляет текст в переданном {@code cellInfo} на основе содержимого {@code cell}.
     *
     * @param cellInfo элемент {@link Text} для отображения информации
     * @param cell     клетка острова, из которой берутся данные
     */
    private void updateCellInfo(Text cellInfo, IslandCell cell) {
        String dominantType = getDominantAnimalType(cell);
        cellInfo.setText(dominantType);
    }

    /**
     * Определяет «доминирующее» животное (или растение) в клетке для краткого отображения.
     *
     * @param cell клетка
     * @return строка вида "Wolf (3)" или "🌿 15.0", либо пустая строка
     */
    private String getDominantAnimalType(IslandCell cell) {
        String dominantType = "";
        int maxCount = 0;

        for (Map.Entry<Class<? extends Animal>, Set<Animal>> entry : cell.getAnimals().entrySet()) {
            int count = entry.getValue().size();
            if (count > maxCount) {
                maxCount = count;
                dominantType = entry.getKey().getSimpleName(); // Название класса животного
            }
        }

        if (maxCount > 0) {
            return dominantType + " (" + maxCount + ")";
        } else if (cell.getPlant() > 0) {
            return "🌿 " + String.format("%.1f", cell.getPlant());
        } else {
            return "";
        }
    }

    /**
     * Callback, вызываемый из фонового потока симуляции для обновления UI.
     * <p>
     * Использует {@code Platform.runLater}, чтобы изменения шли в JavaFX Application Thread.
     *
     * @param islandGrid текущее состояние всех клеток.
     */
    public void updateUI(IslandGrid islandGrid) {
        Platform.runLater(() -> {
            for (int y = 0; y < islandGrid.getHeight(); y++) {
                for (int x = 0; x < islandGrid.getWidth(); x++) {
                    IslandCell cell = islandGrid.getLocation(y, x);
                    if (cell != null) {
                        updateCell(y, x, cell);
                    }
                }
            }
            statusLabel.setText("Simulation step: " + worldLifeCycle.getSimulationStepNumber());
        });
    }

    /**
     * Обновляет UI (цвет и текст) для одной клетки в {@link GridPane}.
     *
     * @param row  индекс строки
     * @param col  индекс столбца
     * @param cell данные клетки
     */
    private void updateCell(int row, int col, IslandCell cell) {
        if (islandGridPane != null) {
            StackPane cellPane = null;
            for (javafx.scene.Node node : islandGridPane.getChildren()) {
                Integer nodeRow = GridPane.getRowIndex(node);
                Integer nodeCol = GridPane.getColumnIndex(node);
                if (nodeRow != null && nodeCol != null && nodeRow == row && nodeCol == col) {
                    cellPane = (StackPane) node;
                    break;
                }
            }

            if (cellPane != null && cellPane.getChildren().size() >= 2) {
                Rectangle background = (Rectangle) cellPane.getChildren().get(0);
                Text cellInfo = (Text) cellPane.getChildren().get(1);

                // Обновляем текст
                updateCellInfo(cellInfo, cell);

                // Определяем цвет клетки
                String dominantType = getDominantAnimalType(cell).split(" ")[0];
                if (animalColors.containsKey(dominantType)) {
                    background.setFill(animalColors.get(dominantType));
                } else if (cell.getPlant() > 0) {
                    double maxPlantAmount = 200.0;
                    try {
                        java.lang.reflect.Field maxPlantField = SimulationConfig.class.getDeclaredField("MAX_AMOUNT_OF_PLANT_ON_ONE_CELL");
                        maxPlantField.setAccessible(true);
                        maxPlantAmount = (double) maxPlantField.get(null);
                    } catch (Exception e) {
                        System.err.println("Не удалось получить MAX_AMOUNT_OF_PLANT_ON_ONE_CELL: " + e.getMessage());
                    }
                    double intensity = Math.min(1.0, cell.getPlant() / maxPlantAmount);
                    background.setFill(Color.rgb(34, (int) (139 + 116 * intensity), 34));
                } else {
                    background.setFill(Color.FORESTGREEN);
                }
            }
        }
    }
}
