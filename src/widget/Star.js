import React,{Component} from 'react'

import {View,StyleSheet,Image} from "../../lib/Common"

export default class Star extends Component{
  constructor(props){
    super(props);
    this.state={num:props.num  || 0};
  }

  componentWillReceiveProps(nextProp){
    if(nextProp.num !== this.state.num){
      this.setState({num:nextProp.num});
    }
  }

  /*componentDidMount(){
    var num=3;
    var full=[];
    var empty=[];
    console.log(num);
    for(var i=0;i<num;i++){
      full.push(" ");
    }
    for(var i=num;i<5;i++){
      empty.push(" ");
    }
    this.setState({full:full,empty:empty});
  }*/

  render(){
    let imgStyle;
    if(this.props.style){
      imgStyle=this.props.style;
    }else{
      imgStyle=styles.starImg;
    }

    // console.log(this.state);
    let arr = [];
    let num = this.state.num;
    let num_int = Math.floor(num);
    for(let i =1; i <= 5; ++i){
      if(i<=num)
        arr.push(<Image key={"star"+i} source={require("../images/star_1.png")} style={imgStyle} />);
      else if(i==num_int+1 && num!=num_int)
        arr.push(<Image key={"star"+i} source={require("../images/star_3.png")} style={imgStyle} />);
      else
        arr.push(<Image key={"star"+i} source={require("../images/star_2.png")} style={imgStyle} />);
    }

    return <View style={{flexDirection:'row'}}>{arr}</View>
    /*
    return <View style={{flexDirection:'row'}}>
    {
      this.state.full.map((data,index)=>{
        return <Image key={"star"+index} source={require("../images/star_1.png")} style={styles.starImg} />
      })

    }
    {
      this.state.empty.map(
        (data,index)=>{
          return <Image key={"star"+index} source={require("../images/star_2.png")} style={styles.starImg} />
        }
      )
    }
    </View>*/
  }
}

const styles=StyleSheet.create({
  starImg:{
    width:15,
    height:15,
  },
})
