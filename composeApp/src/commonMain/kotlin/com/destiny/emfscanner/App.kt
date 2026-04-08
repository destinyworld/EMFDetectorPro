package com.destiny.emfscanner

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.painterResource

import emfdetectorpro.composeapp.generated.resources.Res
import emfdetectorpro.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        // State to hold our sensor values
        var sensorData by remember { mutableStateOf(MagnetometerData(0f, 0f, 0f)) }
        // Initialize our expect/actual sensor class
        // Note: For Android, you'll need to pass the context via a platform-specific provider
        val sensor = remember { MagnetometerSensor() }

        LaunchedEffect(sensorData) {
    // Only send to server if the field is strong (to save battery/data)
    if (abs(sensorData.x) > 50) {
        try {
            httpClient.post("http://YOUR_LOCAL_IP:3000/api/logs") {
                contentType(ContentType.Application.Json)
                setBody(mapOf("value" to sensorData.x, "axis" to "X"))
            }
        } catch (e: Exception) {
            println("Failed to sync with Node.js: ${e.message}")
        }
    }
}
        LaunchedEffect(Unit) {
            sensor.start { data ->
                sensorData = data
            }
        }
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("EMF Detector Pro", style = MaterialTheme.typography.h4)
            Spacer(modifier = Modifier.height(24.dp))
            
            // Display X, Y, Z values
            SensorReading("X-Axis", sensorData.x)
            SensorReading("Y-Axis", sensorData.y)
            SensorReading("Z-Axis", sensorData.z)

            val totalStrength = sqrt(
                sensorData.x.toDouble().pow(2) + 
                sensorData.y.toDouble().pow(2) + 
                sensorData.z.toDouble().pow(2)
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Total Field: ${"%.2f".format(totalStrength)} µT",
                style = MaterialTheme.typography.h5,
                color = if (totalStrength > 60) Color.Red else Color.Green
            )
        }
    }
}

@Composable
fun SensorReading(label: String, value: Float) {
    Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Text("$label:", modifier = Modifier.weight(1f))
        Text("${"%.2f".format(value)}", fontWeight = FontWeight.Bold)
    }
}