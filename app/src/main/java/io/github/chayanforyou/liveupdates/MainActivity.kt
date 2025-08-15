package io.github.chayanforyou.liveupdates

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import io.github.chayanforyou.liveupdates.ui.theme.AppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                MainContent()
            }
        }
    }
}

@Composable
private fun MainContent() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LiveUpdateSample(
                modifier = Modifier.padding(innerPadding)
            )
        } else {
            UnsupportedVersionMessage(
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
private fun UnsupportedVersionMessage(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(stringResource(R.string.unsupported_message))
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LiveUpdateSample(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val notificationManager = remember {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
    SnackbarNotificationManager.initialize(context.applicationContext, notificationManager)

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { contentPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(horizontal = 16.dp)
        ) {
            Text(stringResource(R.string.live_update_summary_text))
            Spacer(modifier = Modifier.height(4.dp))

            NotificationPermission()

            Button(
                onClick = {
                    onCheckout()
                    scope.launch {
                        snackbarHostState.showSnackbar("Order placed")
                    }
                }
            ) {
                Text("Checkout")
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun onCheckout() {
    SnackbarNotificationManager.start()
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NotificationPermission() {
    @SuppressLint("InlinedApi")
    val notificationPermissionState = rememberPermissionState(
        android.Manifest.permission.POST_NOTIFICATIONS
    )

    if (!notificationPermissionState.status.isGranted) {
        NotificationPermissionCard(
            shouldShowRationale = notificationPermissionState.status.shouldShowRationale,
            onGrantClick = notificationPermissionState::launchPermissionRequest,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun NotificationPermissionCard(
    shouldShowRationale: Boolean,
    onGrantClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = stringResource(R.string.permission_message))

            if (shouldShowRationale) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = stringResource(R.string.permission_rationale))
            }

            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.BottomEnd
            ) {
                Button(onClick = onGrantClick) {
                    Text(text = stringResource(R.string.permission_grant))
                }
            }
        }
    }
}