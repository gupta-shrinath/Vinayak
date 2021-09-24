package learn.droid.vinayak.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location")
data class Location(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "disaster") val disaster:String?,
    @ColumnInfo(name = "address") val address: String?
)
