package work.kcs_labo.dara_log.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import work.kcs_labo.dara_log.data.CheckBoxDTO
import work.kcs_labo.dara_log.data.CommittedDTO

@Dao
interface AppDao {
  @Query("SELECT * FROM checkboxes")
  fun loadCheckBoxDTOs(): List<CheckBoxDTO>

  @Query("SELECT * FROM committed WHERE yyyymmdd=:yyyymmdd")
  fun getCommittedDTOs(yyyymmdd: String): List<CommittedDTO>

  @Query("SELECT * FROM committed")
  fun getCommittedDTOs(): List<CommittedDTO>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  @JvmSuppressWildcards
  fun registerCommittedTasks(committed: List<CommittedDTO>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  @JvmSuppressWildcards
  fun initCheckBoxDTOs(checkBoxDTOs: List<CheckBoxDTO>)

  @Query("DELETE FROM committed WHERE yyyymmdd=:yyyymmdd")
  fun deleteCommittedTasks(yyyymmdd: String)
}