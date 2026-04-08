data class MagnetometerData(val x: Float, val y: Float, val z: Float)

expect class MagnetometerSensor {
    fun start(onUpdate: (MagnetometerData) -> Unit)
    fun stop()
}