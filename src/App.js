import React, { Component } from 'react';
import {  Router, IndexRoute, Route } from 'react-router';
import {createRouter,history} from '../lib/Config'
import {Api} from '../lib/Common'
import Notifier from '../lib/Notifier'
import PushUtil,{openMessage} from '../lib/PushUtil'
import NfcUtil from '../lib/NfcUtil'
import {loginSuccess,logoutSuccess} from '../lib/LoginUtil'
import Navigator from './lib/Navigator'
import {DeviceEventEmitter} from 'react-native'
import {headChanged} from './lib/Notes'
import DateUtil from './lib/DateUtil'

import MapUtil from './lib/MapUtil'
//========================================================

import _Main from './_home'
import QrUtils from './busqr/QrUtils'

//========================================================
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

//========================================================
//========================================================
import MessageSubmit from './realName/MessageSubmit'
import CheckSubmit from './realName/CheckSubmit'
import BankMessage from './realName/BankMessage'
import BankCheck from './realName/BankCheck'
import RealInfo from './realName/RealInfo'
import RealLead from './realName/RealLead'
import VerifyProcess from './realName/VerifyProcess'
import OverTrans from './realName/OverTrans'
import VerifyProcessFail from './realName/VerifyProcessFail'
import RCardList from './realName/RCardList'
import RCardMessage from './realName/RCardMessage'
import RCardMessageSubmit from './realName/RCardMessageSubmit'
import RCard from './realName/RCard'
import RCardNoReal from './realName/RCardNoReal'
import RCardLostList from './realName/RCardLostList'
import RCardDetail from './realName/RCardDetail'
import RCardLost from './realName/RCardLost'
import RCardLostDetail from './realName/RCardLostDetail'
import PCMessage from './realName/PCMessage'
import RCardChecking from './realName/RCardChecking'
import LetterAgreeVerify from './realName/LetterAgreeVerify'

import NoRealMessage from './realName/NoRealMessage'
import QuestionVertify from './realName/QuestionVertify'
import BindingWays from './realName/BindingWays'
import NFCVertify from './realName/NFCVertify'
import OtherWay from './realName/OtherWay'
import OpenLost from './realName/OpenLost'
import OpenLostConfirm from './realName/OpenLostConfirm'
import NewMessageSubmit from './realName/NewMessageSubmit'

import NetpotMain from './netpot/NetpotMain'
import NetpotView from './netpot/NetpotView'
// import QrRequest from './busqr/QrRequest'
// import QrRequestVerify from './busqr/QrRequestVerify'
//========================================================
//========================================================
//const hisMidware = routerMiddleware(history)
//
//
//
 import QrMain from './busqr/QrMain'
 import QrAccount from './busqr/QrAccount'
 import QrInitPwd from './busqr/QrInitPwd'
 import QrRequest from './busqr/QrRequest'
 import QrSetting from './busqr/QrSetting'
 import QrResetPwd from './busqr/QrResetPwd'
 import QrUpdatePwd from './busqr/QrUpdatePwd'
 import QrAccActive from './busqr/QrAccActive'
 import QrChage from './busqr/QrChage'
 import QrChargeResult from './busqr/QrChargeResult'
 import QrTrans from './busqr/QrTrans'
 import QrRequestVerify from './busqr/QrRequestVerify'
 import QrOutFund from './busqr/QrOutFund'
 import QrFundProgress from './busqr/QrFundProgress'
 import QrHelp from './busqr/QrHelp'
 import QrBusRecord from './busqr/QrBusRecord'
 import QrRequestSuccess from './busqr/QrRequestSuccess'
 import QrForgetPass from './busqr/QrForgetPass'
 import QrAccStatus from './busqr/QrAccStatus'
 import QrReturnUrl from './busqr/QrReturnUrl'
 import QrOutFundFirStep from './busqr/QrOutFundFirStep'
 import QrIntroduce from './busqr/QrIntroduce'
 import QrChargeRefund from './busqr/QrChargeRefund'


