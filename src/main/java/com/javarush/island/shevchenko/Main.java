package com.javarush.island.shevchenko;

import com.javarush.island.shevchenko.services.WorldInitializer;
import com.javarush.island.shevchenko.services.WorldLifeCycle;
import com.javarush.island.shevchenko.settings.IslandSettings;

import java.util.Scanner;

import static com.javarush.island.shevchenko.settings.IslandSettings.*;

public class Main {
    public static void main(String[] args) {
        printWelcomeMessage();
        Scanner scanner = new Scanner(System.in);
        printEnter(scanner);
        countdown(3);


        WorldInitializer world = new WorldInitializer(IslandSettings.FIELD_TO_SIZE_Y, IslandSettings.FIELD_TO_SIZE_X);
        WorldLifeCycle lifeWorker = new WorldLifeCycle(world);
        lifeWorker.start();
    }


}