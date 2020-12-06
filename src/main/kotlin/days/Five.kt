package days

import Util.readFileToStringList
import kotlin.math.pow

class Five {

    companion object {
        const val examplefileLocation = "src/main/resources/inputs/examples/five.txt"
        const val fileLocation = "src/main/resources/inputs/five.txt"
    }

    fun executeA(): Int? {
        val seats = readFileToStringList(fileLocation).map { line ->
            BoardingPass(line)
        }
        return seats.maxByOrNull { it.id }?.id
    }


    fun executeB(): Int {
        val seats = readFileToStringList(fileLocation).map { line ->
            BoardingPass(line)
        }
        val sortedIds = seats.sortedBy { it.id }.map { it.id }
        var curId = sortedIds[0]
        sortedIds.forEach {
            if (it != curId) return it-1
            else curId += 1
        }
        return 0
    }

}

class BoardingPass(inputLine: String){
    var row = 0
    var column = 0

    init {
        inputLine.forEachIndexed { index, char ->
            when(char){
                'F', 'B' -> {
                    if (char == 'B') row += 2.0.pow(6.0 - index).toInt()
                }
                'L', 'R' -> {
                    if (char == 'R') column += 2.0.pow(9.0 - index).toInt()
                }
                else -> throw Exception("Bad Input")
            }
        }
    }

    val id = row * 8 + column

    override fun toString(): String {
        return "Row $row, Col $column, Id: $id"
    }

}
