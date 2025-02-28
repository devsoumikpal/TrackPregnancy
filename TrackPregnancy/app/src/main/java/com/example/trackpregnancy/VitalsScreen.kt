package com.example.trackpregnancy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackpregnancy.models.VitalsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun VitalsScreen(viewModel: VitalsViewModel, modifier: Modifier = Modifier) {
    val vitalsList by viewModel.vitalsList.collectAsState()

    LazyColumn(modifier = modifier.padding(16.dp)) {
        items(vitalsList) { vital ->
            HealthStatsCard(vital)
        }
    }
}

@Composable
fun HealthStatsCard(vital: Vital) {
    val bloodPressure = "${vital.bloodPressureSys}/${vital.bloodPressureDia} mmHg"
    val weightText = "${vital.weight} kg"
    val heartRateText = "${vital.heartRate} bpm"
    val babyKicksText = "${vital.babyKicks} kicks"

    val date = remember(vital.timestamp) {
        SimpleDateFormat("EEE, d MMM yyyy hh:mm a", Locale.getDefault()).format(Date(vital.timestamp))
    }

    Column(
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFDAC3FC)) // Light purple background
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                InfoRow(R.drawable.heart_rate, "Heart rate", heartRateText)
                Spacer(modifier = Modifier.height(12.dp))
                InfoRow(R.drawable.scale, "Weight", weightText)
            }
            Column {
                InfoRow(R.drawable.blood_pressure, "Blood pressure", bloodPressure)
                Spacer(modifier = Modifier.height(12.dp))
                InfoRow(R.drawable.baby, "Baby Kicks", babyKicksText)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF854DB1)) // Darker purple background
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = date,
                fontSize = 14.sp,
                color = Color.White,
                modifier = Modifier.align(Alignment.Center), // Center the date text
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun InfoRow(iconId: Int, contentDesc: String, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = contentDesc,
            tint = Color.Black,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, fontSize = 16.sp, color = Color.Black)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewVitalsStatusCard() {
    HealthStatsCard(
        vital = Vital(
            bloodPressureSys = 120,
            bloodPressureDia = 80,
            weight = 68f,
            heartRate = 90,
            babyKicks = 15,
            timestamp = System.currentTimeMillis()
        )
    )
}
