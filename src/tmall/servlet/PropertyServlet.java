package tmall.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tmall.bean.Category;
import tmall.bean.Property;
import tmall.util.Page;

public class PropertyServlet extends BaseBackServlet{

	@Override
	public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
		// TODO Auto-generated method stub
		/*int cid = Integer.parseInt(request.getParameter("cid"));
		Category c = categoryDAO.get(cid);
		
		String name = request.getParameter("name");
		Property pt = new Property();
		pt.setName(name);
		pt.setCategory(c);
		propertyDAO.add(pt);
		System.out.println(cid);
		//return "@admin_property_list?cid="+cid;
		return null;*/
		
		int cid = Integer.parseInt(request.getParameter("cid"));
	    Category c = categoryDAO.get(cid);
	     
	    String name= request.getParameter("name");
	    Property p = new Property();
	    p.setCategory(c);
	    p.setName(name);
	    propertyDAO.add(p);
	    return "@admin_property_list?cid="+cid;
	}

	@Override
	public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
		// TODO Auto-generated method stub
		int id = Integer.parseInt(request.getParameter("id"));
		Property pt = propertyDAO.get(id);
		propertyDAO.delete(id);
		return "@admin_property_list?cid="+pt.getCategory().getId();
	}

	@Override
	public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
		// TODO Auto-generated method stub
		int id=Integer.parseInt(request.getParameter("id"));
		Property pt = propertyDAO.get(id);
		request.setAttribute("pt", pt);
		
		return "admin/editProperty.jsp";
	}

	@Override
	public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
		// TODO Auto-generated method stub
		int cid = Integer.parseInt(request.getParameter("cid"));
		Category c = categoryDAO.get(cid);
		int id = Integer.parseInt(request.getParameter("id"));
		String name = request.getParameter("name");
		Property pt = new Property();
		pt.setCategory(c);
		pt.setId(id);
		pt.setName(name);
		propertyDAO.update(pt);
		
		return "@admin_property_list?cid="+pt.getCategory().getId();
	}

	@Override
	public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
		// TODO Auto-generated method stub
		int cid = Integer.parseInt(request.getParameter("cid"));
		Category c = categoryDAO.get(cid);
		List<Property> pts = propertyDAO.list(cid, page.getStart(), page.getCount());
		int total = propertyDAO.getTotal(cid);
		page.setTotal(total);
		page.setParam("&cid="+c.getId());
		
		request.setAttribute("pts", pts);
		request.setAttribute("c", c);
		request.setAttribute("page", page);
		
		return "admin/listProperty.jsp";
	}

}
