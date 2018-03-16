import React,{Component} from 'react';

import {
		View,
		Text,
		StyleSheet,
		Image,
		TouchableOpacity,
		Dimensions,
		ScrollView,
		ImagePicker,
		Switch,
		TextInput,
		Api,
		CommonStyle,
		ListView,
		A,
		PixelRatio,
		Platform
		} from '../../lib/Common'

import TitleBar from '../widget/TitleBar'
import StateListView from '../widget/StateListView'
const SCREEN_WIDTH = Dimensions.get('window').width;
import Notifier from '../../lib/Notifier'
import LoadingButton from '../widget/LoadingButton'
import {formatDate} from '../lib/StringUtil'
import RCardNoReal from './RCardNoReal'
import StandardStyle from '../lib/StandardStyle'
import NfcUtil from '../../lib/NfcUtil'
import {FROM_REAL,FROM_REAL_CARD,BANK_CARD_FUNDING ,FINISH} from './CommonData'
import {
	radioButton,
	loadingButton,
	loadingButtonDisabled
} from '../GlobalStyle'
const NORMAL = 0; //正常
const LOST  = 1;  //报失中
const FUNDING  = 2; //打款中
const FUND_SUCCESS  = 3; //打款成功
const FUND_ERROR = 4;//打款失败
const RESULT_LACKOFMONEY = 99//核销余额不足


const GO_CHECK = 10 //去验证
const CHECKING = 20 //验证中

export function 	isReal(callback){
		// let data = {fromto:FROM_REAL_CARD,isReal:FINISH}
		Api.api({
			api:"newRcard/isAgree",
			success:callback,
		})
	}


export function itemState(rowData){
  let itemStateCompont ;
  if (rowData.lostStatus===NORMAL) {
      itemStateCompont=	<TouchableOpacity style={styles.lostTouStyle}
        onPress={()=>{_lost(rowData)}}>
          <Text style={{color:'#fff'}}>挂失</Text>
        </TouchableOpacity>
  }else if (rowData.lostStatus===LOST) {
    itemStateCompont=	<View style={styles.statusStyle}>
        <Text>挂失中...</Text>
      </View>
  }else if (rowData.lostStatus===FUNDING) {
    itemStateCompont=	<View style={styles.statusStyle}>
        <Text>打款中...</Text>
      </View>
  }else if (rowData.lostStatus=== FUND_SUCCESS) {
    itemStateCompont=	<View style={styles.statusStyle}>
        <Text>打款成功</Text>
      </View>
  }else if (rowData.lostStatus=== FUND_ERROR) {
    itemStateCompont=	<View style={styles.statusStyle}>
        <Text>打款失败</Text>
      </View>
  }else if (rowData.lostStatus=== RESULT_LACKOFMONEY) {
		itemStateCompont=	<View style={styles.statusStyle}>
				<Text>打款失败</Text>
			</View>
  }else if(rowData.lostStatus===GO_CHECK){
		itemStateCompont=	<View style={{justifyContent:'space-between'}}><TouchableOpacity style={styles.lostTouStyle}
			onPress={()=>{_goCheck(rowData)}}>
				<Text style={{color:'#fff'}}>去验证</Text>
			</TouchableOpacity><TouchableOpacity style={[styles.lostTouStyle,{marginTop:10,backgroundColor:"#DBDBDB"}]}
				onPress={()=>{_cancel(rowData)}}>
					<Text style={{color:'#fff'}}>撤销</Text>
				</TouchableOpacity></View>
	}
	else if(rowData.lostStatus===CHECKING){
		itemStateCompont=	<View style={styles.statusStyle}>
				<Text>验证中</Text>
			</View>
	}
  return(
      itemStateCompont
  );
}
export function _cardDetail(rowData){

	if (rowData.lostStatus===GO_CHECK) {
		// Api.push("/realName/questionVertify/"+rowData.cardId);
		_goCheck(rowData);
	}else if (rowData.lostStatus===CHECKING) {
		//验证中页面
		Api.push("/realName/rCardChecking/"+rowData.cardId);
	}else {
		Api.push("/realName/rCardDetail/"+rowData.cardId);
	}


}
export function _cancel(rowData){
	let data = {cardId:rowData.cardId};
	A.confirm("是否确定撤销验证？",(index)=>{
		if(index==0){

			Api.api({
				api:"rcard/deletehis",
				data:data,
				crypt:0,
				success:(result)=>{
					A.alert("撤销成功")
				}
			});
		}else{
			return;
		}
	});
}
export function _lost(rowData){
  Api.push("/realName/rCardLost/"+rowData.cardId);
  // console.log(rowData.cardId);

}

