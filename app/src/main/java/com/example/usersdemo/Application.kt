package com.example.usersdemo

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.module.Module
import org.koin.dsl.module


class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@Application)
            modules(appModule)
        }
    }
}

val appModule: Module = module {
    single { ServiceBuilder.buildService() }
    viewModel { UserListViewModel(get()) }
}