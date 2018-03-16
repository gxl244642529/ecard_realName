
import React, { Component } from 'react';
import {
  StyleSheet,
  View,
  Animated,
  Dimensions
} from './Common';


import StaticContainer from 'react-static-container'
const SCREEN_WIDTH = Dimensions.get('window').width;

export default class TabContainer extends Component{


  static propTypes={
    comps:React.PropTypes.array.isRequired,			//组的样式
  }

	constructor(props) {
	  super(props);
	  this.visited={};
	}

  shouldComponentUpdate(nextProps, nextState) {
    return nextProps.selectedIndex!==undefined && nextProps.selectedIndex !== this.props.selectedIndex;
  }

	render(){
		let arr = [];
		for(let i=0 , c = this.props.comps.length; i <c ;++i){
			let Component  = this.props.comps[i];
			if(i==this.props.selectedIndex){
        this.visited[i]=true;
				arr.push(<View 
          			pointerEvents={'auto'}
          			style={styles.comp} 
          			removeClippedSubviews={false}
          			key={"comp"+i}>
                <StaticContainer shouldUpdate={true}><Component {...this.props.compProps} /></StaticContainer>
          		</View>);
			}else{
        if(this.visited[i]){
           arr.push(<View style={[styles.comp,styles.hide]} 
                  pointerEvents={'none'}
                  removeClippedSubviews={true}
                  key={"comp"+i}>
                <StaticContainer shouldUpdate={false}><Component {...this.props.compProps} /></StaticContainer>
            </View>);
        }
			 
			}
		}


		return (
			<View style={styles.container}> 
	        {arr}
	        </View>
        );
	}
}



const styles = StyleSheet.create({
  container:{
	flex:1
  },
  comp:{
    position: 'absolute',
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
  },
  hide:{
    overflow: 'hidden',
    opacity: 0,
  },
 
});