package com.example.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.project.data.CozieDatabase
import com.example.project.data.UserCycleEntry
import com.example.project.ui.CozieArcade
import com.example.project.ui.LogScreen
import com.example.project.ui.LoginScreen
import com.example.project.ui.OracleDashboard
import com.example.project.ui.VaultScreen
import com.example.project.ui.theme.CozieTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CozieTheme {
                val context = LocalContext.current
                val database = CozieDatabase.getDatabase(context)
                val entries by database.userCycleDao().getAllEntries().collectAsState(initial = emptyList())
                val scope = rememberCoroutineScope()

                var userName by remember { mutableStateOf("") }
                var isLoggedIn by remember { mutableStateOf(false) }
                var currentScreen by remember { mutableStateOf("log") }
                var selectedMood by remember { mutableStateOf("Sensitive") }
                var selectedDay by remember { mutableIntStateOf(1) }
                var isMoodLogged by remember { mutableStateOf(false) }

                if (!isLoggedIn) {
                    LoginScreen(onLoginSuccess = { name ->
                        userName = name
                        isLoggedIn = true
                    })
                } else {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        bottomBar = {
                            NavigationBar {
                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.Edit, contentDescription = "Log") },
                                    label = { Text(if (isMoodLogged) "Oracle" else "Log") },
                                    selected = currentScreen == "log" || currentScreen == "dashboard",
                                    onClick = { 
                                        currentScreen = if (isMoodLogged) "dashboard" else "log" 
                                    }
                                )
                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.Gamepad, contentDescription = "Arcade") },
                                    label = { Text("Arcade") },
                                    selected = currentScreen == "arcade",
                                    onClick = { currentScreen = "arcade" }
                                )
                                NavigationBarItem(
                                    icon = { Icon(Icons.Default.History, contentDescription = "Vault") },
                                    label = { Text("Vault") },
                                    selected = currentScreen == "vault",
                                    onClick = { currentScreen = "vault" }
                                )
                            }
                        }
                    ) { innerPadding ->
                        Box(modifier = Modifier.padding(innerPadding)) {
                            when (currentScreen) {
                                "log" -> {
                                    LogScreen(
                                        userName = userName,
                                        onLogComplete = { mood, day ->
                                            selectedMood = mood
                                            selectedDay = day
                                            isMoodLogged = true
                                            
                                            scope.launch {
                                                database.userCycleDao().insertEntry(
                                                    UserCycleEntry(
                                                        date = System.currentTimeMillis(),
                                                        moodId = mood,
                                                        cycleDay = day,
                                                        notes = ""
                                                    )
                                                )
                                            }
                                            currentScreen = "dashboard"
                                        }
                                    )
                                }
                                "dashboard" -> {
                                    OracleDashboard(
                                        userName = userName,
                                        selectedMood = selectedMood,
                                        cycleDay = selectedDay,
                                        onBack = { currentScreen = "log" }
                                    )
                                }
                                "arcade" -> {
                                    CozieArcade(
                                        userName = userName,
                                        currentMood = selectedMood,
                                        onBack = { 
                                            currentScreen = if (isMoodLogged) "dashboard" else "log" 
                                        }
                                    )
                                }
                                "vault" -> {
                                    VaultScreen(
                                        userName = userName,
                                        entries = entries,
                                        onBack = { 
                                            currentScreen = if (isMoodLogged) "dashboard" else "log" 
                                        }
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
