
const GeoCoderModule = require('react-native').NativeModules.GeoCoderModule;

export default class GeoUtil{
	static getAddress(lat,lng,success,fail){
		GeoCoderModule.getAddress(lat,lng,success,fail);
	}
}

