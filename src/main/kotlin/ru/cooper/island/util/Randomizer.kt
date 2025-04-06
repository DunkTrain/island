package ru.cooper.island.util

import java.util.concurrent.ThreadLocalRandom

/**
 * Randomizer - Утилитарный класс для генерации случайных значений.
 * Этот класс предоставляет методы для получения случайных чисел в различных форматах
 * и определения случайных событий на основе вероятности.
 */
class Randomizer private constructor() {
    init {
        throw IllegalStateException("Utility class")
    }

    companion object {
        fun getRandom(from: Int, to: Int): Int {
            return ThreadLocalRandom.current().nextInt(from, to + 1)
        }


        fun getRandom(from: Double, to: Double): Double {
            return ThreadLocalRandom.current().nextDouble(from, to + 1)
        }


        fun getRandom(probability: Int): Boolean {
            val i = ThreadLocalRandom.current().nextInt(0, 100)
            return i < probability
        }


        val random: Boolean
            get() = ThreadLocalRandom.current().nextBoolean()
    }
}
