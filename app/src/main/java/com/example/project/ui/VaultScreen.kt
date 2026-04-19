package com.example.project.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project.data.UserCycleEntry
import com.example.project.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaultScreen(
    userName: String,
    entries: List<UserCycleEntry>,
    onBack: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    
    val filteredEntries = if (searchQuery.isEmpty()) {
        entries
    } else {
        entries.filter { 
            it.moodId.contains(searchQuery, ignoreCase = true) || 
            it.notes.contains(searchQuery, ignoreCase = true) 
        }
    }

    // Mood Frequency for Trends
    val moodFrequency = entries.groupingBy { it.moodId }.eachCount()
    val totalEntries = entries.size.coerceAtLeast(1)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("The History Vault", fontWeight = FontWeight.Bold)
                        Text("For $userName", style = MaterialTheme.typography.labelSmall)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ShilpkaarCreamyYellow,
                    titleContentColor = DeepMauve
                )
            )
        },
        containerColor = ShilpkaarCreamyYellow
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                placeholder = { Text("Search your mood history...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = CircleShape,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DeepMauve,
                    unfocusedBorderColor = DeepMauve.copy(alpha = 0.5f),
                    cursorColor = DeepMauve
                ),
                singleLine = true
            )

            // Mood Trend Summary (Simplified Graph)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Mood Analysis",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DeepMauve
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Simple progress bars for top moods
                    moodFrequency.entries.sortedByDescending { it.value }.take(3).forEach { (mood, count) ->
                        val progress = count.toFloat() / totalEntries
                        Column(modifier = Modifier.padding(vertical = 4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(mood, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                Text("${(progress * 100).toInt()}%", fontSize = 12.sp)
                            }
                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(CircleShape),
                                color = DeepMauve,
                                trackColor = DeepMauve.copy(alpha = 0.1f)
                            )
                        }
                    }
                }
            }

            Text(
                "Past Entries",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = DeepMauve,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(filteredEntries) { entry ->
                    ElegantEntryCard(entry)
                }
            }
        }
    }
}

@Composable
fun ElegantEntryCard(entry: UserCycleEntry) {
    val dateFormat = SimpleDateFormat("EEEE, MMM dd", Locale.getDefault())
    val dateString = dateFormat.format(Date(entry.date))
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Cycle Day Indicator
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(DeepMauve),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = entry.cycleDay.toString(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.moodId,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = DeepMauve
                )
                Text(
                    text = dateString,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            // Chocolate Rating
            if (entry.chocolateRating > 0) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = entry.chocolateRating.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 2.dp)
                    )
                }
            }
        }
    }
}
