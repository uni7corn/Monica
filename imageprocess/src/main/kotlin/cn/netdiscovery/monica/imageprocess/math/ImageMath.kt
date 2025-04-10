package cn.netdiscovery.monica.imageprocess.math

/**
 *
 * @FileName:
 *          cn.netdiscovery.monica.imageprocess.utils.ImageMath
 * @author: Tony Shen
 * @date:  2025/3/8 15:43
 * @version: V1.0 <描述当前版本功能>
 */

val PI: Float = Math.PI.toFloat()

val HALF_PI = Math.PI.toFloat() / 2.0f

val TWO_PI = Math.PI.toFloat() * 2.0f

/**
 * Return a mod b. This differs from the % operator with respect to negative numbers.
 * @param a the dividend
 * @param b the divisor
 * @return a mod b
 */
fun mod(a: Double, b: Double): Double {
    var a = a
    val n = (a / b).toInt()

    a -= n * b
    if (a < 0) return a + b
    return a
}

/**
 * Return a mod b. This differs from the % operator with respect to negative numbers.
 * @param a the dividend
 * @param b the divisor
 * @return a mod b
 */
fun mod(a: Float, b: Float): Float {
    var a = a
    val n = (a / b).toInt()

    a -= n * b
    if (a < 0) return a + b
    return a
}

/**
 * Return a mod b. This differs from the % operator with respect to negative numbers.
 * @param a the dividend
 * @param b the divisor
 * @return a mod b
 */
fun mod(a: Int, b: Int): Int {
    var a = a
    val n = a / b

    a -= n * b
    if (a < 0) return a + b
    return a
}

/**
 * Linear interpolation of ARGB values.
 * @param t the interpolation parameter
 * @param rgb1 the lower interpolation range
 * @param rgb2 the upper interpolation range
 * @return the interpolated value
 */
fun mixColors(t: Float, rgb1: Int, rgb2: Int): Int {
    var a1 = (rgb1 shr 24) and 0xff
    var r1 = (rgb1 shr 16) and 0xff
    var g1 = (rgb1 shr 8) and 0xff
    var b1 = rgb1 and 0xff
    val a2 = (rgb2 shr 24) and 0xff
    val r2 = (rgb2 shr 16) and 0xff
    val g2 = (rgb2 shr 8) and 0xff
    val b2 = rgb2 and 0xff
    a1 = lerp(t, a1, a2)
    r1 = lerp(t, r1, r2)
    g1 = lerp(t, g1, g2)
    b1 = lerp(t, b1, b2)
    return (a1 shl 24) or (r1 shl 16) or (g1 shl 8) or b1
}

/**
 * Linear interpolation.
 * @param t the interpolation parameter
 * @param a the lower interpolation range
 * @param b the upper interpolation range
 * @return the interpolated value
 */
fun lerp(t: Float, a: Int, b: Int): Int {
    return (a + t * (b - a)).toInt()
}

/**
 * Bilinear interpolation of ARGB values.
 * @param x the X interpolation parameter 0..1
 * @param y the y interpolation parameter 0..1
 * @param rgb array of four ARGB values in the order NW, NE, SW, SE
 * @return the interpolated value
 */
fun bilinearInterpolate(x: Float, y: Float, nw: Int, ne: Int, sw: Int, se: Int): Int {
    var m0: Float
    var m1: Float
    val a0 = (nw shr 24) and 0xff
    val r0 = (nw shr 16) and 0xff
    val g0 = (nw shr 8) and 0xff
    val b0 = nw and 0xff
    val a1 = (ne shr 24) and 0xff
    val r1 = (ne shr 16) and 0xff
    val g1 = (ne shr 8) and 0xff
    val b1 = ne and 0xff
    val a2 = (sw shr 24) and 0xff
    val r2 = (sw shr 16) and 0xff
    val g2 = (sw shr 8) and 0xff
    val b2 = sw and 0xff
    val a3 = (se shr 24) and 0xff
    val r3 = (se shr 16) and 0xff
    val g3 = (se shr 8) and 0xff
    val b3 = se and 0xff

    val cx = 1.0f - x
    val cy = 1.0f - y

    m0 = cx * a0 + x * a1
    m1 = cx * a2 + x * a3
    val a = (cy * m0 + y * m1).toInt()

    m0 = cx * r0 + x * r1
    m1 = cx * r2 + x * r3
    val r = (cy * m0 + y * m1).toInt()

    m0 = cx * g0 + x * g1
    m1 = cx * g2 + x * g3
    val g = (cy * m0 + y * m1).toInt()

    m0 = cx * b0 + x * b1
    m1 = cx * b2 + x * b3
    val b = (cy * m0 + y * m1).toInt()

    return (a shl 24) or (r shl 16) or (g shl 8) or b
}


/**
 * A smoothed step function. A cubic function is used to smooth the step between two thresholds.
 * @param a the lower threshold position
 * @param b the upper threshold position
 * @param x the input parameter
 * @return the output value
 */
fun smoothStep(a: Float, b: Float, x: Float): Float {
    var x = x
    if (x < a) return 0f
    if (x >= b) return 1f
    x = (x - a) / (b - a)
    return x * x * (3 - 2 * x)
}

/**
 * The triangle function. Returns a repeating triangle shape in the range 0..1 with wavelength 1.0
 * @param x the input parameter
 * @return the output value
 */
fun triangle(x: Float): Float {
    val r = mod(x, 1.0f)
    return 2.0f * (if (r < 0.5) r else 1 - r)
}


/**
 * Apply a bias to a number in the unit interval, moving numbers towards 0 or 1
 * according to the bias parameter.
 * @param a the number to bias
 * @param b the bias parameter. 0.5 means no change, smaller values bias towards 0, larger towards 1.
 * @return the output value
 */
fun bias(a: Float, b: Float): Float {
    return a / ((1.0f / b - 2) * (1.0f - a) + 1)
}

/**
 * A variant of the gamma function.
 * @param a the number to apply gain to
 * @param b the gain parameter. 0.5 means no change, smaller values reduce gain, larger values increase gain.
 * @return the output value
 */
fun gain(a: Float, b: Float): Float {
    val c = (1.0f / b - 2.0f) * (1.0f - 2.0f * a)
    return if (a < 0.5) a / (c + 1.0f)
    else (c - a) / (c - 1.0f)
}