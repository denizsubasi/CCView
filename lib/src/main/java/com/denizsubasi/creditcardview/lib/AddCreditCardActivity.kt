package com.denizsubasi.creditcardview.lib

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.denizsubasi.creditcardview.lib.adapter.CardDetailsViewPagerAdapter
import com.denizsubasi.creditcardview.lib.databinding.ActivityAddCreditCardBinding

class AddCreditCardActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityAddCreditCardBinding

    private lateinit var cardDetailsViewPagerAdapter: CardDetailsViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_credit_card)
        setClickListeners()
        setAdapters()
    }

    private fun setClickListeners() {
        viewBinding.nextInputFieldButton.setOnClickListener {
            nextInputField()
        }
    }

    private fun setAdapters() {
        cardDetailsViewPagerAdapter =
            CardDetailsViewPagerAdapter(
                this,
                ::onCardNumberChanged,
                ::onCardHolderNameChanged,
                ::onCardCvvChanged,
                ::onCardExpiryDateChanged
            )
        viewBinding.cardDetailsViewPager.adapter = cardDetailsViewPagerAdapter
    }

    private fun nextInputField() {
        viewBinding.cardDetailsViewPager.currentItem =
            viewBinding.cardDetailsViewPager.currentItem + 1
    }

    private fun onCardNumberChanged(number: String, goNextField: Boolean = false) {
        viewBinding.creditCardView.setCardNumber(number)
        if (goNextField) nextInputField()
    }

    private fun onCardHolderNameChanged(holderName: String, goNextField: Boolean = false) {
        viewBinding.creditCardView.setCardHolderName(holderName)
        if (goNextField) nextInputField()
    }

    private fun onCardCvvChanged(cvv: Int, goNextField: Boolean = false) {
        viewBinding.creditCardView.setCardCvv(cvv)
        if (goNextField) nextInputField()
    }

    private fun onCardExpiryDateChanged(expiryDate: String, goNextField: Boolean = false) {
        viewBinding.creditCardView.setCardExpiryDate(expiryDate)
        if (goNextField) nextInputField()
    }


}