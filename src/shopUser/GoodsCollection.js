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
		A,
		CommonStyle
		} from '../../lib/Common'

import TitleBar from '../widget/TitleBar'
import Notifier from '../../lib/Notifier'

import StateListView from '../widget/StateListView'
const SCREEN_WIDTH = Dimensions.get('window').width;


export default class GoodsCollection extends Component{

	constructor(props) {
   		super(props);
			this.state={};

	}
	componentWillMount() {
    Notifier.addObserver("bgoods/col",this.refreshList);

  }

  componentWillUnmount() {
      Notifier.removeObserver("bgoods/col",this.refreshList);
  }

  refreshList=()=>{
    this.refs.LIST.reloadWithStatus();
  }
	_cancel=(rowData)=>{

		let data = {id:rowData.id,type:0};
		A.confirm("确定要删除该收藏商品吗?",(index)=>{
			if(index==0){
				Api.api({
							api:"bgoods/col",
							data:data,
							success:(result)=>{
								A.toast("删除收藏成功");
							}
						})
			}
		});


	}
	_itemClick=(rowData)=>{
		Api.push("shopUser/goodsDetail/"+rowData.id);
	}


	renderGoodsItem=(rowData)=>{
		return(
			<TouchableOpacity style={styles.main} onPress={()=>{this._itemClick(rowData)}}>
					<Image style={styles.thumbStyle}source={{uri:rowData.thumb}} />

				<View style={styles.mainRight}>
					<View style={styles.mainRightOne}>
							<Text numberOfLines={1} style={{ fontSize:12,width:SCREEN_WIDTH*0.5 }}>	{rowData.title}	</Text>

					</View>



					<View style={styles.mainRightOne}>
								<Text numberOfLines={1} style={{ fontSize:12, marginLeft:3,marginTop:10,width:SCREEN_WIDTH*0.5}}>{rowData.desc}</Text>
								<TouchableOpacity onPress={()=>{this._cancel(rowData) }}style={{width:40,height:40,justifyContent:'center',alignItems:'center'}}>
									<Image style={{ width:16, height:16, }}
										source={require('./images/delete.png')} />
								</TouchableOpacity>
					</View>
					<View style={styles.mainRightThree}>

								<Text style={{ color:'#F90303', }}>￥{rowData.disprice}</Text>
							{rowData.price && <Text style={{textDecorationLine:'line-through',color:'#9f9fa0',marginLeft:8}}>￥{rowData.price}</Text>}
					</View>
				</View>
		</TouchableOpacity>
		);
	}

	render(){
    	return(
				<View style={styles.container}>
							<StateListView
								ref="LIST"
								api="bgoods/cols"
								style={{flex:1}}
								paged ={true}
								pageSize={10}
								renderRow={this.renderGoodsItem}
							/>
			 	</View>


    	)
  	}
}

const styles = StyleSheet.create({
	container:{
		backgroundColor:"#f0f0f0",
		flex:1,
	},

	main:{
		padding:10,
		backgroundColor:'#fff',
		flexDirection:'row',
		marginTop:8,
	},

	mainRight:{
		marginLeft:10,
		justifyContent:'space-between',
		flex:1,
	},

	mainRightOne:{
		justifyContent:'space-between',
		flexDirection:'row',
	},

	mainRightTwo:{
		flexDirection:'row',
		height:20,
	},

	mainRightThree:{
		flexDirection:'row',
		height:20,
		// fontSize:12,
		// justifyContent:'space-between',
	},
	thumbStyle:{
	 width:80,
	 height:80,
	//  backgroundColor:'#f0f',
	 borderRadius:5,
	},
});
