import React, { Component } from 'react';
import {ART} from 'react-native'
const {Surface, Shape, Group,Path} = ART;

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

//圈、圆中点
export const IC_CircleDot=()=>{
    let color="#2fa3ba";
    return (
        <Surface
        width={20}
        height={20}
        >
        <Group scale={0.019}>
            <Shape
                fill={color}
                d="M154.582339 512a11.242 11.293 0 1 0 719.443035 0 11.242 11.293 0 1 0-719.443035 0Z"
            />
            <Shape
                fill={color}
                d="M514.303856 0C232.817449 0 4.607712 229.233673 4.607712 512s228.209737 512 509.696144 512 509.696144-229.233673 509.696144-512S795.790263 0 514.303856 0zM514.303856 986.978314c-261.135679 0-472.834448-212.658709-472.834448-474.978314 0-262.319605 211.698769-474.978314 472.834448-474.978314S987.138304 249.680395 987.138304 512C987.138304 774.319605 775.439535 986.978314 514.303856 986.978314z"
            />
        </Group>
      </Surface>
    )
}

//空心圆
export const IC_HollowCircle=()=>{
    let path="M512 39.384615c259.938462 0 472.615385 212.676923 472.615385 472.615385s-212.676923 472.615385-472.615385 472.615385S39.384615 771.938462 39.384615 512 252.061538 39.384615 512 39.384615M512 0C228.430769 0 0 228.430769 0 512s228.430769 512 512 512 512-228.430769 512-512S795.569231 0 512 0L512 0z";
    return createIcon(20,20,0.019,"#2fa3ba",path);
}
