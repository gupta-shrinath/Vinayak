package learn.droid.vinayak.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import learn.droid.vinayak.database.daos.LocationDao
import learn.droid.vinayak.database.entities.Location

@Database(entities = [Location::class], version = 1)
abstract class LocationDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
    companion object {
        private var databaseInstance: LocationDatabase? = null

        @Synchronized
        fun getInstance(context: Context):LocationDatabase {
            if(databaseInstance == null) {
                databaseInstance = Room.databaseBuilder(context.applicationContext,
                                                        LocationDatabase::class.java,
                                                        "vinayak").build()
            }
            return databaseInstance!!
        }
    }
}
