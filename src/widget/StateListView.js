

import React, { Component } from 'react';
import {
  AppRegistry,
  StyleSheet,
  Text,
  View,
  RefreshControl,
  ListView,
  TouchableOpacity,
  Api,
  ActivityIndicator,
  Log,
  Platform,
  Image,
  A
} from '../../lib/Common';

import {InteractionManager} from 'react-native'
import {EMPTY_DATA_SOURCE} from '../lib/ArrayUtil'


export const refreshColors = ['#ff0000','#00ff00','#0000ff','#3ad564'];

export const renderEmpty=(component)=>{
	return (
		<TouchableOpacity style={styles.centering}
			onPress={()=>{component.reloadWithStatus();}}>
			<Text style={{fontSize:14}}>没有搜索结果</Text>
			<Text style={{fontSize:12,marginTop:5}}>点击重试</Text>
		</TouchableOpacity>
	);
}

export const renderError=(error,isNetworkError,component)=>{
	if(isNetworkError){
		return (
			<TouchableOpacity style={styles.centering}
				onPress={()=>{component.reloadWithStatus();}}>
				<Image source={require('./images/ic_network_error.png')} resizeMode="contain" style={{width:100,height:100}} />
				<Text style={{fontSize:14,marginTop:5}}>数据获取失败</Text>
				<Text style={{fontSize:12,marginTop:5}}>请检查网络后点击重新加载</Text>
			</TouchableOpacity>
		);
	}else{
		return (
			<TouchableOpacity style={styles.centering}
				onPress={()=>{component.reloadWithStatus();}}>
				<Image source={require('./images/ic_load_error.png')}
					resizeMode="contain" style={{width:100,height:100}} />
				<Text style={{fontSize:14,marginTop:5}}>服务器开了个小差</Text>
				<Text style={{fontSize:12,marginTop:5}}>点击这里重试</Text>
			</TouchableOpacity>
		);
	}

}

export const renderLoading=()=>{
	return (
		<View style={styles.centering}>
			<ActivityIndicator
		        animating={true}
		        style={styles.loading}
		    />
		</View>
	);
}

const onEndReachedThreshold = 10;


const PAGE_INIT_TIME = 500;
/**

状态：

loadingState：表示加载状态


isRefresh: 表示下拉刷新
dataSource: 表示是否有数据，在有数据的情况下才绘制ListView
isLast: 表示是否是分页的最后一页，只有分页的才会有这个属性


*/
/**

trigger:

[
	{
	name:'xxapi/xxadd',
	type:'add'/'update'/'delete',
	id: 'id'			//对于delete 和 update是必须的
	}
]
*/
/*
const INIT_STATE = {
	loading:false,
	list:null,
	dataSource:null,
	error:null,		//与list互斥,悠闲判断
	isNetworkError:false
 };*/

class LoadMoreFooter extends Component {
      constructor(props) {
          super(props);
      }
      render() {
          return (
              <View style={styles.footer}>
                  <Text style={styles.footerTitle}>{this.props.hasLoadAll ? '已加载全部' : '正在加载更多……'}</Text>
              </View>
          )
      }
  }



export default class StateListView extends Component{

	static propTypes={
		cancelLoad:React.PropTypes.bool, //是否在开始的时候加载
		renderEmpty:React.PropTypes.func,
		reserveProps:React.PropTypes.bool,	//是否保留原来的数据
		paged: React.PropTypes.bool,      		   //是否分页
		api: React.PropTypes.string.isRequired,       //使用哪个分页
		data: React.PropTypes.object,				   //提交数据
		renderRow:  React.PropTypes.func,              //

		/**
		 * 状态改变的时候调用
		 *  //状态更新   loading状态,complete状态,error状态 格式: { list:数据,page: loading: }
		 * @type {[type]}
		 */
		onChange: React.PropTypes.func,
		trigger: React.PropTypes.array,				//触发的api
		triggerRefresh: React.PropTypes.bool,//触发是否刷新
	}

	constructor(props) {
	  super(props);
	  let state = {
	  	isRefreshing :false,
	  	loading:false,
		list:null,
		dataSource:EMPTY_DATA_SOURCE,
		error:null,		//与list互斥,悠闲判断
		isNetworkError:false,
	  };
	  this.state = state;
	  this.isLoadingMore = false;

	}

	/**
	 * 更新数据
	 * @param  {[type]} data [description]
	 * @return {[type]}      [description]
	 */
	updateData(data){
		if(!this.state.list){
			return;
		}

		this.setState(loadComplete(this.state.list));

	}

	componentWillReceiveProps(nextProps) {
		if(nextProps.data!==this.props.data){
		//	console.log("data changed,refresh list")
			this.loadData(true,true,nextProps.data);
		}
	}



	componentDidMount() {
		if(this.props.cancelLoad===true){
			return;
		}
		this.loadData(true,true);
	}

	_parseData=(result)=>{
		InteractionManager.runAfterInteractions(()=>{
			this._onReceiveData(result);
		});
	}

	_onReceivePage=(result)=>{
		this.isLoadingMore = false;
		//console.log('=====收到消息',result)
		let list = this.state.list;
		if(result.isFirst()){
			list = result.list;
		}else{
			list=list.concat(result.list);
		}
		this.setState(loadPageComplete(list,result.isLast()));
		this.props.onChange && this.props.onChange({loading:false,list:list,page:result});
	}


	_renderFooter=()=>{
          //通过当前product数量和刷新状态（是否正在下拉刷新）来判断footer的显示
          if (this.state.loading || !this.state.list || this.state.list.length < 1) {
              return null
          };
         // console.log(this.state);
          if (!this.state.isLast) {
              //还有更多，默认显示‘正在加载更多...’
              return <LoadMoreFooter />
          }else{
              // 加载全部
              return <LoadMoreFooter hasLoadAll={true}/>
          }
      }

