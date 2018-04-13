package com.sub6resources.vampir.fragments

import android.graphics.Color
import android.support.v4.content.ContextCompat
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.sub6resources.utilities.BaseFragment
import com.sub6resources.vampir.R
import kotlinx.android.synthetic.main.fragment_chart.*

class ChartFragment: BaseFragment() {
    override val fragLayout = R.layout.fragment_chart

    override fun setUp() {
        chart.description.isEnabled = false
        chart.setDrawGridBackground(false)
        chart.setTouchEnabled(true)
        chart.setBackgroundColor(ContextCompat.getColor(baseActivity, R.color.colorPrimary))

        chart.data = getLineData()
        chart.legend.isEnabled = false

        chart.axisLeft.isEnabled = true
        chart.axisLeft.spaceTop = 0f
        chart.axisRight.isEnabled = false
        chart.axisLeft.spaceBottom = 0f

        chart.xAxis.isEnabled = false
    }

    fun getLineData(): LineData {

        val yVals =listOf(Entry(0f, 100f), Entry(1f, 110f), Entry(2f, 100f), Entry(3f, 95f), Entry(4f, 85f))

        val set1 = LineDataSet(yVals, "DataSet 1")

        set1.lineWidth = 1.75f
        set1.circleRadius = 5f
        set1.circleHoleRadius = 0f
        set1.color = Color.WHITE
        set1.highLightColor = Color.WHITE
        set1.fillColor = ContextCompat.getColor(baseActivity, R.color.colorPrimaryDark)
        set1.highlightLineWidth = 0f
        set1.setDrawValues(false)

        return LineData(set1)

    }
}