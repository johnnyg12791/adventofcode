package days

class TwentyThree {

    companion object {
        const val ONE_MILLION = 1000000
        const val TEN_MILLION = ONE_MILLION * 10
    }

    fun executeA(): String {
        val example = "389125467"
        val real = "368195742"
        val input = real.map { it.toString().toInt() }.toMutableList()
        println(input)
        var currentCupIndex = 0
        for (i in 0..99) {
            //println("currentCup: $currentCup")
            val currentCup = input[currentCupIndex]
            val pickupIndicies = mutableListOf<Int>()
            val pickup = mutableListOf<Int>().also {
                for (j in 1..3) {
                    val pickupIndex = (currentCupIndex+j) %  input.size
                    it.add(input[pickupIndex])
                    pickupIndicies.add(pickupIndex)
                }
            }
            var destination = currentCup - 1
            // Destination can't be less than our minimum, if it is, set to maximum
            // Also can't be in the list of pickup. If it is, subtract one more
            while (pickup.contains(destination) || destination < input.minOrNull()!!) {
                if (destination < input.minOrNull()!!) {
                    destination = input.maxOrNull()!!
                } else {
                    destination -= 1
                }
            }
            // Readjust the input
            println("Turn $i : $input")
            println(currentCup)
            pickupIndicies.sorted().reversed().forEach {
                input.removeAt(it)
                if(it < currentCupIndex) {
                    currentCupIndex -= 1
                }
            }
            println(pickup)
            println(destination)
            val destinationIndex = input.indexOf(destination) + 1
            input.addAll(destinationIndex, pickup)

            // If I place the 3 pieces ahead of my cup, then I need to increment index
            if(destinationIndex < currentCupIndex) {
                currentCupIndex += 3
            } // And always increment index by 1, also mod size
            currentCupIndex = (currentCupIndex + 1) % input.size
        }
        println(input)
        return buildAnswer(input)
    }

    fun buildAnswer(input: List<Int>): String {
        val startIndex = input.indexOf(1)
        var answer = ""
            for (j in 1..8) {
                answer += input[(startIndex+j) %  input.size].toString()
            }
        return answer

    }

    // Need to mess with a new datastructure here don't I
    fun executeB(): String {
        val example = "389125467"
        val real = "368195742"
        val input = example.map { it.toString().toInt() }.toMutableList()
        var curMax = input.maxOrNull()!!

        while (input.size < ONE_MILLION) {
            curMax += 1
            input.add(curMax)
        }
        assert(input.last() == ONE_MILLION)
        return "0"
        //return buildAnswer(input)
    }


}