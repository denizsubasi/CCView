package com.denizsubasi.creditcardview.lib.utils

import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.widget.AppCompatTextView

class MaskTextWatcher(
    private val mask: String
) : TextWatcher {

    private var selfChange = false
    var isEnabled = true
        set(value) {
            if (value) {

            }
            field = value
        }

    override fun afterTextChanged(s: Editable?) {
        if (selfChange || !isEnabled) return
        selfChange = true
        format(s)
        selfChange = false
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    fun format(text: Editable?) {
        if (text.isNullOrBlank()) return
        text.apply {
            // reset input filters
            val editableFilters = filters
            filters = emptyArray()

            val formatted = StringBuilder()
            val list = toMutableList()

            // apply mask
            mask.forEach { m ->
                if (list.isNullOrEmpty()) return@forEach
                var c = list.firstOrNull()
                if (m.isPlaceHolder()) {
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
            /* try {
                replace(0, previousLength, formatted, 0, currentLength)
            } catch (e: Exception) {

            } */

            // restore input filters
            filters = editableFilters
        }
    }

    fun unformat(text: CharSequence?): String? {
        if (text.isNullOrEmpty()) return null
        val unformatted = StringBuilder()
        val textLength = text.length
        mask.forEachIndexed { index, m ->
            if (index >= textLength) return@forEachIndexed
            if (m.isPlaceHolder()) {
                text.getOrNull(index)?.let {
                    unformatted.append(it)
                }
            }
        }
        return unformatted.toString()
    }

    private fun findCursorPosition(text: Editable?, start: Int): Int {
        if (text.isNullOrEmpty()) return start
        val textLength = text.length
        val maskLength = mask.length
        var position = start
        for (i in start until maskLength) {
            if (mask[i].isPlaceHolder()) {
                break
            }
            position++
        }
        position++
        return if (position < textLength) position else textLength
    }
}

internal fun Char.isPlaceHolder(): Boolean = this == '#'
