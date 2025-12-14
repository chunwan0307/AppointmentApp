package com.example.appointment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
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
import com.example.appointment.ui.AppMutedText
import com.example.appointment.ui.AppTextBrown

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectServiceScreen(
    selectedCut: String?,
    selectedColor: String?,
    selectedTreatment: String?,
    selectedStyling: String?,
    onCutChange: (String) -> Unit,
    onColorChange: (String) -> Unit,
    onTreatmentChange: (String) -> Unit,
    onStylingChange: (String) -> Unit,
    onNavigateBack: () -> Unit,
    onConfirmClicked: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToAppointments: () -> Unit,
    onNavigateToAccount: () -> Unit
) {
    var showNoServiceDialog by remember { mutableStateOf(false) }

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
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = paddingValues.calculateTopPadding() + 16.dp,
                bottom = paddingValues.calculateBottomPadding() + 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column {
                    Text(
                        text = "Appointment Booking",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        color = AppTextBrown
                    )
                    Text(
                        text = "Select a service",
                        style = MaterialTheme.typography.bodyLarge,
                        fontStyle = FontStyle.Italic,
                        color = AppMutedText
                    )
                }
            }

            item { HairLengthGuideline() }

            item {
                ServiceDropdown(
                    label = "Cut",
                    options = listOf("Cut", "None"),
                    selectedValue = selectedCut,
                    onValueChange = onCutChange
                )
            }
            item {
                ServiceDropdown(
                    label = "Color",
                    options = listOf("Touch up", "Short", "Medium", "Long", "None"),
                    selectedValue = selectedColor,
                    onValueChange = onColorChange
                )
            }
            item {
                ServiceDropdown(
                    label = "Hair Treatment",
                    options = listOf("Scalp only", "Short", "Medium", "Long", "None"),
                    selectedValue = selectedTreatment,
                    onValueChange = onTreatmentChange
                )
            }
            item {
                ServiceDropdown(
                    label = "Styling",
                    options = listOf("Wash and Blow Dry", "Wash and Styling", "None"),
                    selectedValue = selectedStyling,
                    onValueChange = onStylingChange
                )
            }

            //  Confirm Button
            item {
                Button(
                    onClick = {
                        val hasValidService = listOfNotNull(selectedCut, selectedColor, selectedTreatment, selectedStyling)
                            .any { it != "None" }

                        if (hasValidService) {
                            onConfirmClicked()
                        } else {
                            showNoServiceDialog = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
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
    }

    if (showNoServiceDialog) {
        NoServiceSelectedDialog(onDismiss = { showNoServiceDialog = false })
    }
}

@Composable
fun HairLengthGuideline() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = "Hair Length Guideline",
            style = MaterialTheme.typography.titleSmall,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
            color = AppTextBrown
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Short", style = MaterialTheme.typography.bodyMedium, color = AppMutedText)
            Text("above collarbone", style = MaterialTheme.typography.bodyMedium, color = AppMutedText)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Medium", style = MaterialTheme.typography.bodyMedium, color = AppMutedText)
            Text("between collarbone and chest line", style = MaterialTheme.typography.bodyMedium, color = AppMutedText)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Long", style = MaterialTheme.typography.bodyMedium, color = AppMutedText)
            Text("past chest line", style = MaterialTheme.typography.bodyMedium, color = AppMutedText)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceDropdown(
    label: String,
    options: List<String>,
    selectedValue: String?,
    onValueChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(
            text = "$label | Includes wash & styling",
            style = MaterialTheme.typography.bodyMedium,
            color = AppMutedText,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                modifier = Modifier.menuAnchor(),
                readOnly = true,
                value = selectedValue ?: "Select",
                onValueChange = {},
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                    disabledTextColor = LocalContentColor.current.copy(alpha = 0.8f),
                    disabledBorderColor = AppMutedText,
                    disabledTrailingIconColor = LocalContentColor.current.copy(alpha = 0.8f),
                    unfocusedContainerColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                enabled = false
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Color.White)
            ) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption, color = Color.Black) },
                        onClick = {
                            onValueChange(selectionOption)
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }
    }
}

@Composable
fun NoServiceSelectedDialog(onDismiss: () -> Unit) {
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
                    text = "Please select at least one service to continue.",
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


@Preview(showBackground = true, device = "id:pixel_4")
@Composable
fun SelectServiceScreenPreview() {
    MaterialTheme {

        SelectServiceScreen(
            selectedCut = "Cut",
            selectedColor = "Medium",
            selectedTreatment = null,
            selectedStyling = "None",
            onCutChange = {},
            onColorChange = {},
            onTreatmentChange = {},
            onStylingChange = {},
            onNavigateBack = {},
            onConfirmClicked = {},
            onNavigateToHome = {},
            onNavigateToAppointments = {},
            onNavigateToAccount = {}
        )
    }
}
