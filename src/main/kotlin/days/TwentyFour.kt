package days

import Util.readFileToStringList

class TwentyFour {

    private val examplefileLocation = "src/main/resources/inputs/examples/24.txt"
    private val fileLocation = "src/main/resources/inputs/24.txt"
    private var input = readFileToStringList(fileLocation)

    fun executeA(): Long {
        return doPartA().values.count { !it.isWhite }.toLong()
    }

    fun doPartA(): MutableMap<Pair<Int, Int>, HexTile> {
        val locationToHex = mutableMapOf<Pair<Int, Int>, HexTile>()
        locationToHex[Pair(0, 0)] = HexTile(0, 0)
        input.forEach { line ->
            var currentLocation = Pair(0, 0)
            var idx = 0
            while (idx < line.length) {
                val nextDirection = when (line.substring(idx, idx + 1)) {
                    "e" -> Direction.EAST
                    "w" -> Direction.WEST
                    else -> when (line.substring(idx, idx + 2)) {
                        "se" -> Direction.SOUTHEAST
                        "sw" -> Direction.SOUTHWEST
                        "ne" -> Direction.NORTHEAST
                        "nw" -> Direction.NORTHWEST
                        else -> throw Exception("bad input $line")
                    }
                }
                idx += 1
                if (nextDirection.toString().length > 4) idx += 1
                currentLocation = moveNextDirection(locationToHex, nextDirection, currentLocation)
            }
            // Flip the last one
            locationToHex[currentLocation]!!.flip()
        }
        return locationToHex
    }

    fun moveNextDirection(locationToHex: MutableMap<Pair<Int, Int>, HexTile>,
                          direction: Direction, curLocation: Pair<Int, Int>) =
        locationToHex[curLocation]!!.neighbor(direction).also { nextLocation ->
            locationToHex.putIfAbsent(nextLocation, HexTile(nextLocation.first, nextLocation.second))
        }

    fun executeB(): Long {
        return doPartB(doPartA()).values.count { !it.isWhite }.toLong()
    }

    fun doPartB(locationToHex: MutableMap<Pair<Int, Int>, HexTile>): MutableMap<Pair<Int, Int>, HexTile> {
        val currentMapping = locationToHex.toMutableMap()
        for (i in 0 until 100) {
            println("Num black tiles at iter $i ${currentMapping.values.count { !it.isWhite }}")
            runIteration(currentMapping)
        }
        return currentMapping
    }

    // Updating the locationToHex in place
    fun runIteration(locationToHex: MutableMap<Pair<Int, Int>, HexTile>) {
        val tilesToFlip = mutableSetOf<Pair<Int, Int>>()
        locationToHex.values.forEach {
            if (shouldFlip(it, locationToHex)) {
                tilesToFlip.add(it.location)
            }
            // I also need to check each of their neighbors that are not currently in the map
            // And if that neighbor flips, add it to the map
            it.neighborsNotInMap(locationToHex).forEach { locationNotInMap ->
                val hex = HexTile(locationNotInMap.first, locationNotInMap.second)
                if (shouldFlip(hex, locationToHex)) {
                    tilesToFlip.add(locationNotInMap)
                }
            }
        }
        tilesToFlip.forEach {
            locationToHex.putIfAbsent(it, HexTile(it.first, it.second))
            locationToHex[it]!!.flip()
        }
    }

    fun shouldFlip(tile: HexTile, locationToHex: MutableMap<Pair<Int, Int>, HexTile>): Boolean{
        val numBlackNeighbors = tile.numBlackNeighbors(locationToHex)
        if (tile.isWhite && numBlackNeighbors == 2) {
            return true
        } else if (!tile.isWhite && (numBlackNeighbors == 0 || numBlackNeighbors > 2)) {
            return true
        }
        return false
    }

    enum class Direction {
        EAST,
        WEST,
        NORTHEAST,
        SOUTHEAST,
        NORTHWEST,
        SOUTHWEST,
    }

    class HexTile(val x: Int, val y: Int) {
        var isWhite = true
        val location = Pair(x, y)

        fun neighbor(direction: Direction) =
            when (direction) {
                Direction.EAST -> Pair(x + 2, y)
                Direction.WEST -> Pair(x - 2, y)
                Direction.NORTHEAST -> Pair(x + 1, y + 1)
                Direction.SOUTHEAST -> Pair(x + 1, y - 1)
                Direction.NORTHWEST -> Pair(x - 1, y + 1)
                Direction.SOUTHWEST -> Pair(x - 1, y - 1)
            }

        fun numBlackNeighbors(locationToHex: MutableMap<Pair<Int, Int>, HexTile>) =
            // How many neighbors are not white (if the tile is not in the map, it's white)
            Direction.values().count {
                !(locationToHex[neighbor(it)]?.isWhite ?: true)
            }

        fun neighborsNotInMap(locationToHex: MutableMap<Pair<Int, Int>, HexTile>) =
            Direction.values().mapNotNull {
                if (!locationToHex.containsKey(neighbor(it))) {
                    neighbor(it)
                } else null
            }

        fun flip() {
            isWhite = !isWhite
        }
    }

}
