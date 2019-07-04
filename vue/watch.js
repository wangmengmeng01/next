// var obj = {};
// var initValue = 'hello';
// Object.defineProperty(obj,"newKey",{
//     get:function (){
//         //当获取值的时候触发的函数
//         return initValue;
//     },
//     set:function (value){
//         //当设置值的时候触发的函数,设置的新值通过参数value拿到
//         initValue = value;
//     }
// });
// //获取值
// console.log( obj.newKey );  //hello

// //设置值
// obj.newKey = 'change value';

// console.log( obj.newKey ); //change value

var data = {
  name:'wang',
  age:18,
  deep:{
    name:'deep'
  }
}
var w = 1;
setTimeout(() => { w = 2 ;console.log(w,'w')}, 1111)
var d = data;
var watch = {
  name(old,val) {
    // data.[watch.name]
    // console.log(this,'this')
    console.log('我改变了','this')
    document.querySelector("#app").innerHTML = `<p>${old}</p>`+val;
    return w
  },
  // age(val) {
  //   // data.[watch.name]
  //   console.log(this,'this')
  // },
  deep(val) {
    console.log(val,'deep')
  }
}
function get(vm,key) {
  let arr = key.split('.');
  console.log(key,'key');
  // return data[key];
  for(let i = 0;i < arr.length; i++){
    if(i === 0){
      obj = vm[arr[i]]
    }else {
      obj = obj[arr[i]]
    }
  }
  return obj;
}
// var value = data.name;
// Object.defineProperty(data, 'name', {
//     enumerable:true,
//     configurable:true,
//     get() {
//       console.log(this,'this')
//       // console.log(data.key,'get')
//       return value;
//     },
//     set(val) {
//       // watch[key](val);
//     }
//   })

// console.log(data.name,'a')
var B = 1;
var objCom = {
  A() {
    return B
  }
}
var flag1 = objCom.A;
Object.defineProperty(objCom,'A',{
  get() {
    return 1111111
  },
  set(val) {
    console.log(val,'com')
  }
})
B = 2;
console.log(objCom.A,'AAAAA')
console.log(flag1 == objCom.A,"ccccccccccccc")
Object.keys(watch).forEach((key) => {defineNative(key,data[key])})
function defineNative(key,value) {
  Object.defineProperty(data, key, {
    enumerable:true,
    configurable:true,
    get() {
      console.log(value,'get')
      return value;
    },
    set(val) {
      watch[key](value,val);
    }
  })
}
console.log(data.name,'a')
var obj1 = {
  name:'tom',
  age:18
}
var arr1 = [].slice.call([1,1,1]);
console.log(arr1,'slice')
// var key = Object.keys(watch);
// var keyObj = []
// key.forEach((item) => {
//   console.log(watch[item],'item')
//   keyObj.push({item:item,obj:{
//     enumerable: true,
//     configurable: true,
//     get: function reactiveGetter (val) {
//       // watch[item](val);
//       return val;
//     },
//     set: function reactiveSetter (val) {
//       watch[item](val)
//     }
//   }})
// })
// console.log(key,'key')
// console.log(keyObj,'keyObj')

// for(var i = 0;i<keyObj.length;i++){
//   Object.defineProperty(data,keyObj[i].name,keyObj[i].obj)
// }
// Object.defineProperty(data,'name',{
//   // value:'w',
//   get(val) {
//     console.log(val,'get')
//     watch.name();
//   },
//   set(val) {
//     console.log(val,'set')
//     console.log(this,'name-set')
//     watch.name();
//   }
// })
// data.name = 'a'
// data.deep.name = 'b'
// console.log(data.name,'data.name')
// console.log(data.age,'data.name')
var templater = `<div>${data.name}</div>`;
var str = 'wang';
data.name = 'tom'
// document.querySelector("#app").innerHTML = data.name;

