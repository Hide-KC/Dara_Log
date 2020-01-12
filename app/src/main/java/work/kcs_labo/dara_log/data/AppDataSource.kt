package work.kcs_labo.dara_log.data

import java.util.*

interface AppDataSource {
  suspend fun loadCheckBoxDTOs(): List<CheckBoxDTO>
  suspend fun registerCommittedDTOs(committed: List<CommittedDTO>)
  suspend fun getCommittedDTOs(date: Calendar): List<CommittedDTO>
  suspend fun getCommittedDTOs(): List<CommittedDTO>
  suspend fun deleteCommittedDTOs(date: Calendar)
}