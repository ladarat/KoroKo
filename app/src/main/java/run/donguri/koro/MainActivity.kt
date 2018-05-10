package run.donguri.koro

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView

class MainActivity : AppCompatActivity(), SensorEventListener, StepListener {
    private var textView: TextView? = null
    private var simpleStepDetector: SimpleStepDetector? = null
    private var sensorManager: SensorManager? = null
    private var accel: Sensor? = null
    private var numSteps: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = TextView(this)
        textView!!.textSize = 30f
        setContentView(textView)

        // Get an instance of the SensorManager
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accel = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        simpleStepDetector = SimpleStepDetector()
        simpleStepDetector!!.registerListener(this)
    }

    public override fun onResume() {
        super.onResume()
        numSteps = 0
        textView!!.text = TEXT_NUM_STEPS + numSteps
        sensorManager!!.registerListener(this, accel, SensorManager.SENSOR_DELAY_FASTEST)
    }

    public override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector!!.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2])
        }
    }

    override fun step(timeNs: Long) {
        numSteps++
        textView!!.text = TEXT_NUM_STEPS + numSteps
    }

    companion object {
        private val TEXT_NUM_STEPS = "Number of Steps: "
    }

}
