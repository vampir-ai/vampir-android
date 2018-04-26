package org.vampirai.vampir.models

import com.google.gson.annotations.SerializedName

data class EncryptedCredentials(@SerializedName("message") val encryptedCredentials: String, val hour: Int)