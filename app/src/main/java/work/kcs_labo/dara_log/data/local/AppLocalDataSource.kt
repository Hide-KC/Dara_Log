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

  override suspend fun registerCommittedTasks(committed: List<CommittedDTO>) {
    appDao.registerCommittedTasks(committed)
  }

  override suspend fun deleteCommittedTasks(date: Calendar) {
    val sdfSrc = SimpleDateFormat("yyyyMMdd", Locale.JAPAN)
    val formatDate = sdfSrc.format(date.time)

    appDao.deleteCommittedTasks(formatDate)
  }

  override suspend fun getCommittedTasks(date: Calendar): List<CommittedDTO> {
    val sdfSrc = SimpleDateFormat("yyyyMMdd", Locale.JAPAN)
    val formatDate = sdfSrc.format(date.time)

    return appDao.getCommittedTasks(formatDate)
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