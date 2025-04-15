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

    /**
     * Сетка острова, содержащая ссылки на все клетки.
     */
    val islandGrid: IslandGrid = IslandGrid(gridHeight, gridWidth)

    init {
        initializeCells()
        populateGridWithLife()
    }

    /**
     * Инициализирует ячейки (IslandCell) в сетке острова.
     */
    private fun initializeCells() {
        for (y in 0 until gridHeight) {
            for (x in 0 until gridWidth) {
                islandGrid.islandCells[x][y] = IslandCell(y, x)
            }
        }
    }

    /**
     * Заполняет клетки животными и растениями с заданной вероятностью.
     */
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

    /**
     * Устанавливает связи между клеткой и соседними клетками (8 направлений).
     */
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

    /**
     * Наполняет клетку животными, количество выбирается случайно, но не превышает установленных параметров.
     */
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

    /**
     * Заполняет клетку растениями (plant) случайным образом в установленных пределах.
     */
    private fun populateCellWithPlants(cell: IslandCell) {
        cell.plant = Randomizer.getRandom(
            0,
            SimulationConfig.MAX_AMOUNT_OF_PLANT_ON_ONE_CELL.toInt()
        ).toDouble()
    }

    /**
     * Создает животное через рефлексию.
     */
    private fun createAnimal(animalClass: Class<out Animal>): Animal? {
        return try {
            animalClass.getDeclaredConstructor().newInstance()
        } catch (e: ReflectiveOperationException) {
            System.err.println("Error creating animal ${animalClass.simpleName}: ${e.message}")
            null
        }
    }

    companion object {
        /**
         * Вероятность (в %) создания живности/растений в каждой клетке.
         */
        private const val CREATION_PROBABILITY = 70
    }
}
