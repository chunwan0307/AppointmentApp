package com.example.appointment

import android.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.appointment.ui.AppBackground
import com.example.appointment.ui.AppBottomNavigationBar
import com.example.appointment.ui.AppBrown
import com.example.appointment.ui.AppMutedText
import com.example.appointment.ui.AppTextBrown
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle


data class Branch(
    val id: Int,
    val name: String,
    val phoneNumber: String,
    val address: String,
    val hours: String,
    val imageResId: Int
)

@Composable
fun InfoRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = AppMutedText,
            modifier = Modifier.padding(top = 2.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = AppMutedText
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    branches: List<Branch>,
    onNavigateBack: () -> Unit,
    onBranchSelected: (Branch) -> Unit,

    onNavigateToHome: () -> Unit,
    onNavigateToAppointments: () -> Unit,
    onNavigateToAccount: () -> Unit
) {
    var showDiscardDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { showDiscardDialog = true }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go back",
                            tint = Color.Black.copy(alpha = 0.7f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppBrown
                )
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Text
            item {
                Column {
                    Text(
                        text = "Appointment Booking",
                        style = MaterialTheme.typography.headlineSmall,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Bold,
                        color = AppTextBrown
                    )
                    Text(
                        text = "Select a branch",
                        style = MaterialTheme.typography.bodyLarge,
                        fontStyle = FontStyle.Italic,
                        color = AppMutedText
                    )
                }
            }

            // List of Branch Cards
            items(branches) { branch ->
                BranchCard(
                    branch = branch,
                    onClick = { onBranchSelected(branch) }
                )
            }
        }
    }


    if (showDiscardDialog) {
        DiscardBookingDialog(
            onConfirm = {
                showDiscardDialog = false
                onNavigateBack()
            },
            onDismiss = {
                showDiscardDialog = false
            }
        )
    }
}

@Composable
fun DiscardBookingDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
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
                // warning icon
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Discard warning",
                    tint = AppBrown,
                    modifier = Modifier.size(56.dp)
                )

                Text(
                    text = "Are you sure you want to discard this booking?",
                    style = MaterialTheme.typography.headlineSmall.copy(fontSize = 20.sp),
                    color = AppTextBrown,
                    textAlign = TextAlign.Center
                )

                Button(
                    onClick = onConfirm,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppTextBrown),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text(
                        text = "Discard",
                        modifier = Modifier.padding(vertical = 4.dp),
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun BranchCard(branch: Branch, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = branch.imageResId),
                contentDescription = "${branch.name} branch interior",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = branch.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    color = AppTextBrown
                )

                InfoRow(icon = Icons.Default.Phone, text = branch.phoneNumber)
                InfoRow(icon = Icons.Default.LocationOn, text = branch.address)
                InfoRow(icon = Icons.Default.Schedule, text = branch.hours)
            }
        }
    }
}

private val BranchDetailTextStyle = TextStyle(
    fontWeight = FontWeight.Normal,
    fontStyle = FontStyle.Normal,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = 0.25.sp
)

@Preview(showBackground = true, device = "id:pixel_4")
@Composable
fun BookingScreenPreview() {

    val sampleBranches = listOf(
        Branch(
            id = 1,
            name = "Cheras",
            phoneNumber = "03-72841935",
            address = "Lot 3A, Jalan Perniagaan Cheras 2, Pusat Perniagaan Cheras, 56100 Kuala Lumpur",
            hours = "Tue to Sun | 10am-2pm & 3pm-7pm\nMon | Closed",
            imageResId = com.example.appointment.R.drawable.cheras_branch
        ),
        Branch(
            id = 2,
            name = "Bangsar",
            phoneNumber = "03-22019876",
            address = "12, Jalan Telawi 3, Bangsar, 59100 Kuala Lumpur",
            hours = "Mon to Sat | 10am-8pm\nSun | Closed",
            imageResId = com.example.appointment.R.drawable.bangsar_branch
        )
    )

    MaterialTheme {
        BookingScreen(
            branches = sampleBranches,
            onNavigateBack = {},
            onBranchSelected = {},
            onNavigateToHome = {},
            onNavigateToAppointments = {},
            onNavigateToAccount = {}
        )
    }
}
