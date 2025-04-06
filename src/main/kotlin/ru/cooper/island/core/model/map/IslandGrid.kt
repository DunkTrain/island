package ru.cooper.island.core.model.map

import ru.cooper.island.core.model.Animal
import ru.cooper.island.config.SimulationConfig

class IslandGrid(
    private val gridHeight: Int,
    private val gridWidth: Int
) {

    val height: Int get() = gridHeight
    val width: Int get() = gridWidth

    @JvmField
    val islandCells: Array<Array<IslandCell?>> = Array(gridWidth) { arrayOfNulls(gridHeight) }

    private var iterationCount = 0

    fun getLocation(y: Int, x: Int): IslandCell? = islandCells[x][y]

    fun output() {
        printGridLayout()
        printIslandStatistics()
        increaseIterationCount()
    }

    private fun printGridLayout() {
        for (y in 0 until gridHeight) {
            for (x in 0 until gridWidth) {
                print(islandCells[x][y])
            }
            println()
        }
    }

    private fun printIslandStatistics() {
        println("\n${"*".repeat(105)}\n")
        println("Статистика острова:")

        val animalStatistics = calculateAnimalPopulation()
        val plantStatistics = calculateTotalPlants()

        printAnimalStatistics(animalStatistics)
        printPlantStatistics(plantStatistics)
        printSimulationProgress()
        println("\n${"*".repeat(105)}\n")
    }

    private fun calculateAnimalPopulation(): Map<Class<out Animal>, Int> {
        val populationMap = mutableMapOf<Class<out Animal>, Int>()

        for (y in 0 until gridHeight) {
            for (x in 0 until gridWidth) {
                val currentLocation = islandCells[x][y] ?: continue

                SimulationConfig.ANIMAL_CLASSES.forEach { animalClass ->
                    currentLocation.animals[animalClass]?.let { animals ->
                        populationMap.merge(animalClass, animals.size, Int::plus)
                    }
                }
            }
        }
        return populationMap
    }

    private fun calculateTotalPlants(): Double {
        var totalPlants = 0.0
        for (y in 0 until gridHeight) {
            for (x in 0 until gridWidth) {
                totalPlants += islandCells[x][y]?.plant ?: 0.0
            }
        }
        return totalPlants
    }

    private fun printAnimalStatistics(statistics: Map<Class<out Animal>, Int>) {
        statistics.forEach { (animalClass, count) ->
            println("${animalClass.simpleName}: $count")
        }
    }

    private fun printPlantStatistics(totalPlants: Double) {
        println("Общее количество растений: ${totalPlants.toInt()}")
    }

    private fun printSimulationProgress() {
        println("Прошедшие дни: $iterationCount из ${SimulationConfig.NUMBER_OF_SIMULATION_STEPS}")
    }

    private fun increaseIterationCount() {
        iterationCount++
    }
}
