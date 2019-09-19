package com.yunda.base.system.filter;

import com.yunda.base.system.config.SysConfig;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/*", filterName = "CsrfFilter")
public class CsrfFilter implements Filter {
	/**
	 * 日志
	 */
	private static Logger logger = LoggerFactory.getLogger(CsrfFilter.class);

	// 登录过滤
	@Override
	public void doFilter(ServletRequest servletrequest, ServletResponse servletresponse, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletrequest;
		HttpServletResponse response = (HttpServletResponse) servletresponse;

		String referer = request.getHeader("Referer");

		// 若没有配置，那么放弃该功能
		if (referer == null || StringUtils.isBlank(SysConfig.configFilter) || SysConfig.configFilters == null
				|| SysConfig.configFilters.size() < 1) {
			chain.doFilter(request, response);
			return;
		}

		boolean hit = false;
		if (referer != null) {
			for (String cf : SysConfig.configFilters) {
				if (StringUtils.isNotBlank(cf) && referer.contains(cf)) {
					hit = true;
					break;
				}
			}
		}
		if (hit) {
			chain.doFilter(request, response);
			return;
		} else {
			logger.warn("疑似CSRF攻击，referer:" + referer + ",采用配置" + SysConfig.configFilters);
			response.setStatus(403);
			// request.getRequestDispatcher("/403").forward(request, response);
			// throw new ServletException("未知访问");
		}
	}

	/**
	 * description
	 * 
	 * @return
	 */
	public String description() {
		return null;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}
}
