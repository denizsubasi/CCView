package com.denizsubasi.creditcardview

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.denizsubasi.creditcardview.adapter.CardDetailsViewPagerAdapter
import com.denizsubasi.creditcardview.utils.CardType
import com.denizsubasi.creditcardview.utils.cardLength
import com.denizsubasi.creditcardview.utils.checkIfCardExpired
import com.denizsubasi.creditcardview.utils.selectCardType
import kotlinx.android.synthetic.main.activity_add_credit_card.*

class AddCreditCardActivity : AppCompatActivity() {

    private lateinit var cardDetailsViewPagerAdapter: CardDetailsViewPagerAdapter

    private var lastViewPagerPosition = 0

    private var cvvPagerPosition = 3

    private var isCardEditing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_credit_card)
        setAdapters()
        setClickListeners()
        intent?.extras?.getParcelable<CreditCardItem>(
            KEY_CREDIT_CARD
        )?.let {
            isCardEditing = true
            creditCardView.fillDefaultItems(it)
            cardDetailsViewPagerAdapter.fillDefaultItems(it)
        }
    }


    private fun setClickListeners() {
        nextInputFieldButton.setOnClickListener {
            if (isLastPage()) {
                if (checkValidation()) {
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
            } else {
                nextInputField()
            }
        }
        cardDetailsViewPager.addOnPageChangeListener(object :
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
                creditCardView.notifyPagerPositionChanged(newPosition)
                if (newPosition == cvvPagerPosition) {
                    creditCardView.showBackOfCard()
                    nextInputFieldButton.text =
                        if (isCardEditing) getString(R.string.edit) else getString(R.string.addCard)
                } else {
                    if (lastViewPagerPosition == cvvPagerPosition) {
                        creditCardView.showFrontOfCard()
                    }
                    nextInputFieldButton.text = getString(R.string.next)
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
        cardDetailsViewPager.adapter = cardDetailsViewPagerAdapter
    }

    private fun checkValidation(): Boolean {
        var isValid: Boolean
        with(cardDetailsViewPagerAdapter.getCreditCardItem()) {
            isValid = when {
                cardNumber.length != cardNumber.selectCardType().cardLength() -> {
                    cardDetailsViewPager.currentItem = 0
                    false
                }
                holderName.isBlank() -> {
                    cardDetailsViewPager.currentItem = 1
                    false
                }
                checkIfCardExpired() -> {
                    cardDetailsViewPager.currentItem = 2
                    false
                }
                cvv.isBlank() -> {
                    cardDetailsViewPager.currentItem = 3
                    false
                }
                else -> true
            }
        }
        return isValid
    }

    private fun nextInputField() {
        cardDetailsViewPager.currentItem =
            cardDetailsViewPager.currentItem + 1
    }

    private fun onCardNumberChanged(
        number: String,
        cardType: CardType,
        goNextField: Boolean = false
    ) {
        creditCardView.setCardNumber(number, cardType)
        if (goNextField) nextInputField()
    }

    private fun onCardHolderNameChanged(holderName: String, goNextField: Boolean = false) {
        creditCardView.setCardHolderName(holderName)
        if (goNextField) nextInputField()
    }

    private fun onCardExpiryDateChanged(expiryDate: String, goNextField: Boolean = false) {
        creditCardView.setCardExpiryDate(expiryDate)
        if (goNextField) nextInputField()
    }

    private fun onCardCvvChanged(cvv: String, goNextField: Boolean = false) {
        creditCardView.setCardCvv(cvv)
        if (goNextField) nextInputField()
    }

    private fun isLastPage(): Boolean = lastViewPagerPosition == cvvPagerPosition

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