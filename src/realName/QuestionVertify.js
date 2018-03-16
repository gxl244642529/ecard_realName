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
  DatePicker,
  Account,
  ActivityIndicator,
  CommonStyle,
  PixelRatio,
  ScrollView,
  Form
} from '../../lib/Common';

import TitleBar from '../widget/TitleBar'
import {IC_CircleDot,IC_HollowCircle} from './component/Icons'
import LoadingButton from '../widget/LoadingButton'
import CheckBox from './component/Checkbox'
import StateListView from '../widget/StateListView'
import Notifier from '../../lib/Notifier'

import {
  radioButton,
  loadingButton,
  loadingButtonReal,
  loadingButtonRealDisabled,
  container
} from '../GlobalStyle'
var CheckBoxData=[];
import BindingWays from './BindingWays'
import {FROM_REAL,FROM_REAL_CARD} from './CommonData'
export default class QuestionVertify extends Component{
    constructor(props){
        super(props);
        let json=props.params.json;
        this.state={
            index:0,
            canVerify:true,
            errorCount:3,
            reqtext:true,
            hasbut:false,
            title:"问题验证",
            stadata:{
              cardId:JSON.parse(json).cardId,
            },
            data:JSON.parse(json),
        }
    }
    componentDidMount(){
        // console.log(      Api.getRoutes());
      Api.api({
        api:'rcard/getErrorCount',
        success:(result)=>{
          this.setState({errorCount:result})
        }
      });
    }
    componentWillMount() {
      Notifier.addObserver("androidBack",this._onBack);
    }
    componentWillUnmount() {
      CheckBoxData = [];
      Notifier.removeObserver("androidBack",this._onBack);

    }


    // _changeIndex=(index)=>{
    //     this.setState({index});
    // }
    // _renderRow=(data)=>{
    //     return <QuestionOption {...data} index={this.state.index} onPress={()=>this._changeIndex(data.order)}/>
    // }
    onSuccess=(result)=>{

      console.log(result)
      if (!result) {
        this.setState({canVerify:false});
      }else {
        A.alert("开通挂失成功");
        this._goPage();
      }

    }
    onSubmit=()=>{
      if(CheckBoxData.length==0){
        A.alert("请先选择您最近七天乘坐的公交路线进行提交");
        return;
      }
      var lineList = CheckBoxData.join(',');
      // let data = {cardId:this.props.params.id,lineList:lineList}///
      let data = {cardId:this.state.data.cardId,lineList:lineList}
      A.confirm("确定提交答案?回答错误将减少一次回答机会",(index)=>{
        if(index==0){
          Api.api({
            api:"rcard/submitVerify",
            data:data,
            success:this.onSuccess
          });
        }else {
          return;
        }
      });

    }
    gotoOtherVertify=()=>{
        // Api.push('/realName/bindingWays/'+this.props.params.id);
        //  Api.push('/realName/otherWay/'+this.props.params.id);///
        let data = {cardId:this.state.data.cardId,fromto:this.state.data.fromto}
         Api.push('/realName/otherWay/'+JSON.stringify(data));

    }
    // initCheckBoxData=(checkbox)=>{
    //
    //   if(checkbox!=null){
    //     CheckBoxData.push(checkbox);
    //   }
    //   console.log(CheckBoxData);
    // }
    onClick=(isCheck,rowData,rowIDD)=>{
      if (!isCheck) {
        CheckBoxData.push(rowData.BUS_NAME);
        console.log(CheckBoxData);
      }else {
        for (var i = 0; i < CheckBoxData.length; i++) {
          if (CheckBoxData[i] == rowData.BUS_NAME) {
                CheckBoxData.splice(i, 1);
                  console.log(CheckBoxData);
                break;
            }
        }
      }

    }
    renderRow=(rowData,rowID)=>{
      let data = "乘坐公交线路"
      return(
        <View style={{flexDirection:'row',paddingLeft:20,paddingTop:20,alignItems:'center'}}>
          <CheckBox
            style={{backgroundColor:'red'}}
            rightText={data+rowData.BUS_NAME}
            value={rowID}
            style={styles.check}
            onClick={(isCheck)=>this.onClick(isCheck,rowData)} />

        </View>
      );
    }
    _renderHeader(rowData,rowID){

      return(
        <View style={styles.topMsg}>
            <Text style={styles.topMsgText}>要实名绑卡您还需要经过小小的验证哦！</Text>
            <Text style={styles.topMsgText}>请选择您最近七天乘坐的公交线路进行提交</Text>
        </View>
      );
    }
    // <TouchableOpacity style={styles.noreqView} onPress={this.gotoOtherVertify}>
    //   <Image style={styles.checkFialImage} source={require('./images/checkFail.png')}/>
    //   {this.state.reqtext?<Text style={[styles.topMsgText,styles.tip]}>您已回答问题错误{this.state.errorCount}次，无法再生成问题，请选择其他方式进行验证绑卡</Text>:
    // <Text style={[styles.topMsgText,styles.tip]}>无乘车记录，问题获取失败，请选择其他方式验证绑卡</Text>}
    // </TouchableOpacity>

