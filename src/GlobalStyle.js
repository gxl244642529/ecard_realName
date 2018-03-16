
import StateList from './widget/StateList'
import Drawable from './widget/Drawable'

import {StyleSheet} from '../lib/Common'


/**
加载按钮
*/
export const loadingButton = StyleSheet.create({
	loadingButtonText:{
		fontSize:16,
		color:'#FFFFFF'
	},

	loadingButton:{
		flexDirection:'row',
		margin:10,
		height:35,
		backgroundColor:'#ee6060',
		borderColor:'#ee6060',
		//borderRadius:5,
		justifyContent:'center',
		alignItems:'center'
	},
	loadingButtonDisabled:{
		backgroundColor:'#cccccc',
	},
	loadingButtonReal:{
		flexDirection:'row',
		margin:20,
		height:40,
		backgroundColor:'#e8464c',
		borderColor:'#e8464c',
		justifyContent:'center',
		alignItems:'center'
	},

});

export const pageButton =StyleSheet.create({
	button:{
		flexDirection:'row',
		margin:10,
		height:35,
		backgroundColor:'#ee6060',
		borderColor:'#ee6060',
		//borderRadius:5,
		justifyContent:'center',
		alignItems:'center',

	},
	buttonText:{
		fontSize:16,
		color:'#FFFFFF'
	},
})

export const container = StyleSheet.create({

	container:{
		backgroundColor:'#fff'
	}
});

export const loadingButtonDisabled = [loadingButton.loadingButton,loadingButton.loadingButtonDisabled];
export const loadingButtonRealDisabled = [loadingButton.loadingButtonReal,loadingButton.loadingButtonDisabled]
/**
常用的布局
*/
export const globalStyles = StyleSheet.create({
  centering: {
  	flex:1,
    alignItems: 'center',
    justifyContent: 'center'
  },
  gray: {
    backgroundColor: '#cccccc',
  },
  horizontal: {
    flexDirection: 'row',
    justifyContent: 'space-around',
    padding: 8,
  },
  loading:{
  	height: 30,
  	width: 30
  }
});


const radioButtonStyle = StyleSheet.create({
	buttonStyle:{
		backgroundColor:'#FFFFFF',flexDirection:'row',flex:1
	},
	textStyle:{fontSize:13,marginLeft:5},
	iconStyle:{
		width:15,
		height:15
	},
});


/**
用于 sex 等单选按钮组
*/
export const radioButton = {
	icon:require('./images/s_ic_off.png'),
	iconSelected:require('./images/s_ic_on.png'),
	iconStyle:radioButtonStyle.iconStyle,
	buttonStyle:radioButtonStyle.buttonStyle,
	textStyle:radioButtonStyle.textStyle
}
