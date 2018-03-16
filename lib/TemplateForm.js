import React,{Component,PropTypes} from 'react'
import {TouchableOpacity,TextInput,View,Text,Image,StyleSheet,Switch,Select,ImagePicker} from './Common'

import {findGetIndex} from './ArrayUtil'

export class FormItem extends Component{

  constructor(props) {
    super(props);
    this.state = {value:props.value};
  }

  componentWillReceiveProps(nextProps) {
    if(nextProps.value!==undefined && nextProps.value!==this.state.value){
      this.setState({value:nextProps.value});
    }
  }

  getValue(){
    return this.state.value;
  }


  onChange=(date)=>{
    this.setState({value:date});
    this.props.onChange && this.props.onChange(date);
  }

   render(){
    let props = this.props;
    let Comp = this.props.comp;
    return (
      <Comp
        style={[styles.formItem,props.style]}
        onChange={this.onChange}
        value={this.state.value}>
        <Text style={styles.label}>{props.label}</Text>
        <Text
          style={this.state.value?[styles.holder,styles.holderDark,props.inputStyle]:styles.holder}>{this.state.value ? this.state.value : props.placeholder}</Text>
          {!props.noimg &&<Image
            style={styles.rightArrow}
            source={require('../img/ic_arrow.png')} />}
      </Comp>
    );
  }
}

const COMPONENTS = {};

/**
选择器,专门选择一个值
*/
export class FormPicker extends Component{


  getValue(){
    return props.value;
  }

  render(){
    let props = this.props;
    let Comp = COMPONENTS[this.props.type];
    return (
      <Comp
        style={[styles.formItem,props.style]}
        onChange={props.onChange}
        value={props.value}>
        <Text style={styles.label}>{props.label}</Text>
        <Text
          style={[styles.input,props.inputStyle]}>{props.placeholder}</Text>
          <Image
            style={styles.rightArrow}
            source={require('../img/ic_arrow.png')} />
      </Comp>
    );
  }
}


export class FormInput extends Component{

  static propTypes = {
      placeholder:PropTypes.string,
      defaultValue:PropTypes.any,
  }

  getValue(){
    return this.props.value;
  }

  parseValue=(value)=>{
    if(!value)return value;
    console.log(value);
    if(this.props.type=='int')return Number(value);
    return value;
  }

  parseDisplayValue=(value)=>{
    if(!value)return value;
    console.log(value);
    return value.toString();
  }

  onChange=(value)=>{
    if(this.props.onChange){
      this.props.onChange(this.parseValue(value));
    }
  }

  render(){
    let props = this.props;
    let txtProps = {keyboardType:this.props.keyboardType};
    if(this.props.type=='int'){
      txtProps.keyboardType="numeric";
    }
    return (
      <View style={[styles.formItem,props.style]}>
        <Text style={styles.label}>{props.label}</Text>
        <TextInput
          {...txtProps}
          secureTextEntry={props.secureTextEntry}
          returnKeyType="done"
          numberOfLines={1}
          underlineColorAndroid={'#FEFEFE'}
          placeholder={props.placeholder}
          placeholderTextColor={props.placeholderTextColor}
          onChangeText={this.onChange}
          style={[styles.input,props.inputStyle]}
          value={this.parseDisplayValue(props.value)}
          keyboardType={this.props.keyboardType}
          editable ={this.props.editable}
          />
      </View>
    );
  }
}



const renderImage = ()=>{
    return   <Image style={styles.rightArrow} source={require('../img/ic_arrow.png')} />
};

export class FormSelect extends Component{

  static propTypes = {
    placeholder:PropTypes.string,
    value:PropTypes.any,
    values:PropTypes.array.isRequired,
    labels:PropTypes.array.isRequired,
  }

  constructor(props) {
    super(props);
    this.state = {value:props.value};
  }
  componentWillReceiveProps(nextProps) {
    if(nextProps.value!== undefined && nextProps.value!==this.state.value){
      this.setState({value:nextProps.value});
    }
  }

