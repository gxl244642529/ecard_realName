

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
  LocalData,
  Log,
  Form
} from '../../lib/Common';

import {
  TitleBar,
} from '../Global'

import AdvView from '../widget/AdvView'

import ButtonGroup from '../widget/ButtonGroup'
import LoadingButton from '../widget/LoadingButton'

import {loadingButton} from '../GlobalStyle'
import {arrayFind} from '../lib/ArrayUtil'

import {getDiscardCardName,isOldCard,getDiscardCardPlaceHolder,getDiscardType,TYPE_OLD,TYPE_WORK,TYPE_HERO,isStudentCard} from '../discard/DiscardUtil'

import Select from '../../lib/Select'

const labels = ["学生卡","敬老卡","劳模卡","烈属卡"];
const values = [124000,125000,126000,127000];

const FORM_KEY = "discard_query";

export default class DiscardQuery extends Component{

	constructor(props) {
		super(props);
	    this.state={type:0,idCard:null}
	    LocalData.getObject(FORM_KEY).then((result)=>{
	    	Log.info(result);
	    	this.setState(result);
	    },()=>{

	    });
	    this.form = new Form();
	}

	_onChange=(index,value)=>{
		this.setState({type:value});
	}

	_info=(data)=>{
		Api.discard=data;
		Api.push("/discard/info");
	}

	_buyCard=(data)=>{
		Api.push("/discard/buy/"+JSON.stringify(data));
	}

	_onQuerySuccess=(data)=>{
		switch(data.status){
		case 0:
			this._info(data);
			break;
		case 1:
			A.alert("正在审核中，请耐心等待");
			break;
		case 2:
			A.confirm("审核已经通过，您可以在线购卡",(index)=>{
				if(index==0){
					this._buyCard(data);
				}
			});
			break;
		case 3:
			A.confirm("审核失败，原因为:"
				+data.comment
				+",是否重新提交资料？",(index)=>{
					if(index==0){
						this._info(data);
					}
				});
			break;
		case 4:
			A.alert("本预约已经付款,我们会在7-10个工作日内制卡，请耐心等待发货");
			break;
		case 5:
			A.alert("本预约已经完成，请等待收货");
			break;
		case 6:
			A.alert("预约失败");
			break;
		case 7:
			// A.confirm({
			// 	message:"您已经办理过"+getDiscardCardName(data)+"，请确认是否补办一张新卡",
			// 	ok:"补办卡",
			// 	cancel:"不用了"
			// },(index)=>{
			// 		if(index==0){
			// 			this._info(data);
			// 		}
			// 	});
      A.confirm({
      	message:"您已经办理过"+getDiscardCardName(data)+"，补卡后旧卡无法挂失，请确认是否补办新卡。",
      	ok:"补办卡",
      	cancel:"不用了"
      },(index)=>{
      		if(index==0){
      			this._info(data);
      		}
      	});
			break;
		}

	}

	_query=()=>{
		LocalData.putObject(FORM_KEY,this.state);
		this.form.submit(this,{
			api:"book/query",
			success:this._onQuerySuccess
		});
	}

	render(){
		let label = this.state.type>0 ?
							labels[arrayFind(values,this.state.type)] :
							"请选择查询类型";


		return (
			<View style={styles.container}>
				<AdvView style={styles.adv} id="3">

				</AdvView>
				<View style={styles.query}>
					<TextInput
						ref="idCard"
						placeholder="请输入身份证号"
						style={styles.ecardText}
						editable={!this.state.submiting}
						defaultValue={this.state.idCard}
						onChangeText={(text)=>{this.setState({idCard:text})}}
						/>
					<Select
						ref="type"
						onChange={this._onChange}
						title="请选择查询类型"
						disabled={this.state.submiting}
						labels={labels}
						values={values}
						placeholder="请选择查询类型"
						defaultValue={this.state.type}
						style={{marginTop:7,
							justifyContent:'space-between',
							alignItems:'center',
							flexDirection:'row',
							backgroundColor:'#FFFFFF',padding:5,height:35}}>
						<Text
							style={{color:'#333333',fontSize:15}}>
							{label}
						</Text>
						<Image
							style={{width:7,height:12}}
							source={require('../images/_ic_arrow.png')}
							/>
					</Select>
					<LoadingButton text="查询"
						onPress={this._query}
						loading={this.state.submiting}
						disabled={this.state.submiting}
						style={this.state.submiting ? [loadingButton.loadingButton,loadingButton.loadingButtonDisabled] : loadingButton.loadingButton}
						textStyle={loadingButton.loadingButtonText} />
				</View>
			</View>

		);
	}

}



const styles = StyleSheet.create({
	adv:{height:Dimensions.get('window').width / 4},
	container:{flex:1},
	query:{padding:10},
	ecardText:{backgroundColor:'#FFFFFF',padding:5,height:35,fontSize:15},

});

/*
<TouchableOpacity style={styles.leftButtonSelected}>
		            		<Text style={styles.textSelected}>优惠卡</Text>
		            	</TouchableOpacity>
*/
