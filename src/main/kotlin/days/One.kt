package days

import Util.twoSum

class One {

    companion object {
        const val fileLocation = "src/main/resources/inputs/one.txt"
        const val targetSum = 2020
    }

    fun executeA(): Long {
        twoSum(Util.readFileToLongList(fileLocation), targetSum.toLong())?.let {
            it.first * it.second
        }
        return 0L
    }

    fun executeB() {
        val intMap = Util.readFileToIntMap(fileLocation)
        intMap.forEach { (key, _) ->
            val newTargetSum = targetSum-key
            // Check through the rest of the map, seeing if I can find 2 sum again
            intMap.forEach { (secondKey, _) ->
                if (intMap.containsKey(newTargetSum - secondKey)) {
                    val thirdKey = newTargetSum - secondKey
                    println("$key, $secondKey ${thirdKey}, ${key * thirdKey * secondKey}")
                }
            }
        }

    }

}