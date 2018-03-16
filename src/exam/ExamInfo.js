



import React, { Component } from 'react';
import {
  StyleSheet,
  Text,
  TextInput,
  View,
  Image,
  TouchableOpacity,
  Dimensions,
  TouchableHighlight,
  Api,
  A,
  Account,
  ActivityIndicator,
  CommonStyle,
  PixelRatio,
  ScrollView,
  Log,
  Form
} from '../../lib/Common';

import {FormSelect} from '../../lib/TemplateForm'

import {
  TitleBar,
} from '../Global'

import {
	radioButton,
	loadingButton,
	loadingButtonDisabled
} from '../GlobalStyle'
import ButtonGroup from '../widget/ButtonGroup'
import LoadingButton from '../widget/LoadingButton'
import Select from '../../lib/Select'
import ImagePicker from '../../lib/ImagePicker'
import RadioButtonGroup from '../widget/RadioButtonGroup'

import {getDiscardCardName,isOldCard,getDiscardCardPlaceHolder,getDiscardType,TYPE_OLD,TYPE_WORK,TYPE_HERO} from '../discard/DiscardUtil'


function getSecPicNameText(data) {
	switch (data.type) {
	case TYPE_OLD:
		return "户口本";
	case TYPE_WORK:
		return "劳模证";
	case TYPE_HERO:
		return "烈属证";
	default:
		return "学生证";
	}
}


//string|思明区:611,湖里区:612,同安区:613,翔安区:614,集美区:615,海沧区:616
const areaLabels = ['思明区','湖里区','同安区','翔安区','集美区','海沧区'];
const areaValues = ['611','612','613','614','615','616'];
const idCardTypeLables = ["身份证","户口簿"];
const idCardTypeValues = [0,1];
const sexLabels = ["男","女"];
const sexValues = [1,0];
const yesNoLabels = ["是","否"];
const localValues = [true,false];
const picBg = require('../images/s_ic_add_diy.png');


export default class ExamInfo extends Component{

	constructor(props) {
		super(props);
	    this.state = Api.data;
	    Log.info(this.state);
	    this.form = new Form();
	}

	_onSubmitSuccess=()=>{
		A.alert("提交成功，我们将在2-3个工作日内进行审核",()=>{
			Api.goBack();
		});
	}
	/*
data.put("status", info.getStatus());
		data.put("type", info.getType());
		data.put("savType", info.getSavType());
		data.put("cardId", info.getCardId());
		data.put("custNo", info.getCustNo());*/
	_submit=()=>{
		this.form.submit(this,{
			api:"exam/submitInfo",
			timeoutMs:30000,
			data:{
				status:this.state.status,
				type:this.state.type,
				savType:this.state.savType,
				cardId:this.state.cardId,
				custNo:this.state.custNo
			},
			success:this._onSubmitSuccess
		});
	}

