package com.denizsubasi.creditcardview.ext

import android.content.Context
import android.view.KeyEvent
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText


inline fun EditText.onImeAction(crossinline action: (text: String) -> Unit) {
    setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
        if ((event?.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
            action(text.toString())
            return@OnKeyListener true
        }
        false
    })
    setOnEditorActionListener { _, _, _ ->
        action(text.toString())
        true
    }
}

inline fun EditText.onDone(hideKeyboard: Boolean = true, crossinline action: (text: String) -> Unit) {
    imeOptions = EditorInfo.IME_ACTION_DONE
    onImeAction {
        if (hideKeyboard) hideKeyboard()
        action(text.toString())
    }
}

inline fun EditText.onNext(hideKeyboard: Boolean = true, crossinline action: (text: String) -> Unit) {
    imeOptions = EditorInfo.IME_ACTION_NEXT
    onImeAction {
        if (hideKeyboard) hideKeyboard()
        action(text.toString())
    }
}


fun View.gone() {
    this.visibility = View.GONE
}


fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.showKeyboard() {
    this.post {
        requestFocus()
        val imm =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun View.hideKeyboard() {
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

inline fun <T : View> T.afterMeasured(crossinline f: T.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                f()
            }
        }
    })
}
