

cd "/SourceCode/projects/ecard"
#编译项目
xcodebuild -workspace ecard.xcworkspace -scheme myecardbundle -configuration Release
xcodebuild -workspace ecard.xcworkspace -scheme rechargebundle -configuration Release
xcodebuild -workspace ecard.xcworkspace -scheme ecardlibbundle -configuration Release
xcodebuild -workspace ecard.xcworkspace -scheme safebundle -configuration Release


xcodebuild -workspace ecard.xcworkspace -scheme dmlib -configuration Release
xcodebuild -workspace ecard.xcworkspace -scheme ecardlib -configuration Release
xcodebuild -workspace ecard.xcworkspace -scheme myecard -configuration Release
xcodebuild -workspace ecard.xcworkspace -scheme safe -configuration Release
xcodebuild -workspace ecard.xcworkspace -scheme recharge -configuration Release
xcodebuild -workspace ecard.xcworkspace -scheme amap -configuration Release

xcodebuild -workspace ecard.xcworkspace -scheme ecard -configuration Release


xcrun -sdk iphoneos -v PackageApplication ./DerivedData/ecard/Build/Products/Release-iphoneos/ecard.app -o /Temp/ecard_dev.ipa

