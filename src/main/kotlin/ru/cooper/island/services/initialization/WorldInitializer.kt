package ru.cooper.island.services.initialization

import ru.cooper.island.core.model.map.IslandGrid
import ru.cooper.island.core.model.map.IslandCell
import ru.cooper.island.core.model.Animal
import ru.cooper.island.config.SimulationConfig
import ru.cooper.island.util.Randomizer
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.max
import kotlin.math.min

/**
 * Инициализатор игрового мира. Создает остров с клетками, заполняет их животными и растениями,
 * устанавливает связи между соседними клетками.
 */
class WorldInitializer(
    private val gridHeight: Int,
    private val gridWidth: Int
) {
    @JvmField
    val islandGrid: IslandGrid = IslandGrid(gridHeight, gridWidth)

    init {
        initializeCells()
        populateGridWithLife()
    }

    private fun initializeCells() {
        for (y in 0 until gridHeight) {
            for (x in 0 until gridWidth) {
                islandGrid.islandCells[x][y] = IslandCell(y, x)
            }
        }
    }

    private fun populateGridWithLife() {
        for (y in 0 until gridHeight) {
            for (x in 0 until gridWidth) {
                val cell = islandGrid.getLocation(y, x) ?: continue

                connectAdjacentCells(cell)
                if (Randomizer.getRandom(0, 100) <= CREATION_PROBABILITY) {
                    populateCellWithAnimals(cell)
                    populateCellWithPlants(cell)
                }
            }
        }
    }

    private fun connectAdjacentCells(cell: IslandCell) {
        val minY = max(cell.yCoordinate - 1, 0)
        val maxY = min(cell.yCoordinate + 1, gridHeight - 1)
        val minX = max(cell.xCoordinate - 1, 0)
        val maxX = min(cell.xCoordinate + 1, gridWidth - 1)

        for (y in minY..maxY) {
            for (x in minX..maxX) {
                if (cell.yCoordinate != y || cell.xCoordinate != x) {
                    islandGrid.getLocation(y, x)?.let { neighbor ->
                        cell.addNeighboringLocation(neighbor)
                    }
                }
            }
        }
    }

    private fun populateCellWithAnimals(cell: IslandCell) {
        SimulationConfig.ANIMAL_CLASSES.forEach { animalClass ->
            val maxCount = SimulationConfig.ANIMAL_PARAMETERS[animalClass]?.get(1)?.toInt() ?: 0
            val animalCount = Randomizer.getRandom(0, maxCount)

            val animals = ConcurrentHashMap.newKeySet<Animal>()
            cell.setAnimals(animalClass, animals)

            repeat(animalCount) {
                createAnimal(animalClass)?.let { animals.add(it) }
            }
        }
    }

    private fun populateCellWithPlants(cell: IslandCell) {
        cell.plant = Randomizer.getRandom(
            0,
            SimulationConfig.MAX_AMOUNT_OF_PLANT_ON_ONE_CELL.toInt()
        ).toDouble()
    }

    private fun createAnimal(animalClass: Class<out Animal>): Animal? {
        return try {
            animalClass.getDeclaredConstructor().newInstance()
        } catch (e: ReflectiveOperationException) {
            System.err.println("Error creating animal ${animalClass.simpleName}: ${e.message}")
            null
        }
    }

    companion object {
        private const val CREATION_PROBABILITY = 70 // 70% chance to create entities in a cell
    }
}
