package days

import Util.readFileToStringList

class Eight {

    companion object {
        const val examplefileLocation = "src/main/resources/inputs/examples/eight.txt"
        const val fileLocation = "src/main/resources/inputs/eight.txt"
    }

    fun executeA(): Long? {
        return returnAccumOrNullIfInfinite(readFileToStringList(fileLocation), true)
    }


    fun executeB(): Long {
        val input = readFileToStringList(fileLocation).toMutableList()
        input.forEachIndexed { index, line ->
            if (line.take(3) == "nop") {
                input[index] = line.replace("nop", "jmp")
                returnAccumOrNullIfInfinite(input)?.also { return it}
                input[index] = line.replace("jmp", "nop")

            }
            if (line.take(3) == "jmp") {
                input[index] = line.replace("jmp", "nop")
                returnAccumOrNullIfInfinite(input)?.also { return it}
                input[index] = line.replace("nop", "jmp")
            }
        }
        return 0
    }

    fun returnAccumOrNullIfInfinite(input: List<String>, returnInfiniteVal: Boolean = false): Long? {
        val linesUsed = mutableSetOf<Int>()
        var accumulator = 0L
        var index = 0
        val finalLineIndex = input.size
        while(!linesUsed.contains(index)){
            if (index == finalLineIndex){
                return accumulator
            }
            linesUsed.add(index)
            val curLine = input[index]
            val sign = curLine[4].toString()
            val amt = curLine.substring(5).toInt()
            when(curLine.take(3)){
                "nop" -> {
                    index += 1
                }
                "acc" -> {
                    if(sign == "+") accumulator += amt
                    else accumulator -= amt
                    index += 1
                }
                "jmp" -> {
                    if(sign == "+") index += amt
                    else index -= amt
                }
            }
        }
        if (returnInfiniteVal) return accumulator
        return null
    }

}
