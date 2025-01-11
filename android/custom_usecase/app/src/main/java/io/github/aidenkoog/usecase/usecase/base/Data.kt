package io.github.aidenkoog.usecase.usecase.base

import com.google.gson.annotations.SerializedName

open class Data<T> {
    @SerializedName("code")
    var code: String? = ""

    @SerializedName("msg")
    var message: String? = ""

    @SerializedName("data")
    var data: T? = null

    fun isSuccess() = code == STATUS_SUCCESS

    companion object {
        const val STATUS_SUCCESS = "200"
    }
}