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

    private fun onCardNumberChanged(number: String) {
        viewBinding.creditCardView.setCardNumber(number)
    }

    private fun onCardHolderNameChanged(holderName: String) {
        viewBinding.creditCardView.setCardHolderName(holderName)
    }

    private fun onCardCvvChanged(cvv: Int) {
        viewBinding.creditCardView.setCardCvv(cvv)
    }

    private fun onCardExpiryDateChanged(expiryDate: String) {
        viewBinding.creditCardView.setCardExpiryDate(expiryDate)
    }


}