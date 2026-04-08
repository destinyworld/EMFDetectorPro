import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

actual class MagnetometerSensor(private val context: Context) : SensorEventListener {
    private var sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var magnetometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    private var onUpdate: ((MagnetometerData) -> Unit)? = null

    actual fun start(onUpdate: (MagnetometerData) -> Unit) {
        this.onUpdate = onUpdate
        magnetometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    actual fun stop() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            onUpdate?.invoke(MagnetometerData(it.values, it.values, it.values))
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}