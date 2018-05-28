package maehara08.github_user_mvi.data.source.remote

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Single
import maehara08.github_user_mvi.BuildConfig
import maehara08.github_user_mvi.data.User
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface UsersService {
    companion object {
        private val logLevel =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.NONE
        private val okClient = OkHttpClient.Builder()
                .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(logLevel))
                .build()
        val instance: UsersService by lazy {
            val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.github.com/")
                    .client(okClient)
                    .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
            retrofit.create(UsersService::class.java)
        }
    }

    @GET("/users")
    fun getUsers(@Query("since") since: String): Single<List<User>>

}