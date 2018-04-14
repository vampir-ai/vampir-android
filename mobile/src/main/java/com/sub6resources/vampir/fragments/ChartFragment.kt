package com.sub6resources.vampir.fragments

import android.arch.lifecycle.Observer
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.util.Log
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
        val bottomAxis = chart.xAxis
        bottomAxis.setDrawAxisLine(false)
        bottomAxis.setDrawGridLines(false)
        bottomAxis.textColor = Color.WHITE
        bottomAxis.labelCount = chart.data.entryCount
        //Font?

        chart.legend.isEnabled = false


        chart.onClick {
            val credentials = baseActivity.sharedPreferences.getString("encryptedRealtimeCredentials", "")
            predictionViewModel.predict(EncryptedCredentials(credentials))
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
            Entry(i * 5f, value)
        }

        val set1 = LineDataSet(yVals, "Blood Glucose Level")

        set1.lineWidth = 2.75f
        set1.circleRadius = 5f
        set1.circleColors = listOf(Color.WHITE)
        set1.circleHoleRadius = 0f
        set1.color = Color.WHITE
        set1.fillColor = ContextCompat.getColor(baseActivity, R.color.colorPrimaryDark)
        set1.setDrawFilled(true)
        set1.highlightLineWidth = 0f
        set1.setDrawValues(true)
        set1.valueTextSize = 15f
        set1.valueTextColor = Color.WHITE
        set1.setDrawHighlightIndicators(false)
        set1.cubicIntensity = 0.1f
        set1.mode = LineDataSet.Mode.CUBIC_BEZIER

        return LineData(set1)

    }
}