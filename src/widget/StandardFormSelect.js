import React,{Component,PropTypes} from 'react'
import {TouchableOpacity,TextInput,View,Text,Image,StyleSheet,Switch,Select,ImagePicker,
  Dimensions,PixelRatio}
from  '../../lib/Common'
import {findGetIndex} from '../../lib/ArrayUtil'
// import {lineView} from './realNameUtils'
const SCREEN_WIDTH = Dimensions.get('window').width;

const renderImage = ()=>{
    return   <Image style={styles.rightArrow} source={require('../img/ic_arrow.png')} />
};
export default class FormSelect extends Component{

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
      <View>
        {this.props.hasHead && <View style={styles.viewStyle}></View>}
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
        {this.props.hasBottom && <View style={styles.viewStyle}></View>}
      </View>
    );
  }

}
const styles = StyleSheet.create({
  formItem:{
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
    // marginRight:10,
  },
  holder:{
    fontSize:16,
    alignItems:'center',
    marginLeft:5,

    flex:1,
    color:'#ccc'
  },
  holderDark:{
    color:'#333',
    marginLeft:5
  },
  rightArrow:{
    width:7,height:12
  },
  borderBottom:{
    borderBottomWidth:1,
    borderBottomColor:'#d7d7d7'
  },
  //分割线
  viewStyle:{
		height:1/PixelRatio.get(),
		backgroundColor:'#d7d7d7',
		marginLeft:5,
	},
})