	render(){
		let data = this.state;
		console.log(data);
		return (
			<View style={CommonStyle.container}>
				<TitleBar title="提交预约资料" />  
				<ScrollView keyboardShouldPersistTaps="always"  style={styles.scrollView}>
					<View style={styles.row}>
						<Text style={styles.label}>客户号</Text>
						<TextInput 
							ref="custNo"
							style={styles.input} 
							editable={!this.state.submiting}
							returnKeyType="done"
							value={data.custNo} 
							placeholder="请输入客户号" />
					</View>
					<View style={styles.row}>
						<Text style={styles.label}>卡号</Text>
						<TextInput 
							ref="cardId"
							style={styles.input} 
							editable={false}
							value={data.cardId} 
							returnKeyType="done"
							placeholder="请输入卡号" />
					</View>
					<View style={styles.row}>
						<Text style={styles.label}>证件类型</Text>
						<RadioButtonGroup
							ref="idCardType"
							value={data.idCardType} 
							style={styles.right} 
							styleConfig={radioButton}
							labels={idCardTypeLables}
							values={idCardTypeValues}
							onChange={(index,value)=>{this.setState({idCardType:value})}} 
							/>
					</View>
					<View style={styles.row}>
						<Text style={styles.label}>证件号码</Text>
						<TextInput 
							ref="idCard"
							keyboardType="email-address"
							style={styles.input} 
							editable={false}
							returnKeyType="done"
							value={data.idCard} 
							placeholder="请输入证件号码" />
					</View>
					<View style={styles.row}>
						<Text style={styles.label}>姓名</Text>
						<TextInput 
							ref="name"
							onChangeText={(name)=>this.setState({name})}
							style={styles.input} 
							editable={!this.state.submiting}
							value={data.name} 
							returnKeyType="done"
							placeholder="请输入姓名" />
					</View>
					<View style={styles.row}>
						<Text style={styles.label}>性别</Text>
						<RadioButtonGroup 
							ref="sex"
							onChange={(index,sex)=>{this.setState({sex})}} 
							disabled={!this.state.submiting}
							value={data.sex} 
							labels={sexLabels}
							values={sexValues}
							style={styles.right} 
							styleConfig={radioButton} 
							/>
					</View>
					<View style={styles.row}>
						<Text style={styles.label}>是否本地户口</Text>
						<RadioButtonGroup 
							ref="local"
							onChange={(index,local)=>this.setState({local})}
							values={localValues}
							disabled={!this.state.submiting}
							value={data.local} 
							labels={yesNoLabels}
							style={styles.right} 
							styleConfig={radioButton} 
							/>
					</View>
					<View style={styles.row}>
						<Text style={styles.label}>手机</Text>
						<TextInput 
							ref="phone"
							onChangeText={(phone)=>this.setState({phone})}
							value={data.phone} 
							keyboardType="email-address"
							editable={!this.state.submiting}
							style={styles.input} 
							returnKeyType="done"
							placeholder="请输入手机号码" />
					</View>
					<View style={styles.row}>
						<Text style={styles.label}>生日</Text>
						<TextInput 
							ref="birth"
							value={data.birth} 
							style={styles.input} 
							placeholder="请输入生日" 
							returnKeyType="done"
							editable={false} />
					</View>
					<FormSelect 
						labelStyle={styles.label}
						ref="areaCode" 
						label="地区"
						style={styles.row}  
						value={this.state.areaCode}
						onChange={(index,areaCode)=>this.setState({areaCode})}
						values={areaValues} 
						placeholder="请选择地区" 
						labels={areaLabels} />
					{!isOldCard(data)&&<View style={styles.row}>
						<Text style={styles.label}>{getDiscardType(this.state)}</Text>
						<TextInput 
							ref="navCode"
							editable={!this.state.submiting}
							onChangeText={(navCode)=>this.setState({navCode})}
							value={this.state.navCode}
							style={styles.input} 
							returnKeyType="done"
							placeholder={getDiscardCardPlaceHolder(this.state)} />
					</View>}
					<View style={styles.picContainer}>
						<Text style={[styles.label,styles.picLabel]}>身份证:</Text>
						<ImagePicker 
							ref="img1"
							disabled={this.state.submiting}
							placeholder="请选择照片"
							file={true}
							onChange={(img1)=>this.setState({img1})}
							album={false} 
							style={{marginBottom:10}}>
							<Image 
								resizeMode="contain" 
								style={{alignSelf:'center',width:300,height:100}} 
								source={this.state.img1 ?  {uri:this.state.img1} : picBg} 
								/>
						</ImagePicker>
					</View>
					<View style={styles.picContainer}>
						<Text style={[styles.label,styles.picLabel]}>{getSecPicNameText(this.state)}:</Text>
						<ImagePicker 
							file={true}
							disabled={this.state.submiting}
							ref="img2"
							placeholder={"请选择"+getSecPicNameText(this.state)}
							album={false} 
							onChange={(img2)=>this.setState({img2})}
							style={{marginBottom:10}}
							>
							<Image 
								resizeMode="contain" 
								style={{alignSelf:'center',width:300,height:100}} 
								source={this.state.img2 ?  {uri:this.state.img2} : picBg} 
								/>
						</ImagePicker>
					</View>
					<LoadingButton 
						loading={this.state.submiting}
						text="提交" 
						onPress={this._submit}
						disabled={this.state.submiting}
						style={this.state.submiting ? loadingButtonDisabled : loadingButton.loadingButton} 
						textStyle={loadingButton.loadingButtonText} />
				</ScrollView>
			</View>
		);
	}
}


const styles = StyleSheet.create({
	arrow:{width:7,height:12,marginRight:5,marginLeft:5},
	scrollView:{flex:1,backgroundColor:'#F2F2F2'},
	row:{backgroundColor:'#FFFFFF',marginTop:5,height:36,flexDirection:'row',alignItems:'center',paddingLeft:0},
	input:{fontSize:14,flex:1,height:36,marginLeft:10},
	right:{flexDirection:'row',flex:1,marginLeft:10},
	label:{marginLeft:10,fontSize:13,color:'#555555'},
	picLabel:{marginTop:10},
	picContainer:{backgroundColor:'#FFFFFF',marginTop:5}
});
/*
<TouchableOpacity style={styles.leftButtonSelected}>
		            		<Text style={styles.textSelected}>优惠卡</Text>
		            	</TouchableOpacity>
*/