	_onReceiveData=(result)=>{
		this.isLoadingMore = false;
		//console.log('=====收到消息',result)
		if(this.props.paged){
			this._onReceivePage(result);
		}else{
			this.setState(loadComplete(result));
			this.props.onChange && this.props.onChange({loading:false,list:result});
		}
	}

	_onError=(error,isNetworkError)=>{
		//console.log('====================',error);
		this.setState({loading:false,error:error,isNetworkError:isNetworkError})
		return true;
	}

	_onRefresh=()=>{
		this.reloadData(false,true);
	}

	reloadData=()=>{
		this.loadData(true);
	}

	reloadWithStatus=()=>{
		this.loadData(true,true);
	}
  _message=(error)=>{

    if (this.props._message) {
      this.props._message&&this.props._message(error);
      return true;
    }
  }

	loadData=(reload=false,setLoadingState=false,initData=null)=>{



		//console.log('Begin load data in StateListView')
		let api = {
			api:this.props.api,
			error:this._onError,
      message:this._message,
		};
		if(Platform.OS=='web'){
			api.success = this._onReceiveData;
		}else{
			api.success=this._parseData;
		}
		let data = initData ? {...initData} : {...this.props.data};
		//console.log('========data:',data);
		if(this.props.paged){
			api.type = 1;
			data.position = reload ? 0 : (this.state.list ? this.state.list.length : 0) ;
			data.pageSize = this.props.pageSize || 0;
		}
		api.data = data;
		if(setLoadingState){
			this.setState(loadStart());
		}

		Api.api(api);
	}

	_renderState=()=>{
		let extra = null;
		if(this.state.loading){
			//loading
			let loadingCallback = this.props.renderLoading || renderLoading;
			extra = loadingCallback();

		}else if(this.state.error){
			//error
			let renderErrorCallback = this.props.renderError || renderError;
      		this.props.sendError && this.props.sendError();
			extra = renderErrorCallback(this.state.error,this.state.isNetworkError,this);

		}else if( this.state.list && this.state.list.length == 0 ){
			//empty
			let renderEmptyCallback = this.props.renderEmpty || renderEmpty;
			extra = renderEmptyCallback(this);
		}
		return extra;
	}

	_renderHeader=()=>{
		let extra = this._renderState();
		return (
			<View>{this.props.renderHeader()}{extra}</View>
		);
	}


	_loadMore=()=>{
		if(this.isLoadingMore || this.state.loading){
			return;
		}
	 	InteractionManager.runAfterInteractions(() => {
          this.isLoadingMore = true;
          this.loadData(false,false);
      });
	}


	render(){
		let props = (this.props.paged && !this.state.isLast) ?
		{
			onEndReached:this._loadMore,
			onEndReachedThreshold: this.props.onEndReachedThreshold || onEndReachedThreshold
		} : null;

		if(this.props.paged){
			if(!props){
				props = {};
			}
			props.renderFooter=this._renderFooter;
		}

		if(this.props.renderHeader){
			//有头需要始终绘制ListView
			if(!props){
				props = {};
			}
			//console.log(props);
			props.renderHeader = this._renderHeader;
			return (
				<ListView
					{...props}
					style={styles.list}
					enableEmptySections={true}
			      	dataSource={this.state.dataSource}
			      	refreshControl={
			          <RefreshControl
			           	refreshing={this.state.isRefreshing}
			           	onRefresh={this._onRefresh}
			           	colors={refreshColors}
			           	progressBackgroundColor="#ffffff"
			          />
			        }
			       renderSeparator={this.props.renderSeparator}
			      renderRow={this.props.renderRow}
			    />
			);
		}else{
			//无头，不需要始终绘制ListView
			let extra = this._renderState();
			return (
				<View style={this.props.style}>
				{extra || <ListView
					{...props}
					style={styles.list}
					enableEmptySections={true}
			      	dataSource={this.state.dataSource}

			      	refreshControl={
			          <RefreshControl
			           	refreshing={this.state.isRefreshing}
			           	onRefresh={this._onRefresh}
			           	colors={refreshColors}
			           	progressBackgroundColor="#ffffff"
			          />
			        }
			        renderSeparator={this.props.renderSeparator}
			      renderRow={this.props.renderRow}
			    />}
				</View>
			)
		}

	}
}



const styles = StyleSheet.create({
	retry:{fontSize:12,marginTop:5},


	list:{
		flex:1
	},
	centering: {
		flex:1,
		alignItems: 'center',
		justifyContent: 'center'
	},
	gray: {
		backgroundColor: '#cccccc',
	},
	horizontal: {
		flexDirection: 'row',
		justifyContent: 'space-around',
		padding: 8,
	},
	loading:{
		height: 30,
		width: 30
	},
	footer: {
          flexDirection: 'row',
          justifyContent: 'center',
          alignItems: 'center',
          height: 40,
      },
      footerTitle: {
          marginLeft: 10,
          fontSize: 15,
          color: 'gray'
      }
});



function loadStart(){
	return {loading:true,error:null,list:null,dataSource:EMPTY_DATA_SOURCE};
}

function loadComplete(list){
	return {loading:false,error:null,list:list,dataSource:list.length>0 ? EMPTY_DATA_SOURCE.cloneWithRows(list) : EMPTY_DATA_SOURCE};
}
function loadPageComplete(list,isLast){
	return {loading:false,error:null,list:list,isLast:isLast,dataSource:list.length>0 ? EMPTY_DATA_SOURCE.cloneWithRows(list) : EMPTY_DATA_SOURCE};
}
