package com.yunda.base.common.controller;

import com.google.code.kaptcha.Producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.awt.image.BufferedImage;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
public class KaptchaController {
	Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	private Producer captchaProducer;

	@Autowired
	StringRedisTemplate stringRedisTemplate;
	
	// 获取图片验证码,google的kaptcha，img标签src='captcha-image' ，访问
	// http://127.0.0.1:8000/captcha-image
	@RequestMapping(value = "/captcha-image", method = RequestMethod.GET)
	public ModelAndView getKaptchaImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setDateHeader("Expires", 0);
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		response.setHeader("Pragma", "no-cache");
		response.setContentType("image/jpeg");

		String capText = captchaProducer.createText();
		System.out.println("capText: " + capText);

		try {
			String uuid = UUID.randomUUID().toString();
			//JedisUtil.set(uuid, capText, 60 * 5);
			stringRedisTemplate.opsForValue().set(uuid, capText, 60 * 5, TimeUnit.SECONDS);
			log.info("------set验证码uuid-----" + uuid);
			Cookie cookie = new Cookie("captchaCode", uuid);
			response.addCookie(cookie);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		BufferedImage bi = captchaProducer.createImage(capText);
		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(bi, "jpg", out);
		try {
			out.flush();
		} finally {
			out.close();
		}
		return null;
	}

}
