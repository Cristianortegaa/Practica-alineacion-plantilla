package org.example.newteamultimateedition.common.locale

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDate

class LocaleTest(){
    private val localDate: LocalDate = LocalDate.parse("2020-01-01")

    @Test
    @DisplayName("formatear un local date a  locale")
    fun localDateToLocale(){

    }

}
/*

    fun LocalDate.toLocalDate(): String {
        return this.format(
            DateTimeFormatter
                .ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.getDefault())
        )
    }

    fun LocalDateTime.toLocalDateTime(): String {
        return this.format(
            DateTimeFormatter
                .ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(Locale.getDefault())
        )
    }

    fun Double.toLocalMoney(): String {
        return NumberFormat.getCurrencyInstance(Locale.getDefault()).format(this)
    }

    fun Double.toLocalNumber(): String {
        return NumberFormat.getNumberInstance(Locale.getDefault()).format(this)
    }

    fun Double.round(decimals: Int): Double {
        val factor = 10.0.pow(decimals)
        return (this * factor).roundToInt() / factor
    }
 */