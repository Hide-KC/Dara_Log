package work.kcs_labo.dara_log.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import work.kcs_labo.dara_log.R
import work.kcs_labo.dara_log.data.local.AppLocalDataSource
import work.kcs_labo.dara_log.data.local.AppLocalDatabase
import java.util.*
import kotlin.coroutines.CoroutineContext

class AppRepository(
  private val localDataSource: AppLocalDataSource
) : AppDataSource, CoroutineScope {
  override val coroutineContext: CoroutineContext
    get() = Dispatchers.IO

  override suspend fun loadCheckBoxDTOs(): List<CheckBoxDTO> {
    return localDataSource.loadCheckBoxDTOs()
  }

  override suspend fun registerCommittedDTOs(committed: List<CommittedDTO>) {
    localDataSource.registerCommittedDTOs(committed)
  }

  override suspend fun deleteCommittedDTOs(date: Calendar) {
    localDataSource.deleteCommittedDTOs(date)
  }

  override suspend fun getCommittedDTOs(date: Calendar): List<CommittedDTO> {
    return localDataSource.getCommittedDTOs(date)
  }

  override suspend fun getCommittedDTOs(): List<CommittedDTO> {
    return localDataSource.getCommittedDTOs()
  }

  private fun initCheckBoxes() {
    launch(coroutineContext) {
      val checkBoxDTOs = withContext(Dispatchers.IO) { localDataSource.loadCheckBoxDTOs() }
      if (checkBoxDTOs.isNullOrEmpty()) {
        val list = listOf(
          CheckBoxDTO(
            checkboxId = 0,
            text = "ご飯を作った",
            shortText = "ご飯",
            imageId = R.drawable.ic_ryourisuru_hito
          ),
          CheckBoxDTO(
            checkboxId = 1,
            text = "ゴミ出しした",
            shortText = "ゴミ出し",
            imageId = R.drawable.ic_gomidasisuru_hito
          ),
          CheckBoxDTO(
            checkboxId = 2,
            text = "勉強した",
            shortText = "勉強",
            imageId = R.drawable.ic_benkyousuru_hito
          ),
          CheckBoxDTO(
            checkboxId = 3,
            text = "掃除をした",
            shortText = "掃除",
            imageId = R.drawable.ic_soujisuru_hito
          ),
          CheckBoxDTO(
            checkboxId = 4,
            text = "創作した",
            shortText = "創作活動",
            imageId = R.drawable.ic_sousakusuru_hito
          ),
          CheckBoxDTO(
            checkboxId = 5,
            text = "送り迎えした",
            shortText = "送り迎え",
            imageId = R.drawable.ic_oyako
          ),
          CheckBoxDTO(
            checkboxId = 6,
            text = "鷹のポーズ",
            shortText = "鷹のポーズ",
            imageId = R.drawable.ic_takanoposesuru_hito
          ),
          CheckBoxDTO(
            checkboxId = 7,
            text = "そのほか",
            shortText = "そのほか",
            imageId = R.drawable.ic_yahooosuru_hito
          )
        )
        localDataSource.initCheckBoxes(list)
      }
    }
  }

  companion object {
    private var INSTANCE: AppRepository? = null
    private val lock = Any()

    fun getInstance(
      localDataSource: AppLocalDataSource
    ): AppRepository =
      INSTANCE ?: synchronized(lock) {
        INSTANCE ?: AppRepository(localDataSource)
          .also {
            it.initCheckBoxes()
            INSTANCE = it
          }
      }

    fun destroyInstance() {
      AppLocalDatabase.destroyInstance()
      AppLocalDataSource.destroyInstance()
      INSTANCE = null
    }
  }
}