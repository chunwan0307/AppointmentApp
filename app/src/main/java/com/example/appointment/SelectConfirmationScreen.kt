package com.example.appointment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.appointment.ui.AppBackground
import com.example.appointment.ui.AppBottomNavigationBar
import com.example.appointment.ui.AppBrown
import com.example.appointment.ui.AppTextBrown

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectConfirmationScreen(

    date: String,
    time: String,
    branch: String,
    stylist: String,
    services: List<String>,
    // Navigation Callbacks
    onNavigateBack: () -> Unit,
    onConfirmBooking: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToAppointments: () -> Unit,
    onNavigateToAccount: () -> Unit
) {
    var showSuccessDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go back",
                            tint = Color.Black.copy(alpha = 0.7f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppBrown)
            )
        },
        bottomBar = {
            AppBottomNavigationBar(
                currentScreen = "Appointment",
                onHomeClick = onNavigateToHome,
                onAppointmentClick = onNavigateToAppointments,
                onAccountClick = onNavigateToAccount
            )
        },
        containerColor = AppBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Confirmation Details
            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                // Header
                Column {
                    Text(
                        text = "Appointment Booking",
                        style = MaterialTheme.typography.headlineSmall,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Bold,
                        color = AppTextBrown
                    )
                    Text(
                        text = "Confirmation",
                        style = MaterialTheme.typography.bodyLarge,
                        fontStyle = FontStyle.Italic,
                        color = AppTextBrown.copy(alpha = 0.8f)
                    )
                }

                ConfirmationDetailItem(label = "Date", value = date)
                ConfirmationDetailItem(label = "Time", value = time)
                ConfirmationDetailItem(label = "Branch", value = branch)
                ConfirmationDetailItem(label = "Stylist", value = stylist)
                ConfirmationDetailItem(label = "Service", value = services.joinToString(" | "))
            }

            Button(
                onClick = {
                    onConfirmBooking()
                    showSuccessDialog = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppTextBrown)
            ) {
                Text(
                    text = "Confirm",
                    modifier = Modifier.padding(8.dp),
                    fontStyle = FontStyle.Italic,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }
    }

    if (showSuccessDialog) {
        SuccessDialog(
            onDismiss = {
                showSuccessDialog = false
                onNavigateToHome()
            }
        )
    }
}

@Composable
fun ConfirmationDetailItem(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = AppTextBrown.copy(alpha = 0.7f),
            fontWeight = FontWeight.Normal
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            color = AppTextBrown,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun SuccessDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircleOutline,
                    contentDescription = "Success",
                    tint = AppTextBrown,
                    modifier = Modifier.size(64.dp)
                )
                Text(
                    text = "Success!",
                    style = MaterialTheme.typography.headlineSmall,
                    color = AppTextBrown,
                    textAlign = TextAlign.Center
                )
                Button(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppTextBrown),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Continue",
                        modifier = Modifier.padding(vertical = 4.dp),
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_4")
@Composable
fun SelectConfirmationScreenPreview() {
    MaterialTheme {
        SelectConfirmationScreen(
            date = "17 August 2025",
            time = "12:00PM",
            branch = "Bangsar Branch",
            stylist = "Jackson",
            services = listOf("Color", "Touch Up"),
            onNavigateBack = {},
            onConfirmBooking = {},
            onNavigateToHome = {},
            onNavigateToAppointments = {},
            onNavigateToAccount = {}
        )
    }
}

@Preview
@Composable
fun SuccessDialogPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            SuccessDialog(onDismiss = {})
        }
    }
}