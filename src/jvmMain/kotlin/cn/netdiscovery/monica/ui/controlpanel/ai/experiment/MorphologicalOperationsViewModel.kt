package cn.netdiscovery.monica.ui.controlpanel.ai.experiment

import cn.netdiscovery.monica.imageprocess.image2ByteArray
import cn.netdiscovery.monica.opencv.ImageProcess
import cn.netdiscovery.monica.opencv.OpenCVManager
import cn.netdiscovery.monica.state.ApplicationState
import cn.netdiscovery.monica.ui.controlpanel.ai.experiment.model.MorphologicalOperationSettings
import cn.netdiscovery.monica.utils.extension.launchWithLoading
import cn.netdiscovery.monica.utils.logger
import com.safframework.rxcache.utils.GsonUtils
import org.slf4j.Logger
import java.awt.image.BufferedImage

/**
 *
 * @FileName:
 *          cn.netdiscovery.monica.ui.controlpanel.ai.experiment.MorphologicalOperationsViewModel
 * @author: Tony Shen
 * @date: 2024/12/26 20:21
 * @version: V1.0 <描述当前版本功能>
 */
class MorphologicalOperationsViewModel {
    private val logger: Logger = logger<MorphologicalOperationsViewModel>()

    fun morphologyEx(state: ApplicationState, morphologicalOperationSettings: MorphologicalOperationSettings) {

        logger.info("morphologicalOperationSettings = ${GsonUtils.toJson(morphologicalOperationSettings)}")

        state.scope.launchWithLoading {
            OpenCVManager.invokeCV(state, type = BufferedImage.TYPE_BYTE_BINARY, action = { byteArray ->

                ImageProcess.morphologyEx(byteArray, morphologicalOperationSettings)
            }, failure = { e ->
                logger.error("contourAnalysis is failed", e)
            })
        }
    }
}