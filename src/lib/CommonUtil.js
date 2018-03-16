
import {A,Linking} from '../../lib/Common'


export function makePhoneCall(phone){
	A.confirm("是否拨打电话"+phone+"？",(index)=>{
		if(index==0){
			Linking.openURL("tel:"+phone);
		}
	});
}
