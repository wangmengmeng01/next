const INITALLOGO = 'home/INITALLOGO';
const CHANGEHISTORY = 'home/CHANGEHISTORY';

const initialState = {
  movelogo: false
};

// export default function me(state = initialState, action = {}) {
//   switch (action.type) {
//     case 'flag1':
//     	console.log(action.data)
//       return action.data;

//     case 'flag2':
//       return {
//         ...state,
//         movelogo: true,
//         text: action.text
//       };
//     default:
//       return {
//       	name:'jack',
//       	age:18,
//       };
//   }
// }

export function setTest(obj) {
  return {
  	type:'flag1',
  	data:obj
  }
}
