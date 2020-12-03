package days

import Util.readFileToStringList
import java.math.BigInteger

class Three {

    companion object {
        const val fileLocation = "src/main/resources/inputs/three.txt"
    }

    fun executeA(rightSlope: Int = 3, downSlope: Int = 1): BigInteger {
        var treesHit = 0
        var index = 0
        readFileToStringList(fileLocation).forEachIndexed { rowNum, line ->
            if (rowNum % downSlope == 0) {
                if(line[index] == '#') {
                    treesHit += 1
                }
                index = (index + rightSlope) % line.length
            }
        }
        return treesHit.toBigInteger()

    }

    fun executeB(): BigInteger {
        println("${executeA(1, 1)} ${executeA(3, 1)} ${executeA(5, 1)} ${executeA(7, 1)} ${executeA(1, 2)}")
        return executeA(1, 1) * executeA(3, 1) * executeA(5, 1) * executeA(7, 1) * executeA(1, 2)
    }

}
