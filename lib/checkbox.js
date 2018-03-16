'use strict';

import React, { Component } from 'react';
var PropTypes = React.PropTypes;
import {
    StyleSheet,
    Image,
    Text,
    View,
    TouchableHighlight,
    TouchableOpacity,
    Dimensions
} from 'react-native';
const CB_ENABLED_IMAGE = require('./images/ic_insurance_checked@2x.png');
const CB_DISABLED_IMAGE = require('./images/ic_insurance_uncheck@2x.png');
const SCREEN_WIDTH = Dimensions.get('window').width;

class CheckBox extends Component {
    constructor(props) {
        super(props);

        this.state = {
            internalChecked: false
        };

        this.onChange = this.onChange.bind(this);
    }

    onChange() {
        if (this.props.onChange &&  typeof this.props.checked === 'boolean') {
            this.props.onChange(this.props.checked);
        } else {
            let internalChecked = this.state.internalChecked;

            if(this.props.onChange){
              this.props.onChange(internalChecked);
            }
            this.setState({
                internalChecked: !internalChecked
            });
        }
    }


    render() {
        let container = (
            <View style={this.props.containerStyle || styles.container}>
                  <TouchableOpacity onPress={this.onChange} style={{padding:5}}><Image
                style={this.props.checkboxStyle || styles.checkbox}
                source={source}/>  </TouchableOpacity>
                <TouchableOpacity style={styles.labelContainer} onPress={this.props.urlPress}>
                    <Text style={[styles.label, this.props.labelStyle]}>{this.props.label}</Text>
                </TouchableOpacity>
            </View>
        );

        let source;

        if(typeof this.props.checked === 'boolean') {
          source = this.props.checked ? this.props.checkedImage : this.props.uncheckedImage;
        } else {
          source = this.state.internalChecked ? this.props.checkedImage : this.props.uncheckedImage;
        }


        if (this.props.labelBefore) {
            container = (
                <View style={this.props.containerStyle || [styles.container, styles.flexContainer]}>
                    <TouchableOpacity style={styles.labelContainer} onPress={this.props.urlPress}>
                        <Text numberOfLines={this.props.labelLines} style={[styles.label, this.props.labelStyle]}>{this.props.label}</Text>
                    </TouchableOpacity>
                      <TouchableOpacity onPress={this.onChange}><Image
                    style={[styles.checkbox, this.props.checkboxStyle]}
                    source={source}/>  </TouchableOpacity>
                </View>
            );
        } else {
            container = (
                <View style={[styles.container, this.props.containerStyle]}>
                    <TouchableOpacity onPress={this.onChange} style={{padding:5}}><Image
                    style={[styles.checkbox, this.props.checkboxStyle]}
                    source={source}/></TouchableOpacity>
                    <TouchableOpacity style={styles.labelContainer} onPress={this.props.urlPress}>
                        <Text numberOfLines={this.props.labelLines} style={[styles.label, this.props.labelBeStyle]}>{this.props.labelBe}</Text>
                        <Text numberOfLines={this.props.labelLines} style={[styles.label, this.props.labelStyle]}>{this.props.label}</Text>
                    </TouchableOpacity>
                </View>
            );
        }

        return (
            <View onPress={this.onChange} underlayColor={this.props.underlayColor} style={styles.flexContainer}>
                {container}
            </View>
        );
    }
}

var styles = StyleSheet.create({
    container: {
        flexDirection: 'row',
        alignItems: 'center',
        marginBottom: 5,
    },
    checkbox: {
        width: 26,
        height: 26
    },
    labelContainer: {
        marginLeft: 10,
        marginRight: 10,
        width:SCREEN_WIDTH-50,
        flexDirection:'row',

    },
    label: {
        fontSize: 15,
        color: 'grey'
    }
});

CheckBox.propTypes = {
    label: PropTypes.string,
    labelBefore: PropTypes.bool,
    labelStyle: PropTypes.oneOfType([PropTypes.array,PropTypes.object,PropTypes.number]),
    labelLines: PropTypes.number,
    checkboxStyle: PropTypes.oneOfType([PropTypes.array,PropTypes.object,PropTypes.number]),
    containerStyle: PropTypes.oneOfType([PropTypes.array,PropTypes.object,PropTypes.number]),
    checked: PropTypes.bool,
    checkedImage: PropTypes.number,
    uncheckedImage: PropTypes.number,
    underlayColor: PropTypes.string,
    onChange: PropTypes.func
};

CheckBox.defaultProps = {
    label: 'Label',
    labelLines: 1,
    labelBefore: false,
    checked: null,
    checkedImage: CB_ENABLED_IMAGE,
    uncheckedImage: CB_DISABLED_IMAGE,
    underlayColor: 'white'
};

module.exports = CheckBox;
