package com.sub6resources.vampir.models

import com.google.gson.annotations.SerializedName

data class Prediction(@SerializedName("Timestamp") val timestamp: String, @SerializedName("Value") val value: Int)

data class PredictionResponse(@SerializedName("Predictions") val predictions: List<Prediction>)