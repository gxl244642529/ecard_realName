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
		A
		} from '../../lib/Common'

import TitleBar from '../widget/TitleBar'
import StateListView from '../widget/StateListView'
const SCREEN_WIDTH = Dimensions.get('window').width;
import Notifier from '../../lib/Notifier'
import LoadingButton from '../widget/LoadingButton'
import {formatDate} from '../lib/StringUtil'
import {renderRow} from './realNameUtils'

import {
	radioButton,
	loadingButton,
	loadingButtonDisabled,
	container
} from '../GlobalStyle'



export default class RCardLostList extends Component{

	constructor(props) {
		super(props);
		this.state = {
		};
	}
	componentDidMount(){

	}

  _submit=()=>{
		A.confirm("一个APPID最多实名化5张易通卡，是否确认新增实名绑卡?",(index)=>{
			if(index==0){
				Api.push("/realName/rCardMessage");
			}else{
				return;
			}
		});
  }

	_renderRow=(rowData)=>{
		return <TouchableOpacity  style={{height:50,backgroundColor:'#f0f0f0',}} onPress={()=>{this._couponDetail(rowData)}}>
          <Text>{rowData.cardId} </Text>
					<Text>{rowData.cardIdExt} </Text>
					<Text>{formatDate(rowData.createDate)}</Text>

		</TouchableOpacity>
	}


	render(){


    	return(
    		<View style={[CommonStyle.container,container.container]}>
    			<TitleBar style={{ color:'#804e21'}} title="挂失记录" />
						<StateListView
						 ref="LIST"
						 api="rcard/his"
						 style={styles.container}
						 renderRow={(rowData,rowID)=> renderRow(rowData,rowID)}
						 />
			</View>
    	)
  	}
}

const styles = StyleSheet.create({
	container:{
		// backgroundColor:"#fef3cc",
		flex:1,
	},
  listStyle:{
    flex:1,
    marginBottom:10
  },
	container:{flex:1},
	listHasData:{flex:1},
	listEmpty:{height:0},

	MyCouponMain:{
		padding:15,
		backgroundColor:'#fff',
		marginTop:10,
	},

	MyCouponMain_one:{
		flexDirection:'row',

	},

	MyCouponMain_two:{
		flexDirection:'row',
		marginTop:5,

	},

	MyCouponMain_three:{
		flexDirection:'row',
		marginTop:5,
	},
	list:{
		paddingLeft:25,
		paddingTop:12,
		paddingBottom:15,
		paddingRight:15,
		marginLeft:10,
		justifyContent:'space-between',

		width:SCREEN_WIDTH-20,
		height:135,

		marginTop:20,
	},


});
