package com.sub6resources.vampir.fragments

import android.arch.lifecycle.Observer
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.ImageView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.sub6resources.utilities.*
import com.sub6resources.vampir.BasicNetworkState
import com.sub6resources.vampir.R
import com.sub6resources.vampir.models.EncryptedCredentials
import com.sub6resources.vampir.viewmodels.PredictionViewModel
import kotlinx.android.synthetic.main.fragment_chart.*
import java.text.SimpleDateFormat
import java.util.*


class ChartFragment: BaseFragment() {
    override val fragLayout = R.layout.fragment_chart

    private val predictionViewModel by getViewModel<PredictionViewModel>()

    override fun setUp() {


        chart.data = getLineData(listOf(0f))
        chart.legend.isEnabled = false

        chart.axisRight.isEnabled = false
        chart.axisLeft.axisMinimum = 0f
        chart.axisLeft.setDrawGridLines(false)
        chart.axisLeft.isEnabled = false
        chart.description.isEnabled = false
        chart.extraBottomOffset = 10f

        chart.xAxis.apply {
            setDrawAxisLine(false)
            setDrawGridLines(false)
            textColor = Color.WHITE
            labelCount = 4
            isGranularityEnabled = true
            granularity = 5f
            axisMinimum = -.2f
            axisMaximum = 20.2f
            position = XAxis.XAxisPosition.BOTTOM
            setAvoidFirstLastClipping(true)
            textSize = 15f
            setValueFormatter { value, _ ->
                if (value == 0f) {
                    "Now"
                } else {
                    val df = SimpleDateFormat("hh:mm", Locale.US)
                    df.format(Calendar.getInstance().apply { add(Calendar.MINUTE, value.toInt()) }.time)
                }
            }
        }

        chart.legend.isEnabled = false
        predict()

        chart.onClick {
            predict()
        }



        baseActivity.findViewById<ImageView>(R.id.vampir_logo).onClick {
            chart.data =  getLineData(listOf(120f, 128f, 137f, 140f, 141f))
            chart.xAxis.axisMaximum = chart.data.xMax + 0.2f
            chart.xAxis.axisMinimum = chart.data.xMin - 0.2f
            chart.invalidate()
            current_time.text = "${120f} mg/dL"
        }

        val loadingDialog = baseActivity.dialog {
            progress(true, 0)
            content("Loading...")
        }

        predictionViewModel.predictedData.observe(this, Observer {
            when(it) {
                is BasicNetworkState.Success -> {

                    chart.data =  getLineData(it.data.predictions)
                    chart.xAxis.axisMaximum = 5*(it.data.predictions.size-1) + 0.2f
                    chart.xAxis.axisMinimum = chart.data.xMin - 0.2f
                    chart.invalidate()
                    current_time.text = "${it.data.predictions[0]} mg/dL"
                    loadingDialog.hide()

                }
                is BasicNetworkState.Error -> {
                    Log.e("Vampïr", "ERROR! " + it.message)
                    loadingDialog.hide()

                }
                is BasicNetworkState.Loading -> {
                    loadingDialog.show()
                }
            }
        })
    }

    private fun getLineData(values: List<Float>): LineData {

        val yVals = values.mapIndexed { i, value ->
            Entry(i * 5f, value.toInt().toFloat())
        }

        val set1 = LineDataSet(yVals, "Blood Glucose Level")
        set1.apply {
            lineWidth = 2.75f
            circleRadius = 5f
            circleColors = listOf(Color.WHITE)
            setDrawCircleHole(false)
            color = Color.WHITE
            fillColor = ContextCompat.getColor(baseActivity, R.color.colorPrimaryDark)
            setDrawFilled(true)
            highlightLineWidth = 0f
            setDrawValues(true)
            valueTextSize = 15f
            valueTextColor = Color.WHITE
            setDrawHighlightIndicators(false)
            cubicIntensity = 0.1f
            mode = LineDataSet.Mode.CUBIC_BEZIER
        }
        return LineData(set1)
    }

    private fun predict() {
        val credentials = baseActivity.sharedPreferences.getString("encryptedRealtimeCredentials", "")
        if(credentials == "---") {
            baseActivity.dialog {
                title("Enter current blood glucose level (mg/dL):")
                input("120", current_time.text.toString().toInt().toString()) { _, input ->
//                    predictionViewModel.predictFromValue(input.toString(), Calendar.getInstance().get(Calendar.HOUR_OF_DAY))
                }
                positiveText("Predict")
            }.show()
        } else {
            predictionViewModel.predict(EncryptedCredentials(credentials, Calendar.getInstance().get(Calendar.HOUR_OF_DAY)))
        }
    }
}