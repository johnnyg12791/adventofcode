package days

import Util.readFileToStringList
import helpers.Dimension
import helpers.Quad
import helpers.Triple

class Seventeen {

    companion object {
        const val examplefileLocation = "src/main/resources/inputs/examples/17.txt"
        const val fileLocation = "src/main/resources/inputs/17.txt"
        const val file = fileLocation
    }

    fun inputToStartDimensions(cubeMap: MutableMap<Dimension, ConwayCube>,
                               cubes: MutableList<ConwayCube>, isQuad: Boolean = false){
        readFileToStringList(file).forEachIndexed { xIndex, line ->
            line.forEachIndexed { yIndex, character ->
                val coords = if (isQuad) {
                    Quad(xIndex, yIndex, 0, 0)
                } else {
                    Triple(xIndex, yIndex, 0)
                }
                val cube = ConwayCube(coords, character == '#')
                cubes.add(cube)
                cubeMap[coords] = cube
            }
        }
    }

    fun executeA(): Long {
        val dimensionToCubeMap = mutableMapOf<Dimension, ConwayCube>()
        val curCubes = mutableListOf<ConwayCube>()
        inputToStartDimensions(dimensionToCubeMap, curCubes)
        runIterations(6, dimensionToCubeMap, curCubes)
        // Slim down cubes?
        return curCubes.map { it.isActive }.count { it }.toLong()
    }

    fun runIterations(numIters: Int, cubeMap: MutableMap<Dimension, ConwayCube>, cubes: MutableList<ConwayCube>) {
        // Need a map from triple to cube
        for (i in 1..numIters) {
            val nextCubeMap = mutableMapOf<Dimension, ConwayCube>()
            val nextCubes = mutableListOf<ConwayCube>()
            val neighborsChecked = mutableSetOf<Dimension>()
            // For each neighbor
            cubes.forEach { curCube ->
                // Handle neighbors but keep track of what we've worked on already
                curCube.neighbors.forEach { neighbor ->
                    // Skip if we've "used" this already
                    if (!neighborsChecked.contains(neighbor)) {
                        // Count neighbors by getting neighbors and checking if each is active
                        val activeNeighbors = neighbor.getNeighbors().map { locationIsActive(it, cubeMap) }.count { it }
                        val nextCube = nextCubeFromCurrent(locationIsActive(neighbor, cubeMap), activeNeighbors, neighbor)
                        nextCubeMap[neighbor] = nextCube
                        nextCubes.add(nextCube)
                        neighborsChecked.add(neighbor)
                    }
                }
            }
            // Reset the things to the next things
            cubes.clear()
            cubes.addAll(nextCubes)
            cubeMap.clear()
            cubeMap.putAll(nextCubeMap)
        }
    }

    fun nextCubeFromCurrent(isActive: Boolean, activeNeighbors: Int, dimension: Dimension) =
        if (isActive) {
            if (listOf(2,3).contains(activeNeighbors)) ConwayCube(dimension, true)
            else ConwayCube(dimension, false)
        } else {
            if (activeNeighbors == 3) ConwayCube(dimension, true)
            else ConwayCube(dimension, false)
        }

    fun locationIsActive(location: Dimension, dimensionToCubeMap: MutableMap<Dimension, ConwayCube>) =
        dimensionToCubeMap.getOrDefault(location, null)?.isActive == true

    // Prints where z plane = input
    // Used for debugging partA
    private fun printCubeInPlane(z: Int = 0, cube: List<ConwayCube>, dimension: Int) {
        cube.filter { it.coords.z == z }.sorted().windowed(dimension, dimension).forEach {
            println(it)
        }
    }

    fun executeB(): Long {
        val dimensionToCubeMap = mutableMapOf<Dimension, ConwayCube>()
        val curCubes = mutableListOf<ConwayCube>()
        inputToStartDimensions(dimensionToCubeMap, curCubes, isQuad = true)
        runIterations(6, dimensionToCubeMap, curCubes)
        return curCubes.map { it.isActive }.count { it }.toLong()
    }

    class ConwayCube(val coords: Dimension, val isActive: Boolean) : Comparable<ConwayCube> {

        val neighbors = coords.getNeighbors()

        override fun toString() =
            if (isActive) "#"
            else "."

        // Only compare x and y planes (this was for printing in partA)
        override fun compareTo(other: ConwayCube) =
            if (coords.x != other.coords.x) {
                coords.x - other.coords.x
            } else if (coords.y != other.coords.y) {
                coords.y - other.coords.y
            } else {
                0
            }
    }


}