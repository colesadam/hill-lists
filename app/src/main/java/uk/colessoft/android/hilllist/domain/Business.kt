package uk.colessoft.android.hilllist.domain

data class Business (
    var latitude: Float = 0.toFloat(),
    var longitude: Float = 0.toFloat(),
    var companyname: String? = null,
    var resultNumber: Int = 0,
    var telephone: String? = null,
    var address: List<String>? = null,
    var postCode: String? = null,
    var scootLink: String? = null)

