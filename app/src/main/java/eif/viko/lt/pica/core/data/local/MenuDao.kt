package eif.viko.lt.pica.core.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MenuDao {
    @Query("SELECT * FROM menu_items")
    fun getMenu(): Flow<List<MenuItemEntity>>   // Flow = reactive, auto-updates UI

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<MenuItemEntity>)

    @Query("DELETE FROM menu_items")
    suspend fun clearAll()
}