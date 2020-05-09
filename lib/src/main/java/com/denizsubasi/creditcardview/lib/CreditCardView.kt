package com.denizsubasi.creditcardview.lib

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater.from
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import com.denizsubasi.creditcardview.lib.databinding.CreditCardViewBinding
import com.denizsubasi.creditcardview.lib.ext.gone
import com.denizsubasi.creditcardview.lib.ext.visible


class CreditCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int? = 0
) : LinearLayout(context, attrs) {


    private val viewBinding: CreditCardViewBinding =
        CreditCardViewBinding.inflate(from(context), this, true)


    init {
        viewBinding.frontView.cardHolderNameTextView.text = "Deniz Subaşı"
        viewBinding.frontView.cardNumberTextView.text = "1244 4343 3434 3434"
    }

    fun setCardNumber(number: String) {
        viewBinding.frontView.cardNumberTextView.text = number
    }

    fun setCardHolderName(holderName: String) {
        viewBinding.frontView.cardHolderNameTextView.text = holderName
    }

    fun setCardCvv(cvv: Int) {
        viewBinding.backView.cardCvvTextView.text = cvv.toString()
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

    fun showFrontOfCard(){
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
    }

}