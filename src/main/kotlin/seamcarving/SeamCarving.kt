package seamcarving

import seamcarving.PixelEnergy.Companion.calcPixelEnergy
import java.awt.image.BufferedImage

class SeamCarving(val image: BufferedImage) {
    // Map representing the energy of each pixel energy by pair of its coordinates and energy
    private val energy = mutableMapOf<Pair<Int, Int>, Double>()

    // Map representing the distance of each pixel by pair of its coordinates and distance of path to the end of the image
    private val distance = mutableMapOf<Pair<Int, Int>, Double>()

    init {
        for (x in 0 until image.width) {
            for (y in 0 until image.height) {
                energy[Pair(x, y)] = image.calcPixelEnergy(x, y)
                distance[Pair(x, y)] = Double.MAX_VALUE
            }
        }
        calculateDistances()
    }

    private fun calculateDistances() {
        //All pixels in the top row have 0 distance - beginning of the path
        distance.putAll(energy.filterKeys { it.first == 0 })

        for (x in 0 until image.width - 1) {
            var queue = mutableMapOf<Pair<Int, Int>, Double>()
            queue.putAll(distance.filterKeys { it.first == x })
            queue.toList().sortedBy { (_, value) -> value }.toMap().toMutableMap().also { queue = it }
            for (pixel in queue) {
                for (neighbour in getNeighborsY(pixel.key.first + 1, pixel.key.second)) {
                    val tmpDst = distance[pixel.key]!! + energy[neighbour]!!
                    if (tmpDst < distance[neighbour]!!) distance[neighbour] = tmpDst
                }
            }
        }
    }

    private fun getNeighborsX(x: Int, y: Int): List<Pair<Int, Int>> {
        val neighbors = mutableListOf(Pair(x, y))

        if (x != 0) neighbors.add(Pair(x - 1, y))
        if (x != image.width - 1) neighbors.add(Pair(x + 1, y))

        return neighbors.toList()
    }

    private fun getNeighborsY(x: Int, y: Int): List<Pair<Int, Int>> {
        val neighbors = mutableListOf(Pair(x, y))

        if (y != 0) neighbors.add(Pair(x, y - 1))
        if (y != image.height - 1) neighbors.add(Pair(x, y + 1))

        return neighbors.toList()
    }

    fun resize(widthReduce: Int, heightReduce: Int): BufferedImage {
        return image
        TODO("Implement")
    }
}
