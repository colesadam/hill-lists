package uk.colessoft.android.hilllist.dao

enum class IsHillClimbed(val sql: String) {
    YES("dateClimbed NOT NULL"),
    NO("dateClimbed IS NULL")
}