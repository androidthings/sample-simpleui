/*
 * Copyright 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.androidthings.simpleui

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.CompoundButton.OnCheckedChangeListener
import android.widget.Switch
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.PeripheralManager
import java.io.IOException

class MainActivity : Activity() {

    private val gpioList = ArrayList<Gpio>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gpioContainer: ViewGroup = findViewById(R.id.gpio_list)

        val pioManager = PeripheralManager.getInstance()
        pioManager.gpioList.forEach { pinName ->
            val gpio = pioManager.openGpio(pinName).apply {
                setActiveType(Gpio.ACTIVE_HIGH)
                setEdgeTriggerType(Gpio.EDGE_NONE)
                setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
            }
            gpioList.add(gpio)

            val child = layoutInflater.inflate(R.layout.list_item_gpio, gpioContainer, false)
            val button: Switch = child.findViewById(R.id.gpio_switch)
            button.apply {
                text = pinName
                setOnCheckedChangeListener(ChangeListener(gpio))
            }
            gpioContainer.addView(button)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        gpioList.forEach { gpio -> gpio.close() }
    }

    inner class ChangeListener(private val gpio: Gpio) : OnCheckedChangeListener {

        override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
            try {
                gpio.value = isChecked
            } catch (e: IOException) {
                Log.e(LOG_TAG, "Error toggling gpio ${gpio.name}", e)
                // reset button to previous state.
                buttonView.apply {
                    setOnCheckedChangeListener(null)
                    setChecked(!isChecked)
                    setOnCheckedChangeListener(this@ChangeListener)
                }
            }
        }
    }
}

private const val LOG_TAG = "SimpleUI"
