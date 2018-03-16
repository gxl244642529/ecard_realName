
import Api,{ERROR_NETWORK,ERROR_SERVER,ERROR_ALERT,ERROR_TOAST,CachePolicy_NoCache} from './network/Api'
const ApiModule = require('react-native').NativeModules.ApiModule;
import {CRYPT_BOTH} from '../lib/network/Api'
import A from './Alert'
import MD5 from 'crypto-js/md5'


const FORM_RULES = {

	phone:function(text){
		var myreg = (/^0?[1][3|4|5|7|8][0-9]\d{8}$/);
		return myreg.test(text);
	},
	order:function(text){
		var myreg=/^.{1,3}$/;
		return myreg.test(text);
	},
	postCode:function(text){
		if(text.length!=6){
			return false;
		}
		var myreg = (/[0-9]{6}/);
		return myreg.test(text);
	}
}

const FORM_RULES_TEXT = {
	phone:"请输入正确格式的手机号码",
	order:"请输入0-999的数字作为商品序号",
	postCode:"请输入6位邮编号码"

}

function setError(parentComponent,currentComponent,error){
	if(parentComponent.props.setError){
		parentComponent.props.setError(currentComponent.props.placeholder);
	}else{
		A.alert(error,()=>{
			currentComponent.focus && currentComponent.focus();
		});
	}
}

function validateRule(parentComponent,currentComponent,value){
	let validate = FORM_RULES[currentComponent.props.rule];
	if(validate){
		if(!validate(value)){
			setError(parentComponent,currentComponent,FORM_RULES_TEXT[currentComponent.props.rule]);
			return false;
		}
	}
	return true;
}

function getValue(value,currentComponent){
	if(currentComponent.props.secureTextEntry){
		return MD5(value).toString();
	}
	return value;
}

function defaultShowError(error,errorType){
	if(errorType == ERROR_TOAST || errorType == ERROR_NETWORK){
		A.toast(error);
	}else{
		A.alert(error);
	}
}
// || comp.state[key] || (c.getValue && c.getValue());
export default class Form{

	constructor(s){
		this.state={...s};
	}

	setState(s){
		this.state = Object.assign(this.state,s);
	}

	submit(comp,api,fields=null){
		let refs = comp.refs;
		let postData = {};
		let files = {};
		for( let key in refs){
			let c = comp.refs[key];
			//判断类型
			let value = this.state[key];
			if(value === null || value === "" || value===undefined){
				value = comp.state[key];
				if(value === null || value === "" || value===undefined){
					value = c.getValue && c.getValue();
				}
			}
			if( value === null || value === "" || value===undefined){
				//不检查
				if(c.props.required === false){
					continue;
				}
				setError(comp,c,c.props.placeholder);
				return;
			}
			//值，检测正确性
			if(c.props.rule){
				if(!validateRule(comp,c,value)){
					return;
				}
			}
			if(c.props.file ||　c.file){
				files[key] = value;
			}else{
				postData[key] = getValue(value,c);
			}
		}


		console.log(postData,files);

		if(api.data){
			postData=Object.assign(postData,api.data);
		}

		comp.setState({submiting:true});
		ApiModule.api({
			api:api.api,
			data:postData,
			files:files,
			timeoutMs : api.timeoutMs || 5000,
			cachePolicy:CachePolicy_NoCache,
			cancelLevel: api.cancelLevel ,
			type:api.type ,
			crypt: api.crypt,
			waitingMessage:api.waitingMessage
			},
			(result)=>{
				comp.setState({submiting:false});
				api.success && api.success(result,postData);
			},
			(error,errorType)=>{
				comp.setState({submiting:false});
				if(api.error){
					if(api.error(error,errorType)){
						return;
					}
				}
				defaultShowError(error,errorType);
			}
		);

	}
}
