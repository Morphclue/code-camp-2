package org.feature.fox.coffee_counter.di.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.feature.fox.coffee_counter.data.local.ItemDatabase
import org.feature.fox.coffee_counter.data.local.UserDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun providesUserDatabase(
        @ApplicationContext context: Context,
    ) = Room.databaseBuilder(context, UserDatabase::class.java, "users").build()

    @Singleton
    @Provides
    fun providesUserDao(
        database: UserDatabase,
    ) = database.userDao()

    @Singleton
    @Provides
    fun providesItemDatabase(
        @ApplicationContext context: Context,
    ) = Room.databaseBuilder(context, ItemDatabase::class.java, "items").build()

    @Singleton
    @Provides
    fun providesItemDao(
        database: ItemDatabase,
    ) = database.itemDao()
}
