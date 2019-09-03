package com.arvifox.arvi.simplemisc.sensor

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.BatteryManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.method.ScrollingMovementMethod
import com.arvifox.arvi.R
import com.arvifox.arvi.utils.FormatUtils.format
import kotlinx.android.synthetic.main.activity_sensor.*
import kotlinx.android.synthetic.main.app_bar_layout.*

class SensorActivity : AppCompatActivity(), SensorEventListener {

    companion object {
        fun newIntent(c: Context): Intent {
            return Intent(c, SensorActivity::class.java)
        }
    }

    private lateinit var sensorManager: SensorManager
    private var curTimer: Long = System.nanoTime()
    private var light: Float = 0f
    private var pressure: Float = 0f
    private var ambtemp: Float = 0f
    private val mAccelerometerReading = FloatArray(3)
    private val mMagnetometerReading = FloatArray(3)
    private val mRotationMatrix = FloatArray(9)
    private val mOrientationAngles = FloatArray(3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensor)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        tvSensor.movementMethod = ScrollingMovementMethod()
    }

    @SuppressLint("NewApi")
    override fun onResume() {
        super.onResume()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE), SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE), SensorManager.SENSOR_DELAY_UI)
        getInfo()
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { acc -> sensorManager.registerListener(this, acc, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI) }
        sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)?.also { mag -> sensorManager.registerListener(this, mag, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI) }
    }

    override fun onPause() {
        sensorManager.unregisterListener(this)
        super.onPause()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        val a = accuracy
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_LIGHT) {
            light = event.values[0]
        }
        if (event?.sensor?.type == Sensor.TYPE_PRESSURE) {
            pressure = event.values[0]
        }
        if (event?.sensor?.type == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            ambtemp = event.values[0]
        }
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, mAccelerometerReading, 0, mAccelerometerReading.size)
        }
        if (event?.sensor?.type == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, mMagnetometerReading, 0, mMagnetometerReading.size)
        }
        val timer = System.nanoTime()
        if ((timer - curTimer) / 1000_000_000 > 2) {
            val sb = StringBuilder()
            sb.append("light = ").append(light).append(" lx").append("\n")
            sb.append("pressure = ").append(pressure).append(" mbar").append("\n")
            sb.append("amb temp = ").append(ambtemp).append("°C").append("\n")
            updateOrientationAngles()
            sb.append("abgles:\n").append(mOrientationAngles[0].format(2)).append(";\n")
                    .append(mOrientationAngles[1].format(2)).append(";\n")
                    .append(mOrientationAngles[2].format(2)).append(";\n")
            tvSensor.text = sb.toString()
            curTimer = timer
        }
    }

    // Compute the three orientation angles based on the most recent readings from
    // the device's accelerometer and magnetometer.
    fun updateOrientationAngles() {
        // Update rotation matrix, which is needed to update orientation angles.
        SensorManager.getRotationMatrix(
                mRotationMatrix,
                null,
                mAccelerometerReading,
                mMagnetometerReading
        )

        // "mRotationMatrix" now has up-to-date information.

        SensorManager.getOrientation(mRotationMatrix, mOrientationAngles)

        // "mOrientationAngles" now has up-to-date information.
    }

    /*
    The system computes the orientation angles by using a device's geomagnetic field sensor in combination with the device's accelerometer. Using these two hardware sensors, the system provides data for the following three orientation angles:

    Azimuth (degrees of rotation about the -z axis). This is the angle between the device's current compass direction and magnetic north. If the top edge of the device faces magnetic north, the azimuth is 0 degrees; if the top edge faces south, the azimuth is 180 degrees. Similarly, if the top edge faces east, the azimuth is 90 degrees, and if the top edge faces west, the azimuth is 270 degrees.
    Pitch (degrees of rotation about the x axis). This is the angle between a plane parallel to the device's screen and a plane parallel to the ground. If you hold the device parallel to the ground with the bottom edge closest to you and tilt the top edge of the device toward the ground, the pitch angle becomes positive. Tilting in the opposite direction— moving the top edge of the device away from the ground—causes the pitch angle to become negative. The range of values is -180 degrees to 180 degrees.
    Roll (degrees of rotation about the y axis). This is the angle between a plane perpendicular to the device's screen and a plane perpendicular to the ground. If you hold the device parallel to the ground with the bottom edge closest to you and tilt the left edge of the device toward the ground, the roll angle becomes positive. Tilting in the opposite direction—moving the right edge of the device toward the ground— causes the roll angle to become negative. The range of values is -90 degrees to 90 degrees.

    Note that these angles work off of a different coordinate system than the one used in aviation (for yaw, pitch, and roll). In the aviation system, the x axis is along the long side of the plane, from tail to nose.
     */

    private fun getInfo() {
        val sb = StringBuilder()
        val bat = registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val level = bat!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = bat.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        val res = level / scale * 100
        sb.append("battery=").append(res.toString()).append("%").append("\n")

        val temper = bat.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)
        sb.append("bat temp=").append(temper / 10).append(" °C").append("\n")

        val sensorlist = sensorManager.getSensorList(Sensor.TYPE_ALL)
        sb.append("sensors: ")
        for (s in sensorlist) {
            sb.append(s?.name).append("; ")
        }
        tvSeInfo.text = sb.toString()
    }
}
