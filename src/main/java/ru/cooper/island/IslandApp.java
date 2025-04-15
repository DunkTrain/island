package ru.cooper.island;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Точка входа в JavaFX-приложение "Island Simulation".
 * <p>
 * Отвечает за:
 * <ul>
 *     <li>Загрузку главного FXML-файла {@code /main-view.fxml}</li>
 *     <li>Создание {@link Scene} и показ {@link Stage}</li>
 *     <li>Управление жизненным циклом JavaFX (методы {@code init()}, {@code stop()}, {@code start(Stage)})</li>
 * </ul>
 * Проект использует Kotlin и Java совместно.
 */
public class IslandApp extends Application {

    /**
     * Точка входа для запуска приложения (вызов {@link Application#launch(String...)}).
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Метод {@code start} вызывается JavaFX при старте приложения.
     * <p>
     * Здесь:
     * <ul>
     *     <li>Загружается FXML (главная сцена)</li>
     *     <li>Создаётся {@link Scene} заданного размера</li>
     *     <li>Отображается {@link Stage}</li>
     * </ul>
     *
     * @param primaryStage основной контейнер UI
     * @throws Exception если произошла ошибка при загрузке ресурса FXML
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main-view.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 1000, 800);
            primaryStage.setTitle("Island Simulation");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            System.err.println("Не удалось запустить JavaFX-приложение: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Вызывается при закрытии приложения (например, пользователь закрыл окно).
     * Завершает процесс через {@link System#exit(int)}.
     */
    @Override
    public void stop() {
        System.exit(0);
    }
}
