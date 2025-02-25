package cn.netdiscovery.monica.utils

import androidx.compose.ui.graphics.toAwtImage
import androidx.compose.ui.graphics.toComposeImageBitmap
import cn.netdiscovery.monica.imageprocess.filter.*
import cn.netdiscovery.monica.imageprocess.filter.blur.*
import cn.netdiscovery.monica.imageprocess.filter.sharpen.LaplaceSharpenFilter
import cn.netdiscovery.monica.imageprocess.filter.sharpen.SharpenFilter
import cn.netdiscovery.monica.imageprocess.filter.sharpen.USMFilter
import cn.netdiscovery.monica.state.ApplicationState
import com.safframework.kotlin.coroutines.IO
import kotlinx.coroutines.withContext
import java.awt.image.BufferedImage

/**
 *
 * @FileName:
 *          cn.netdiscovery.monica.imageprocess.ImageUtils
 * @author: Tony Shen
 * @date: 2024/4/26 22:11
 * @version: V1.0 <描述当前版本功能>
 */

suspend fun doFilter(filterName:String, array:MutableList<Any>, state: ApplicationState):BufferedImage {

    return withContext(IO) {
        when(filterName) {
            "AverageFilter" -> {
                AverageFilter().transform(state.currentImage!!)
            }
            "BilateralFilter" -> {
                BilateralFilter(array[0] as Double,array[1] as Double).transform(state.currentImage!!)
            }
            "BlockFilter" -> {
                BlockFilter(array[0] as Int).transform(state.currentImage!!)
            }
            "BoxBlurFilter" -> {
                BoxBlurFilter(array[0] as Int,array[2] as Int,array[1] as Int).transform(state.currentImage!!)
            }
            "BumpFilter" -> {
                BumpFilter().transform(state.currentImage!!)
            }
            "ColorFilter" -> {
                ColorFilter(array[0] as Int).transform(state.currentImage!!)
            }
            "ConBriFilter" -> {
                ConBriFilter(array[0] as Float,array[1] as Float).transform(state.currentImage!!)
            }
            "CropFilter" -> {
                CropFilter(array[2] as Int,array[3] as Int,array[1] as Int,array[0] as Int).transform(state.currentImage!!)
            }
            "EmbossFilter" -> {
                EmbossFilter(array[0] as Int).transform(state.currentImage!!)
            }
            "GammaFilter" -> {
                GammaFilter(array[0] as Double).transform(state.currentImage!!)
            }
            "FastBlur2D" -> {
                FastBlur2D(array[0] as Int).transform(state.currentImage!!)
            }
            "GaussianFilter" -> {
                GaussianFilter(array[0] as Float).transform(state.currentImage!!.toComposeImageBitmap().toAwtImage())
            }
            "GradientFilter" -> {
                GradientFilter().transform(state.currentImage!!)
            }
            "GrayFilter" -> {
                GrayFilter().transform(state.currentImage!!)
            }
            "HighPassFilter" -> {
                HighPassFilter(array[0] as Float).transform(state.currentImage!!.toComposeImageBitmap().toAwtImage())
            }
            "LaplaceSharpenFilter" -> {
                LaplaceSharpenFilter().transform(state.currentImage!!)
            }
            "MosaicFilter" -> {
                MosaicFilter(array[0] as Int).transform(state.currentImage!!)
            }
            "MotionFilter" -> {
                MotionFilter(array[0] as Float,array[1] as Float,array[2] as Float).transform(state.currentImage!!)
            }
            "NatureFilter" -> {
                NatureFilter(array[0] as Int).transform(state.currentImage!!)
            }
            "OilPaintFilter" -> {
                OilPaintFilter(array[1] as Int,array[0] as Int).transform(state.currentImage!!)
            }
            "SepiaToneFilter" -> {
                SepiaToneFilter().transform(state.currentImage!!)
            }
            "SharpenFilter" -> {
                SharpenFilter().transform(state.currentImage!!)
            }
            "SpotlightFilter" -> {
                SpotlightFilter(array[0] as Int).transform(state.currentImage!!)
            }
            "StrokeAreaFilter" -> {
                StrokeAreaFilter(array[0] as Double).transform(state.currentImage!!)
            }
            "USMFilter" -> {
                USMFilter(array[1] as Float,array[0] as Float,array[2] as Int).transform(state.currentImage!!.toComposeImageBitmap().toAwtImage())
            }
            "VariableBlurFilter"-> {
                VariableBlurFilter(array[0] as Int,array[2] as Int,array[1] as Int).transform(state.currentImage!!)
            }
            "VignetteFilter"-> {
                VignetteFilter(array[0] as Int,array[1] as Int).transform(state.currentImage!!)
            }
            "WhiteImageFilter" -> {
                WhiteImageFilter(array[0] as Double).transform(state.currentImage!!)
            }

            else -> {
                state.currentImage!!
            }
        }
    }
}