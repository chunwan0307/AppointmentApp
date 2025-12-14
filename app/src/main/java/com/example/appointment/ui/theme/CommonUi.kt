package com.example.appointment.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.AccessTime

val AppBrown = Color(0xFF9C8B77)
val AppTextBrown = Color(0xFF6D4C41)
val AppMutedText = Color(0xFFBCAAA4)
val AppBackground = Color(0xFFF7F5F2)

val Schedule = Icons.Default.AccessTime

// common ui for home, appointment, account
@Composable
fun AppBottomNavigationBar(
    currentScreen: String,
    onHomeClick: () -> Unit,
    onAppointmentClick: () -> Unit,
    onAccountClick: () -> Unit,
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 0.dp
    ) {
        NavigationItem(
            label = "Home",
            icon = Icons.Outlined.Home,
            selectedIcon = Icons.Filled.Home,
            isSelected = currentScreen == "Home",
            onClick = onHomeClick
        )
        NavigationItem(
            label = "Appointment",
            icon = Icons.Outlined.DateRange,
            selectedIcon = Icons.Filled.DateRange,
            isSelected = currentScreen == "Appointment",
            onClick = onAppointmentClick
        )
        NavigationItem(
            label = "Account",
            icon = Icons.Outlined.Person,
            selectedIcon = Icons.Filled.Person,
            isSelected = currentScreen == "Account",
            onClick = onAccountClick
        )
    }
}

@Composable
fun RowScope.NavigationItem(
    label: String,
    icon: ImageVector,
    selectedIcon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val contentColor = if (isSelected) Color.Black else Color.Gray
    Column(
        modifier = Modifier
            .weight(1f)
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(if (isSelected) AppBrown.copy(alpha = 0.5f) else Color.Transparent)
                .padding(horizontal = 16.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isSelected) selectedIcon else icon,
                contentDescription = label,
                tint = contentColor
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, color = contentColor, fontSize = 12.sp)
    }
}
