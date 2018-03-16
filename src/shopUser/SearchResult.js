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
		Picker,
		A
} from '../../lib/Common'


const SCREEN_WIDTH = Dimensions.get('window').width;
const SCREEN_HEIGHT= Dimensions.get('window').height;

const TYPE_ARR=["全部",'衣', '食',"住","行","购","娱"];
const DISTANCE_ARR=['全城', '1千米以内','2千米以内','3千米以内'];
const DISTANCE_VALUE=[null,1000,2000,3000];

import {FormInput,FormSelect,FormSwitch} from '../../lib/TemplateForm'
import TitleBar from '../widget/TitleBar'
import StateListView from '../widget/StateListView'
import Star from '../widget/Star'
import ModalDropdown from '../widget/ModalDropdown';
import MapUtil from '../lib/MapUtil'
import {renderEmptyUtil} from './ShopUserUtils'


import DataUtil from '../lib/DataUtil'
import TopMenu from '../widget/TopMenu'


export default class SearchResult extends Component{

	constructor(props) {
   		super(props);

   		let data=JSON.parse(props.params.json);
   		let type = data.type;
   		let distanceIndex = 0;
   		let isArround = true;
   		if(type < 0){
   			data.type = 0;
   			distance = 3000;
   			distanceIndex = 3;
   		}else{
   			distance = null;
   			distanceIndex = 0;
   			isArround = false;
   		}
   		data.distance = distance;
   		let config = [
			  {
			    type:'title',
			    selectedIndex:data.type,
			    data:[{
			      title:"全部"
			    }, {
			      title:'衣'
			    }, {
			      title:'食'
			    }, {
			      title:"住"
			    }, {
			      title:"行"
			    }, {
			      title:"购"
			    }, {
			      title:"娱"
			    }]
			  },
			  {
			    type:'title',
			    selectedIndex:distanceIndex,
			    data:[{
			      title:'全城'
			    }, {
			      title:'1千米以内'
			    }, {
			      title:'2千米以内'
			    }, {
			      title:'3千米以内'
			    }]
			  }
			];

		this.state={data,config,isArround};
		console.log(this.state);
		if(isArround){
			MapUtil.getPos(this.onGetPos);
		}

	}

	onGetPos=(data)=>{

		this.setState( {data:Object.assign({},this.state.data,{lat:data.lat.toString(),lng:data.lng.toString()})}  );
	}

	componentDidMount(){
		
	}
	_onBack=()=>{
		//从搜索结果页返回，默认返回首页
		Api.push('/shopUser/shopUserMain');
	}
	_onPress=()=>{
		Api.push("/shopUser/search");
	}

	_renderRight=()=>{
		return(
			<TouchableOpacity style={styles.titleTou} onPress={this._onPress}>
				<Image source={require('./images/search.png')} style={{width:18,height:20}} onPress={this._opress}/>
			</TouchableOpacity>
		);
	}

	_renderRow=(data)=>{
		let typeArr=[" ","服装","美食","住宿","出行","购物","娱乐"];
		let type=parseInt(this.props.params.type);
		return <TouchableOpacity onPress={()=>{Api.push('/shopUser/shopDetail/'+data.id)}}>
			<View style={ styles.bottomMain}>
				<View>
					<Image style={styles.image}  source={{uri:data.thumb}} />
				</View>
				<View style={styles.bottomCeng}>
					<Text numberOfLines={1} style={{width:SCREEN_WIDTH*0.6}}>商户名：{data.title}</Text>
					<Text>商户类型：{typeArr[data.type]}</Text>
					<View style={{ flexDirection:'row',}}>
						<Text>评分:　</Text>
						<Star num={data.score}/>
					</View>
				</View>
		</View></TouchableOpacity>

	}

 onSelectMenu=(index, subindex, data)=>{
 	if(index==0){
 		this.setState({data:Object.assign({},this.state.data,{type:subindex})});
 	}else{
 		if(this.state.data.lat){

 			this.setState( {data:Object.assign({},this.state.data,{ distance:DISTANCE_VALUE[subindex] })}  );

 		}else{
 			MapUtil.getPos(this.onGetPos);
 		}
 	}
  //  this.setState({key:"",distance:});
  }
	renderContent=()=>{

	    return (
	      <StateListView
				cancelLoad={this.state.isArround}
				api="bshop/list"
				style={{flex:1,backgroundColor:'#f0f0f0'}}
				paged ={true}
				pageSize={10}
				data={this.state.data}
				renderRow={this._renderRow}
				renderEmpty={renderEmptyUtil}
			/>
	    );
	  }

	render(){
    	return(
    		<View style={CommonStyle.container} ref="MAIN">
		        <TitleBar title="联盟商家" />
		        <TopMenu config={this.state.config} onSelectMenu={this.onSelectMenu} renderContent={this.renderContent} />
		    </View>
    	)
  	}
}

const styles = StyleSheet.create({
	container:{
		backgroundColor:"#F0F0F0",
		flex:1,
	},
	top:{
		backgroundColor:'#fffdea',
		padding:15,
		alignItems:'center',
		justifyContent:'space-between',
		flexDirection:'row',
	},

	inputC:{
		marginLeft:40,
		marginRight:40,
		justifyContent:'space-between',
		flexDirection:'row',
		alignItems:'center',
		marginTop:15,
		backgroundColor:'#fff',
		borderRadius:5,

	},

	TextInput:{
		flex:0.9,
		height:40,
		marginLeft:10,
		// color:'#736451',

	},
	menuContent:{
		flexDirection:'row',
		//marginTop:20,
		justifyContent:'space-between',
	},
	/*mainOne:{
		flex:1,
		height:30,
		backgroundColor:'#dcdcdd',
		justifyContent:'center',
		alignItems:'center',

	},*/
	mainTwo:{
		flex:1,
		height:30,
		backgroundColor:'#3fb0b1',
		justifyContent:'center',
		alignItems:'center',
	},

	bottomMain:{
		flex:1,
		padding:10,
		borderBottomWidth:1,
		borderColor:'#ddd',
		flexDirection:'row',
	},

	bottomCeng:{
		marginLeft:10,
		justifyContent:'space-between',
		// color:'#6a3906',
	},
	image:{
		width:80,
		height:80,
	},
	titleTou:{
		flexDirection:'row',
		height:45,
		width:45,
		justifyContent:'center',
		alignItems:'center',
	},
	menu:{
		flex:0.5,
		backgroundColor:'#3fb0b1',
		height:30,
		justifyContent:'center',
		alignItems:'center',
		color:'#fff'
	},
	dropdown:{
		borderColor:'#f0f0f0',
		borderWidth:1,
		backgroundColor:'#3fb0b1',
		flex:0.5
	}
});
