package com.pwr.BookPlatform.data.models

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.pwr.BookPlatform.R

enum class SortType(val sortValue: String) {
    RATING_DESC("rating desc"),
    RATING_ASC("rating asc"),
    TITLE("title"),
    NEW("new"),
    OLD("old");
}

@Composable
fun SortType.getLabel(): String {
    return when(this) {
        SortType.RATING_DESC -> stringResource(R.string.sort_rating_desc)
        SortType.RATING_ASC -> stringResource(R.string.sort_rating_asc)
        SortType.TITLE -> stringResource(R.string.sort_title)
        SortType.NEW -> stringResource(R.string.sort_new)
        SortType.OLD -> stringResource(R.string.sort_old)
    }
}
