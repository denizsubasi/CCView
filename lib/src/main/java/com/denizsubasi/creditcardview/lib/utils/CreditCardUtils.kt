package com.denizsubasi.creditcardview.lib.utils

import android.text.Editable
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import com.denizsubasi.creditcardview.lib.CreditCardItem
import java.util.*
import java.util.regex.Pattern


private const val PATTERN_AMEX = "^3(4|7)[0-9 ]*"
private const val PATTERN_VISA = "^4[0-9 ]*"
private const val PATTERN_MASTER = "^5[0-9 ]*"
private const val PATTERN_DISCOVER = "^6[0-9 ]*"

const val MAX_LENGTH_CARD_NUMBER = 16
const val MAX_LENGTH_CARD_NUMBER_AMEX = 15

const val CARD_NUMBER_FORMAT = "#### #### #### ####"
const val CARD_NUMBER_FORMAT_AMEX = "#### ###### #####"
const val CARD_EXPIRY_DATE_FORMAT = "##/##"
const val SLASH_SEPERATOR = "/"

fun CharSequence.selectCardType(): CardType {
    var cardType =
        Pattern.compile(PATTERN_VISA)
    if (cardType.matcher(this).matches()) return CardType.VISA_CARD
    cardType =
        Pattern.compile(PATTERN_MASTER)
    if (cardType.matcher(this).matches()) return CardType.MASTER_CARD
    cardType =
        Pattern.compile(PATTERN_AMEX)
    if (cardType.matcher(this).matches()) return CardType.AMEX_CARD
    cardType =
        Pattern.compile(PATTERN_DISCOVER)
    return if (cardType.matcher(this)
            .matches()
    ) CardType.DISCOVER_CARD else CardType.UNKNOWN_CARD
}

fun CardType.cardLength(): Int {
    return if (this == CardType.AMEX_CARD) MAX_LENGTH_CARD_NUMBER_AMEX else MAX_LENGTH_CARD_NUMBER
}

fun CardType.cvvLength(): Int {
    return if (this == CardType.AMEX_CARD) 4 else 3
}

fun CardType.cardNumberMask(): String {
    return when (this) {
        CardType.AMEX_CARD -> {
            CARD_NUMBER_FORMAT_AMEX
        }
        else -> {
            CARD_NUMBER_FORMAT
        }
    }
}

fun String.formatByMask(mask: String): String {
    val text = this
    if (text.isBlank()) return ""
    val formatted = StringBuilder()
    Editable.Factory.getInstance().newEditable(text).apply {
        // reset input filters
        val editableFilters = filters
        filters = emptyArray()

        val list = toMutableList()

        // apply mask
        mask.forEach { m ->
            if (list.isNullOrEmpty()) return@forEach
            var c = list.firstOrNull()
            if (m == '#') {
                if (c?.isLetterOrDigit() == false) {
                    // find next letter or digit
                    val iterator = list.iterator()
                    while (iterator.hasNext()) {
                        c = iterator.next()
                        if (c.isLetterOrDigit()) break
                        iterator.remove()
                    }
                }
                if (list.isNullOrEmpty()) return@forEach
                formatted.append(c)
                list.removeAt(0)
            } else {
                formatted.append(m)
                if (m == c) {
                    list.removeAt(0)
                }
            }
        }
        val previousLength = length
        val currentLength = formatted.length
        replace(0, previousLength, formatted, 0, currentLength)


        // restore input filters
        filters = editableFilters
    }
    return formatted.toString()
}

fun AppCompatEditText.format(mask: String) {
    setText(text.toString().formatByMask(mask))
    setSelection(text?.length ?: 1 - 1)
}

fun AppCompatTextView.format(mask: String) {
    text = text.toString().formatByMask(mask)
}

fun CreditCardItem.checkIfCardExpired(): Boolean {
    val month: String
    var year = ""
    if (expiryDate.length >= 2) {
        month = expiryDate.substring(0, 2)
        if (expiryDate.length > 2) {
            year = expiryDate.substring(3)
        }
        val mm = month.toInt()
        if (mm <= 0 || mm >= 13) {
            return true
        }
        if (expiryDate.length >= 4) {
            val yy = year.toInt()
            val calendar = Calendar.getInstance()
            val currentYear = calendar[Calendar.YEAR]
            val currentMonth = calendar[Calendar.MONTH] + 1
            val millennium = currentYear / 1000 * 1000
            if (yy + millennium < currentYear) {
                return true
            } else if (yy + millennium == currentYear && mm < currentMonth) {
                return true
            }
        }
    }
    return false
}
