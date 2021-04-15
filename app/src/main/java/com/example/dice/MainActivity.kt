package com.example.dice

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity(), SensorEventListener, SeekBar.OnSeekBarChangeListener {

    companion object {
        const val SHAKE_THRESHOLD: Int = 3
    }

    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor

    private lateinit var dices: List<ImageView>
    private lateinit var drawables: List<Int>
    private lateinit var dicesNumSeekBar: SeekBar
    private lateinit var statusTextView: TextView

    private var dicesAmount: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        setupDices()
        setupSeekBar()
        setStatus()
        displayDices()
    }

    private fun setupDices() {
        dices = listOf(findViewById(R.id.dice1), findViewById(R.id.dice2), findViewById(R.id.dice3))
        drawables = listOf(R.drawable.dice_1, R.drawable.dice_2, R.drawable.dice_3, R.drawable.dice_4, R.drawable.dice_5, R.drawable.dice_6)
    }

    private fun setupSeekBar() {
        dicesNumSeekBar = findViewById(R.id.dicesAmountSeekBar)
        statusTextView = findViewById(R.id.dicesStatusTxtView)
        dicesNumSeekBar.progress = dicesAmount
        dicesNumSeekBar.max = 3
        dicesNumSeekBar.setOnSeekBarChangeListener(this)
    }

    private fun setStatus() {
        statusTextView.text = dicesAmount.toString()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) {
            return
        }

        val x: Float = event.values[0]
        val y: Float = event.values[1]
        val z: Float = event.values[2]

        val acceleration: Double = kotlin.math.sqrt((x * x + y * y + z * z).toDouble()) - SensorManager.GRAVITY_EARTH

        if (acceleration > SHAKE_THRESHOLD) {
            shake()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    private fun generateRandomNumber(): Int {
        val randomGenerator = Random()
        return randomGenerator.nextInt(6) + 1
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setDiceValue(_dice: Int, _value: Int) {
        val imgResource: Drawable = resources.getDrawable(drawables[_value - 1], theme)
        dices[_dice].setImageDrawable(imgResource)
    }

    private fun shake() {
        var generatedValue: Int

        for (i in 0 until dicesAmount) {
            dices[i].visibility = ImageView.VISIBLE
            generatedValue = generateRandomNumber()
            setDiceValue(i, generatedValue)
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        dicesAmount = progress
        setStatus()
        displayDices()
    }

    private fun displayDices() {
        dices[0].visibility = if (dicesAmount > 0) ImageView.VISIBLE else ImageView.INVISIBLE
        dices[1].visibility = if (dicesAmount > 1) ImageView.VISIBLE else ImageView.INVISIBLE
        dices[2].visibility = if (dicesAmount > 2) ImageView.VISIBLE else ImageView.INVISIBLE
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }
}