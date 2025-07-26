import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import android.graphics.Color as AndroidColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

@Composable
fun MPLineChart(
    title: String,
    data: List<Float>,
    color: Int = AndroidColor.rgb(0, 128, 255) // Default blue
) {
    val entries = data.mapIndexed { index, value -> Entry(index.toFloat() + 1, value) }

    AndroidView(
        factory = { context ->
            LineChart(context).apply {
                val dataSet = LineDataSet(entries, title).apply {
                    this.color = color
                    valueTextColor = AndroidColor.BLACK
                    lineWidth = 2f
                    setCircleColor(color)
                    circleRadius = 4f
                    mode = LineDataSet.Mode.CUBIC_BEZIER
                    setDrawFilled(true)
                    fillAlpha = 80
                    fillColor = color
                }

                val lineData = LineData(dataSet)
                this.data = lineData
                this.animateX(1000)
                this.description = Description().apply { text = "" } // Remove default description
                axisRight.isEnabled = false
                legend.isEnabled = false
                xAxis.setDrawGridLines(false)
                xAxis.textColor = AndroidColor.BLACK
                axisLeft.textColor = AndroidColor.BLACK
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        update = { chart ->
            chart.invalidate() // refresh chart
        }
    )
}
