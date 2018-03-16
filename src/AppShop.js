import React, { Component } from 'react';
import { createStore, combineReducers, applyMiddleware } from 'redux'
import thunk from 'redux-thunk';
import {  Router, IndexRoute, Route } from 'react-router';

import { Provider } from 'react-redux'
import { syncHistoryWithStore, routerMiddleware} from 'react-router-redux'
import {createRouter,history} from '../lib/Config'
import {Api} from '../lib/Common'

import rootReducer from './reducers';
import {DeviceEventEmitter} from 'react-native'
import Ecshop from './shopUser/Ecshop'
import GoodsDetail from './shopUser/GoodsDetail'
import ShopDetail from './shopUser/ShopDetail'
import Search from './shopUser/Search'
import SearchResult from './shopUser/SearchResult'
import ShopUserMain from './shopUser/ShopUserMain'
import CollectionMain from './shopUser/CollectionMain'
import PCenter from './shopUser/PCenter'

import JoinVip from './shopUser/JoinVip'
import MyVipcard from './shopUser/MyVipcard'
import MyVipdetail from './shopUser/MyVipdetail'
import Evaluate from './shopUser/Evaluate'
import Login from './Login'
import MyCoupon from './shopUser/MyCoupon'
import HistoryCoupon from './shopUser/HistoryCoupon'
import MyCouponDetails from './shopUser/MyCouponDetails'
import GetCoupon from './shopUser/GetCoupon'

import MoreShoppic from './shopUser/MoreShoppic'
import MoreGoodspic from './shopUser/MoreGoodspic'
import MyCouponHistory from './shopUser/MyCouponHistory'


import Account from '../lib/network/Account'

import DiscardInfo from './discard/DiscardInfo'
import DiscardBuy from './discard/DiscardBuy'
import DiscardMain from './discard/DiscardMain'
import ExamInfo from './exam/ExamInfo'

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
            <IndexRoute component={ShopUserMain} />
            <Route path='/discard/info' component={DiscardInfo} />
            <Route path='/discard/buy/:json' component={DiscardBuy} />
            <Route path='/exam/info' component={ExamInfo} />
            <Route path='/main' component={ShopUserMain}/>
            <Route path='/shopUser/shopUserMain' component={ShopUserMain}/>
            <Route path='/shopUser/ecshop' component={Ecshop}/>
            <Route path='/shopUser/goodsDetail/:id' component={GoodsDetail} />
            <Route path='/shopUser/shopDetail/:id' component={ShopDetail}/>
            <Route path='/shopUser/search' component={Search}/>
            <Route path='/shopUser/searchResult/:json' component={SearchResult}/>
            <Route onEnter={requireAuth}  path='/shopUser/collectionMain' component={CollectionMain}/>
            <Route path='/shopUser/pCenter' component={PCenter}/>
            <Route onEnter={requireAuth}  path='/shopUser/joinvip/:id/:title' component={JoinVip}/>
            <Route onEnter={requireAuth}  path='/shopUser/evaluate/:id/:title/:thumb' component={Evaluate}/>
            <Route onEnter={requireAuth}  path='/shopUser/myVipcard' component={MyVipcard}/>
            <Route onEnter={requireAuth}  path='/shopUser/myVipdetail/:id' component={MyVipdetail}/>
            <Route onEnter={requireAuth} path='/shopUser/myCoupon' component={MyCoupon}/>
            <Route onEnter={requireAuth}  path='/shopUser/historyCoupon' component={HistoryCoupon}/>
            <Route onEnter={requireAuth}  path='/shopUser/myCouponDetails/:id' component={MyCouponDetails}/>
            <Route  path='/shopUser/moreshop/:id' component={MoreShoppic}/>
            <Route  path='/shopUser/moregoods/:id/:src' component={MoreGoodspic}/>
            <Route onEnter={requireAuth}  path='/shopUser/getCoupon/:id' component={GetCoupon}/>
            <Route  path='/shopUser/MoreShoppic/:id/:src' component={MoreShoppic}/>
            <Route onEnter={requireAuth}  path='/shopUser/myCouponHistory' component={MyCouponHistory}/>
          </Route>
        </Router>
      </Provider>
    );
  }
}
