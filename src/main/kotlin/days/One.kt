package days

import java.io.File


class One {

    fun executeA() {
        val fileLocation = "src/main/resources/inputs/one_a.csv"
        Util.readFileToIntList(fileLocation)
        val intMap = Util.readFileToIntMap(fileLocation)
        val targetSum = 2020
        intMap.forEach { (key, _) ->
            if (intMap.containsKey(targetSum-key)) {
                println("$key ${targetSum-key}, ${key * (targetSum-key)}")
            }
        }

    }

    fun executeB() {
        val fileLocation = "src/main/resources/inputs/one_a.csv"
        Util.readFileToIntList(fileLocation)
        val intMap = Util.readFileToIntMap(fileLocation)
        val targetSum = 2020
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