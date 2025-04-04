package ru.cooper.island.services

import ru.cooper.island.entities.islandMap.IslandField
import ru.cooper.island.entities.islandMap.Location
import ru.cooper.island.entities.organism.Animal
import ru.cooper.island.settings.IslandSettings
import ru.cooper.island.util.Randomizer
import java.lang.reflect.InvocationTargetException
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.max
import kotlin.math.min

/**
 * Класс `WorldInitializer` отвечает за инициализацию и создание начального состояния игрового мира.
 * Этот класс создает поле (остров) из локаций, заполняет его животными и растениями,
 * а также определяет соседние локации для каждой локации.
 */
class WorldInitializer(y: Int, x: Int) {

    /**
     * Метод `getIslandField` возвращает игровое поле (остров), созданное классом.
     *
     * @return Объект класса IslandField
     */
    @JvmField
    val islandField: IslandField = IslandField(y, x) // остров

    /**
     * Конструктор класса `WorldInitializer` создает объект класса, инициализируя игровое поле заданными размерами.
     *
     * @param y Количество строк (вертикальных координат)
     * @param x Количество столбцов (горизонтальных координат)
     */
    init {
        initializeLocation()
        generateLocations()
    }

    /**
     * Метод `initializeLocation` заполняет поле (остров) объектами класса Location
     * в каждой ячейке, инициализируя их координатами.
     */
    private fun initializeLocation() {
        val locations = islandField.locations
        for (y in locations[y].indices) {
            for (x in locations.indices) {
                locations[x][y] = Location(y, x)
            }
        }
    }

    private fun setNeighboringLocations(location: Location) {
        val yMin = max((location.coordinate_Y - 1).toDouble(), 0.0).toInt()
        val yMax = min((location.coordinate_Y + 1).toDouble(), (IslandSettings.FIELD_TO_SIZE_Y - 1).toDouble()).toInt()
        val xMin = max((location.coordinate_X - 1).toDouble(), 0.0).toInt()
        val xMax = min((location.coordinate_X + 1).toDouble(), (IslandSettings.FIELD_TO_SIZE_X - 1).toDouble()).toInt()

        for (y in yMin..yMax) {
            for (x in xMin..xMax) {
                if (location.coordinate_Y != y || location.coordinate_X != x) {
                    location.neighboringLocations.add(islandField.getLocation(y, x))
                }
            }
        }
    }

    /**
     * Метод `generateLocations` заполняет каждую локацию на острове соседними локациями,
     * создает наборы животных и растений, а также размещает их на каждой локации.
     */
    private fun generateLocations() {
        val locations = islandField.locations
        for (y in locations[y].indices) {
            for (location in locations) {
                setNeighboringLocations(location[y]!!)
                generationAnimals(location[y]!!)
                generationPlants(location[y]!!)
            }
        }
    }

    private fun generationAnimals(location: Location) {
        for (classAnimal in IslandSettings.ANIMAL_CLASSES) {
            val set: MutableSet<Animal> = ConcurrentHashMap.newKeySet()
            location.animals.put(classAnimal, set)

            if (isCreateEntityType) {
                val numberOfAnimalType =
                    Randomizer.getRandom(0, IslandSettings.ANIMAL_PARAMETERS[classAnimal]!![1].toInt())
                for (i in 0..<numberOfAnimalType) {
                    val animal = tryCreateAnimal(classAnimal)
                    set.add(animal)
                }
            }
        }
    }

    private fun generationPlants(location: Location) {
        if (isCreateEntityType) {
            location.plant = Randomizer.getRandom(0, IslandSettings.MAX_AMOUNT_OF_PLANT_ON_ONE_CELL.toInt()).toDouble()
        }
    }

    private fun <T> tryCreateAnimal(tClass: Class<T>): T {
        try {
            return tClass.getDeclaredConstructor().newInstance()
        } catch (e: InstantiationException) {
            throw RuntimeException("Error creating animal: " + e.message, e)
        } catch (e: IllegalAccessException) {
            throw RuntimeException("Error creating animal: " + e.message, e)
        } catch (e: InvocationTargetException) {
            throw RuntimeException("Error creating animal: " + e.message, e)
        } catch (e: NoSuchMethodException) {
            throw RuntimeException("Error creating animal: " + e.message, e)
        }
    }

    private val isCreateEntityType: Boolean
        get() = Randomizer.getRandom()
}
