package ru.cooper.island.util

import java.util.concurrent.ThreadLocalRandom

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
    }
}
