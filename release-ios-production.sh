cd "/SourceCode/projects/ecard2.6.2/dev2.6.2_20170612/ecard/"
react-native bundle --entry-file ./index.ios.js --bundle-output ./ios/bundle/index.ios.jsbundle --platform ios --assets-dest ./ios/bundle --dev false

cd "/SourceCode/projects/ecard2.6.2/dev2.6.2_20170612/"
xcodebuild -workspace ecard.xcworkspace -scheme ecard -configuration Release

xcrun -sdk iphoneos -v PackageApplication ./DerivedData/ecard/Build/Products/Release-iphoneos/ecard.app -o /Temp/ecard_release.ipa

