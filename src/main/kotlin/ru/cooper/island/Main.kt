package ru.cooper.island

import ru.cooper.island.services.WorldInitializer
import ru.cooper.island.services.WorldLifeCycle
import ru.cooper.island.settings.IslandSettings
import ru.cooper.island.settings.IslandSettings.Companion.countdown
import ru.cooper.island.settings.IslandSettings.Companion.printEnter
import ru.cooper.island.settings.IslandSettings.Companion.printWelcomeMessage
import java.util.*

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        printWelcomeMessage()
        val scanner = Scanner(System.`in`)
        printEnter(scanner)
        countdown(3)


        val world = WorldInitializer(IslandSettings.FIELD_TO_SIZE_Y, IslandSettings.FIELD_TO_SIZE_X)
        val lifeWorker = WorldLifeCycle(world)
        lifeWorker.start()
    }
}
