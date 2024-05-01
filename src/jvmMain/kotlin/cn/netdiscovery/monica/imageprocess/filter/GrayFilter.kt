package cn.netdiscovery.monica.imageprocess.filter

import java.awt.Color
import java.awt.image.BufferedImage

/**
 *
 * @FileName:
 *          cn.netdiscovery.monica.imageprocess.filter.GrayFilter
 * @author: Tony Shen
 * @date: 2024/5/1 10:44
 * @version: V1.0 <描述当前版本功能>
 */
class GrayFilter: BaseFilter() {

    override fun doFilter(image: BufferedImage): BufferedImage {
        val bufferedImage = BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_RGB)

        for (row in 0 until height) {
            for (col in 0 until width) {
                val rgb = image.getRGB(col,row)
                val r = rgb and (0x00ff0000 shr 16)
                val g = rgb and (0x0000ff00 shr 8)
                val b = rgb and 0x000000ff

                val color = (r * 0.299 + g * 0.587 + b * 0.114).toInt()
                bufferedImage.setRGB(col, row, Color(color, color, color).rgb)
            }
        }

        return bufferedImage
    }
}