package ru.cooper.island.core.model.map

class IslandGrid(
    private val gridHeight: Int,
    private val gridWidth: Int
) {

    val height: Int get() = gridHeight
    val width: Int get() = gridWidth

    @JvmField
    val islandCells: Array<Array<IslandCell?>> = Array(gridWidth) { arrayOfNulls(gridHeight) }

    fun getLocation(y: Int, x: Int): IslandCell? = islandCells[x][y]
}
