package days

import Util.readFileToStringList
import kotlin.math.abs

class Thirteen {


    companion object {
        const val examplefileLocation = "src/main/resources/inputs/examples/13.txt"
        const val fileLocation = "src/main/resources/inputs/13.txt"
        const val file = fileLocation
    }


    fun executeA(): Long {
        val earliestDepart = readFileToStringList(file)[0].toInt()
        val buses = readFileToStringList(file)[1]
        val busesInRotation = buses.split(",").filter { it != "x" }.map { it.toInt() }
        println(busesInRotation)
        var busToTake = 0
        var minTimeUntil = 100000
        busesInRotation.forEach { busInterval ->
            (0..busInterval).forEach { minutesAfterDeparture ->
                if ((earliestDepart + minutesAfterDeparture) % busInterval == 0) {
                    if (minutesAfterDeparture < minTimeUntil) {
                        busToTake = busInterval
                        minTimeUntil = minutesAfterDeparture
                    }
                }
            }
        }
        return busToTake * minTimeUntil.toLong()
    }


    fun executeB(): Long {
        val buses = readFileToStringList(file)[1]
        val busesInRotation = buses.split(",").map { busInterval ->
            if (busInterval == "x") -1L
            else busInterval.toLong()
        }
        println(busesInRotation)
        var startTimestamp = busesInRotation[0]
        var jumpSize = 0L
        var shouldRunLoop = true
        while (shouldRunLoop) {
            startTimestamp += jumpSize
            // println(jumpSize)
            var newJumpSize = 1L
            shouldRunLoop = false
            for (incTimestamp in busesInRotation.indices) {
                if (shouldRunLoop || busesInRotation[incTimestamp] == -1L){
                    continue
                } else if ((startTimestamp+incTimestamp) % busesInRotation[incTimestamp] == 0L) {
                    newJumpSize *= busesInRotation[incTimestamp]
                    if (newJumpSize > jumpSize){
                        jumpSize = newJumpSize
                    }
                    continue
                } else {
                    shouldRunLoop = true
                }
            }
        }
        return startTimestamp
    }
}