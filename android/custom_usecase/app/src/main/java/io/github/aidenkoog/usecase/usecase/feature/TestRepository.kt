package io.github.aidenkoog.usecase.usecase.feature

import com.google.gson.annotations.SerializedName
import io.github.aidenkoog.usecase.usecase.base.Data

interface TestRepository {
    suspend fun testRepository(request: RequestModel): Data<ResponseModel>
}

data class RequestModel(
    @SerializedName("test") val test: String = "",
)

data class ResponseModel(
    @SerializedName("test") val test: String = "",
)