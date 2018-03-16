import React,{Component} from 'react';

import {
		View,
		Text,
		StyleSheet,
		Image,
		TouchableOpacity,
		Dimensions,
		ScrollView,
		ImagePicker,
		Switch,
		TextInput,
		Api,
		CommonStyle
		} from '../../lib/Common'

import TitleBar from '../widget/TitleBar'
import StateListView from '../widget/StateListView'
import {renderShopItem} from './ShopUserUtils'
import Notifier from '../../lib/Notifier'


export default class ShopCollection extends Component{

	constructor(props) {
   		super(props);
	}
	componentWillMount() {
		Notifier.addObserver("bshop/col",this.refreshList);

	}

	componentWillUnmount() {
			Notifier.removeObserver("bshop/col",this.refreshList);
	}

	refreshList=()=>{
		this.refs.LIST.reloadWithStatus();
	}


	render(){

    	return(
				<View style={styles.container}>

					<StateListView
						ref="LIST"
						api="bshop/cols"
						style={{flex:1}}
						paged ={true}
						pageSize={10}
						renderRow={renderShopItem}
					/>
		 </View>
    	)
  	}
}

const styles = StyleSheet.create({
	container:{
		backgroundColor:'#f0f0f0',
		flex:1,
	},

	main:{
		padding:10,
		backgroundColor:'#fff',
		flexDirection:'row',
		marginTop:8,
	},

	mainRight:{
		marginLeft:10,
		justifyContent:'space-between',
		flex:1,
	},

	mainRightOne:{
		justifyContent:'space-between',
		flexDirection:'row',
	},

	mainRightTwo:{
		flexDirection:'row',
		height:20,
	},

	mainRightThree:{
		flexDirection:'row',
		height:20,
		//fontSize:12,
		justifyContent:'space-between',
	},
	image:{
		width:12,
		height:12,
		marginRight:5,
	},
});
