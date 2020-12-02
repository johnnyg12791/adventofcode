import java.io.File

object Util {

    fun readFileToIntList(fileName: String)
            = mutableListOf<Int>().also { File(fileName).forEachLine { line -> it.add(line.toInt()) } }

    fun readFileToIntMap(fileName: String)
            = hashMapOf<Int, Int>().also { File(fileName).forEachLine { line -> it[line.toInt()] = 1 } }

}