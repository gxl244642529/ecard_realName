
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
  ScrollView,
  
} from '../../lib/Common';

import {
  TitleBar,
} from '../Global'
import WebUtil from '../../lib/WebUtil'
import {IC_SERVICE,IC_CON,IC_PURCH,IC_CHARGE,getIcon,NetpotStyles,formatDistance} from './NetpotUtils'
import StateListView from '../widget/StateListView'



class SearchItem extends Component{
  constructor(props){
    super(props);
  }

  onPress=()=>{
      //这里进入导航
      /**
       *   NSString* url = [NSString stringWithFormat:@"http://m.amap.com/navi/?start=%f,%f&dest=%@,%@&destName=%@&naviBy=car&key=7ffc0743945d569cc8501e757af68d25", _info.lng,_info.lat,
                     data[@"location"][0],data[@"location"][1], data[@"address"]  ];
    url = [self clean:url];
    DMWebViewController* web = [[DMWebViewController alloc]initWithTitle:@"路线规划" url:url];
    
    [self.navigationController pushViewController:web animated:YES];
  
       */
      
      let url = "http://m.amap.com/navi/?start="+Api.pos.lng+","+Api.pos.lat+"&dest="+this.props._location
        +"&destName="+this.props._name+"&naviBy=car&key=7ffc0743945d569cc8501e757af68d25";
      url = encodeURI(url);
      WebUtil.open(url,"");
  }

  render(){
    let props = this.props;
    return (
       <TouchableHighlight onPress={this.onPress} underlayColor="#efefef">
        <View style={NetpotStyles.netpot}>
          <Image source={getIcon(props.type)} style={NetpotStyles.netpotIcon} />
          <View style={NetpotStyles.netpotContainer}>
            <Text style={NetpotStyles.netpotMainTitle}>{props._name}</Text>
            <Text style={NetpotStyles.netpotSubTitle}>{props._address}</Text>
            <Text style={NetpotStyles.distance}>{formatDistance(props._distance)}</Text>
          </View>
          <Image style={NetpotStyles.arrow} source={require('../img/ic_arrow.png')} />
        </View>
      </TouchableHighlight>
    );

  }
}


const TYPE_VALUE=[-1,0,1,2,3,4];
const DISTANCE_VALUE=[50000,1000,2000,3000];

import TopMenu from '../widget/TopMenu'

/**
 * 属性为：
 *
 *  1、是否自动加载
 *  2、是否搜索关键字
 * 
 */
export default class SearchView extends Component{
	constructor(props){
		super(props);
    let data = props;

    let typeIndex = TYPE_VALUE.indexOf(data.type);

		let config = [
        {
          type:'title',
          selectedIndex:typeIndex,
          data:[{
            title:"全部"
          }, {
            title:'消费点'
          }, {
            title:'充值点'
          }, {
            title:"售卡点"
          }, {
            title:"服务网点"
          },{
            title:"补登点"
          }]
        },
        {
          type:'title',
          selectedIndex:2,
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
//this.setState({data:Object.assign({type:0,radius:3000},this.state.data,Api.pos)});
    
    data = Object.assign({radius:DISTANCE_VALUE[2]},data,Api.pos);

    this.state={config,data};
	}


  componentWillReceiveProps(nextProps) {
    let data = Object.assign({},this.state.data,{keyword:nextProps.keyword});
    this.setState({data});
  }


  _renderRow=(row)=>{

    return (
      <SearchItem {...row} />
    );

  }

   onSelectMenu=(index, subindex, data)=>{
    if(index==0){
      this.setState({data:Object.assign({},this.state.data,{type:TYPE_VALUE[subindex]})});
    }else{
      this.setState( {data:Object.assign({},this.state.data,{ radius:DISTANCE_VALUE[subindex] })}  );
    }
   
  }

  /**
   * shuju
   * @param  {[type]} data [description]
   * @return {[type]}      [description]
   */
  onChange=(data)=>{
    this.props.onSuccess && this.props.onSuccess(data);
  }


  renderSeparator=(row,index)=>{
    return <View key={"same"+index} style={NetpotStyles.line} />
  }
  
  renderContent=()=>{
    return (
        <StateListView
          cancelLoad={this.props.cancelLoad}
          onChange={this.onChange}
          api="lbs_api/search"
          style={styles.list}
          paged ={true}
          pageSize={29}
          data={this.state.data}
          renderRow={this._renderRow}
          renderSeparator={this.renderSeparator}
        />
      );
  }

	render(){
			return (
				 <TopMenu 
          style={this.props.style}
          config={this.state.config} 
          onSelectMenu={this.onSelectMenu} 
          renderContent={this.renderContent} />
			);
	}
}


const styles = StyleSheet.create({
	container:{
    flex:1,
  },
  list:{flex:1,backgroundColor:'#fff'},
 
});
