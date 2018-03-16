
import {
  AsyncStorage
} from 'react-native';


export default class LocalData{

	static putObject(key:String,value:any){
		if(typeof value === "object"){
			value = JSON.stringify(value);
		}
		AsyncStorage.setItem(key,value);
	}

	static getValue(key:String){
		let promise = new Promise((resolve,reject)=>{
			AsyncStorage.getItem(key)
				.then((value)=>{
					if(value){
						resolve(value);
					}else{
						reject();
					}
				},()=>{
					reject();
				});

		});
		
		return promise;
	}
	static getObject(key:String){
		let promise = new Promise((resolve,reject)=>{
			AsyncStorage.getItem(key)
				.then((value)=>{
					let result = JSON.parse(value);
					if(result){
						resolve(result);
					}else{
						reject();
					}
				},()=>{
					reject();
				});

		});

		
		return promise;
	}


}