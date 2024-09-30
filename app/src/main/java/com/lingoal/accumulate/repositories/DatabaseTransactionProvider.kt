package com.lingoal.accumulate.repositories

import androidx.room.withTransaction
import com.lingoal.accumulate.AppDatabase
import javax.inject.Inject

class DatabaseTransactionProvider @Inject constructor(
    private val database: AppDatabase
) {
    suspend fun <T> runAsTransaction(block: suspend () -> T): T {
        return database.withTransaction(block)
    }
}