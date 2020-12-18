package days

import Util.readFileToStringList

class Eighteen {

    private val examplefileLocation = "src/main/resources/inputs/examples/18.txt"
    private val fileLocation = "src/main/resources/inputs/18.txt"
    private var input = readFileToStringList(fileLocation)

    fun executeA() =
        input.map { computeValuesInsideParens(it, ::leftToRightCompute).toLong() }.sum()

    fun executeB() =
        input.map { computeValuesInsideParens(it, ::advancedCompute).toLong() }.sum()

    fun computeValuesInsideParens(equation: String, baseCaseFct: (String) -> String): String {
        if (!equation.contains("(")) {
            return baseCaseFct(equation)
        } else {
            var updatedEquation = ""
            var numOpenParens = 0
            var numClosedParens = 0
            for (idx in equation.indices) {
                if (equation[idx] == '(') {
                    numOpenParens += 1
                } else if (equation[idx] == ')') {
                    numClosedParens += 1
                    if (numClosedParens == numOpenParens) {
                        // Before my first openParen, slice around the parens, after idx of matching
                        updatedEquation = equation.substringBefore("(") + computeValuesInsideParens(equation.slice(equation.indexOf("(") + 1 until idx), baseCaseFct) + equation.substring(idx + 1)
                        break
                    }
                }
            }
            return computeValuesInsideParens(updatedEquation, baseCaseFct)
        }
    }

    fun advancedCompute(equation: String): String =
        ("\\d+ \\+ \\d+").toRegex().find(equation)?.value?.let {
            advancedCompute(equation.replaceFirst(it, doMath(it)))
        } ?: ("\\d+ \\* \\d+").toRegex().find(equation)?.value?.let {
            advancedCompute(equation.replaceFirst(it, doMath(it)))
        } ?: equation


    fun leftToRightCompute(equation: String): String =
        ("\\d+ [\\+\\*] \\d+").toRegex().find(equation)?.value?.let {
            leftToRightCompute(doMath(it) + equation.replaceFirst(it, ""))
        } ?:  equation


    fun doMath(operation: String) =
        if(operation.indexOf("+") != -1){
            (operation.substringBefore(" +").toLong() + operation.substringAfter("+ ").toLong()).toString()
        } else{
            (operation.substringBefore(" *").toLong() * operation.substringAfter("* ").toLong()).toString()
        }

}