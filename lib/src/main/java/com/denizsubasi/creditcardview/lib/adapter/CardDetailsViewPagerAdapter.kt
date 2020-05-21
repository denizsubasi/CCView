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
import com.denizsubasi.creditcardview.lib.CreditCardItem
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
    private val cardCvvCallbackFunc: ((cvv: String, goNextField: Boolean) -> Unit),
    private val cardExpiryDateCallbackFunc: ((expiryDate: String, goNextField: Boolean) -> Unit)
) : PagerAdapter() {

    private val items = getPagerItems()

    private val inflater: LayoutInflater
        get() = from(context)

    private val expiryDateTextWatcher = MaskTextWatcher(CARD_EXPIRY_DATE_FORMAT)

    private var creditCardItem = CreditCardItem(id = UUID.randomUUID().toString())


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val item = items[position]
        return when (item.inputType) {
            NUMBER -> {
                LayoutCardNumberBinding.inflate(inflater, container, true).apply {
                    var applyingMask = false
                    cardNumberEditText.apply {
                        setText(creditCardItem.cardNumber)
                        doAfterTextChanged {
                            if (applyingMask) {
                                return@doAfterTextChanged
                            }
                            val cardType = it?.selectCardType() ?: CardType.UNKNOWN_CARD
                            applyingMask = true
                            cardNumberEditText.format(cardType.cardNumberMask())
                            applyingMask = false
                            creditCardItem.cardNumber = it.toString().replace(" ", "")
                            cardNumberCallbackFunc(creditCardItem.cardNumber, cardType,  creditCardItem.cardNumber.length == cardType.cardLength())
                        }
                        onNext(hideKeyboard = false) {
                            if (it.isNotBlank()) {
                                cardNumberCallbackFunc(
                                    creditCardItem.cardNumber,
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
                        setText(creditCardItem.holderName)
                        addTextChangedListener {
                            creditCardItem.holderName = it.toString()
                            cardHolderNameCallbackFunc(creditCardItem.holderName, false)
                        }
                        onNext(hideKeyboard = false) {
                            if (it.isNotBlank()) {
                                cardHolderNameCallbackFunc(creditCardItem.holderName, true)
                            }
                        }
                    }
                }.root
            }
            EXPIRY_DATE -> {
                LayoutCardExpiryDateBinding.inflate(inflater, container, true).apply {
                    cardExpiryDateEditText.addTextChangedListener(expiryDateTextWatcher)
                    cardExpiryDateEditText.apply {
                        setText(creditCardItem.expiryDate)
                        addTextChangedListener {
                            creditCardItem.expiryDate = it.toString()
                            if (creditCardItem.checkIfCardExpired().not()) {
                                cardExpiryDateEditText.background =
                                    getDrawable(context, R.drawable.input_field_background)
                                cardExpiryDateCallbackFunc(creditCardItem.expiryDate, false)
                            } else {
                                cardExpiryDateEditText.background =
                                    getDrawable(context, R.drawable.input_field_error_background)
                                cardExpiryDateCallbackFunc(it.toString(), false)
                            }
                        }
                        onNext(hideKeyboard = false) {
                            if (it.isNotBlank()) {
                                cardExpiryDateCallbackFunc(creditCardItem.expiryDate, true)
                            }
                        }
                    }
                }.root
            }
            CVV -> {
                LayoutCardCvvBinding.inflate(inflater, container, true).apply {
                    cardCvvEditText.apply {
                        setText(creditCardItem.cvv)
                        addTextChangedListener {
                            creditCardItem.cvv = it.toString()
                            cardCvvCallbackFunc(creditCardItem.cvv, false)
                        }
                        onDone {
                            if (it.isNotBlank()) {
                                cardCvvCallbackFunc(creditCardItem.cvv, true)
                            }
                        }
                    }
                }.root
            }

        }
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

    fun fillDefaultItems(creditCardItem: CreditCardItem) {
        this.creditCardItem = creditCardItem
    }

    fun getCreditCardItem() = creditCardItem
}