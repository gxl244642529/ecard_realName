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
import DatePickerItem from './DatePickerItem'
import ImagePicker from '../../lib/ImagePicker'
import {lineView} from './realNameUtils'
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
import StandardStyle from '../lib/StandardStyle'
import   CryptoJS from 'crypto-js'

const SORT_VALUES = [1,2,3,4];
const SORT_LABELS = ['身份证','台胞证','港澳居民身份证','外籍护照'];
const SCREEN_WIDTH = Dimensions.get('window').width;
const SCREEN_HEIGHT= Dimensions.get('window').height;
const DEFAULT = 1;
const SORT1_VALUES = [0,1];
const SORT1_LABELS = ['身份证','户口薄'];
const DEFAULT_IMGTYPE = 0;
const picBg = require('./images/nopic.png');
const img1Default = require('./images/CharacterImage3.png');
const img2Default = require('./images/plus1.png');

export default class NewMessageSubmit extends Component{
  constructor(props){
    super(props);
    let now = new Date();
    this.state = {
      idType:DEFAULT,
      imgType:DEFAULT_IMGTYPE,
      validDate:null,
      minDate:now.format("yyyy-MM-dd")
    };
    this.form = new Form();

  }
  componentDidMount(){
    Api.api({
      api:"real/info",
      crypt:3,
      success:(result)=>{
        console.log(result);
        if (result) {
          // this.setState(result);
          this.setState({name:result.name,idType:result.idType,idCard:result.idCard});
        }

      }
    });
  }
  encCrypt=(str)=>{
    var img1 = CryptoJS.enc.Utf8.parse(str);
    var _base64 = CryptoJS.enc.Base64.stringify(img1);
    return _base64;
  }
  _submit=()=>{
    console.log("正在提交");
    let data =   this.state;
    let idCard = data.idCard;

    if (!this.state.idType) {
      this.setState({idType:DEFAULT});
    }
    if(!data.name){
      A.alert("请输入真实姓名");
      return;
    }
    if(!data.idCard){
      A.alert("请输入证件号码");
      return;
    }
    if (data.idType===1) {
      if (!checkCard(idCard)) {
        A.alert("请输入正确的身份证号码");
        return;
      }
    }
    if(!this.state.validDate){
      A.alert("请选择证件有效期!");
      return;
    }

    if (this.state.validDate<this.state.minDate) {
      A.alert("您的证件已过有效期!");
      return;
    }


    if(!this.state.img1){
      A.alert("请上传证件照片");
      return;
    }

    if(!this.state.img2){
      A.alert("请上传证件照片");
      return;
    }

    // let sdata = {name:data.name,idType:data.idType+"",idCard:data.idCard};
    var _base64img1 = this.encCrypt(data.img1);
    var _base64img2 = this.encCrypt(data.img2);

    let sdata = {name:data.name,idType:data.idType
      ,idCard:data.idCard,validDate:data.validDate,img1:_base64img1,img2:_base64img2,imgType:data.imgType};

    console.log(sdata);
    A.confirm("请确保填写信息的真实性",(index)=>{
      if(index==0){
        let nowDate = new Date().format("yyyy-MM-dd");
        if(this.state.validDate == nowDate){
          A.confirm("确认您填写的有效期和身份证背面的有效期一致吗?",(index)=>{
            if(index==0){
              Api.push('/realName/bankMessage/'+JSON.stringify(sdata))
            }
          })
        }else {
          Api.push('/realName/bankMessage/'+JSON.stringify(sdata))
        }

      }else{
        return;
      }
    });
  }
  _setIdType=(index,idType)=>{

    this.setState({idType})
  }

  render(){
    let self = this.state;
    return <View style={[CommonStyle.container,container.container]}>
      <TitleBar title="信息提交"/>
      <ScrollView>
        <View style={styles.top}>
           <Image source={require('./images/img1.png')} style={styles.topImage} resizeMode="contain"/>
        </View>
        {this.props.errorMsg&&<View style={{padding:15}}><Text style={{color:'#e8464c'}}>上一次身份认证失败，失败原因：{this.props.errorMsg}</Text></View>}
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
          onChange={(index,idType)=>{this._setIdType(index,idType)}} />
        <FormInput
          label="证件号码:"
          placeholder={"请输入证件号码"}
          ref="idCard"
          value = {self.idCard}
          onChange={(idCard)=>{this.setState({idCard})}}
          hasBottom={true}
          hasHead={true}
          />
          <DatePickerItem
            required={true}
            label="证件有效期至："
            placeholder="请选择证件有效期"
            ref="validDate"
            value={this.state.validDate}
            min={this.state.minDate}
            onChange={(validDate)=>{this.setState({validDate})}}
            hasBottom={true}
            hasHead={true}
            />

            <View style={styles.Photo}>
              <View style={styles.Photo_text}>
                  <Image source={require('./images/star.png')} style={styles.starImage}/>

                  {(parseInt(this.state.idType)==1&&this.state.imgType==0) &&
                  <Text style={[StandardStyle.h5,StandardStyle.fontBlack]}>请上传身份证正反面照片
                    </Text>}
                  {parseInt(this.state.idType)!=1 && <Text style={[StandardStyle.h5,StandardStyle.fontBlack]}>请上传有效证件正反面照片
                    </Text>}
              </View>
           </View>

            <View style={styles.Photo_img}>
              <ImagePicker
                ref="img1"
                disabled={this.state.submiting}
                placeholder="请上传证件照片"
                file={true}
                onChange={(img1)=>this.setState({img1})}
                album={true}
                style={{marginBottom:10}}>
                <Image

                  style={styles.photo}
                  source={this.state.img1 ?  {uri:this.state.img1} : img1Default}
                  />
              </ImagePicker>
              <ImagePicker
                ref="img2"
                disabled={this.state.submiting}
                placeholder="请上传证件照片"
                file={true}
                onChange={(img2)=>this.setState({img2})}
                album={true}
                style={{marginBottom:10}}>
                <Image

                  style={styles.photo}
                  source={this.state.img2 ?  {uri:this.state.img2} : img2Default}
                  />
              </ImagePicker>
            </View>
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
    marginTop:10,
  },
  topImage:{
    width:300,
    height:65,
  },
  photo:{
     alignSelf:'center',
     width:150,
     height:100
  },

Photo_text:{
    padding:15,
    flexDirection:"row",
},

Photo_img:{
    padding:10,
    flexDirection:"row",
    justifyContent:"space-around",
},
inputStyle:{
  marginLeft:5,
  height:60,
  width:SCREEN_WIDTH*0.6,
  fontSize:16,
},
starImage:{
  width:12,
  height:12,
  marginTop:3,
  marginRight:5,
},

})
