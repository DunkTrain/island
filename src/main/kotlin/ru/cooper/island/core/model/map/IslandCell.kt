package ru.cooper.island.core.model.map

import ru.cooper.island.config.SimulationConfig
import ru.cooper.island.core.model.Animal
import ru.cooper.island.core.model.animal.herbivores.Boar
import ru.cooper.island.core.model.animal.herbivores.Buffalo
import ru.cooper.island.core.model.animal.herbivores.Caterpillar
import ru.cooper.island.core.model.animal.herbivores.Deer
import ru.cooper.island.core.model.animal.herbivores.Duck
import ru.cooper.island.core.model.animal.herbivores.Goat
import ru.cooper.island.core.model.animal.herbivores.Horse
import ru.cooper.island.core.model.animal.herbivores.Mouse
import ru.cooper.island.core.model.animal.herbivores.Rabbit
import ru.cooper.island.core.model.animal.herbivores.Sheep
import ru.cooper.island.core.model.animal.predators.Bear
import ru.cooper.island.core.model.animal.predators.Eagle
import ru.cooper.island.core.model.animal.predators.Fox
import ru.cooper.island.core.model.animal.predators.Snake
import ru.cooper.island.core.model.animal.predators.Wolf
import ru.cooper.island.services.simulation.animal.AnimalSimulationTask
import ru.cooper.island.services.simulation.plant.PlantGrowthTask
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.math.min

class IslandCell(
    @JvmField val yCoordinate: Int,
    @JvmField val xCoordinate: Int
) {

    private val _neighboringLocations = mutableListOf<IslandCell>()
    val neighboringLocations: List<IslandCell>
        get() = _neighboringLocations.toList()

    @Volatile
    @JvmField
    var plant: Double = 0.0

    fun addNeighboringLocation(cell: IslandCell) {
        _neighboringLocations.add(cell)
    }

    fun setAnimals(animalClass: Class<out Animal>, animals: MutableSet<Animal>) {
        this.animals[animalClass] = animals
    }

    @JvmField
    val animals: ConcurrentMap<Class<out Animal>, MutableSet<Animal>> = ConcurrentHashMap()

    @JvmField
    val lock: Lock = ReentrantLock(true)

    fun startSimulationTasks(executor: ExecutorService): List<Future<*>> {
        val futures = mutableListOf<Future<*>>()
        lock.withLock {
            animals.forEach { (_, animals) ->
                animals.forEach { animal ->
                    futures.add(executor.submit(AnimalSimulationTask(animal, this)))
                }
            }
            futures.add(executor.submit(PlantGrowthTask(this)))
        }
        return futures
    }

    fun removeAnimal(animal: Animal) {
        lock.withLock {
            animals.getOrDefault(animal.javaClass, mutableSetOf()).remove(animal)
        }
    }

    fun addAnimal(animal: Animal) {
        lock.withLock {
            animals.getOrPut(animal.javaClass) { ConcurrentHashMap.newKeySet() }.add(animal)
        }
    }

    fun hasSpaceFor(animalClass: Class<out Animal>): Boolean {
        val maxCount = SimulationConfig.ANIMAL_PARAMETERS[animalClass]?.get(1) ?: 0.0
        return animals.getOrDefault(animalClass, emptySet()).size < maxCount
    }

    fun growPlants() {
        lock.withLock {
            plant = min(
                plant + SimulationConfig.GROWTH_OF_PLANT,
                SimulationConfig.MAX_AMOUNT_OF_PLANT_ON_ONE_CELL
            )
        }
    }

    override fun toString(): String {
        val animalsInfo = buildString {
            append("Location[$xCoordinate, $yCoordinate]\n")
            append("Animals:\n")
            animals.filterValues { it.isNotEmpty() }.forEach { (clazz, animals) ->
                append("\t${clazz.simpleName}: ${animals.size} ${emojiFor(clazz)}\n")
            }
            append("Plants: ${"%.2f".format(plant)} \uD83C\uDF31\n")
        }

        return """
        [${compactAnimalStats()}] 
        $animalsInfo
        """.trimIndent()
    }

    private fun compactAnimalStats() = buildString {
        append(animalIcon(Bear::class.java, "🐻"))
        append(animalIcon(Snake::class.java, "🐍"))
        append(animalIcon(Boar::class.java, "🐗"))
        append(animalIcon(Buffalo::class.java, "🐃"))
        append(animalIcon(Deer::class.java, "🦌"))
        append(animalIcon(Fox::class.java, "🦊"))
        append(animalIcon(Goat::class.java, "🐐"))
        append(animalIcon(Horse::class.java, "🐎"))
        append(animalIcon(Mouse::class.java, "🐁"))
        append(animalIcon(Rabbit::class.java, "🐇"))
        append(animalIcon(Sheep::class.java, "🐑"))
        append(animalIcon(Wolf::class.java, "🐺"))
        append(animalIcon(Duck::class.java, "🦆"))
        append(animalIcon(Eagle::class.java, "🦅"))
        append(animalIcon(Caterpillar::class.java, "🐛"))
        append("🌱${"%.2f".format(plant)}")
    }

    private fun animalIcon(clazz: Class<out Animal>, emoji: String) =
        "${emoji}${animals.getOrDefault(clazz, emptySet()).size}"

    private fun emojiFor(animalClass: Class<out Animal>): String = when (animalClass) {
        Bear::class.java -> "🐻"
        Snake::class.java -> "🐍"
        Boar::class.java -> "🐗"
        Buffalo::class.java -> "🐃"
        Deer::class.java -> "🦌"
        Fox::class.java -> "🦊"
        Goat::class.java -> "🐐"
        Horse::class.java -> "🐎"
        Mouse::class.java -> "🐁"
        Rabbit::class.java -> "🐇"
        Sheep::class.java -> "🐑"
        Wolf::class.java -> "🐺"
        Duck::class.java -> "🦆"
        Eagle::class.java -> "🦅"
        Caterpillar::class.java -> "🐛"
        else -> ""
    }

    private inline fun <T> Lock.withLock(action: () -> T): T {
        lock()
        try {
            return action()
        } finally {
            unlock()
        }
    }

    val animalsForJava: ConcurrentMap<Class<out Animal>, MutableSet<Animal>>
        @JvmName("getAnimals")
        get() = animals

    val plantForJava: Double
        @JvmName("getPlant")
        get() = plant
}
