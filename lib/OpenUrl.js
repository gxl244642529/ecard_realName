
const SysModule = require('react-native').NativeModules.SysModule;

export default class OpenUrl{

  //包名+入口如com.czc.sharecharge:com.czc.sharecharge.MainActivity
	static open(url,callback){
	   SysModule.open(url,callback);
	}
  //包名如com.czc.sharecharge
  static canOpen(url,callback){
    SysModule.canOpen(url,callback);
  }

}
