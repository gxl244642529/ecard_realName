import React,{Component,PropTypes} from 'react'
import {TouchableOpacity,TextInput,View,Text,Image,StyleSheet,
  Switch,Select,ImagePicker,Dimensions,PixelRatio}
from '../../lib/Common'
const SCREEN_WIDTH = Dimensions.get('window').width;
const SCREEN_HEIGHT= Dimensions.get('window').height;
export default class FormInput extends Component{

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
    // console.log(value);
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
      <View>
      {this.props.hasHead && <View style={styles.viewStyle}></View>}
      <View style={[styles.formItem,props.style , styles.borderBottom]}>
        <Text style={styles.label}>{props.label}</Text>
        <TextInput
          {...txtProps}
          secureTextEntry={props.secureTextEntry}
          returnKeyType="done"
          numberOfLines={1}
          underlineColorAndroid={'#FEFEFE'}
          placeholder={props.placeholder}
          placeholderTextColor="#b3b3b4"
          onChangeText={this.onChange}
          style={this.props.inputStyle?props.inputStyle:styles.input}
          value={this.parseDisplayValue(props.value)}
          keyboardType={this.props.keyboardType}
          editable ={this.props.editable}
          />
      </View>
        {this.props.hasBottom &&<View style={styles.viewStyle}></View>}
    </View>
    );
  }
}
const styles = StyleSheet.create({
  formItem:{
    // marginTop:5,
    backgroundColor:'#ffffff',
    height:60,
    paddingRight:10,
    paddingLeft:15,
    alignItems:'center',
    flexDirection:'row',
    // borderTopWidth:1,
    // borderTopColor:'#d7d7d7',
  },
  label:{
    color:'#595757',
    fontSize:16,
    lineHeight:16,
  },
  input:{marginLeft:5,height:60,flex:1,fontSize:16},

  //分割线
  viewStyle:{
		height:1/PixelRatio.get(),
		backgroundColor:'#d7d7d7',
		marginLeft:5,
	},

})
