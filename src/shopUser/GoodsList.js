import React,{Component} from 'react'
import {
    View,Text,StyleSheet,Image,A,ListView,CommonStyle,Api,TouchableOpacity,Dimensions,
} from '../../lib/Common'

import StateListView from '../widget/StateListView'

const SCREEN_WIDTH = Dimensions.get('window').width;

export default class GoodsList extends Component{
  constructor(props){
    super(props);
  }
  render(){
      let data=this.props.data;
      return (<TouchableOpacity onPress={()=>{Api.push('/shopUser/goodsDetail/'+data.id)}}>
        <View style={styles.goodsView}>
            <Image source={{uri:data.thumb}} style={styles.goodsImg}>
              {this.props.isMain && data.recommand && <Image source={require('./images/recommend.png')} style={styles.goodszan}/>}
            </Image>
              <View style={{flex:1,justifyContent:'space-between',padding:10}}>
                <Text style={{color:"#595757",fontSize:14}}>{data.title}</Text>
                <Text numberOfLines={2} style={{color:"#9f9fa0",fontSize:12}}>{data.desc}</Text>
                <View style={{flexDirection:'row',alignSelf:"flex-end",justifyContent:'center',alignItems:'center'}}>
                    <Text style={{color:'#EA5441',fontSize:16,fontWeight:'bold'}}>¥{data.disprice}</Text>
                    {data.price&&  <Text style={{color:'#A5A5A5',fontSize:14,marginLeft:5,textDecorationLine:'line-through'}}>¥{data.price}</Text> }

                </View>
              </View>
    </View></TouchableOpacity>)
  }
}
const styles=StyleSheet.create({
    goodsView:{
        flexDirection:'row',
        flex:1,
        backgroundColor:'#fff',
        marginTop:10
    },
    goodsImg:{
        width:(SCREEN_WIDTH-20)*0.27,
        height:(SCREEN_WIDTH-20)*0.27,
    //borderRadius:5,
    },
    goodszan:{
      width:25,
      height:23,
      position:'absolute',
      top:5,
      right:2
    },
})
