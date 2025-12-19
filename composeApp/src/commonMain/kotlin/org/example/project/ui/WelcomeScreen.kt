package org.example.project.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WelcomeScreen(
    state: viewmodel.WelcomeUIState,
    onStartCheckIn: () -> Unit,
    onMoodClick: (String) -> Unit = {},
    onViewHistory: () -> Unit = {}
) {
    // Theme Colors
    // Matching the user's uploaded image: #F7F9F2 / pale yellowish-green tint
    val BackgroundColor = Color(0xFFF7F9F2) 
    val TextColor = Color(0xFF134E4A) // Dark Teal
    val SubTextColor = Color(0xFF134E4A).copy(alpha = 0.7f)
    
    // Gradient for the CTA button
    val ctaGradient = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFF2D8A7F), // Teal Green
            Color(0xFF3B9E91)  // Lighter Teal
        )
    )

    Scaffold(
        containerColor = BackgroundColor,
        contentColor = TextColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Header
            Text(
                text = "Welcome Back",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextColor
                ),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "How are you feeling today?",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 18.sp,
                    color = SubTextColor
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            // CTA Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(ctaGradient)
                    .clickable { onStartCheckIn() },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Start a Check-in",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Express your feelings freely",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Recent Moods
            Text(
                text = "Recent Moods",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextColor
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (state.recentMoods.isEmpty()) {
                    // Placeholder if no history
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "No recent moods yet",
                            style = MaterialTheme.typography.bodyMedium.copy(color = SubTextColor)
                        )
                    }
                } else {
                    state.recentMoods.forEach { item ->
                        MoodCard(
                            emoji = item.emoji,
                            label = item.label,
                            date = item.date,
                            onClick = { onMoodClick(item.id) }
                        )
                    }
                    // Fill remaining space if less than 3
                     repeat(3 - state.recentMoods.size) {
                        Spacer(modifier = Modifier.width(100.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Stats Card
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Color(0xFFFFFBEB), // Very light yellow/beige
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatItem(state.totalEntries.toString(), "Entries")
                    
                    // Vertical Divider
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(40.dp)
                            .background(Color.Black.copy(alpha = 0.1f))
                    )
                    
                    StatItem(state.averageEnergy, "Avg Mood")
                    
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(40.dp)
                            .background(Color.Black.copy(alpha = 0.1f))
                    )
                    
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(state.mostCommonMoodEmoji, fontSize = 24.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Most Common",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = SubTextColor,
                                fontSize = 10.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Bottom Actions Card (View History)
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp) // Fixed height for a nice button/card feel
                    .clip(RoundedCornerShape(24.dp))
                    .clickable { onViewHistory() }, // Surface click handles ripple for whole container
                shadowElevation = 0.dp
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "View History",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF134E4A),
                            fontSize = 18.sp
                        )
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun MoodCard(emoji: String, label: String, date: String, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        modifier = Modifier
            .width(100.dp)
            .height(140.dp)
            .clip(RoundedCornerShape(24.dp))
            .clickable { onClick() },
        shadowElevation = 0.dp // Flat style as per mockup
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFF7ED)), // Light orange bg for emoji
                contentAlignment = Alignment.Center
            ) {
                Text(emoji, fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF134E4A)
                ),
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = date,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Color.Gray
                )
            )
        }
    }
}

@Composable
fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF134E4A)
            )
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = Color(0xFF134E4A).copy(alpha = 0.7f)
            )
        )
    }
}

// ActionRowItem removed as it is no longer used
