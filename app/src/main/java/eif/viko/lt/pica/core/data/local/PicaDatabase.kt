package eif.viko.lt.pica.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MenuItemEntity::class], version = 1)
abstract class PicaDatabase : RoomDatabase() {
    abstract fun menuDao(): MenuDao
}