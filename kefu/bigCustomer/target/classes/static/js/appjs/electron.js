var remote = require('static/js/appjs/electron').remote;
var mac = remote.getGlobal('sharedObject').mac;
window.nodeRequire = require;
delete window.require;
delete window.exports;
delete window.module;
console.log(mac);