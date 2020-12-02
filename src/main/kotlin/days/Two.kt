package days

class Two {

    companion object {
        const val fileLocation = "src/main/resources/inputs/two.txt"
    }

    fun printA() =
        Util.readFileToStringList(fileLocation).forEach { line ->
            val passwordLine = PasswordInput(line)
            println(passwordLine.firstNum)
            println(passwordLine.secondNum)
            println(passwordLine.letter)
            println(passwordLine.password)
            println(passwordLine.isValidA())
        }

    fun executeA() =
        Util.readFileToStringList(fileLocation).count { line ->
            PasswordInput(line).isValidA()
        }

    fun executeB() =
        Util.readFileToStringList(fileLocation).count { line ->
            PasswordInput(line).isValidB()
        }


}


class PasswordInput(
    private val inputLine: String
) {
    var firstNum = -1
    var secondNum = -1
    var letter = 'A'
    var password = ""

    init {
        var curStr = ""
        inputLine.forEach { inputChar ->
            if (inputChar == '-') {
                firstNum = curStr.toInt()
                curStr = ""
            } else if (inputChar == ' ' && secondNum == -1) {
                secondNum = curStr.toInt()
                curStr = ""
            } else if (inputChar == ':') {
                letter = curStr.single()
                curStr = ""
            } else {
                curStr += inputChar
            }
        }
        password = curStr.substring(1) // Remove the initial space
    }

    fun isValidA() =
        password.count { it == letter } in (firstNum..secondNum)

    fun isValidB() =
        (password[firstNum-1].toString() + password[secondNum-1]).count{it == letter} == 1

}