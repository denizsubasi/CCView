package com.denizsubasi.creditcardview.lib

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.denizsubasi.creditcardview.lib.adapter.CardDetailsViewPagerAdapter
import com.denizsubasi.creditcardview.lib.databinding.ActivityAddCreditCardBinding
import com.denizsubasi.creditcardview.lib.utils.CardType
import com.denizsubasi.creditcardview.lib.utils.cardLength

class AddCreditCardActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityAddCreditCardBinding

    private lateinit var cardDetailsViewPagerAdapter: CardDetailsViewPagerAdapter

    private var lastViewPagerPosition = 0

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
        viewBinding.cardDetailsViewPager.addOnPageChangeListener(object :
            ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(newPosition: Int) {
                viewBinding.creditCardView.notifyPagerPositionChanged(newPosition)
                if (newPosition == 3) {
                    viewBinding.creditCardView.showBackOfCard()
                } else {
                    if (lastViewPagerPosition == 3) {
                        viewBinding.creditCardView.showFrontOfCard()
                    }
                }
                lastViewPagerPosition = newPosition
            }

        })
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

    private fun onCardNumberChanged(
        number: String,
        cardType: CardType,
        goNextField: Boolean = false
    ) {
        viewBinding.creditCardView.setCardNumber(number, cardType)
        viewBinding.creditCardView.setCardType(cardType)
        if (goNextField || number.length == cardType.cardLength()) nextInputField()
    }

    private fun onCardHolderNameChanged(holderName: String, goNextField: Boolean = false) {
        viewBinding.creditCardView.setCardHolderName(holderName)
        if (goNextField) nextInputField()
    }

    private fun onCardExpiryDateChanged(expiryDate: String, goNextField: Boolean = false) {
        viewBinding.creditCardView.setCardExpiryDate(expiryDate)
        if (goNextField) nextInputField()
    }

    private fun onCardCvvChanged(cvv: String, goNextField: Boolean = false) {
        viewBinding.creditCardView.setCardCvv(cvv)
        if (goNextField) nextInputField()
    }


}