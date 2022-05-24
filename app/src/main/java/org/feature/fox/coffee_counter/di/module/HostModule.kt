package org.feature.fox.coffee_counter.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import org.feature.fox.coffee_counter.BaseApplication
import org.feature.fox.coffee_counter.util.Constants

@Module
@InstallIn(BaseApplication::class)
object HostModule {
    @Provides
    fun provideBaseUrl(): String {
        return Constants.BASE_URL
    }
}