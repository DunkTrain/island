package com.javarush.island.shevchenko.services;

import com.javarush.island.shevchenko.entities.islandMap.Location;
import com.javarush.island.shevchenko.settings.IslandSettings;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Класс WorldLifeCycle представляет собой жизненный цикл симуляции на острове животных.
 * Он отвечает за запуск, остановку и управление шагами симуляции.
 */


public class WorldLifeCycle {

    private final WorldInitializer world;
    private final AtomicInteger simulationStepNumber = new AtomicInteger(0);
    private final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

    public WorldLifeCycle(WorldInitializer worldGenerator) {
        this.world = worldGenerator;
    }

    private void startingTheSimulation() {
        Location[][] locations = world.getIslandField().getLocations();
        simulationStepNumber.incrementAndGet();
        world.getIslandField().output();
        for (int y = 0; y < locations[y].length; y++) {
            for (Location[] location : locations) {
                location[y].start();
            }
        }
    }


    private final Runnable lifeTask = new Runnable() {
        @Override
        public void run() {
            Location[][] locations = world.getIslandField().getLocations();
            startingTheSimulation();
            int numberOfSimulationSteps = IslandSettings.NUMBER_OF_SIMULATION_STEPS;
            if (simulationStepNumber.get() >= numberOfSimulationSteps) {
                service.shutdown();
                for (int y = 0; y < locations[y].length; y++) {
                    for (Location[] location : locations) {
                        location[y].shutdown();
                    }
                }
                for (int y = 0; y < locations[y].length; y++) {
                    for (Location[] location : locations) {
                        try {
                            location[y].await(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                world.getIslandField().output();
            }
        }
    };


    public void start() {
        int stepDuration = IslandSettings.STEP_DURATION;
        service.scheduleAtFixedRate(lifeTask, 1, stepDuration, TimeUnit.MILLISECONDS);
    }
}
