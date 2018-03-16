import React,{Component} from 'react'

import TitleBar from '../widget/TitleBar'
import Button from '../widget/Button'
import VipCard from './VipCard'

import{
  View,
  Text,
  Image,
  StyleSheet,
  Api,
  A,
  CommonStyle
}from '../../lib/Common'

export default class JoinVip extends Component{
  constructor(props){
    super(props);
    this.state={privilege:""};
  }
  componentDidMount(){
    let data = {id:parseInt(this.props.params.id)}
    Api.api({
      api:"member/privilege",
      data:data,
      success:(result)=>{
        console.log(result);
        this.setState(result);
        // this.setState({privilege:result});
      }
    });
  }



  _addVip=()=>{
    let data = {id:parseInt(this.props.params.id)};
    let id=parseInt(this.props.params.id);
    Api.api({
      api:'member/join',
      data:data,
      waitingMessage:'正在提交...',
      success:(data)=>{
        // this.setState(data);
        A.alert("加入会员成功，可在我的会员卡中查看!",function(){
         //   Api.replace('/shopUser/shopDetail/'+id);
            Api.goBack();
        });
      }
    });
  }
  render(){
    return <View style={[CommonStyle.container,{backgroundColor:'#f0f0f0'}]}>
      <TitleBar title="加入会员"/>
      <View style={styles.top}>
        <VipCard title={this.props.params.title}  endtime={this.state.expire}/>
      </View>
      <View>
        <Text style={{marginLeft:50,marginTop:20,marginBottom:20}}>会员特权</Text>
        <View style={styles.list}>
          {this.state.privilege?<Text>{this.state.privilege}</Text>:<Text>商家未编辑会员特权</Text>}

        </View>
    {/**   <Text style={{marginLeft:50,color:'#eb614b',marginTop:10,marginBottom:10}}>开通会员&gt;&gt;</Text>**/}
        <Button text="加入会员" style={styles.btn} textStyle={styles.btnText} onPress={this._addVip}/>
      </View>
    </View>
  }
}
const styles=StyleSheet.create({
  top:{
    //flex:1,
    height:160,
    backgroundColor:'#43b0b1',
    alignItems:'center',
    justifyContent:'center',
    marginBottom:10,
  },
  topImg:{
    width:200,
    height:125,
  },
  list:{
    justifyContent:'center',
    backgroundColor:'#fff',
    paddingLeft:50,
    paddingTop:15,
    paddingBottom:15,
  },
  btn:{
    marginTop:30,
    backgroundColor:'#43b0b1',
    marginLeft:50,
    marginRight:70,
    borderRadius:5
  },
  btnText:{
    color:'#fff'
  }
})
