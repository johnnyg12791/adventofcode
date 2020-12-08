package days

import Util.readFileToStringList
import kotlin.collections.HashSet

class Seven {

    companion object {
        const val examplefileLocation = "src/main/resources/inputs/examples/seven.txt"
        const val examplefileLocation2 = "src/main/resources/inputs/examples/sevenB.txt"
        const val fileLocation = "src/main/resources/inputs/seven.txt"
    }

    fun executeA(): Long {
        val treeMap = inputToTree()
        return findContainersWith(mutableSetOf("shiny gold"), treeMap).size.toLong()
    }

    fun findContainersWith(suitcaseChildren: Set<String>,
                           treeMap: MutableMap<String, List<SuitcaseContainer>>,
                           distinctParents: HashSet<String> = HashSet()): HashSet<String> {
        val initialCount = distinctParents.size
        treeMap.forEach { (suitcaseParent, suitcaseChild) ->
            suitcaseChild.forEach { container ->
                if (suitcaseChildren.contains(container.color)) {
                    distinctParents.add(suitcaseParent)
                }
            }
        }
        if (distinctParents.size != initialCount) {
            // Non base case: Keep Adding Parents
            findContainersWith(suitcaseChildren + distinctParents, treeMap, distinctParents)
        }
        // End base case: If those parents have no additional parents then I'm done
        return distinctParents
    }

    fun executeB(): Long {
        val treeMap = inputToTree()
        return findBagsInside("shiny gold", treeMap).toLong() - 1 // For the root bag itself
    }

    fun findBagsInside(root: String, treeMap: MutableMap<String, List<SuitcaseContainer>>): Int{
        var sum = 0
        treeMap.forEach { (suitcaseParent, suitcaseChild) ->
            if (suitcaseParent == root) {
                if (suitcaseChild.isEmpty()) return 1 // Base case: We have no children
                suitcaseChild.forEach {
                    sum += it.amount * findBagsInside(it.color, treeMap)
                }
                sum += 1 // To account for the actual bag of the 'root'
            }

        }
        return sum
    }


    fun inputToTree(): MutableMap<String, List<SuitcaseContainer>> {
        val rulesMap = mutableMapOf<String, List<SuitcaseContainer>>()
        readFileToStringList(fileLocation).forEach {
            val firstMatch = ("\\w+ \\w+").toRegex().find(it)!!.value
            val containedIn = mutableListOf<SuitcaseContainer>()
            ("[0-9]+ \\w+ \\w+").toRegex().findAll(it).forEach { contain ->
                val splitResults = contain.value.split(" ", limit = 2)
                containedIn.add(SuitcaseContainer(splitResults[0].toInt(), splitResults[1]))
            }
            rulesMap[firstMatch] = containedIn
        }
        return rulesMap
    }

    class SuitcaseContainer(val amount: Int, val color: String) {
        override fun toString(): String {
            return "$amount - $color"
        }
    }


}