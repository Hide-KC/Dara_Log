package work.kcs_labo.dara_log.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import work.kcs_labo.dara_log.data.CheckBoxDTO
import work.kcs_labo.dara_log.data.CommittedDTO

@Database(entities = [CheckBoxDTO::class, CommittedDTO::class], version = 1, exportSchema = false)
abstract class AppLocalDatabase : RoomDatabase() {
  abstract fun appDao(): AppDao

  companion object {
    private var INSTANCE: AppLocalDatabase? = null
    private val lock = Any()

    fun getInstance(context: Context): AppLocalDatabase =
      INSTANCE ?: synchronized(lock) {
        INSTANCE ?: Room.databaseBuilder(
          context.applicationContext,
          AppLocalDatabase::class.java, "AppLocal.db"
        )
          .build()
          .also { INSTANCE = it }
      }

    fun destroyInstance() {
      INSTANCE = null
    }
  }
}