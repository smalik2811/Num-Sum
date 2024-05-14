package com.yangian.numsum.core.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.yangian.numsum.core.designsystem.theme.NumSumAppTheme
import com.yangian.numsum.core.model.CallResource

fun LazyListScope.callFeed(
    feedState: CallFeedUiState,
    modifier: Modifier = Modifier,
) {
    when(feedState) {
        is CallFeedUiState.Loading -> {
            item {
                CircularProgressIndicator()
            }
        }

        is CallFeedUiState.Success -> {
            items(
                items = feedState.feed,
                key = { item: CallResource ->
                    item.id
                }
            ) {
                callResource ->
                CallResourceListItem(
                    callResource = callResource
                )
            }
        }
    }
}

sealed interface CallFeedUiState {
    data object Loading : CallFeedUiState

    data class Success(
        val feed: List<CallResource>,
    ) : CallFeedUiState
}

@Preview
@Composable
private fun LoadingCallFeedPreview() {
    NumSumAppTheme {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(),
        ) {
            callFeed(
                feedState = CallFeedUiState.Loading,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}