//=================================================平安=====
import PinganMain from './pingan/PinganMain'

//========================================================

//-------------------------------------------天安理财
import Zhongchengmain from './zhongcheng/Zhongchengmain'


import PersonalCenter from './personal/PersonalCenter'
import MessageCenter from './_home/MessageCenter'


import RechargeMain from './recharge/RechargeMain'
import RechargeSuccess from './recharge/RechargeSuccess'
import RechargeRefund from './recharge/RechargeRefund'
import Ticketmain from './ticket/Ticketmain'
//----------------------------电子发票
import TicketUrl from './ticket/TicketUrl'
//personal/center
/////////

//-----------------------------lvsechuxing
import GreenTravel from './greentravel/GreentTravelMain'
//Api.imageUrl = "http://192.168.1.241";
//Api.imageUrl = "http://218.5.80.17:8092";

/****我的e通卡***/
import MyEcardList from './myecard/MyEcardList'











export default class App extends Component {
  constructor(props){
    super(props);
    if(props.json){
      Account.user = JSON.parse(props.json);
    }
    Api.isFirst = props.isFirst;
    Api.nfc = this.props.nfc;
    Api.imageUrl = props.imageUrl;
    Api.version= props.version;
    this.subscription = DeviceEventEmitter.addListener(loginSuccess, this.onLoginSuccess);
    this.logout = DeviceEventEmitter.addListener(logoutSuccess, this.onLogoutSuccess);
    this.headChanged = DeviceEventEmitter.addListener(headChanged, this.headChanged);
    this.onPush = DeviceEventEmitter.addListener("onPush", this.onPush);
    this.nfcTag = DeviceEventEmitter.addListener("nfcTag", this.nfcTag);
    this.gotoRealCard = DeviceEventEmitter.addListener("gotoRealCard", this.gotoRealCard);
    MapUtil.getPos(this.onGetPos);
  }

  onGetPos=(pos)=>{
    Api.pos=  pos;
  }

  gotoRealCard=(card)=>{
    if(card.createDate){
      //卡详情
      Api.push('/realName/rCardDetail/'+card.cardId);

    }else{
      if(card.real){
          //去绑卡
          // Api.push('/realName/rCardMessage');
          //是否开启信用承诺书

          Api.api({
            api:"newRcard/isAgree",
            success:this._success,
          })

      }else{
          //去实名
          Api.push('/realName/overTrans');
      }
    }

  }
  _success=(result)=>{
    console.log("isAgree="+result)
    let FROM_REAL_CARD = 1;
    let FINISH = 88;
    let isAgreeData = {fromto:FROM_REAL_CARD,isReal:FINISH};
    if(result){
      Api.push('/realName/openLostConfirm/'+JSON.stringify(isAgreeData));
    }else {
      Api.push('/realName/openLost/'+JSON.stringify(isAgreeData));
    }
  }



  componentWillUnmount(){
    if(this.subscription){
       this.subscription.remove();
      this.subscription = null;
    }
    if(this.logout){
      this.logout.remove();
      this.logout = null;
    }
    if(this.headChanged){
      this.headChanged.remove();
      this.headChanged=null;
    }
    if(this.onPush){
      this.onPush.remove();
      this.onPush = null;
    }
    if(this.nfcTag){
      this.nfcTag.remove();
      this.nfcTag = null;
    }
    if(this.gotoRealCard){
      this.gotoRealCard.remove();
      this.gotoRealCard = null;
    }
  }

  nfcTag=()=>{
    if(!Notifier.notifyObservers('nfcTag')){
      NfcUtil.handleSelf();
    }
  }

  onPush=(data)=>{
    console.log("正在调用onPush方法")
    Notifier.notifyObservers("onPush");
    openMessage(data,true);
  }

  headChanged=(data)=>{
    if(Account.user){
       Account.user.head = data.url;
       Notifier.notifyObservers(headChanged,data.url);
    }
  }

