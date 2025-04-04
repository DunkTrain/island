package ru.cooper.island.services

import ru.cooper.island.settings.IslandSettings
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

/**
 * Класс WorldLifeCycle представляет собой жизненный цикл симуляции на острове животных.
 * Он отвечает за запуск, остановку и управление шагами симуляции.
 */
class WorldLifeCycle(private val world: WorldInitializer) {
    private val simulationStepNumber = AtomicInteger(0)
    private val service: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()

    private fun startingTheSimulation() {
        val locations = world.islandField.locations
        simulationStepNumber.incrementAndGet()
        world.islandField.output()
        for (y in locations[y].indices) {
            for (location in locations) {
                location[y]!!.start()
            }
        }
    }

    private val lifeTask = Runnable {
        val locations = world.islandField.locations
        startingTheSimulation()
        val numberOfSimulationSteps = IslandSettings.NUMBER_OF_SIMULATION_STEPS
        if (simulationStepNumber.get() >= numberOfSimulationSteps) {
            service.shutdown()
            for (y in locations[y].indices) {
                for (location in locations) {
                    location[y]!!.shutdown()
                }
            }
            for (y in locations[y].indices) {
                for (location in locations) {
                    try {
                        location[y]!!.await(500)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }
            world.islandField.output()
        }
    }

    fun start() {
        val stepDuration = IslandSettings.STEP_DURATION
        service.scheduleAtFixedRate(lifeTask, 1, stepDuration.toLong(), TimeUnit.MILLISECONDS)
    }
}
