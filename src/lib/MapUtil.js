import React, { Component } from 'react';

const PositionModule = require('react-native').NativeModules.AMapModule;

export default class MapUtil{

	static getPos(success){
		PositionModule.getPosition(success);
	}
	/**
	 * 获取距离
	 * @param  {[type]}   lat       [description]
	 */
	static getDistance(opts,callback){
		PositionModule.getDistance(opts,callback);
	}

}