  onLoginSuccess=(data)=>{
    let json = data.json;
      Account.user = JSON.parse(json);
      Notifier.notifyObservers(loginSuccess,Account.user);
  }

  onLogoutSuccess=(data)=>{
    Account.user = null;
    QrUtils.clear();
    QrUtils.disableToken();
    Notifier.notifyObservers(logoutSuccess,null);
  }
  // <Route path='/realName/bankMessage/:error' component={BankMessage}/>

  render() {
    return (
      <Router history={history}>
          <Route path='/' component={createRouter()}>
            <IndexRoute component={_Main} />
            <Route path='/realName/_main' component={_Main}/>
            <Route path='/realName/messageSubmit' component={MessageSubmit}/>
            <Route path='/realName/checkSubmit/:json' component={CheckSubmit}/>

            <Route path='/realName/bankMessage/:json' component={BankMessage}/>
            <Route path='/realName/realInfo' component={RealInfo}/>
            <Route path='/realName/realLead'component={RealLead}/>
            <Route path='/realName/verifyProcess' component={VerifyProcess}/>
            <Route path='/realName/verifyProcessFail' component={VerifyProcessFail}/>
            <Route path='/realName/overTrans' component={OverTrans}/>
            <Route path='/realName/bankCheck'component={BankCheck}/>
            <Route path='/realName/rCardList' component={RCardList}/>
            <Route path='/realName/rCardMessage' component={RCardMessage}/>
            <Route path='/realName/rCardMessageSubmit/:json' component={RCardMessageSubmit}/>
            <Route path='/realName/rCardNoReal' component={RCardNoReal}/>
            <Route path='/realName/rCard' component={RCard}/>
            <Route path='/realName/rCardLostList' component={RCardLostList}/>
            <Route path='/realName/rCardDetail/:id' component={RCardDetail}/>
            <Route path='/realName/rCardLost/:id' component={RCardLost}/>
            <Route path='/realName/rCardLostDetail/:id' component={RCardLostDetail}/>
            <Route path='/realName/pCMessage' component={PCMessage}/>

            <Route path='/realName/noRealMessage/:json' component={NoRealMessage} />
            <Route path='/realName/questionVertify/:json' component={QuestionVertify}/>
            <Route path="/realName/bindingWays/:id" component={BindingWays}/>
            <Route path="/realName/nfcVertify/:json" component={NFCVertify}/>
			      <Route path="/realName/rCardChecking/:id" component={RCardChecking}/>
            <Route path="/realName/otherWay/:json" component={OtherWay}/>
            <Route path="/realName/openLost/:json" component={OpenLost}/>
            {/****fromto:0表示实名化流程 fromto:1表示实名绑卡流程****/}
            <Route path='/realName/openLostConfirm/:json' component={OpenLostConfirm}/>
            <Route path='/realName/newMessageSubmit' component={NewMessageSubmit}/>
            <Route path='/realName/letterAgreeVerify/:id' component={LetterAgreeVerify}/>


            <Route path='/busqr/qrRequest' component={QrRequest}/>
            <Route path='/busqr/qrRequestVerify/:json' component={QrRequestVerify}/>


            {/****************个人中心******/}
            <Route path='/personal/center' component={PersonalCenter} />
              {/****************消息中心******/}
               <Route path='/msgenter' component={MessageCenter} />

               {/******扫码*****/}
               <Route path='/busqr/account' component={QrAccount} />
               <Route path='/busqr/init_pwd' component={QrInitPwd} />
               <Route path='/busqr/request' component={QrRequest} />
               <Route path='/busqr/setting' component={QrSetting} />
               <Route path='/busqr/reset_pwd' component={QrResetPwd} />
               <Route path='/busqr/update_pwd' component={QrUpdatePwd} />
               <Route path='/busqr/acc_active' component={QrAccActive} />
               <Route path='/busqr/chage' component={QrChage} />
               <Route path='/busqr/charge_result/:orderId' component={QrChargeResult} />
               <Route path='/busqr/trans' component={QrTrans} />
               <Route path='/busqr/request_verify' component={QrRequestVerify} />
               <Route path='/busqr/out_fund' component={QrOutFund} />
               <Route path='/busqr/fund_progress' component={QrFundProgress} />
               <Route path='/busqr/help' component={QrHelp} />
               <Route path='/busqr/bus_record' component={QrBusRecord} />
               <Route path='/busqr/request_success' component={QrRequestSuccess} />
               <Route path='/busqr/forget_pass' component={QrForgetPass} />
               <Route path='/busqr/qrAccStatus/:status' component={QrAccStatus}/>
               <Route path='/busqr/qrReturnUrl/:status' component={QrReturnUrl}/>
               <Route path='/busqr/qrOutFundFirStep' component={QrOutFundFirStep}/>
               <Route path='/busqr/qrChargeRefund' component={QrChargeRefund}/>

             {/**吥噔**/}
             <Route path='/recharge' component={RechargeMain} />
             <Route path='/recharge/success/:orderId' component={RechargeSuccess} />
             <Route path='/recharge/refundSuccess' component={RechargeRefund} />
            {/****************优惠卡******/}
            <Route path='/discard/info' component={DiscardInfo} />
            <Route path='/discard/buy/:json' component={DiscardBuy} />
            <Route path='/exam/info' component={ExamInfo} />
            <Route path='/discard/main' component={DiscardMain} />

          {/****************平安******/}
         <Route path='/pingan' component={PinganMain} />
          {/****************公交扫码******/}
          <Route path='/busqr/main' component={QrMain} />

        {/****************网点查询******/}
          <Route path='/netpot' component={NetpotMain} />
          <Route path='/netpot/view/:json' component={NetpotView} />
          {/****************商城******/}
            <Route path='/shopUser/main' component={ShopUserMain}/>
            <Route path='/shopUser/shopUserMain' component={ShopUserMain}/>
            <Route path='/shopUser/ecshop' component={Ecshop}/>
            <Route path='/shopUser/goodsDetail/:id' component={GoodsDetail} />
            <Route path='/shopUser/shopDetail/:id' component={ShopDetail}/>
            <Route path='/shopUser/search' component={Search}/>
            <Route path='/shopUser/searchResult/:json' component={SearchResult}/>
            <Route path='/shopUser/collectionMain' component={CollectionMain}/>
            <Route path='/shopUser/pCenter' component={PCenter}/>
            <Route path='/shopUser/joinvip/:id/:title' component={JoinVip}/>
            <Route path='/shopUser/evaluate/:id/:title/:thumb' component={Evaluate}/>
            <Route path='/shopUser/myVipcard' component={MyVipcard}/>
            <Route path='/shopUser/myVipdetail/:id' component={MyVipdetail}/>
            <Route path='/shopUser/myCoupon' component={MyCoupon}/>
            <Route path='/shopUser/historyCoupon' component={HistoryCoupon}/>
            <Route path='/shopUser/myCouponDetails/:id' component={MyCouponDetails}/>
            <Route  path='/shopUser/moreshop/:id' component={MoreShoppic}/>
            <Route  path='/shopUser/moregoods/:id/:src' component={MoreGoodspic}/>
            <Route path='/shopUser/getCoupon/:id' component={GetCoupon}/>
            <Route  path='/shopUser/MoreShoppic/:id/:src' component={MoreShoppic}/>
            <Route path='/shopUser/myCouponHistory' component={MyCouponHistory}/>



               <Route path='/ticket' component={Ticketmain} />
            <Route path='/ticketurl' component={TicketUrl}/>

            {/*****************天安理财*********************/}
          <Route path='/zhongcheng' component={Zhongchengmain}/>
        {/***********************绿色出行**********************/}
        <Route path="/greentravel" component={GreenTravel}/>
        {/***********************我的e通卡******************************/}
        <Route path="/myecardlist" component={MyEcardList}/>



          </Route>
        </Router>
    );
  }
}
