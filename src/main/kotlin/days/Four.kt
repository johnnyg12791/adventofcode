package days

import Util.readFileToStringList

class Four {

    companion object {
        const val examplefileLocation = "src/main/resources/inputs/examples/four.txt"
        const val exampleBLocation = "src/main/resources/inputs/examples/four_b.txt"

        const val fileLocation = "src/main/resources/inputs/four.txt"
    }

    fun executeA(): Long {
        var validPassports = 0L
        val requiredFields = listOf("byr:", "iyr:", "eyr:", "hgt:", "hcl:", "ecl:", "pid:")
        var multiline = ""
        val lines = readFileToStringList(fileLocation)
        lines.forEachIndexed { index, line ->
            if (line.isEmpty() || lines.size - 1 == index) {
                if (lines.size - 1 == index) multiline += "$line "
                if (requiredFields.map { multiline.contains(it) }.all { it }) validPassports += 1
                multiline = ""
            } else {
                multiline += "$line "
            }
        }
        return validPassports
    }

    fun executeB(): Long {
        var validPassports = 0L
        var multiline = ""
        val lines = readFileToStringList(fileLocation)
        lines.forEachIndexed { index, line ->
            if (line.isEmpty() || lines.size - 1 == index) {
                if (lines.size - 1 == index) multiline += "$line "
                if (isValidPassport(multiline)) {
                    validPassports += 1
                }
                multiline = ""
            } else {
                multiline += "$line "
            }
        }
        return validPassports
    }

    private fun yearValid(passport: String, regex: Regex, minYear: Int, maxYear: Int): Boolean {
        (regex).find(passport)?.value?.let {
            val year = it.takeLast(4).toInt()
            if (year < minYear || year > maxYear) return false
        } ?: return false
        return true
    }

    private fun isHeightValid(passport: String): Boolean {
        ("hgt:[0-9]+[a-z]+".toRegex()).find(passport)?.value?.let {
            val height = it.substring(4, it.length - 2).toInt()
            if (it.takeLast(2) == "cm") {
                if (height < 150 || height > 193) return false
            } else if (it.takeLast(2) == "in") {
                if (height < 59 || height > 76) return false
            }
        } ?: return false
        return true
    }

    private fun isValidPassport(passport: String): Boolean {
        if (!yearValid(passport, "byr:[0-9]+".toRegex(), 1920, 2002)) return false
        if (!yearValid(passport, "iyr:[0-9]+".toRegex(), 2010, 2020)) return false
        if (!yearValid(passport, "eyr:[0-9]+".toRegex(), 2020, 2030)) return false

        if (!isHeightValid(passport)) return false

        if (("hcl:#[0-9a-f]{6}".toRegex()).find(passport) == null) return false

        ("ecl:[a-z]{3}".toRegex()).find(passport)?.value?.let {
            val validEyeColors = setOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
            if (!validEyeColors.contains(it.trim().takeLast(3))) return false
        } ?: return false

        if (("pid:[0-9]{9} ".toRegex()).find(passport) == null) return false

        return true
    }

}
