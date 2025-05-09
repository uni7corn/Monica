package cn.netdiscovery.monica.ui.controlpanel.ai.experiment.viewmodel

import cn.netdiscovery.monica.opencv.ImageProcess
import cn.netdiscovery.monica.manager.OpenCVManager
import cn.netdiscovery.monica.state.ApplicationState
import cn.netdiscovery.monica.utils.extensions.launchWithLoading
import cn.netdiscovery.monica.utils.logger
import org.slf4j.Logger

/**
 *
 * @FileName:
 *          cn.netdiscovery.monica.ui.controlpanel.ai.experiment.viewmodel.ImageEnhanceViewModel
 * @author: Tony Shen
 * @date: 2024/7/17 21:33
 * @version: V1.0 <描述当前版本功能>
 */
class ImageEnhanceViewModel {
    private val logger: Logger = logger<ImageEnhanceViewModel>()

    fun equalizeHist(state: ApplicationState) {
        state.scope.launchWithLoading {
            OpenCVManager.invokeCV(state, action = { byteArray ->
                ImageProcess.equalizeHist(byteArray)
            }, failure = { e ->
                logger.error("equalizeHist is failed", e)
            })
        }
    }

    fun clahe(state: ApplicationState, clipLimit:Double, size:Int) {
        state.scope.launchWithLoading {
            OpenCVManager.invokeCV(state, action = { byteArray ->
                ImageProcess.clahe(byteArray, clipLimit, size)
            }, failure = { e ->
                logger.error("clahe is failed", e)
            })
        }
    }

    fun gammaCorrection(state: ApplicationState, gamma:Float) {
        state.scope.launchWithLoading {
            OpenCVManager.invokeCV(state, action = { byteArray ->
                ImageProcess.gammaCorrection(byteArray, gamma)
            }, failure = { e ->
                logger.error("gammaCorrection is failed", e)
            })
        }
    }

    fun laplaceSharpening(state: ApplicationState) {
        state.scope.launchWithLoading {
            OpenCVManager.invokeCV(state, action = { byteArray ->
                ImageProcess.laplaceSharpening(byteArray)
            }, failure = { e ->
                logger.error("laplace is failed", e)
            })
        }
    }

    fun unsharpMask(state: ApplicationState,radius:Int,threshold:Int,amount:Int) {
        state.scope.launchWithLoading {
            OpenCVManager.invokeCV(state, action = { byteArray ->
                ImageProcess.unsharpMask(byteArray,radius,threshold,amount)
            }, failure = { e ->
                logger.error("unsharpMask is failed", e)
            })
        }
    }

    fun ace(state: ApplicationState, ratio:Int, radius:Int) {
        state.scope.launchWithLoading {
            OpenCVManager.invokeCV(state, action = { byteArray ->
                ImageProcess.ace(byteArray,ratio,radius)
            }, failure = { e ->
                logger.error("ace is failed", e)
            })
        }
    }
}