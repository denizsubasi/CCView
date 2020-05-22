package com.denizsubasi.creditcardview.adapter

import androidx.annotation.LayoutRes
import com.denizsubasi.creditcardview.CardInputType
import com.denizsubasi.creditcardview.R


class PagerItem(@LayoutRes val layoutRes: Int, val inputType: CardInputType)

fun getPagerItems(): List<PagerItem> {
    return listOf(
        PagerItem(
            R.layout.layout_card_number,
            CardInputType.NUMBER
        ),
        PagerItem(
            R.layout.layout_card_holder_name,
            CardInputType.HOLDER_NAME
        ),
        PagerItem(
            R.layout.layout_card_expiry_date,
            CardInputType.EXPIRY_DATE
        ),
        PagerItem(
            R.layout.layout_card_cvv,
            CardInputType.CVV
        )
    )
}
