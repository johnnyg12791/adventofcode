package days

import Util.readFileToStringList

class Six {


    companion object {
        const val examplefileLocation = "src/main/resources/inputs/examples/six.txt"
        const val fileLocation = "src/main/resources/inputs/six.txt"
    }

    fun readToGroups(): MutableList<GroupAnswers> {
        val groups = mutableListOf<GroupAnswers>()
        var curGroup = GroupAnswers()
        readFileToStringList(fileLocation).map { line ->
            if (line.isEmpty()){
                groups.add(curGroup)
                curGroup = GroupAnswers()
            } else {
                curGroup.numPeople += 1
                line.forEach { char ->
                    // Feels like there's a better way to do this...
                    curGroup.answersMap[char.toString()] =
                        curGroup.answersMap.getOrDefault(char.toString(), 0) + 1
                }
            }
        }
        groups.add(curGroup) // Always that last one that doesn't get added in loop
        return groups
    }


    fun executeA(): Int? {
        return readToGroups().map { it.answersMap.keys.size }.sum()
    }


    fun executeB(): Int {
        return readToGroups().map { group ->
            group.answersMap.values.filter { it == group.numPeople }.size
        }.sum()
    }
}

class GroupAnswers() {
    val answersMap = mutableMapOf<String, Int>()
    var numPeople = 0
}