package com.example.appointment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.appointment.ui.*
import com.example.appointment.ui.theme.AppointmentTheme

// Data class for a past appointment to be reviewed
data class PastAppointment(
    val id: String,
    val date: String,
    val time: String,
    val branch: String,
    val stylist: String,
    val services: String
)

// --- Screen 1: Choose Appointment for Feedback ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseAppointmentForFeedbackScreen(
    pastAppointments: List<PastAppointment>,
    onAppointmentSelected: (String) -> Unit, // Passes the ID
    onNotificationClicked: () -> Unit,
    // Bottom navigation
    onNavigateToHome: () -> Unit,
    onNavigateToAppointments: () -> Unit,
    onNavigateToAccount: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { /* No title */ },
                actions = {
                    IconButton(onClick = onNotificationClicked) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = Color.Black.copy(alpha = 0.7f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppBrown)
            )
        },
        bottomBar = {
            AppBottomNavigationBar(
                currentScreen = "Account", // Assuming Feedback is part of Account
                onHomeClick = onNavigateToHome,
                onAppointmentClick = onNavigateToAppointments,
                onAccountClick = onNavigateToAccount
            )
        },
        containerColor = AppBackground
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Choose an Appointment",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    color = AppTextBrown,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            items(pastAppointments) { appointment ->
                PastAppointmentCard(
                    appointment = appointment,
                    onClick = { onAppointmentSelected(appointment.id) }
                )
            }
        }
    }
}

@Composable
fun PastAppointmentCard(appointment: PastAppointment, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E0E0)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = appointment.date,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                color = AppTextBrown
            )
            FeedbackDetailRow(icon = Icons.Default.Schedule, text = appointment.time)
            FeedbackDetailRow(icon = Icons.Default.LocationOn, text = appointment.branch)
            FeedbackDetailRow(icon = Icons.Default.Person, text = appointment.stylist)
            FeedbackDetailRow(icon = Icons.Default.ContentCut, text = appointment.services)
        }
    }
}

@Composable
fun FeedbackDetailRow(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AppTextBrown,
            modifier = Modifier.size(20.dp)
        )
        Text(text = text, style = MaterialTheme.typography.bodyLarge, color = AppTextBrown)
    }
}


