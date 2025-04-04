package ru.cooper.island.entities.islandMap

import ru.cooper.island.entities.organism.Animal
import ru.cooper.island.settings.IslandSettings

/**
 * Класс IslandField представляет собой поле - остров с клетками (локациями),
 * где живут различные виды животных и растения.
 */
class IslandField(y: Int, x: Int) {
    @JvmField
    val locations: Array<Array<Location?>> =
        Array(x) {
            arrayOfNulls(y)
        }
    private var iterationCount = 0 // Счетчик прошедших итераций (дней)

    fun getLocation(yPosition: Int, xPosition: Int): Location? {
        return locations[xPosition][yPosition]
    }

    fun output() {
        for (y in locations[y].indices) {
            for (location in locations) {
                print(location[y])
            }
            println()
        }


        println("\n*******************************************************************************************\n")
        // Вывод информации о жизни на острове
        println("Статистика острова:")

        // Подсчет количества животных каждого вида
        val animalCountMap: MutableMap<Class<out Animal?>, Int> = HashMap()
        for (y in locations[y].indices) {
            for (value in locations) {
                val location = value[y]
                for (animalClass in IslandSettings.ANIMAL_CLASSES) {
                    val animalCount = location!!.animals[animalClass]!!.size
                    animalCountMap[animalClass] = animalCountMap.getOrDefault(animalClass, 0) + animalCount
                }
            }
        }

        // Вывод количества животных каждого вида
        for ((animalClass, count) in animalCountMap) {
            println(animalClass.simpleName + ": " + count)
        }

        // Вывод общего количества растений на острове
        var totalPlantCount = 0
        for (y in locations[y].indices) {
            for (value in locations) {
                val location = value[y]
                totalPlantCount = (totalPlantCount + location!!.plant).toInt()
            }
        }
        println("Общее количество растений: $totalPlantCount")


        // Вывод информации о прошедших итерациях (днях)
        // Output information about the passed iterations (days)
        println("Прошедшие дни: $iterationCount из 30")
        increaseIterationCount()
        println("\n*******************************************************************************************\n")
    }

    // Метод для увеличения счетчика прошедших итераций (дней)
    // Method to increase the iteration count (number of days)
    fun increaseIterationCount() {
        iterationCount++
    }
}
