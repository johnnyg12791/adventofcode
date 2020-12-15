package days

import Util.readFileToStringList
import kotlin.math.pow

class Fourteen {

    private val examplefileLocation = "src/main/resources/inputs/examples/14.txt"
    private val fileLocation = "src/main/resources/inputs/14.txt"
    private var input = readFileToStringList(fileLocation)
    private val inputExampleB = readFileToStringList("src/main/resources/inputs/examples/14_b.txt")

    fun executeA(): Long {
        var currentMask = input[0].split('=')[1].trim()
        val results = mutableMapOf<Long, Long>()

        input.slice(1 until input.size).forEach { line ->
            if (line.contains("mem")) {
                val (key, value) = line.replace("mem[", "").replace("]", "").replace(" ", "").split("=")
                results[key.toLong()] = maskOnValue(currentMask, value.toLong())
            } else {
                currentMask = line.split('=')[1].trim()
            }
        }
        return results.values.sum()
    }


    private fun maskOnValue(mask: String, value: Long): Long {
        var newVal = value
        // if odd (%2 == 1) and mask at largest spot is 0, Subtract 1
        // if even (%2 == 0) and mask at largest spot is 1, Add 1

        // If (val%4 > 2) and mask at second largest spot is 0, Subtract 2
        // Add 2 if if (val%4==0) mask at largest spot is 1

        // subtract 4 if (val%8 >= 4) and mask at second largest spot is 0
        // Add 4 if if (val%8<4) mask at largest spot is 1....
        mask.reversed().forEachIndexed{ maskIndex, maskVal ->
            if (maskVal != 'X'){
                val upperLimit = 2.toDouble().pow(maskIndex+1).toLong()
                val halfLimit = 2.toDouble().pow(maskIndex).toLong()
                if (value % upperLimit >= halfLimit && maskVal == '0') {
                    newVal -= halfLimit
                }
                if (value % upperLimit < halfLimit && maskVal == '1') {
                    newVal += halfLimit
                }
            }
        }
        return newVal
    }



    fun executeB(): Long {
        var currentMask = input[0].split('=')[1].trim()
        val results = mutableMapOf<Long, Long>()
        input.slice(1 until input.size).forEach { line ->
            if (line.contains("mem")) {
                val (possibleAddr, value) = line.replace("mem[", "").replace("]", "").replace(" ", "").split("=")
                versionTwoMask(currentMask, possibleAddr.toInt()).forEach { newAddress ->
                    results[newAddress] = value.toLong()
                }
            } else {
                currentMask = line.split('=')[1].trim()
            }
        }
        return results.values.sum()
    }

    fun versionTwoMask(mask: String, value: Int): Set<Long> {
        val possibleAddresses = mutableSetOf<Long>()
        var baseVal = 0L
        val xIndicies = mutableListOf<Int>()
        mask.reversed().forEachIndexed{ maskIndex, maskVal ->
            val upperLimit = 2.toDouble().pow(maskIndex+1).toLong()
            val halfLimit = 2.toDouble().pow(maskIndex).toLong()
            if (maskVal == 'X'){
                xIndicies.add(maskIndex)
            } else if (maskVal == '1'){
                baseVal += 2.toDouble().pow(maskIndex).toLong()
            } else if (value % upperLimit >= halfLimit && maskVal == '0') {
                baseVal += halfLimit
            }
        }
        possibleAddresses.add(baseVal)

        xIndicies.forEach { xIndex ->
            val newPossibleAddress = mutableSetOf<Long>()
            possibleAddresses.forEach { baseAddr ->
                val addition = 2.toDouble().pow(xIndex).toLong()
                newPossibleAddress.add(baseAddr+addition)
            }
            possibleAddresses.addAll(newPossibleAddress)
        }
        return possibleAddresses
    }

}