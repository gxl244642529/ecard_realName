cd "/SourceCode/react/react-native/ebusiness_new/ecard/android"
./gradlew assembleRelease

adb uninstall com.citywithincity.ecard
adb install /SourceCode/react/react-native/ebusiness_new/ecard/android/app/build/outputs/apk/app-release.apk
adb shell am start -n com.citywithincity.ecard/com.citywithincity.ecard.ReactEnterActivity


curl -X "POST" "http://api.fir.im/apps" \
     -H "Content-Type: application/json" \
     -d "{\"type\":\"android\", \"bundle_id\":\"com.citywithincity.ecard\", \"api_token\":\"af9730f9c5e84f971ef9b02c2e1214de\"}"