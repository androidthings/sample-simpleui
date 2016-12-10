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
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class SimpleUiActivity extends Activity {

    private static final String TAG = SimpleUiActivity.class.getSimpleName();

    private Map<String, Gpio> mGpioMap = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout gpioPinsView = (LinearLayout) findViewById(R.id.gpio_pins);
        LayoutInflater inflater = getLayoutInflater();
        PeripheralManagerService pioService = new PeripheralManagerService();

        for (String name : pioService.getGpioList()) {
            View child = inflater.inflate(R.layout.list_item_gpio, gpioPinsView, false);
            Switch button = (Switch) child.findViewById(R.id.gpio_switch);
            button.setText(name);
            gpioPinsView.addView(button);
            Log.d(TAG, "Added button for GPIO: " + name);

            try {
                final Gpio ledPin = pioService.openGpio(name);
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
                            buttonView.setOnCheckedChangeListener(null);
                            // reset button to previous state.
                            buttonView.setChecked(!isChecked);
                            buttonView.setOnCheckedChangeListener(this);
                        }
                    }
                });

                mGpioMap.put(name, ledPin);
            } catch (IOException e) {
                Log.e(TAG, "Error initializing GPIO: " + name, e);
                // disable button
                button.setEnabled(false);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        for (Map.Entry<String, Gpio> entry : mGpioMap.entrySet()) {
            try {
                entry.getValue().close();
            } catch (IOException e) {
                Log.e(TAG, "Error closing GPIO " + entry.getKey(), e);
            }
        }
        mGpioMap.clear();
    }
}
