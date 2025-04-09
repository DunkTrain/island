package ru.cooper.island;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The main entry point for the Island Simulation application.
 * <p>
 * This class initializes the JavaFX application, loads the UI layout from {@code /main-view.fxml},
 * and displays the primary window. The project combines both Kotlin and Java code.
 * </p>
 */
public class IslandApp extends Application {

    /**
     * Starts the JavaFX application by loading the main FXML layout
     * and setting up the primary stage with the scene.
     *
     * @param primaryStage the main application window.
     * @throws Exception if the FXML resource fails to load or initialization fails.
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
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * The main entry point of the program. Launches the JavaFX application.
     *
     * @param args command-line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Called when the application is about to stop.
     * Ensures a clean shutdown of the process.
     */
    @Override
    public void stop() {
        System.exit(0);
    }
}
