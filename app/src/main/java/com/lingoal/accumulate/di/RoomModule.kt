package com.lingoal.accumulate.di

import android.app.Application
import androidx.room.Room
import com.lingoal.accumulate.AppDatabase
import com.lingoal.accumulate.constants.DatabaseConstants
import com.lingoal.accumulate.helpers.RoomHelper
import com.lingoal.accumulate.models.GoalDao
import com.lingoal.accumulate.models.LiftEntryDao
import com.lingoal.accumulate.models.LiftGoalDao
import com.lingoal.accumulate.models.LiftSessionDao
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
    fun providesAppDatabase(application: Application): AppDatabase
    = RoomHelper.getDatabase(application)

    @Singleton
    @Provides
    fun provideGoalDao(appDatabase: AppDatabase): GoalDao = appDatabase.goalDao()

    @Singleton
    @Provides
    fun provideLiftGaolDao(appDatabase: AppDatabase): LiftGoalDao = appDatabase.liftGoalDao()

    @Singleton
    @Provides
    fun provideLiftSessionDao(appDatabase: AppDatabase): LiftSessionDao = appDatabase.liftSessionDao()

    @Singleton
    @Provides
    fun provideLiftEntryDao(appDatabase: AppDatabase): LiftEntryDao = appDatabase.liftEntryDao()

}