// --- Screen 2: Submit Feedback and Rating ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubmitFeedbackScreen(
    onNavigateBack: () -> Unit,
    onSubmitFeedback: (rating: Int, feedbackText: String) -> Unit,
    // Bottom navigation
    onNavigateToHome: () -> Unit,
    onNavigateToAppointments: () -> Unit,
    onNavigateToAccount: () -> Unit
) {
    var rating by remember { mutableIntStateOf(0) }
    var feedbackText by remember { mutableStateOf("") }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showDiscardDialog by remember { mutableStateOf(false) }
    var showMissingRatingDialog by remember { mutableStateOf(false) }

    val hasUnsavedChanges = rating > 0 || feedbackText.isNotEmpty()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { /* No title */ },
                navigationIcon = {
                    IconButton(onClick = {
                        if (hasUnsavedChanges) {
                            showDiscardDialog = true
                        } else {
                            onNavigateBack()
                        }
                    }) {
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
            // NOTE: I am using the corrected Surface version from our previous discussion
            Surface(color = Color.White) {
                Column {
                    Button(
                        // 2. UPDATE the onClick logic
                        onClick = {
                            if (rating == 0) {
                                // If no rating is given, show the validation dialog
                                showMissingRatingDialog = true
                            } else {
                                // Otherwise, proceed with submission
                                onSubmitFeedback(rating, feedbackText)
                                showSuccessDialog = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 16.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AppTextBrown)
                    ) {
                        Text(
                            text = "Submit Feedback",
                            modifier = Modifier.padding(8.dp),
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                    AppBottomNavigationBar(
                        currentScreen = "Account",
                        onHomeClick = onNavigateToHome,
                        onAppointmentClick = onNavigateToAppointments,
                        onAccountClick = onNavigateToAccount
                    )
                }
            }
        },
        containerColor = AppBackground
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Submit Feedback",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                color = AppTextBrown
            )
            Text(
                text = "Rate Your Experience!",
                style = MaterialTheme.typography.titleMedium,
                color = AppMutedText
            )
            // Star Rating
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                (1..5).forEach { starIndex ->
                    Icon(
                        imageVector = if (starIndex <= rating) Icons.Filled.Star else Icons.Outlined.Star,
                        contentDescription = "Star $starIndex",
                        tint = if (starIndex <= rating) Color(0xFFFFC107) else AppTextBrown,
                        modifier = Modifier
                            .size(48.dp)
                            .padding(4.dp)
                            .clickable { rating = starIndex }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Feedback Text Field
            Text(
                text = "Write Your Feedback",
                style = MaterialTheme.typography.titleMedium,
                color = AppMutedText
            )
            OutlinedTextField(
                value = feedbackText,
                onValueChange = { feedbackText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                placeholder = { Text("Feedback....") },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AppBrown,
                    unfocusedBorderColor = AppMutedText,
                    cursorColor = AppTextBrown,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )
        }
    }

    if (showSuccessDialog) {
        FeedbackSuccessDialog(onDismiss = {
            showSuccessDialog = false
            onNavigateBack()
        })
    }

    if (showDiscardDialog) {
        DiscardChangesDialog(
            onConfirmDiscard = {
                showDiscardDialog = false
                onNavigateBack()
            },
            onDismiss = {
                showDiscardDialog = false
            }
        )
    }


    if (showMissingRatingDialog) {
        MissingRatingDialog(onDismiss = { showMissingRatingDialog = false })
    }
}

// --- Dialogs ---

@Composable
fun FeedbackSuccessDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
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
                    Text("Continue", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun MissingRatingDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Rating required",
                    tint = AppTextBrown,
                    modifier = Modifier.size(56.dp)
                )
                Text(
                    text = "Please provide a rating before submitting your feedback.",
                    style = MaterialTheme.typography.headlineSmall.copy(fontSize = 20.sp),
                    color = AppTextBrown,
                    textAlign = TextAlign.Center
                )
                Button(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppTextBrown),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Continue", color = Color.White)
                }
            }
        }
    }
}


@Composable
fun DiscardChangesDialog(onConfirmDiscard: () -> Unit, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "You have unsaved changes. Are you sure to discard them?",
                    style = MaterialTheme.typography.bodyLarge,
                    color = AppTextBrown,
                    textAlign = TextAlign.Center
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onConfirmDiscard,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f),
                        border = BorderStroke(1.dp, AppTextBrown)
                    ) {
                        Text("Discard", color = AppTextBrown)
                    }
                    Button(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AppTextBrown),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Continue", color = Color.White)
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, device = "id:pixel_4")
@Composable
fun ChooseAppointmentForFeedbackScreenPreview() {
    val samplePastAppointments = listOf(
        PastAppointment(
            "1",
            "11 July 2025",
            "12:00 PM",
            "Bangsar Branch",
            "Jackson",
            "Cut | Styling | Wash and Styling"
        ),
        PastAppointment(
            "2",
            "14 May 2025",
            "12:00 PM",
            "Bangsar Branch",
            "Jackson",
            "Cut | Color | Touch Up"
        ),
    )
    AppointmentTheme {
        ChooseAppointmentForFeedbackScreen(
            pastAppointments = samplePastAppointments,
            onAppointmentSelected = {},
            onNotificationClicked = {},
            onNavigateToHome = {},
            onNavigateToAppointments = {},
            onNavigateToAccount = {}
        )
    }
}

@Preview(showBackground = true, device = "id:pixel_4")
@Composable
fun SubmitFeedbackScreenPreview() {
    AppointmentTheme {
        SubmitFeedbackScreen(
            onNavigateBack = {},
            onSubmitFeedback = { _, _ -> },
            onNavigateToHome = {},
            onNavigateToAppointments = {},
            onNavigateToAccount = {}
        )
    }
}

@Preview(widthDp = 300)
@Composable
fun FeedbackSuccessDialogPreview() {
    AppointmentTheme {
        FeedbackSuccessDialog {}
    }
}

@Preview(widthDp = 300)
@Composable
fun DiscardChangesDialogPreview() {
    AppointmentTheme {
        DiscardChangesDialog({}, {})
    }
}