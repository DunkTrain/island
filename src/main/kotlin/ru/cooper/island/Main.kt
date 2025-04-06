package ru.cooper.island

import ru.cooper.island.services.initialization.WorldInitializer
import ru.cooper.island.services.lifecycle.WorldLifeCycle
import ru.cooper.island.config.SimulationConfig
import ru.cooper.island.config.SimulationConfig.Companion.countdown
import ru.cooper.island.config.SimulationConfig.Companion.printEnter
import ru.cooper.island.config.SimulationConfig.Companion.printWelcomeMessage
import java.util.*

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        printWelcomeMessage()
        val scanner = Scanner(System.`in`)
        printEnter(scanner)
        countdown(3)


        val world = WorldInitializer(SimulationConfig.FIELD_TO_SIZE_Y, SimulationConfig.FIELD_TO_SIZE_X)
        val lifeWorker = WorldLifeCycle(world)
        lifeWorker.start()
    }
}
