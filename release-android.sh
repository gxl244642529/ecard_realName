cd "/SourceCode/projects/ecard/ecard/android"
./gradlew assembleRelease


echo '========================================正在上传========================================'
curl -F "file=@/SourceCode/projects/ecard/ecard/android/app/build/outputs/apk/app-atest-release.apk" -F "uKey=a1b41c68a16625dc75b2375680bed018" -F "_api_key=6c22b1571772be5a045bfa07e42cc5a3" https://qiniu-storage.pgyer.com/apiv1/app/upload --progress
echo '\n========================================上传成功========================================'



