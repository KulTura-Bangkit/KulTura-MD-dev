package academy.bangkit.kultura.ui.dashboard

import academy.bangkit.kultura.ui.upload.ApiService
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

data class UserResponse(
    @field:SerializedName("data")
    val items: List<Item>
) {
    data class Item(

        @field:SerializedName("id")
        val id: Int,

        @field:SerializedName("name")
        val name: String,

        @field:SerializedName("description")
        val description: String,

        @field:SerializedName("url_image")
        val url_image: String,

        @field:SerializedName("url_product")
        val url_product: String,
    )
}

interface ApiServices{
    @GET("all/{nama}")
    fun getData(
        @Path("nama") nama: String
    ): Call<List<UserResponse.Item>>
}

class ApiConfigs{
    fun getApiService(): ApiServices {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://kultura-v1.et.r.appspot.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiServices::class.java)
    }
}