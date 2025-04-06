package ru.cooper.island.config

import ru.cooper.island.config.SimulationConfig.Companion.ANIMAL_CLASSES
import ru.cooper.island.config.SimulationConfig.Companion.ANIMAL_PARAMETERS
import ru.cooper.island.config.SimulationConfig.Companion.FEEDING_CHANCES
import ru.cooper.island.config.SimulationConfig.Companion.FIELD_TO_SIZE_X
import ru.cooper.island.config.SimulationConfig.Companion.FIELD_TO_SIZE_Y
import ru.cooper.island.config.SimulationConfig.Companion.GROWTH_OF_PLANT
import ru.cooper.island.config.SimulationConfig.Companion.MAX_AMOUNT_OF_PLANT_ON_ONE_CELL
import ru.cooper.island.config.SimulationConfig.Companion.NUMBER_OF_SIMULATION_STEPS
import ru.cooper.island.config.SimulationConfig.Companion.STEP_DURATION
import ru.cooper.island.core.model.Animal
import ru.cooper.island.core.model.animal.herbivores.*
import ru.cooper.island.core.model.animal.predators.*
import java.util.*

/**
 * Конфигурация симуляции "Остров животных".
 * Содержит все настройки и параметры для работы симуляции.
 *
 * Configuration for "Animal Island" simulation.
 * Contains all settings and parameters for the simulation.
 *
 * @property MAX_AMOUNT_OF_PLANT_ON_ONE_CELL Максимальное количество растений на клетке
 * @property GROWTH_OF_PLANT Скорость роста растений
 * @property FIELD_TO_SIZE_Y Высота игрового поля (в клетках)
 * @property FIELD_TO_SIZE_X Ширина игрового поля (в клетках)
 * @property STEP_DURATION Длительность одного шага симуляции (мс)
 * @property NUMBER_OF_SIMULATION_STEPS Общее количество шагов симуляции
 * @property ANIMAL_PARAMETERS Параметры для каждого вида животных
 * @property ANIMAL_CLASSES Все доступные классы животных
 * @property FEEDING_CHANCES Вероятности поедания между видами
 */
class SimulationConfig private constructor() {

