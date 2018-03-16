

import React, { Component } from 'react';
import {
  StyleSheet,
  View,
  TextInput,
  Text,
  ART,
  Animated,
  InteractionManager,
  Dimensions,
  TouchableOpacity,
  Image
} from 'react-native';

const {Surface, Shape, Path, Group} = ART;


const createIcon=(width,height,scale,color,path)=>{
    return (
        <Surface
        width={width}
        height={height}
        >
        <Group scale={scale}>
            <Shape
                fill={color}
                d={path}
            />
        </Group>
      </Surface>
    )
}


export const SearchIcon = (props)=>{
  return (
     <Surface
        width={20}
        height={20}
        style={props.style}
        >
        <Group scale={0.7}>
            <Shape
                fill={"#939393"}
                d="M27,25l-5.6-5.6c1-1.4,1.6-3.1,1.6-4.9C23,9.8,19.2,6,14.5,6S6,9.8,6,14.5S9.8,23,14.5,23c1.8,0,3.5-0.6,4.9-1.6L25,27
  c0.5,0.5,1.4,0.5,2,0l0,0C27.6,26.4,27.6,25.6,27,25z M14.5,21C10.9,21,8,18.1,8,14.5S10.9,8,14.5,8s6.5,2.9,6.5,6.5
  S18.1,21,14.5,21z"
            />
        </Group>
      </Surface>
  );
}

//titlebar返回图标
export const BackIcon=(props)=>{
  return (
     <Surface
        width={15}
        height={17}
        style={props.style}
        >
        <Group scale={1.3}>
            <Shape
                fill={"#595757"}
                d="M1.208,6.012l5.676-5.197c0.187-0.186,0.187-0.489,0-0.675
      c-0.187-0.186-0.49-0.186-0.677,0L0.299,5.549C0.241,5.572,0.187,5.607,0.14,5.654c-0.099,0.099-0.142,0.23-0.136,0.36
      c-0.005,0.128,0.039,0.258,0.137,0.356c0.046,0.046,0.099,0.08,0.156,0.104l5.91,5.411c0.187,0.187,0.49,0.187,0.677,0
      c0.187-0.187,0.187-0.489,0-0.676L1.208,6.012z"
            />
        </Group>
      </Surface>
  );
}



//金额
export const IC_Money=()=>{
    let path="M512 1.024A508.8256 508.8256 0 0 0 19.182933 378.5728l115.2 89.6C153.6 276.206933 313.617067 128.989867 512 128.989867c153.6 0 281.6 89.6 345.6 217.6l-121.617067 32.017066L1024 506.606933C1017.582933 231.389867 793.6 0.989867 512 0.989867z m0 889.582933c-153.6 0-281.6-89.6-345.6-217.6h121.617067L0 481.006933v31.982934c6.417067 275.217067 230.4 505.617067 512 505.617066 243.2 0 448-172.817067 499.2-403.217066l-121.617067-64c-19.182933 185.617067-179.2 339.217067-377.582933 339.217066z m83.217067-595.217066l-32.017067 70.417066c-25.6 51.2-38.4 76.8-38.4 89.6 0 6.382933-6.417067 12.8-6.417067 12.8-12.765867-25.6-38.365867-83.217067-83.182933-172.817066H339.182933l108.817067 192h-76.8v57.617066h102.4v44.782934h-108.817067v57.617066h102.4v76.8h89.634134v-76.8h95.982933v-64h-96.017067v-38.4h96.017067v-57.617066h-76.8l108.817067-192h-89.6z";
    return createIcon(30,30,0.02,'#595757',path);
}

//支付控件-叉号图标
export const IC_XMark=()=>{
    let path="M792.779871 287.370986 736.623897 231.220129 512.001023 455.841979 287.379173 231.220129 231.220129 287.370986 455.845049 511.997953 231.220129 736.620827 287.379173 792.779871 512.001023 568.153928 736.623897 792.779871 792.779871 736.620827 568.156998 511.997953Z";
    return createIcon(20,20,0.02,'#595757',path);
}