package it.wemake.alc_40_android_challenge_2.model

class TravelDeal(){

    var title : String? = null
    var description : String? = null
    var price : String? = null
    var imageUri : String? = null
    var id : String? = null

    constructor(title : String, description : String, price : String, imageUri : String) : this(){

        this.title = title
        this.description = description
        this.price = price
        this.imageUri = imageUri

    }

}