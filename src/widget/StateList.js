

//可能是一个颜色，也可能是一个图片
export default class StateList{

	constructor(normal,selected,disabled) {
	 	this.normal = normal;
	 	this.selected = selected;
	 	this.disabled = disabled;
	}

	static create(data){
		if(data.normal){
			return new StateList(data.normal,data.selected || data.normal,data.disabled || data.normal);
		}
		return new StateList(data,data,data);
	}

	get(props){
		//状态
		if(props.disabled && !props.selected){
			return this.disabled;
		}
		if(props.selected){
			return this.selected;
		}
		return this.normal;
	}


}