package cn.netdiscovery.monica.ui.controlpanel.ai.faceswap

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.netdiscovery.monica.state.ApplicationState
import cn.netdiscovery.monica.ui.widget.*
import cn.netdiscovery.monica.utils.chooseImage
import cn.netdiscovery.monica.utils.getBufferedImage
import loadingDisplay
import org.koin.compose.koinInject
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 *
 * @FileName:
 *          cn.netdiscovery.monica.ui.controlpanel.ai.faceswap.FaceSwapView
 * @author: Tony Shen
 * @date: 2024/8/25 13:02
 * @version: V1.0 <描述当前版本功能>
 */
private val logger: Logger = LoggerFactory.getLogger(object : Any() {}.javaClass.enclosingClass)

private var showToast by mutableStateOf(false)
private var toastMessage by mutableStateOf("")

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun faceSwap(state: ApplicationState) {

    val viewModel: FaceSwapViewModel = koinInject()

    val showSwapFaceSettings = remember { mutableStateOf(false) }
    val selectedOption = remember { mutableStateOf(false) }

    PageLifecycle(
        onInit = {
            logger.info("FaceSwapView 启动时初始化")
        },
        onDisposeEffect = {
            logger.info("FaceSwapView 关闭时释放资源")
            viewModel.clearTargetImage()
        }
    )

    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column (modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Row (
                modifier = Modifier.fillMaxSize().padding(top= 20.dp, bottom = 20.dp, end = 90.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Card(
                    modifier = Modifier.padding(10.dp).weight(1.0f),
                    shape = RoundedCornerShape(8.dp),
                    elevation = 4.dp,
                    onClick = {
                        chooseImage(state) { file ->
                            state.rawImage = getBufferedImage(file, state)
                            state.currentImage = state.rawImage
                            state.rawImageFile = file
                        }
                    },
                    enabled = state.currentImage == null
                ) {
                    if (state.currentImage == null) {
                        Text(
                            modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center),
                            text = "请点击选择图像",
                            textAlign = TextAlign.Center
                        )
                    } else {
                        Box {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "source",
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colors.primary,
                                    fontSize = 36.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Image(
                                    painter = state.currentImage!!.toPainter(),
                                    contentDescription = null,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                )
                            }

                            Row(modifier = Modifier.align(Alignment.TopEnd)) {
                                toolTipButton(text = "上一步",
                                    buttonModifier = Modifier,
                                    iconModifier = Modifier.size(20.dp),
                                    painter = painterResource("images/doodle/previous_step.png"),
                                    onClick = {
                                        viewModel.getLastSourceImage(state)
                                    })

                                toolTipButton(text = "检测 source 图中的人脸",
                                    painter = painterResource("images/ai/face_landmark.png"),
                                    buttonModifier = Modifier,
                                    iconModifier = Modifier.size(20.dp),
                                    onClick = {
                                        viewModel.faceLandMark(state, state.currentImage, state.rawImageFile, success = {
                                            state.addQueue(state.currentImage!!)
                                            state.currentImage = it
                                        }, failure = {
                                            showToast("算法服务异常")
                                        })
                                    })

                                toolTipButton(text = "删除 source 的图",
                                    painter = painterResource("images/preview/delete.png"),
                                    buttonModifier = Modifier,
                                    iconModifier = Modifier.size(20.dp),
                                    onClick = {
                                        state.clearImage()
                                    })
                            }
                        }
                    }
                }

                Card(
                    modifier = Modifier.padding(10.dp).weight(1.0f),
                    shape = RoundedCornerShape(8.dp),
                    elevation = 4.dp,
                    onClick = {
                        chooseImage(state) { file ->
                            viewModel.targetImage = getBufferedImage(file)
                            viewModel.targetImageFile = file
                        }
                    },
                    enabled = viewModel.targetImage == null
                ) {
                    if (viewModel.targetImage == null) {
                        Text(
                            modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center),
                            text = "请点击选择图像",
                            textAlign = TextAlign.Center
                        )
                    } else {
                        Box {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "target",
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colors.primary,
                                    fontSize = 36.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Image(
                                    painter = viewModel.targetImage!!.toPainter(),
                                    contentDescription = null,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier)
                            }

                            Row(modifier = Modifier.align(Alignment.TopEnd)) {
                                toolTipButton(text = "上一步",
                                    buttonModifier = Modifier,
                                    iconModifier = Modifier.size(20.dp),
                                    painter = painterResource("images/doodle/previous_step.png"),
                                    onClick = {

                                        if (viewModel.lastTargetImage!=null) {
                                            viewModel.targetImage = viewModel.lastTargetImage
                                        }
                                    })

                                toolTipButton(text = "检测 target 图中的人脸",
                                    painter = painterResource("images/ai/face_landmark.png"),
                                    buttonModifier = Modifier,
                                    iconModifier = Modifier.size(20.dp),
                                    onClick = {
                                        viewModel.faceLandMark(state, viewModel.targetImage, viewModel.targetImageFile, success = {
                                            viewModel.lastTargetImage = viewModel.targetImage
                                            viewModel.targetImage = it
                                        }, failure = {
                                            showToast("算法服务异常")
                                        })
                                    })

                                toolTipButton(text = "删除 target 的图",
                                    painter = painterResource("images/preview/delete.png"),
                                    buttonModifier = Modifier,
                                    iconModifier = Modifier.size(20.dp),
                                    onClick = {
                                        viewModel.clearTargetImage()
                                    })
                            }
                        }
                    }
                }
            }
        }

        rightSideMenuBar(modifier = Modifier.align(Alignment.CenterEnd)) {
            toolTipButton(text = "设置",
                painter = painterResource("images/cropimage/settings.png"),
                iconModifier = Modifier.size(36.dp),
                onClick = {
                    showSwapFaceSettings.value = true
                })

            toolTipButton(text = "人脸替换",
                painter = painterResource("images/ai/face_swap2.png"),
                iconModifier = Modifier.size(36.dp),
                onClick = {

                    if (state.currentImage!=null && viewModel.targetImage!=null) {
                        viewModel.faceSwap(state, state.currentImage, viewModel.targetImage, selectedOption.value, success = {
                            viewModel.lastTargetImage = viewModel.targetImage
                            viewModel.targetImage = it
                        }, failure = {
                            showToast("算法服务异常")
                        })
                    }
                })

            toolTipButton(text = "保存结果",
                painter = painterResource("images/doodle/save.png"),
                iconModifier = Modifier.size(36.dp),
                onClick = {
                    if (viewModel.targetImage!=null) {
                        state.addQueue(state.currentImage!!)
                        state.currentImage = viewModel.targetImage
                    }
                    state.togglePreviewWindow(false)
                })
        }

        if (loadingDisplay) {
            showLoading()
        }

        if (showToast) {
            centerToast(message = toastMessage) {
                showToast = false
            }
        }

        if (showSwapFaceSettings.value) {
            AlertDialog(onDismissRequest = {},
                title = {
                    Text("替换 target 中人脸的数量")
                },
                text = {
                    Column {
                        Row {
                            RadioButton(
                                selected = !selectedOption.value,
                                onClick = { selectedOption.value = false }
                            )
                            Text("替换1个人脸", modifier = Modifier.align(Alignment.CenterVertically))
                        }

                        Row {
                            RadioButton(
                                selected = selectedOption.value,
                                onClick = { selectedOption.value = true }
                            )
                            Text("替换全部的人脸", modifier = Modifier.align(Alignment.CenterVertically))
                        }
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        showSwapFaceSettings.value = false
                    }) {
                        Text("关闭")
                    }
                })
        }
    }
}

private fun showToast(message: String) {
    toastMessage = message
    showToast = true
}