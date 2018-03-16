
import React, { Component } from 'react';
import {
  StyleSheet,
  View,
  TextInput,
  Text,
  Animated,
  
  Dimensions,
  TouchableOpacity,
  Image,
  CommonStyle,
  Platform
} from '../../lib/Common';

import{
  ART,InteractionManager
} from 'react-native'

const {Surface, Shape, Path, Group} = ART;

const TITLE_HEIGHT = (Platform.OS === 'android') ? 45 : 65;
const PADDING_TOP = (Platform.OS === 'android') ? 0 : 20;

import {SearchIcon} from './Icons'

const BackIcon=(props)=>{
  return (
     <Surface
        width={15}
        height={17}
        style={props.style}
        >
        <Group scale={1.3}>
            <Shape
                fill={"#ffffff"}
                d="M1.208,6.012l5.676-5.197c0.187-0.186,0.187-0.489,0-0.675
      c-0.187-0.186-0.49-0.186-0.677,0L0.299,5.549C0.241,5.572,0.187,5.607,0.14,5.654c-0.099,0.099-0.142,0.23-0.136,0.36
      c-0.005,0.128,0.039,0.258,0.137,0.356c0.046,0.046,0.099,0.08,0.156,0.104l5.91,5.411c0.187,0.187,0.49,0.187,0.677,0
      c0.187-0.187,0.187-0.489,0-0.676L1.208,6.012z"
            />
        </Group>
      </Surface>
  );
}

export default class SearchBar extends Component {  

  constructor(props){
    super(props);
    let pos = this.props.value ? 0 : 100;
    this.state={value:this.props.value, marginLeft:new Animated.Value(pos)};
  }

  componentDidMount() {
    if(!this.props.disable){
      this.doFocus();
    }
  }

  componentWillReceiveProps(nextProps) {
    if(nextProps.disable!==this.props.disable && !nextProps.disable){
      this.doFocus();
    }
    if(nextProps.value !== this.state.value){
      this.setState({value:nextProps.value});
    }
  }

  doFocus=()=>{
    if(!this.state.value){
       setTimeout(()=>{
         Animated.timing(                 
                this.state.marginLeft,               
                {
                  toValue: 0,                 
                  duration : 250
                }
              ).start();
            InteractionManager.runAfterInteractions(() => {
              this.refs.TEXT && this.refs.TEXT.focus();
            });
       }, 500);
    }else{
      InteractionManager.runAfterInteractions(() => {
          this.refs.TEXT && this.refs.TEXT.focus();
        });
    }
  }

  onChangeText=(text)=>{
    this.setState({value:text});
    this.props.onChange && this.props.onChange(text);
  }

  onLayout=(e)=>{

  }

  _onBack=()=>{
    this.setState({value:''})
    this.props.onCancel && this.props.onCancel();
  }

  onSearch=()=>{
    this.props.onSearch && this.props.onSearch(this.state.value);
  }
  // <!--<Image source={require('../images/back.png')} style={styles.backImage} />-->

  render() {  
    return (  
      <View style={[styles.searchRow,this.props.style]}>
        <TouchableOpacity onPress={this._onBack} style={styles.backButton}>
          <BackIcon style={styles.backImage} />
        </TouchableOpacity>
        <View style={styles.textContainer}>
          <Animated.View style={[styles.row, {marginLeft:this.state.marginLeft}]}>
            <SearchIcon style={styles.searchIcon} />
            {!this.state.value && <Text style={styles.text} 
              onLayout={this.onLayout}>{this.props.placeholder}</Text>}
          </Animated.View>
          <TextInput
            numberOfLines={1}
            underlineColorAndroid={'#fff'}
            onSubmitEditing={this.onSearch}
            enablesReturnKeyAutomatically={true}
            ref="TEXT"
            value={this.state.value}
            onChangeText={this.onChangeText}
            returnKeyType="search"
            autoCapitalize="none"  
            autoCorrect={false} 
            clearButtonMode="always"  
            style={styles.searchTextInput}  
          />
        </View>
      </View>  
    );  
  }  
}  



const styles = StyleSheet.create({
  backImage:{
    width:9, height:19
  },
  backButton:{
    height:45, width:40, justifyContent:'center', alignItems:'center'
  },
  searchIcon:{marginTop:3},
  text:{marginLeft:2, marginTop:7, fontSize:14, color:'#939393'},
  row:{flexDirection:'row', position:'absolute'},
  textContainer:{backgroundColor:'#fff', height:30, marginRight:10, flex:1, borderRadius:3},
  searchRow: {  
    backgroundColor: '#3292e9',  
    height:TITLE_HEIGHT,
    paddingTop:PADDING_TOP,
    flexDirection:'row',
    alignItems:'center',
    justifyContent:'center',
  },  
  searchTextInput: {  
    fontSize:14,
    paddingLeft:22,
    flex:1,
    padding:4,
    backgroundColor: 'transparent',  
  },  
});  