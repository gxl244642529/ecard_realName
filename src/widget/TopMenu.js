/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
  StyleSheet,
  Text,
  TextInput,
  View,
  Animated,
  TouchableWithoutFeedback,
  ScrollView,
  TouchableOpacity,
  TouchableHighlight,
  PixelRatio,
  Dimensions,
  Easing,
  Modal
} from 'react-native';


const SCREEN_WIDTH = Dimensions.get('window').width;
const SCREEN_HEIGHT = Dimensions.get('window').height;


class Form extends Component{


  componentDidMount() {

    /*
    Api.bind(this,"someapi",(props)=>{
      return parseInt(props.params.id);
    });*/
    
  }

  render(){
    if(Array.isArray(this.props.children)){
      for(var i=0; i < this.props.children.length; ++i){
        console.log(this.props.children[i].props.ref)
      }
    }else{
       console.log(this.props.children.ref)
    }
    

    return <View style={styles.container}>{this.props.children}</View>;
  }

}

import {
    ART
} from 'react-native'

const {Surface, Shape, Path, Group} = ART;

const T_WIDTH = 7;
const T_HEIGHT = 4;


const COLOR_HIGH = '#3fb0b1';
const COLOR_NORMAL = '#6c6c6c';

class Triangle extends React.Component{

    render(){

        var path;
        var fill;
        if(this.props.selected){
          fill = COLOR_HIGH;
          path = new Path()
            .moveTo(T_WIDTH/2, 0)
            .lineTo(0, T_HEIGHT)
            .lineTo(T_WIDTH, T_HEIGHT)
            .close();
        }else{
          fill = COLOR_NORMAL;
          path = new Path()
            .moveTo(0, 0)
            .lineTo(T_WIDTH, 0)
            .lineTo(T_WIDTH/2, T_HEIGHT)
            .close();
        }

        return(
            <Surface width={T_WIDTH} height={T_HEIGHT}>
                <Shape d={path} stroke="#00000000" fill={fill} strokeWidth={0} />
            </Surface>
        )
    }
}

const TopMenuItem = (props)=>{
  const onPress=()=>{
    props.onSelect(props.index);
  }
  return (
    <TouchableWithoutFeedback onPress={onPress}>
      <View style={styles.item}>
        <Text style={props.selected?styles.menuTextHigh:styles.menuText}>{props.label}</Text>
        <Triangle selected={props.selected} />
      </View>
    </TouchableWithoutFeedback>
  );
};


const Check = ()=>{
  return (
     <Surface
        width={18}
        height={12}
        >
        <Group scale={0.03}>
            <Shape
                fill={COLOR_HIGH}
                d={`M494,52c-13-13-33-13-46,0L176,324L62,211c-13-13-33-13-46,0s-13,33,0,46l137,136c6,6,15,10,23,10s17-4,23-10L494,99
      C507,86,507,65,494,52z`}
            />
        </Group>
      </Surface>
  );
}


const Subtitle = (props)=>{
  let textStyle = props.selected ? 
    [styles.tableItemText, styles.highlight, styles.marginHigh] : 
    [styles.tableItemText, styles.margin];

  let rightTextStyle = props.selected ? [styles.tableItemText, styles.highlight] : styles.tableItemText;

  let onPress = ()=>{
    props.onSelectMenu(props.index, props.subindex, props.data);
  }

  return (
    <TouchableHighlight onPress={onPress} underlayColor="#f5f5f5">
      <View style={styles.tableItem}>
        <View style={styles.row}>
          {props.selected && <Check />}
          <Text style={textStyle}>{props.data.title}</Text>
        </View>
        <Text style={rightTextStyle}>{props.data.subtitle}</Text>
      </View>
    </TouchableHighlight>
  );
};

const Title = (props)=>{
   let textStyle = props.selected ? 
    [styles.tableItemText, styles.highlight, styles.marginHigh] : 
    [styles.tableItemText, styles.margin];

  let rightTextStyle = props.selected ? [styles.tableItemText, styles.highlight] : styles.tableItemText;


  let onPress = ()=>{
    props.onSelectMenu(props.index, props.subindex, props.data);
  }

  return (
    <TouchableHighlight onPress={onPress} underlayColor="#f5f5f5">
      <View style={styles.titleItem}>
        {props.selected && <Check />}
        <Text style={textStyle}>{props.data.title}</Text>
      </View>
    </TouchableHighlight>
  );
};

/**
 * 使用方法：
 *
 *
 *
 * <TopMenu config={[  
 * {type:'single',label:'初始显示', selectedIndex:0, data:[ "label1","label2"  ]}
 *   
 * ]} />
 * 
 */



const MAX_HEIGHT = 11 * 43;

export default class TopMenu extends Component {

 
  constructor(props){
    super(props);
    let array = props.config;
    let top = [];
    let maxHeight = [];
    let subselected = [];
    let height = [];
    //最大高度
    var max = parseInt((SCREEN_HEIGHT - 80) * 0.8 / 43);


    for(let i=0, c=array.length; i < c; ++i ){
      let item = array[i];
      top[i] = item.data[item.selectedIndex].title;
      maxHeight[i] = Math.min(item.data.length, max) * 43;
      subselected[i] = item.selectedIndex;
      height[i] = new Animated.Value(0);
    }


    //分析数据
    this.state = {
      top : top,
      maxHeight : maxHeight,
      subselected: subselected,
      height: height,
      fadeInOpacity : new Animated.Value(0),
      selectedIndex : null
    };

    ///数据

  }


  componentDidMount() {
  
  }

