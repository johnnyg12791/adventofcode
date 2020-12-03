import days.One
import days.Three
import days.Two

fun main(args: Array<String>) {
    val day = Three()

    //day.printA()

    val resultA = day.executeA()
    println(resultA)
    val resultB = day.executeB()
    println(resultB)
}