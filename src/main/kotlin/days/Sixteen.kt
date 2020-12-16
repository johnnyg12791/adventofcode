package days

import Util.readFileToStringList

class Sixteen {

    companion object {
        const val exampleFileLocation = "src/main/resources/inputs/examples/16.txt"
        const val exampleFileB = "src/main/resources/inputs/examples/16_b.txt"
        const val fileLocation = "src/main/resources/inputs/16.txt"
        const val file = fileLocation
    }

    fun executeA(): Long {
        val nearbyTickets = mutableListOf<List<Int>>()
        val validities = mutableListOf<TicketValidity>()
        var isNearbyTicket = false
        readFileToStringList(file).forEach { line ->
            if (line.isEmpty()) {
                println("empty line, continuing")
            } else if (line[0].isLetter() && line != "your ticket:" && line != "nearby tickets:") {
                validities.add(TicketValidity(line))
            } else if (line[0].isDigit() && isNearbyTicket) {
                nearbyTickets.add(line.split(",").map { it.toInt() })
            } else if (line == "nearby tickets:") {
                isNearbyTicket = true
            }
        }
        return nearbyTickets.map { ticketInvalidSum(it, validities) }.sum()
    }

    // Returns the sum of invalid values
    fun ticketInvalidSum(ticket: List<Int>, validities: MutableList<TicketValidity>) =
        ticket.filter { position ->
            validities.all { !(it.isWithinARange(position)) }
        }.sum().toLong()


    // Similar to above, but doesnt return a sum, just boolean
    fun ticketIsValid(ticket: List<Int>, validities: MutableList<TicketValidity>) =
        ticket.all { position ->
            (validities.any { it.isWithinARange(position) })
        }


    fun executeB(): Long {
        // Mostly copy paste from A for now, kinda got ugly...
        val nearbyTickets = mutableListOf<List<Int>>()
        var yourTicket = listOf<Int>()
        val validities = mutableListOf<TicketValidity>()
        var isYourTicket = false
        var isNearbyTicket = false
        readFileToStringList(file).forEach { line ->
            if (line.isEmpty()) {
                println("empty line, continuing")
            } else if (line[0].isLetter() && line != "your ticket:" && line != "nearby tickets:") {
                validities.add(TicketValidity(line))
            } else if (line[0].isDigit() && isNearbyTicket) {
                nearbyTickets.add(line.split(",").map { it.toInt() })
            } else if (isYourTicket) {
                yourTicket = line.split(",").map { it.toInt() }
                isYourTicket = false
            } else if (line == "nearby tickets:") {
                isNearbyTicket = true
            } else if (line == "your ticket:") {
                isYourTicket = true
            }
        }

        val validNearbyTickets = nearbyTickets.filter { ticketIsValid(it, validities) }
        println("number of valid nearby tickets: ${validNearbyTickets.size}")

        val ticketClassOrdering = mutableListOf<List<TicketValidity>>()

        validNearbyTickets[0].indices.forEach { curPos ->
            //find which ticket says all of these are valid
            ticketClassOrdering.add(validities.filter {
                validNearbyTickets.map { ticket ->
                    ticket[curPos]
                }.all { position -> it.isWithinARange(position) }
            })
        }

        val justnames = ticketClassOrdering.map { it.map { it.name }.toMutableList() }
        var result = 1L
        reduceToSingles(justnames).forEachIndexed { idx, name ->
            if (name.startsWith("departure")) {
                result *= yourTicket[idx]
            }
        }

        return result
    }

    // [[row], [class, row], [class, row, seat]] -> [row, class, seat]
    fun reduceToSingles(input: List<MutableList<String>>): List<String> {
        val singles = input.map { "-" }.toMutableList()
        while (singles.contains("-")){
            input.forEachIndexed { idx, listOfLists ->
                if (listOfLists.size == 1){
                    singles[idx] = listOfLists[0]
                    // all other lists should remove this
                }
            }
            input.forEach {
                it.removeAll(singles)
            }
        }
        println(singles)
        return singles
    }


    class TicketValidity(inputRow: String) {
        var name: String = ""
        val validRanges: MutableList<Pair<Int, Int>> = mutableListOf()

        init {
            name = inputRow.split(":")[0]
            val ranges = inputRow.split(":")[1]
            ("\\d+\\-\\d+").toRegex().findAll(ranges).toList().forEach {
                val (minBound, maxBound) = it.value.split('-')
                validRanges.add(Pair(minBound.toInt(), maxBound.toInt()))
            }
        }

        fun isWithinARange(position: Int): Boolean {
            validRanges.forEach {
                if (position >= it.first && position <= it.second) return true
            }
            return false
        }

        override fun toString(): String {
            return "$name: $validRanges"
        }
    }

}