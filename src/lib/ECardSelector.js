



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
  Notifier
} from '../../lib/Common';

import {InteractionManager} from 'react-native'
import {
  TitleBar,
  Button,
  ButtonGroup
} from '../Global'
import NfcUtil from '../../lib/NfcUtil'

import {onRequireLoginPress} from '../../lib/LoginUtil'


const ECardSelectorModule = require('react-native').NativeModules.ECardSelectorModule;
const systemButtonOpacity = 0.2;
export default class ECardSelector extends Component{

	constructor(props) {
		super(props);
		this._selectECard = onRequireLoginPress(this._selectECard);
	}


	onNfc=()=>{
	  NfcUtil.readCard(this._onSelect);
	  return true;
	}

	componentDidMount() {
	  Notifier.addObserver("nfcTag",this.onNfc);
	}

	componentWillUnmount() {
	  Notifier.removeObserver("nfcTag",this.onNfc);
	}

	_onSelect=(cardId)=>{
		//需要通知
		this.props.onSelectECard && this.props.onSelectECard(cardId);
	}
	
	_selectECard=()=>{

		ECardSelectorModule.selectECard(this._onSelect);
	}
	_computeActiveOpacity() {
	    if (this.props.disabled) {
	      return 1;
	    }
	    return this.props.activeOpacity != null ?
	      this.props.activeOpacity :
	      systemButtonOpacity;
	  }


	  focus(){
	  	
	  }


	render(){
		let activeOpacity = this._computeActiveOpacity();
		let touchableProps = {
	      activeOpacity: activeOpacity,
	    };
		

	    if (!this.props.disabled) {
	      touchableProps.onPress = this._selectECard;
	    }
		return (
			<TouchableOpacity style={this.props.style} {...touchableProps}>
				{this.props.children}
			</TouchableOpacity>
		);
	}

}

/*
<TouchableOpacity style={styles.leftButtonSelected}>
		            		<Text style={styles.textSelected}>优惠卡</Text>
		            	</TouchableOpacity>
*/

