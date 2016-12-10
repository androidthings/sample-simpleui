/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.androidthings.simpleui;

import android.app.Activity;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;

import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import java.io.IOException;

public class SimpleUiActivity extends Activity {

    private static final String TAG = SimpleUiActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout gpioPinsView = (LinearLayout) findViewById(R.id.gpio_pins);
        PeripheralManagerService pioService = new PeripheralManagerService();
        for (String s : pioService.getGpioList()) {
            final Switch button = new Switch(this);
            button.setText(s);
            try {
                final Gpio ledPin = pioService.openGpio(s);
                ledPin.setEdgeTriggerType(Gpio.EDGE_NONE);
                ledPin.setActiveType(Gpio.ACTIVE_HIGH);
                ledPin.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
                button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        try {
                            ledPin.setValue(isChecked);
                        } catch (IOException e) {
                            Log.e(TAG, "error toggling gpio:", e);
                            button.setOnCheckedChangeListener(null);
                            // reset button to previous state.
                            button.setChecked(!isChecked);
                            button.setOnCheckedChangeListener(this);
                        }
                    }
                });
            } catch (IOException e) {
                Log.e(TAG, "pio error:", e);
                // disable button
                button.setEnabled(false);
            } finally {
                Log.d(TAG, "added button for GPIO: " + s);
                gpioPinsView.addView(button);
            }
        }
    }
}
