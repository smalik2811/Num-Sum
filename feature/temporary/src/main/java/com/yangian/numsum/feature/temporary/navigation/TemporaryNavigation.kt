package com.yangian.numsum.feature.temporary.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.yangian.numsum.feature.temporary.ui.TemporaryRoute

const val TEMPORARY_ROUTE = "temporary"

fun NavGraphBuilder.temporaryScreen() {
    composable(route = TEMPORARY_ROUTE) {
        TemporaryRoute()
//        Table(
//            headers = listOf("Column1", "Column2", "Column3"),
//            rows = listOf(
//                listOf("Row1", "Row2", "Row3"),
//                listOf("Row4", "Row5", "Row6")
//            )
//        )
    }
}

@Composable
fun Table(
    headers: List<String>,
    rows: List<List<String?>>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Header row
        Row {
            headers.forEach { header ->
                Text(
                    text = header,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Data rows
        rows.forEach { row ->
            Row {
                row.forEach { cell ->
                    Text(
                        text = cell ?: "",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}