package com.denizsubasi.creditcardview.lib

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater.from
import android.widget.LinearLayout
import com.denizsubasi.creditcardview.lib.databinding.LayoutCreditCardBinding

class CreditCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int? = 0
) : LinearLayout(context, attrs) {


    private val viewBinding: LayoutCreditCardBinding =
        LayoutCreditCardBinding.inflate(from(context), this, true)

    init {
        viewBinding.cardHolderNameTextView.text = "Deniz Subaşı"
        viewBinding.cardNumberTextView.text = "1244 4343 3434 3434"
    }

}