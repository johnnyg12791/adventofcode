package days

import Util.readFileToStringList
import kotlin.math.abs

class Twelve {


    companion object {
        const val examplefileLocation = "src/main/resources/inputs/examples/12.txt"
        const val fileLocation = "src/main/resources/inputs/12.txt"
        const val file = fileLocation
    }

    class Coodinate(var x: Int, var y: Int) {
        override fun toString(): String {
            return "($x,$y)"
        }
    }

    fun executeA(): Long {
        val directions = readFileToStringList(file)
        var facing = 'E'
        val coordinate = Coodinate(0, 0)
        directions.forEach {
            when (it[0]) {
                'N', 'S', 'E', 'W' -> moveDirection(it[0], it.substring(1).toInt(), coordinate)
                'F' -> moveDirection(facing, it.substring(1).toInt(), coordinate)
                'R', 'L' -> facing = rotateFacing(facing, it[0], it.substring(1).toInt())
            }
        }
        return abs(coordinate.x).toLong() + abs(coordinate.y)
    }

    fun rotateFacing(facing: Char, turn: Char, angle: Int): Char {
        val clockwise = listOf('E', 'S', 'W', 'N')
        val counterClockwise = listOf('E', 'N', 'W', 'S')
        if (angle % 90 != 0) throw Exception("Angle not a multiple of 90: $angle")
        when (turn) {
            'R' -> return clockwise[(clockwise.indexOf(facing) + angle / 90) % 4]
            'L' -> return counterClockwise[(counterClockwise.indexOf(facing) + angle / 90) % 4]
        }
        throw Exception("Bad turn character: $turn")
    }

    fun moveDirection(direction: Char, distance: Int, coordinate: Coodinate) {
        when (direction) {
            'N' -> coordinate.y += distance
            'S' -> coordinate.y -= distance
            'E' -> coordinate.x += distance
            'W' -> coordinate.x -= distance
        }
    }

    fun executeB(): Long {
        val directions = readFileToStringList(file)
        val shipCoordinate = Coodinate(0, 0)
        val waypointCoordinate = Coodinate(10, 1)
        directions.forEach {
            when (it[0]) {
                // Moves just the waypoint
                'N', 'S', 'E', 'W' -> moveDirection(it[0], it.substring(1).toInt(), waypointCoordinate)
                'F' -> moveTowardsWaypoint(it.substring(1).toInt(), shipCoordinate, waypointCoordinate)
                'R', 'L' -> rotateWaypoint(it[0], it.substring(1).toInt(), waypointCoordinate, shipCoordinate)
            }
        }
        return abs(shipCoordinate.x).toLong() + abs(shipCoordinate.y)
    }

    // They both move together by the same amount
    fun moveTowardsWaypoint(amount: Int, shipCoord: Coodinate, waypointCoord: Coodinate) {
        val xToMove = amount * (waypointCoord.x - shipCoord.x)
        val yToMove = amount * (waypointCoord.y - shipCoord.y)

        shipCoord.x += xToMove
        shipCoord.y += yToMove
        waypointCoord.x += xToMove
        waypointCoord.y += yToMove
    }

    fun rotateWaypoint(direction: Char, angle: Int, waypointCoord: Coodinate, shipCoord: Coodinate) {
        val xDistance = waypointCoord.x - shipCoord.x
        val yDistance = waypointCoord.y - shipCoord.y

        if (angle == 180) {
            // In this case, left or right, we are reversing both directions
            waypointCoord.x = shipCoord.x - xDistance
            waypointCoord.y = shipCoord.y - yDistance
        } else if ((angle == 90 && direction == 'R') || (angle == 270 && direction == 'L')) {
            // turn one way
            waypointCoord.x = shipCoord.x + yDistance
            waypointCoord.y = shipCoord.y - xDistance
        } else if ((angle == 90 && direction == 'L') || (angle == 270 && direction == 'R')) {
            // reverse!
            waypointCoord.x = shipCoord.x - yDistance
            waypointCoord.y = shipCoord.y + xDistance
        } else {
            throw Exception("Unexpected direction/angle: $direction, $angle")
        }

    }
}