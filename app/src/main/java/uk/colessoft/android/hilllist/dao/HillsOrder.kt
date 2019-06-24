package uk.colessoft.android.hilllist.dao

enum class HillsOrder(val sql: String) {
    ID_ASC("h_id asc"),
    ID_DESC("h_id desc"),
    NAME_ASC("upper(name) asc"),
    NAME_DESC("upper(name) desc"),
    HEIGHT_ASC("Metres asc"),
    HEIGHT_DESC("Metres desc")
}