  getValue(){
    return this.state.value;
  }

  onChange=(index,value,label)=>{
    console.log(index,value,label);
    this.setState({value});
    this.props.onChange && this.props.onChange(index,value,label);
  }

  render(){
    let index = findGetIndex(this.props.values,this.state.value);
    console.log(index);
    return (
      <Select
        style={[styles.formItem,this.props.style]}
        placeholder={this.props.placeholder}
        onChange={this.onChange}
        value={this.state.value}
        values={this.props.values}
        labels={this.props.labels}>
        <Text style={[styles.label,this.props.labelStyle]}>{this.props.label}</Text>
        <Text style={index>=0?[styles.holder,styles.holderDark]:styles.holder}>{index>=0?this.props.labels[index]:this.props.placeholder}</Text>
        {this.props.renderImage ? this.props.renderImage():renderImage()}
      </Select>
    );
  }

}


export class FormSwitch extends Component{

   static propTypes = {
    onChange:PropTypes.func,
    value:PropTypes.bool, //默认值
    label:PropTypes.string.isRequired,    //label
  }

  render(){
    return (
       <View style={[styles.formItem,styles.switch,this.props.style]}>
          <Text style={this.props.textstyle}>{this.props.label}</Text>
          <Switch onValueChange={this.props.onChange} value={this.props.value} />
        </View>
    );
  }
}


export class FormImage extends Component{

  static propTypes = {
    imageHolder: React.PropTypes.any,
    imageStyle:React.PropTypes.any,
    style:React.PropTypes.any,
  }


  constructor(props) {
    super(props);
    this.state = {value:props.value};
    this.file = true;
  }

  getValue=()=>{
    let value = this.state.value;
    if(!value)return null;
    if(value.startsWith("http")){
      return null;
    }
    return value.replace("file://","");
  }

  // onChange=(uri)=>{
  //   this.setState({uri});
  // }
  componentWillReceiveProps(nextProps) {
    if(nextProps.value && nextProps.value!==this.state.value){
      this.setState({value:nextProps.value});
    }
  }
  onChange=(uri)=>{
    console.log(uri);
    this.setState({value:uri});
    this.props.onChange && this.props.onChange(uri);
  }

  render(){
    console.log(this.state.value);
    return (
      <View style={this.props.style}>
          <Text style={styles.imageLabel}>{this.props.label}</Text>
          <ImagePicker
            album={this.props.album}
            style={styles.imagePicker}
            onChange={this.onChange}>
             <Image
               source={this.state.value?{uri:this.state.value}:this.props.imageHolder}
               style={this.props.imageStyle} />
           </ImagePicker>
      </View>
    );
  }
}

/**
配置
*/
/*
{items:[{name,label,placeHolder,comp},function],
  //外部的style
style:
*/

export default class TemplateForm extends Component{
	render(){
		return (
      <View style={this.props.style}>

      </View>
    );
	}
}



const styles = StyleSheet.create({
  imageLabel:{alignSelf:'center', marginBottom:10, color:'#a99268',},
   imagePicker:{
      alignSelf:'center',
      flexDirection:'row',
      justifyContent:'space-around',
    },
  label:{
    color:'#7F4E20',
    fontSize:15
  },
  rightArrow:{
    width:7,height:12
  },
	container:{

  },
  holder:{
    fontSize:15,
    alignItems:'center',
    marginLeft:3,
    flex:1,
    color:'#ccc'
  },
  holderDark:{
    color:'#333'
  },
  button:{
    margin:10
  },
  text:{marginLeft:5,flex:1,fontSize:15},
  input:{marginLeft:5,height:40,flex:1,fontSize:15},
  switch:{justifyContent:'space-between'},
  formItem:{
    marginTop:5,
    backgroundColor:'#ffffff',
    height:40,
    paddingRight:10,
    paddingLeft:10,
    alignItems:'center',
    flexDirection:'row'
  }

});