function getCardIdExt(cardIdExt){
	if(cardIdExt!=null&&cardIdExt.startsWith('000000000000000000')){
		return '';
	}
	return cardIdExt;
}
export function _goCheck(rowData){
	isReal((result)=>{
		if(result){
			//跳转信用承若书
			Api.push('/realName/letterAgreeVerify/'+rowData.cardId);
		}else {
			noLetterCheck(rowData);
		}
	})
}
export function noLetterCheck(rowData){
	let data = {cardId:rowData.cardId,fromto:FROM_REAL_CARD}
	if(Platform.OS=='android'){
		NfcUtil.isAvailable((result)=>{
			if(result){
				Api.push("/realName/nfcVertify/"+JSON.stringify(data));
			}else {
				Api.push("/realName/questionVertify/"+JSON.stringify(data));
			}
		});
	}else {
		Api.push("/realName/questionVertify/"+JSON.stringify(data));
	}
}
// <View style={{flexDirection:'row',}}>
// 	<Image source={require('./images/time.png')} style={styles.timeStyle}/>
// 	<Text style={{marginLeft:5}}>{rowData.createDate?formatDate(rowData.createDate):null}</Text>
// </View>
export function renderRow(rowData,index){
	console.log(index);


    return (
			<View key={rowData.cardId}>
	      <TouchableOpacity  style={styles.rowDataStyle} onPress={()=>{_cardDetail(rowData)}}>
	  					<View style={styles.msgViewStyle}>
	  						<View  style={styles.rowViewStyle}>
	  							<Image source={require('./images/card.png')} style={styles.cardStyle }/>
	  							<View style={{padding:10}}>
	  								<Text>{rowData.cardId} </Text>
	  								<Text>{getCardIdExt(rowData.cardIdExt)}</Text>
	  								<View style={{flexDirection:'row',}}>
	  									<Image source={require('./images/name.png')} style={styles.timeStyle}/>
	  									<Text style={{marginLeft:5}}>{rowData.bindName}</Text>
	  								</View>
	  							</View>
	  						</View>
	  						<View style={styles.itemStyle}>
	  							{itemState(rowData)}
	  						</View>
	  					</View>
	  		</TouchableOpacity>
				{lineView()}
			</View>
     );

}
export  class MsgView extends Component{
  constructor(props) {
    super(props);
  }
  render(){
    return(
      <View>
        <View style={this.props.msgSty? this.props.msgSty:styles.main}>
          <Text style={[StandardStyle.h3,StandardStyle.fontBlack]}>{this.props.tip}</Text>
          <Text style={[StandardStyle.h4,StandardStyle.fontBlack]}>{this.props.msg} {this.props.msgTo}</Text>
        </View>
        {lineView()}
      </View>
    );
  }
}
export function lineView(){
	return  <View style={styles.viewStyle}></View>
}


const styles = StyleSheet.create({

	rowDataStyle:{
		height:100,
		backgroundColor:'#fff',
		marginTop:10,
		justifyContent:'center',
	},
	lostTouStyle:{
		width:80,
		height:35,
		backgroundColor:'#e8464c',
		justifyContent:'center',
		alignItems:'center',
		borderRadius:5
	},
	statusStyle:{
		width:80,
		height:50,
		justifyContent:'center',
		alignItems:'center'
	},
  msgViewStyle:{
    flexDirection:'row',
    justifyContent:'space-between',
    padding:10,
    alignItems:'center'
  },
  rowViewStyle:{
    flexDirection:'row',
    padding:10,
    alignItems:'center'
  },
  cardStyle:{
    width:30,
    height:30,
    resizeMode:'contain'
  },
  itemStyle:{
    justifyContent:'flex-end',
    alignItems:'flex-end',
  },
  timeStyle:{
     width:15,
     height:15,
     resizeMode:'contain'
  },
	viewStyle:{
		height:1/PixelRatio.get(),
		backgroundColor:'#d7d7d7',
		marginLeft:5,
	},
	main:{
		// padding:20,
		// marginLeft:5,
		// paddingLeft:10,
		// paddingTop:20,
		// paddingBottom:20,
		// paddingRight:10,
		height:60,
		paddingRight:10,
		paddingLeft:15,
		flexDirection:"row",
		justifyContent:'center',
		alignItems:'center',
		justifyContent:"space-between",
	},


});
