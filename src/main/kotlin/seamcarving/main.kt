package seamcarving

import java.io.File
import javax.imageio.ImageIO

fun main() {
    println("Type input image path:")
    val input = readLine()!!
    println("Type output image path:")
    val output = readLine()!!
    println("By how many pixels wide do you want to reduce your image?:")
    val widthReduce = readLine()!!.toInt()
    println("By how many pixels high do you want to reduce your image?")
    val heightReduce = readLine()!!.toInt()

    val image = ImageIO.read(File(input))
    val sc = SeamCarving(image)
    repeat(widthReduce) {
        sc.removeSeam()
    }
    ImageIO.write(sc.image, "png", File("out.png"))
    println("Done!")
}