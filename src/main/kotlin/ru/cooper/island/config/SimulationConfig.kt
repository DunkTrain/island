package ru.cooper.island.config

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

/**
 * Конфигурация симуляции "Остров животных".
 *
 * Хранит основные параметры поля, животных и другие настройки,
 * необходимые для корректной работы симуляции.
 */
object SimulationConfig {

    // ----------------------------------------------------------------
    // Параметры растений и поля
    // ----------------------------------------------------------------

    /**
     * Максимальное количество растений на одной клетке.
     */
    const val MAX_AMOUNT_OF_PLANT_ON_ONE_CELL: Double = 200.0

    /**
     * Скорость роста растений за один шаг симуляции.
     */
    const val GROWTH_OF_PLANT: Double = 20.0

    /**
     * Высота игрового поля (количество клеток по вертикали).
     */
    const val FIELD_TO_SIZE_Y: Int = 5

    /**
     * Ширина игрового поля (количество клеток по горизонтали).
     */
    const val FIELD_TO_SIZE_X: Int = 10

    // ----------------------------------------------------------------
    // Параметры времени симуляции
    // ----------------------------------------------------------------

    /**
     * Длительность одного шага симуляции в миллисекундах.
     */
    const val STEP_DURATION: Int = 500

    /**
     * Общее количество шагов симуляции.
     */
    const val NUMBER_OF_SIMULATION_STEPS: Int = 30

    // ----------------------------------------------------------------
    // Параметры животных
    // ----------------------------------------------------------------

    /**
     * Параметры животных в формате:
     * [максимальный вес, максимальное количество на клетке, максимальное количество шагов передвижения, скорость насыщения].
     *
     * Пример: для медведя (Bear) doubleArrayOf(500.0, 5.0, 2.0, 80.0)
     */
    @JvmField
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
        Rabbit::class.java   to doubleArrayOf(2.0, 150.0, 2.0, 0.45),
        Duck::class.java     to doubleArrayOf(1.0, 200.0, 4.0, 0.15),
        Mouse::class.java    to doubleArrayOf(0.05, 500.0, 1.0, 0.01),
        Caterpillar::class.java to doubleArrayOf(0.01, 1000.0, 0.0, 0.001)
    )

    /**
     * Набор всех доступных классов животных в симуляции.
     */
    @JvmField
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
     * Вероятности поедания (в %) между хищниками (или всеядными) и потенциальной добычей.
     */
    @JvmField
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

        // Орёл / Eagle
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
