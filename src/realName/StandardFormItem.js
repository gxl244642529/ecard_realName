import React,{Component,PropTypes} from 'react'
import {TouchableOpacity,TextInput,View,Text,Image,StyleSheet,Switch,Select,ImagePicker} from '../../lib/Common'

import {findGetIndex} from '../../lib/ArrayUtil'
import {lineView} from './realNameUtils'

export default class FormItem extends Component{

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
    let {style,value,...otherProp} = props;
    return (
      <View>
      {this.props.hasHead&&lineView()}
      <Comp
        {...otherProp}
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
      {this.props.hasBottom&&lineView()}
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
    // color:'#7F4E20',
    // fontSize:15
    color:'#595757',
    fontSize:16,
    lineHeight:16,
  },
  holder:{
    fontSize:16,
    alignItems:'center',
    // marginLeft:10,
    flex:1,
    color:'#ccc'
  },
  holderDark:{
    color:'#333',
  },
  rightArrow:{
    width:7,height:12
  },


})
