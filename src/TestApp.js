import {Scene, Router} from 'react-native-router-flux';
import React, { Component } from 'react';
import{
  View,
  Text,
  Image,
  StyleSheet,
  Api,
  A,
  CommonStyle,
  TextInput,
  DatePicker,
  TouchableOpacity,
  Form,
  Dimensions,
  ScrollView
} from '../lib/Common'


import {Actions} from 'react-native-router-flux'
class Login extends Component{

	render(){
		return <TouchableOpacity onPress={()=>{

			Actions.home();

		}}><Text style={{marginTop:100}}>login</Text></TouchableOpacity>
	}
}


class Register extends Component{

	render(){
		return <Text>Register</Text>
	}
}
class Home extends Component{

	render(){
		return <Text>Home</Text>
	}
}

export default class App extends React.Component {
  render() {
    return <Router>
      <Scene key="root">
        <Scene key="login" component={Login} title="Login"/>
        <Scene key="register" component={Register} title="Register"/>
        <Scene key="home" component={Home}/>
      </Scene>
    </Router>
  }
}