package days

class Fifteen {

    private var exampleInput = listOf(0, 3, 6)
    private var input = listOf(15,5,1,4,7,0)
    private var finalTurn = 2020

    fun executeA(): Long {
        val numbersSaid = input.toMutableList()
        var turn = numbersSaid.size + 1
        val indicidesOfNumbersSaid = mutableMapOf<Int, MutableList<Int>>()
        numbersSaid.forEach { indicidesOfNumbersSaid[it] = mutableListOf(numbersSaid.lastIndexOf(it)+1) }

        while (turn < finalTurn + 1) { // Because I actually want to run the final turn
            val mostRecentlySaidNumber = numbersSaid.last()
            if (indicidesOfNumbersSaid[mostRecentlySaidNumber]!!.size == 1) {
                numbersSaid.add(0)
                indicidesOfNumbersSaid[0]!!.add(turn)
            } else {
                val nextNumber = turn - 1 - indicidesOfNumbersSaid[mostRecentlySaidNumber]!!.let {
                    it[it.size-2] // Second to last index is the number I want to subtract from
                }
                numbersSaid.add(nextNumber)
                indicidesOfNumbersSaid.putIfAbsent(nextNumber, mutableListOf())
                indicidesOfNumbersSaid[nextNumber]!!.add(turn)
            }
            turn += 1
        }
        return numbersSaid.last().toLong()
    }


    fun executeB(): Long {
        finalTurn = 30000000
        return executeA()
    }

}