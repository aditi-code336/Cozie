package com.example.project.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project.R
import com.example.project.ui.theme.ShilpkaarMauve

sealed class AvatarImage {
    data class Vector(val imageVector: ImageVector) : AvatarImage()
    data class Resource(val resId: Int) : AvatarImage()
}

data class AvatarData(
    val id: String,
    val name: String,
    val description: String,
    val image: AvatarImage,
    val moodTitle: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogScreen(
    userName: String,
    showBack: Boolean = false,
    onBack: () -> Unit = {},
    onLogComplete: (String, Int) -> Unit
) {
    val avatars = listOf(
        AvatarData(
            "whistledown",
            "Lady Whistledown",
            "The \"Venting & Tea\" Mood: For the days when you are observant, perhaps a bit judgmental, and want to document every single grievance.",
            AvatarImage.Resource(R.drawable.whistledown),
            "Sensitive"
        ),
        AvatarData(
            "waldorf",
            "Blair Waldorf",
            "The \"Elite Meltdown\" Mood: For when your standards are through the roof but your patience is non-existent.",
            AvatarImage.Resource(R.drawable.waldorf),
            "Irritable"
        ),
        AvatarData(
            "geller",
            "Monica Geller",
            "The \"High-Stress Control\" Mood: For the frantic, high-energy PMS phase. You’re crying, you’re cleaning, you’re organizing.",
            AvatarImage.Resource(R.drawable.monica),
            "Calm"
        ),
        AvatarData(
            "priestly",
            "Miranda Priestly",
            "The \"Disappointed & Dismissive\" Mood: Pure, cold authority. For the days when you literally cannot look at anyone.",
            AvatarImage.Resource(R.drawable.miranda),
            "Distracted"
        )
    )

    var selectedAvatar by remember { mutableStateOf(avatars[0]) }
    var selectedDay by remember { mutableIntStateOf(1) }
    var isDayExpanded by remember { mutableStateOf(false) }
    val isDark = isSystemInDarkTheme()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text("Daily Check-in", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Hi, $userName!", style = MaterialTheme.typography.labelSmall)
                    }
                },
                navigationIcon = {
                    if (showBack) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = if (isDark) MaterialTheme.colorScheme.primary else ShilpkaarMauve
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                "Pick your Vibe Avatar",
                style = MaterialTheme.typography.titleMedium,
                color = if (isDark) MaterialTheme.colorScheme.primary else ShilpkaarMauve,
                fontWeight = FontWeight.Bold
            )
            
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                items(avatars) { avatar ->
                    val isSelected = selectedAvatar.id == avatar.id
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(100.dp).clickable { selectedAvatar = avatar }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
                                .border(
                                    width = if (isSelected) 3.dp else 1.dp,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.5f),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            when (val img = avatar.image) {
                                is AvatarImage.Vector -> {
                                    Icon(
                                        imageVector = img.imageVector,
                                        contentDescription = avatar.name,
                                        modifier = Modifier.size(48.dp),
                                        tint = if (isSelected) MaterialTheme.colorScheme.onPrimary else Color.Gray
                                    )
                                }
                                is AvatarImage.Resource -> {
                                    Image(
                                        painter = painterResource(id = img.resId),
                                        contentDescription = avatar.name,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.size(74.dp).clip(CircleShape)
                                    )
                                }
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(
                            avatar.name,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            textAlign = TextAlign.Center,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }

            // Avatar Description Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        selectedAvatar.name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        selectedAvatar.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 16.sp
                    )
                }
            }

            // Dropdown for Cycle Day
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    "Day of Cycle",
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isDark) MaterialTheme.colorScheme.primary else ShilpkaarMauve,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                        .clickable { isDayExpanded = true }
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Day $selectedDay", 
                            color = if (isDark) MaterialTheme.colorScheme.primary else ShilpkaarMauve, 
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            Icons.Default.ArrowDropDown, 
                            contentDescription = null, 
                            tint = if (isDark) MaterialTheme.colorScheme.primary else ShilpkaarMauve
                        )
                    }
                    
                    DropdownMenu(
                        expanded = isDayExpanded,
                        onDismissRequest = { isDayExpanded = false },
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        (1..7).forEach { day ->
                            DropdownMenuItem(
                                text = { Text("Day $day") },
                                onClick = {
                                    selectedDay = day
                                    isDayExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            Button(
                onClick = { onLogComplete(selectedAvatar.moodTitle, selectedDay) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDark) MaterialTheme.colorScheme.primary else ShilpkaarMauve
                ),
                shape = CircleShape
            ) {
                Text("Log as ${selectedAvatar.name}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}
