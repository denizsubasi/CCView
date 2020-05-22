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
import androidx.constraintlayout.widget.ConstraintLayout
import com.denizsubasi.creditcardview.databinding.CreditCardViewBinding
import com.denizsubasi.creditcardview.ext.afterMeasured
import com.denizsubasi.creditcardview.ext.gone
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


    private val viewBinding: CreditCardViewBinding =
        CreditCardViewBinding.inflate(from(context), this, true)


    init {
        viewBinding.frontView.cardNumberTextView.afterMeasured {
            viewBinding.frontView.viewPointer.moveTo(this)
        }
    }

    fun setCardNumber(cardNumber: String, cardType: CardType) {
        setCardType(cardType)
        viewBinding.frontView.cardNumberTextView.text = cardNumber
        viewBinding.frontView.cardNumberTextView.format(cardType.cardNumberMask())
        viewBinding.frontView.viewPointer.moveTo(viewBinding.frontView.cardNumberTextView)
    }

    fun setCardHolderName(holderName: String) {
        viewBinding.frontView.viewPointer.moveTo(viewBinding.frontView.cardHolderNameTextView)
        viewBinding.frontView.cardHolderNameTextView.text = holderName
    }

    fun setCardCvv(cvv: String) {
        viewBinding.backView.cardCvvTextView.text = cvv
    }

    fun showBackOfCard() {
        val back = ObjectAnimator.ofFloat(viewBinding.frontView.root, "scaleX", 1f, 0f)
            .apply {
                interpolator = DecelerateInterpolator()
                duration = 500
            }
        val front = ObjectAnimator.ofFloat(viewBinding.backView.root, "scaleX", 0f, 1f)
            .apply {
                interpolator = AccelerateDecelerateInterpolator()
                duration = 500
            }
        back.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                super.onAnimationStart(animation)
                viewBinding.backView.root.gone()
            }

            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                viewBinding.frontView.root.gone()
                viewBinding.backView.root.visible()
                front.start()
            }
        })
        back.start()

    }

    fun showFrontOfCard() {
        val back = ObjectAnimator.ofFloat(viewBinding.backView.root, "scaleX", 1f, 0f)
            .apply {
                interpolator = DecelerateInterpolator()
                duration = 500
            }
        val front = ObjectAnimator.ofFloat(viewBinding.frontView.root, "scaleX", 0f, 1f)
            .apply {
                interpolator = AccelerateDecelerateInterpolator()
                duration = 500
            }
        back.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                super.onAnimationStart(animation)
                viewBinding.frontView.root.gone()
            }

            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                viewBinding.backView.root.gone()
                viewBinding.frontView.root.visible()
                front.start()
            }
        })
        back.start()
    }

    fun setCardExpiryDate(expiryDate: String) {
        viewBinding.frontView.expiryDateTextView.text = expiryDate
        viewBinding.frontView.viewPointer.moveTo(viewBinding.frontView.expiryDateTextView)
    }

    private fun setCardType(cardType: CardType) {
        when (cardType) {
            UNKNOWN_CARD -> {
                viewBinding.frontView.cardTypeImageView.setImageResource(0)
                viewBinding.backView.cardTypeImageView.setImageResource(0)
            }
            AMEX_CARD -> {
                viewBinding.frontView.cardTypeImageView.setImageResource(R.drawable.ic_amex)
                viewBinding.backView.cardTypeImageView.setImageResource(R.drawable.ic_amex)
            }
            MASTER_CARD -> {
                viewBinding.frontView.cardTypeImageView.setImageResource(R.drawable.ic_mastercard)
                viewBinding.backView.cardTypeImageView.setImageResource(R.drawable.ic_mastercard)
            }
            VISA_CARD -> {
                viewBinding.frontView.cardTypeImageView.setImageResource(R.drawable.ic_visa)
                viewBinding.backView.cardTypeImageView.setImageResource(R.drawable.ic_visa)
            }
            DISCOVER_CARD -> {
                viewBinding.frontView.cardTypeImageView.setImageResource(R.drawable.ic_discover)
                viewBinding.backView.cardTypeImageView.setImageResource(R.drawable.ic_discover)
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
                viewBinding.frontView.viewPointer.moveTo(viewBinding.frontView.cardNumberTextView)
            }
            1 -> {
                viewBinding.frontView.viewPointer.moveTo(viewBinding.frontView.cardHolderNameTextView)
            }
            2 -> {
                viewBinding.frontView.viewPointer.moveTo(viewBinding.frontView.expiryDateTextView)
            }
            else -> {
                viewBinding.frontView.viewPointer.moveTo(viewBinding.frontView.cardNumberTextView)
            }
        }
    }

    fun fillDefaultItems(item: CreditCardItem) {
        viewBinding.frontView.cardNumberTextView.text = item.cardNumber
        viewBinding.frontView.cardHolderNameTextView.text = item.holderName
        viewBinding.frontView.expiryDateTextView.text = item.expiryDate
        viewBinding.backView.cardCvvTextView.text = item.cvv
    }

}