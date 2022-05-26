package org.feature.fox.coffee_counter.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import org.feature.fox.coffee_counter.data.local.dao.FundingDao

@Database(
    entities = [Funding::class],
    version = 1
)
abstract class FundingDatabase : RoomDatabase() {

    abstract fun fundingDao(): FundingDao
}
