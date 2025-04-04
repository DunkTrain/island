package ru.cooper.island.settings

import ru.cooper.island.entities.animals.herbivores.*
import ru.cooper.island.entities.animals.predators.*
import ru.cooper.island.entities.organism.Animal
import java.util.*

/**
 * IslandSettings - Класс, содержащий настройки и параметры для симуляции "Остров животных".
 * В этом классе определены следующие настройки:
 * - Приветственное сообщение для острова животных и растений.
 * - Максимальное количество растений на одной клетке и скорость их роста.
 * - Размеры игрового поля по вертикали и горизонтали.
 * - Продолжительность одного шага симуляции и общее количество шагов для выполнения симуляции.
 * - Основные параметры для каждого вида животных.
 * - Коллекция с доступными видами животных в симуляции.
 * - Коллекция с информацией о вероятности быть съеденным для каждого вида животных.
 * Все поля в этом классе объявлены статическими и доступны из других классов без необходимости создания объекта.
 * Класс предоставляет методы для вывода приветственного сообщения и сообщения для запуска симуляции.
 */
class IslandSettings private constructor() {

    init {
        throw IllegalStateException("Configuration class")
    }

    companion object {
        @JvmStatic
        fun countdown(seconds: Int) {
            for (i in seconds downTo 1) {
                println("Старт через $i...")
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
            println()
        }

        @JvmStatic
        fun printWelcomeMessage() {
            println("-------------------------------------------------------------------------")
            println("Добро пожаловать на остров животных!")
            println("Здесь природа во всей своей красе, и каждый день происходит много интересного!")
            println("Подготовьтесь к захватывающей жизни и увлекательным приключениям!")
            println("Размер острова составляет: " + FIELD_TO_SIZE_Y + " x " + FIELD_TO_SIZE_X)
            println("-------------------------------------------------------------------------")
        }


        @JvmStatic
        fun printEnter(scanner: Scanner) {
            println("Нажмите Enter, чтобы запустить жизнь на острове...")
            scanner.nextLine()
        }


        const val MAX_AMOUNT_OF_PLANT_ON_ONE_CELL: Double = 200.0


        const val GROWTH_OF_PLANT: Double = 20.0


        const val FIELD_TO_SIZE_Y: Int = 5
        const val FIELD_TO_SIZE_X: Int = 10


        const val STEP_DURATION: Int = 500


        const val NUMBER_OF_SIMULATION_STEPS: Int = 30

        val ANIMAL_PARAMETERS: MutableMap<Class<out Animal?>, DoubleArray> = HashMap()

        init {
            ANIMAL_PARAMETERS[Bear::class.java] = doubleArrayOf(500.0, 5.0, 2.0, 80.0)
            ANIMAL_PARAMETERS[Snake::class.java] = doubleArrayOf(15.0, 30.0, 1.0, 3.0)
            ANIMAL_PARAMETERS[Boar::class.java] = doubleArrayOf(400.0, 50.0, 2.0, 50.0)
            ANIMAL_PARAMETERS[Buffalo::class.java] =
                doubleArrayOf(700.0, 10.0, 3.0, 100.0)
            ANIMAL_PARAMETERS[Deer::class.java] = doubleArrayOf(300.0, 20.0, 4.0, 50.0)

            ANIMAL_PARAMETERS[Fox::class.java] = doubleArrayOf(8.0, 30.0, 2.0, 2.0)
            ANIMAL_PARAMETERS[Goat::class.java] = doubleArrayOf(60.0, 140.0, 3.0, 10.0)
            ANIMAL_PARAMETERS[Horse::class.java] = doubleArrayOf(400.0, 20.0, 4.0, 60.0)
            ANIMAL_PARAMETERS[Mouse::class.java] = doubleArrayOf(0.05, 500.0, 1.0, 0.01)
            ANIMAL_PARAMETERS[Rabbit::class.java] = doubleArrayOf(2.0, 150.0, 2.0, 0.45)
            ANIMAL_PARAMETERS[Sheep::class.java] = doubleArrayOf(70.0, 140.0, 3.0, 15.0)
            ANIMAL_PARAMETERS[Wolf::class.java] = doubleArrayOf(50.0, 30.0, 3.0, 8.0)
            ANIMAL_PARAMETERS[Duck::class.java] = doubleArrayOf(1.0, 200.0, 4.0, 0.15)
            ANIMAL_PARAMETERS[Eagle::class.java] = doubleArrayOf(6.0, 20.0, 3.0, 1.0)
            ANIMAL_PARAMETERS[Caterpillar::class.java] =
                doubleArrayOf(0.01, 1000.0, 0.0, 0.001)
        }


        val ANIMAL_CLASSES: Set<Class<out Animal?>> = java.util.Set.of(
            Bear::class.java,
            Snake::class.java,
            Boar::class.java,
            Buffalo::class.java,
            Deer::class.java,
            Fox::class.java,
            Goat::class.java,
            Horse::class.java,
            Mouse::class.java,
            Rabbit::class.java,
            Sheep::class.java,
            Wolf::class.java,
            Caterpillar::class.java,
            Duck::class.java,
            Eagle::class.java
        )

        val FEEDING_CHANCES: MutableMap<Class<out Animal?>, Map<Class<out Animal?>, Int>> = HashMap()

        private val WOLF_FOOD_REQUIREMENT: MutableMap<Class<out Animal?>, Int> = HashMap()
        private val SNAKE_FOOD_REQUIREMENT: MutableMap<Class<out Animal?>, Int> = HashMap()
        private val FOX_FOOD_REQUIREMENT: MutableMap<Class<out Animal?>, Int> = HashMap()
        private val BEAR_FOOD_REQUIREMENT: MutableMap<Class<out Animal?>, Int> = HashMap()
        private val EAGLE_FOOD_REQUIREMENT: MutableMap<Class<out Animal?>, Int> = HashMap()
        private val MOUSE_FOOD_REQUIREMENT: MutableMap<Class<out Animal?>, Int> = HashMap()
        private val BOAR_FOOD_REQUIREMENT: MutableMap<Class<out Animal?>, Int> = HashMap()
        private val DUCK_FOOD_REQUIREMENT: MutableMap<Class<out Animal?>, Int> = HashMap()


        init {
            WOLF_FOOD_REQUIREMENT[Horse::class.java] = 10
            WOLF_FOOD_REQUIREMENT[Deer::class.java] = 15
            WOLF_FOOD_REQUIREMENT[Rabbit::class.java] = 60
            WOLF_FOOD_REQUIREMENT[Mouse::class.java] = 80
            WOLF_FOOD_REQUIREMENT[Goat::class.java] = 60
            WOLF_FOOD_REQUIREMENT[Sheep::class.java] = 70
            WOLF_FOOD_REQUIREMENT[Boar::class.java] = 15
            WOLF_FOOD_REQUIREMENT[Buffalo::class.java] = 10
            WOLF_FOOD_REQUIREMENT[Duck::class.java] = 40

            SNAKE_FOOD_REQUIREMENT[Fox::class.java] = 15
            SNAKE_FOOD_REQUIREMENT[Rabbit::class.java] = 20
            SNAKE_FOOD_REQUIREMENT[Mouse::class.java] = 40
            SNAKE_FOOD_REQUIREMENT[Duck::class.java] = 10

            FOX_FOOD_REQUIREMENT[Rabbit::class.java] = 70
            FOX_FOOD_REQUIREMENT[Mouse::class.java] = 90
            FOX_FOOD_REQUIREMENT[Duck::class.java] = 60
            FOX_FOOD_REQUIREMENT[Caterpillar::class.java] = 40

            BEAR_FOOD_REQUIREMENT[Snake::class.java] = 80
            BEAR_FOOD_REQUIREMENT[Horse::class.java] = 40
            BEAR_FOOD_REQUIREMENT[Deer::class.java] = 80
            BEAR_FOOD_REQUIREMENT[Rabbit::class.java] = 80
            BEAR_FOOD_REQUIREMENT[Mouse::class.java] = 90
            BEAR_FOOD_REQUIREMENT[Goat::class.java] = 70
            BEAR_FOOD_REQUIREMENT[Sheep::class.java] = 70
            BEAR_FOOD_REQUIREMENT[Boar::class.java] = 50
            BEAR_FOOD_REQUIREMENT[Buffalo::class.java] = 20
            BEAR_FOOD_REQUIREMENT[Duck::class.java] = 10

            EAGLE_FOOD_REQUIREMENT[Fox::class.java] = 10
            EAGLE_FOOD_REQUIREMENT[Rabbit::class.java] = 90
            EAGLE_FOOD_REQUIREMENT[Mouse::class.java] = 90
            EAGLE_FOOD_REQUIREMENT[Duck::class.java] = 80

            MOUSE_FOOD_REQUIREMENT[Caterpillar::class.java] = 90

            BOAR_FOOD_REQUIREMENT[Mouse::class.java] = 50
            BOAR_FOOD_REQUIREMENT[Caterpillar::class.java] = 90

            DUCK_FOOD_REQUIREMENT[Caterpillar::class.java] = 90

            FEEDING_CHANCES[Wolf::class.java] = WOLF_FOOD_REQUIREMENT
            FEEDING_CHANCES[Snake::class.java] =
                SNAKE_FOOD_REQUIREMENT
            FEEDING_CHANCES[Fox::class.java] = FOX_FOOD_REQUIREMENT
            FEEDING_CHANCES[Bear::class.java] = BEAR_FOOD_REQUIREMENT
            FEEDING_CHANCES[Eagle::class.java] =
                EAGLE_FOOD_REQUIREMENT
            FEEDING_CHANCES[Mouse::class.java] =
                MOUSE_FOOD_REQUIREMENT
            FEEDING_CHANCES[Boar::class.java] = BOAR_FOOD_REQUIREMENT
            FEEDING_CHANCES[Duck::class.java] = DUCK_FOOD_REQUIREMENT
        }
    }
}
