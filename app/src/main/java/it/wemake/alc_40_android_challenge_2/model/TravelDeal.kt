package it.wemake.alc_40_android_challenge_2.model

import android.os.Parcel
import android.os.Parcelable

class TravelDeal() : Parcelable {

    var title : String? = null
    var description : String? = null
    var price : String? = null
    var imageUri : String? = null
    var id : String? = null

    constructor(parcel: Parcel) : this() {
        title = parcel.readString()
        description = parcel.readString()
        price = parcel.readString()
        imageUri = parcel.readString()
        id = parcel.readString()
    }

    constructor(title : String, description : String, price : String, imageUri : String) : this(){

        this.title = title
        this.description = description
        this.price = price
        this.imageUri = imageUri

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(price)
        parcel.writeString(imageUri)
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TravelDeal> {
        override fun createFromParcel(parcel: Parcel): TravelDeal {
            return TravelDeal(parcel)
        }

        override fun newArray(size: Int): Array<TravelDeal?> {
            return arrayOfNulls(size)
        }
    }

    override fun equals(other: Any?): Boolean {

        return this.id == (other as TravelDeal).id

//        return super.equals(other)
    }

}