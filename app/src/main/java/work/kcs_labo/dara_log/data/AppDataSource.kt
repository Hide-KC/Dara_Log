package work.kcs_labo.dara_log.data

import java.util.*

interface AppDataSource {
  suspend fun loadCheckBoxDTOs(): List<CheckBoxDTO>
  suspend fun registerCommittedTasks(committed: List<CommittedDTO>)
  suspend fun getCommittedTasks(date: Calendar): List<CommittedDTO>
  suspend fun deleteCommittedTasks(date: Calendar)
}