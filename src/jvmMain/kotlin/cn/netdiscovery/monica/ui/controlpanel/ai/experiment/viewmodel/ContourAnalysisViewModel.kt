package cn.netdiscovery.monica.ui.controlpanel.ai.experiment.viewmodel

import cn.netdiscovery.monica.manager.OpenCVManager
import cn.netdiscovery.monica.state.ApplicationState
import cn.netdiscovery.monica.domain.ContourDisplaySettings
import cn.netdiscovery.monica.domain.ContourFilterSettings
import cn.netdiscovery.monica.imageprocess.utils.extension.image2ByteArray
import cn.netdiscovery.monica.opencv.ImageProcess
import cn.netdiscovery.monica.utils.extensions.launchWithLoading
import cn.netdiscovery.monica.utils.logger
import com.safframework.rxcache.utils.GsonUtils
import org.slf4j.Logger
import java.awt.image.BufferedImage

/**
 *
 * @FileName:
 *          cn.netdiscovery.monica.ui.controlpanel.ai.experiment.viewmodel.ContourAnalysisViewModel
 * @author: Tony Shen
 * @date: 2024/10/26 13:54
 * @version: V1.0 <描述当前版本功能>
 */
class ContourAnalysisViewModel {
    private val logger: Logger = logger<ContourAnalysisViewModel>()

    fun contourAnalysis(state: ApplicationState, contourFilterSettings: ContourFilterSettings, contourDisplaySettings: ContourDisplaySettings) {

        logger.info("contourFilterSettings = ${GsonUtils.toJson(contourFilterSettings)}")
        logger.info("contourDisplaySettings = ${GsonUtils.toJson(contourDisplaySettings)}")

        val type = if (contourDisplaySettings.showOriginalImage) { BufferedImage.TYPE_INT_ARGB } else BufferedImage.TYPE_BYTE_BINARY

        state.scope.launchWithLoading {
            OpenCVManager.invokeCV(state, type = type, action = { byteArray ->
                val srcByteArray = state.rawImage!!.image2ByteArray()

                val scalar = state.toOutputBoxScalar()

                ImageProcess.contourAnalysis(srcByteArray, byteArray, scalar, contourFilterSettings, contourDisplaySettings)
            }, failure = { e ->
                logger.error("contourAnalysis is failed", e)
            })
        }
    }
}