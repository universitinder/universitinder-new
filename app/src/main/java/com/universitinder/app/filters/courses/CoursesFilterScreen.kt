package com.universitinder.app.filters.courses

import androidx.compose.runtime.Composable
import com.universitinder.app.components.FilterListWithCheckbox

@Composable
fun CoursesFilterScreen(
    courses: List<String>,
    checkedCourses: List<String>,
    onCourseCheckChange: (course: String) -> Unit
) {
    FilterListWithCheckbox(
        values = courses,
        checkedValues = checkedCourses,
        onCheckChange = onCourseCheckChange
    )
}