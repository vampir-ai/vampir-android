package com.sub6resources.vampir.fragments

import android.arch.lifecycle.Observer
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.util.Log
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.sub6resources.utilities.BaseFragment
import com.sub6resources.utilities.dialog
import com.sub6resources.utilities.onClick
import com.sub6resources.utilities.sharedPreferences
import com.sub6resources.vampir.BasicNetworkState
import com.sub6resources.vampir.R
import com.sub6resources.vampir.models.EncryptedCredentials
import com.sub6resources.vampir.viewmodels.PredictionViewModel
import kotlinx.android.synthetic.main.fragment_chart.*
import java.util.*


class ChartFragment: BaseFragment() {
    override val fragLayout = R.layout.fragment_chart

    val predictionViewModel by getViewModel<PredictionViewModel>()

    override fun setUp() {




        chart.data = getLineData(listOf(0f))
        chart.legend.isEnabled = false

        chart.axisRight.isEnabled = false
        chart.axisLeft.axisMinimum = 50f
        chart.axisLeft.setDrawGridLines(false)
        chart.axisLeft.isEnabled = false
        chart.description.isEnabled = false
        chart.extraBottomOffset = 10f

        val bottomAxis = chart.xAxis
        bottomAxis.setDrawAxisLine(false)
        bottomAxis.setDrawGridLines(false)
        bottomAxis.textColor = Color.WHITE
        bottomAxis.labelCount = 4
        bottomAxis.isGranularityEnabled = true
        bottomAxis.granularity = 5f
        bottomAxis.axisMinimum = -.2f
        bottomAxis.axisMaximum = 15.2f
        bottomAxis.position = XAxis.XAxisPosition.BOTTOM
        bottomAxis.setAvoidFirstLastClipping(true)
        bottomAxis.textSize = 15f

        chart.legend.isEnabled = false
        val credentials = baseActivity.sharedPreferences.getString("encryptedRealtimeCredentials", "")
        predictionViewModel.predict(EncryptedCredentials(credentials, Calendar.getInstance().get(Calendar.HOUR_OF_DAY)))

        chart.onClick {
            predictionViewModel.predict(EncryptedCredentials(credentials, Calendar.getInstance().get(Calendar.HOUR_OF_DAY)))
        }

        val loadingDialog = baseActivity.dialog {
            progress(true, 0)
            content("Loading...")
        }

        predictionViewModel.predictedData.observe(this, Observer {
            when(it) {
                is BasicNetworkState.Success -> {
                    chart.data =  getLineData(it.data.predictions)
                    chart.invalidate()
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
            circleHoleRadius = 0f
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
}