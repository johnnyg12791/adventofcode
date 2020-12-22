package days

import Util.readFileToStringList

class Nineteen {

    private val examplefileLocation = "src/main/resources/inputs/examples/19.txt"
    private val fileLocation = "src/main/resources/inputs/19.txt"
    private var input = readFileToStringList(fileLocation)

    fun executeA(): Long {
        var hitBlankLine = false
        val numToRule = mutableMapOf<Int, String>()
        val inputsToTest = mutableListOf<String>()
        input.forEach { line ->
            if (line.isEmpty()) {
                hitBlankLine = true
            } else if (!hitBlankLine) {
                val (ruleNum, rule) = line.split(":")
                numToRule[ruleNum.toInt()] = rule.trim().replace("\"", "")
            } else {
                inputsToTest.add(line)
            }

        }
        val zeroRule = reduceRule(numToRule[0] ?: throw Exception("bad input"), numToRule).replace(" ", "")

        return inputsToTest.count {
            zeroRule.toRegex().find(it)?.value?.length == it.length
        }.toLong()
    }

    fun reduceRule(rule: String, numToRule: Map<Int,String>): String {
        if (!rule.contains("\\d".toRegex())) {
            return rule
        } else {
            var updatedRule = rule
            //work backwards from ranges to replace?
            val rangesToReplace = mutableMapOf<IntRange, String>()
            ("\\d+").toRegex().findAll(rule).forEach {
                val replacementVal = numToRule[it.value.toInt()]?.let { mapVal ->
                    if (mapVal.contains("|")) "($mapVal)"
                    else mapVal
                } ?: throw Exception("bad input: ${it.value}")
                rangesToReplace[it.range] = replacementVal
            }
            val sortedRanges = rangesToReplace.toSortedMap(compareBy<IntRange> { it.first }.reversed())
            sortedRanges.forEach { (range, replacementVal) ->
                updatedRule = updatedRule.replaceRange(range, replacementVal)

            }
            return reduceRule(updatedRule, numToRule)
        }
    }

    fun executeB(): Long {
        var hitBlankLine = false
        val numToRule = mutableMapOf<Int, String>()
        val inputsToTest = mutableListOf<String>()
        input.forEach { line ->
            if (line.isEmpty()) {
                hitBlankLine = true
            } else if (!hitBlankLine) {
                val (ruleNum, rule) = line.split(":")
                numToRule[ruleNum.toInt()] = rule.trim().replace("\"", "")
            } else {
                inputsToTest.add(line)
            }
        }

        val fourtyTwoRule = "(" + reduceRule(numToRule[42] ?: throw Exception("bad input"), numToRule).replace(" ", "") + ")"
        val thirtyOneRule = "(" + reduceRule(numToRule[31] ?: throw Exception("bad input"), numToRule).replace(" ", "") + ")"
        val thirtyOneRuleOptional = "$thirtyOneRule?"

        // Minimum match is 42 42 31
        // Then I can have as many 42s as I want, as long as I have <= 31s (aka optional)
        val ruleList = mutableListOf<String>()
        (1..10).forEach { numReps ->
            ruleList.add("""(${fourtyTwoRule.repeat(numReps)})($fourtyTwoRule)+(${thirtyOneRule})(${thirtyOneRuleOptional.repeat(numReps-1)})""")
        }

        // Got some help from reddit on this one:
        // https://www.reddit.com/r/adventofcode/comments/kg5ov4/2020_day_19_part_2_rethinking_my_life_choices/
        return inputsToTest.count { strInput ->
            ruleList.any { it.toRegex().find(strInput)?.value?.length == strInput.length }
        }.toLong()
    }

}
