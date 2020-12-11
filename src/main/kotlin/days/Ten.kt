package days

import Util.readFileToIntList

class Ten {

    companion object {
        const val examplefileLocation = "src/main/resources/inputs/examples/ten.txt"
        const val fileLocation = "src/main/resources/inputs/ten.txt"
        const val file = fileLocation
        var memoizedVoltsToPossibilities = mutableMapOf<List<Int>, Long>()
        val usedLists = mutableSetOf<List<Int>>() // This is not the prettiest but went for it anyway
    }

    fun executeA(): Int {
        val voltDifferences = mutableMapOf<Int, Int>()
        readFileToIntList(file).sorted().toMutableList().also {
            it.add(0,0) // Seat voltage is 0
            it.add(it.last()+3) // Our thing is 3 greater than max
        }.windowed(2).forEach {
            voltDifferences.putIfAbsent(it.last() - it.first(), 0)
            voltDifferences[it.last() - it.first()] = voltDifferences[it.last() - it.first()]!!.plus(1)
        }
        return voltDifferences[3]!! * voltDifferences[1]!!
    }


    fun executeB(): Long {
        val factors = mutableListOf<Long>()
        val adapters = readFileToIntList(file).sorted().toMutableList().also {
            it.add(0,0) // Seat is 0
            it.add(it.last()+3) // Our thing is 3 greater than max
        }

        // How many gaps of 3 are there
        // Calculate ways that go up to the number 3 difference
        var windowSize = 0
        var startWindowIndex = 0
        adapters.forEachIndexed{ index, adapterVolts ->
            windowSize += 1
            if (index +1 < adapters.size && adapterVolts + 3 == adapters[index+1]){
                // take everything in adapters from index, index+windowSize
                // subtract the smallest number from the list, so we always start at 0. Allows us to memoize
                val adapterList = adapters.slice(startWindowIndex..startWindowIndex+windowSize).map {
                    it - adapters[startWindowIndex]
                }
                computePossibilites(adapterList, adapterList.last()).also {
                    factors.add(it)
                    memoizedVoltsToPossibilities[adapterList] = it
                }
                // Reset my things
                startWindowIndex = index+1
                windowSize = 0
                usedLists.removeAll(usedLists)
            }

        }

        println(factors)
        var product = 1L
        factors.forEach { product *= it }
        return product
    }


    private fun computePossibilites(list: List<Int>, maxNum: Int): Long {
        memoizedVoltsToPossibilities[list]?.let {
            return it
        }
        if (usedLists.contains(list)) return 0 // During this iteration we're already done
        var validPossibilites = 0L
        validPossibilites += isValidAdapterList(list) // whole list
        usedLists.add(list)
        if (list.size <= 2){
            return validPossibilites
        }
        list.forEach {
            if (it != 0 && it != maxNum) { // The first and last element in the initial list are always required
                validPossibilites += computePossibilites(list.minus(it), maxNum)
            }
        }
        return validPossibilites
    }

    private fun isValidAdapterList(list: List<Int>): Long {
        if (list.isEmpty()) return 0
        if (list.size == 1) return 1
        list.windowed(2).forEach {
            if (it.last() - it.first() > 3){
                return 0
            }
        }
        return 1
    }

}
