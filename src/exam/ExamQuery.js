

import React, { Component } from 'react';
import {
  StyleSheet,
  Text,
  TextInput,
  View,
  Image,
  TouchableOpacity,
  TouchableHighlight,
  Api,
  A,
  CommonStyle,
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


import ECardSelector from '../lib/ECardSelector'

import {loadingButton} from '../GlobalStyle'

const FORM_KEY = "exam_query";
export default class ExamQuery extends Component{

	constructor(props) {
		super(props);
	    this.state={cardId:null,loading:false}
	     LocalData.getObject(FORM_KEY).then((result)=>{
	    	Log.info(result);
	    	this.setState({cardId:result.cardId});
	    },()=>{
	    	
	    });
	       this.form = new Form();
	}
	_onSelectECard=(cardId)=>{
		 this.setState({cardId:cardId});
	}
	_onQuerySuccess=(data)=>{
		LocalData.putObject(FORM_KEY,{cardId:this.state.cardId});
		this._onData(data);
	}
	_query=()=>{
		this.form.submit(this,{
			api:"exam/query",
			success:this._onQuerySuccess
		});
	}

	_info=(data)=>{
		Api.data = data;
		Api.push("/exam/info");
	}

	_onData(info){
		switch (info.status) {
		case 0:
			A.alert("本卡已经审核完成,请线下补登",()=>{

			});
			break;
		case 1:
			A.alert("本卡年审已经完成");
			break;
		case 2:
			A.toast("查询成功");
			this._info(info);
			break;
		case 3:
			A.alert("本卡资料已经提交，正在进行人工审核，请耐心等待");
			break;
		case 4:
			A.alert("审核没有通过，请重新提交资料",this._info(info));
			break;
		case 5:
			A.alert( "年审失败，请去e通卡营业网点办理年审");
			break;
		case 9:
			A.toast("查询成功");
			this._info(info);
			break;
		default:
			break;
		}
	}

	render(){
		return (
			<View style={styles.container}>
				<AdvView style={styles.adv} id="3">

				</AdvView>
				<View style={{padding:10}}>

					<View
						style={styles.row}>
						<TextInput 
							onChangeText={this._onSelectECard}
							editable={!this.state.submiting}
							style={styles.ecardText} 
							placeholder="请选择或输入e通卡" 
							value={this.state.cardId}></TextInput>
						<ECardSelector 
							ref="cardId"
							placeholder="选择或输入e通卡"
							disabled={this.state.submiting}
							onSelectECard={this._onSelectECard}>
							<Image source={require('../images/ic_sel_ecard.png')} />
						</ECardSelector>
					</View>
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
	container:{flex:1},
	row:{
		justifyContent:'space-between',
		alignItems:'center',
		flexDirection:'row',
		backgroundColor:'#FFFFFF',
		height:35
	},
	ecardText:{color:'#333333',fontSize:15,height:35,flex:1,padding:5},

});

/*
<TouchableOpacity style={styles.leftButtonSelected}>
		            		<Text style={styles.textSelected}>优惠卡</Text>
		            	</TouchableOpacity>
*/

