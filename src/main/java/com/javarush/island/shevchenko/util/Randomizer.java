package com.javarush.island.shevchenko.util;

import java.util.concurrent.ThreadLocalRandom;


/**
 * Randomizer - Утилитарный класс для генерации случайных значений.
 * Этот класс предоставляет методы для получения случайных чисел в различных форматах
 * и определения случайных событий на основе вероятности.
 */


public class Randomizer {

    private Randomizer() {
        throw new IllegalStateException("Utility class");
    }


    public static int getRandom(int from, int to) {
        return ThreadLocalRandom.current().nextInt(from, to + 1);
    }


    public static double getRandom(double from, double to) {
        return ThreadLocalRandom.current().nextDouble(from, to + 1);
    }


    public static boolean getRandom(int probability) {
        int i = ThreadLocalRandom.current().nextInt(0, 100);
        return i < probability;
    }


    public static boolean getRandom() {
        return ThreadLocalRandom.current().nextBoolean();
    }
}
