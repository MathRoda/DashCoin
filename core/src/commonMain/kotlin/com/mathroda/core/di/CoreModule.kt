package com.mathroda.core.di

import com.mathroda.core.util.AppInfo
import org.koin.core.module.Module
import org.koin.dsl.module

expect fun platformModule(): Module
val coreModule = module {
    includes(platformModule())
    single { AppInfo() }
}
