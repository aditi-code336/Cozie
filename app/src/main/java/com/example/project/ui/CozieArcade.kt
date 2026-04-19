package com.example.project.ui

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.project.data.RecommendationProvider
import com.example.project.ui.theme.*
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun CozieArcade(
    userName: String,
    currentMood: String,
    onBack: () -> Unit,
    viewModel: ArcadeViewModel = viewModel()
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    
    Column(modifier = Modifier.fillMaxSize().background(ShilpkaarCreamyYellow)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = DeepMauve)
            }
            Column {
                Text(
                    "Mood Arcade",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = DeepMauve
                )
                Text("Playing as $userName", style = MaterialTheme.typography.labelSmall, color = DeepMauve.copy(alpha = 0.7f))
            }
        }

        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.Transparent,
            contentColor = DeepMauve,
            divider = {},
            indicator = { tabPositions ->
                if (selectedTab < tabPositions.size) {
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = DeepMauve
                    )
                }
            }
        ) {
            Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }) {
                Text("Relate", modifier = Modifier.padding(12.dp), fontWeight = FontWeight.Bold)
            }
            Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }) {
                Text("Play", modifier = Modifier.padding(12.dp), fontWeight = FontWeight.Bold)
            }
            Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }) {
                Text("Bingo", modifier = Modifier.padding(12.dp), fontWeight = FontWeight.Bold)
            }
            Tab(selected = selectedTab == 3, onClick = { selectedTab = 3 }) {
                Text("Vibe", modifier = Modifier.padding(12.dp), fontWeight = FontWeight.Bold)
            }
        }

        Box(modifier = Modifier.weight(1f)) {
            when (selectedTab) {
                0 -> RecommendationScreen(currentMood)
                1 -> {
                    when (currentMood) {
                        "Spicy Rickshaw" -> StressBusterGame()
                        "Monsoon Cloud" -> ZenGardenGame()
                        "Zen Pavilion" -> BreathingLotusGame()
                        "Brain Fog" -> CravingRoulette()
                        else -> CravingRoulette()
                    }
                }
                2 -> SelfCareBingo(viewModel)
                3 -> MemeScroll()
            }
        }
    }
}

@Composable
fun RecommendationScreen(currentMood: String) {
    val rec = RecommendationProvider.getRecommendationForMood(currentMood)
    
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.7f)),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Current Vibe: $currentMood",
                    style = MaterialTheme.typography.titleMedium,
                    color = DeepMauve,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                RecommendationItem(Icons.Default.Coffee, "The Sip", rec.beverage)
                RecommendationItem(Icons.Default.Cookie, "The Bite", rec.snack)
                RecommendationItem(Icons.Default.Tv, "The Stream", rec.media)
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Surface(
                    color = DeepMauve.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        rec.rationale,
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = DeepMauve,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun RecommendationItem(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(40.dp).background(DeepMauve, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(label, style = MaterialTheme.typography.labelSmall, color = DeepMauve.copy(alpha = 0.6f))
            Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = Color.Black)
        }
    }
}

