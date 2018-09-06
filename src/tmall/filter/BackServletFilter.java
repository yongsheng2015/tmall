package tmall.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import tmall.bean.User;


public class BackServletFilter implements Filter {

	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		String uri = request.getRequestURI();
		String contextPath=request.getServletContext().getContextPath();
		uri =StringUtils.remove(uri, contextPath);
		
		if(uri.startsWith("/admin_")){		
			String servletPath = StringUtils.substringBetween(uri,"_", "_") + "Servlet";
			String method = StringUtils.substringAfterLast(uri,"_" );
			request.setAttribute("method", method);
			req.getRequestDispatcher("/" + servletPath).forward(request, response);
			return;
		}
		
		chain.doFilter(request, response);
	}


	public void init(FilterConfig arg0) throws ServletException {
	
	}
}
