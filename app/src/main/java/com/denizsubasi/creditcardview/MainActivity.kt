package com.denizsubasi.creditcardview

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.denizsubasi.creditcardview.lib.AddCreditCardActivity
import com.denizsubasi.creditcardview.lib.CreditCardItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var creditCardsAdapter: CreditCardsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        creditCardsAdapter = CreditCardsAdapter { creditCardItem ->
            startCreditCardIntent(creditCardItem)
        }

        recyclerViewCreditCard.apply {
            adapter = creditCardsAdapter
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        }

        addCreditCard.setOnClickListener {
            startCreditCardIntent()
        }

    }

    private fun startCreditCardIntent(creditCardItem: CreditCardItem? = null) {
        startActivityForResult(
            AddCreditCardActivity.newIntent(
                this, creditCardItem
            ), creditCardIntentCode
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == creditCardIntentCode) {
            if (resultCode == Activity.RESULT_OK) {
                data?.extras?.getParcelable<CreditCardItem>(AddCreditCardActivity.KEY_CREDIT_CARD)
                    ?.let { creditCardItem ->
                        creditCardsAdapter.addCard(creditCardItem)
                    }
            }
        }
    }


    companion object {
        const val creditCardIntentCode = 101
    }
}
