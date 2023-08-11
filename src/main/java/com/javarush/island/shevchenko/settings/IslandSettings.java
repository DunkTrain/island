package com.javarush.island.shevchenko.settings;

import com.javarush.island.shevchenko.entities.animals.herbivores.*;
import com.javarush.island.shevchenko.entities.animals.predators.*;
import com.javarush.island.shevchenko.entities.organism.Animal;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

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

public class IslandSettings {

    public static void countdown(int seconds) {
        for (int i = seconds; i > 0; i--) {
            System.out.println("Старт через " + i + "...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println();
    }

    public static void printWelcomeMessage() {
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Добро пожаловать на остров животных!");
        System.out.println("Здесь природа во всей своей красе, и каждый день происходит много интересного!");
        System.out.println("Подготовьтесь к захватывающей жизни и увлекательным приключениям!");
        System.out.println("Размер острова составляет: " + FIELD_TO_SIZE_Y + " x " + FIELD_TO_SIZE_X);
        System.out.println("-------------------------------------------------------------------------");
    }


    public static void printEnter(Scanner scanner) {
        System.out.println("Нажмите Enter, чтобы запустить жизнь на острове...");
        scanner.nextLine();
    }


    public static final double MAX_AMOUNT_OF_PLANT_ON_ONE_CELL = 200;


    public static final double GROWTH_OF_PLANT = 20;


    public static final int FIELD_TO_SIZE_Y = 5;
    public static final int FIELD_TO_SIZE_X = 10;


    public static final int STEP_DURATION = 500;


    public static final int NUMBER_OF_SIMULATION_STEPS = 30;

    private IslandSettings() {
        throw new IllegalStateException("Configuration class");
    }


    public static final Map<Class<? extends Animal>, double[]> ANIMAL_PARAMETERS = new HashMap<>();

    static {
        ANIMAL_PARAMETERS.put(Bear.class, new double[]{500, 5, 2, 80});
        ANIMAL_PARAMETERS.put(Snake.class, new double[]{15, 30, 1, 3});
        ANIMAL_PARAMETERS.put(Boar.class, new double[]{400, 50, 2, 50});
        ANIMAL_PARAMETERS.put(Buffalo.class, new double[]{700, 10, 3, 100});
        ANIMAL_PARAMETERS.put(Deer.class, new double[]{300, 20, 4, 50});

        ANIMAL_PARAMETERS.put(Fox.class, new double[]{8, 30, 2, 2});
        ANIMAL_PARAMETERS.put(Goat.class, new double[]{60, 140, 3, 10});
        ANIMAL_PARAMETERS.put(Horse.class, new double[]{400, 20, 4, 60});
        ANIMAL_PARAMETERS.put(Mouse.class, new double[]{0.05, 500, 1, 0.01});
        ANIMAL_PARAMETERS.put(Rabbit.class, new double[]{2, 150, 2, 0.45});
        ANIMAL_PARAMETERS.put(Sheep.class, new double[]{70, 140, 3, 15});
        ANIMAL_PARAMETERS.put(Wolf.class, new double[]{50, 30, 3, 8});
        ANIMAL_PARAMETERS.put(Duck.class, new double[]{1, 200, 4, 0.15});
        ANIMAL_PARAMETERS.put(Eagle.class, new double[]{6, 20, 3, 1});
        ANIMAL_PARAMETERS.put(Caterpillar.class, new double[]{0.01, 1000, 0, 0.001});
    }


    public static final Set<Class<? extends Animal>> ANIMAL_CLASSES = Set.of(Bear.class, Snake.class, Boar.class, Buffalo.class, Deer.class,
            Fox.class, Goat.class, Horse.class, Mouse.class, Rabbit.class, Sheep.class, Wolf.class, Caterpillar.class,
            Duck.class, Eagle.class);


    public static final Map<Class<? extends Animal>, Map<Class<? extends Animal>, Integer>> FEEDING_CHANCES = new HashMap<>();

    private static final Map<Class<? extends Animal>, Integer> WOLF_FOOD_REQUIREMENT = new HashMap<>();
    private static final Map<Class<? extends Animal>, Integer> SNAKE_FOOD_REQUIREMENT = new HashMap<>();
    private static final Map<Class<? extends Animal>, Integer> FOX_FOOD_REQUIREMENT = new HashMap<>();
    private static final Map<Class<? extends Animal>, Integer> BEAR_FOOD_REQUIREMENT = new HashMap<>();
    private static final Map<Class<? extends Animal>, Integer> EAGLE_FOOD_REQUIREMENT = new HashMap<>();
    private static final Map<Class<? extends Animal>, Integer> MOUSE_FOOD_REQUIREMENT = new HashMap<>();
    private static final Map<Class<? extends Animal>, Integer> BOAR_FOOD_REQUIREMENT = new HashMap<>();
    private static final Map<Class<? extends Animal>, Integer> DUCK_FOOD_REQUIREMENT = new HashMap<>();


    static {
        WOLF_FOOD_REQUIREMENT.put(Horse.class, 10);
        WOLF_FOOD_REQUIREMENT.put(Deer.class, 15);
        WOLF_FOOD_REQUIREMENT.put(Rabbit.class, 60);
        WOLF_FOOD_REQUIREMENT.put(Mouse.class, 80);
        WOLF_FOOD_REQUIREMENT.put(Goat.class, 60);
        WOLF_FOOD_REQUIREMENT.put(Sheep.class, 70);
        WOLF_FOOD_REQUIREMENT.put(Boar.class, 15);
        WOLF_FOOD_REQUIREMENT.put(Buffalo.class, 10);
        WOLF_FOOD_REQUIREMENT.put(Duck.class, 40);

        SNAKE_FOOD_REQUIREMENT.put(Fox.class, 15);
        SNAKE_FOOD_REQUIREMENT.put(Rabbit.class, 20);
        SNAKE_FOOD_REQUIREMENT.put(Mouse.class, 40);
        SNAKE_FOOD_REQUIREMENT.put(Duck.class, 10);

        FOX_FOOD_REQUIREMENT.put(Rabbit.class, 70);
        FOX_FOOD_REQUIREMENT.put(Mouse.class, 90);
        FOX_FOOD_REQUIREMENT.put(Duck.class, 60);
        FOX_FOOD_REQUIREMENT.put(Caterpillar.class, 40);

        BEAR_FOOD_REQUIREMENT.put(Snake.class, 80);
        BEAR_FOOD_REQUIREMENT.put(Horse.class, 40);
        BEAR_FOOD_REQUIREMENT.put(Deer.class, 80);
        BEAR_FOOD_REQUIREMENT.put(Rabbit.class, 80);
        BEAR_FOOD_REQUIREMENT.put(Mouse.class, 90);
        BEAR_FOOD_REQUIREMENT.put(Goat.class, 70);
        BEAR_FOOD_REQUIREMENT.put(Sheep.class, 70);
        BEAR_FOOD_REQUIREMENT.put(Boar.class, 50);
        BEAR_FOOD_REQUIREMENT.put(Buffalo.class, 20);
        BEAR_FOOD_REQUIREMENT.put(Duck.class, 10);

        EAGLE_FOOD_REQUIREMENT.put(Fox.class, 10);
        EAGLE_FOOD_REQUIREMENT.put(Rabbit.class, 90);
        EAGLE_FOOD_REQUIREMENT.put(Mouse.class, 90);
        EAGLE_FOOD_REQUIREMENT.put(Duck.class, 80);

        MOUSE_FOOD_REQUIREMENT.put(Caterpillar.class, 90);

        BOAR_FOOD_REQUIREMENT.put(Mouse.class, 50);
        BOAR_FOOD_REQUIREMENT.put(Caterpillar.class, 90);

        DUCK_FOOD_REQUIREMENT.put(Caterpillar.class, 90);

        FEEDING_CHANCES.put(Wolf.class, WOLF_FOOD_REQUIREMENT);
        FEEDING_CHANCES.put(Snake.class, SNAKE_FOOD_REQUIREMENT);
        FEEDING_CHANCES.put(Fox.class, FOX_FOOD_REQUIREMENT);
        FEEDING_CHANCES.put(Bear.class, BEAR_FOOD_REQUIREMENT);
        FEEDING_CHANCES.put(Eagle.class, EAGLE_FOOD_REQUIREMENT);
        FEEDING_CHANCES.put(Mouse.class, MOUSE_FOOD_REQUIREMENT);
        FEEDING_CHANCES.put(Boar.class, BOAR_FOOD_REQUIREMENT);
        FEEDING_CHANCES.put(Duck.class, DUCK_FOOD_REQUIREMENT);
    }
}