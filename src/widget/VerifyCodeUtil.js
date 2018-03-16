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
  DatePicker,
  TouchableOpacity,
  Form
} from '../../lib/Common'
import {FormItem,FormSelect,FormImage} from '../../lib/TemplateForm'
import FormInput from './StandardFormInput'

var  CodeTime = 60;
export class VerifyCode extends Component{
  constructor(props){
    super(props);
    this.state={
      verifyText:"获取验证码",
      count:CodeTime,
      disabled:false,
    }

  }
  componentWillUnmount() {
    clearInterval(this.timer);//FIXME:无法实现卸载时清除计时器
   }

  verifyPress=()=>{

      let data = {phone:this.props.phone};
      let apiName = this.props.apiName;
      if (!checkPhone(this.props.phone)) {
        A.alert("请输入正确的手机号码");
        return;
      }

      Api.api({
        api:apiName,
        data:data,
        success:(result)=>{
          // this.setState({verifyId:result.toString()});
          this.props.setVerify(result.toString());
            this._countTimer();
        }
      });
  }
  _countTimer=()=>{
    this.timer = setInterval(()=> {

      this.setState({
          count: this.state.count - 1,
          verifyText:"再次发送("+this.state.count+")",
          disabled:true,
      });
      if (this.state.count === 0) {
          clearInterval(this.timer);
          this.setState({count:CodeTime});
          this.setState({verifyText:"重新发送",disabled:false,});

      }
    }, 1000);
  }
  render(){
    return(
        <View >
          <FormInput
            rule="phone"
            label="手机号码:"
            placeholder={"请输入手机号码"}
            ref={this.props.phoneRef}
            value={this.props.phoneValue}
            onChange={this.props.phoneChange}
            editable={this.props.editable}
            hasBottom={true}
            hasHead={true}
           />

           <View>
            <FormInput
              label="验证码:"
              placeholder={"请输入验证码"}
              ref={this.props.codeRef}
              value = {this.props.codeValue}
              onChange={this.props.codeChange}
              inputStyle={this.props.inputStyle}
              />
            <TouchableOpacity style={this.props.verifyStyle} disabled={this.state.disabled}
              onPress={this.verifyPress}>
              <Text>{this.state.verifyText}</Text>
            </TouchableOpacity>
          </View>

        </View>
    );
  }
}
export  function checkPhone(text){
		var myreg = (/^0?[1][3|4|5|7|8][0-9]\d{8}$/);
		return myreg.test(text);
	}
const styles=StyleSheet.create({
 viewStyle:{
   height:1,
   backgroundColor:'#d7d7d7',
   flexDirection:'row'
 },


})
