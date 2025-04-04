package ru.cooper.island.entities.organism.interfaces

import ru.cooper.island.entities.islandMap.Location
import ru.cooper.island.entities.organism.Animal
import ru.cooper.island.settings.IslandSettings
import ru.cooper.island.util.Randomizer
import kotlin.math.max
import kotlin.math.min

/**
 * Интерфейс Omnivore предоставляет функциональность для всеядных существ на острове животных.
 * Он содержит метод eat, который определяет, как всеядное существо будет питаться на определенной локации.
 */
interface Omnivore {
    /**
     * Метод eat определяет поведение всеядных существ при питании.
     * Всеядное существо охотится на других животных на локации и пытается их съесть.
     * При успешной охоте, всеядное существо увеличивает свой вес на вес съеденного животного до определенного максимального значения.
     * Если всеядное существо становится насыщенным (его вес достигает определенного предела) или все животные на локации были съедены, процесс питания прекращается.
     * После этого всеядное существо может попытаться съесть растения на локации.
     * Если на локации достаточно растений, всеядное существо увеличивает свой вес на съеденное растение.
     * Если растений недостаточно, всеядное существо увеличивает свой вес только на доступное количество растений.
     *
     * @param location Локация, на которой находится всеядное существо.
     */
    fun eat(location: Location) {
        location.lock.lock()
        val omnivores = this as Animal
        var isAte = false
        val startingWeightOmnivores = omnivores.weight
        val maxWeightOmnivores = IslandSettings.ANIMAL_PARAMETERS[omnivores.aClass]!![0]
        var satiation = IslandSettings.ANIMAL_PARAMETERS[omnivores.aClass]!![3]
        val differentWeight = maxWeightOmnivores - startingWeightOmnivores
        try {
            val victimsMap = IslandSettings.FEEDING_CHANCES[omnivores.aClass]!!
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
                        omnivores.weight = min(omnivores.weight + victim.currentWeight, maxWeightOmnivores)
                        if (omnivores.weight >= startingWeightOmnivores + satiation || omnivores.weight == maxWeightOmnivores) {
                            isAte = true
                        }
                        victimsIterator.remove()
                    }
                }
            }
            satiation = omnivores.weight - startingWeightOmnivores
            if (location.plant > satiation) {
                location.plant = max(location.plant - satiation, 0.0)
                omnivores.weight = min(
                    omnivores.currentWeight + satiation,
                    IslandSettings.ANIMAL_PARAMETERS[omnivores.aClass]!![0]
                )
            } else {
                omnivores.weight = omnivores.weight + location.plant
                location.plant = 0.0
            }
        } finally {
            location.lock.unlock()
        }
    }
}
