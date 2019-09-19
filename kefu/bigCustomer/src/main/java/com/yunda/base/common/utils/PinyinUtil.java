package com.yunda.base.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.stuxuhai.jpinyin.ChineseHelper;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

//开源组件jpinyin，此类只是记录下用法，未封装也无需再次封装
public class PinyinUtil {
	static Logger log = LoggerFactory.getLogger(PinyinUtil.class);
	
	public static void main(String[] args) {
		// ========示例1，汉字转拼音
		try {
			String str = "你好世界";
			// 设置声调表示格式
			System.out.println(PinyinHelper.convertToPinyinString(str, ",", PinyinFormat.WITH_TONE_MARK)); // nǐ,hǎo,shì,jiè
			// 数字表示声调
			System.out.println(PinyinHelper.convertToPinyinString(str, ",", PinyinFormat.WITH_TONE_NUMBER)); // ni3,hao3,shi4,jie4
			// 无声调
			System.out.println(PinyinHelper.convertToPinyinString(str, ",", PinyinFormat.WITHOUT_TONE)); // ni,hao,shi,jie
			// 获取拼音首字母
			System.out.println(PinyinHelper.getShortPinyin(str)); // nhsj
			// 判断是否多音字
			System.out.println(PinyinHelper.hasMultiPinyin('啊'));// true
		} catch (PinyinException e) {
			log.error(e.getMessage(),e);
		}

		// ========示例2，简繁体中文转换
		try {
			// 简体转繁体
			char traditionalChinese = ChineseHelper.convertToTraditionalChinese('义');
			// 繁体转简体
			char simplifiedChinese = ChineseHelper.convertToSimplifiedChinese('義');
			System.out.println(traditionalChinese);
			System.out.println(simplifiedChinese);
			// 判断是否是汉字
			System.out.println(ChineseHelper.isChinese('義'));// true
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}

}
