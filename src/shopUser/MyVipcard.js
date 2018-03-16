import React,{Component} from 'react'

import TitleBar from '../widget/TitleBar'

import{
  View,
  Text,
  Image,
  StyleSheet,
  ScrollView,
  Api,

  TouchableOpacity,
  CommonStyle,
  A
}from '../../lib/Common'

import {VipCard} from './ShopUserUtils'
import Notifier from '../../lib/Notifier'
import StateListView,{renderLoading,refreshColors} from '../widget/StateListView'



export default class MyVipcard extends Component{
    constructor(props) {
      super(props);
      this.state={loading:true,hasData:false};
    }
    componentWillMount() {
  		Notifier.addObserver("member/join",this.refreshList);

  	}
  	componentWillUnmount() {
  			Notifier.removeObserver("member/join",this.refreshList);
  	}
    refreshList=()=>{
      this.refs.list.reloadWithStatus();
    }
    _onListChange=(data)=>{
      this.setState({loading:data.loading,hasData: data.list && data.list.length>0});
    }

    _itemClick=(rowData)=>{
      Api.push("/shopUser/myVipdetail/"+rowData.code);

    }
    _expireItem=(rowData)=>{
      let data = {id:rowData.shpId}
      A.confirm("您的会员卡已过期，是否再次加入会员?",(index)=>{
        if(index==0){

          Api.api({
            api:'member/join',
            data:data,
            waitingMessage:'正在提交...',
            success:(data)=>{
              // this.setState(data);
              A.alert("加入会员成功!")
            }
          });
        }
      });
    }
    _vipCard=(rowData,sectionID,rowID)=>{
      return<VipCard
              rowData={rowData}
              itemClick={()=>{this._itemClick(rowData)}}
              expireItem={()=>{this._expireItem(rowData)}}/>
    }

    render(){
      let state = this.state;
      let style = !(this.state.loading !== this.state.hasData) ?
                              CommonStyle.containerCenter : {};
      console.log("正在执行");
      return(
        <View style={[CommonStyle.container,{backgroundColor:'#f0f0f0'}]}>
          <TitleBar title="我的会员卡"/>
          <StateListView

            ref="list"
            api="member/cards"
            style={  (this.state.loading !== this.state.hasData) ? styles.container : styles.listEmpty}
            renderRow={this._vipCard} />

        </View>
      );
    }


}
const styles=StyleSheet.create({
  card:{
    margin:15,
  },
  container:{flex:1},
  listHasData:{flex:1},
  listEmpty:{height:0},
})
