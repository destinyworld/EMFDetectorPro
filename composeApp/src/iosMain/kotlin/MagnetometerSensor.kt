import platform.CoreMotion.CMMotionManager
import platform.Foundation.NSOperationQueue

actual class MagnetometerSensor {
    private val motionManager = CMMotionManager()

    actual fun start(onUpdate: (MagnetometerData) -> Unit) {
        if (motionManager.isMagnetometerAvailable()) {
            motionManager.magnetometerUpdateInterval = 0.1 // 10Hz
            motionManager.startMagnetometerUpdatesToQueue(NSOperationQueue.mainQueue) { data, error ->
                data?.magneticField?.let { field ->
                    onUpdate(MagnetometerData(field.x.toFloat(), field.y.toFloat(), field.z.toFloat()))
                }
            }
        }
    }

    actual fun stop() {
        motionManager.stopMagnetometerUpdates()
    }
}