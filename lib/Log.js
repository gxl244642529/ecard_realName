const DEBUG = true;

const Log = {
	info:function(){
		if(!DEBUG)return;
		console.log(arguments);
	},
	warn:function(){
		if(!DEBUG)return;
		console.warn(arguments);
	},
	error:function(){
		if(!DEBUG)return;
		console.error(arguments);
	}

}

module.exports = Log;