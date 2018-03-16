import keyMirror from 'key-mirror';

export default keyMirror({
	/*
	登录
	*/
	LOGIN_REQUEST:null, //登录请求
  LOAD_USER:null,     //加载用户
  SET_ACCOUNT:null,   //设置用户信息（将读取到的用户信息设置到store)
  /**
  报修
  */
  REPAIR_LOAD_RECENT:null,
  REPAIR_LOAD_RECENT_COMPLETE:null,
  REPAIR_LOAD_RECENT_ERROR:null,

  //详情
  REPAIR_ORDER_COMPLETE:null,
  REPAIR_ORDER_SUBMING:null,


  REPAIR_FINISH: null,
  REPAIR_SUBMIT: null,
  /**
  推送有关的
  */

  
  PUSH_DELIVERY: null
});
