package com.example.appointment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.window.Dialog
import com.example.appointment.ui.AppBackground
import com.example.appointment.ui.AppBottomNavigationBar
import com.example.appointment.ui.AppBrown
import com.example.appointment.ui.AppTextBrown
import com.example.appointment.ui.theme.AppointmentTheme // Make sure this is imported

enum class AppointmentStatus {
    Approved, Pending, Rejected
}


data class Appointment(
    val id: String,
    val date: String,
    val time: String,
    val branch: String,
    val stylist: String,
    val services: List<String>,
    val status: AppointmentStatus
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentScreen(
    appointments: List<Appointment>,
    onCancelAppointment: (String) -> Unit,
    onBookAppointmentClicked: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToAccount: () -> Unit,
    onNotificationClicked: () -> Unit
) {
    var showCancelDialog by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
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
                currentScreen = "Appointment",
                onHomeClick = onNavigateToHome,
                onAppointmentClick = {},
                onAccountClick = onNavigateToAccount
            )
        },
        floatingActionButton = {
            Button(
                onClick = onBookAppointmentClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppTextBrown)
            ) {

                Text(
                    text = "Book Appointment",
                    style = MaterialTheme.typography.labelLarge,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(8.dp),
                    color = Color.White
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        containerColor = AppBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Upcoming Appointments",
                style = MaterialTheme.typography.headlineSmall,
                fontStyle = FontStyle.Italic,
                color = AppTextBrown,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            if (appointments.isEmpty()) {
                NoAppointmentsView()
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(appointments, key = { it.id }) { appointment ->
                        AppointmentDetailCard(
                            appointment = appointment,
                            onCancelClick = { showCancelDialog = appointment.id }
                        )
                    }
                }
            }
        }
    }


    if (showCancelDialog != null) {
        CancelAppointmentDialog(
            onConfirm = {
                onCancelAppointment(showCancelDialog!!)
                showCancelDialog = null
            },
            onDismiss = {
                showCancelDialog = null
            }
        )
    }
}

@Composable
fun AppointmentDetailCard(appointment: Appointment, onCancelClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E0E0)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = appointment.date,
                    style = MaterialTheme.typography.titleLarge, // Correct: No fontWeight override
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    color = AppTextBrown
                )
                AppointmentInfoRow(icon = Icons.Default.Schedule, text = appointment.time)
                AppointmentInfoRow(icon = Icons.Default.LocationOn, text = appointment.branch)
                AppointmentInfoRow(icon = Icons.Default.Person, text = appointment.stylist)
                AppointmentInfoRow(icon = Icons.Default.ContentCut, text = appointment.services.joinToString(" | "))
            }

            Spacer(modifier = Modifier.width(16.dp))


            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "status",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Black.copy(alpha = 0.6f)
                )
                Text(
                    text = appointment.status.name,
                    style = MaterialTheme.typography.bodyLarge, // Correct: No fontWeight override
                    color = when (appointment.status) {
                        AppointmentStatus.Approved -> Color(0xFF2E7D32) // Green
                        AppointmentStatus.Pending -> Color(0xFFF57F17) // Amber
                        AppointmentStatus.Rejected -> Color(0xFFC62828) // Red
                    }
                )
                Spacer(modifier = Modifier.height(8.dp)) // Extra space above the button
                Button(
                    onClick = onCancelClick,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppTextBrown),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        "Cancel",
                        style = MaterialTheme.typography.labelLarge, // Correct: Use theme style
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun AppointmentInfoRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AppTextBrown,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = AppTextBrown
        )
    }
}

@Composable
fun NoAppointmentsView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "- No Appointments -",
            style = MaterialTheme.typography.bodyLarge,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
    }
}

@Composable
fun CancelAppointmentDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
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
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Warning",
                    tint = AppTextBrown,
                    modifier = Modifier.size(56.dp)
                )
                Text(
                    text = "Are you sure you want to cancel this appointment?",
                    style = MaterialTheme.typography.titleLarge, // Correct: Use a direct style, no .copy()
                    color = AppTextBrown,
                    textAlign = TextAlign.Center
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f),
                        border = BorderStroke(1.dp, AppTextBrown)
                    ) {
                        Text(
                            "Cancel",
                            style = MaterialTheme.typography.labelLarge, // Correct: Use theme style
                            modifier = Modifier.padding(vertical = 4.dp),
                            color = AppTextBrown
                        )
                    }
                    Button(
                        onClick = onConfirm,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AppTextBrown),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            "Confirm",
                            style = MaterialTheme.typography.labelLarge, // Correct: Use theme style
                            modifier = Modifier.padding(vertical = 4.dp),
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, device = "id:pixel_4")
@Composable
fun AppointmentScreenWithDataPreview() {
    val sampleAppointments = listOf(
        Appointment(
            id = "1",
            date = "17 August 2025",
            time = "12:00 PM",
            branch = "Bangsar Branch",
            stylist = "Jackson",
            services = listOf("Color", "Touch Up"),
            status = AppointmentStatus.Approved
        ),
        Appointment(
            id = "2",
            date = "24 August 2025",
            time = "03:00 PM",
            branch = "Mid Valley",
            stylist = "Samantha",
            services = listOf("Haircut"),
            status = AppointmentStatus.Pending
        )
    )
    AppointmentTheme {
        AppointmentScreen(
            appointments = sampleAppointments,
            onCancelAppointment = {},
            onBookAppointmentClicked = {},
            onNavigateToHome = {},
            onNavigateToAccount = {},
            onNotificationClicked = {}
        )
    }
}

@Preview(showBackground = true, device = "id:pixel_4")
@Composable
fun AppointmentScreenEmptyPreview() {
    AppointmentTheme {
        AppointmentScreen(
            appointments = emptyList(),
            onCancelAppointment = {},
            onBookAppointmentClicked = {},
            onNavigateToHome = {},
            onNavigateToAccount = {},
            onNotificationClicked = {}
        )
    }
}

@Preview
@Composable
fun CancelAppointmentDialogPreview() {
    AppointmentTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            CancelAppointmentDialog(onConfirm = {}, onDismiss = {})
        }
    }
}
