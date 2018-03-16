
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
  Platform
} from '../../lib/Common';

import NetpotSearch from './NetpotSearch'

const TITLE_HEIGHT = (Platform.OS === 'android') ? 45 : 65;
const PADDING_TOP = (Platform.OS === 'android') ? 0 : 20;

import {BackIcon,SearchIcon} from '../widget/Icons'


import {IC_SERVICE,IC_CON,IC_PURCH,IC_CHARGE,IC_RECHARGE,NetpotStyles,NetpotItem} from './NetpotUtils'




export default class NetpotMain extends Component{
	constructor(props){
		super(props);
		this.state={showSearch:false};
	}


  _onBack=()=>{
    Api.goBack();
  }
  
  _renderRight=()=>{
    return null;
  }

  _showSearch=()=>{
    this.setState({showSearch:true})
  }

  onCancel=()=>{
     this.setState({showSearch:false})
  }
  onPress=()=>{

  }
	render(){
			return (
				<View style={CommonStyle.container}>
          <View style={CommonStyle.all}>
            <View style={styles.titleBar}>
                <TouchableOpacity onPress={this._onBack} style={styles.backButton}>
                  <BackIcon  style={styles.backImage}  />
                </TouchableOpacity>
                <TouchableOpacity onPress={this._showSearch} activeOpacity={0.8} style={styles.titleCon}>
                  <SearchIcon />
                  <Text style={styles.titleText}>请输入商家名</Text>
                </TouchableOpacity>
                <TouchableOpacity onPress={this._onBack} style={styles.backButton}>
                 
                </TouchableOpacity>
                <View style={styles.titleLine} />
            </View>
            <View style={styles.list}>
              <NetpotItem icon={IC_CON} title="消费点" type={0} subtitle="可持e通卡刷卡消费"  />
              <View style={NetpotStyles.line}></View>
              <NetpotItem icon={IC_CHARGE} title="充值点" type={1} subtitle="可地图定位充值点"  />
              <View style={NetpotStyles.line}></View>
              <NetpotItem icon={IC_PURCH} title="售卡点" type={2} subtitle="可地图定位售卡点"  />
              <View style={NetpotStyles.line}></View>
              <NetpotItem icon={IC_SERVICE} title="服务网点" type={3} subtitle="e通卡营业厅地图定位查询"  />
              <View style={NetpotStyles.line}></View>
               <NetpotItem icon={IC_RECHARGE} title="补登点" type={4} subtitle="年审补登点地图定位查询"  />
              <View style={NetpotStyles.line}></View>
            </View>
          </View>
					
         

          {this.state.showSearch && <NetpotSearch 
            onChange={this.onChangeSchool} 
            show={this.state.showSearch} 
            onCancel={this.onCancel} />}
				</View>
			);
	}
}

const screenWidth = Dimensions.get('window').width;
const styles = StyleSheet.create({
  /**网点**/
 
  list:{backgroundColor:'#fff',flex:1},

  titleCon:{ justifyContent:'center', flexDirection:'row', alignItems:'center', backgroundColor:'#ebeced',height:31,borderRadius:15,marginTop:8,flex:1},
  titleText:{color:'#676768'},

	container:{
    flex:1,

  },
  titleLine:{
    height:1/PixelRatio.get(),
     width:screenWidth,
     position:'absolute',
     left:0,
     top:TITLE_HEIGHT-1,
     backgroundColor:'#dadbdb'
  },
  backImage:{
    width:9,height:19
  },
  backButton:{
    height:45,width:40,justifyContent:'center',alignItems:'center'
  },
  titleBar:{
    height:TITLE_HEIGHT,
      paddingTop:PADDING_TOP,
      flexDirection:'row',
      backgroundColor:'#FEFEFE'
  }
});
