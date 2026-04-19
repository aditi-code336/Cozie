package com.example.project.ui

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Picture
import android.net.Uri
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.project.ui.theme.ShilpkaarMauve
import java.io.File
import java.io.FileOutputStream
import kotlin.math.sin

@Composable
fun OracleDashboard(
    userName: String,
    selectedMood: String,
    cycleDay: Int,
    onBack: () -> Unit,
    viewModel: OracleViewModel = viewModel()
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val picture = remember { Picture() }
    val isDark = isSystemInDarkTheme()

    val comfortPackage by viewModel.comfortPackage.collectAsState()
    
    val moodColor = when (selectedMood) {
        "Spicy Rickshaw" -> if (isDark) Color(0xFFFF8A65) else Color(0xFFE2725B)
        "Monsoon Cloud" -> if (isDark) Color(0xFFB39DDB) else Color(0xFF917FB3)
        "Zen Pavilion" -> if (isDark) Color(0xFFA5D6A7) else Color(0xFFB2C8BA)
        "Brain Fog" -> if (isDark) Color(0xFFFFF59D) else Color(0xFFFFFAD7)
        else -> ShilpkaarMauve
    }

    LaunchedEffect(selectedMood) {
        viewModel.updateMood(selectedMood)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack, 
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Column {
                Text(
                    text = "Oracle Guidance",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = moodColor
                )
                Text(
                    text = "For $userName (Day $cycleDay)", 
                    style = MaterialTheme.typography.labelSmall, 
                    color = moodColor.copy(alpha = 0.8f)
                )
            }
        }

        val pkg = comfortPackage
        if (pkg != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = moodColor.copy(alpha = if (isDark) 0.2f else 0.1f)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Lightbulb, contentDescription = null, tint = moodColor)
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = pkg.rationale,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            DashboardCard(
                title = "The Sip & Bite",
                icon = Icons.Default.Restaurant,
                accentColor = moodColor
            ) {
                Column(
                    modifier = Modifier.animateContentSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    RecommendationItem(
                        icon = Icons.Default.LocalCafe,
                        label = "Sip:",
                        value = pkg.beverage,
                        accentColor = moodColor
                    )
                    RecommendationItem(
                        icon = Icons.Default.BakeryDining,
                        label = "Bite:",
                        value = pkg.chocolate,
                        accentColor = moodColor
                    )
                }
            }

            DashboardCard(
                title = "The Watch & Listen",
                icon = Icons.Default.LiveTv,
                accentColor = moodColor
            ) {
                Column(
                    modifier = Modifier.animateContentSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    RecommendationItem(
                        icon = Icons.Default.Movie,
                        label = "Watch/Listen:",
                        value = pkg.media,
                        accentColor = moodColor
                    )
                    
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=${pkg.media}"))
                            context.startActivity(intent)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = moodColor,
                            contentColor = if (selectedMood == "Brain Fog" && !isDark) Color.Black else Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Search, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Search for this")
                    }
                }
            }

            Text(
                text = "Did this help, $userName?",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = moodColor
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                (1..5).forEach { rating ->
                    IconButton(onClick = { /* Save feedback logic */ }) {
                        Icon(
                            imageVector = if (rating <= 3) Icons.Default.StarOutline else Icons.Default.Star,
                            contentDescription = "Rate $rating",
                            tint = moodColor
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .size(300.dp, 533.dp)
                    .drawWithCache {
                        val width = size.width.toInt()
                        val height = size.height.toInt()
                        onDrawWithContent {
                            val canvas = picture.beginRecording(width, height)
                            drawIntoCanvas { canvas ->
                                val paint = android.graphics.Paint().apply {
                                    color = moodColor.toArgb()
                                    style = android.graphics.Paint.Style.FILL
                                }
                                canvas.nativeCanvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
                                
                                val watermarkPaint = android.graphics.Paint().apply {
                                    color = android.graphics.Color.WHITE
                                    alpha = 30
                                    style = android.graphics.Paint.Style.STROKE
                                    strokeWidth = 2f
                                }
                                for (i in 0..height step 50) {
                                    val path = android.graphics.Path()
                                    path.moveTo(0f, i.toFloat())
                                    for (x in 0..width step 20) {
                                        path.lineTo(x.toFloat(), i + (sin(x.toFloat() / 30f) * 20f))
                                    }
                                    canvas.nativeCanvas.drawPath(path, watermarkPaint)
                                }

                                val textPaint = android.graphics.Paint().apply {
                                    color = android.graphics.Color.WHITE
                                    textSize = 40f
                                    isFakeBoldText = true
                                    textAlign = android.graphics.Paint.Align.CENTER
                                }
                                canvas.nativeCanvas.drawText("$userName'S STATUS: ${selectedMood.uppercase()}", width / 2f, 100f, textPaint)
                                
                                textPaint.textSize = 60f
                                canvas.nativeCanvas.drawText("\uD83C\uDFA8", width / 2f, height / 2f, textPaint)

                                textPaint.textSize = 30f
                                canvas.nativeCanvas.drawText("Intervention Required:", width / 2f, height - 150f, textPaint)
                                textPaint.textSize = 45f
                                canvas.nativeCanvas.drawText("Bring ${pkg.chocolate}", width / 2f, height - 100f, textPaint)
                            }
                            picture.endRecording()
                        }
                    }
                    .absoluteOffset(x = 2000.dp)
            )

            Button(
                onClick = {
                    shareStatusCard(context, picture)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = moodColor,
                    contentColor = if (selectedMood == "Brain Fog" && !isDark) Color.Black else Color.White
                )
            ) {
                Icon(Icons.Default.Campaign, contentDescription = null)
                Spacer(Modifier.width(12.dp))
                Text(
                    text = "Broadcast Alert",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        
        Spacer(Modifier.height(32.dp))
    }
}

@Composable
fun DashboardCard(
    title: String,
    icon: ImageVector,
    accentColor: Color,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, accentColor.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = accentColor.copy(alpha = 0.15f)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.padding(8.dp).size(20.dp),
                        tint = accentColor
                    )
                }
                Spacer(Modifier.width(12.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(Modifier.height(16.dp))
            content()
        }
    }
}

@Composable
fun RecommendationItem(
    icon: ImageVector,
    label: String,
    value: String,
    accentColor: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = accentColor.copy(alpha = 0.9f)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

fun shareStatusCard(context: Context, picture: Picture) {
    val width = if (picture.width > 0) picture.width else 1080
    val height = if (picture.height > 0) picture.height else 1920
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    canvas.drawPicture(picture)

    val cachePath = File(context.cacheDir, "images")
    cachePath.mkdirs()
    val file = File(cachePath, "status_alert.png")
    val stream = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    stream.close()

    val contentUri = FileProvider.getUriForFile(context, "com.example.project.fileprovider", file)

    if (contentUri != null) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, contentUri)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share Artistic Alert"))
    }
}
