package com.universitinder.app.filters.city

import androidx.compose.runtime.Composable
import com.universitinder.app.components.FilterListWithCheckbox

@Composable
fun CityFilterScreen(
    cities: List<String>,
    checkedCities: List<String>,
    onCheckChange: (city: String) -> Unit
) {
    FilterListWithCheckbox(
        values = cities,
        checkedValues = checkedCities,
        onCheckChange = onCheckChange
    )
}