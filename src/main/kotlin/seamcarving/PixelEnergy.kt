package seamcarving

import java.awt.Color
import java.awt.image.BufferedImage
import kotlin.math.pow
import kotlin.math.sqrt

class PixelEnergy {
    companion object {
        // Calculating pixel energy value using dual-gradient energy function
        fun BufferedImage.calcPixelEnergy(x: Int, y: Int): Double {
            val x1 = if (x == 0) 1 else if (x == this.width - 1) x - 1 else x
            val y1 = if (y == 0) 1 else if (y == this.height - 1) y - 1 else y
            val a = Color(this.getRGB(x1 - 1, y))
            val b = Color(this.getRGB(x1 + 1, y))
            val c = Color(this.getRGB(x, y1 - 1))
            val d = Color(this.getRGB(x, y1 + 1))

            return sqrt(calcGradient(a, b) + calcGradient(c, d))
        }

        private fun calcGradient(rgb1: Color, rgb2: Color): Double {
            return (rgb1.red - rgb2.red).toDouble().pow(2.0) +
                    (rgb1.green - rgb2.green).toDouble().pow(2.0) +
                    (rgb1.blue - rgb2.blue).toDouble().pow(2.0)
        }
    }
}
