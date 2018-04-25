package com.sub6resources.vampir

import com.firebase.jobdispatcher.FirebaseJobDispatcher
import com.firebase.jobdispatcher.GooglePlayDriver
import com.sub6resources.utilities.BaseApplication
import com.sub6resources.utilities.loggedWithAuthToken
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
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.applicationContext
import retrofit2.Retrofit

class App: BaseApplication(appModule) {
    val firebaseJobDispatcher by lazy { FirebaseJobDispatcher(GooglePlayDriver(this)) }
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