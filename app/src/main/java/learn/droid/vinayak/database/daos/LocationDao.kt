package learn.droid.vinayak.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import learn.droid.vinayak.database.entities.Location

@Dao
interface LocationDao {
    @Query("SELECT * FROM location")
    suspend fun getLocations(): List<Location>

    @Insert()
    suspend fun insertLocation(location:Location)

}