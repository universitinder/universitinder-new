package com.universitinder.app.helpers

import java.text.NumberFormat
import java.util.Locale

class CurrencyFormatter {
    companion object {
        fun format(value: Double) : String {
            val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("en", "PH"))
            return currencyFormatter.format(value)
        }
    }
}
