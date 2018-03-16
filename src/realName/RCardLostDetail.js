
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


import {FormInput,FormSelect,FormSwitch,FormItem} from '../../lib/TemplateForm'
import TitleBar from '../widget/TitleBar'
import Select from '../../lib/Select'
import ImagePicker from '../../lib/ImagePicker'
import RadioButtonGroup from '../widget/RadioButtonGroup'
import LoadingButton from '../widget/LoadingButton'


import {
radioButton,
loadingButton,
loadingButtonDisabled
} from '../GlobalStyle'


const picPlaceHolder = require('../images/s_ic_add_diy.png');

export default class FormView extends Component{

constructor(props) {
  super(props);
  this.state={};
  this.form = new Form();
}

onSuccess=(result)=>{
  console.log(result);
}
componentDidMount(){
  let data = {cardId:this.props.params.id};
  Api.api({
    api:"rcard/progress",
    data:data,
    success:(result)=>{

    }
  });
}

onSubmit=()=>{
  this.form.submit(this,{
    api:'rcard/progress',
    crypt:0,
    success:this.onSuccess
  });
}

render(){
  let self = this.state;
  return (
    <ScrollView style={CommonStyle.container}>
        <TitleBar title="挂失详情" />

        <Text style={{justifyContent:'center',padding:5}}>卡号  {self.cardId}  {self.cardIdExt}</Text>
        <Text style={{justifyContent:'center',padding:5}}>绑定时间   {self.createDate?formatDate(self.createDate):null}</Text>
        <Text style={{justifyContent:'center',padding:5}}>姓名  {self.name} </Text>
        <Text style={{justifyContent:'center',padding:5}}>证件号码  {self.idCard} </Text>







        <LoadingButton
          loading={this.state.submiting}
          text="提交"
          onPress={this.onSubmit}
          disabled={this.state.submiting}
          style={this.state.submiting ? loadingButtonDisabled : loadingButton.loadingButton}
          textStyle={loadingButton.loadingButtonText} />
    </ScrollView>
  );
}
}


const styles = StyleSheet.create({
arrow:{width:7,height:12,marginRight:5,marginLeft:5},
scrollView:{flex:1,backgroundColor:'#F2F2F2'},
row:{backgroundColor:'#FFFFFF',marginTop:5,height:36,flexDirection:'row',alignItems:'center'},
inputText:{fontSize:14,flex:1,alignSelf:'center',marginLeft:10},
inputTextPlaceholder:{color:"#ccc"},
input:{fontSize:14,flex:1,height:36,marginLeft:10},
right:{flexDirection:'row',flex:1,marginLeft:10},
label:{marginLeft:10,fontSize:13,color:'#555555'},
picLabel:{marginTop:10},
picContainer:{backgroundColor:'#FFFFFF',marginTop:5}
});
