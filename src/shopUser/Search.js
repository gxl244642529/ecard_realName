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
		A,
		AsyncStorage
		} from '../../lib/Common'


import {FormInput,FormSelect,FormSwitch} from '../../lib/TemplateForm'

const SCREEN_WIDTH = Dimensions.get('window').width;

export default class Search extends Component{

	constructor(props) {
   		super(props);
			this.state={skey:" "}
	}
	componentDidMount(){
		Api.api({
			api:'bshop/hot',
			success:(result)=>{
				this.setState({hotList:result})
			}
		});
		this._getHistory();
		this._setStyle();
	}
	_setStyle=()=>{
		var Platform = require('Platform');
		if (Platform.OS === 'android') {
			this.setState({paddingtop:0});
		}else{
			this.setState({paddingtop:30});
		}
	}
	_onBack=()=>{

		Api.goBack();
	}
	//根据热门条目搜索
	_onSearchByHot=(data)=>{
		var str=JSON.stringify(data);
		var newStr = str.substring(str.indexOf("\"")+1,str.lastIndexOf("\""));
		let kdata = {key:newStr,type:-1};
		Api.push('/shopUser/searchResult/'+JSON.stringify(kdata));

	}
	//搜索
	_searchResult=()=>{
		if(!this.state.skey || this.state.skey==" "){
			A.alert("请输入搜索内容",function(){
				return;
			})
			return;
		}
		this._setAsyncStorage(this.state.skey);
		let data = {key:this.state.skey,type:-1};
	  Api.push('/shopUser/searchResult/'+JSON.stringify(data));
		// Api.push("/shopUser/searchResult/"+null+"/"+this.state.skey+"/"+null+"/"+null+"/"+null);
	}
	//将搜索内容保存至本地历史记录
	_setAsyncStorage=(value)=>{
		//获得搜索历史
		AsyncStorage.getItem('searchHistory',function(errs,result){
			if(!errs){
				var value_str=result;
				var value_json;
				//console.log(result);
				if(result!=null){
					value_json=JSON.parse(value_str);
				}else{
					value_json=[];
				}
				value_json.push(value);
				var obj=JSON.stringify(value_json);
				AsyncStorage.setItem('searchHistory',obj,function(errs){
					//TODO:错误处理
					if (errs) {
					console.log('存储错误');
					}
					if (!errs) {
					console.log('存储无误');
					}
				});
			}
		});

		//var value_str=JSON.stringify(value_arr);

	}
	//提取本地搜索历史
	_getHistory=()=>{
		let self=this;
		AsyncStorage.getItem('searchHistory',function(errs,result){
			//TODO:错误处理
			if (!errs) {
				console.log("历史搜索记录："+result);
				if(result!=null){
					var length=result.length;
					var arr=result.substring(2,length-2);
					//console.log(arr);
					var newArr=arr.split("\",\"");
					self.setState({history:newArr});
				}else{
					self.setState({history:null});
				}
			}
		});
	}
	//清空历史记录
	_cleanHistory=()=>{
		let self=this;
		A.confirm("确认删除全部历史记录？",(index)=>{
			if(index==0){
				AsyncStorage.removeItem('searchHistory',function(errs) {
					if (!errs) {
					console.log('移除成功');
					self.setState({history:null});
					}
          		});
		  }
		})

	}
	render(){
    	return(
	    		<ScrollView style={CommonStyle.container}>
    			<View style={ [styles.top,{paddingTop:this.state.paddingtop}]}>
    				<TouchableOpacity onPress={this._onBack} style={{height:45,width:40,justifyContent:'center',alignItems:'center'}}>
							<Image style={{ width:9,height:19}}
										 source={require('../images/back.png')}
										 />
						</TouchableOpacity>

    				<TextInput
							style={styles.TextInput}
							placeholder="请输入商家名称"
							onChangeText={(text)=>{this.setState({skey:text});console.log(this.state.skey);}}
							underlineColorAndroid='transparent' />

    				<TouchableOpacity style={styles.hisSearch} onPress={this._searchResult}>
    					<Image style={{  width:15, height:18, marginRight:5, }} source={require('./images/hisSearch.png')} />
    					<Text style={{ color:'#f39800', paddingRight:10, }}>搜索</Text>
    				</TouchableOpacity>
    			</View>

    			<View style={styles.main}>
    				<Text style={styles.Inquire}>热门搜索</Text>
    				<View style={styles.hotContent}>
						{
							this.state.hotList && this.state.hotList.map((data,index)=>{
								return <TouchableOpacity  key={"hot"+index} onPress={
									()=>this._onSearchByHot(data)
								} style={styles.historyTap}
								>
									<Text>{data}</Text>
								</TouchableOpacity>
							})
						}
    				</View>
    			</View>
				<View style={styles.historyTip}>
    				<Text style={{ color:'#eb614b'}}>搜索历史</Text>
					<TouchableOpacity onPress={this._cleanHistory}><Text>清空</Text></TouchableOpacity>
				</View>
				<View style={styles.historyContent}>
				{
					this.state.history && this.state.history.map((data,index)=>{
						let sdata = {key:data,type:-1};
						return <TouchableOpacity onPress={()=>{
							//根据历史记录搜索
							Api.push('/shopUser/searchResult/'+JSON.stringify(sdata));
						}}  key={"lishi"+index} style={styles.historyTap}>
							<Text style={styles.historyText}>{data}</Text>
						</TouchableOpacity>
					})
				}
				</View>

			</ScrollView>
    	)
  	}
}

const styles = StyleSheet.create({
	container:{
		backgroundColor:"#F0F0F0",
		flex:1,
	},
	top:{
		// paddingTop:30,
		alignItems:'center',
		justifyContent:'space-between',
		flexDirection:'row',
		height:65,
		backgroundColor:'#FEFEFE'
	},
	TextInput:{
		flex:0.7,
		padding:5,
		height:35,
		backgroundColor:'#fff',
		// color:'#736451',
		borderWidth:1,
		borderColor:'#9F9FA0',
		borderRadius:5,
		alignSelf:'center'

	},

	hisSearch:{
		flexDirection:'row',
		marginLeft:3

	},

	main:{
		padding:20,
		paddingBottom:5,
		backgroundColor:'#fefefe',
	},
	Inquire:{
		color:'#43B0B1',
	},
	hotContent:{
		flexDirection:'row',
		marginTop:10,
		//justifyContent:'space-around',
		flexWrap:'wrap',
		// height:40,
		//marginBottom:10,
	},
	historyTip:{
		flexDirection:'row',
		justifyContent:'space-between',
		paddingLeft:20,
		paddingRight:20,
		paddingTop:15
	},
	historyContent:{
		//width:SCREEN_WIDTH-20,
		//backgroundColor:'red',
		marginLeft:20,
		marginRight:20,
		marginTop:15,
		flexDirection:'row',
		flexWrap:'wrap',
		//justifyContent:'space-between',
		//height:60
	},
	historyTap:{
		padding:6,
		borderColor:'#ddd',
		borderWidth:1,
		borderRadius:2,
		height:35,
		//width:40,
		marginRight:10,
		marginBottom:7,
	},
	historyText:{
		color:'#804e21',



	}
});
