package com.denizsubasi.creditcardview.lib

import android.os.Parcel
import android.os.Parcelable

data class CreditCardItem(
    var id: String = "",
    var holderName: String = "",
    var cardNumber: String = "",
    var expiryDate: String = "" ,
    var cvv: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(holderName)
        parcel.writeString(cardNumber)
        parcel.writeString(expiryDate)
        parcel.writeString(cvv)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CreditCardItem> {
        override fun createFromParcel(parcel: Parcel): CreditCardItem {
            return CreditCardItem(parcel)
        }

        override fun newArray(size: Int): Array<CreditCardItem?> {
            return arrayOfNulls(size)
        }
    }
}