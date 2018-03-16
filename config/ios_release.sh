#测试版本，适合发到蒲公英
project_path=$(cd `dirname $0`; pwd)

echo '开始打包'
echo ${project_path}


ios_path=${project_path}/ios
app_path=${ios_path}/DerivedData/ecard/Build/Products/Release-iphoneos/sharecharge_release.app
ipa_path=${ios_path}/DerivedData/ecard/Build/Products/Release-iphoneos/sharecharge_release.ipa


cd ${project_path}
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


/usr/local/bin/react-native bundle --entry-file ./src/index.ios.js --bundle-output ./ios/bundle/index.ios.jsbundle --platform ios --assets-dest ./ios/bundle --dev false

cd ${ios_path}
xcodebuild -project ecard.xcodeproj -scheme sharecharge_release -configuration Release
xcrun -sdk iphoneos -v PackageApplication ${app_path} -o ${ipa_path}


open ${ios_path}/DerivedData/ecard/Build/Products/Release-iphoneos



echo '========================================正在上传========================================'
curl -F "file=@/"${ipa_path} -F "uKey=a1b41c68a16625dc75b2375680bed018" -F "_api_key=6c22b1571772be5a045bfa07e42cc5a3" https://qiniu-storage.pgyer.com/apiv1/app/upload --progress

echo '========================================上传成功========================================'
