import React,{Component} from 'react'


import {
  View,
  Text,
  Image,
  StyleSheet,
  Dimensions,
  ScrollView,
  TouchableOpacity,
  Api,
  A,
  CommonStyle
} from '../../lib/Common'


const SCREEN_WIDTH = Dimensions.get('window').width;

import TitleBar from '../widget/TitleBar'
import Button from '../widget/Button'
import StarButton  from '../widget/StarButton'
import Star from '../widget/Star'
import StateListView from '../widget/StateListView'
import TitleName from '../widget/TitleName'
import CommonApi from '../widget/CommonApi'
import GoodsList from './GoodsList'
import Notifier from '../../lib/Notifier'
import {formatScore,formatTitle} from '../lib/StringUtil'
import MapUtil from '../lib/MapUtil'
import {isLogin} from '../../lib/LoginUtil'
export default class ShopDetail extends Component{

  constructor(props){
    super(props);
    let id=parseInt(this.props.params.id);
    this.state={commType:-1,isMember:false,data:{
      type:-1,shpId:id
    }
   };
  }


  onGetDistance=(distance)=>{
    console.log('distance===========',distance);
     distance = parseFloat(distance);
    if (distance<1000) {
      this.setState({targetDistance:parseInt(distance)+"m"});
    }else{
      this.setState({targetDistance:parseInt(distance/1000)+"km"});
    }
  }

  onGetPos=(data)=>{
    console.log(data);
    if(data.flag<0 || !data.lat || !data.lng){
      A.toast('定位失败');
      return;
    }
    console.log(data.lat+" "+data.lng);
    let opts = {lat:data.lat,lng:data.lng,targetLat:this.state.lat,targetLng:this.state.lng};
    MapUtil.getDistance(opts,this.onGetDistance);
  }

  onGetDetail=(data)=>{
      this.setState(data);
      MapUtil.getPos(this.onGetPos)
  }

  componentDidMount(){

    let id=parseInt(this.props.params.id);
    let self=this;
    let data={id:id}

     Api.detail(this,{api:"bshop/detail",data:data,success:this.onGetDetail});
    //获取商品详情
    //CommonApi._getShopDetail(data,this.onGetDetail);
    if(isLogin()){
      console.log('已经登录')
      //获取是否收藏
      CommonApi._getShopIsCol(data,function(data){
        //console.log(data);
        self.setState({isCol:data});
      });
      //获取是否是会员
      CommonApi._getShopIsVip(data,function(data){
        //console.log(data);
        self.setState({isMember:data});
      });
      //优惠券
      Api.detail(this,{
        api:"bshop/getCoupons",
        data:data,
        success:(coupons)=>{
          self.setState({coupons});
        }
      });
    }else{
 console.log('么有登录')
    }

  }

  componentWillMount() {
    Notifier.addObserver("bshop/evaluate",this.refreshList);

  }

  componentWillUnmount() {
    Notifier.removeObserver("bshop/evaluate",this.refreshList);
  }

  refreshList=()=>{
    this.refs.LIST.reloadWithStatus();
  }
  //收藏、取消收藏
  _onUpdateScore=(state)=>{

    //console.log("isSelect="+state);
    if (state) {


      Api.api({
        api:'bshop/col',
        data:{
          id:parseInt(this.props.params.id),
          type:1
        },
        success:(data)=>{
          // this.setState(data);
          A.toast("收藏成功");
        }
      })
    }else if(state===false){
      console.log("未选择，正在取消收藏");
      this.setState({collection:"收藏"});
      Api.api({
        api:'bshop/col',
        data:{
          id:parseInt(this.props.params.id),
          type:0,
        },
        success:(data)=>{
          // this.setState(data);
          A.toast("取消收藏");
        }
      })
    }

  }
  _getDistance=()=>{

  }
  //进入VIP页面
  _addVip=()=>{
    if (this.state.isMember) {
      Api.push('/shopUser/myVipcard')
    }else {
      Api.push('/shopUser/joinvip/'+parseInt(this.props.params.id)+"/"+this.state.title);
    }

  }
  //加入会员
  _renderRight=()=>{

    return <TouchableOpacity onPress={this._addVip}><View style={{flexDirection:'row',height:45,alignItems:'center',marginRight:5}}>
      <Image source={require('./images/addvip.png')} style={{width:18,height:18,resizeMode:'contain'}}/>
      <Text style={{color:'#EB614B',marginLeft:5,fontSize:12}}>加入会员</Text>
    </View></TouchableOpacity>
  }

