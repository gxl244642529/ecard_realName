
import StateList from './StateList'
//只能是图片
export default class Drawable{

	constructor(source,style) {
	 	this.source = source;
	 	this.style = style;
	}

	static create(source,style){
		return new Drawable(StateList.create(source),StateList.create(style));
	}

	get(props){
		//状态
		if(props.disabled){
			return {source:this.source.disabled,style:this.style.disabled};
		}
		if(props.selected){
			return {source:this.source.selected,style:this.style.selected};
		}
		return {source:this.source.normal,style:this.style.normal};
	}


}