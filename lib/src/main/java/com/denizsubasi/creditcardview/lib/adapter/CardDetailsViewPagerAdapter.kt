package com.denizsubasi.creditcardview.lib.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.LayoutInflater.from
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.viewpager.widget.PagerAdapter
import com.denizsubasi.creditcardview.lib.CardInputType.*
import com.denizsubasi.creditcardview.lib.databinding.LayoutCardCvvBinding
import com.denizsubasi.creditcardview.lib.databinding.LayoutCardExpiryDateBinding
import com.denizsubasi.creditcardview.lib.databinding.LayoutCardHolderNameBinding
import com.denizsubasi.creditcardview.lib.databinding.LayoutCardNumberBinding
import com.denizsubasi.creditcardview.lib.ext.onDone
import com.denizsubasi.creditcardview.lib.ext.onNext

class CardDetailsViewPagerAdapter(
    private val context: Context,
    private val cardNumberCallbackFunc: ((cardNumber: String, goNextField: Boolean) -> Unit),
    private val cardHolderNameCallbackFunc: ((holderName: String, goNextField: Boolean) -> Unit),
    private val cardCvvCallbackFunc: ((cvv: Int, goNextField: Boolean) -> Unit),
    private val cardExpiryDateCallbackFunc: ((expiryDate: String, goNextField: Boolean) -> Unit)
) : PagerAdapter() {

    private val items = getPagerItems()

    private val inflater: LayoutInflater
        get() = from(context)

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val item = items[position]
        return when (item.inputType) {
            NUMBER -> {
                LayoutCardNumberBinding.inflate(inflater, container, true).apply {
                    cardNumberEditText.apply {
                        addTextChangedListener {
                            cardNumberCallbackFunc(it.toString(),false)
                        }
                        onNext(hideKeyboard = false) {
                            // TODO card number validation
                            if (it.isNotBlank()) {
                                cardNumberCallbackFunc(it, true)
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
                            // TODO card holder name validation
                            if (it.isNotBlank()) {
                                cardHolderNameCallbackFunc(it, true)
                            }
                        }
                    }
                }.root
            }
            EXPIRY_DATE -> {
                LayoutCardExpiryDateBinding.inflate(inflater, container, true).apply {
                    cardExpiryDateEditText.apply {
                        addTextChangedListener {
                            cardExpiryDateCallbackFunc(it.toString(),false)
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
                            // TODO card cvv validation
                            if (it.isNotBlank()) {
                                cardCvvCallbackFunc(it.toIntOrNull() ?: 0, true)
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

}