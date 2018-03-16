import React, { Component } from 'react';
import {
  View,
  ActivityIndicator,
  StyleSheet,
  CommonStyle,
  Account,
  Api,
  A,
  Text
} from '../../lib/Common'
// import {login} from '../lib/LoginUtil'
import Notifier from '../../lib/Notifier'

import RealInfo from './RealInfo'
import RealLead from './RealLead'
import BankMessage from './BankMessage'
import MessageSubmit from './MessageSubmit'
import VerifyProcess from './VerifyProcess'
import RCardNoReal from './RCardNoReal'
import RCardList from './RCardList'



export default class RCard extends Component{

  constructor(props){
    super(props);
    this.state={};
  }

  componentDidMount() {
    // Notifier.addObserver("verify",this._loadRecent);
    // this._loadRecent();
  }

  componentWillUnmount() {
    //  Notifier.removeObserver("verify",this._loadRecent);
  }



  _loadRecent=()=>{
    Api.api({
      api:"rcard/list",
      crypt:0,
      waitingMessage:"",
      success:(result)=>{
        this.setState({result});
      }
    });
  }
  // <View style={[CommonStyle.container,styles.content]}>
  //   <ActivityIndicator />
  // </View>
  render(){
    let data = this.state;
      return (
        <View style={[CommonStyle.container,styles.content]}>
          {data.valid===2 &&  <RecardNoReal />}
          {data.valid!=2 &&  <RCardList />}
        </View>
      );

    }
}

const styles = StyleSheet.create({
  // content:{justifyContent:'center',alignItems:'center'}
});
