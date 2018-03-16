
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
  ScrollView,
  TouchableAll
} from '../../lib/Common';



import {isLogin,loginSuccess,onRequireLoginPress} from '../../lib/LoginUtil'
import Notifier from '../../lib/Notifier'


export default class NoticeIcon extends Component{

  constructor(props){
    super(props);
    this.state={msg:0};
    this._msgCenter = onRequireLoginPress(this._msgCenter);
  }

  /*
 componentDidMount() {
    Notifier.addObserver("verify",this._loadRecent);
  }

  componentWillUnmount() {
     Notifier.removeObserver("verify",this._loadRecent);
  }*/

  /**
   * 加载
   * @return {[type]} [description]
   */
  _refresh=()=>{
    Api.api({
      api:'mc/count',
      success:this._onMcCount
    });
  }

  _onMcCount=(msg)=>{
    this.setState({msg})
  }

  componentDidMount() {
    Notifier.addObserver(loginSuccess,this._refresh);
    Notifier.addObserver('mc/setReaded',this._refresh);

    if(isLogin()){
      this._refresh();
    }
  }

  componentWillUnmount() {
     Notifier.removeObserver(loginSuccess,this._refresh);
     Notifier.removeObserver('mc/setReaded',this._refresh);
  }

  

  _msgCenter=()=>{
     Api.push('/msgenter');
  }

  render(){
    let icon = this.state.msg > 0 ? 
    (
       <View style={styles.iconContainer}>
        <Text style={styles.iconText}>{this.state.msg}</Text>
      </View>
    ) : null;

    return (
      <TouchableOpacity onPress={this._msgCenter} style={styles.button}>
        <Image style={styles.bell} source={ this.props.bell } />
        {icon}
      </TouchableOpacity>
    );

  }

}


const styles = StyleSheet.create({
  button:{width:40,height:40,justifyContent:'center',alignItems:'center'},
  bell:{width:19,height:24},
  iconText:{fontSize:8,color:'#fff'},
  iconContainer:{ position:'absolute',top:4,right:0, width:15,height:15,backgroundColor:'#f00',borderRadius:10,justifyContent:'center',alignItems:'center'},
});