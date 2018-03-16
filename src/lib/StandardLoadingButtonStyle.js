import {StyleSheet} from '../../lib/Common'

export const loadingButton = StyleSheet.create({
	loadingButtonText:{
		fontSize:16,
		color:'#FFFFFF'
	},
	loadingButton:{
		flexDirection:'row',
		margin:10,
		height:35,
		backgroundColor:'#e8464c',
		borderColor:'#2e6da4',
		//borderRadius:5,
		justifyContent:'center',
		alignItems:'center',


		//自动距上
		marginTop:20
	},
	loadingButtonDisabled:{
		backgroundColor:'#cccccc',
	}
});


export const loadingButtonDisabled = [loadingButton.loadingButton,loadingButton.loadingButtonDisabled];