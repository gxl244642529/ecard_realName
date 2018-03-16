import{
  Api,
  Account
}from '../../lib/Common';


const CommonApi={
  //商品详情页：获取商品详情
  _getGoodsDetail:function(data,callback){
    Api.api({
      api:'bgoods/detail',
      data:data,
      success:(data)=>{
        callback(data);
      }
    })
  },
  //商品详情页：是否收藏
  _getGoodsIsCol:function(data,callback){
    // if(!Account.isLogin()){
    //   return;
    // }
    Api.api({
      api:'bgoods/isCol',
      data:data,
      success:(result)=>{
        callback(result);
      }
    })
  },

  //商户详情页：是否收藏
  _getShopIsCol:function(data,callback){

    Api.api({
      api:'bshop/isCol',
      data:data,
      success:(data)=>{
        console.log("商户是否被收藏="+data);
        callback(data);
      }
    })
  },
  //商户详情页：获取商户详情
  _getShopDetail:function(data,callback){
    Api.api({
      api:'bshop/detail',
      data:data,
      success:(data)=>{
        console.log(data);
        callback(data);
        //console.info(self.state.images);
      }
    })
  },
  //商户详情页：获取是否是会员
  _getShopIsVip:function(data,callback){

    Api.api({
      api:'member/isMember',
      data:data,
      success:(data)=>{
        callback(data);
      }
    })
  }
}
module.exports=CommonApi;
