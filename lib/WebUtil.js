
const WebModule = require('react-native').NativeModules.WebModule;


export default class WebUtil{

	static open(url,title){
		if(!title){
			title = "";
		}
		WebModule.open(url,title);
	}

}