  createAnimation=(index, height)=>{
    return Animated.timing(                 
        this.state.height[index],               
        {
          toValue: height,                 
          duration : 250
        }
      );
  }

  createFade=(value)=>{
    return Animated.timing(                 
        this.state.fadeInOpacity,               
        {
          toValue: value,                 
          duration : 250, 
        }
      );
  }


  onSelect=(index)=>{
    if(index===this.state.selectedIndex){
      //消失
      this.hide(index);
    }else{
      this.setState({selectedIndex:index, current: index});
      this.onShow(index);
    }
  }

  hide=(index, subselected)=>{
    let opts = {selectedIndex:null, current:index};
    if(subselected!==undefined){
      this.state.subselected[index]= subselected;
      this.state.top[index] = this.props.config[index].data[subselected].title;
      opts = {selectedIndex:null, current:index, subselected: this.state.subselected.concat() };
    }
    this.setState(opts);
    this.onHide(index);
  }


  onShow=(index)=>{
    
    Animated.parallel([this.createAnimation(index, this.state.maxHeight[index]), this.createFade(1)]).start(); 
  }


  onHide=(index)=>{
    //其他的设置为0
    for(let i=0, c = this.state.height.length; i < c; ++i){
      if(index!=i){
        this.state.height[i].setValue(0);
      }
    }
     Animated.parallel([this.createAnimation(index, 0), this.createFade(0)]).start();
    
  }

  faceIn=(index)=>{
    this.onHide(-1);
  }

  onSelectMenu = (index, subindex, data)=>{
    this.hide(index, subindex);
    this.props.onSelectMenu && this.props.onSelectMenu(index, subindex, data);
  }


  renderList=(d, index)=>{
    let subselected = this.state.subselected[index];
    let Comp = null;
    if(d.type=='title'){
      Comp = Title;
    }else{
      Comp = Subtitle;
    }

    let enabled = this.state.selectedIndex ==index || this.state.current == index;

    return (
      <Animated.View 
        key={index} 
        pointerEvents={enabled ? 'auto':'none'} 
        style={[styles.content, {opacity: enabled ? 1 : 0, height: this.state.height[index]}]}>
        <ScrollView style={styles.scroll}>
        {d.data.map((data, subindex)=>{
          return <Comp 
                  onSelectMenu={this.onSelectMenu} 
                  index={index} 
                  subindex={subindex} 
                  data={data} 
                  selected={subselected == subindex} 
                  key={subindex} />
        })}
        </ScrollView>
      </Animated.View>
    );
  }
/**/
  render() {
    let list = null;
    if(this.state.selectedIndex !== null){
      list = this.props.config[this.state.selectedIndex].data;
    }
   
    //渲染的
    let renderRows = [];
    for(var index=0 ,c = this.state.top.length; index <  c; ++index){
      let t = this.state.top[index];
        if(index > 0){
          renderRows.push(<View key={"s"+index} style={styles.line}></View>);
        }
        renderRows.push(
          <TopMenuItem 
            key={index}
            index={index} 
            onSelect={this.onSelect} 
            label={t} 
            selected={this.state.selectedIndex === index} />
        );
    }

    return (
      <View style={this.props.style ? this.props.style : styles.container}>
        <View style={styles.topMenu}>
        {renderRows}
        </View>
        {this.props.renderContent()}
        <View onPress={this.fadeIn} style={styles.bgContainer} pointerEvents={this.state.selectedIndex !== null ? "auto" : "none"}>
          <Animated.View style={[styles.bg, {opacity:this.state.fadeInOpacity}]} />
          {this.props.config.map((d, index)=>{
            return this.renderList(d, index);
          })}
        </View>
      </View>
    );
  }
}

const LINE = 1/PixelRatio.get();

const styles = StyleSheet.create({
  container:{flex:1},
  line:{backgroundColor:'#eee',height:24,marginTop:6,width:LINE},
  scroll:{flex:1, backgroundColor:'#fff'},
  bgContainer:{position:'absolute', top:40, width:SCREEN_WIDTH, height:SCREEN_HEIGHT},
  bg:{width:SCREEN_WIDTH, height:SCREEN_HEIGHT, position:'absolute', top:0,backgroundColor:'rgba(50,50,50,0.2)'},
  content:{ 
    position:'absolute',
    width:SCREEN_WIDTH
  },

  highlight:{
    color:COLOR_HIGH
  },

  marginHigh:{marginLeft:10},
  margin:{marginLeft:28},



  titleItem:{
    height:43,
    alignItems:'center',
    paddingLeft:10,
    paddingRight:10,
    borderBottomWidth:LINE,
    borderBottomColor:'#eee',
    flexDirection:'row',
  },

  tableItem:{
    height:43,
    alignItems:'center',
    paddingLeft:10,
    paddingRight:10,
    borderBottomWidth:LINE,
    borderBottomColor:'#eee',
    flexDirection:'row',
    justifyContent:'space-between'
  },
  tableItemText:{fontWeight:'300', fontSize:14 },
  row:{
    flexDirection:'row'
  },

  item:{
    backgroundColor:'#ffffff',
    flex:1,
    flexDirection:'row',
    alignItems:'center',
    justifyContent:'center',
  },
  menuTextHigh:{
    marginRight:3,
    fontSize:13,
    color:COLOR_HIGH
  },
  menuText:{
    marginRight:3,
    fontSize:13,
    color:COLOR_NORMAL
  },
  topMenu:{
    flexDirection:'row',
    height:40,
    borderTopWidth:LINE,
    borderTopColor:'#bdbdbd',
    borderBottomWidth:1,
    borderBottomColor:'#f2f2f2'
  },

});

