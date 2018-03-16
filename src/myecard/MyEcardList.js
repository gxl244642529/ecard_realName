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
    A,
    RefreshControl,
    ActivityIndicator,
    PixelRatio
    } from '../../lib/Common'

import TitleBar from '../widget/TitleBar'
import StateListView from '../widget/StateListView'
const SCREEN_WIDTH = Dimensions.get('window').width;
import Notifier from '../../lib/Notifier'
import LoadingButton from '../widget/LoadingButton'
import {formatDate} from '../lib/StringUtil'
import {InteractionManager} from 'react-native'
import {DeviceEventEmitter} from 'react-native'
import NfcUtil from '../../lib/NfcUtil'

import {
  radioButton,
  loadingButton,
  loadingButtonReal,
  loadingButtonRealDisabled,
  container
} from '../GlobalStyle'
const SysModule = require('react-native').NativeModules.SysModule;

class Line extends Component {
  render(){
    return(
      <View style={{    height:1/PixelRatio.get(),
          backgroundColor:'#d7d7d7',
          marginLeft:5,}}/>
    )
  }
}

export default class MyEcardList extends Component{

  constructor(props) {
    super(props);
    this.state={data:{moneyCard:false}};
  }

  componentDidMount(){
      this.ecardUpdate = DeviceEventEmitter.addListener('ecardUpdate', this._refreshList);
      Notifier.addObserver('nfcTag',this.onNfc)
  }

  componentWillUnmount() {
    if(this.ecardUpdate){
       this.ecardUpdate.remove();
      this.ecardUpdate = null;
    }
    Notifier.removeObserver('nfcTag',this.onNfc);
  }

  onNfc=()=>{
    NfcUtil.readCard((cardId)=>{
          A.confirm("确定要绑定卡号"+cardId+"吗",(index)=>{
            if(index==0){
              let data = {cardId:cardId,name:""}
              Api.api({
                api:"ecard/bind",
                data:data,
                success:(result)=>{
                  this._refreshList();
                }
              });
            }else{
              return;
            }
          });
    })
    return true
  }

  _refreshList=(data)=>{
    let list = this.refs.list;
    list.reloadWithStatus();
  }

 _bindCard=()=>{
   Api.push('bindECard');
 }
 _cardDetail=(rowData)=>{
   Api.push('ecardDetail',rowData);
 }
 renderRow=(rowData,rowID)=>{
   return(
     <View>
       <TouchableOpacity style={styles.itemTou} onPress={()=>{this._cardDetail(rowData)}}>
         <View style={styles.leftView}>
           {rowData.createDate?<Image source={require('./images/isreal.png')} style={styles.cardImg} resizeMode="contain"/>:
         <Image source={require('./images/card_g.png')} style={styles.cardImg} resizeMode="contain"/>}
         </View>
         <View style={styles.midView}>
           <View style={styles.contentView}>
             <Text style={styles.cardid}>{rowData.cardid}</Text>
             {!(/^0+$/.test((rowData.cardidExt)&&(rowData.cardidExt).replace(/\s/g,'')))&&<Text style={styles.cardidExt}>{rowData.cardidExt}</Text>}
             <Text style={styles.cardName}>{rowData.cardName}</Text>
           </View>
           {rowData.createDate&&<Image source={require('./images/isreal_flag.png')} style={styles.flagImg} resizeMode="contain"/>}
           {rowData.cardFlag==1&&<Image source={require('./images/account_flag.png')} style={styles.accountflag} resizeMode="contain"/>}
           {!rowData.createDate&&rowData.cardFlag!=1&&<Image source={require('./images/isnoreal_flag.png')} style={styles.flagImg} resizeMode="contain"/>}
         </View>
           <View style={styles.rightView}>
             <Image source={require('./images/go.png')} style={styles.cardImg} resizeMode="contain"/>
           </View>
       </TouchableOpacity>
       <Line/>
     </View>
   );
 }


  render(){
      return(
        <View style={CommonStyle.container}>
          <TitleBar title="我的e通卡"/>
          <Text style={styles.notice}>友情提醒：卡片余额仅供参考，如需确定卡片余额，请持卡片到就近的服务网点进行查询。</Text>
          <Text style={{color:'#e8464c',fontSize:14,paddingLeft:10}}>请尽快对已绑定的卡片开通挂失服务。</Text>
          <View style={{flex:1}}>
            <StateListView
             ref="list"
             data={this.state.data}
             api="myecard/list"
             style={styles.container}
             renderRow={(rowData,rowID)=> this.renderRow(rowData,rowID)}
             />
             <View style={styles.touView}>
               <TouchableOpacity style={styles.bindtou} onPress={this._bindCard}>
                  <Text style={styles.bindtext}>绑定e通卡</Text>
               </TouchableOpacity>
             </View>
           </View>
        </View>
      )
    }
}

const styles = StyleSheet.create({
    container:{
      flex:0.8,
    },
    notice:{padding:10,fontSize:14,color:'#333333'},
    itemTou:{
      flexDirection:'row',flex:1,paddingTop:20,paddingBottom:20
    },
    leftView:{flex:0.2,alignItems:'center',justifyContent:'space-around'},
    cardImg:{width:35,height:35},
    midView:{flexDirection:'row',flex:1,justifyContent:'space-between',paddingLeft:15},
    contentView:{flex:0.7,justifyContent:'center'},
    flagImg:{width:60,height:60},
    rightView:{flex:0.1,alignItems:'center',justifyContent:'space-around'},
    touView:{flex:0.2},
    bindtou:{backgroundColor:'#e8464c',height:40,justifyContent:'center',alignItems:'center',margin:30,borderRadius:5},
    bindtext:{color:'#fff',fontSize:16},
    cardid:{fontSize:16,color:'#333333'},
    cardIdExt:{fontSize:14,color:'#cccccc'},
    cardName:{fontSize:12,color:'#9f9f9f'},
    accountflag:{width:40,height:40,marginRight:10},

});
