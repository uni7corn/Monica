package cn.netdiscovery.monica.ui.controlpanel.doodle.widget

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import cn.netdiscovery.monica.ui.controlpanel.doodle.model.PathProperties
import cn.netdiscovery.monica.ui.widget.color.Blue400
import cn.netdiscovery.monica.ui.widget.properties.ExposedSelectionMenu

/**
 *
 * @FileName:
 *          cn.netdiscovery.monica.ui.controlpanel.doodle.PropertiesMenuDialog
 * @author: Tony Shen
 * @date: 2024/6/16 12:38
 * @version: V1.0 <描述当前版本功能>
 */
@Composable
fun PropertiesMenuDialog(pathOption: PathProperties, onDismiss: () -> Unit) {

    var strokeWidth by remember { mutableStateOf(pathOption.strokeWidth) }
    var strokeCap by remember { mutableStateOf(pathOption.strokeCap) }
    var strokeJoin by remember { mutableStateOf(pathOption.strokeJoin) }

    Dialog(onDismissRequest = onDismiss) {

        Card(
            elevation = 2.dp,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {

                Text(
                    text = "Properties",
                    color = Blue400,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 12.dp, top = 12.dp)
                )

                Canvas(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 20.dp)
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    val path = Path()
                    path.moveTo(0f, size.height / 2)
                    path.lineTo(size.width, size.height / 2)

                    drawPath(
                        color = pathOption.color,
                        path = path,
                        style = Stroke(
                            width = strokeWidth,
                            cap = strokeCap,
                            join = strokeJoin
                        )
                    )
                }

                Text(
                    text = "Stroke Width ${strokeWidth.toInt()}",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )

                Slider(
                    value = strokeWidth,
                    onValueChange = {
                        strokeWidth = it
                        pathOption.strokeWidth = strokeWidth
                    },
                    valueRange = 1f..100f,
                    onValueChangeFinished = {}
                )

                ExposedSelectionMenu(title = "Stroke Cap",
                    index = when (strokeCap) {
                        StrokeCap.Butt -> 0
                        StrokeCap.Round -> 1
                        else -> 2
                    },
                    options = listOf("Butt", "Round", "Square"),
                    onSelected = {
                        println("STOKE CAP $it")
                        strokeCap = when (it) {
                            0 -> StrokeCap.Butt
                            1 -> StrokeCap.Round
                            else -> StrokeCap.Square
                        }

                        pathOption.strokeCap = strokeCap

                    }
                )

                ExposedSelectionMenu(title = "Stroke Join",
                    index = when (strokeJoin) {
                        StrokeJoin.Miter -> 0
                        StrokeJoin.Round -> 1
                        else -> 2
                    },
                    options = listOf("Miter", "Round", "Bevel"),
                    onSelected = {
                        println("STOKE JOIN $it")

                        strokeJoin = when (it) {
                            0 -> StrokeJoin.Miter
                            1 -> StrokeJoin.Round
                            else -> StrokeJoin.Bevel
                        }

                        pathOption.strokeJoin = strokeJoin
                    }
                )
            }
        }
    }
}
