package cn.netdiscovery.monica.ui.controlpanel.ai.experiment

import cn.netdiscovery.monica.opencv.ImageProcess
import cn.netdiscovery.monica.opencv.OpenCVManager
import cn.netdiscovery.monica.state.ApplicationState
import cn.netdiscovery.monica.utils.logger
import org.slf4j.Logger
import java.awt.image.BufferedImage

/**
 *
 * @FileName:
 *          cn.netdiscovery.monica.ui.controlpanel.ai.experiment.BinaryImageAnalysisViewModel
 * @author: Tony Shen
 * @date: 2024/10/7 16:07
 * @version: V1.0 <描述当前版本功能>
 */
class BinaryImageAnalysisViewModel {
    private val logger: Logger = logger<BinaryImageAnalysisViewModel>()

    fun cvtGray(state: ApplicationState) {

        OpenCVManager.invokeCV(state, type = BufferedImage.TYPE_BYTE_GRAY, action = { byteArray ->
            ImageProcess.cvtGray(byteArray)
        }, failure = { e ->
            logger.error("cvtGray is failed", e)
        })
    }

    fun binary(state: ApplicationState, typeSelect: String, thresholdSelect: String) {

        val iTypeSelect = when(typeSelect) {
            "THRESH_BINARY" -> 0
            "THRESH_BINARY_INV" -> 1
            else -> 0
        }

        val iThresholdSelect = when(thresholdSelect) {
            "THRESH_OTSU" -> 8
            "THRESH_TRIANGLE" -> 16
            else -> 8
        }

        OpenCVManager.invokeCV(state, type = BufferedImage.TYPE_BYTE_BINARY, action = { byteArray ->
            ImageProcess.binary(byteArray, iTypeSelect, iThresholdSelect)
        }, failure = { e ->
            logger.error("cvtGray is failed", e)
        })
    }
}