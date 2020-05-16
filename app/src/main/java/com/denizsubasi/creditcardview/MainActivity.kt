package com.denizsubasi.creditcardview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.denizsubasi.creditcardview.lib.AddCreditCardActivity
import com.denizsubasi.creditcardview.lib.CreditCardItem

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startActivity(
            AddCreditCardActivity.newIntent(
                this
            )
        )
    }
}
