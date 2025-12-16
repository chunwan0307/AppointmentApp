package com.example.appointment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.appointment.ui.theme.AppointmentTheme
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.Date
import java.util.Locale

enum class Screen {
    AppointmentList,
    SelectBranch,
    SelectStylist,
    SelectDate,
    SelectService,
    SelectConfirmation,
    Notification,
    ChooseFeedback,
    SubmitFeedback
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppointmentTheme {
                var currentScreen by remember { mutableStateOf(Screen.AppointmentList) }

                //  State for the entire booking flow
                var selectedBranch by remember { mutableStateOf<Branch?>(null) }
                var selectedStylist by remember { mutableStateOf<Stylist?>(null) }
                var selectedDate by remember { mutableStateOf<Long?>(null) }
                var selectedTime by remember { mutableStateOf<String?>(null) }

                // State for each individual service dropdown
                var selectedCut by remember { mutableStateOf<String?>(null) }
                var selectedColor by remember { mutableStateOf<String?>(null) }
                var selectedTreatment by remember { mutableStateOf<String?>(null) }
                var selectedStyling by remember { mutableStateOf<String?>(null) }

                // State for the feedback flow
                var selectedFeedbackAppointmentId by remember { mutableStateOf<String?>(null) }

                // Main navigation logic
                when (currentScreen) {
                    Screen.AppointmentList -> {
                        AppointmentScreen(
                            appointments = listOf(
                                Appointment(
                                    id = "1",
                                    date = "17 August 2025",
                                    time = "12:00 PM",
                                    branch = "Bangsar Branch",
                                    stylist = "Jackson",
                                    services = listOf("Color", "Touch Up"),
                                    status = AppointmentStatus.Approved
                                )
                            ),
                            onCancelAppointment = { appointmentId ->
                                println("User wants to cancel appointment: $appointmentId")
                            },
                            onBookAppointmentClicked = {
                                selectedBranch = null
                                selectedStylist = null
                                selectedDate = null
                                selectedTime = null
                                selectedCut = null
                                selectedColor = null
                                selectedTreatment = null
                                selectedStyling = null
                                currentScreen = Screen.SelectBranch
                            },
                            onNavigateToHome = { currentScreen = Screen.AppointmentList },
                            onNavigateToAccount = { currentScreen = Screen.ChooseFeedback },
                            onNotificationClicked = {
                                currentScreen = Screen.Notification
                            }
                        )
                    }

                    Screen.SelectBranch -> {
                        val sampleBranches = listOf(
                            Branch(id = 1, name = "Cheras Branch", phoneNumber = "03-72841935",
                                address = "Lot 3A, Jalan Perniagaan Cheras 2, Pusat Perniagaan Cheras, 56100 Kuala Lumpur",
                                hours = "Tue to Sun | 10am-2pm & 3pm-7pm\nMon | Closed",
                                imageResId = R.drawable.cheras_branch),
                            Branch(id = 2, name = "Bangsar Branch", phoneNumber = "03-22019876",
                                address = "12, Jalan Telawi 3, Bangsar, 59100 Kuala Lumpur",
                                hours = "Mon to Sat | 10am-8pm\nSun | Closed",
                                imageResId = R.drawable.bangsar_branch)
                        )

                        BookingScreen(
                            branches = sampleBranches,
                            onNavigateBack = { currentScreen = Screen.AppointmentList },
                            onBranchSelected = { branch ->
                                selectedBranch = branch
                                currentScreen = Screen.SelectStylist
                            },
                            onNavigateToHome = { currentScreen = Screen.AppointmentList },
                            onNavigateToAppointments = { currentScreen = Screen.AppointmentList },
                            onNavigateToAccount = { currentScreen = Screen.ChooseFeedback }
                        )
                    }

                    Screen.SelectStylist -> {
                        val sampleStylists = listOf(
                            Stylist(id = 1, name = "Jackson", imageResId = R.drawable.jackson_stylist),
                            Stylist(id = 2, name = "Minnie", imageResId = R.drawable.minnie_stylist),
                            Stylist(id = 3, name = "Winter", imageResId = R.drawable.winter_stylist),
                            Stylist(id = 4, name = "Leo", imageResId = R.drawable.leo_stylist)
                        )

                        SelectStylistScreen(
                            stylists = sampleStylists,
                            onNavigateBack = { currentScreen = Screen.SelectBranch },
                            onStylistSelected = { stylist ->
                                selectedStylist = stylist
                                currentScreen = Screen.SelectDate
                            },
                            onNavigateToHome = { currentScreen = Screen.AppointmentList },
                            onNavigateToAppointments = { currentScreen = Screen.AppointmentList },
                            onNavigateToAccount = { currentScreen = Screen.ChooseFeedback }
                        )
                    }

                    Screen.SelectDate -> {
                        SelectDateScreen(
                            onNavigateBack = { currentScreen = Screen.SelectStylist },
                            onDateSelected = { localDate ->
                                val zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault())
                                selectedDate = zonedDateTime.toInstant().toEpochMilli()
                            },
                            onTimeSelected = { time -> selectedTime = time },
                            onConfirmClicked = { currentScreen = Screen.SelectService },
                            onNavigateToHome = { currentScreen = Screen.AppointmentList },
                            onNavigateToAppointments = { currentScreen = Screen.AppointmentList },
                            onNavigateToAccount = { currentScreen = Screen.ChooseFeedback }
                        )
                    }

                    Screen.SelectService -> {
                        SelectServiceScreen(
                            selectedCut = selectedCut,
                            selectedColor = selectedColor,
                            selectedTreatment = selectedTreatment,
                            selectedStyling = selectedStyling,
                            onCutChange = { selectedCut = it },
                            onColorChange = { selectedColor = it },
                            onTreatmentChange = { selectedTreatment = it },
                            onStylingChange = { selectedStyling = it },
                            onNavigateBack = { currentScreen = Screen.SelectDate },
                            onConfirmClicked = { currentScreen = Screen.SelectConfirmation },
                            onNavigateToHome = { currentScreen = Screen.AppointmentList },
                            onNavigateToAppointments = { currentScreen = Screen.AppointmentList },
                            onNavigateToAccount = { currentScreen = Screen.ChooseFeedback }
                        )
                    }

                    Screen.SelectConfirmation -> {
                        val finalServices = listOfNotNull(
                            selectedCut,
                            selectedColor,
                            selectedTreatment,
                            selectedStyling
                        ).filter { it != "None" }

                        SelectConfirmationScreen(
                            date = selectedDate?.toDateString() ?: "Not selected",
                            time = selectedTime ?: "Not selected",
                            branch = selectedBranch?.name ?: "Not selected",
                            stylist = selectedStylist?.name ?: "Not selected",
                            services = finalServices,
                            onNavigateBack = { currentScreen = Screen.SelectService },
                            onConfirmBooking = {
                                println("Booking Confirmed!")
                            },
                            onNavigateToHome = { currentScreen = Screen.AppointmentList },
                            onNavigateToAppointments = { currentScreen = Screen.AppointmentList },
                            onNavigateToAccount = { currentScreen = Screen.ChooseFeedback }
                        )
                    }

                    Screen.Notification -> {
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
                        NotificationScreen(
                            notifications = sampleNotifications,
                            onNavigateBack = { currentScreen = Screen.AppointmentList },
                            onNavigateToHome = { currentScreen = Screen.AppointmentList },
                            onNavigateToAppointments = { currentScreen = Screen.AppointmentList },
                            onNavigateToAccount = { currentScreen = Screen.ChooseFeedback }
                        )
                    }

                    Screen.ChooseFeedback -> {
                        val samplePastAppointments = listOf(
                            PastAppointment("1", "11 July 2025", "12:00 PM", "Bangsar Branch", "Jackson", "Cut | Styling | Wash and Styling"),
                            PastAppointment("2", "14 May 2025", "12:00 PM", "Bangsar Branch", "Jackson", "Cut | Color | Touch Up"),
                            PastAppointment("3", "05 January 2025", "12:00 PM", "Bangsar Branch", "Jackson", "Color | Medium | Hair Treatment | Scalp Only"),
                        )
                        ChooseAppointmentForFeedbackScreen(
                            pastAppointments = samplePastAppointments,
                            onAppointmentSelected = { appointmentId ->
                                selectedFeedbackAppointmentId = appointmentId
                                currentScreen = Screen.SubmitFeedback
                            },
                            onNotificationClicked = { currentScreen = Screen.Notification },
                            onNavigateToHome = { currentScreen = Screen.AppointmentList },
                            onNavigateToAppointments = { currentScreen = Screen.AppointmentList },
                            onNavigateToAccount = { currentScreen = Screen.ChooseFeedback }
                        )
                    }

                    Screen.SubmitFeedback -> {
                        SubmitFeedbackScreen(
                            onNavigateBack = { currentScreen = Screen.ChooseFeedback },
                            onSubmitFeedback = { rating, feedback ->
                                println("Feedback submitted for appointment $selectedFeedbackAppointmentId: Rating=$rating, Feedback='$feedback'")
                            },
                            onNavigateToHome = { currentScreen = Screen.AppointmentList },
                            onNavigateToAppointments = { currentScreen = Screen.AppointmentList },
                            onNavigateToAccount = { currentScreen = Screen.ChooseFeedback }
                        )
                    }
                }
            }
        }
    }
}

fun Long.toDateString(): String {
    val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    return sdf.format(Date(this))
}
