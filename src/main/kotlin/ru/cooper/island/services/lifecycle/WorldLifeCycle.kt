package ru.cooper.island.services.lifecycle

import ru.cooper.island.config.SimulationConfig
import ru.cooper.island.core.model.map.IslandGrid
import ru.cooper.island.services.initialization.WorldInitializer
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger

class WorldLifeCycle(private val worldInitializer: WorldInitializer) {
    private val simulationStepNumber = AtomicInteger(0)
    private val scheduler: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
    private val taskExecutor: ExecutorService = Executors.newWorkStealingPool()
    private val islandGrid: IslandGrid = worldInitializer.islandGrid

    private fun executeSimulationStep() {
        simulationStepNumber.incrementAndGet()
        islandGrid.output()

        val futures = mutableListOf<Future<*>>()
        for (y in 0 until islandGrid.height) {
            for (x in 0 until islandGrid.width) {
                islandGrid.getLocation(y, x)?.let { cell ->
                    futures += cell.startSimulationTasks(taskExecutor)
                }
            }
        }

        // Ожидаем завершения всех задач текущего шага
        futures.forEach { it.get() }
    }

    private val simulationTask = Runnable {
        executeSimulationStep()

        if (simulationStepNumber.get() >= SimulationConfig.NUMBER_OF_SIMULATION_STEPS) {
            shutdownSimulation()
        }
    }

    private fun shutdownSimulation() {
        scheduler.shutdown()
        taskExecutor.shutdown()

        try {
            if (!taskExecutor.awaitTermination(1, TimeUnit.SECONDS)) {
                taskExecutor.shutdownNow()
            }
        } catch (e: InterruptedException) {
            taskExecutor.shutdownNow()
            Thread.currentThread().interrupt()
        }

        islandGrid.output()
    }

    fun start() {
        scheduler.scheduleWithFixedDelay(
            simulationTask,
            1,
            SimulationConfig.STEP_DURATION.toLong(),
            TimeUnit.MILLISECONDS
        )
    }
}
