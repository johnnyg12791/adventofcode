package days

import Util.readFileToStringList
import Util.reduceToSingles

class TwentyOne {


    private val examplefileLocation = "src/main/resources/inputs/examples/21.txt"
    private val fileLocation = "src/main/resources/inputs/21.txt"
    private var input = readFileToStringList(fileLocation)

    val allergenMap = mutableMapOf<String, Allergen>()
    val allWords = mutableSetOf<String>()
    val notAllergenWords = mutableListOf<String>()
    val wordCount = mutableMapOf<String, Int>()

    fun executeA(): Long {
        readInputToMapsAndSets()
        return notAllergenWords.map {
            wordCount[it] ?: 0
        }.sum().toLong()
    }

    fun readInputToMapsAndSets() {
        input.forEach { line ->
            val (words, allergens) = line.split(" (contains")
            val wordsOnLine = words.split(" ")
            allergens.split(", ").map { it.replace(")", "").replace(" ", "") }.forEach {
                allergenMap.putIfAbsent(it, Allergen(it))
                allergenMap[it]!!.addToPossibilities(wordsOnLine)
            }
            allWords.addAll(wordsOnLine)
            wordsOnLine.forEach {
                wordCount.putIfAbsent(it, 0)
                wordCount[it] = wordCount[it]!! + 1
            }
        }
        // Addition by subtraction
        notAllergenWords.addAll(allWords)
        val possibleAllergenWords = mutableSetOf<String>()
        allergenMap.values.forEach { possibleAllergenWords.addAll(it.possibilities) }
        notAllergenWords.removeAll(possibleAllergenWords)
    }

    fun executeB(): String {
        readInputToMapsAndSets()
        // Start of part B
        val allergenOrder = mutableListOf<String>()
        val possibilities = mutableListOf<MutableList<String>>()
        allergenMap.values.forEach {
            allergenOrder.add(it.name)
            it.possibilities.removeAll(notAllergenWords)
            possibilities.add(it.possibilities)
        }
        val singles = reduceToSingles(possibilities)
        val singleAllergenMapping = mutableMapOf<String, String>()
        allergenOrder.forEachIndexed { idx, name ->
            singleAllergenMapping[name] = singles[idx]
        }
        return singleAllergenMapping.toSortedMap().values.joinToString(",")
    }

    class Allergen(val name: String){
        var possibilities = mutableListOf<String>()

        fun addToPossibilities(possibleWords: List<String>) =
            if (possibilities.isEmpty()){
                possibilities = possibleWords.toMutableList()
            } else {
                possibilities = possibilities.intersect(possibleWords).toMutableList()
            }

    }

}