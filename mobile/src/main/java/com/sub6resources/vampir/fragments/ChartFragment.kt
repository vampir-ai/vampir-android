package com.sub6resources.vampir.fragments

import android.arch.lifecycle.Observer
import android.graphics.Color
import android.support.v4.content.ContextCompat
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
        chart.description.isEnabled = false
        chart.setDrawGridBackground(false)
        chart.setTouchEnabled(true)

        chart.setViewPortOffsets(10f, 0f, 10f, 0f)

        chart.data = getLineData()
        chart.legend.isEnabled = false

        chart.axisLeft.isEnabled = false
        chart.axisLeft.spaceTop = 0f
        chart.axisRight.isEnabled = false
        chart.axisLeft.spaceBottom = 0f


        chart.xAxis.isEnabled = false

        chart.onClick {
            val credentials = baseActivity.sharedPreferences.getString("encryptedRealtimeCredentials", "")
            if(credentials.isNotEmpty())
                predictionViewModel.predict(EncryptedCredentials(credentials))
            else {
                //TODO Remove in PROD
                baseActivity.dialog {
                    content("CREDENTIALS ARE EMPTY!!!!")
                }.show()
            }
        }

        val loadingDialog = baseActivity.dialog {
            progress(true, 0)
            content("Loading...")
        }

        predictionViewModel.predictedData.observe(this, Observer {
            when(it) {
                is BasicNetworkState.Success -> {
                    loadingDialog.hide()

                }
                is BasicNetworkState.Error -> {
                    loadingDialog.hide()

                }
                is BasicNetworkState.Loading -> {
                    loadingDialog.show()
                }
            }
        })
    }

    fun getLineData(): LineData {

        val yVals =listOf(Entry(0f, 100f), Entry(1f, 110f), Entry(2f, 100f), Entry(3f, 95f), Entry(4f, 85f))

        val set1 = LineDataSet(yVals, "DataSet 1")

        set1.lineWidth = 2.75f
        set1.circleRadius = 5f
        set1.circleColors = listOf(Color.WHITE)
        set1.circleHoleRadius = 0f
        set1.color = Color.WHITE
//        set1.highLightColor = Color.WHITE
        set1.fillColor = ContextCompat.getColor(baseActivity, R.color.colorPrimaryDark)
        set1.setDrawFilled(true)
        set1.highlightLineWidth = 0f
        set1.setDrawValues(false)

        return LineData(set1)

    }
}