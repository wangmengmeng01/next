export default {
	mobile(rule, value, callback) {
		if (value === '') {
			callback();
		} else if (!(/^1\d{10}$/.test(value))) {
			callback(new Error('请输入正确的手机号'));
		}else {
			callback();
		}
	},
	email(rule, value, callback) {
		if (value === '') {
			callback();
		} else if (!(/^[a-zA-Z0-9_-]+@([a-zA-Z0-9]+\.)+(com|cn|net|org)$/.test(value))) {
			callback(new Error('请输入正确的邮箱'));
		}else {
			callback();
		}
	},
}