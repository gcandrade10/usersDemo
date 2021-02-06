package com.example.usersdemo

import android.os.Parcelable
import com.itkacher.okhttpprofiler.OkHttpProfilerInterceptor
import io.reactivex.Observable
import kotlinx.parcelize.Parcelize
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object ServiceBuilder {

    private val client = OkHttpClient
        .Builder()
        .addInterceptor(OkHttpProfilerInterceptor())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://randomuser.me")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
        .create(Endpoints::class.java)

    fun buildService(): Endpoints {
        return retrofit
    }
}

interface Endpoints {

    @GET("/api/")
    fun getUsers(@Query("page") page: Int, @Query("results") results: Int = 10, @Query("seed") seed: String = "abc"): Observable<Response>

}

class Response(val results: List<User>)

@Parcelize
class User(val gender: String, val picture: Picture, val name: Name, val email:String) : Parcelable
fun User.rawName() = "${name.title} ${name.first} ${name.last}"

@Parcelize
class Picture(val large: String) : Parcelable

@Parcelize
class Name(val title: String, val first: String, val last: String) : Parcelable



