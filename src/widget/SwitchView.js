
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
  Account,
  ActivityIndicator,
  CommonStyle,
  PixelRatio,
  LocalData,
  Log,
  ScrollView
} from '../../lib/Common';

import {
  TitleBar,
} from '../Global'

const greenColor = '#2bb232';
const white = '#fff'
const width= 1;
const nowidth=0;
export default class SwitchView extends Component{
	constructor(props){
		super(props);
		this.state={
    };
	}
  componentDidMount(){
      this.setState({switchTrue:this.props.switchTrue,});
  }
  // shouldComponentUpdate(nextProps, nextState) {
  //   console.log("nextProps="+nextProps+"  nextState="+nextState);
  //    return nextProps.switchTrue === this.props.switchTrue;
  // }

  _switch=()=>{
    let self = this.props;
    let isopen ;
    if (!self.switchTrue) {
      isopen=true;
    }else {
      isopen=false;
    }
    this.props.onChange(isopen);

  }

	render(){
  // console.log( this.state.switchTrue);
  // console.log(this.props.switchTrue);
			return (
				<View style={this.props.switchTrue?[styles.swithView,styles.havebackgroud]:[styles.swithView,styles.nobackgroud]}>
          <TouchableOpacity style={this.props.switchTrue?[styles.radiu,styles.noradiunobord]:[styles.radiu,styles.radiunobord]}
           onPress={this._switch}>
          </TouchableOpacity>
        </View>

			);
	}
}


const styles = StyleSheet.create({
	container:{flex:1},

  radiu:{
    height:27,
    width:27,
    backgroundColor:'#fff',
    borderRadius:15,
    marginRight:1,

    borderColor:'#d7d7d7'
  },
  swithView:{
    height:30,
    width:50,
    borderRadius:20,
    justifyContent:'center',

    borderColor:'#d7d7d7'
  },
  nobackgroud:{
    alignItems:'flex-start',
    backgroundColor:white,
    borderWidth:width,
  },
  havebackgroud:{
    alignItems:'flex-end',
    backgroundColor:greenColor,
    borderWidth:nowidth,
  },
  radiunobord:{
    borderWidth:width,
  },
  noradiunobord:{
    borderWidth:nowidth,
  }


});
