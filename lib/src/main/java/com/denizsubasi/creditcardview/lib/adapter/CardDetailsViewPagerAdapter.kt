package com.denizsubasi.creditcardview.lib.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.LayoutInflater.from
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.viewpager.widget.PagerAdapter
import com.denizsubasi.creditcardview.lib.CardInputType.*
import com.denizsubasi.creditcardview.lib.R
import com.denizsubasi.creditcardview.lib.databinding.LayoutCardCvvBinding
import com.denizsubasi.creditcardview.lib.databinding.LayoutCardExpiryDateBinding
import com.denizsubasi.creditcardview.lib.databinding.LayoutCardHolderNameBinding
import com.denizsubasi.creditcardview.lib.databinding.LayoutCardNumberBinding
import com.denizsubasi.creditcardview.lib.ext.onDone
import com.denizsubasi.creditcardview.lib.ext.onNext
import com.denizsubasi.creditcardview.lib.utils.*
import java.util.*

class CardDetailsViewPagerAdapter(
    private val context: Context,
    private val cardNumberCallbackFunc: ((cardNumber: String, cardType: CardType, goNextField: Boolean) -> Unit),
    private val cardHolderNameCallbackFunc: ((holderName: String, goNextField: Boolean) -> Unit),
    private val cardCvvCallbackFunc: ((cvv: Int, goNextField: Boolean) -> Unit),
    private val cardExpiryDateCallbackFunc: ((expiryDate: String, goNextField: Boolean) -> Unit)
) : PagerAdapter() {

    private val items = getPagerItems()

    private val inflater: LayoutInflater
        get() = from(context)

    private val expiryDateTextWatcher = MaskTextWatcher(CARD_EXPIRY_DATE_FORMAT)

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val item = items[position]
        return when (item.inputType) {
            NUMBER -> {
                LayoutCardNumberBinding.inflate(inflater, container, true).apply {
                    var applyingMask = false
                    cardNumberEditText.apply {
                        doAfterTextChanged {
                            if (applyingMask) {
                                return@doAfterTextChanged
                            }
                            val cardType = it?.selectCardType() ?: CardType.UNKNOWN_CARD
                            applyingMask = true
                            cardNumberEditText.format(cardType.cardNumberMask())
                            applyingMask = false
                            cardNumberCallbackFunc(it.toString().replace(" ", ""), cardType, false)
                        }
                        onNext(hideKeyboard = false) {
                            if (it.isNotBlank()) {
                                cardNumberCallbackFunc(
                                    it.toString().replace(" ", ""),
                                    it.selectCardType(),
                                    true
                                )
                            }
                        }
                    }
                }.root
            }
            HOLDER_NAME -> {
                LayoutCardHolderNameBinding.inflate(inflater, container, true).apply {
                    cardholderNameEditText.apply {
                        addTextChangedListener {
                            cardHolderNameCallbackFunc(it.toString(),false)
                        }
                        onNext(hideKeyboard = false) {
                            if (it.isNotBlank()) {
                                cardHolderNameCallbackFunc(it, true)
                            }
                        }
                    }
                }.root
            }
            EXPIRY_DATE -> {
                LayoutCardExpiryDateBinding.inflate(inflater, container, true).apply {
                    cardExpiryDateEditText.addTextChangedListener(expiryDateTextWatcher)
                    cardExpiryDateEditText.apply {
                        addTextChangedListener {
                            if (checkIfCardExpired(it.toString()).not()) {
                                cardExpiryDateEditText.background =
                                    getDrawable(context, R.drawable.input_field_background)
                                cardExpiryDateCallbackFunc(it.toString(), false)
                            } else {
                                cardExpiryDateEditText.background =
                                    getDrawable(context, R.drawable.input_field_error_background)
                            }
                        }
                        onNext(hideKeyboard = false) {
                            if (it.isNotBlank()) {
                                cardExpiryDateCallbackFunc(it, true)
                            }
                        }
                    }
                }.root
            }
            CVV -> {
                LayoutCardCvvBinding.inflate(inflater, container, true).apply {
                    cardCvvEditText.apply {
                        addTextChangedListener {
                            cardCvvCallbackFunc(it.toString().toIntOrNull() ?: 0, false)
                        }
                        onDone {
                            if (it.isNotBlank()) {
                                cardCvvCallbackFunc(it.toIntOrNull() ?: 0, true)
                            }
                        }
                    }
                }.root
            }

        }
    }

    private fun checkIfCardExpired(text: String): Boolean {
        val month: String
        var year = ""
        if (text.length >= 2) {
            month = text.substring(0, 2)
            if (text.length > 2) {
                year = text.substring(3)
            }
            val mm = month.toInt()
            if (mm <= 0 || mm >= 13) {
                return true
            }
            if (text.length >= 4) {
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

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

}