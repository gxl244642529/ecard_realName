
import React, { Component } from 'react';
import {
  AppRegistry,
  StyleSheet,
  Text,
  View,
  Image,
  TouchableOpacity,
  Dimensions,
  PixelRatio,
  A,
  Api,
  Platform
} from '../../lib/Common';

import {IC_Back,IC_XmarkSmall} from '../busqr/icons/Icons'


const screenWidth = Dimensions.get('window').width;


export default class TitleBarWebView extends Component{
  constructor(props) {
    super(props);
  }

  _onBack=()=>{
    if(this.props.onBack){
      this.props.onBack();
      return;
    }
    Api.goBack();
  }
  _onDelete=()=>{
      Api.goBack();
  }
  render(){
    return (
      <View style={styles.titleBar}>
         <View style={{flexDirection:'row',alignItems:'center'}}>
          {this.props.renderBack!==false&&<TouchableOpacity onPress={this._onBack} style={styles.backButton}>
                  <IC_Back/>
          </TouchableOpacity>}
          {this.props.renderDele!==false&&<TouchableOpacity onPress={this._onDelete} style={[styles.backButton,{marginTop:5}]}>
                  <IC_XmarkSmall/>
          </TouchableOpacity>}
        </View>
            <Text style={styles.titleText} numberOfLines={1}>{this.props.title}</Text>
            <View style={this.props.rightStyle}>{this.props.renderRight && this.props.renderRight()}</View>

      </View>
    );
	}

}


let paddingTop = Platform.OS=='ios' ? 20 : 0;
let height = Platform.OS=='ios' ? 65 : 45;
const styles = StyleSheet.create({

	backImage:{
		width:9,height:19
	},
	backButton:{
		height:45,width:40,justifyContent:'center',alignItems:'center'
	},
	titleText:{
		marginTop:15+paddingTop,//修改
		//color:'#7F4E20',//修改
    color:'#333',
		fontSize:15,
		textAlign:'center',
		position:'absolute',
		left:100,
		right:100
	},
	titleBar:{
    paddingTop:paddingTop,
		height:height,
		justifyContent:'space-between',
		flexDirection:'row',



	}
});
