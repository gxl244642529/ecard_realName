import React,{Component} from 'react'
import{
  View,
  Api,
  Text,
  StyleSheet,
  Image,
  Dimensions,
  TouchableOpacity
} from '../../lib/Common';
const SCREEN_WIDTH = Dimensions.get('window').width;
import Urls from '../lib/Urls'

import {InteractionManager} from 'react-native'
import Swiper from '../../lib/Swiper'

const OnClickAdv=(props)=>{
   Urls.openUrl(props.url,props.title);
}

class AdvViewImage extends Component{
  render(){
    return (
      <TouchableOpacity onPress={()=>{OnClickAdv(this.props)}}>
        <Image
            resizeMode='stretch'
            style={this.props.style}
            source={{uri:Api.imageUrl+this.props.img}}
            />
      </TouchableOpacity>
    );
  }
}

/**
 * id:
 */
export default class AdvView extends Component{
    constructor(props){
        super(props);
        this.state={
          style:{
            width:props.width ? props.width : SCREEN_WIDTH ,
            height:props.height ? props.height : (SCREEN_WIDTH / 4)
          },
          autoplay:true
        };
    }

    shouldComponentUpdate(nextProps, nextState) {
      return (nextProps.data!==undefined && nextProps.data !== this.state.data) ||
            (nextState.data!==undefined  && nextState.data !== this.state.data) || 
                nextState.autoplay!=this.state.autoplay;
    }

    componentWillReceiveProps(nextProps) {
      this.setState({data:nextProps.data});
    }

    setAutoplay=(enable)=>{
      InteractionManager.runAfterInteractions(()=>{
         this.setState({autoplay:enable});
      });
    }


    componentDidMount(){
        let self=this;
        let id=this.props.id;
        if(!id)return;
        Api.detail(this,
          {
            api:"adv/list",
            data:{
                id:id,
            },
            name:'data'
        })
    }

    render(){
        let data = this.state.data;
        if(!data || !data.length){
          return <View style={this.state.style}></View>;
        }
        if(data.length==1){
          let vo = data[0];
          return <AdvViewImage style={this.state.style} {...vo} />
        }
        let images = [];
        var len=this.state.data.length;
        for(var i=0;i<len;i++){
          let data = this.state.data[i];
          images.push(<AdvViewImage style={this.state.style} key={i} {...data} />);
        }
        return (
          <Swiper {...this.state.style} 
            autoplay={this.state.autoplay} 
            autoplayTimeout={8}>
              {images}
          </Swiper>
        );
    }
}

