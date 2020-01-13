package work.kcs_labo.dara_log.util

import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.*

class CalendarContentsCreatorTest {

  @Before
  fun setUp() {
  }

  @After
  fun tearDown() {
  }

  @Test
  fun create() {
    val year = 2019
    val month = Calendar.JANUARY
    val size = 32

    val contents = CalendarContentsCreator.create(year, month)
    assertThat(contents)
      .hasSize(size)
  }
}