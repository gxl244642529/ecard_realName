

import React, { Component } from 'react';
import {
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
  Image,
  Dimensions,
  PixelRatio,
  Api,
  A,
  ActivityIndicator,
  CommonStyle,
  Select,
  TextInput
} from '../../lib/Common';

import {TitleBar} from '../Global'
import StateListView from '../widget/StateListView'
import  Star from '../widget/Star'
const SCREEN_WIDTH = Dimensions.get('window').width;
const CARD_WIDTH=SCREEN_WIDTH*0.8;

import Button from '../widget/Button'



const _cancelCon=(rowData)=>{
  console.log("执行");
  let data = {id:rowData.id,type:0}

  A.confirm("确定要删除该收藏商家吗?",(index)=>{
    if(index==0){
      Api.api({
        api:"bshop/col",
        data:data,
        success:(result)=>{
          A.toast("删除收藏成功");
        }
      });
    }
  });
}
const _itemClick=(rowData)=>{
  //console.log("执行itemClick");
  Api.push("/shopUser/shopDetail/"+rowData.id);
}


export class VipCard extends Component{
  render(){
    let rowData = this.props.rowData;
    return(
      <View key={rowData.cmId} style={{justifyContent:'center',alignItems:'center',marginTop:20}}>
        {rowData.expire? <TouchableOpacity onPress={this.props.expireItem} disabled={this.props.disabled}>
          <Image source={require('./images/vvip_get.png')} style={[styles.img,this.props.style]}>
              <Text style={styles.shpName}>{rowData.shpName}</Text>
              <View  style={styles.validity}>
                <Text style={{fontSize:12,color:'#fff',marginTop:3,backgroundColor:'transparent'}}>有效期至</Text>
                <Text style={{fontSize:12,color:'#fff',backgroundColor:'transparent'}}>{rowData.endTime}</Text>
                <Text style={{fontSize:12,color:'#fff',backgroundColor:'transparent'}}>No.{rowData.code}</Text>
              </View>
          </Image>
        </TouchableOpacity> :<TouchableOpacity onPress={this.props.itemClick} disabled={this.props.disabled}>
          <Image source={require('./images/vvip.png')} style={[styles.img,this.props.style]}>
              <Text style={styles.shpName}>{rowData.shpName}</Text>
              <View style={styles.validity}>
                <Text style={{fontSize:12,color:'#fff',marginTop:3,backgroundColor:'transparent'}}>有效期至</Text>
                <Text style={{fontSize:14,color:'#fff',backgroundColor:'transparent'}}>{rowData.endTime}</Text>
                <Text style={{fontSize:12,color:'#fff',backgroundColor:'transparent'}}>No.{rowData.code}</Text>
              </View>
          </Image>
        </TouchableOpacity>

        }
    </View>
    );
  }
}



export function renderGoodsItem(rowData){
  return(
    <TouchableOpacity style={styles.main}>
        <Image style={styles.thumbStyle}  source={{uri:rowData.thumb}}/>

      <View style={styles.mainRight}>
        <View style={styles.mainRightOne}>
            <Text style={{ fontSize:12, }}>	{rowData.title}	</Text>
            <Image style={{ width:16, height:16, }} source={require('./images/delete.png')} />
        </View>



        <View style={styles.mainRightThree}>
              <Text>20元起送,5元配送费</Text>

        </View>
        <View style={styles.mainRightThree}>

              <Text style={{ color:'#54b5b7', }}>1000米/45分钟</Text>
        </View>
      </View>
  </TouchableOpacity>
  );

}
export function renderShopItem(rowData){
  console.log(rowData);
  let typeArr=[" ","服装","美食","住宿","出行","购物","娱乐"];
  let type=parseInt(rowData.type);
  return(
    <TouchableOpacity style={styles.main} onPress={()=>{_itemClick(rowData)}}>
          <Image style={{ width:80, height:80, borderRadius:5, marginLeft:10,marginTop:10,marginBottom:10 }}
           source={{uri:rowData.thumb}} />

          <View style={styles.mainRight}>
            <View style={styles.mainRightOne}>
                <Text style={{ fontSize:12, marginTop:10}}>	{rowData.title}	</Text>

              </View>

            <View style={styles.mainRightOne}>

                <Text style={{  fontSize:12,marginLeft:5,marginTop:10}}>{typeArr[type]}</Text>
                <TouchableOpacity onPress={()=>{_cancelCon(rowData)}} style={{width:40,height:40,justifyContent:'center',alignItems:'center'}}>
                  <Image style={{ width:16, height:16,marginRight:10 }}
                    source={require('./images/delete.png')} />
                </TouchableOpacity>
            </View>

            <View style={styles.mainRightThree}>
                <Star num={rowData.score} style={{width:15,height:15}}/>

            </View>
          </View>
      </TouchableOpacity>
  );

}

export function getContent(){
  var buf = new Buffer();

}
export function renderEmptyUtil(component){
  return <TouchableOpacity style={styles.centering}
			onPress={()=>{component.reloadWithStatus();}}>
			<Text style={{fontSize:14}}>没有搜索结果</Text>
			<Text style={{fontSize:12,marginTop:5}}>点击重试</Text>
		</TouchableOpacity>
}





const styles = StyleSheet.create({
	container:{
		backgroundColor:"#eff1f1",
		flex:1,
	},

	main:{
		// padding:10,
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
    justifyContent:'space-between',
		height:20,
	},

	mainRightThree:{
		flexDirection:'row',
		height:20,
		// fontSize:12,
    marginBottom:10,
		justifyContent:'space-between',
	},
	thumbStyle:{
	 width:80,
	 height:80,
	//  backgroundColor:'#f0f',
	 borderRadius:5,
	},
  img:{
    width:CARD_WIDTH,
    height:CARD_WIDTH/1.5,
    //width:235,
    //height:155,
    //paddingTop:55,
    //paddingLeft:15,
  },
  centering:{
    flex:1,
    // marginTop:120,
    alignItems: 'center',
    justifyContent: 'center'
  },
  shpName:{
    color:'#fff',
    backgroundColor:'transparent',
    flexWrap:'wrap',
    width:CARD_WIDTH*0.6,
    marginTop:CARD_WIDTH*0.26,
    marginLeft:10
  },
  validity:{
    //alignSelf:'flex-end',
    alignItems:'flex-end',
    //marginTop:28,
    //marginRight:6,
    position:'absolute',
    right:5,
    bottom:5
  }
});
