import java.io.File

object Util {

    fun readFileToIntList(fileName: String)
            = mutableListOf<Int>().also { File(fileName).forEachLine { line -> it.add(line.toInt()) } }

    fun readFileToLongList(fileName: String)
        = mutableListOf<Long>().also { File(fileName).forEachLine { line -> it.add(line.toLong()) } }

    fun readFileToIntMap(fileName: String)
            = hashMapOf<Int, Int>().also { File(fileName).forEachLine { line -> it[line.toInt()] = 1 } }

    fun readFileToStringList(fileName: String)
        = File(fileName).readLines()

    // Returns the pair of numbers summing to the target if there exists one
    fun twoSum(inputList: List<Long>, targetSum: Long): Pair<Long, Long>? {
        val map = inputList.map { it to 1 }.toMap()
        map.forEach { (key, _) ->
            if (map.containsKey(targetSum - key)) {
                return Pair(key, targetSum -key)
            }
        }
        return null
    }
}