  //评论标签渲染
  _renderCommontTag=(data)=>{
    if(data==1){
      return <View style={{flexDirection:'row',marginTop:2}}>
        <Image source={require("./images/chaping.png")} style={{width:14,height:14,}}/>
        <Text style={{fontSize:12,marginLeft:5,color:'#eb614b'}}>差评</Text>
      </View>
    }else if(data>3){
      return <View style={{flexDirection:'row',marginTop:2}}>
        <Image source={require("./images/haoping.png")} style={{width:14,height:14,}}/>
        <Text style={{fontSize:12,marginLeft:5,color:'#eb614b'}}>好评</Text>
      </View>
    }else{
      return <View style={{flexDirection:'row',marginTop:2}}>
        <Image source={require("./images/zhongping.png")} style={{width:14,height:14,}}/>
        <Text style={{fontSize:12,marginLeft:5,color:'#eb614b'}}>中评</Text>
      </View>
    }
  }
  //评论渲染
  _renderCommont=(data)=>{
    return <View style={styles.commList}>
    <View style={styles.commTop}>
      <Image source={require("./images/headshot.png")} style={{width:25,height:25}}/>
      <View style={{marginLeft:5,justifyContent:'space-between'}}>
        <Text style={{paddingLeft:2,fontSize:12}}>{data.acc}</Text>
        <Star num={data.score} style={{width:12,height:12}}/>
      </View>
      <View style={{flex:1,alignItems:'flex-end'}}>
        <Text style={{alignSelf:'flex-end',color:'#9f9fa0',fontSize:12}}>{data.time}</Text>
        {this._renderCommontTag(data.score)}
      </View>
    </View>
    <View style={{paddingLeft:32}}>
      <View style={{flexDirection:'row',justifyContent:'space-between'}}>
        {/*<Text style={{color:'#A38B77',color:'#9f9fa0',fontSize:12}}>XXX商品</Text>*/}

      </View>
      <Text style={{fontSize:12,marginTop:4}}>{data.content}</Text>
    </View></View>
  }

  returnTime=(str)=>{
    return str.substring(0,2)+":"+str.substring(2,4);
  }
  //领取优惠券
  _getCoupon=(id)=>{
    Api.api({
      api:"coupon/fetch",
      data:{
        id:id
      },
      success:(data)=>{
        if(data==1)A.toast("领取成功");
        else if(data==0)A.toast("你已领取过该优惠券");
      }
    })
  }

  _returnScore=(data)={

  }


