package days

import Util.readFileToStringList
import kotlin.math.min

class TwentyTwo {

    companion object {
        const val PLAYER_1 = "Player1"
        const val PLAYER_2 = "Player2"
    }

    private val examplefileLocation = "src/main/resources/inputs/examples/22.txt"
    private val fileLocation = "src/main/resources/inputs/22.txt"
    private var input = readFileToStringList(fileLocation)

    val player1Hand = mutableListOf<Int>()
    val player2Hand = mutableListOf<Int>()

    fun executeA(): Long {
        readInputToHands()
        println(player1Hand)
        println(player2Hand)
        playGameA()
        //
        return computeHandScores().toLong()
    }

    fun computeHandScores() =
        player2Hand.mapIndexed { index, card ->
            (player2Hand.size - index) * card
        }.sum() +
            player1Hand.mapIndexed { index, card ->
                (player1Hand.size - index) * card
            }.sum()


    fun playGameA() {
        var iter = 0
        while (!(player1Hand.size == 0 || player2Hand.size == 0)) {
            val player1Card = player1Hand.removeFirst()
            val player2Card = player2Hand.removeFirst()
            if (player1Card > player2Card) {
                player1Hand.addAll(listOf(player1Card, player2Card))
            } else {
                player2Hand.addAll(listOf(player2Card, player1Card))
            }
            iter += 1
            //println("Player $iter hands")
        }
    }

    fun readInputToHands() {
        var isPlayerTwo = false
        input.forEach { line ->
            if (line == "Player 1:") {
                isPlayerTwo = false
            } else if (line == "Player 2:") {
                isPlayerTwo = true
            } else if (line.contains("\\d".toRegex())) {
                if (isPlayerTwo) {
                    player2Hand.add(line.toInt())
                } else {
                    player1Hand.add(line.toInt())
                }
            }
        }
    }

    fun executeB(): Long {
        player1Hand.clear()
        player2Hand.clear()
        readInputToHands()
        println(player1Hand)
        println(player2Hand)
        playGameB(player1Hand, player2Hand, mutableListOf())
        return computeHandScores().toLong()
    }

    // Returns the winner of the game
    // handsSeen is a list of the handSeen, and a handSeen is a list<
    fun recursiveCombat(player1Card: Int, player2Card: Int, player1Hand: MutableList<Int>, player2Hand: MutableList<Int>,
                        handsSeen: MutableList<Pair<List<Int>, List<Int>>>): String {
        if (handsSeen.contains(Pair(player1Hand, player2Hand))) {
            // Prevent infinite loops
            return PLAYER_1
        } else if (player1Hand.size >= player1Card && player2Hand.size > player2Card) {
            // Recursive Case
            // handsSeen.add(Pair(player1Hand, player2Hand))
            val updatedPlayer1Hand = player1Hand.subList(0, min(player1Card, player1Hand.size))
            val updatedPlayer2Hand = player2Hand.subList(0, min(player2Card, player2Hand.size))
//            val newPlayer1Card = updatedPlayer1Hand.removeFirst()
//            val newPlayer2Card = updatedPlayer2Hand.removeFirst()
            return playGameB(
                updatedPlayer1Hand.toMutableList(),
                updatedPlayer2Hand.toMutableList(), mutableListOf())
        } else {
            handsSeen.add(Pair(player1Hand, player2Hand))
            // Base case
            if (player1Card > player2Card) {
                return PLAYER_1
            } else {
                return PLAYER_2
            }
        }
    }

    fun playGameB(hand1: MutableList<Int>, hand2: MutableList<Int>, handsSeen: MutableList<Pair<List<Int>, List<Int>>>): String {
        var iter = 0
        while (!(hand1.size == 0 || hand2.size == 0)) {
            val player1Card = hand1.removeFirst()
            val player2Card = hand2.removeFirst()

            val winner = recursiveCombat(player1Card, player2Card,
                hand1.toMutableList(),
                hand2.toMutableList(), handsSeen)
            //If both players have at least as many cards remaining in their deck as the value of the card they just drew,
            // the winner of the round is determined by playing a new game of Recursive Combat (see below).
            if (winner == PLAYER_1){
                hand1.addAll(listOf(player1Card, player2Card))
            } else {
                hand2.addAll(listOf(player2Card, player1Card))
            }
            iter += 1
            println("Played $iter hands")
            //println(handsSeen)
//            println(hand1)
//            println(hand2)
        }
        if (hand1.size > hand2.size) return PLAYER_1
        else return PLAYER_2
    }


}