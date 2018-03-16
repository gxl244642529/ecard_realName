



export default class DataUtil{

	static toInt(value){
		if(value === undefined || value === null){
			return null;
		}
		if(value==='' || value==='null'){
			return null;
		}

		return parseInt(value);
	}

	static toDouble(value){
		if(value === undefined || value === null){
			return null;
		}
		if(value==='' || value==='null'){
			return null;
		}

		return parseFloat(value);
	}


	static toString(value){
		if(value==='null')return value;
		return value;
	}
}