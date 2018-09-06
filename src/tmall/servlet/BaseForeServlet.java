package tmall.servlet;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tmall.dao.CategoryDAO;
import tmall.dao.OrderDAO;
import tmall.dao.OrderItemDAO;
import tmall.dao.ProductDAO;
import tmall.dao.ProductImageDAO;
import tmall.dao.PropertyDAO;
import tmall.dao.PropertyValueDAO;
import tmall.dao.ReviewDAO;
import tmall.dao.UserDAO;
import tmall.util.Page;

public class BaseForeServlet extends HttpServlet{
	
	protected CategoryDAO categoryDAO = new CategoryDAO();
	protected PropertyDAO propertyDAO = new PropertyDAO();
	protected ProductDAO productDAO = new ProductDAO();
	protected PropertyValueDAO propertyValueDAO = new PropertyValueDAO();
	protected ProductImageDAO productImageDAO = new ProductImageDAO();
	protected UserDAO userDAO = new UserDAO();
	protected ReviewDAO reviewDAO = new ReviewDAO();
	protected OrderDAO orderDAO = new OrderDAO();
	protected OrderItemDAO orderItemDAO = new OrderItemDAO();
	
	public void service(HttpServletRequest request,HttpServletResponse response) {
		
		try {
			int start = 0;
			int count = 10;
			try {
				start = Integer.parseInt(request.getParameter("page.start"));
			}catch (Exception e) {
				// TODO: handle exception
			}
			
			try {
				count = Integer.parseInt(request.getParameter("page.count"));
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			Page page = new Page(start, count);
			String method = (String) request.getAttribute("method");
			Method m = this.getClass().getMethod(method, javax.servlet.http.HttpServletRequest.class,
					javax.servlet.http.HttpServletResponse.class,Page.class);
			String redirect = m.invoke(this, request,response,page).toString();
			
			if(redirect.startsWith("@"))
				response.sendRedirect(redirect.substring(1));
			else if(redirect.startsWith("%"))
				response.getWriter().print(redirect.substring(1));
			else {
				request.getRequestDispatcher(redirect).forward(request, response);
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new RuntimeException(e);
		}		
	}
}
