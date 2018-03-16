import React,{Component} from 'react'

import{
  View,
  Text,
  Image,
  StyleSheet,
  Api,
  A,
  CommonStyle,
  TextInput,
  Form,
  ScrollView,
  Dimensions,
  PixelRatio
} from '../../lib/Common'
import {FormItem,FormImage} from '../../lib/TemplateForm'

import FormInput from './StandardFormInput'
import FormSelect from './StandardFormSelect'
import LoadingButton from '../widget/LoadingButton'
import {
	radioButton,
	loadingButton,
	// loadingButtonDisabled
  loadingButtonReal,
  loadingButtonRealDisabled,
  container
} from '../GlobalStyle'

import {
  TitleBar,
} from '../Global'
import {checkCard} from './IDCheckUtils';

const SORT_VALUES = [1,2,3,4];
const SORT_LABELS = ['身份证','台胞证','港澳居民身份证','外籍护照'];
const SCREEN_WIDTH = Dimensions.get('window').width;
const SCREEN_HEIGHT= Dimensions.get('window').height;
const DEFAULT = 1;

export default class MessageSubmit extends Component{
  constructor(props){
    super(props);
    this.state = {idType:DEFAULT};
    this.form = new Form();

  }
  componentDidMount(){
    Api.api({
      api:"real/info",
      crypt:3,
      success:(result)=>{
        console.log(result);
        if (result) {
          this.setState(result);
        }

      }
    });
  }
  _submit=()=>{
    console.log("正在提交");
    let data =   this.state;
    let idCard = data.idCard;

    // if (!this.state.idType) {
    //   this.setState({idType:DEFAULT});
    // }
    if(!data.name){
      A.alert("请输入真实姓名");
      return;
    }
    if(!data.idCard){
      A.alert("请输入证件号码");
      return;
    }
    // if (data.idType===1) {
    //   if (!checkCard(idCard)) {
    //     A.alert("请输入正确的身份证号码");
    //     return;
    //   }
    // }

    let sdata = {name:data.name,idType:data.idType+"",idCard:data.idCard};
    A.confirm("请确保填写信息的真实性",(index)=>{
      if(index==0){
        // this.form.submit(this,{
        //   api:"real/check",
        //   crypt:3,
        //   waitingMessage:'正在提交...',
        //   success:(result)=>{
        //     Api.replace('/realName/checkSubmit/'+JSON.stringify(sdata));
        //     console.log(result);
        //   }
        // });
        Api.push('/realName/checkSubmit/'+JSON.stringify(sdata))
      }else{
        return;
      }
    });
  }

  render(){
    let self = this.state;
    return <View style={[CommonStyle.container,container.container]}>
      <TitleBar title="信息提交"/>
      <ScrollView>
        <View style={styles.top}>
           <Image source={require('./images/img1.png')} style={styles.topImage} resizeMode="contain"/>
        </View>

        <FormInput
          label="真实姓名:"
          placeholder={"请输入真实姓名"}
          ref="name"
          value={self.name}
          onChange={(name)=>{this.setState({name})}}
          hasBottom={true}
          hasHead={true}
          />


        <FormSelect
          label="证件类型:"
          placeholder={"请选择证件类型"}
          ref="idType"
          values={SORT_VALUES}
          labels={SORT_LABELS}
          value={self.idType?self.idType:DEFAULT}
          onChange={(index,idType)=>{this.setState({idType})}} />

        <FormInput
          label="证件号码:"
          placeholder={"请输入证件号码"}
          ref="idCard"
          value = {self.idCard}
          onChange={(idCard)=>{this.setState({idCard})}}
          hasBottom={true}
          hasHead={true}
          />
          <View style={styles.butStyle}>
            <LoadingButton
              loading={this.state.submiting}
              text="下一步"
              onPress={this._submit}
              disabled={this.state.submiting}
              style={this.state.submiting ? loadingButtonRealDisabled : loadingButton.loadingButtonReal}
              textStyle={loadingButton.loadingButtonText} />
          </View>
      </ScrollView>
    </View>
  }
}
const styles=StyleSheet.create({
  container:{
    backgroundColor:'#ffffff'
  },
  top:{
   justifyContent:'center',
   alignItems:'center',
   marginTop:25,
   marginBottom:25,
 },

  item:{
    padding:15

  },
  butStyle:{
    marginTop:150,
  },
  topImage:{
    width:300,
    height:65,
  }

})
