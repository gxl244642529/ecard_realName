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
import Button from "../widget/Button"
import StateListView from '../widget/StateListView'
const SCREEN_WIDTH = Dimensions.get('window').width;



export default class GetCoupon extends Component{

	constructor(props) {
		super(props);
		this.state={loading:true,hasData:false};
	}

  componentDidMount(){
    let data = {code:this.props.params.id}
    Api.api({
			api:"coupon/detail",
			data:data,
			success:(result)=>{
				this.setState(result);
			}
		});
  }
  _getCoupon=()=>{
    let data = {code:this.props.params.id}
    Api.api({
			api:"coupon/fetch",
			data:data,
			success:(result)=>{
				console.log(result);
			}
		});
  }
	render(){
    	return(
    		<View style={CommonStyle.container}>
    			<TitleBar style={{ color:'#804e21'}} title="领取优惠券" renderRight={this._renderRight} />
          <Image style={styles.list} source={require('./images/count_re.png')}>
          <View style={{flexDirection:'row',justifyContent:'space-between'}}>
              <View style={{flexDirection:'row',marginBottom:10}}>
                <Text style={{color:'#fff'}}>{this.state.title}</Text>
                <Image source={require('./images/counter.png')} style={{width:19,height:19}}/>
              </View>
            </View>
            <View style={{justifyContent:'space-between',flexDirection:'row'}}>
      				<View style={{justifyContent:'space-between'}}>
      					<Text style={styles.listText}>截止时间:{this.state.endtime}</Text>
                <Text style={styles.listText}>优惠内容:{this.state.desc}</Text>
      					<Text style={styles.listText}>所属商家:{this.state.shpName}</Text>
      				</View>
      			</View>
          </Image>
          <Button style={styles.btn} text="领取优惠券" textStyle={styles.btnText}
          onPress={this._getCoupon}
          />
			</View>
    	)
  	}
}

const styles = StyleSheet.create({
	container:{
		backgroundColor:"#fef3cc",
		flex:1,
	},
	container:{flex:1},
	listHasData:{flex:1},
	listEmpty:{height:0},

	MyCouponMain:{
		padding:15,
		backgroundColor:'#fff',
		marginTop:10,
	},

	MyCouponMain_one:{
		flexDirection:'row',

	},

	MyCouponMain_two:{
		flexDirection:'row',
		marginTop:5,

	},

	MyCouponMain_three:{
		flexDirection:'row',
		marginTop:5,
	},
	list:{
		paddingLeft:25,
		paddingTop:12,
		paddingBottom:15,
		paddingRight:15,
		marginLeft:10,
		justifyContent:'space-between',

		width:SCREEN_WIDTH-20,
		height:120,

		marginTop:20,
	},
  listText:{
    fontSize:12,
    marginTop:5,
    marginBottom:5
  },
  btn:{
    margin:20,
    marginLeft:40,
    marginRight:40,
    backgroundColor:'#43b0b1',
    borderRadius:5,
  },
  btnText:{
    color:"#fff",
  }

});
