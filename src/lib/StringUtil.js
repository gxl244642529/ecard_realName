//评分格式化：将3.33333333更改为3.3(缺陷代码)
export function formatScore(val){
    var fix=parseFloat(val).toFixed(1);
    var fl=parseFloat(fix);
    let n=  Math.floor(fl * 10 / 10) ;
    let m = fl * 10 % 10;
    return n + "." + (m == 0 ? "0" : (m*10/10) );
}

//TitleBar名称过长省略
export function formatTitle(str){
    if(!str)
      var str="";
    var len=str.length;
    if(len>10){
      return str.substr(0,8)+".."
    }
    return str
}
//日期格式化：将20161010更改格式为2016-10-10
export function formatDate(val){
  return val.substring(0,4)+"-"+val.substring(4,6)+"-"+val.substring(6,8);
}
//日期格式化：将20161010更改格式为2016-10-10 10:10:10
export function format15Time(val){
  return val.substring(0,4)+"-"+val.substring(4,6)+"-"+val.substring(6,8)+val.substring(8,11)+":"+val.substring(11,13)+":"
  +val.substring(13,15);
}
//日期格式化：将20161010更改格式为2016-10-10 10:10:10
export function format14Time(val){
  return val.substring(0,4)+"-"+val.substring(4,6)+"-"+val.substring(6,8)+" "+val.substring(8,10)+":"+val.substring(10,12)+":"
  +val.substring(12,14);
}

//将分改成元
export function formatFee(val){
  return val/100;
}
