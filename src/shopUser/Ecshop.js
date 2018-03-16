import React,{Component} from 'react'

import TitleBar from '../widget/TitleBar'
import StateListView from '../widget/StateListView'
import Swiper from '../widget/Swiper'
import TitleName from '../widget/TitleName'

import {
  View,
  Text,
  Image,
  StyleSheet,
  Dimensions,
  ScrollView,
  TouchableOpacity,
  Api,
  CommonStyle,
  A
} from '../../lib/Common'


import AdvView from '../widget/AdvView'
import GoodsList from './GoodsList'
import MapUtil from '../lib/MapUtil'
import Urls from '../lib/Urls'

const SCREEN_WIDTH = Dimensions.get('window').width;

class NavTitle extends Component{
  constructor(props){
    super(props);
  }
  _onPress=()=>{
    let data = {type:this.props.type};
    Api.push('/shopUser/searchResult/'+JSON.stringify(data));
  }
  render(){
    return <TouchableOpacity  style={{flex:0.25,margin:10,marginTop:10,marginBottom:0}}
    onPress={this.props.onPress || this._onPress}>
    <View style={{alignItems:'center',justifyContent:'center',marginLeft:10,marginRight:10}}>
        <Image source={this.props.img} style={this.props.imgStyle}/>
        <Text style={{color:'#A38B77',paddingTop:5,paddingBottom:5,fontSize:12}}>{this.props.title}</Text>
      </View>
    </TouchableOpacity>
  }
}
class Br extends Component{
  render(){
    return <View style={{borderLeftWidth:1,borderLeftColor:'#ddd',height:40}}></View>
  }
}

export default class Ecshop extends Component{


  _togoodsDetail=(data)=>{
    console.log("/shopUser/goodsDetail/"+data.id);
    Api.push('/shopUser/goodsDetail/'+data.id);
    // /shopUser/goodsDetail/:id
      // Api.push('/repair/detail/'+data.id);

  }
  _onPress=()=>{
    Api.push("/shopUser/pCenter");
  }

  _renderRight=()=>{
    return (
        <TouchableOpacity style={{flexDirection:'row',height:45,width:45,justifyContent:'center',alignItems:'center'}}
          onPress={this._onPress}>
          <Image source={require('./images/per.png')} style={{width:16,height:19}}/>
        </TouchableOpacity>
    );
  }
  //商品渲染
  _renderGoods=(data)=>{
    return <View style={{padding:10,paddingTop:0,backgroundColor:'#fff',marginBottom:10}}>
    <GoodsList data={data} isMain={false}/>
    </View>
    {/*旧渲染方式：console.log("_renderGoods="+data.id)
    return <TouchableOpacity  onPress={()=>{this._togoodsDetail(data)}}>
    <View style={styles.goodsView}>
      <Image source={require("./images/test.png")} style={styles.goodsImg}/>
      <View style={{flex:0.7,marginTop:5,marginRight:5,marginLeft:5,justifyContent:'space-between'}}>
        <Text style={{color:"#693905",fontSize:14}}>{data.title}</Text>
        <Text style={{color:"#A38B77",fontSize:12}}>{data.desc}</Text>
        <View style={{flexDirection:'row',alignSelf:"flex-end"}}>
          <Text style={{color:'#EA5441',fontSize:16,fontWeight:'bold'}}>￥{data.price}</Text>
          <Text style={{color:'#A5A5A5',fontSize:14,marginLeft:5,marginRight:15}}>￥{data.disprice}</Text>
        </View>
      </View>
    </View></TouchableOpacity>*/}
  }
  searchAmbitus=()=>{
    MapUtil.getPos(
			(data)=>{
        console.log(data.lat+" "+data.lng);
        let sdata = {type:0,lat:data.lat+"",lng:data.lng+"",distance:3000};
				Api.push('/shopUser/searchResult/'+JSON.stringify(sdata));
			},
			function(){
				console.log("error");
			}
		);

  }
    //头部渲染
  _renderHeader=()=>{
    return (
      <View style={{backgroundColor:'#f0f0f0'}}>
      <View style={{marginTop:10}}>
          <TouchableOpacity onPress={()=>{Api.push('/shopUser/search')}}>
          <View style={styles.searchBar}>
            <Image source={require('./images/search.png')} style={styles.searchico}/>
          </View>
          </TouchableOpacity>
        <AdvView  id="6"/>


          <View style={{backgroundColor:'#fff'}}>
          <View style={{flexDirection:'row',alignItems:'center'}}>
            <NavTitle title="衣" img={require("./images/yi.png")} imgStyle={styles.imgStyle}
             type={1}/>

            <NavTitle title="食" img={require('./images/shi.png')} imgStyle={styles.imgStyle} type={2}/>

            <NavTitle title="住" img={require('./images/zhu.png')} imgStyle={styles.imgStyle} type={3}/>

            <NavTitle title="行" img={require('./images/xing.png')} imgStyle={styles.imgStyle} type={4}/>
          </View>
          <View style={{flexDirection:'row',alignItems:'center'}}>
            <NavTitle title="购" img={require('./images/gou.png')} imgStyle={styles.imgStyle} type={5}/>

            <NavTitle title="娱" img={require('./images/yu.png')} imgStyle={styles.imgStyle} type={6}/>

            <NavTitle title="全部" img={require('./images/quanbu.png')} imgStyle={styles.imgStyle} type={0}/>

            <NavTitle title="周边" img={require('./images/zhoubian.png')} imgStyle={styles.imgStyle}  type={-1}/>
          </View>
          </View>
          <TitleName title="推荐商品" icon={require("./images/good_ico.png")} iconStyle={{width:17,height:17}}/>



      </View>
      </View>
    )
  }

  render(){
    return (
      <View style={CommonStyle.container}>
      <TitleBar title="联盟商家" renderRight={this._renderRight}/>
      <StateListView
           api="bgoods/recommand"
           style={{flex:1,backgroundColor:'#f0f0f0'}}
           paged ={false}
           renderHeader={this._renderHeader}
           pageSize={10}
           renderRow={this._renderGoods}
           />
      </View>
    );
  }
}

const styles=StyleSheet.create({
  goodsView:{
    flexDirection:'row',
    flex:1,
    backgroundColor:'#fff',
    marginTop:10,
    padding:10
  },
  goodsImg:{
    // flex:0.3,
    width:(SCREEN_WIDTH-20)/3,
    height:(SCREEN_WIDTH-20)/3*0.8
  },
  searchBar:{
    width:SCREEN_WIDTH-40,
    marginLeft:20,marginRight:20,
    marginBottom:8,marginTop:0,
    height:30,
    paddingRight:10,
    backgroundColor:'#fff',
    justifyContent:'center',
    alignItems:'flex-end',
    borderRadius:10
  },
  imgStyle:{
    width:40,
    height:40
  },
  searchico:{
    width:19,
    height:19,
    resizeMode:'contain'
  },
  // t_title:{
  //   fontSize:14,
  //   color:'#EA5414',
  //   marginLeft:10
  // },
  t_ico:{
    width:10,
    height:14,
    paddingBottom:4,
    marginLeft:4
  },
  shop:{
    borderTopWidth:4,
    borderTopColor:'#fff'
  },
  shopImg:{
    width:(SCREEN_WIDTH-20)*0.3,
    height:(SCREEN_WIDTH-20)*0.3,
    padding:10,
  },
  startImg:{
    width:14,
    height:14,
  },
})
