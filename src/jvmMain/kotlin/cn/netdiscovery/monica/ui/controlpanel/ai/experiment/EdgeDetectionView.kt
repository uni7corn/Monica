package cn.netdiscovery.monica.ui.controlpanel.ai.experiment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.netdiscovery.monica.state.ApplicationState
import cn.netdiscovery.monica.ui.widget.divider
import cn.netdiscovery.monica.ui.widget.subTitle
import cn.netdiscovery.monica.utils.composeClick
import org.koin.compose.koinInject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.awt.image.BufferedImage

/**
 *
 * @FileName:
 *          cn.netdiscovery.monica.ui.controlpanel.ai.experiment.EdgeDetectionView
 * @author: Tony Shen
 * @date:  2024/10/13 22:17
 * @version: V1.0 <描述当前版本功能>
 */
private val logger: Logger = LoggerFactory.getLogger(object : Any() {}.javaClass.enclosingClass)

val firstDerivativeOperatorTags = arrayListOf("Roberts算子", "Prewitt算子", "Sobel算子")
val secondDerivativeOperatorTags = arrayListOf("Laplace算子","LoG算子")


@Composable
fun edgeDetection(state: ApplicationState) {
    val viewModel: EdgeDetectionViewModel = koinInject()

    var firstDerivativeOperatorSelectedOption = remember { mutableStateOf("Null") }
    var secondDerivativeOperatorSelectedOption = remember { mutableStateOf("Null") }

    var threshold1Text = remember { mutableStateOf("") }
    var threshold2Text = remember { mutableStateOf("") }
    var apertureSizeText = remember { mutableStateOf("3") }

    fun clearCannyParams() {
        threshold1Text.value = ""
        threshold2Text.value = ""
        apertureSizeText.value = "3"
    }

    Column (modifier = Modifier.fillMaxSize().padding(start = 20.dp, end =  20.dp, top = 20.dp)) {
        Column{
            subTitle(text = "边缘检测算子", color = Color.Black)
            divider()

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(state.isFirstDerivativeOperator, onCheckedChange = {
                    state.isFirstDerivativeOperator = it

                    if (!state.isFirstDerivativeOperator) {
                        firstDerivativeOperatorSelectedOption.value = "Null"
                    } else {
                        state.isSecondDerivativeOperator = false
                        state.isCannyOperator = false
                        clearCannyParams()
                    }
                })
                Text("一阶导数算子", modifier = Modifier.align(Alignment.CenterVertically))
            }

            Row {
                firstDerivativeOperatorTags.forEach {
                    RadioButton(
                        selected = (state.isFirstDerivativeOperator && it == firstDerivativeOperatorSelectedOption.value),
                        onClick = {
                            firstDerivativeOperatorSelectedOption.value = it
                        }
                    )

                    Text(text = it, modifier = Modifier.align(Alignment.CenterVertically))
                }

                Column(modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center) {
                    Button(
                        onClick = composeClick {
                            when(firstDerivativeOperatorSelectedOption.value) {
                                "Roberts算子" -> viewModel.roberts(state)
                                "Prewitt算子" -> viewModel.prewitt(state)
                                "Sobel算子"   -> viewModel.sobel(state)
                                else         -> {}
                            }

                        }
                    ) {
                        Text(text = "一阶导数算子边缘检测", color = Color.Unspecified)
                    }
                }
            }

            Row(modifier = Modifier.padding(top = 10.dp),verticalAlignment = Alignment.CenterVertically) {
                Checkbox(state.isSecondDerivativeOperator, onCheckedChange = {
                    state.isSecondDerivativeOperator = it

                    if (!state.isSecondDerivativeOperator) {
                        secondDerivativeOperatorSelectedOption.value = "Null"
                    } else {
                        state.isFirstDerivativeOperator = false
                        state.isCannyOperator = false
                        clearCannyParams()
                    }
                })
                Text("二阶导数算子", modifier = Modifier.align(Alignment.CenterVertically))
            }

            Row {
                secondDerivativeOperatorTags.forEach {
                    RadioButton(
                        selected = (state.isSecondDerivativeOperator && it == secondDerivativeOperatorSelectedOption.value),
                        onClick = {
                            secondDerivativeOperatorSelectedOption.value = it
                        }
                    )

                    Text(text = it, modifier = Modifier.align(Alignment.CenterVertically))
                }

                Column(modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center) {
                    Button(
                        onClick = composeClick {
                            when(secondDerivativeOperatorSelectedOption.value) {
                                "Laplace算子" -> viewModel.laplace(state)
                                "LoG算子"     -> viewModel.log(state)
                                else         -> {}
                            }
                        }
                    ) {
                        Text(text = "二阶导数算子边缘检测", color = Color.Unspecified)
                    }
                }
            }

            Row(modifier = Modifier.padding(top = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                Checkbox(state.isCannyOperator, onCheckedChange = {
                    state.isCannyOperator = it

                    if (!state.isCannyOperator) {
                        clearCannyParams()
                    } else {
                        state.isFirstDerivativeOperator = false
                        state.isSecondDerivativeOperator = false
                    }
                })
                Text("Canny算子", modifier = Modifier.align(Alignment.CenterVertically))
            }

            Row {
                Text(text = "threshold1")

                BasicTextField(
                    value = threshold1Text.value,
                    onValueChange = { str ->
                        if (state.isCannyOperator) {
                            threshold1Text.value = str
                        }
                    },
                    keyboardOptions = KeyboardOptions.Default,
                    keyboardActions = KeyboardActions.Default,
                    cursorBrush = SolidColor(Color.Gray),
                    singleLine = true,
                    modifier = Modifier.padding(start = 10.dp).width(120.dp).background(Color.LightGray.copy(alpha = 0.5f), shape = RoundedCornerShape(3.dp)).height(20.dp),
                    textStyle = TextStyle(Color.Black, fontSize = 12.sp)
                )
            }

            Row(modifier = Modifier.padding(top = 10.dp)) {
                Text(text = "threshold2")

                BasicTextField(
                    value = threshold2Text.value,
                    onValueChange = { str ->
                        if (state.isCannyOperator) {
                            threshold2Text.value = str
                        }
                    },
                    keyboardOptions = KeyboardOptions.Default,
                    keyboardActions = KeyboardActions.Default,
                    cursorBrush = SolidColor(Color.Gray),
                    singleLine = true,
                    modifier = Modifier.padding(start = 10.dp).width(120.dp).background(Color.LightGray.copy(alpha = 0.5f), shape = RoundedCornerShape(3.dp)).height(20.dp),
                    textStyle = TextStyle(Color.Black, fontSize = 12.sp)
                )
            }

            Row(modifier = Modifier.padding(top = 10.dp)) {
                Text(text = "apertureSize")

                BasicTextField(
                    value = apertureSizeText.value,
                    onValueChange = { str ->
                        if (state.isCannyOperator) {
                            apertureSizeText.value = str
                        }
                    },
                    keyboardOptions = KeyboardOptions.Default,
                    keyboardActions = KeyboardActions.Default,
                    cursorBrush = SolidColor(Color.Gray),
                    singleLine = true,
                    modifier = Modifier.padding(start = 10.dp).width(120.dp).background(Color.LightGray.copy(alpha = 0.5f), shape = RoundedCornerShape(3.dp)).height(20.dp),
                    textStyle = TextStyle(Color.Black, fontSize = 12.sp)
                )
            }

            Button(
                modifier = Modifier.align(Alignment.End),
                onClick = composeClick {
                    if(state.currentImage!= null && state.currentImage?.type != BufferedImage.TYPE_BYTE_GRAY) {
                        // TODO 增加校验
                        viewModel.canny(state, threshold1Text.value.toDouble(), threshold2Text.value.toDouble(), apertureSizeText.value.toInt())
                    }
                }
            ) {
                Text(text = "Canny 边缘检测", color = Color.Unspecified)
            }
        }
    }
}