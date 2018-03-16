
export function findGetIndex(arr,value){
	for(let i=0 , c = arr.length; i < c; ++i){
		if(arr[i]===value){
			return i;
		}
	}
	return -1;
}