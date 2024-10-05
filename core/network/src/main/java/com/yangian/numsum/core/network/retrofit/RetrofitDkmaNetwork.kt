package com.yangian.numsum.core.network.retrofit

import androidx.annotation.Keep
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.yangian.numsum.core.network.model.DkmaManufacturer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import javax.inject.Inject
import javax.inject.Singleton

private interface RetrofitDkmaNetworkAPI {
    @GET("{manufacturer}.json")
    suspend fun getDkmaManufacturer(
        @Path("manufacturer") manufacturer: String
    ): DkmaManufacturer
}

private const val DONT_KILL_MY_APP_BASE_URL = "https://dontkillmyapp.com"
private const val DONT_KILL_MY_APP_BASE_API_ENDPOINT = "$DONT_KILL_MY_APP_BASE_URL/api/v2/"

@Serializable
private data class NetworkResponse<T>(
    val data: T
)

@Singleton
class RetrofitDkmaNetwork @Inject constructor(
    networkJson: Json,
    okhttpCallFactory: dagger.Lazy<Call.Factory>,
) : DkmaNetworkDataSource {

    private val networkAPI = Retrofit.Builder()
        .baseUrl(DONT_KILL_MY_APP_BASE_API_ENDPOINT)
        .callFactory { okhttpCallFactory.get().newCall(it) }
        .addConverterFactory(
            networkJson.asConverterFactory("application/json".toMediaType()),
        )
        .build()
        .create(RetrofitDkmaNetworkAPI::class.java)

    override suspend fun getDkmaManufacturer(manufacturer: String): DkmaManufacturer =
        networkAPI.getDkmaManufacturer(manufacturer.lowercase())
}