    _renderNoRequestion(){
      return(
        <BindingWays id={this.state.data.cardId} fromto={this.state.data.fromto}/>
      );
    }
    _message=(error)=>{
      // if (error=='notRecord') {
      //   this.setState({canVerify:false,reqtext:false});
      // }else if(error=='exceedError'){
      //   this.setState({canVerify:false,reqtext:true});
      // }
      this.setState({canVerify:false,title:"绑卡方式选择"});
    }

    _sendError=()=>{
      console.log("测试是否执行")
      if(this.state.hasbut===true){
        this.setState({hasbut:false});
      }
    }
    _onChange=(loading,list)=>{
      console.log(loading);
      if(this.state.hasbut===false){
          this.setState({hasbut:true})
      }

    }
    _renderRequestion(){
      return(
        <View style={styles.flex}><StateListView
           ref="LIST"
           api="rcard/getVerify"
           data={this.state.stadata}
           _message={this._message}
           style={styles.container}
           renderHeader={this._renderHeader}
           renderRow={this.renderRow}
           sendError={this._sendError}
           onChange={this._onChange}

           />
           {this.state.hasbut&&  <View style={styles.btn}>
                  <LoadingButton
                      loading={this.state.submiting}
                      text="确认绑定"
                      onPress={this.onSubmit}
                      disabled={this.state.submiting}
                      style={this.state.submiting ? loadingButtonRealDisabled : loadingButton.loadingButtonReal}
                      textStyle={loadingButton.loadingButtonText} />
                </View>}



        </View>
      );
    }
    _renderRight=()=>{
      return(
        <TouchableOpacity style={styles.lost} onPress={this.gotoOtherVertify}>
					<Text style={{color:'red'}}>其他方式></Text>
				</TouchableOpacity>
      );
    }
    _goPage=()=>{
      if(this.state.data.fromto==FROM_REAL){
        Api.returnTo('/');
        // return true;
      }else if (this.state.data.fromto==FROM_REAL_CARD) {
        Api.returnTo('/realName/rCard');
        // return true;
      }
    }
    _onBack=()=>{
      //返回
      CheckBoxData = [];//清空数据
      // let arr = Api.getRoutes();
      // console.log( arr);
      var url =  Api.getRoutes()[Api.routeCount()-1].location.pathname;
      if(url.startsWith('/realName/questionVertify/'+this.props.params.json)){
        console.log(this.state.data.fromto);
        this._goPage();
       return true;

      }
      return true;
    }
    render(){
        return <View style={CommonStyle.container}>
		    <TitleBar title={this.state.title}  renderRight={this.state.canVerify&&this._renderRight} onBack={this._onBack}/>
            {this.state.canVerify?this._renderRequestion():this._renderNoRequestion()
            }


        </View>
    }
}

const styles=StyleSheet.create({
    topMsg:{
        backgroundColor:'#ededee',
        paddingTop:10,
        paddingBottom:10,
        flex:0.1,
        alignItems:'center',
        justifyContent:'center',
    },
    topMsgText:{
        color:'#595757',
        fontSize:14,
        lineHeight:21
    },
    btn:{
    //  marginTop:30
    },
    otherVertify:{
        alignItems:'flex-end',
        paddingRight:20,
        paddingBottom:20,
        // paddingTop:20,
        marginTop:5
    },
    //选项条
    option:{
        flexDirection:'row',
        padding:15,
        paddingLeft:20,

        borderBottomColor:'#d7d7d7',
        borderBottomWidth:1,
    },
    //功能样式
    row:{
        flexDirection:'row'
    },
    font14:{
        color:'#595757',
        fontSize:14,
    },
    mleft10:{
        marginLeft:10
    },
    p5:{
        padding:5
    },
    check:{
      // color:'#27a7c4'
    },
    container:{
      flex:0.7
    },
    checkFialImage:{
      width:150,
      height:150,
      resizeMode:'contain'
    },
    tip:{
      paddingLeft:25,
      paddingRight:25,
      paddingTop:10,
    },
    flex:{
      flex:1
    },
    noreqView:{
      flex:1,
      alignItems:'center',
      justifyContent:'center'
    },
    lost:{
    	flexDirection:'row',
    	height:45,
    	width:80,
    	justifyContent:'center',
    	alignItems:'center'
    },
})
