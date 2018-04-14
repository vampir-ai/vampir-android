package com.sub6resources.vampir

import android.util.Log
import com.firebase.jobdispatcher.FirebaseJobDispatcher
import com.firebase.jobdispatcher.GooglePlayDriver
import com.sub6resources.utilities.BaseApplication
import com.sub6resources.utilities.sharedPreferences
import com.sub6resources.vampir.api.AccountApi
import com.sub6resources.vampir.api.LinkAccountsApi
import com.sub6resources.vampir.api.PredictionApi
import com.sub6resources.vampir.repository.AccountRepository
import com.sub6resources.vampir.repository.LinkAccountsRepository
import com.sub6resources.vampir.repository.PredictionRepository
import com.sub6resources.vampir.viewmodels.LinkAccountsViewModel
import com.sub6resources.vampir.viewmodels.LoginViewModel
import com.sub6resources.vampir.viewmodels.PredictionViewModel
import com.sub6resources.vampir.viewmodels.SignUpViewModel
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.applicationContext
import retrofit2.Retrofit

class App: BaseApplication(appModule) {

    val firebaseJobDispatcher by lazy { FirebaseJobDispatcher(GooglePlayDriver(this)) }

    override fun onCreate() {
        super.onCreate()
        SharedPref.sharedPreferences = sharedPreferences
    }
}

val appModule = applicationContext {
    val retrofit = Retrofit.Builder().loggedWithAuthToken("http://vampirai.ryanberger.me", "token")

    //APIs
    provide { retrofit.create(AccountApi::class.java) }
    provide { retrofit.create(LinkAccountsApi::class.java)}
    provide { retrofit.create(PredictionApi::class.java) }

    //Repositories
    provide { AccountRepository(get()) }
    provide { LinkAccountsRepository(get()) }
    provide { PredictionRepository(get()) }

    //ViewModels
    viewModel { SignUpViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { LinkAccountsViewModel(get()) }
    viewModel { PredictionViewModel(get()) }

}

fun Retrofit.Builder.loggedWithAuthToken(baseUrl: String, tokenSharedPreferencesKey: String): Retrofit {
    return this.apply {
        baseUrl(baseUrl)
        client(OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
                .apply {
                    addInterceptor { chain ->
                        val original: Request = chain.request()
                        val request: Request = original.newBuilder()
                                .apply {
                                    header("Accept", "application/json")
                                    val token = SharedPref.sharedPreferences.getString(tokenSharedPreferencesKey, "")
                                    if (token.isNotEmpty()) {
                                        Log.d("Vampïr", "REQUEST HEADER: Authorization, $token")
                                        header("Authorization", token)

                                    } else {
                                        Log.d("Vampïr", "No Authorization")
                                    }
                                    method(original.method(), original.body())
                                }
                                .build()

                        val response = chain.proceed(request)
                        response
                    }
                }
                .build())
        addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
        addCallAdapterFactory(retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory.create())
    }.build()
}