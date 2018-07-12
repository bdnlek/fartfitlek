package be.bdnlek.fitlek;

import static com.google.common.net.HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS;
import static com.google.common.net.HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS;
import static com.google.common.net.HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS;
import static com.google.common.net.HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN;
import static com.google.common.net.HttpHeaders.ACCESS_CONTROL_MAX_AGE;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class CORSFilter implements Filter {

	// This is the param-name of initialization parameter in web.xml
	private static final String FILTER_PARAM_CORS_ORIGIN_HEADER = "CORSOriginHeader";
	// This private variable will hold the value of FILTER_PARAM_CORS_ORIGIN_HEADER
	private FilterConfig filterConfig = null;
	// This is the default value of ACCESS_CONTROL_ALLOW_ORIGIN in case the value
	// from web.xml in empty
	private static final String DEFAULT_ACCESS_CONTROL_ALLOW_ORIGIN_VALUE = "*";
	// Setup the values of HTTP Headers
	private static final String ACCESS_CONTROL_ALLOW_CREDENTIALS_VALUE = "true";
	private static final String ACCESS_CONTROL_ALLOW_METHODS_VALUE = "POST, GET, OPTIONS, DELETE";
	private static final String ACCESS_CONTROL_MAX_AGE_VALUE = "3600";
	private static final String ACCESS_CONTROL_ALLOW_HEADERS_VALUE = "Origin, Content-Type, Accept, X-Requested-With, Authorization, Access-Control-Request-Method,Access-Control-Request-Headers";

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		HttpServletResponse response = (HttpServletResponse) res;

		String corsOrigin = filterConfig.getInitParameter(FILTER_PARAM_CORS_ORIGIN_HEADER);
		corsOrigin = corsOrigin.isEmpty() ? DEFAULT_ACCESS_CONTROL_ALLOW_ORIGIN_VALUE : corsOrigin;
		response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, corsOrigin);
		response.setHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS, ACCESS_CONTROL_ALLOW_CREDENTIALS_VALUE);
		response.setHeader(ACCESS_CONTROL_ALLOW_METHODS, ACCESS_CONTROL_ALLOW_METHODS_VALUE);
		response.setHeader(ACCESS_CONTROL_MAX_AGE, ACCESS_CONTROL_MAX_AGE_VALUE);
		response.setHeader(ACCESS_CONTROL_ALLOW_HEADERS, ACCESS_CONTROL_ALLOW_HEADERS_VALUE);
		chain.doFilter(req, res);
	}

	@Override
	public void init(FilterConfig filterConfig) {
		this.filterConfig = filterConfig;
	}

	@Override
	public void destroy() {
	}
}