package com.denizsubasi.creditcardview.lib.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.LayoutInflater.from
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.denizsubasi.creditcardview.lib.CardInputType.*
import com.denizsubasi.creditcardview.lib.databinding.LayoutCardCvvBinding
import com.denizsubasi.creditcardview.lib.databinding.LayoutCardExpiryDateBinding
import com.denizsubasi.creditcardview.lib.databinding.LayoutCardHolderNameBinding
import com.denizsubasi.creditcardview.lib.databinding.LayoutCardNumberBinding

class CardDetailsViewPagerAdapter(private val context: Context) : PagerAdapter() {

    private val items = getPagerItems()

    private val inflater: LayoutInflater
        get() = from(context)

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val item = items[position]
        return when (item.inputType) {
            NUMBER -> {
                LayoutCardNumberBinding.inflate(inflater, container, true).apply {

                }.root
            }
            HOLDER_NAME -> {
                LayoutCardHolderNameBinding.inflate(inflater, container, true).apply {

                }.root
            }
            CVV -> {
                LayoutCardCvvBinding.inflate(inflater, container, true).apply {

                }.root
            }
            EXPIRY_DATE -> {
                LayoutCardExpiryDateBinding.inflate(inflater, container, true).apply {

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