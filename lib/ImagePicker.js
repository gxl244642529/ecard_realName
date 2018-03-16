import React, { Component } from 'react';
import {
  TouchableOpacity
} from 'react-native';

const ImagePickerModule = require('react-native').NativeModules.ImagePickerModule;

const systemButtonOpacity = 0.2;
export default class ImagePicker extends Component{

	constructor(props) {
	  super(props);
	}

	focus(){

	}

	_computeActiveOpacity() {
	    if (this.props.disabled) {
	      return 1;
	    }
	    return this.props.activeOpacity != null ?
	      this.props.activeOpacity :
	      systemButtonOpacity;
	}


	_onPress=()=>{
		ImagePickerModule.select({album:this.props.album===true,
			editWidth:this.props.editWidth,
			editHeight:this.props.editHeight,
			width:this.props.width,
			height:this.props.height,
			quality:this.props.quality
		},(path)=>{
			this.props.onChange && this.props.onChange(path);
		});
	}

	render(){
		let activeOpacity = this._computeActiveOpacity();
		let touchableProps = {
	      activeOpacity: activeOpacity,
	    };

	    if (!this.props.disabled) {
	      touchableProps.onPress = this._onPress;
	    }

		return (
			<TouchableOpacity {...touchableProps} style={this.props.style}>
				{this.props.children}
			</TouchableOpacity>
		);
	}
}

ImagePicker.propTypes = {
  album: React.PropTypes.bool,
  edit:React.PropTypes.any
}
