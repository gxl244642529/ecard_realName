

cd "/SourceCode/projects/ecard/ecard"
react-native bundle --entry-file ./index.ios.js --bundle-output ./ios/bundle/index.ios.jsbundle --platform ios --assets-dest ./ios/bundle --dev false

cd "/SourceCode/projects/ecard"
xcodebuild -workspace ecard.xcworkspace -scheme ecard -configuration Release


xcrun -sdk iphoneos -v PackageApplication ./DerivedData/ecard/Build/Products/Release-iphoneos/ecard.app -o /Temp/ecard1.ipa


echo '========================================正在上传========================================'
curl -F "file=@/Temp/ecard1.ipa" -F "uKey=a1b41c68a16625dc75b2375680bed018" -F "_api_key=6c22b1571772be5a045bfa07e42cc5a3" https://qiniu-storage.pgyer.com/apiv1/app/upload --progress

echo '========================================上传成功========================================'
