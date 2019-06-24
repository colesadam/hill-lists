package uk.colessoft.android.hilllist.dao


enum class CountryClause(val sql: String) {
    SCOTLAND("country = 'S'"),
    ENGLAND("country = 'E'"),
    WALES("country = 'W'"),
    UK("country not in ('S','E','I','W')")
}