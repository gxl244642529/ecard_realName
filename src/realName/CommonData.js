import React,{Component} from 'react'

import{
  View,
  Text,
  Image,
  StyleSheet,
  Api,
  A,
  CommonStyle,
  TextInput,
  DatePicker,
  TouchableOpacity,
  Form,
  Dimensions,
  ScrollView
} from '../../lib/Common'


export const SORT_VALUES = [1,2,3,4];
export const SORT_LABELS = ['身份证','台胞证','港澳居民身份证','外籍护照'];

export const SCREEN_WIDTH = Dimensions.get('window').width;
export const SCREEN_HEIGHT= Dimensions.get('window').height;

export const BANKID_VALUES = [1,2,3,4,5,6,7];
export const BANKID_LABELS = ["工商银行" ,"农业银行", "中国银行", "建设银行", "交通银行" ,"招商银行","华夏银行"];

export const LIAN_ID = ["51301","51551","58001","58301","58651","58701","59052","59351","59551","59763","51651",
  "51952", "52364","52451","52541","53590","53151","53272","53605","53901","54001","54351","54751","55039","55351","55395",
  "55860","56888","56780","57151","56901","57901","57451","50001","50190","50202","50666","50701","51002"];
export const LIAN_NAME = ['辽宁分行(不含大连)','大连分行','贵州分行','云南分行','西藏分行',
' 陕西分行','甘肃分行','青海分行','宁夏分行','新疆分行','吉林分行','黑龙江分行','上海分行',
'江苏分行(不含苏州)','苏州分行','浙江分行(不含宁波)','宁波分行','安徽分行','福建分行(不含厦门)',
'厦门分行','江西分行','山东分行(不含青岛)','青岛分行','河南分行',' 湖北分行(不含三峡)','三峡分行',
'湖南分行','广东分行(不含深圳)','深圳分行',' 广西分行','海南分行','重庆分行','四川分行','总行营业部',
'北京分行','天津分行',' 河北分行','山西分行','内蒙古分行'];

export const BANK_CARD_FUNDING = 8;//实名中
export const   FINISH = 88; // 实名成功
export const FROM_REAL = 0; //来自实名
export const FROM_REAL_CARD = 1;//来自实名绑卡
