import React, { Component } from 'react';

import PushUtil from './PushUtil'

import {
  StyleSheet,
  Text,
  TextInput,
  View,
  Image,
  TouchableOpacity,
  Dimensions,
  TouchableHighlight,
  PixelRatio,
  Linking,
  RefreshControl,
  ListView,
  ScrollView,
  ActivityIndicator,
  Switch,
  Platform,
  Animated,
  AsyncStorage,
  Picker,TouchableNativeFeedback,
  DeviceEventEmitter,
} from 'react-native';



import Select from './Select'
import Api from './network/Api'
import Account from './network/Account'
import A from './Alert'
import CommonStyle from './CommonStyle'
import LocalData from './LocalData'
import Log from './Log'
import Form from './Form'
import ImagePicker from './ImagePicker'
import DatePicker from './DatePicker'
import MonthPicker from './MonthPicker'
import TimePicker from './TimePicker'
import Notifier from './Notifier'


var TouchableAll;

if(Platform.OS=='ios'){
	TouchableAll = (props)=>{
		return <TouchableOpacity activeOpacity={props.activeOpacity ? props.activeOpacity : 0.2} onPress={props.onPress} style={props.style}>{props.children}</TouchableOpacity>
	} ;
}else{

	TouchableAll = (props)=>{
		return <TouchableNativeFeedback onPress={props.onPress}><View style={props.style}>{props.children}</View></TouchableNativeFeedback>
	} ;
}


const Common={
	Notifier,
	DeviceEventEmitter,
	MonthPicker,
	DatePicker,
	ImagePicker,
	TimePicker,
	Select,
	Form,
	Log,
	LocalData,
	CommonStyle,
	ScrollView,
	RefreshControl,
	StyleSheet,
	Linking,
	Text,
	ListView,
	TextInput,
	ActivityIndicator,
	View,
	Animated,
	Image,
	TouchableOpacity,
	Dimensions,
	TouchableHighlight,
	Api,
	A,
	TouchableAll,
	PixelRatio,
	Account,
	Switch,
	Platform,
  AsyncStorage,
  Picker
};


module.exports = Common;
