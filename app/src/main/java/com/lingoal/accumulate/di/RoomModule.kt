package com.lingoal.accumulate.di

import android.app.Application
import androidx.room.Room
import com.lingoal.accumulate.AppDatabase
import com.lingoal.accumulate.constants.DatabaseConstants
import com.lingoal.accumulate.models.GoalDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun providesAppDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            DatabaseConstants.APP_DB_NAME
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideGoalDao(appDatabase: AppDatabase): GoalDao = appDatabase.goalDao()

}