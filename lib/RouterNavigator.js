import React, { Component } from 'react';
import { Platform, BackAndroid, Navigator, View, StyleSheet } from 'react-native';
import Alert from './Alert'
import Api from './network/Api'
import Notifier from './Notifier'

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: 'black',
  },
});

let exit = false;

export function createNavigatorRouter(onBack = null, style = {}) {
  if (!onBack) {
    onBack = function (index) {

      if(Notifier.notifyObservers('androidBack')){
        return true;
      }


      if (index > 1) {
        this.context.router.goBack();
        return true;
      }
      if(exit){
        Api.navigator = null;
        //navigator.getCurrentRoutes().length;
        Api.exit();
        return true;
      }
      exit = true;
      Alert.toast("再按一下退出");
      setTimeout(()=>{
        exit = false;
      },2000);

      return true;
    };
  }

  return class NavigatorRouter extends Component {

    static contextTypes = {
      router: React.PropTypes.object.isRequired,
    };

    static childContextTypes = {
      addBackButtonListener: React.PropTypes.func,
      removeBackButtonListener: React.PropTypes.func,
    };

    constructor() {
      super();
      this.isSynchronizingRoute = false;
      this.backHandlers = [];

      this.requests = [];
    }

    componentDidMount() {
      BackAndroid.addEventListener('hardwareBackPress', this.handleBackButton);
      this.context.router.listen(this.handleRouteChange);
      Api.router = this.context.router;
      Api.navigator = this.refs.navigator;
    }

    componentWillUnmount() {
      BackAndroid.removeEventListener('hardwareBackPress', this.handleBackButton);
    }

    shouldComponentUpdate() {
      return false;
    }

    getChildContext() {
      return {
        addBackButtonListener: this.addBackButtonListener.bind(this),
        removeBackButtonListener: this.removeBackButtonListener.bind(this),
      };
    }

    addBackButtonListener(listener) {
      this.backHandlers.push(listener);
    }

    removeBackButtonListener(listener) {
      this.backHandlers = this.backHandlers.filter((handler) => handler !== listener);
    }

    handleBackButton =()=>{
      for (let i = this.backHandlers.length - 1; i >= 0; i--) {
        if (this.backHandlers[i]()) {
          return true;
        }
      }

      const { navigator } = this.refs;
      if (navigator) {
        return onBack.apply(this, [navigator.getCurrentRoutes().length]);
      }

      return false;
    }

    handleRouteChangeImpl(location){
      // Skip change route when synchronize route from navigator.
        if (this.isSynchronizingRoute) return;

        const route = {
          location,
          query: location.query,
          component: this.props.children,
        };

        if (location.action === 'PUSH') {
          this.refs.navigator.push(route);
        } else if (location.action === 'POP') {
          const routes = this.refs.navigator.getCurrentRoutes().filter(
            route => (location.pathname === '/' && route.root)
            || (route.location && route.location.key === location.key)
          );
          if(routes){
            this.refs.navigator.popToRoute(routes[0]);
          }

        } else if (location.action === 'REPLACE') {
          this.refs.navigator.replace(route);
        }
    }

    /**
     * [description]
     * @param  {[type]} location [description]
     * @return {[type]}          [description]
     */
    handleRouteChange =(location)=>{
      setTimeout(() => {
        this.handleRouteChangeImpl(location);
      }, 0);
    }

    handleNavigatorDidFocus=(route)=>{
     // console.log(route);
      Notifier.notifyObservers('componentDidFocus',route);
    }
    /*
    handleNavigatorDidFocus(route) {
      const current = this.props.location;
      if ((route.root && current.pathname === '/')
        || (route.location && route.location.key === current.key)) return;
      this.isSynchronizingRoute = true;
      this.context.router.goBack();
      this.isSynchronizingRoute = false;
    }*/

    render() {
      return (
        <Navigator
          ref="navigator"
          style={[styles.container, style]}
          initialRoute={{ root: true, component: this.props.children }}
          renderScene={this.renderScene}
          onDidFocus={this.handleNavigatorDidFocus}
           configureScene={this.configureScene}
        />
      );
    }

    renderScene(route) {
      if (route.component) {
        return React.cloneElement(route.component, {
          location: route.location,
          query: route.query,
        });
      }
      return <View />;
    }
    configureScene(route, routeStack){
      return ({
               ...Navigator.SceneConfigs.PushFromRight,
              gestures: null
       });
    }
  }
};
