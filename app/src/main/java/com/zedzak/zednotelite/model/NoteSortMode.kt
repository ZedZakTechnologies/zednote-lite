package com.zedzak.zednotelite.model

enum class NoteSortMode {
    LAST_EDITED,
    CREATED_DATE,
    TITLE
}

//enum class SortDirection {
//    ASC,
//    DESC
//}
data class NoteSortConfig(
    val mode: NoteSortMode,
    val direction: SortDirection
)