package work.kcs_labo.dara_log.data.local

import work.kcs_labo.dara_log.data.AppDataSource
import work.kcs_labo.dara_log.data.CheckBoxDTO
import work.kcs_labo.dara_log.data.CommittedDTO
import java.text.SimpleDateFormat
import java.util.*

class AppLocalDataSource(private val appDao: AppDao) : AppDataSource {
  override suspend fun loadCheckBoxDTOs(): List<CheckBoxDTO> {
    return appDao.loadCheckBoxDTOs()
  }

  override suspend fun registerCommittedDTOs(committed: List<CommittedDTO>) {
    appDao.registerCommittedTasks(committed)
  }

  override suspend fun deleteCommittedDTOs(date: Calendar) {
    val sdfSrc = SimpleDateFormat("yyyyMMdd", Locale.JAPAN)
    val formatDate = sdfSrc.format(date.time)

    appDao.deleteCommittedTasks(formatDate)
  }

  override suspend fun getCommittedDTOs(date: Calendar): List<CommittedDTO> {
    val sdfSrc = SimpleDateFormat("yyyyMMdd", Locale.JAPAN)
    val formatDate = sdfSrc.format(date.time)

    return appDao.getCommittedDTOs(formatDate)
  }

  override suspend fun getCommittedDTOs(): List<CommittedDTO> {
    return appDao.getCommittedDTOs()
  }

  suspend fun initCheckBoxes(checkBoxDTOs: List<CheckBoxDTO>) {
    appDao.initCheckBoxDTOs(checkBoxDTOs)
  }

  companion object {
    private var INSTANCE: AppLocalDataSource? = null
    private val lock = Any()

    fun getInstance(appDao: AppDao): AppLocalDataSource =
      INSTANCE ?: synchronized(lock) {
        INSTANCE ?: AppLocalDataSource(appDao)
          .also {
            INSTANCE = it
          }
      }

    fun destroyInstance() {
      INSTANCE = null
    }
  }
}