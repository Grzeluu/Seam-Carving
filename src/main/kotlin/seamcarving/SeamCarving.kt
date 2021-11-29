package seamcarving

import seamcarving.PixelEnergy.Companion.calcPixelEnergy
import java.awt.image.BufferedImage

class SeamCarving(var image: BufferedImage) {
    // Map representing the energy of each pixel energy by pair of its coordinates and energy
    private val energy = mutableMapOf<Pair<Int, Int>, Double>()

    // Map representing the distance of each pixel by pair of its coordinates and distance of path to the end of the image
    private val distance = mutableMapOf<Pair<Int, Int>, Double>()

    init {
        refreshData()
    }

    private fun refreshData() {
        energy.clear()
        distance.clear()
        for (x in 0 until image.width) {
            for (y in 0 until image.height) {
                energy[Pair(x, y)] = image.calcPixelEnergy(x, y)
                distance[Pair(x, y)] = Double.MAX_VALUE
            }
        }

    }

    private fun calculateVerticalDistances() {
        //All pixels in the top row have starting distance equivalent to their energy - the beginning of the path
        distance.putAll(energy.filterKeys { it.second == 0 })

        for (y in 0 until image.height - 1) {
            var queue = mutableMapOf<Pair<Int, Int>, Double>()
            queue.putAll(distance.filterKeys { it.second == y })
            queue.toList().sortedBy { (_, value) -> value }.toMap().toMutableMap().also { queue = it }
            for (pixel in queue) {
                for (neighbour in getNeighborsX(pixel.key.first, pixel.key.second + 1)) {
                    val tmpDst = distance[pixel.key]!! + energy[neighbour]!!
                    if (tmpDst < distance[neighbour]!!) distance[neighbour] = tmpDst
                }
            }
        }
    }

    private fun calculateHorizontalDistances() {
        //All pixels in the left row have starting distance equivalent to their energy - the beginning of the path
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

    fun resize(width: Int, height: Int) {
        repeat(width) {
            calculateVerticalDistances()
            val seam = mutableListOf<Pair<Int, Int>>()
            seam.add(
                distance.filterKeys { it.second == image.height - 1 }.toList()
                    .minByOrNull { (_, value) -> value }!!.first
            )
            for (y in image.height - 1 downTo 1) {
                seam.add(getNeighborsX(seam.last().first, seam.last().second - 1).minByOrNull { distance[it]!! }!!)
            }
            image = image.removeVerticalSeam(seam)
            refreshData()
        }
        repeat(height) {
            calculateHorizontalDistances()
            val seam = mutableListOf<Pair<Int, Int>>()
            seam.add(
                distance.filterKeys { it.first == image.width - 1 }.toList()
                    .minByOrNull { (_, value) -> value }!!.first
            )
            for (x in image.width - 1 downTo 1) {
                seam.add(getNeighborsY(seam.last().first - 1, seam.last().second).minByOrNull { distance[it]!! }!!)
            }
            image = image.removeHorizontalSeam(seam)
            refreshData()
        }
    }


    private fun BufferedImage.removeVerticalSeam(seam: MutableList<Pair<Int, Int>>): BufferedImage {
        val resizedImage = BufferedImage(this.width - 1, this.height, BufferedImage.TYPE_INT_RGB)
        for (y in 0 until this.height) {
            var newX = 0
            for (x in 0 until this.width) {
                if (!seam.contains(Pair(x, y))) {
                    resizedImage.setRGB(newX, y, this.getRGB(x, y))
                    newX++
                }
            }
        }
        return resizedImage
    }

    private fun BufferedImage.removeHorizontalSeam(seam: MutableList<Pair<Int, Int>>): BufferedImage {
        val resizedImage = BufferedImage(this.width, this.height - 1, BufferedImage.TYPE_INT_RGB)
        for (x in 0 until this.width) {
            var newY = 0
            for (y in 0 until this.height) {
                if (!seam.contains(Pair(x, y))) {
                    resizedImage.setRGB(x, newY, this.getRGB(x, y))
                    newY++
                }
            }
        }
        return resizedImage
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
}
