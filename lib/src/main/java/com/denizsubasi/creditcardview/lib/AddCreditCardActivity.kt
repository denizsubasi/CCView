package com.denizsubasi.creditcardview.lib

import android.app.Activity
import android.content.Context
import android.content.Intent
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

    private var isShowingCardBackView = false

    private var isCardEditing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_credit_card)
        setClickListeners()
        setAdapters()

        intent?.extras?.getParcelable<CreditCardItem>(KEY_CREDIT_CARD)?.let {
            isCardEditing = true
            viewBinding.creditCardView.fillDefaultItems(it)
            cardDetailsViewPagerAdapter.fillDefaultItems(it)
        }
    }


    private fun setClickListeners() {
        viewBinding.nextInputFieldButton.setOnClickListener {
            if (isShowingCardBackView) {
                Intent()
                    .apply {
                        putExtra(
                            KEY_CREDIT_CARD,
                            cardDetailsViewPagerAdapter.getCreditCardItem()
                        )
                    }
                    .also { setResult(Activity.RESULT_OK, it) }
                    .also { finish() }
            }
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
                    viewBinding.nextInputFieldButton.text = if(isCardEditing) getString(R.string.edit) else getString(R.string.addCard)
                    isShowingCardBackView = true
                } else {
                    if (lastViewPagerPosition == 3) {
                        viewBinding.creditCardView.showFrontOfCard()
                    }
                    isShowingCardBackView = false
                    viewBinding.nextInputFieldButton.text = getString(R.string.next)
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


    companion object {
        const val KEY_CREDIT_CARD = "KEY_CREDIT_CARD"

        @JvmStatic
        fun newIntent(
            context: Context,
            creditCardItem: CreditCardItem? = null
        ): Intent {
            return Intent(context, AddCreditCardActivity::class.java)
                .apply {
                    putExtra(KEY_CREDIT_CARD, creditCardItem)
                }
        }
    }
}