    companion object {
        // Region: Simulation UI Messages
        // ===================================================================

        /**
         * Выводит обратный отсчет перед стартом симуляции
         * Displays countdown before simulation start
         *
         * @param seconds Количество секунд отсчета / Countdown seconds
         */
        @JvmStatic
        fun countdown(seconds: Int) {
            for (i in seconds downTo 1) {
                println("Старт через $i... / Starting in $i...")
                Thread.sleep(1000)
            }
            println()
        }

        /**
         * Выводит приветственное сообщение с параметрами острова
         * Displays welcome message with island parameters
         */
        @JvmStatic
        fun printWelcomeMessage() {
            println("""
                |-------------------------------------------------------------------------
                |Добро пожаловать на остров животных! / Welcome to Animal Island!
                |Здесь природа во всей своей красе! / Nature in all its glory!
                |Размер острова: ${FIELD_TO_SIZE_Y}x${FIELD_TO_SIZE_X} / Island size: ${FIELD_TO_SIZE_Y}x${FIELD_TO_SIZE_X}
                |-------------------------------------------------------------------------
            """.trimMargin())
        }

        /**
         * Ожидает нажатия Enter для старта симуляции
         * Waits for Enter press to start simulation
         */
        @JvmStatic
        fun printEnter(scanner: Scanner) {
            println("Нажмите Enter, чтобы запустить симуляцию... / Press Enter to start simulation...")
            scanner.nextLine()
        }

        // Region: Plant Configuration
        // ===================================================================

        /**
         * Максимальное количество растений на одной клетке
         * Max plants amount per cell
         */
        const val MAX_AMOUNT_OF_PLANT_ON_ONE_CELL: Double = 200.0

        /**
         * Скорость роста растений за шаг симуляции
         * Plants growth rate per simulation step
         */
        const val GROWTH_OF_PLANT: Double = 20.0

        // Region: Field Configuration
        // ===================================================================

        /**
         * Высота игрового поля (количество клеток по вертикали)
         * Field height (vertical cells count)
         */
        const val FIELD_TO_SIZE_Y: Int = 5

        /**
         * Ширина игрового поля (количество клеток по горизонтали)
         * Field width (horizontal cells count)
         */
        const val FIELD_TO_SIZE_X: Int = 10

        // Region: Simulation Timing
        // ===================================================================

        /**
         * Длительность одного шага симуляции в миллисекундах
         * Simulation step duration in milliseconds
         */
        const val STEP_DURATION: Int = 500

        /**
         * Общее количество шагов симуляции
         * Total simulation steps count
         */
        const val NUMBER_OF_SIMULATION_STEPS: Int = 30

        /**
         * Параметры животных в формате:
         * [максимальный вес, максимальное количество на клетке, максимальное перемещение, скорость насыщения]
         *
         * Animal parameters format:
         * [max weight, max per cell, max movement, saturation speed]
         */
        val ANIMAL_PARAMETERS: Map<Class<out Animal>, DoubleArray> = mapOf(
            // Хищники / Predators
            Bear::class.java   to doubleArrayOf(500.0, 5.0, 2.0, 80.0),
            Wolf::class.java   to doubleArrayOf(50.0, 30.0, 3.0, 8.0),
            Fox::class.java    to doubleArrayOf(8.0, 30.0, 2.0, 2.0),
            Eagle::class.java  to doubleArrayOf(6.0, 20.0, 3.0, 1.0),
            Snake::class.java  to doubleArrayOf(15.0, 30.0, 1.0, 3.0),

            // Травоядные / Herbivores
            Buffalo::class.java to doubleArrayOf(700.0, 10.0, 3.0, 100.0),
            Horse::class.java   to doubleArrayOf(400.0, 20.0, 4.0, 60.0),
            Deer::class.java    to doubleArrayOf(300.0, 20.0, 4.0, 50.0),
            Boar::class.java    to doubleArrayOf(400.0, 50.0, 2.0, 50.0),
            Goat::class.java    to doubleArrayOf(60.0, 140.0, 3.0, 10.0),
            Sheep::class.java   to doubleArrayOf(70.0, 140.0, 3.0, 15.0),

            // Мелкие животные / Small animals
            Rabbit::class.java  to doubleArrayOf(2.0, 150.0, 2.0, 0.45),
            Duck::class.java    to doubleArrayOf(1.0, 200.0, 4.0, 0.15),
            Mouse::class.java   to doubleArrayOf(0.05, 500.0, 1.0, 0.01),
            Caterpillar::class.java to doubleArrayOf(0.01, 1000.0, 0.0, 0.001)
        )

        /**
         * Все доступные классы животных в симуляции
         * All available animal classes in simulation
         */
        val ANIMAL_CLASSES: Set<Class<out Animal>> = setOf(
            Bear::class.java,
            Wolf::class.java,
            Fox::class.java,
            Eagle::class.java,
            Snake::class.java,
            Buffalo::class.java,
            Horse::class.java,
            Deer::class.java,
            Boar::class.java,
            Goat::class.java,
            Sheep::class.java,
            Rabbit::class.java,
            Duck::class.java,
            Mouse::class.java,
            Caterpillar::class.java
        )

        /**
         * Вероятности поедания между видами (в процентах)
         * Feeding probabilities between species (in percent)
         */
        val FEEDING_CHANCES: Map<Class<out Animal>, Map<Class<out Animal>, Int>> = mapOf(
            // Волк / Wolf
            Wolf::class.java to mapOf(
                Horse::class.java to 10,
                Deer::class.java to 15,
                Rabbit::class.java to 60,
                Mouse::class.java to 80,
                Goat::class.java to 60,
                Sheep::class.java to 70,
                Boar::class.java to 15,
                Buffalo::class.java to 10,
                Duck::class.java to 40
            ),

            // Медведь / Bear
            Bear::class.java to mapOf(
                Snake::class.java to 80,
                Horse::class.java to 40,
                Deer::class.java to 80,
                Rabbit::class.java to 80,
                Mouse::class.java to 90,
                Goat::class.java to 70,
                Sheep::class.java to 70,
                Boar::class.java to 50,
                Buffalo::class.java to 20,
                Duck::class.java to 10
            ),

            // Лиса / Fox
            Fox::class.java to mapOf(
                Rabbit::class.java to 70,
                Mouse::class.java to 90,
                Duck::class.java to 60,
                Caterpillar::class.java to 40
            ),

            // Орел / Eagle
            Eagle::class.java to mapOf(
                Fox::class.java to 10,
                Rabbit::class.java to 90,
                Mouse::class.java to 90,
                Duck::class.java to 80
            ),

            // Змея / Snake
            Snake::class.java to mapOf(
                Fox::class.java to 15,
                Rabbit::class.java to 20,
                Mouse::class.java to 40,
                Duck::class.java to 10
            ),

            // Мышь / Mouse
            Mouse::class.java to mapOf(
                Caterpillar::class.java to 90
            ),

            // Кабан / Boar
            Boar::class.java to mapOf(
                Mouse::class.java to 50,
                Caterpillar::class.java to 90
            ),

            // Утка / Duck
            Duck::class.java to mapOf(
                Caterpillar::class.java to 90
            )
        )
    }
}
