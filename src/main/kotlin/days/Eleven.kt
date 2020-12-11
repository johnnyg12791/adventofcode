package days

import Util.readFileToStringList
import kotlin.math.max
import kotlin.math.min

class Eleven {


    companion object {
        const val examplefileLocation = "src/main/resources/inputs/examples/11.txt"
        const val fileLocation = "src/main/resources/inputs/11.txt"
        const val file = fileLocation
        var maxRow = 0
        var maxCol = 0
    }

    fun executeA(): Long {
        val seatingMap = readToSeatMap(readFileToStringList(file))
        val newSeatMap = mutableMapOf<Pair<Int, Int>, Seat>()
        var isChanged = true
        var iter = 0
        while (isChanged) {
            iter += 1
            isChanged = false
            // Go through each seat, finding the "new seat" and checking if it's changed
            seatingMap.forEach { (coordinate, seat) ->
                // Not 'great' to do this everytime. Can optimize later..
                seat.initPartANeighbors()
                newSeatMap[coordinate] = getNewSeat(coordinate, seat, seatingMap, seat.neighbors, 4)
                if (newSeatMap[coordinate]?.state != seat.state) isChanged = true
            }
            seatingMap.putAll(newSeatMap)
            println(iter)
        }

        return seatingMap.values.count { it.isOccupied() }.toLong()
    }

    fun executeB(): Long {
        val seatingMap = readToSeatMap(readFileToStringList(file))
        val newSeatMap = mutableMapOf<Pair<Int, Int>, Seat>()
        var isChanged = true
        var iter = 0
        while (isChanged) {
            iter += 1
            isChanged = false
            // Go through each seat
            seatingMap.forEach { (coordinate, seat) ->
                // Not 'great' to do this everytime. Can optimize later..
                seat.initPartBNeighbors(seatingMap)
                newSeatMap[coordinate] = getNewSeat(coordinate, seat, seatingMap, seat.partBneighbors, 5)
                if (newSeatMap[coordinate]?.state != seat.state) isChanged = true
            }
            seatingMap.putAll(newSeatMap)
            println(iter)
        }

        return seatingMap.values.count { it.isOccupied() }.toLong()
    }

    fun getNewSeat(coordinate: Pair<Int, Int>, seat: Seat, seatingMap: MutableMap<Pair<Int, Int>, Seat>,
                   neighbors: Set<Pair<Int, Int>>, numOccupied: Int): Seat{

        val numOccupiedNeighbors = neighbors.map { seatingMap[it]?.isOccupied() }.count { it == true }

        return if (seat.isEmpty() && numOccupiedNeighbors == 0) {
            Seat(coordinate.first, coordinate.second, '#')
        } else if (seat.isOccupied() && numOccupiedNeighbors >= numOccupied) {
            Seat(coordinate.first, coordinate.second, 'L')
        } else {
            Seat(coordinate.first, coordinate.second, seat.state)
        }
    }

    fun readToSeatMap(inputLines: List<String>): MutableMap<Pair<Int, Int>, Seat> {
        maxCol = inputLines[0].length - 1
        maxRow = inputLines.size - 1
        //row, col -> Seat
        val seatMap = mutableMapOf<Pair<Int, Int>, Seat>()
        inputLines.forEachIndexed { row, seatRow ->
            seatRow.forEachIndexed { col, seatVal ->
                seatMap[Pair(row, col)] = Seat(row, col, seatVal)
            }
        }
        return seatMap
    }

    class Seat(val row: Int, val col: Int, val state: Char) {
        fun isEmpty() = state == 'L'
        fun isOccupied() = state == '#'
        fun isFloor() = state == '.'
        val neighbors = mutableSetOf<Pair<Int, Int>>()
        val partBneighbors = mutableSetOf<Pair<Int, Int>>()

        override fun toString() =
            "$row, $col, $state"

        fun initPartANeighbors() {
            // Set (partA) neighbors
            (max(row - 1, 0)..min(row + 1, maxRow)).forEach { neighborRow ->
                (max(col - 1, 0)..min(col + 1, maxCol)).forEach { neighborCol ->
                    if (!(row == neighborRow && col == neighborCol)) {
                        neighbors.add(Pair(neighborRow, neighborCol))
                    }
                }
            }
        }

        fun initPartBNeighbors(seatMap: MutableMap<Pair<Int, Int>, Seat>) {
            val start = Pair(row, col)
            val directions = mutableListOf<Pair<Int, Int>>()
            (-1..1).forEach { xDir ->
                (-1..1).forEach { yDir ->
                    if (!(xDir == 0 && yDir == 0)) {
                        directions.add(Pair(xDir, yDir))
                    }
                }
            }
            directions.forEach {
                // "Move" In the proper direction until we either: Hit the maxRow/Col(wall), Hit !isFloor
                var curX = start.first + it.first
                var curY = start.second + it.second
                while (true) {
                    // We hit a wall or nonFloor
                    if (curX < 0 || curX > maxRow || curY < 0 || curY > maxCol) {
                        break
                    } else if (seatMap[Pair(curX, curY)]?.isFloor() == false) {
                        partBneighbors.add(Pair(curX, curY))
                        break
                    } else {
                        // Continue searching in the same direction
                        curX += it.first
                        curY += it.second
                    }
                }
            } // Have checked all directions
        } // Done with neighbors

    }

}
