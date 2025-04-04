package ru.cooper.island.entities.organism.interfaces

import ru.cooper.island.entities.islandMap.Location
import ru.cooper.island.entities.organism.Animal
import ru.cooper.island.settings.IslandSettings
import ru.cooper.island.util.Randomizer
import kotlin.math.min

/**
 * Интерфейс Predators предоставляет функциональность для хищников на острове животных.
 * Он содержит метод eat, который определяет, как хищник будет питаться и охотиться на других животных на определенной локации.
 */
interface Predators {
    /**
     * Метод eat определяет поведение хищников при питании.
     * Хищник охотится на других животных на локации и пытается их съесть.
     * При успешной охоте, хищник увеличивает свой вес на вес съеденного животного до определенного максимального значения.
     * Если хищник становится насыщенным (его вес достигает определенного предела) или все животные на локации были съедены, процесс питания прекращается.
     *
     * @param location Локация, на которой находится хищник.
     */
    fun eat(location: Location) {
        location.lock.lock()
        val carnivorous = this as Animal
        var isAte = false
        val startingWeightCarnivorous = carnivorous.weight
        val maxWeightCarnivorous = IslandSettings.ANIMAL_PARAMETERS[carnivorous.aClass]!![0]
        val satiation = IslandSettings.ANIMAL_PARAMETERS[carnivorous.aClass]!![3]
        val differentWeight = maxWeightCarnivorous - startingWeightCarnivorous
        try {
            val victimsMap = IslandSettings.FEEDING_CHANCES[carnivorous.aClass]!!
            if (differentWeight > 0) {
                val victimsMapIterator: Iterator<Map.Entry<Class<out Animal>, Int>> = victimsMap.entries.iterator()
                while (!isAte && victimsMapIterator.hasNext()) {
                    val probabilityPair = victimsMapIterator.next()
                    val classVictim: Class<*> = probabilityPair.key
                    val probability = probabilityPair.value
                    val victims = location.animals[classVictim]
                    val victimsIterator = victims!!.iterator()
                    if (Randomizer.getRandom(probability) && !victims.isEmpty() && victimsIterator.hasNext()) {
                        val victim = victimsIterator.next()
                        carnivorous.weight = min(carnivorous.weight + victim.currentWeight, maxWeightCarnivorous)
                        if (carnivorous.weight >= startingWeightCarnivorous + satiation || carnivorous.weight == maxWeightCarnivorous) {
                            isAte = true
                        }
                        victimsIterator.remove()
                    }
                }
            }
        } finally {
            location.lock.unlock()
        }
    }
}
