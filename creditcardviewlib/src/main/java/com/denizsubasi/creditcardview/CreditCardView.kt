package com.denizsubasi.creditcardview

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater.from
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.denizsubasi.creditcardview.ext.afterMeasured
import com.denizsubasi.creditcardview.ext.gone
import com.denizsubasi.creditcardview.ext.invisible
import com.denizsubasi.creditcardview.ext.visible
import com.denizsubasi.creditcardview.utils.CardType
import com.denizsubasi.creditcardview.utils.CardType.*
import com.denizsubasi.creditcardview.utils.cardNumberMask
import com.denizsubasi.creditcardview.utils.format


class CreditCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int? = 0
) : LinearLayout(context, attrs) {


    private val parentView = from(context).inflate(R.layout.credit_card_view, this, true)

    private val frontView = parentView.findViewById<View>(R.id.frontView)

    private val backView = parentView.findViewById<View>(R.id.backView)

    private val cardNumberTextView =
        parentView.findViewById<AppCompatTextView>(R.id.cardNumberTextView)

    private val cardFrontViewViewPointer =
        parentView.findViewById<AppCompatImageView>(R.id.cardFrontViewViewPointer)

    private val cardTypeImageView =
        parentView.findViewById<AppCompatImageView>(R.id.cardTypeImageView)

    private val cardHolderNameTextView =
        parentView.findViewById<AppCompatTextView>(R.id.cardHolderNameTextView)

    private val cardCvvTextView = parentView.findViewById<AppCompatTextView>(R.id.cardCvvTextView)

    private val expiryDateTextView =
        parentView.findViewById<AppCompatTextView>(R.id.expiryDateTextView)


    init {
        cardNumberTextView.afterMeasured {
            cardFrontViewViewPointer.moveTo(this)
        }
    }

    fun setCardNumber(cardNumber: String, cardType: CardType) {
        setCardType(cardType)
        cardNumberTextView.text = cardNumber
        cardNumberTextView.format(cardType.cardNumberMask())
        cardFrontViewViewPointer.moveTo(cardNumberTextView)
    }

    fun setCardHolderName(holderName: String) {
        cardFrontViewViewPointer.moveTo(cardHolderNameTextView)
        cardHolderNameTextView.text = holderName
    }

    fun setCardCvv(cvv: String) {
        cardCvvTextView.text = cvv
    }

    fun showBackOfCard() {
        val back = ObjectAnimator.ofFloat(frontView, "scaleX", 1f, 0f)
            .apply {
                interpolator = DecelerateInterpolator()
                duration = 500
            }
        val front = ObjectAnimator.ofFloat(backView, "scaleX", 0f, 1f)
            .apply {
                interpolator = AccelerateDecelerateInterpolator()
                duration = 500
            }
        back.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                super.onAnimationStart(animation)
                backView.gone()
            }

            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                frontView.gone()
                backView.visible()
                front.start()
            }
        })
        back.start()

    }

    fun showFrontOfCard() {
        val back = ObjectAnimator.ofFloat(backView, "scaleX", 1f, 0f)
            .apply {
                interpolator = DecelerateInterpolator()
                duration = 500
            }
        val front = ObjectAnimator.ofFloat(frontView, "scaleX", 0f, 1f)
            .apply {
                interpolator = AccelerateDecelerateInterpolator()
                duration = 500
            }
        back.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                super.onAnimationStart(animation)
                frontView.gone()
            }

            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                backView.gone()
                frontView.visible()
                front.start()
            }
        })
        back.start()
    }

    fun setCardExpiryDate(expiryDate: String) {
        expiryDateTextView.text = expiryDate
        cardFrontViewViewPointer.moveTo(expiryDateTextView)
    }

    private fun setCardType(cardType: CardType) {
        when (cardType) {
            UNKNOWN_CARD -> {
                cardTypeImageView.setImageResource(0)
                cardTypeImageView.setImageResource(0)
            }
            AMEX_CARD -> {
                cardTypeImageView.setImageResource(R.drawable.ic_amex)
                cardTypeImageView.setImageResource(R.drawable.ic_amex)
            }
            MASTER_CARD -> {
                cardTypeImageView.setImageResource(R.drawable.ic_mastercard)
                cardTypeImageView.setImageResource(R.drawable.ic_mastercard)
            }
            VISA_CARD -> {
                cardTypeImageView.setImageResource(R.drawable.ic_visa)
                cardTypeImageView.setImageResource(R.drawable.ic_visa)
            }
            DISCOVER_CARD -> {
                cardTypeImageView.setImageResource(R.drawable.ic_discover)
                cardTypeImageView.setImageResource(R.drawable.ic_discover)
            }
        }
    }

    private fun View.moveTo(targetView: View) {
        this.post {
            animate()
                .x(targetView.x)
                .y(targetView.y - 10)
                .setUpdateListener {
                    layoutParams = ConstraintLayout.LayoutParams(
                        targetView.width + 10,
                        targetView.height + 10
                    )
                }
            .start()
        }
    }

    fun notifyPagerPositionChanged(newPosition: Int) {

        when (newPosition) {
            0 -> {
                cardFrontViewViewPointer.moveTo(cardNumberTextView)
            }
            1 -> {
                cardFrontViewViewPointer.moveTo(cardHolderNameTextView)
            }
            2 -> {
                cardFrontViewViewPointer.moveTo(expiryDateTextView)
            }
            else -> {
                cardFrontViewViewPointer.moveTo(cardNumberTextView)
            }
        }
    }

    fun fillDefaultItems(item: CreditCardItem) {
        cardNumberTextView.text = item.cardNumber
        cardHolderNameTextView.text = item.holderName
        expiryDateTextView.text = item.expiryDate
        cardCvvTextView.text = item.cvv
    }

}