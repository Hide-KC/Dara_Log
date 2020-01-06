package work.kcs_labo.dara_log.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "committed")
data class CommittedDTO(
  @PrimaryKey(autoGenerate = true)
  val _id: Long = 0,

  val yyyymmdd: String = "",

  val text: String = "",

  @ColumnInfo(name = "short_text")
  val shortText: String = "",

  @ColumnInfo(name = "image_id")
  val imageId: Int = -1
)