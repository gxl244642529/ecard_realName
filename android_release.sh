#测试版本，适合发到蒲公英
project_path=$(cd `dirname $0`; pwd)

echo '开始打包'
echo ${project_path}


android_path=${project_path}/android

echo ${android_path}

cd ${android_path}
${android_path}/gradlew assembleRelease
