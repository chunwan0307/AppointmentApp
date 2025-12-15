package com.example.appointment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appointment.ui.*
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.window.Dialog
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.ui.text.font.FontStyle

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SelectDateScreen(
    onNavigateBack: () -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    onTimeSelected: (String) -> Unit,
    onConfirmClicked: () -> Unit,

    onNavigateToHome: () -> Unit,
    onNavigateToAppointments: () -> Unit,
    onNavigateToAccount: () -> Unit
) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedTime by remember { mutableStateOf<String?>(null) }
    var currentMonth by remember { mutableStateOf(YearMonth.from(selectedDate)) }
    var showMissingTimeDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        onDateSelected(selectedDate)
    }

    val availableTimes =
        listOf("10:00 AM", "12:00 PM", "01:00 PM", "03:00 PM", "04:00 PM", "05:00 PM")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { /* No title */ },
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally // Center content horizontally
        ) {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Appointment Booking",
                        style = MaterialTheme.typography.headlineSmall,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Bold,
                        color = AppTextBrown,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "Select a time slot",
                        style = MaterialTheme.typography.bodyLarge,
                        fontStyle = FontStyle.Italic,
                        color = AppMutedText,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            item {
                CalendarView(
                    currentMonth = currentMonth,
                    selectedDate = selectedDate,
                    onDateSelected = { date ->
                        selectedDate = date
                        onDateSelected(date)
                    },
                    onMonthChanged = { newMonth -> currentMonth = newMonth }
                )
            }

            // -- Time Slots --
            item {
                Spacer(modifier = Modifier.height(24.dp))
                FlowRow(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    availableTimes.forEach { time ->
                        TimeSlotChip(
                            time = time,
                            isSelected = time == selectedTime,
                            onClick = {
                                selectedTime = time
                                onTimeSelected(time)
                            }
                        )
                    }
                }
            }

            //  Confirm Button
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        if (selectedTime == null) {
                            showMissingTimeDialog = true
                        } else {

                            onConfirmClicked()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.9f),
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
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    if (showMissingTimeDialog) {
        MissingTimeDialog(onDismiss = { showMissingTimeDialog = false })
    }
}

@Composable
fun MissingTimeDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = "Warning",
                    tint = AppTextBrown,
                    modifier = Modifier.size(56.dp)
                )
                Text(
                    text = "Please select an available time slot to continue.",
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
fun InvalidDateDialog(onDismiss: () -> Unit) {
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
                    imageVector = Icons.Filled.Warning,
                    contentDescription = "Warning",
                    tint = AppTextBrown,
                    modifier = Modifier.size(56.dp)
                )
                Text(
                    text = "You can only select a future date for an appointment.",
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
fun CalendarView(
    currentMonth: YearMonth,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onMonthChanged: (YearMonth) -> Unit) {
    val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")
    val today = LocalDate.now()

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0ECE5)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Month Header (This part remains the same)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = currentMonth.format(monthTitleFormatter),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Row {
                    IconButton(onClick = { onMonthChanged(currentMonth.minusMonths(1)) }) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, "Previous month")
                    }
                    IconButton(onClick = { onMonthChanged(currentMonth.plusMonths(1)) }) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, "Next month")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                val daysOfWeek = listOf("S", "M", "T", "W", "T", "F", "S")
                daysOfWeek.forEach { day ->
                    Text(
                        text = day,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = AppMutedText
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Calendar Grid
            val firstDayOfMonth = currentMonth.atDay(1)
            val firstDayOfWeek = firstDayOfMonth.dayOfWeek.get(WeekFields.SUNDAY_START.dayOfWeek()) % 7
            val daysInMonth = currentMonth.lengthOfMonth()
            val totalSlots = (firstDayOfWeek + daysInMonth)
            val numRows = (totalSlots + 6) / 7

            Column {
                repeat(numRows) { row ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        repeat(7) { column ->
                            val dayIndex = (row * 7 + column) - firstDayOfWeek + 1
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                if (dayIndex in 1..daysInMonth) {
                                    val date = currentMonth.atDay(dayIndex)
                                    val isPastDate = date.isBefore(today)

                                    DayCell(
                                        date = date,
                                        isSelected = date == selectedDate,
                                        isToday = date == today,
                                        isClickable = !isPastDate,
                                        onClick = { onDateSelected(date) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun DayCell(
    date: LocalDate,
    isSelected: Boolean,
    isToday: Boolean,
    isClickable: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected && isClickable) AppBrown else Color.Transparent

    val textColor = when {
        isSelected && isClickable -> Color.White
        isClickable -> AppTextBrown
        else -> AppMutedText
    }
    val border = if (isToday && !isSelected && isClickable) BorderStroke(1.dp, AppBrown) else null

    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .then(if (border != null) Modifier.border(border, CircleShape) else Modifier)
            .clickable(enabled = isClickable, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            color = textColor,
            fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal,
            fontSize = 14.sp
        )
    }
}

@Composable
fun TimeSlotChip(
    time: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) AppBrown else Color.Transparent
    val contentColor = if (isSelected) Color.White else AppTextBrown
    val borderColor = if (isSelected) AppBrown else AppMutedText

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .border(1.dp, borderColor, RoundedCornerShape(50))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = time,
            color = contentColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview(showBackground = true, device = "id:pixel_4")
@Composable
fun SelectDateScreenPreview() {
    if (LocalInspectionMode.current) {
        com.jakewharton.threetenabp.AndroidThreeTen.init(LocalContext.current)
    }

    MaterialTheme {
        SelectDateScreen(
            onNavigateBack = {},
            onDateSelected = {},
            onTimeSelected = {},
            onConfirmClicked = {},
            onNavigateToHome = {},
            onNavigateToAppointments = {},
            onNavigateToAccount = {}
        )
    }
}
