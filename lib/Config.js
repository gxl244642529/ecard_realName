import { createNavigatorRouter } from './RouterNavigator';
import { createMemoryHistory } from 'react-router';
import { Platform, BackAndroid, Navigator} from 'react-native';
import Api from './network/Api'



//BackAndroid.addEventListener('hardwareBackPress', handleBackButton);

const config = {
createRouter : createNavigatorRouter,
history:createMemoryHistory('/')
};

module.exports = config;