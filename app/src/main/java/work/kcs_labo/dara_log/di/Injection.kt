package work.kcs_labo.dara_log.di

import android.content.Context
import work.kcs_labo.dara_log.data.AppRepository
import work.kcs_labo.dara_log.data.local.AppLocalDataSource
import work.kcs_labo.dara_log.data.local.AppLocalDatabase

object Injection {
  fun provideTasksRepository(context: Context): AppRepository {
    val localDatabase = AppLocalDatabase.getInstance(context)
    return AppRepository.getInstance(
      AppLocalDataSource.getInstance(localDatabase.appDao())
    )
  }
}