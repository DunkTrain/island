package ru.cooper.island.entities.islandMap

import ru.cooper.island.entities.animals.herbivores.*
import ru.cooper.island.entities.animals.predators.*
import ru.cooper.island.entities.organism.Animal
import ru.cooper.island.services.AnimalSimulationTask
import ru.cooper.island.services.PlantGrowthTask
import ru.cooper.island.settings.IslandSettings
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.Volatile
import kotlin.math.min

/**
 * Location - Класс, представляющий отдельную локацию на игровом поле "Остров животных".
 * Каждая локация содержит информацию о своих координатах (позиции), количестве растений, соседних локациях и животных.
 * Локация управляет выполнением симуляции для всех животных и ростом растений, а также предоставляет методы для доступа к своим данным.
 */
class Location(@JvmField val coordinate_Y: Int, @JvmField val coordinate_X: Int) {
    private val threadPool: ExecutorService = Executors.newFixedThreadPool(4)

    @JvmField
    @Volatile
    var plant: Double = 0.0

    @JvmField
    val neighboringLocations: List<Location> = ArrayList()

    @JvmField
    val animals: Map<Class<out Animal>, MutableSet<Animal>> = ConcurrentHashMap()

    @JvmField
    val lock: Lock = ReentrantLock(true)

    fun start() {
        lock.lock()
        try {
            for (animalClass in animals.keys) {
                for (animal in animals[animalClass]!!) {
                    threadPool.submit(AnimalSimulationTask(animal, this))
                }
            }
            threadPool.submit(PlantGrowthTask(this))
        } finally {
            lock.unlock()
        }
    }

    @Throws(InterruptedException::class)
    fun await(milliseconds: Int) {
        threadPool.awaitTermination(milliseconds.toLong(), TimeUnit.MILLISECONDS)
    }

    fun shutdown() {
        threadPool.shutdown()
    }

    fun removeAnimal(animal: Animal) {
        animals[animal.javaClass]!!.remove(animal)
    }

    fun addAnimalToLocation(animal: Animal) {
        if (isThereEnoughSpace(animal.javaClass)) {
            animals[animal.javaClass]!!.add(animal)
        }
    }

    fun isThereEnoughSpace(animalClass: Class<out Animal>): Boolean {
        return animals[animalClass]!!.size < IslandSettings.ANIMAL_PARAMETERS[animalClass]!![1]
    }

    fun plantGrowth() {
        lock.lock()
        try {
            val plantGrowth = IslandSettings.GROWTH_OF_PLANT
            val newPlantValue = this.plant + plantGrowth
            this.plant = min(newPlantValue, IslandSettings.MAX_AMOUNT_OF_PLANT_ON_ONE_CELL)
        } finally {
            lock.unlock()
        }
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("Координаты локации: [").append(coordinate_X).append(", ").append(coordinate_Y).append("]\n")

        // Животные на локации
        sb.append("Животные на локации:\n")
        for (animalClass in animals.keys) {
            val animalCount = animals[animalClass]!!.size
            if (animalCount > 0) {
                val animalName = animalClass.simpleName
                sb.append("\t").append(animalName).append(": ").append(animalCount).append(" ")
                    .append(getAnimalEmoji(animalName)).append("\n")
            }
        }

        // Растения на локации
        sb.append("Растения на локации: ").append(String.format("%.2f", plant)).append(" ").append("\uD83C\uDF31")
            .append("\n\n")

        return ("""
     [🐻${animals[Bear::class.java]!!.size}🐲${animals[Snake::class.java]!!.size}🐗${animals[Boar::class.java]!!.size}🐃${animals[Buffalo::class.java]!!.size}🦌${animals[Deer::class.java]!!.size}🦊${animals[Fox::class.java]!!.size}🐐${animals[Goat::class.java]!!.size}🦄${
            animals.get(
                Horse::class.java
            )!!.size
        }🐁${animals[Mouse::class.java]!!.size}🐇${animals[Rabbit::class.java]!!.size}🐑${animals[Sheep::class.java]!!.size}🐺${animals[Wolf::class.java]!!.size}🦆${animals[Duck::class.java]!!.size}🦅${animals[Eagle::class.java]!!.size}🐛${animals[Caterpillar::class.java]!!.size}🌱${
            String.format(
                "%.2f",
                plant
            )
        }]
     $sb
     """.trimIndent())
    }

    // Вспомогательный метод для получения смайлика по имени животного
    private fun getAnimalEmoji(animalName: String): String {
        return when (animalName) {
            "Bear" -> "\uD83D\uDC3B"
            "Snake" -> "\uD83D\uDC32"
            "Boar" -> "\uD83D\uDC17"
            "Buffalo" -> "\uD83D\uDC03"
            "Deer" -> "\uD83E\uDD8C"
            "Fox" -> "\uD83E\uDD8A"
            "Goat" -> "\uD83D\uDC10"
            "Horse" -> "\uD83E\uDD84"
            "Mouse" -> "\uD83D\uDC01"
            "Rabbit" -> "\uD83D\uDC07"
            "Sheep" -> "\uD83D\uDC11"
            "Wolf" -> "\uD83D\uDC3A"
            "Duck" -> "\uD83E\uDD86"
            "Eagle" -> "\uD83E\uDD85"
            "Caterpillar" -> "\uD83D\uDC1B"
            else -> ""
        }
    }
}