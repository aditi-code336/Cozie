package com.example.project.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import com.example.project.ui.theme.ProjectRupaantarSageGreen
import com.example.project.ui.theme.ShilpkaarCreamyYellow
import com.example.project.ui.theme.ShilpkaarMauve
import kotlin.math.absoluteValue

@Composable
fun LandingScreen(
    onNavigateToDashboard: (String, Int) -> Unit
) {
    val moods = listOf(
        MoodCardData("Monsoon Cloud", Color(0xFF917FB3), Icons.Default.CloudQueue),
        MoodCardData("Spicy Rickshaw", Color(0xFFE2725B), Icons.Default.FireTruck), // Approximate icon
        MoodCardData("Zen Pavilion", Color(0xFFB2C8BA), Icons.Default.Spa),
        MoodCardData("Brain Fog", Color(0xFFFFFAD7), Icons.Default.Grain)
    )

    var selectedMoodIndex by remember { mutableIntStateOf(0) }
    var cycleDay by remember { mutableFloatStateOf(1f) }

    val twilightPurple = Color(0xFF2D1B4E)
    val morningCream = ShilpkaarCreamyYellow
    val backgroundColor by animateColorAsState(
        targetValue = lerp(twilightPurple, morningCream, (cycleDay - 1) / 30f),
        animationSpec = tween(500),
        label = "bgColor"
    )

    val contentColor = if (cycleDay < 15) Color.White else Color(0xFF333333)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = "How are we feeling, Fam?",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            ),
            color = contentColor
        )

        // Mood Selection LazyRow
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(moods.size) { index ->
                MoodCard(
                    data = moods[index],
                    isSelected = selectedMoodIndex == index,
                    onClick = { selectedMoodIndex = index }
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Timeline of the Moon",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )
                Text(
                    text = "Day ${cycleDay.toInt()}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )
            }
            
            Slider(
                value = cycleDay,
                onValueChange = { cycleDay = it },
                valueRange = 1f..31f,
                steps = 29,
                colors = SliderDefaults.colors(
                    thumbColor = ShilpkaarMauve,
                    activeTrackColor = ShilpkaarMauve,
                    inactiveTrackColor = contentColor.copy(alpha = 0.3f)
                )
            )
        }

        Button(
            onClick = { onNavigateToDashboard(moods[selectedMoodIndex].name, cycleDay.toInt()) },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(60.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ProjectRupaantarSageGreen)
        ) {
            Text(
                text = "Fix My Vibe",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
        }
    }
}

data class MoodCardData(val name: String, val color: Color, val icon: ImageVector)

@Composable
fun MoodCard(
    data: MoodCardData,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) data.color else Color.White.copy(alpha = 0.8f),
        label = "cardBg"
    )
    val iconScale by animateFloatAsState(
        targetValue = if (isSelected) 1.2f else 1f,
        label = "iconScale"
    )
    val textColor = if (isSelected && data.color != Color(0xFFFFFAD7)) Color.White else Color.Black

    Card(
        modifier = Modifier
            .size(100.dp, 130.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 8.dp else 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Gond Art Dotted Border
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 2.dp.toPx()
                val dashWidth = 4.dp.toPx()
                val dashGap = 4.dp.toPx()
                
                // Drawing a dotted border manually for Gond style
                val path = androidx.compose.ui.graphics.Path().apply {
                    addRoundRect(
                        androidx.compose.ui.geometry.RoundRect(
                            0f, 0f, size.width, size.height,
                            CornerRadius(16.dp.toPx(), 16.dp.toPx())
                        )
                    )
                }
                drawPath(
                    path = path,
                    color = data.color.copy(alpha = 0.5f),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                        width = strokeWidth,
                        pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(
                            floatArrayOf(dashWidth, dashGap), 0f
                        )
                    )
                )
            }

            Column(
                modifier = Modifier.fillMaxSize().padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = data.icon,
                    contentDescription = data.name,
                    modifier = Modifier
                        .size(40.dp)
                        .graphicsLayer(scaleX = iconScale, scaleY = iconScale),
                    tint = if (isSelected && data.color != Color(0xFFFFFAD7)) Color.White else data.color
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = data.name,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    lineHeight = 14.sp
                )
            }
        }
    }
}
