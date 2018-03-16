
import React, { Component, } from 'react';
import {
  StyleSheet,
  View,
  Text,
  TouchableOpacity,
  Image
} from '../../lib/Common';



export default class StarButton extends Component{
  constructor(props){
    super(props);
    this.state={
      isSelect:props.isSelect,
      num:props.num,
      score:0,
    }

  }


  // constructor(props) {
  //   super(props);
  //   this.state={imageList:props.imageList};
  //   this.imageDataArray = {};
  // }

  componentWillReceiveProps(nextProps){
    if(nextProps.isSelect !== this.state.isSelect){
      this.setState({isSelect:nextProps.isSelect});
    }
  }



  _select=()=>{

    this.setState({isSelect:true,score:1});
    let state = true;
    console.log("_select="+this.state.isSelect);
    this.props.updateScore(state,this.state.num);
  }
  _cancleSelect=()=>{
      this.setState({isSelect:false,score:0});
      let state = false;
      // this.form.state.phone
      // this.form.setState({isSelect:false});
      console.log("设置为false,isselect的变化"+this.state.isSelect+"state="+state);
      // this.props.updateScore(state);
      this.props.updateCanScore(state,this.state.num);
  }

  _renderStar(){

    return(
      <TouchableOpacity onPress={this._select}>
        <Image style={this.props.imageStyle} source={require('./images/star_2.png')}/>
      </TouchableOpacity>
    );

  }
  _renderSeStar(){
    console.log("isselect="+this.state.isSelect);

    return(
      <TouchableOpacity onPress={this._cancleSelect}>
        <Image style={this.props.imageStyle} source={require('./images/star_1.png')}/>
      </TouchableOpacity>
    );

  }
  render(){
    return(
      <View style={this.props.style}>
      {  this.state.isSelect?this._renderSeStar():this._renderStar()}
      </View>

    );
  }

}
