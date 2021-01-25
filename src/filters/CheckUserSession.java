package filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CheckUserSession implements Filter{
		
	public void init(FilterConfig fConfig) throws ServletException {
		//Init
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession();
		if (session.isNew() || session.getAttribute("user") == null) {
			String loginpath = req.getServletContext().getContextPath() + "/index.html";
			res.sendRedirect(loginpath);
			return;
		}
		chain.doFilter(request, response);
	}
	
	public void destroy() {
		//Destroy
	}

}
