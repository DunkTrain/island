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
 * Контроллер главного представления симуляции острова.
 * <p>
 * Отвечает за запуск, остановку и сброс симуляции, а также за обновление визуального состояния сетки.
 * </p>
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

    private WorldInitializer worldInitializer;
    private WorldLifeCycle worldLifeCycle;
    private final Map<String, Color> animalColors = new HashMap<>();
    private final AtomicBoolean simulationRunning = new AtomicBoolean(false);
    private final int CELL_SIZE = 50;

    // Стандартные значения для сетки, если не определены в конфиге
    private final int DEFAULT_GRID_HEIGHT = 10;
    private final int DEFAULT_GRID_WIDTH = 10;

    /**
     * Инициализация контроллера.
     * <p>
     * Вызывается автоматически после загрузки FXML. На этом этапе производится инициализация цветов животных,
     * установка слушателя для изменения скорости симуляции и сброс состояния симуляции.
     * </p>
     */
    @FXML
    public void initialize() {
        initializeAnimalColors();
        resetSimulation();

        speedSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (worldLifeCycle != null) {
                worldLifeCycle.setStepDuration(newVal.intValue());
            }
        });
    }

    /**
     * Инициализирует мапу цветов для различных типов животных.
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
     * Запускает симуляцию.
     * <p>
     * При запуске отключает кнопку старта и включает кнопку остановки, а также передаёт длительность шага симуляции.
     * Симуляция запускается в отдельном потоке.
     * </p>
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

            new Thread(() -> {
                worldLifeCycle.setUIUpdateCallback(this::updateUI);
                worldLifeCycle.start();
            }).start();

            statusLabel.setText("Simulation running");
        }
    }

    /**
     * Останавливает симуляцию.
     * <p>
     * При остановке включает кнопку старта, отключает кнопку остановки и включает кнопку сброса.
     * Также обновляет состояние UI.
     * </p>
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
     * Сбрасывает состояние симуляции.
     * <p>
     * Выполняется сброс текущего состояния, а также инициализируется сетка для отображения симуляции.
     * </p>
     */
    @FXML
    public void handleResetSimulation() {
        resetSimulation();
        statusLabel.setText("Simulation reset");
    }

    /**
     * Выполняет полный сброс симуляции, включая пересоздание мира и перерисовку UI.
     * <p>
     * Пытается получить размеры сетки из конфигурации, используя рефлексию, либо использует значения по умолчанию.
     * Затем инициализируется мир и жизненный цикл симуляции.
     * </p>
     */
    private void resetSimulation() {
        simulationRunning.set(false);
        startButton.setDisable(false);
        stopButton.setDisable(true);
        resetButton.setDisable(false);

        int gridHeight = DEFAULT_GRID_HEIGHT;
        int gridWidth = DEFAULT_GRID_WIDTH;

        try {
            java.lang.reflect.Field gridHeightField = SimulationConfig.class.getDeclaredField("GRID_HEIGHT");
            java.lang.reflect.Field gridWidthField = SimulationConfig.class.getDeclaredField("GRID_WIDTH");
            gridHeightField.setAccessible(true);
            gridWidthField.setAccessible(true);
            gridHeight = (int) gridHeightField.get(null);
            gridWidth = (int) gridWidthField.get(null);
        } catch (Exception ignored) {
            // Используем значения по умолчанию
        }

        worldInitializer = new WorldInitializer(gridHeight, gridWidth);
        worldLifeCycle = new WorldLifeCycle(worldInitializer);

        initializeGridUI(worldInitializer.islandGrid);
    }

    /**
     * Инициализирует графическое представление сетки острова.
     *
     * @param islandGrid Объект, содержащий данные о клетках острова.
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
     * Создает панель для отображения отдельной клетки.
     *
     * @param cell Объект клетки.
     * @return StackPane, представляющий клетку.
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
     * Обновляет текстовую информацию в клетке.
     *
     * @param cellInfo Текстовый элемент для отображения информации.
     * @param cell     Объект клетки.
     */
    private void updateCellInfo(Text cellInfo, IslandCell cell) {
        String dominantType = getDominantAnimalType(cell);
        cellInfo.setText(dominantType);
    }

    /**
     * Определяет доминирующий тип живых организмов в клетке.
     *
     * @param cell Объект клетки.
     * @return Строка с названием доминирующего типа и количеством, либо информацией о растениях.
     */
    private String getDominantAnimalType(IslandCell cell) {
        String dominantType = "";
        int maxCount = 0;

        for (Map.Entry<Class<? extends Animal>, Set<Animal>> entry : cell.getAnimals().entrySet()) {
            int count = entry.getValue().size();
            if (count > maxCount) {
                maxCount = count;
                dominantType = entry.getKey().getSimpleName();
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
     * Обновляет пользовательский интерфейс на основе текущего состояния мира.
     * <p>
     * Метод вызывается из другого потока через UI callback, поэтому используется {@code Platform.runLater}.
     * </p>
     *
     * @param islandGrid Объект, содержащий обновлённую информацию о клетках.
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
     * Обновляет визуальное состояние отдельной клетки в сетке.
     *
     * @param row  Номер строки клетки.
     * @param col  Номер столбца клетки.
     * @param cell Объект клетки с обновлённой информацией.
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

                updateCellInfo(cellInfo, cell);

                String dominantType = getDominantAnimalType(cell).split(" ")[0];
                if (animalColors.containsKey(dominantType)) {
                    background.setFill(animalColors.get(dominantType));
                } else if (cell.getPlant() > 0) {
                    double maxPlantAmount;
                    try {
                        java.lang.reflect.Field maxPlantField = SimulationConfig.class.getDeclaredField("MAX_AMOUNT_OF_PLANT_ON_ONE_CELL");
                        maxPlantField.setAccessible(true);
                        maxPlantAmount = (double) maxPlantField.get(null);
                    } catch (Exception e) {
                        maxPlantAmount = 200.0;
                    }
                    double intensity = Math.min(1.0, cell.getPlant() / maxPlantAmount);
                    background.setFill(Color.rgb(34, (int)(139 + 116 * intensity), 34));
                } else {
                    background.setFill(Color.FORESTGREEN);
                }
            }
        }
    }
}
