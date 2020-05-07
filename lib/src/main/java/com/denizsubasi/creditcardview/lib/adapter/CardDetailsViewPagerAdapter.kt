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

class CardDetailsViewPagerAdapter(
    private val context: Context,
    private val cardNumberCallbackFunc: ((cardNumber: String) -> Unit),
    private val cardHolderNameCallbackFunc: ((holderName: String) -> Unit),
    private val cardCvvCallbackFunc: ((cvv: Int) -> Unit),
    private val cardExpiryDateCallbackFunc: ((expiryDate: String) -> Unit)
) : PagerAdapter() {

    private val items = getPagerItems()

    private val inflater: LayoutInflater
        get() = from(context)

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val item = items[position]
        return when (item.inputType) {
            NUMBER -> {
                LayoutCardNumberBinding.inflate(inflater, container, true).apply {
                    cardNumberEditText.addTextChangedListener {
                        cardNumberCallbackFunc(it.toString())
                    }
                }.root
            }
            HOLDER_NAME -> {
                LayoutCardHolderNameBinding.inflate(inflater, container, true).apply {
                    cardholderNameEditText.addTextChangedListener {
                        cardHolderNameCallbackFunc(it.toString())
                    }
                }.root
            }
            CVV -> {
                LayoutCardCvvBinding.inflate(inflater, container, true).apply {
                    cardCvvEditText.addTextChangedListener {
                        cardCvvCallbackFunc(it.toString().toIntOrNull() ?: 0)
                    }
                }.root
            }
            EXPIRY_DATE -> {
                LayoutCardExpiryDateBinding.inflate(inflater, container, true).apply {
                    cardExpiryDateEditText.addTextChangedListener {
                        cardExpiryDateCallbackFunc(it.toString())
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