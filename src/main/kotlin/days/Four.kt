package days

import Util.readFileToStringList

class Four {

    companion object {
        const val examplefileLocation = "src/main/resources/inputs/examples/four.txt"
        const val fileLocation = "src/main/resources/inputs/four.txt"
    }

    fun executeA(): Long {
        var validPassports = 0L
        val requiredFields = listOf("byr:", "iyr:", "eyr:", "hgt:", "hcl:", "ecl:", "pid:")
        var multiline = ""
        val lines = readFileToStringList(fileLocation)
        lines.forEachIndexed { index, line ->
            if (line.isEmpty() || lines.size-1 == index) {
                if (lines.size-1 == index) multiline += "$line "
                println(multiline)
                println(requiredFields.map { multiline.contains(it) })
                if (requiredFields.map { multiline.contains(it) }.all { it }) validPassports += 1
                multiline = ""
            } else {
                multiline += "$line "
            }
        }
        return validPassports
    }

    fun executeB(): Long {
        return 0
    }

}
