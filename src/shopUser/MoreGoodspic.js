import React,{Component} from 'react';
import{
  View,
  Api,
  StyleSheet,
  Image,
  Dimensions,
  CommonStyle
} from '../../lib/Common';

import Swiper from '../../lib/Swiper'

const SCREEN_WIDTH = Dimensions.get('window').width;
const SCREEN_HEIGHT=Dimensions.get('window').width;
import TitleBar from '../widget/TitleBar'
export default class MoreGoodspic extends Component{
  constructor(props){
    super(props);
  }
  componentDidMount(){
    let self=this;
    let id=parseInt(this.props.params.id);
    Api.api({
      api:"bgoods/images",
      data:{
        id:id,
      },
      success:(data)=>{
        self.setState({data});
        console.log(self.state.data);
      }
    })
  }
  renderImg(){
      var imageViews=[];
      if(this.state){
        var len=this.state.data.length;
        for(var i=0;i<len;i++){
          console.log(this.state.data[i].src);
            imageViews.push(
              <Image
              resizeMode='contain'
              key={i}
              style={{flex:1}}
              source={{uri: this.state.data[i].src}}/>
            );
        }
        if(this.props.params.src){
          imageViews.push(  <Image
            resizeMode='contain'
            key={1}
            style={{flex:1}}
            source={{uri:this.props.params.src}}/>);
        }

      }
      return imageViews;
  }

  render(){


    return <View style={CommonStyle.container}>
    <TitleBar title=" "/>
    <Swiper>
                {this.renderImg()}
            </Swiper>
          </View>
  }
}
