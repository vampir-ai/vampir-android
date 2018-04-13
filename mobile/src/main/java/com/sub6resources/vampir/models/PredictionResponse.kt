package com.sub6resources.vampir.models

import com.google.gson.annotations.SerializedName

data class PredictionResponse(@SerializedName("DT") val deviceType: String, @SerializedName("ST") val serverTime: String, val trend: Int, val value: Int)