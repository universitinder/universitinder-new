package com.universitinder.app.filters.privatePublic

import androidx.compose.runtime.Composable
import com.universitinder.app.components.FilterListWithCheckbox

@Composable
fun PrivatePublicFilterScreen(
    values: List<String>,
    checkedValues: List<String>,
    onCheckChange: (newVal: String) -> Unit
) {
    FilterListWithCheckbox(
        values = values,
        checkedValues = checkedValues,
        onCheckChange = onCheckChange
    )
}