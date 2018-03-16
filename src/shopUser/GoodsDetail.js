import React,{Component} from 'react'
import TitleBar from '../widget/TitleBar'
import Button from '../widget/Button'
import StarButton from '../widget/StarButton'
import CommonApi from '../widget/CommonApi'
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
  A,
  CommonStyle
} from '../../lib/Common'

const SCREEN_WIDTH = Dimensions.get('window').width;
const SCREEN_HEIGHT= Dimensions.get('window').height;

export default class GoodsDetail extends Component{
  constructor(props){
    super(props);
    this.state={isCol:null}
  }



  componentDidMount(){
    let self=this;
    let id=parseInt(this.props.params.id);

    let data = {id :id};
    //获取是否收藏
    CommonApi._getGoodsIsCol(data,function(data){
      self.setState({isCol:data});
    });
    //获取商品详情
    CommonApi._getGoodsDetail(data,function(data){
      self.setState(data);
    });

  }

  _onUpdateScore=(state)=>{

    console.log("isSelect="+state);
    if (state) {

      let data ={id:parseInt(this.props.params.id),type:1};
      Api.api({
        api:'bgoods/col',
        data:data,
        success:(data)=>{
          // this.setState(data);
          A.toast("收藏成功");
        }
      })
    }else if(state===false){
      console.log("未选择，正在取消收藏");

      let data ={id:parseInt(this.props.params.id),type:0};
      this.setState({collection:"收藏"});
      Api.api({
        api:'bgoods/col',
        data:data,
        success:(data)=>{
          // this.setState(data);
          A.toast("取消收藏");
        }
      })
    }

  }

  _renderRight=()=>{
    console.info("isCol"+this.state.isCol);
    return <TouchableOpacity>{/*<View style={{flexDirection:'row',height:45,width:45,justifyContent:'center',alignItems:'center'}}>
      <Image source={require('./images/shopcart.png')} style={{width:19,height:19}}/>
    </View>*/}
    <View style={{flexDirection:'row',height:45,width:45,justifyContent:'center',alignItems:'center'}}>
    <StarButton
    isSelect={this.state.isCol}
    imageStyle={{width:30,height:30,resizeMode:'contain'}}
    updateScore={this._onUpdateScore}
    updateCanScore={this._onUpdateScore}/>

    </View>
    </TouchableOpacity>
  }

  returnTitle=(str)=>{
    if(!str)
      var str="";
    var len=str.length;
    if(len>8){
      return str.substr(0,7)+".."
    }
    return str
  }

  render(){
    console.log("iscol="+this.state.isCol)
    return !this.state?null:<View style={CommonStyle.container}><TitleBar title={this.returnTitle(this.state.title)}
     renderRight={this._renderRight}/>
      <ScrollView style={styles.container}>
        <TouchableOpacity onPress={()=>{
          Api.push("/shopUser/moregoods/"+this.props.params.id+"/"+encodeURIComponent(this.state.src))
        }}>
          <View style={{backgroundColor:'#fff'}}>
          <Image source={{uri:this.state.src}} resizeMode = 'stretch' style={{width:SCREEN_WIDTH,height:SCREEN_WIDTH*0.6}}/>
          </View>
        </TouchableOpacity>
        <View style={{padding:10}}>
          <View style={styles.goodsInfo}>
            <Text style={styles.goodsTitle}>{this.state.title}</Text>
            <View style={{flexDirection:'row',alignItems:'center'}}>
              <Text style={styles.infoPrice}>¥{this.state.disprice　}</Text>
              {this.state.price && <Text style={styles.infoDisprice}>¥{this.state.price}</Text>}

            </View>
            {/*<View style={styles.infoView}>
              <View style={[styles.infoContent,styles.infoLeft]}>
                <Text style={[styles.infoText,{color:'#EA5441',fontWeight:'bold'}]}>￥{this.state.price}</Text>
              </View>
               <View style={styles.infoContent}>
                <Text style={{fontWeight:'bold',color:'#9f9fa0'}}>原价:
                  <Text style={{textDecorationLine:'line-through'}}>￥{this.state.disprice}</Text>
                </Text>
               </View>
            </View>*/}
          </View>
          <View　style={{marginTop:5,marginBottom:5}}>
            <TitleName
              title={this.state.virtual?"商品简介":"商品简介(虚拟商品)"}

              icon={require("./images/info_ico.png")}/>
            <Text style={styles.briefText}>　　{this.state.desc?this.state.desc:"暂无简介"}</Text>
          </View>
          {/*<View style={{flexDirection:'row',flex:1}}>
            <Button text="加入购物车"
            style={[styles.btn,{backgroundColor:'#43B0B1'}]}
            textStyle={styles.btnText}
            icon="ebusiness/shopUser/images/down.png" iconStyle={styles.ico}
            />
            <Button text="立即购买"
            style={[styles.btn,{backgroundColor:'#EB614B'}]}
             textStyle={styles.btnText}
             icon="ebusiness/shopUser/images/hook.png" iconStyle={styles.ico}
             />
          </View>*/}
        </View>
      </ScrollView>
    </View>
  }
}
const styles=StyleSheet.create({
  container:{
    // padding:10,
    flex:1
  },
  goodsInfo:{
    backgroundColor:'#ffffff',
    paddingBottom:10,
  },
  goodsTitle:{
    fontSize:16,
    margin:10,
  },
  infoPrice:{
    marginLeft:10,
    fontSize:16,
    color:'#eb614b',
    fontWeight:'bold',
  },
  infoDisprice:{
    marginLeft:5,
    color:'#A5A5A5',
    textDecorationLine:'line-through',
  },
  btn:{
    flex:0.5,
    borderRadius:8,
    margin:10,
    flexDirection:'row'
  },
  btnText:{
    color:'#fff',
    marginLeft:5
  },
  infoView:{
    borderRadius:5,
    backgroundColor:'#ffffff',
    flexDirection:'row',
    flex:1,
  },
  infoLeft:{
    borderRightWidth:1,
    borderRightColor:'#ddd'
  },
  infoContent:{
    marginTop:15,
    marginBottom:15,
    flex:0.5,
    justifyContent:'center',
    alignItems:'center',
  },
  infoText:{
    // marginTop:5,
    color:'#7F4E20',
    fontSize:14,

  },
  ico:{
    width:18,
		height:18
  },
  briefTitle:{
    fontSize:14,
    margin:10,
    paddingBottom:5,
    borderBottomWidth:1,
    borderBottomColor:'#ddd',
  },
  briefText:{
    lineHeight:18
  }
});
