package work.kcs_labo.dara_log.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "checkboxes", indices = [Index(value = ["checkbox_id"], unique = true)])
data class CheckBoxDTO(
  @PrimaryKey(autoGenerate = true)
  val _id: Long = 0,

  @ColumnInfo(name = "checkbox_id")
  val checkboxId: Int = 0,

  val text: String = "",

  @ColumnInfo(name = "short_text")
  val shortText: String = "",

  @ColumnInfo(name = "image_id")
  val imageId: Int = -1
)