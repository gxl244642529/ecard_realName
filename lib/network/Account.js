import {AsyncStorage} from 'react-native'

import Log from '../Log'

const AccountModule = require('react-native').NativeModules.AccountModule;

const VERSION = "1.0.4";
const ACCOUNT_KEY = "account_key";



export default class Account{

    static get(){
        return Account.user;
    }

    static isLogin(){
        return Account.user !== undefined;
    }

    static load(callback){
        AccountModule.load((user)=>{
            Log.info(user);
            Account.user=user;
            callback(user);
        });

    }

    static append(data){
        Account.user = Object.assign({},Account.user,data);
        Account.save();
    }

    static save(){
        AccountModule.save(Account.user);
    }

}
