## PRCO304 Final Year Project - Adam Foot - AARA

![](https://github.com/Adam-Foot/PRCO304/workflows/Android%20CI/badge.svg)

Trello Link: https://trello.com/invite/b/FzjlqvxM/6326b7ef472f286a9026ad51d32edd0d/prco304-ar

## Introduction
Within this repository you will find my final year project augmented reality Android aplication.

My application has been designed and developed to allow it's users to navigate a chosen heritage site and learn new facts about the history of the area through the use of augmented reality. With their smartphone devices the users can scan objects around the heritage site and be displayed lesser-known facts about the site.


## Install Instructions

### Prerequisites:
- Android Studio 3.6+
- Android Smarthpone running Android (Nougat) 7.0+
- Android device must be supported by AR Core (https://developers.google.com/ar/discover/supported-devices)
- A Google Maps API project, and a created API key (https://console.cloud.google.com/google/maps-apis/overview)

After you have all the requirements above you can download the repository and open the project in Android Studio. Then follow these instructions:

1) Navigate to the AndroidManifest.xml file, found in the app/manifests folder.
2) Find the meta-data section that is named ```com.google.android.geo.API_KEY```.
3) Inside ```<activity android:value""```, paste your Google API key between the quotations. This will allow you to use Google Maps within my application. *If you don't plan to use Google Maps you can leave this blank, but be warned the Google Maps activity will not load and you will be presented with a blank map*.
4) On your Android smartphone you must enable Developer Options and turn on USB Debugging. To do so, follow the steps found here: 
5) Plug your mobile device into your PC and wait for your device name to appear in Android Studio near the "Run" button.
6) Once your device has been detected you can press Run and Android Studio will install the application onto your device. You will find the icon within your app page or home screen, depending on how you have your phone setup.
