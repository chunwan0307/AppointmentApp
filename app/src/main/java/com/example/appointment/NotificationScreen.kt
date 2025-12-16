package com.example.appointment

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appointment.ui.*
import com.example.appointment.ui.theme.AppointmentTheme

data class NotificationItem(
    val title: String,
    val subtitle: String,
    val date: String,
    val time: String,
    val branch: String,
    val stylist: String,
    val service: String,
    val isCancelled: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    notifications: List<NotificationItem>,
    onNavigateBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToAppointments: () -> Unit,
    onNavigateToAccount: () -> Unit
) {
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
                currentScreen = "",
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
                    text = "Alert",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = AppTextBrown
                )
            }

            items(notifications.size) { index ->
                NotificationCard(item = notifications[index])
            }
        }
    }
}

@Composable
fun NotificationCard(item: NotificationItem) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E0E0)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    textDecoration = TextDecoration.Underline
                ),
                fontWeight = FontWeight.Bold,
                color = AppTextBrown
            )
            if (item.subtitle.isNotEmpty()) {
                Text(
                    text = item.subtitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AppTextBrown
                )
            }
            Text(
                text = item.date,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = AppTextBrown
            )
            Spacer(modifier = Modifier.height(8.dp))
            NotificationDetailRow(icon = Icons.Default.Schedule, text = item.time)
            NotificationDetailRow(icon = Icons.Default.LocationOn, text = item.branch)
            NotificationDetailRow(icon = Icons.Default.Person, text = item.stylist)
            NotificationDetailRow(icon = Icons.Filled.ContentCut, text = item.service)
        }
    }
}

@Composable
fun NotificationDetailRow(icon: ImageVector, text: String) {
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
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = AppTextBrown,
            fontSize = 16.sp
        )
    }
}

@Preview(showBackground = true, device = "id:pixel_4")
@Composable
fun NotificationScreenPreview() {
    val sampleNotifications = listOf(
        NotificationItem(
            title = "Booking Cancelled:",
            subtitle = "Due to bad weather",
            date = "20 August 2025",
            time = "12:00 PM",
            branch = "Bangsar Branch",
            stylist = "Jackson",
            service = "Color | Touch Up",
            isCancelled = true
        ),
        NotificationItem(
            title = "Booking Reminder:",
            subtitle = "Your Appointment Tomorrow",
            date = "17 August 2025",
            time = "12:00 PM",
            branch = "Bangsar Branch",
            stylist = "Jackson",
            service = "Color | Touch Up"
        )
    )
    AppointmentTheme {
        NotificationScreen(
            notifications = sampleNotifications,
            onNavigateBack = {},
            onNavigateToHome = {},
            onNavigateToAppointments = {},
            onNavigateToAccount = {}
        )
    }
}