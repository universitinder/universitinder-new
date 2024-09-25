package com.universitinder.app.filters.duration

import androidx.compose.runtime.Composable
import com.universitinder.app.components.FilterListWithCheckbox

@Composable
fun DurationFilterScreen(
    durations: List<String>,
    checkedDurations: List<String>,
    onCheckChange: (newVal: String) -> Unit
) {
    FilterListWithCheckbox(
        values = durations,
        checkedValues = checkedDurations,
        onCheckChange = onCheckChange
    )
}