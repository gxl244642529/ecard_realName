#测试版本，适合发到蒲公英
project_path=$(cd `dirname $0`; pwd)

echo '开始打包'
echo ${project_path}


ios_path=${project_path}
app_path=${project_path}/DerivedData/ecard/Build/Products/Release-iphoneos/ecard.app
ipa_path=${project_path}/DerivedData/ecard/Build/Products/Release-iphoneos/ecard.ipa


cd ${project_path}
#编译项目
xcodebuild -workspace ecard.xcworkspace -scheme myecardbundle -configuration Debug
xcodebuild -workspace ecard.xcworkspace -scheme rechargebundle -configuration Debug
xcodebuild -workspace ecard.xcworkspace -scheme ecardlibbundle -configuration Debug
xcodebuild -workspace ecard.xcworkspace -scheme safebundle -configuration Debug


xcodebuild -workspace ecard.xcworkspace -scheme dmlib -configuration Debug
xcodebuild -workspace ecard.xcworkspace -scheme ecardlib -configuration Debug
xcodebuild -workspace ecard.xcworkspace -scheme myecard -configuration Debug
xcodebuild -workspace ecard.xcworkspace -scheme safe -configuration Debug
xcodebuild -workspace ecard.xcworkspace -scheme recharge -configuration Debug
xcodebuild -workspace ecard.xcworkspace -scheme amap -configuration Debug


/usr/local/bin/react-native bundle --entry-file ./index.ios.js --bundle-output ./ios/bundle/index.ios.jsbundle --platform ios --assets-dest ./ios/bundle --dev false

xcodebuild -workspace ecard.xcworkspace -scheme ecard -configuration Release
xcrun -sdk iphoneos -v PackageApplication ${app_path} -o ${ipa_path}


open ${project_path}/DerivedData/ecard/Build/Products/Release-iphoneos


#echo '========================================正在上传========================================'
#curl -F "file=@"${ipa_path} -F "uKey=a1b41c68a16625dc75b2375680bed018" -F "_api_key=6c22b1571772be5a045bfa07e42cc5a3" https://qiniu-storage.pgyer.com/apiv1/app/upload --progress

#echo '========================================上传成功========================================'
