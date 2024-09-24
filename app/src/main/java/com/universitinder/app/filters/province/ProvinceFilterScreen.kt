package com.universitinder.app.filters.province

import androidx.compose.runtime.Composable
import com.universitinder.app.components.FilterListWithCheckbox

@Composable
fun ProvinceFilterScreen(
    provinces: List<String>,
    checkedProvinces: List<String>,
    provinceOnCheckChange: (province: String) -> Unit
) {
    FilterListWithCheckbox(
        values = provinces,
        checkedValues = checkedProvinces,
        onCheckChange = provinceOnCheckChange
    )
}