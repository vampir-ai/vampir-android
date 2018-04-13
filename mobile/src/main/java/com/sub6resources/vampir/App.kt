package com.sub6resources.vampir

import com.sub6resources.utilities.BaseApplication
import com.sub6resources.utilities.logged
import com.sub6resources.vampir.api.AccountApi
import com.sub6resources.vampir.api.LinkAccountsApi
import com.sub6resources.vampir.viewmodels.LinkAccountsViewModel
import com.sub6resources.vampir.viewmodels.LoginViewModel
import com.sub6resources.vampir.viewmodels.SignUpViewModel
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.applicationContext
import retrofit2.Retrofit

class App: BaseApplication(appModule)

val appModule = applicationContext {
    val retrofit = Retrofit.Builder().logged("http://vampirai.ryanberger.me")

    //APIs
    provide { retrofit.create(AccountApi::class.java) }
    provide { retrofit.create(LinkAccountsApi::class.java)}

    //Repositories
    provide { AccountRepository(get()) }
    provide { LinkAccountsRepository(get()) }

    //ViewModels
    viewModel { SignUpViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { LinkAccountsViewModel(get()) }

}