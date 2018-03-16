import React, { Component,PropTypes } from 'react';
import {
  StyleSheet,
  Text,
  TouchableOpacity,
  Image,
  ActivityIndicator
} from '../../lib/Common';





export default class LoadingButton extends Component{

	static propTypes = {
	    ...TouchableOpacity.propTypes,
	    loading: PropTypes.bool,
	    disabled: PropTypes.bool,
	    textStyle: Text.propTypes.style,
	  };


	constructor(props) {
	  super(props);
	}


	shouldComponentUpdate(nextProps, nextState) {
	  return this.props.disabled !== nextProps.disabled
	    || this.props.loading !== nextProps.loading
	  	|| this.props.text !== nextProps.text
	  	|| this.props.source !== nextProps.source;
	}

	render(){

		let loading = this.props.loading === true;
		let touchableProps = {
	      activeOpacity: loading ? 1: 0.5
	    };
	    if (!this.props.disabled) {
	      touchableProps.onPress = this.props.onPress;
	      touchableProps.onPressIn = this.props.onPressIn;
	      touchableProps.onPressOut = this.props.onPressOut;
	      touchableProps.onLongPress = this.props.onLongPress;
	    }

	    let iconStyle = this.props.iconStyle || styles.icon;

		return (
			<TouchableOpacity 
			 	{...touchableProps}
                style={this.props.style}
            >
            	{this.props.source&&
            		<Image source={this.props.source} 
            		style={iconStyle} />}
                <Text style={this.props.textStyle}>{this.props.text}</Text>
              { loading && <ActivityIndicator style={styles.loadingIndicator} />}
            </TouchableOpacity>

		);

	}

}

const styles = StyleSheet.create({
	loadingIndicator:{
	    width:30,
	    height:30
	},
	icon:{
		width:30,height:30,resizeMode:'contain'
	}
});
