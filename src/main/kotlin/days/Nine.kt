package days

import Util.readFileToIntList
import Util.twoSum
import Util.readFileToLongList

class Nine {


    companion object {
        const val examplefileLocation = "src/main/resources/inputs/examples/nine.txt"
        const val fileLocation = "src/main/resources/inputs/nine.txt"
    }

    fun executeA(): Long {
        val input = readFileToLongList(fileLocation)
        val preambleLen = 25
        var index = 0
        while(index+preambleLen < input.size) {
            val previousN = input.slice(index..preambleLen+index)
            val nextNum = input[preambleLen+index]
            if (twoSum(previousN, nextNum) == null){
                return nextNum
            }
            index += 1
        }
        return -1
    }


    fun executeB(): Long =
        slidingWindowSum(readFileToLongList(fileLocation), executeA()).let {
            it.maxOrNull()!! + it.minOrNull()!!
        }


    // Returns a list of contiguous numbers that sum to target
    fun slidingWindowSum(input: MutableList<Long>, target: Long): List<Long> {
        var window = mutableListOf<Long>()
        var forwardPointer = 0
        var backwardPointer = 0
        while(window.sum() != target) {
            if (window.sum() < target) {
                forwardPointer += 1
            }
            if (window.sum() > target) {
                backwardPointer += 1
            }
            window = input.subList(backwardPointer,forwardPointer)
        }
        println(window)
        return window
    }

}