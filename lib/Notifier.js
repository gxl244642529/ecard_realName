
const Observers = {};


//onNotify(name,data)

export default class Notifier{

	static addObserver(name,func){
		let arr = Observers[name];
		if(arr){
			arr.push(func);
		}else{
			Observers[name]=[func];
		}
	}

	static removeObserver(name,func){
		let arr = Observers[name];
		if(arr){
			arr.splice(arr.indexOf(func),1);
			if(arr.length==0){
				delete Observers[name];
			}
		}
	}

	static notifyObservers(name,data){
		let observers = Observers[name];
		let called = false;
		if(observers){
			for(let i=0, c=observers.length ; i < c ; ++i){
				let observer = observers[i];
				if(true === observer.apply(null,[name,data])){
					called = true;
				}
			}
		}
		return called;
	}

}
