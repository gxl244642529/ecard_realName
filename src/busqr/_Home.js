

import React, { Component } from 'react';
import { createStore, combineReducers, applyMiddleware } from 'redux'
import thunk from 'redux-thunk';
import {  Router, IndexRoute, Route } from 'react-router';

import { Provider } from 'react-redux'
import { syncHistoryWithStore, routerMiddleware} from 'react-router-redux'
import {createRouter,history} from '../../lib/Config'
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


import rootReducer from '../reducers';
import {DeviceEventEmitter} from 'react-native'

       import QrMain from './QrMain'         
       import QrAccount from './QrAccount'         
       import QrInitPwd from './QrInitPwd'         
       import QrRequest from './QrRequest'         
       import QrSetting from './QrSetting'         
       import QrResetPwd from './QrResetPwd'         
       import QrUpdatePwd from './QrUpdatePwd'         
       import QrAccActive from './QrAccActive'         
       import QrChage from './QrChage'         
       import QrChargeResult from './QrChargeResult'         
       import QrTrans from './QrTrans'         
       import QrRequestVerify from './QrRequestVerify'         
       import QrOutFund from './QrOutFund'         
       import QrFundProgress from './QrFundProgress'         
       import QrHelp from './QrHelp'         
       import QrBusRecord from './QrBusRecord'         
       import QrRequestSuccess from './QrRequestSuccess'         
       import QrForgetPass from './QrForgetPass'         

       import QrAccStatus from './QrAccStatus'


//const hisMidware = routerMiddleware(history)

const createStoreWithMiddleware = applyMiddleware(
 thunk//,hisMidware
)(createStore);


const store = createStoreWithMiddleware(
  rootReducer
)
Api.store = store;
Api.imageUrl = "http://192.168.1.241";
Api.imageUrl = "http://218.5.80.17:8092";
//store.subscribe(()=>{console.log(store.getState())});

//const realHistory = syncHistoryWithStore(history, store)

Api.main = '/shopUser/ShopUserMain';



//import {requireAuth} from '../lib/LoginUtil'
//
//
//


const requireAuth = ()=>{


  return true;
}


const ListItem = (props)=>{
   return (
      <TouchableOpacity style={{padding:10, flexDirection:'row', justifyContent:'space-between'}} 
        onPress={()=>{Api.push(props.url)}}>
        <Text>{props.title}</Text>
        <Text>></Text>
      </TouchableOpacity>
    );
}

class _Home extends Component{

  render(){
      return (
        <View style={CommonStyle.container}>
          <TitleBar title="标题" />
          <ScrollView style={styles.container}>
 				<ListItem title="QrMain" url="/busqr/main"/>      
 				<ListItem title="QrAccount" url="/busqr/account"/>      
 				<ListItem title="QrInitPwd" url="/busqr/init_pwd"/>      
 				<ListItem title="QrRequest" url="/busqr/request"/>      
 				<ListItem title="QrSetting" url="/busqr/setting"/>      
 				<ListItem title="QrResetPwd" url="/busqr/reset_pwd"/>      
 				<ListItem title="QrUpdatePwd" url="/busqr/update_pwd"/>      
 				<ListItem title="QrAccActive" url="/busqr/acc_active"/>      
 				<ListItem title="QrChage" url="/busqr/chage"/>      
 				<ListItem title="QrChargeResult" url="/busqr/charge_result"/>      
 				<ListItem title="QrTrans" url="/busqr/trans"/>      
 				<ListItem title="QrRequestVerify" url="/busqr/request_verify"/>      
 				<ListItem title="QrOutFund" url="/busqr/out_fund"/>      
 				<ListItem title="QrFundProgress" url="/busqr/fund_progress"/>      
 				<ListItem title="QrHelp" url="/busqr/help"/>      
 				<ListItem title="QrBusRecord" url="/busqr/bus_record"/>      
 				<ListItem title="QrRequestSuccess" url="/busqr/request_success"/>      
 				<ListItem title="QrForgetPass" url="/busqr/forget_pass"/>     

         <ListItem title="QrStatus/0" url="/busqr/accstatus/0"/>
         <ListItem title="QrStatus/1" url="/busqr/accstatus/1"/>
         <ListItem title="QrStatus/2" url="/busqr/accstatus/2"/>    
          </ScrollView>
        </View>
      );
  }
}

export default class App extends Component {

  componentDidMount() {
    console.log("===============StartAndroid==========\n===============StartAndroid==========\n===============StartAndroid==========",this.props);
    //Account.user = {...this.props};
    if(this.props.id){
      Account.user = {...this.props};
    }
     this.subscription = DeviceEventEmitter.addListener('loginSuccess', this.onLoginSuccess);
  }

  componentWillUnmount(){
    this.subscription.remove();
  }

  onLoginSuccess=(data)=>{
      Account.user = data;
  }

  render() {
    return (
      <Provider store={store}>
        <Router history={history}>
          <Route path='/' component={createRouter()}>
            <IndexRoute component={_Home} />

            <Route path='/busqr/main' component={QrMain} />         
            <Route path='/busqr/account' component={QrAccount} />         
            <Route path='/busqr/init_pwd' component={QrInitPwd} />         
            <Route path='/busqr/request' component={QrRequest} />         
            <Route path='/busqr/setting' component={QrSetting} />         
            <Route path='/busqr/reset_pwd' component={QrResetPwd} />         
            <Route path='/busqr/update_pwd' component={QrUpdatePwd} />         
            <Route path='/busqr/acc_active' component={QrAccActive} />         
            <Route path='/busqr/chage' component={QrChage} />         
            <Route path='/busqr/charge_result' component={QrChargeResult} />         
            <Route path='/busqr/trans' component={QrTrans} />         
            <Route path='/busqr/request_verify' component={QrRequestVerify} />         
            <Route path='/busqr/out_fund' component={QrOutFund} />         
            <Route path='/busqr/fund_progress' component={QrFundProgress} />         
            <Route path='/busqr/help' component={QrHelp} />         
            <Route path='/busqr/bus_record' component={QrBusRecord} />         
            <Route path='/busqr/request_success' component={QrRequestSuccess} />         
            <Route path='/busqr/forget_pass' component={QrForgetPass} />         
            
            <Route path="/busqr/accstatus/:status" component={QrAccStatus}/>
          </Route>
        </Router>
      </Provider>
    );
  }
}










const styles = StyleSheet.create({
	container:{flex:1}
});