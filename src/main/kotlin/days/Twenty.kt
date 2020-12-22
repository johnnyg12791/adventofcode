package days

import Util.readFileToStringList

class Twenty {

    private val examplefileLocation = "src/main/resources/inputs/examples/20.txt"
    private val fileLocation = "src/main/resources/inputs/20.txt"
    private var input = readFileToStringList(fileLocation)

    fun executeA(): Long {
        val tileNumToImg = mutableMapOf<Int, Image>()
        val imgRows = mutableListOf<String>()
        var imgNum = 0
        input.forEach { line ->
            if (line.isBlank()) {
                tileNumToImg[imgNum] = Image(imgRows.toList(), imgNum)
                imgNum = 0
                imgRows.clear()
            } else if (line.startsWith("Tile ")) {
                imgNum = line.substringAfter("Tile ").replace(":", "").toInt()
            } else {
                imgRows.add(line)
            }
        }
        tileNumToImg[imgNum] = Image(imgRows.toList(), imgNum)

        val tileNumToMatches = fitImagesToSquare(tileNumToImg)
        var returnVal = 1L
        tileNumToMatches.filterValues { it == 2 }.keys.forEach { returnVal *= it }
        return returnVal
    }

    fun fitImagesToSquare(tileNumToImg: Map<Int, Image>): MutableMap<Int, Int> {
        val square = mutableListOf<MutableList<Int>>() // Returning a 2D list of lists. Tile numbers that are in the squares
        // Take the first tile. Find all tiles that match it left and right and up and down??
        // Take the second tile try and do the same thing??
        val tileNumToMatches = mutableMapOf<Int, Int>()
        tileNumToImg.forEach { (tileNum, img) ->
            // Check all other images
            var numMatches = 0
            // Assuming no images are duplicates???
            tileNumToImg.keys.filter { it != tileNum }.mapNotNull { tileNumToImg[it] }.forEach { otherImage ->
                if (img.borderMatches(otherImage) > 0){
                    numMatches += 1
                    img.neighbors.add(otherImage)
                }
            }
            tileNumToMatches[tileNum] = numMatches
        }
        println(tileNumToMatches)
        return tileNumToMatches

    }

    fun executeB(): Long {
        val tileNumToImg = mutableMapOf<Int, Image>()
        val imgRows = mutableListOf<String>()
        var imgNum = 0
        input.forEach { line ->
            if (line.isBlank()) {
                tileNumToImg[imgNum] = Image(imgRows.toList(), imgNum)
                imgNum = 0
                imgRows.clear()
            } else if (line.startsWith("Tile ")) {
                imgNum = line.substringAfter("Tile ").replace(":", "").toInt()
            } else {
                imgRows.add(line)
            }
        }
        tileNumToImg[imgNum] = Image(imgRows.toList(), imgNum)

        val tileNumToMatches = fitImagesToSquare(tileNumToImg)
        // Now I need to "construct" the image
        val corners = tileNumToMatches.filterValues { it == 2 }.keys
        val edges = tileNumToMatches.filterValues { it == 3 }.keys
        val middle = tileNumToMatches.filterValues { it == 4 }.keys
        println(corners)
        // build the outside of the map
        buildOuterEdge(tileNumToImg, corners, edges)
        return 0L
    }


    fun buildOuterEdge(tileNumToImg: Map<Int, Image>, corners: Set<Int>, edges: Set<Int>){
        // For each corner image
        // match with the edges?
        tileNumToImg.filterKeys { corners.contains(it) }.forEach{ (tileNum, img) ->
            // set fields on image?
            println(img.neighbors)
            reorient(img, img.neighbors)
        }
    }

    fun reorient(img1: Image, img2: List<Image>){
        //...

    }

    // Corners will only match one on the column, one on the row.
    // Rest of the pieces will match two on column or two on row
    // Is that enough of a hack?? (Ans: Yes)
    class Image(val rows: List<String>, val imgNum: Int){

        val rowOptions = mutableListOf<String>()
        val colOptions = mutableListOf<String>()
        val borderOptions = mutableListOf<String>()
        val neighbors = mutableListOf<Image>()
        var positon = Pair(-1, -1)

        init {
            rowOptions.add(rows[0])
            rowOptions.add(rows[0].reversed())
            rowOptions.add(rows[rows.size-1])
            rowOptions.add(rows[rows.size-1].reversed())

            val firstCol = rows.map { it[0] }.joinToString("")
            val lastCol = rows.map { it[it.length-1] }.joinToString("")
            colOptions.add(firstCol)
            colOptions.add(firstCol.reversed())
            colOptions.add(lastCol)
            colOptions.add(lastCol.reversed())

            borderOptions.addAll(colOptions)
            borderOptions.addAll(rowOptions)
        }

        override fun toString(): String {
            return "\n" + rows.joinToString("\n") + "\n"
        }

        fun borderMatches(otherImg: Image) =
            otherImg.borderOptions.intersect(borderOptions).size


        fun numColMatches(otherImg: Image) =
            // Is there a top border that matches?
            otherImg.colOptions.intersect(colOptions).size


        fun numRowMatches(otherImg: Image) =
            // Is there a top border that matches?
            otherImg.rowOptions.intersect(rowOptions).size

    }

}
