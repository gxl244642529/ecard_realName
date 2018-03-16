

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

import {
  TitleBar,
} from '../Global'

import SchoolSelect from './SchoolSelect'
import ButtonGroup from '../widget/ButtonGroup'
import LoadingButton from '../widget/LoadingButton'

import {
	radioButton,
	loadingButton,
	loadingButtonDisabled
} from '../GlobalStyle'

import Select from '../../lib/Select'
import ImagePicker from '../../lib/ImagePicker'
import RadioButtonGroup from '../widget/RadioButtonGroup'

import {getDiscardCardName,isOldCard,getDiscardCardPlaceHolder,getDiscardType,TYPE_OLD,TYPE_WORK,TYPE_HERO,isStudentCard} from '../discard/DiscardUtil'


function getImgTip(data){
	switch (data.type) {

	case TYPE_WORK:
	  return "劳模证原件照片";
	case TYPE_HERO:
	  return "烈属证原件照片";
	case TYPE_OLD:
	  return "户口本原件照片";
	default:
	  return "学生证原件照片";
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
const picArrow = require('../images/_ic_arrow.png');



export default class DiscardInfo extends Component{

	constructor(props) {
		super(props);
	    this.state = {...Api.discard,...{showSchool:false}};
	    //Log.info(this.state);
	    this.form = new Form();
	}

	componentWillUnmount() {
	  Api.discard = null;
	}

	onCancel=()=>{
		this.setState({showSchool:false})
	}
	/*
data.put("status", info.getStatus());
		data.put("type", info.getType());
		data.put("savType", info.getSavType());
		data.put("cardId", info.getCardId());
		data.put("custNo", info.getCustNo());*/
	_submit=()=>{
		this.form.submit(this,{
			api:"book/submitInfo",
			timeoutMs:30000,
			data:{
				status:this.state.status,
				type:this.state.type,
				savType:this.state.savType,
				cardId:this.state.cardId,
				custNo:this.state.custNo,
				schoolName:this.state.schoolName,
				schoolCode:this.state.schoolCode
			},
			success:(result)=>{
				A.alert("提交成功，我们会在2-3个工作日内进行审核，请耐心等待",()=>{
					Api.goBack();
				});
			}
		});
	}

	onChangeSchool=(data)=>{
		this.setState({...{showSchool:false},...{schoolName:data.name,schoolCode:data.code}});
	}

	render(){
		let data = this.state;
		console.log(data);
		return (
			<View style={CommonStyle.container}>
				<TitleBar title="提交预约资料" />
				<ScrollView keyboardShouldPersistTaps="always"   style={styles.scrollView}>
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
							style={styles.input}
							editable={false}
							returnKeyType="done"
							defaultValue={data.idCard}
							placeholder="请输入证件号码" />
					</View>
					<View style={styles.row}>
						<Text style={styles.label}>姓名</Text>
						<TextInput
							ref="name"
							returnKeyType="done"
							onChangeText={(name)=>this.setState({name})}
							style={styles.input}
							editable={!this.state.submiting}
							defaultValue={data.name}
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
							returnKeyType="done"
							ref="phone"
							onChangeText={(phone)=>this.setState({phone})}
							defaultValue={data.phone}
							editable={!this.state.submiting}
							style={styles.input}
							placeholder="请输入手机号码" />
					</View>
					<View style={styles.row}>
						<Text style={styles.label}>生日</Text>
						<TextInput
							returnKeyType="done"
							ref="birth"
							defaultValue={data.birth}
							style={styles.input}
							placeholder="请输入生日"
							editable={false} />
					</View>
					<Select
						ref="areaCode"
						onChange={(index,value,label)=>{
							this.setState({area:label,areaCode:value})
						}}
						disabled={this.state.submiting}
						defaultValue={this.state.areaCode}
						values={areaValues}
						labels={areaLabels}
						placeholder="请选择地区"
						style={styles.row}
						>
						<Text style={styles.label}>地区</Text>
						<TextInput
							value={this.state.area}
							editable={false}
							style={styles.input}
							placeholder="请选择地区"/>
						<Image
							style={styles.arrow}
							source={picArrow} />
					</Select>

					{isStudentCard(data) && <TouchableOpacity onPress={()=>{this.setState({showSchool:true})}} style={styles.row}>
						<Text style={styles.label}>学校</Text>
						<Text
							ref="schoolName"
							style={this.state.schoolName ? styles.inputText : [styles.inputText,styles.inputTextPlaceholder]}
							placeholder="请选择学校">
						{this.state.schoolName || "请选择学校"}
						</Text>
						<Image
							style={styles.arrow}
							source={picArrow} />
					</TouchableOpacity>}
					{!isOldCard(data) && <View style={styles.row}>
						<Text style={styles.label}>{getDiscardType(this.state)}</Text>
						<TextInput
							returnKeyType="done"
							ref="navCode"
							editable={!this.state.submiting}
							onChangeText={(navCode)=>this.setState({navCode})}
							defaultValue={this.state.navCode}
							style={styles.input}
							placeholder={getDiscardCardPlaceHolder(this.state)} />
					</View>}
					<View style={styles.picContainer}>
						<Text style={[styles.label,styles.picLabel]}>身份证原件图片</Text>
						<ImagePicker
							ref="img1"
							disabled={this.state.submiting}
							placeholder="请选择照片"
							file={true}
							onChange={(img1)=>this.setState({img1})}
							album={false}
							style={{marginBottom:10}}
              width={2000}
              height={2000}>
							<Image
								resizeMode="contain"
								style={{alignSelf:'center',width:300,height:100}}
								source={this.state.img1 ?  {uri:this.state.img1} : picBg}
								/>
						</ImagePicker>
					</View>
					<View style={styles.picContainer}>
						<Text style={[styles.label,styles.picLabel]}>{getImgTip(this.state)}</Text>
						<ImagePicker
							file={true}
							disabled={this.state.submiting}
							ref="img2"
							placeholder="请选择照片"
							album={false}
							onChange={(img2)=>this.setState({img2})}
							style={{marginBottom:10}}
              width={2000}
              height={2000}
							>
							<Image
								resizeMode="contain"
								style={{alignSelf:'center',width:300,height:100}}
								source={this.state.img2 ?  {uri:this.state.img2} : picBg}
								/>
						</ImagePicker>
					</View>
					<View style={styles.picContainer}>
						<Text style={[styles.label,styles.picLabel]}>头像(一寸照片)</Text>
						<ImagePicker
							ref="img3"
							disabled={this.state.submiting}
							file={true}
							placeholder="请选择照片"
							editWidth={530}
							editHeight={636}
							onChange={(img3)=>this.setState({img3})}
							album={false}
							style={{marginBottom:10}}>
							<Image
								resizeMode="contain"
								style={{alignSelf:'center',width:300,height:100}}
								source={this.state.img3 ?  {uri:this.state.img3} : picBg}
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
				<SchoolSelect onChange={this.onChangeSchool} show={this.state.showSchool} onCancel={this.onCancel} />
			</View>
		);
	}
}


const styles = StyleSheet.create({
	arrow:{width:7,height:12,marginRight:5,marginLeft:5},
	scrollView:{flex:1,backgroundColor:'#F2F2F2'},
	row:{backgroundColor:'#FFFFFF',marginTop:5,height:36,flexDirection:'row',alignItems:'center'},
	inputText:{fontSize:14,flex:1,alignSelf:'center',marginLeft:10},
	inputTextPlaceholder:{color:"#ccc"},
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
