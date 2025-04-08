package ru.cooper.island

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import ru.cooper.island.services.initialization.WorldInitializer
import ru.cooper.island.services.lifecycle.WorldLifeCycle
import ru.cooper.island.config.SimulationConfig

class Main : Application() {
    override fun start(primaryStage: Stage) {
        // Load FXML
        val loader = FXMLLoader(Main::class.java.getResource("/main-view.fxml"))
        val scene = Scene(loader.load(), 1000.0, 800.0)

        // Initialization of the world and life cycle
        val world = WorldInitializer(SimulationConfig.FIELD_TO_SIZE_Y, SimulationConfig.FIELD_TO_SIZE_X)
        val lifeWorker = WorldLifeCycle(world)

        // Running a simulation
        lifeWorker.start()

        // Setting window
        primaryStage.title = "Island Simulation"
        primaryStage.scene = scene
        primaryStage.show()
    }
}

fun main(args: Array<String>) {
    Application.launch(Main::class.java, *args)
}