@Composable
fun CravingRoulette() {
    val choices = listOf("Vada Pav", "Bun Maska", "Cutting Chai", "Misal Pav", "Bhel Puri", "Pav Bhaji")
    var selectedChoice by remember { mutableStateOf<String?>(null) }
    val rotationState = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    var isSpinning by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Mumbai Craving Roulette", style = MaterialTheme.typography.headlineMedium, color = Color(0xFF917FB3), fontWeight = FontWeight.Bold)
        Text("Spin for a local comfort snack", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(48.dp))

        Box(contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.size(280.dp).rotate(rotationState.value)) {
                val colors = listOf(Color(0xFF917FB3), Color(0xFFE2725B), Color(0xFFB2C8BA), Color(0xFFE5BEEC), Color(0xFFFDE2F3), Color(0xFFFFFAD7))
                choices.forEachIndexed { index, choice ->
                    val startAngle = index * 60f
                    drawArc(
                        color = colors[index % colors.size],
                        startAngle = startAngle,
                        sweepAngle = 60f,
                        useCenter = true
                    )
                    
                    // Draw labels on the wheel further out and centered
                    rotate(startAngle + 30f) {
                        val textPaint = android.graphics.Paint().apply {
                            color = android.graphics.Color.parseColor("#2D1B4E")
                            textSize = size.width * 0.05f
                            isFakeBoldText = true
                            textAlign = android.graphics.Paint.Align.CENTER
                        }
                        drawContext.canvas.nativeCanvas.drawText(
                            choice,
                            size.width * 0.75f, 
                            size.height / 2 + (textPaint.textSize / 3),
                            textPaint
                        )
                    }
                }
                
                // Outer circle
                drawCircle(color = DeepMauve, style = Stroke(width = 8f))
            }
            // Pointer
            Icon(Icons.Default.ArrowDownward, contentDescription = null, modifier = Modifier.size(48.dp).offset(y = (-155).dp), tint = DeepMauve)
            
            // Center hub
            Surface(modifier = Modifier.size(40.dp), shape = CircleShape, color = Color.White, shadowElevation = 4.dp) {
                Box(contentAlignment = Alignment.Center) {
                    Box(modifier = Modifier.size(10.dp).background(DeepMauve, CircleShape))
                }
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = {
                if (!isSpinning) {
                    isSpinning = true
                    scope.launch {
                        val extraSpins = 5 + Random.nextInt(5)
                        val randomOffset = Random.nextFloat() * 360f
                        rotationState.animateTo(
                            targetValue = rotationState.value + (360 * extraSpins) + randomOffset,
                            animationSpec = tween(3500, easing = CubicBezierEasing(0.1f, 0.1f, 0f, 1f))
                        )
                        val finalAngle = (rotationState.value % 360)
                        // Pointer is at the top (270 degrees in canvas space)
                        // The wheel rotates clockwise.
                        val index = (((360 - finalAngle + 270) % 360) / 60).toInt()
                        selectedChoice = choices[index % choices.size]
                        isSpinning = false
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(64.dp),
            shape = RoundedCornerShape(32.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DeepMauve)
        ) {
            Text(if (isSpinning) "SPINNING..." else "I'M HUNGRY!", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        if (selectedChoice != null) {
            Card(
                modifier = Modifier.padding(top = 24.dp),
                colors = CardDefaults.cardColors(containerColor = ShilpkaarCreamyYellow),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    "Result: $selectedChoice",
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = DeepMauve
                )
            }
        }
    }
}

@Composable
fun SelfCareBingo(viewModel: ArcadeViewModel) {
    val bingoItems = listOf(
        "Hydrated", "Napped", "Walked",
        "Meditation", "Journaled", "Music",
        "No Screen", "Healthy Meal", "Stretched"
    )
    val checkedItems by viewModel.bingoState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Self-Care Bingo", style = MaterialTheme.typography.headlineMedium, color = DeepMauve, fontWeight = FontWeight.Bold)
        Text("Check off your small wins", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(24.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxWidth().aspectRatio(1f),
            contentPadding = PaddingValues(4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(bingoItems) { item ->
                val isChecked = checkedItems.contains(item)
                val backgroundColor by animateColorAsState(
                    targetValue = if (isChecked) ProjectRupaantarSageGreen else ShilpkaarCreamyYellow,
                    label = "bingoBg"
                )

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(backgroundColor)
                        .border(2.dp, DeepMauve.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                        .clickable { viewModel.toggleBingoItem(item) },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (isChecked) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = Color.White)
                        }
                        Text(
                            item,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isChecked) Color.White else DeepMauve,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            "${checkedItems.size} / 9 Wins Today",
            style = MaterialTheme.typography.titleMedium,
            color = DeepMauve,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun MemeScroll() {
    val memes = listOf(
        "Me explaining to my brain why we need to sleep at 2 AM.",
        "When the 'one more episode' turns into a sunrise.",
        "My bank account watching me buy another coffee to feel something.",
        "The face you make when someone says 'just don't be stressed'.",
        "Trying to find my focus like it's a lost remote.",
        "When you successfully avoid a phone call by staring at the screen.",
        "Me after doing one basic chore: 'I deserve a 3-hour break'.",
        "The struggle between wanting to be productive and wanting to be a potato."
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                "Relatable Situations",
                style = MaterialTheme.typography.headlineSmall,
                color = DeepMauve,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        items(memes) { meme ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ShilpkaarCreamyYellow.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Placeholder for Meme Image
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(DeepMauve.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check, // Placeholder
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = DeepMauve.copy(alpha = 0.3f)
                        )
                        Text("Meme Image Placeholder", color = DeepMauve.copy(alpha = 0.5f))
                    }
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = meme,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
fun StressBusterGame() {
    val context = LocalContext.current
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    val annoyances = listOf("Unsolicited Advice", "No Parking", "Slow Internet", "Deadlines", "Traffic", "Crowds")
    var poppedCount by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Pop the Stress!", style = MaterialTheme.typography.headlineMedium, color = Color(0xFFE2725B), fontWeight = FontWeight.Bold)
        Text("Popped: $poppedCount", style = MaterialTheme.typography.titleMedium)
        
        Spacer(modifier = Modifier.height(24.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(30) { index ->
                var isPopped by remember { mutableStateOf(false) }
                val animatedScale = animateFloatAsState(
                    targetValue = if (isPopped) 0.8f else 1f,
                    label = "popScale"
                )

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .scale(animatedScale.value)
                        .clip(CircleShape)
                        .background(if (isPopped) Color.LightGray.copy(alpha = 0.4f) else Color(0xFFE2725B))
                        .border(1.dp, Color.White.copy(alpha = 0.5f), CircleShape)
                        .clickable(enabled = !isPopped) {
                            isPopped = true
                            poppedCount++
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
                            } else {
                                @Suppress("DEPRECATION")
                                vibrator.vibrate(50)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (!isPopped) {
                        Text(
                            annoyances[index % annoyances.size],
                            fontSize = 8.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ZenGardenGame() {
    var points by remember { mutableStateOf(listOf<Offset>()) }
    
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Gond Art Zen Garden", style = MaterialTheme.typography.headlineMedium, color = Color(0xFF917FB3), fontWeight = FontWeight.Bold)
        Text("Draw slow patterns in the sand", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFFFFFAD7))
                .pointerInput(Unit) {
                    detectDragGestures { change, _ ->
                        change.consume()
                        points = points + change.position
                    }
                }
        ) {
            val path = Path()
            if (points.isNotEmpty()) {
                path.moveTo(points.first().x, points.first().y)
                points.forEach { point ->
                    path.lineTo(point.x, point.y)
                }
            }
            
            drawPath(
                path = path,
                color = Color(0xFF917FB3),
                style = Stroke(width = 8f, cap = StrokeCap.Round)
            )
            
            // Subtle "sand" texture effect by drawing some existing points as dots too
            if (points.size > 1) {
                for (i in 0 until points.size step 10) {
                    drawCircle(
                        color = Color(0xFF917FB3).copy(alpha = 0.2f),
                        radius = 12f,
                        center = points[i]
                    )
                }
            }
        }
    }
}

@Composable
fun BreathingLotusGame() {
    val infiniteTransition = rememberInfiniteTransition(label = "breathing")
    val breathingScale by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Breathe with the Lotus", style = MaterialTheme.typography.headlineMedium, color = Color(0xFFB2C8BA), fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(48.dp))
        
        Box(contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.size(300.dp).scale(breathingScale)) {
                val canvasCenter = Offset(size.width / 2, size.height / 2)
                val petalWidth = 80f
                val petalHeight = 150f
                
                for (i in 0 until 8) {
                    this.rotate(degrees = i * 45f) {
                        drawOval(
                            color = Color(0xFFB2C8BA),
                            topLeft = Offset(canvasCenter.x - petalWidth / 2, canvasCenter.y - petalHeight),
                            size = Size(petalWidth, petalHeight)
                        )
                        drawOval(
                            color = Color.White.copy(alpha = 0.5f),
                            topLeft = Offset(canvasCenter.x - petalWidth / 2, canvasCenter.y - petalHeight),
                            size = Size(petalWidth, petalHeight),
                            style = Stroke(width = 2f)
                        )
                    }
                }
            }
            
            Text(
                if (breathingScale > 0.9f) "EXHALE" else "INHALE",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D1B4E)
            )
        }
    }
}
