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
import com.sub6resources.vampir.models.Prediction
import com.sub6resources.vampir.viewmodels.PredictionViewModel
import kotlinx.android.synthetic.main.fragment_chart.*
import com.github.mikephil.charting.components.YAxis



class ChartFragment: BaseFragment() {
    override val fragLayout = R.layout.fragment_chart

    val predictionViewModel by getViewModel<PredictionViewModel>()

    override fun setUp() {
        chart.description.isEnabled = false
        chart.setDrawGridBackground(false)
        chart.setTouchEnabled(true)

        chart.setViewPortOffsets(10f, 0f, 10f, 0f)

        chart.data = getLineData(listOf(0f))
        chart.legend.isEnabled = false

        chart.xAxis.isEnabled = false
        chart.isScaleYEnabled = false
        chart.isScaleXEnabled = false

        val left = chart.axisLeft
        left.setDrawLabels(false) // no axis labels
        left.setDrawAxisLine(false) // no axis line
        left.setDrawGridLines(false) // no grid lines
        left.setDrawZeroLine(true) // draw a zero line
        chart.axisRight.isEnabled = false // no right axis



        chart.axisLeft.setDrawZeroLine(true)


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
                    chart.data =  getLineData(it.data.predictions.map { it.value.toFloat() })
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

    fun getLineData(values: List<Float>): LineData {

        val yVals = values.mapIndexed { i, value ->
            Entry(i * 5f, value)
        }

        val set1 = LineDataSet(yVals, "DataSet 1")

        set1.lineWidth = 2.75f
        set1.circleRadius = 5f
        set1.circleColors = listOf(Color.WHITE)
        set1.circleHoleRadius = 0f
        set1.color = Color.WHITE
        set1.fillColor = ContextCompat.getColor(baseActivity, R.color.colorPrimaryDark)
        set1.setDrawFilled(true)
        set1.highlightLineWidth = 0f
        set1.setDrawValues(true)
        set1.valueTextSize = 16f
        set1.valueTextColor = Color.WHITE

        return LineData(set1)

    }
}