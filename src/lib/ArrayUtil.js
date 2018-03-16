
import {ListView} from '../../lib/Common'

export const EMPTY_DATA_SOURCE = 
	new ListView.DataSource({rowHasChanged: (r1, r2) => r1 !== r2})
	.cloneWithRows([]);


/**
数组中删除元素，数组元素为object，并有id属性
*/
export function deleteById(arr,value){
	for(let i=0 , c = arr.length; i < c; ++i){
		let a = arr[i];
		if(a.id==value){
			arr.splice(i,1);
			break;
		}
	}
}



export function arrayFind(arr:Array<any>,value:any){
	for(let i=0 , c = arr.length; i < c; ++i){
		if(arr[i]==value){
			return i;
		}
	}
	return -1;
}