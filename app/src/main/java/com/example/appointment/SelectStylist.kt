package com.example.appointment


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appointment.ui.AppBackground
import com.example.appointment.ui.AppBottomNavigationBar
import com.example.appointment.ui.AppBrown
import com.example.appointment.ui.AppMutedText
import com.example.appointment.ui.AppTextBrown

data class Stylist(
    val id: Int,
    val name: String,
    val imageResId: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectStylistScreen(
    stylists: List<Stylist>,
    onNavigateBack: () -> Unit,
    onStylistSelected: (Stylist) -> Unit,
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // Header Text
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Appointment Booking",
                style = MaterialTheme.typography.headlineSmall,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                color = AppTextBrown
            )
            Text(
                text = "Select a stylist",
                style = MaterialTheme.typography.bodyLarge,
                fontStyle = FontStyle.Italic,
                color = AppMutedText
            )
            Spacer(modifier = Modifier.height(24.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(stylists) { stylist ->
                    StylistCard(
                        stylist = stylist,
                        onClick = { onStylistSelected(stylist) }
                    )
                }
            }
        }
    }
}

@Composable
fun StylistCard(stylist: Stylist, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0ECE5)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = stylist.imageResId),
                contentDescription = "Stylist ${stylist.name}",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stylist.name,
                style = MaterialTheme.typography.bodyLarge,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                color = AppTextBrown,
                textAlign = TextAlign.Center
            )
        }
    }
}


@Preview(showBackground = true, device = "id:pixel_4")
@Composable
fun SelectStylistScreenPreview() {
    val sampleStylists = listOf(
        Stylist(1, "Jackson", R.drawable.jackson_stylist),
        Stylist(2, "Minnie", R.drawable.minnie_stylist),
        Stylist(3, "Winter", R.drawable.winter_stylist),
        Stylist(4, "Leo", R.drawable.leo_stylist)
    )

    MaterialTheme {
        SelectStylistScreen(
            stylists = sampleStylists,
            onNavigateBack = {},
            onStylistSelected = {},
            onNavigateToHome = {},
            onNavigateToAppointments = {},
            onNavigateToAccount = {}
        )
    }
}