  _renderEmpty=()=>{
    return <View style={[styles.advContent,{marginTop:2,marginBottom:10,paddingLeft:30}]}>
        <Text style={{color:'#A38B77'}}>暂无评论</Text>
    </View>
  }
  //渲染头部
  _renderHeader=()=>{
    let id=parseInt(this.props.params.id);
    let typeArr=["","服装","美食","住宿","出行","购物","娱乐"]
    return !this.state?null:<ScrollView>
      <View style={{padding:10,borderRightColor:'#FEFEFE'}}>
      <View style={{flexDirection:'row',justifyContent:'space-between',marginRight:15}}>
        <View style={{flexDirection:'row'}}>
          <TouchableOpacity
            onPress={()=>{Api.push('/shopUser/MoreShoppic/'+id+"/"+encodeURIComponent(this.state.src))}}>
            <Image source={{uri:this.state.thumb}} style={styles.thumbImg} />
          </TouchableOpacity>
          <View style={{padding:10,paddingTop:5,justifyContent:'space-between',flexWrap:'wrap',width:SCREEN_WIDTH/1.6}}>
            <Text style={styles.shopName}>{this.state.title}</Text>
            <Text style={{fontSize:12,flexWrap:'wrap',marginRight:10}}>{this.state.address}</Text>
            <View style={{flexDirection:'row',alignItems:'center'}}>
            <Image source={require("./images/location.png")} style={styles.locationIco}/>
            <Text style={{marginLeft:4,fontSize:12,flexWrap:'wrap'}}>{this.state.targetDistance}</Text></View>
          </View>
        </View>
        <View style={{paddingTop:5,alignItems:'flex-end'}}>
          	<StarButton
            style={styles.starStyle}
            isSelect={this.state.isCol}
            imageStyle={{width:30,height:30,resizeMode:'contain'}}
            updateScore={this._onUpdateScore}
            updateCanScore={this._onUpdateScore}/>
        </View>
      </View>
      <View style={styles.adv}>
        <View style={{flexDirection:'row'}}>
          <Image source={require("./images/adv.png")} style={{width:15,height:15}}/>
          <Text style={styles.advText}>公告</Text>
        </View>
        <View style={styles.advContent}>
          <Text style={{color:'#A38B77'}}>{this.state.notice?this.state.notice:"商家暂无公告"}</Text>
        </View>
      </View>
      <View>
        {/*<TitleName title="商家简介"/>
        <Text style={{fontSize:12,lineHeight:18}}>　　麦当劳餐厅在中国大陆早期译名是“麦克唐纳快餐”，直到后期才统一采用现今的港式译名。而在民间，因为麦当劳和“牡丹楼”的音近，牡丹楼也被当作是麦当劳的一个昵称，但并不普遍。</Text>
        <View style={{flexDirection:'row',flex:1,marginTop:10}}>
          {
            this.state.images && this.state.images.map(
              (data,index)=>{
                return <Image source={{uri:data}} style={styles.briefImg} key={'img'+index}/>
              }
            )
          }
        </View>
        <TouchableOpacity onPress={()=>{Api.push('/shopUser/moreshop/'+this.props.params.id)}}>
        <Text style={{margin:5,alignSelf:'flex-end',color:'#EA5414',fontSize:12}}>查看更多图片……</Text>
        </TouchableOpacity>*/}
      </View>
      <View>
        <TitleName title="商家信息" icon={require("./images/info_ico.png")} iconStyle={{width:15,height:18}}/>
        <Text　style={{marginBottom:5}}>分　　类:　{typeArr[this.state.type]}</Text>
        {this.state.startTime &&
          <Text>营业时间:　
          {this.returnTime(this.state.startTime)}-
          {this.returnTime(this.state.endTime)}
          </Text>}
          <Text style={{marginTop:5}}>联系电话:　{this.state.sphone}</Text>
      </View>
      <View>
        <TitleName title="优惠券领取(线下使用)" icon={require("./images/count_ico.png")}/>
        <View>
        <ScrollView horizontal={true}  style={{flexDirection:'row',marginRight:10,paddingRight:12}}>
        {
          this.state.coupons && this.state.coupons.map(
            (data,index)=>{
              console.log(data);
              if (data.fetched) {
               return(

                 <TouchableOpacity key={"youhui"+index} disabled={true} >
                   <Image source={require("./images/coupon_bg_used.png")} style={styles.coupon_bg}>
                     <View style={styles.countTag} key={"youhui"+index}>
                       <Text style={[styles.countText,{fontSize:15}]}>{data.title}</Text>
                       <Text numberOfLines={3}  style={[styles.countText,{paddingBottom:5}]}>{data.desc}</Text>
                     </View>
                     <Image source={require("./images/get_ico.png")} style={{resizeMode:'contain',width:40,height:35,position:'absolute',right:5,top:50}}/>
                   </Image>
                 </TouchableOpacity>
               )
             }else{
               return(

                 <TouchableOpacity key={"youhui"+index} onPress={()=>{this._getCoupon(data.id)}} >
                   <Image source={require("./images/coupon_bg.png")} style={styles.coupon_bg}>
                     <View style={styles.countTag} key={"youhui"+index}>
                       <Text style={styles.countText}>{data.title}</Text>
                       <Text numberOfLines={3}  style={[styles.countText,{paddingBottom:5}]}>{data.desc}</Text>
                     </View>

                   </Image>
                 </TouchableOpacity>
               )
             }
            })
        }
        {//如果没有优惠券列表
            this.state.coupons && <View>{
              this.state.coupons.length==0?<View style={styles.advContent}>
              <Text style={{color:'#A38B77'}}>暂无优惠券</Text>
            </View>:null
           }</View>
        }

        </ScrollView>
        {this.state.coupons && (this.state.coupons.length>3 &&
          <Image source={require('./images/rightarrow.png')} style={{position:'absolute',top:30,right:-12,width:30,height:30}}/>
        )}
        </View>

      </View>
      <View>
        <TitleName title="全部商品" icon={require("./images/goodslist_ico.png")} iconStyle={{width:18,height:15}}/>
        <View style={{flex:1}}>
          {
            this.state.list && this.state.list.map((data,index)=>{
                return <GoodsList data={data}  key={"goods"+index} isMain={true} />
            })
          }
          {//如果没有商品列表
            this.state.list && <View>{
              this.state.list.length==0?<View style={styles.advContent}>
              <Text style={{color:'#A38B77'}}>暂无商品</Text>
            </View>:null
           }</View>
          }
        </View>
      </View>
      <View style={{marginTop:20}}>
        <TitleName title="店铺评价" icon={require("./images/evaluation_ico.png")} iconStyle={{width:18,height:15}}/>
        <View style={styles.markView}>
          <View style={styles.scoreTextView}>
            <Text>综合评分:</Text>
          </View>
          <View style={styles.scoreView}>
            <Text style={{fontSize:24,color:'#ffffff'}}>{this.state.score ? this.state.score.toFixed(1) : '' }</Text>
          </View>
          <View style={styles.starView}>
            <Star num={formatScore(this.state.score)}/>
          </View>
        </View>

        <View style={styles.commBtn}>
          <TouchableOpacity onPress={()=>{
            this.setState({
              data:{
                type:-1,
                shpId:id
              }
            });
          }}
          >
          {this.state.evCount && <Text style={{fontSize:14,color:'#595757'}}>
            全部({this.state.evCount[0]+this.state.evCount[1]+this.state.evCount[2]})
          </Text>}
          </TouchableOpacity>
          <TouchableOpacity  onPress={()=>{
            this.setState({data:{
                type:0,
                shpId:id
              }});
          }}
          >
          {this.state.evCount && <Text style={{fontSize:14,color:'#EA5414'}}>好评({this.state.evCount[0]})</Text>}

          </TouchableOpacity>
          <TouchableOpacity  onPress={()=>{
            this.setState({data:{
                type:1,
                shpId:id
              }});
          }}>
          {this.state.evCount && <Text style={{fontSize:14,color:'#E4007E'}}>中评({this.state.evCount[1]})</Text>}
          </TouchableOpacity>
          <TouchableOpacity   onPress={()=>{
            this.setState({data:{
                type:2,
                shpId:id
              }});
          }}>
          {this.state.evCount && <Text style={{fontSize:14,color:'#2DA7E0'}}>差评({this.state.evCount[2]})</Text>}
          </TouchableOpacity>
        </View>
        {/*<View>
          <View style={styles.markContent}>
            <Image source={require("./images/ok.png")} style={styles.selectIco}/>
            <Text style={{fontSize:12,paddingLeft:5}}>只看有内容的评价</Text>
          </View>
        </View>*/}
      </View>
    </View>
    </ScrollView>
  }
  _gotoComm=()=>{
    // let arr ={"thumb":this.state.thumb,"title":this.state.title,"id":this.props.params.id}

    Api.push('/shopUser/evaluate/'+this.props.params.id+"/"+this.state.title+"/"+ encodeURIComponent(this.state.thumb));
  }
  render(){
    return <View style={CommonStyle.container}>
        <TitleBar
          title={this.state.title}
          renderRight={this.state.privilege&&this._renderRight}/>
        <StateListView
         onChange={(data)=>{

           //实时更新评价条数
           /*if(data.evCount){
             let newArr=[];
             for(var j=0;j<data.evCount.length;j++){
               newArr.push(data.evCount[j]);
             }
             this.setState({evCount:newArr});
           }*/
        }}
         ref="LIST"
         api="bshop/evaluates"
         style={{flex:1,backgroundColor:'#f0f0f0',marginBottom:10}}
         paged ={true}
         pageSize={10}
         data={this.state.data}
         renderRow={this._renderCommont}
         renderHeader={this._renderHeader}
         renderEmpty={this._renderEmpty}
         />
         <TouchableOpacity style={{backgroundColor:'#fff'}} onPress={()=>{this._gotoComm()}}>
           <View style={styles.shopComm}>
              <Text style={styles.shopCommTxt}>评价该店铺</Text>
           </View>
         </TouchableOpacity>
         </View>

  }
}
const styles = StyleSheet.create({
  thumbImg:{
    width:(SCREEN_WIDTH-20)*0.25,
    height:(SCREEN_WIDTH-20)*0.25,
    //borderRadius:5
  },
  shopName:{
    fontSize:16
  },
  goodsView:{
    flexDirection:'row',
    flex:1,
    backgroundColor:'#fff',
    marginTop:10,
    //borderRadius:5
  },
  adv:{
    marginTop:20,
    backgroundColor:'#fff',
    borderRadius:5,
    borderTopWidth:10,
    borderTopColor:'#f39700',
    paddingBottom:20,
    paddingTop:5,
    paddingLeft:5,
  },
  advText:{
    marginLeft:4,
    fontSize:14,
    color:'#EB614B'
  },
  advContent:{
    marginTop:5,
    paddingLeft:20
  },
  locationIco:{
    width:12,
    height:15,
    resizeMode:'contain'
  },
  brief:{

  },
  briefImg:{
    width:(SCREEN_WIDTH-35)/3,
    height:(SCREEN_WIDTH-30)/3*0.7,
    marginRight:5,
    borderRadius:5,
  },
  countTag:{
      height:85,
      alignItems:'center',
      //backgroundColor:'#ed7500',
      borderRadius:5,
      padding:5,
      paddingRight:10,
      paddingTop:5,
      paddingBottom:10,
      width:(SCREEN_WIDTH-35)/3,
      marginRight:5,

      //height:(SCREEN_WIDTH-30)/3*0.5,
  },
  countText:{
    color:'#fff',
    fontSize:12,
    marginTop:5,
  },
  goodsImg:{
    width:(SCREEN_WIDTH-20)*0.27,
    height:(SCREEN_WIDTH-20)*0.27,
    //borderRadius:5,
  },
  markView:{
    flexDirection:'row',
    justifyContent:'center',
    alignItems:'center',
    padding:20,
    backgroundColor:'#ffffff',
    borderRadius:5,
  },
  SameScoreStar:{

  },
  scoreTextView:{
    borderLeftWidth:1,
    borderLeftColor:'#eb614b',
    borderTopWidth:1,
    borderTopColor:'#eb614b',
    borderBottomWidth:1,
    borderBottomColor:'#eb614b',
    borderTopLeftRadius:5,
    borderBottomLeftRadius:5,
    //padding:7,
    height:35,
    justifyContent:'center',
    alignItems:'center',
    paddingLeft:10,
    paddingRight:10,
  },
  starView:{
    padding:7,
    borderTopRightRadius:5,
    borderBottomRightRadius:5,
    borderRightWidth:1,
    borderRightColor:'#eb614b',
    borderTopWidth:1,
    borderTopColor:'#eb614b',
    borderBottomWidth:1,
    borderBottomColor:'#eb614b',
    //
    height:35,
    justifyContent:'center',
    alignItems:'center',
    paddingLeft:10,
    paddingRight:10,
  },
  scoreView:{
    width:(SCREEN_WIDTH-20)*0.2,
    height:(SCREEN_WIDTH-20)*0.2,
    backgroundColor:'#eb614b',
    borderRadius:(SCREEN_WIDTH-20)*0.1,
    justifyContent:'center',
    alignItems:'center',
    margin:5,
  },
  markContent:{
    flexDirection:'row',
    marginTop:5,
    marginBottom:5,
    marginLeft:10,
    marginRight:10,
    alignItems:'center'
  },
  commList:{
    margin:5,
    marginTop:2,
    marginBottom:2,
    paddingTop:5,
    paddingBottom:5,
    paddingLeft:10,
    paddingRight:10,
    backgroundColor:'#ffffff',
    borderRadius:5,
  },
  commBtn:{
    flexDirection:'row',
    padding:10,
    backgroundColor:'#ffffff',
    marginBottom:2,
    justifyContent:"space-between",
  },
  commTop:{
    flexDirection:'row',
    borderBottomWidth:1,
    borderBottomColor:'#ddd',
    paddingBottom:3,
    marginBottom:3
},
  selectIco:{
    width:16,
    height:16
  },
  startImg:{
    width:15,
    height:15,
  },
  shopComm:{
    marginTop:5,
    marginBottom:5,
    marginLeft:20,
    marginRight:20,
    paddingLeft:10,
    paddingTop:7,
    paddingBottom:7,
    borderWidth:1,
    borderColor:'#9f9fa0',

  },
  shopCommTxt:{
    color:'#9f9fa0',
    fontSize:12
  },
  coupon_bg:{
      height:90,
      padding:5,
      paddingTop:2,
      paddingBottom:10,
      width:(SCREEN_WIDTH-35)/3,
      marginRight:5,
      backgroundColor:'transparent',
  }

});
