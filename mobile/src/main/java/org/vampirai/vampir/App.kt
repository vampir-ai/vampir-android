package org.vampirai.vampir

import com.firebase.jobdispatcher.FirebaseJobDispatcher
import com.firebase.jobdispatcher.GooglePlayDriver
import com.sub6resources.utilities.BaseApplication
import com.sub6resources.utilities.loggedWithAuthToken
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.applicationContext
import org.vampirai.vampir.api.AccountApi
import org.vampirai.vampir.api.LinkAccountsApi
import org.vampirai.vampir.api.PredictionApi
import org.vampirai.vampir.repository.AccountRepository
import org.vampirai.vampir.repository.LinkAccountsRepository
import org.vampirai.vampir.repository.PredictionRepository
import org.vampirai.vampir.viewmodels.LinkAccountsViewModel
import org.vampirai.vampir.viewmodels.LoginViewModel
import org.vampirai.vampir.viewmodels.PredictionViewModel
import org.vampirai.vampir.viewmodels.SignUpViewModel
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