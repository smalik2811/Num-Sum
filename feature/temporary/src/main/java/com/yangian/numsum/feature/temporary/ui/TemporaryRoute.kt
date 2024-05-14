package com.yangian.numsum.feature.temporary.ui

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.provider.CallLog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yangian.numsum.core.designsystem.component.NumSumAppBackground
import com.yangian.numsum.core.designsystem.theme.NumSumAppTheme
import com.yangian.numsum.core.model.CallResource
import com.yangian.numsum.core.ui.callFeed
import com.yangian.numsum.feature.temporary.TemporaryViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun TemporaryRoute(
    temporaryViewModel: TemporaryViewModel = hiltViewModel()
) {
    val lastCallId by temporaryViewModel.lastCallId.collectAsState("")
    val feedState by temporaryViewModel.feedState.collectAsStateWithLifecycle()
    val ioScope = CoroutineScope(Dispatchers.IO)

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .padding(4.dp)
    ) {
        Text(
            modifier = Modifier,
            text = "Temporary Screen",
            style = MaterialTheme.typography.displaySmall,
            textAlign = TextAlign.Start,
        )

        // Checking and requesting permission
        val localContext = LocalContext.current
        var isCallLogPermissionGranted by remember {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    localContext,
                    Manifest.permission.READ_CALL_LOG
                ) == PackageManager.PERMISSION_GRANTED
            )
        }

        if (isCallLogPermissionGranted) {
            // Permission is already granted, proceed with the call log retrieval
            Text(
                modifier = Modifier,
                text = "Permission already Granted",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Start,
            )
        } else {
            // Permission is not granted, request it
            Button(
                onClick = {
                    ActivityCompat.requestPermissions(
                        localContext as Activity,
                        arrayOf(Manifest.permission.READ_CALL_LOG),
                        1
                    )

                    isCallLogPermissionGranted = ContextCompat.checkSelfPermission(
                        localContext,
                        Manifest.permission.READ_CALL_LOG
                    ) == PackageManager.PERMISSION_GRANTED
                },
                modifier = Modifier.wrapContentSize()

            ) {
                Text(
                    modifier = Modifier,
                    text = "Request Permission",
                    textAlign = TextAlign.Start,
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Add call records to the database
            Button(
                onClick = {
                    if (isCallLogPermissionGranted) {

                        val idColumn = CallLog.Calls._ID
                        val nameColumn = CallLog.Calls.CACHED_NAME
                        val numberColumn = CallLog.Calls.NUMBER
                        val dateColumn = CallLog.Calls.DATE
                        val durationColumn = CallLog.Calls.DURATION
                        val typeColumn = CallLog.Calls.TYPE

                        val projection = arrayOf(
                            idColumn,
                            nameColumn,
                            numberColumn,
                            dateColumn,
                            durationColumn,
                            typeColumn,
                        )

                        ioScope.launch {

                            val listOfCallResource = mutableListOf<CallResource>()

                            val cursor = localContext.contentResolver.query(
                                CallLog.Calls.CONTENT_URI,
                                projection,
                                "_id > ?",
                                arrayOf(lastCallId.toString()),
                                "_id ASC"
                            )

                            val idColIdx = cursor!!.getColumnIndex(idColumn)
                            val nameColIdx = cursor.getColumnIndex(nameColumn)
                            val numberColIdx = cursor.getColumnIndex(numberColumn)
                            val dateColIdx = cursor.getColumnIndex(dateColumn)
                            val durationColIdx = cursor.getColumnIndex(durationColumn)
                            val typeColIdx = cursor.getColumnIndex(typeColumn)


                            while (cursor.moveToNext()) {
                                val callResource = CallResource(
                                    id = cursor.getLong(idColIdx),
                                    name = cursor.getString(nameColIdx),
                                    number = cursor.getString(numberColIdx),
                                    timestamp = cursor.getLong(dateColIdx),
                                    duration = cursor.getLong(durationColIdx),
                                    type = cursor.getInt(typeColIdx),
                                )
                                println("Call Log: $callResource")
                                listOfCallResource.add(callResource)
                            }

                            cursor.close()

                            if (listOfCallResource.isNotEmpty()) {
                                val newLastCallId = listOfCallResource.last().id

                                temporaryViewModel.addCalls(listOfCallResource.toList())
                                temporaryViewModel.updateLastCallId(newLastCallId)
                            }
                        }
                    }
                }
            ) {
                Text("Show Logs")
            }

            Button(
                onClick = {
                    temporaryViewModel.uploadLogsToFirestore()
                }
            ) {
                Text("Upload Logs")
            }
        }
        // Display call records
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth(),
            ) {
                callFeed(
                    feedState = feedState,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE, device = "id:pixel_8_pro")
@Composable
private fun CallLogsDisplayPreview() {
    NumSumAppTheme {
        NumSumAppBackground {
            TemporaryRoute()
        }
    }
}