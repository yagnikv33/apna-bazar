package com.esjayit.apnabazar.data.model.response

data class DummyAddDemand(
    var itemId:String?,
    var subjectName: String?,
    var qty: String?,
    var rate:Float?,
    var amount:String?,
    var bunch: String?,
    var standard: String?
)

data class AddDemandForAPI(
    var itemid:String?,
    var qty: String?,
    var rate:String?,
    var amount:String?,
    var bunchqty: String?
)