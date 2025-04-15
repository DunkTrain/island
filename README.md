# Island Life Simulation

A modern Java + Kotlin desktop simulation using **JavaFX** to visualize the life of animals and plants on a virtual island.

## Features

- **Realistic Ecosystem**: Animals, plants, reproduction, feeding, movement, and more.
- **Live Visualization**: Interactive JavaFX interface instead of console output.
- **Configurable Parameters**: Easily tweak simulation step time, island size, plant growth, and animal behavior.
- **Modern UI**: Responsive design with animations and emoji support for visual clarity.

## Technologies Used

- Java 21 + Kotlin
- JavaFX (FXML + CSS)
- Maven build system

## How to Run

1. Make sure Java 21 is installed and set as default.
2. Build and run the project:
   ```bash
   mvn clean javafx:run
   ```
3. The JavaFX UI will launch. Press `Start` to begin the simulation.

## Project Structure

- `ru.cooper.island.ui` – JavaFX controller and UI components
- `ru.cooper.island.core.model` – Animal and plant models
- `ru.cooper.island.services` – Lifecycle logic and initialization
- `ru.cooper.island.config` – Simulation configuration
