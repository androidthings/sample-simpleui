# Simple Peripheral UI

This utility exposes the GPIO of an Android Things device as switch widgets in
a graphical UI. You review the state of inputs, and click the switches to control
the state of outputs.

## Introduction

This utility shows on the attached display Android [Switch][switch-ref] widgets for each GPIO pin exported by the board.

When you click or tap on the switch, the GPIO active status switch from HIGH to LOW according to the widget state.

You can attach LEDs or other digital output peripheral to your GPIO pins and toggle them on and off without changing a single line of code
(if you connect LEDs: make sure you add 470 ohms current-limiting resistors in series to avoid damaging them).

## Screenshots

![Simple UI sample demo][demo-gif]

[(Watch the demo on YouTube)][demo-yt]

## Pre-requisites

- Android Things compatible board
- Display with touch input or external pointing device
- Android Studio 2.2+
- LEDs
- resistors
- Breadboard

## Build and install

On Android Studio, click on the "Run" button.

If you prefer to run on the command line, type

```bash
./gradlew installDebug
adb shell am start com.example.androidthings.simpleui/.SimpleUiActivity
```

## License

Copyright 2016 The Android Open Source Project, Inc.

Licensed to the Apache Software Foundation (ASF) under one or more contributor
license agreements.  See the NOTICE file distributed with this work for
additional information regarding copyright ownership.  The ASF licenses this
file to you under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License.  You may obtain a copy of
the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
License for the specific language governing permissions and limitations under
the License.

[demo-yt]: https://www.youtube.com/watch?v=Fry_yvoDvt4&list=PLWz5rJ2EKKc-GjpNkFe9q3DhE2voJscDT&index=10
[demo-gif]: demo1.gif
[switch-ref]: https://developer.android.com/reference/android/widget/Switch.html
