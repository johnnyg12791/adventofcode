package helpers

abstract class Dimension(val x: Int, val y: Int, val z: Int) {
    abstract fun getNeighbors(): MutableList<Dimension>
}

// Feels like a Quad is really a triple with an extra dimension?, oh well
class Quad(x: Int, y: Int, z: Int, val w: Int) : Dimension(x, y, z) {
    override fun getNeighbors() =
        // This feels.... wrong
        mutableListOf<Dimension>().also {
            (-1..1).forEach { xVal ->
                (-1..1).forEach { yVal ->
                    (-1..1).forEach { zVal ->
                        (-1..1).forEach { wVal ->
                            if (xVal != 0 || yVal != 0 || zVal != 0 || wVal != 0)
                                it.add(Quad(x + xVal, y + yVal, z + zVal, w + wVal))
                        }
                    }
                }
            }
        }

    // This is so I can compare in the Map even if the objects are different
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Quad -> x == other.x && y == other.y && z == other.z && w == other.w
            else -> false
        }
    }
    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        result = 31 * result + z
        result = 31 * result + z
        return result
    }

    override fun toString(): String {
        return "$x,$y,$z,$w"
    }
}

class Triple(x: Int, y: Int, z: Int) : Dimension(x, y, z) {

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Triple -> x == other.x && y == other.y && z == other.z
            else -> false
        }
    }

    override fun getNeighbors() =
        mutableListOf<Dimension>().also {
            (-1..1).forEach { xVal ->
                (-1..1).forEach { yVal ->
                    (-1..1).forEach { zVal ->
                        if (xVal != 0 || yVal != 0 || zVal != 0)
                            it.add(Triple(x + xVal, y + yVal, z + zVal))
                    }
                }
            }
        }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        result = 31 * result + z
        return result
    }

    override fun toString(): String {
        return "$x,$y,$z"
    }
}
