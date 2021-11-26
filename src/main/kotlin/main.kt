package seamcarving

import seamcarving.seamcarving.SeamCarving
import java.io.File
import javax.imageio.ImageIO

fun main() {
    println("Type input image path")
    val input = readLine()!!
    println("Type output image path")
    val output = readLine()!!
    println("How many pixels in width would you reduce your image?")
    val widthReduce = readLine()!!.toInt()
    println("How many pixels in height would you reduce your image?")
    val heightReduce = readLine()!!.toInt()

    val image = ImageIO.read(File(input))
    val resizedImage = SeamCarving(image).resize(widthReduce, heightReduce)
}