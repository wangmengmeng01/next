let co = require('co')
let fs = require('fs')
let gen = function*() {
	let con1 = yield fs.readFileSync(__dirname+'/a.text','utf8')
	let con2 = yield fs.readFileSync(__dirname+'/b.text','utf8')
	console.log(con1,'=======')
};
co(gen).then(function() {
	console.log('res')
})
// let c = gen()
// c.next();
// c.next();
// c.next();
	let con1 = fs.readFileSync(__dirname+'/a.text','utf8')
	console.log(con1)
