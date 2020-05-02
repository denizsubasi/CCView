package com.denizsubasi.creditcardview.lib

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.denizsubasi.creditcardview.lib.databinding.ActivityAddCreditCardBinding

class AddCreditCardActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityAddCreditCardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_credit_card)

    }

}