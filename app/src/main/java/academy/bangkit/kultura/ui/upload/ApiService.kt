package academy.bangkit.kultura.ui.upload

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.util.concurrent.TimeUnit

data class FileUploadResponse(
    @field:SerializedName("label 1")
    val label_1: String,
    @field:SerializedName("label 2")
    val label_2: String,
    @field:SerializedName("label 3")
    val label_3: String,
    @field:SerializedName("percent 1")
    val percent_1: Int,
    @field:SerializedName("percent 2")
    val percent_2: Int,
    @field:SerializedName("percent 3")
    val percent_3: Int
)

interface ApiService {
    @Multipart
    @POST("/predict1")
    fun uploadImage(
        @Part file: MultipartBody.Part,
    ): Call<FileUploadResponse>
    @Multipart
    @POST("/predict2")
    fun uploadImage2(
        @Part file: MultipartBody.Part,
    ): Call<FileUploadResponse>

    @Multipart
    @POST("/predict3")
    fun uploadImage3(
        @Part file: MultipartBody.Part,
    ): Call<FileUploadResponse>
}
class ApiConfig{
    fun getApiService(): ApiService {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://kultura-flask-4zvftwq7nq-et.a.run.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }
}