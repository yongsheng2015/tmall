package tmall.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.omg.CORBA.Request;
import org.springframework.web.util.HtmlUtils;

import tmall.bean.Category;
import tmall.bean.Order;
import tmall.bean.OrderItem;
import tmall.bean.Product;
import tmall.bean.ProductImage;
import tmall.bean.PropertyValue;
import tmall.bean.Review;
import tmall.bean.User;
import tmall.comparator.ProductAllComparator;
import tmall.comparator.ProductDateComparator;
import tmall.comparator.ProductPriceComparator;
import tmall.comparator.ProductReviewComparator;
import tmall.comparator.ProductSaleCountComparator;
import tmall.dao.CategoryDAO;
import tmall.util.Page;

public class ForeServlet extends BaseForeServlet {
	
	//	1:不需登录的功能----------------------------------------------------------------------------------------------------
	public String home(HttpServletRequest request,HttpServletResponse response,Page page) {
		
		List<Category> cs =categoryDAO.list();
		productDAO.fill(cs);
		productDAO.fillByRow(cs);
		request.setAttribute("cs", cs);
		
		return "home.jsp";
	}
	
	public String category(HttpServletRequest request,HttpServletResponse response,Page page) {
		int cid = Integer.parseInt(request.getParameter("cid"));
		
		Category c = categoryDAO.get(cid);
		productDAO.fill(c);
		productDAO.setSaleAndReviewNumber(c.getProducts());
		
		String sort = request.getParameter("sort");
		System.out.println("sort = "+sort);
		if(null!=sort) {
			switch (sort) {
				case "review":
					Collections.sort(c.getProducts(), new ProductReviewComparator());
					break;
				case "date":
					Collections.sort(c.getProducts(), new ProductDateComparator());
					break;
				case "saleCount":
					Collections.sort(c.getProducts(), new ProductSaleCountComparator());
					break;
				case "price":
					Collections.sort(c.getProducts(), new ProductPriceComparator());
					break;
				case "all":
					Collections.sort(c.getProducts(), new ProductAllComparator());
					break;
			}
		}
		request.setAttribute("c", c);
		return "category.jsp";
	}
	
	public String product(HttpServletRequest request,HttpServletResponse response,Page page) {
		int pid = Integer.parseInt(request.getParameter("pid"));
		Product p = productDAO.get(pid);
		
		
		List<ProductImage> productSingleImages = productImageDAO.list(p, productImageDAO.type_single);
		List<ProductImage> productDetailImages = productImageDAO.list(p, productImageDAO.type_detail);
		p.setProductSingleImages(productSingleImages);
		p.setProductDetailImages(productDetailImages);
		
		List<PropertyValue> pvs = propertyValueDAO.list(p.getId());
		List<Review> reviews = reviewDAO.list(p.getId());
		productDAO.setSaleAndReviewNumber(p);
		
		request.setAttribute("p", p);
		request.setAttribute("pvs", pvs);
		request.setAttribute("reviews", reviews);
		return "product.jsp";
	}
	
	public String search(HttpServletRequest request,HttpServletResponse response,Page page) {
		String keyword = request.getParameter("keyword");
		List<Product> ps = productDAO.search(keyword, 0, 20);
		productDAO.setSaleAndReviewNumber(ps);
		request.setAttribute("ps", ps);
		
		return "searchResult.jsp";
	}
	
	
	public String register(HttpServletRequest request,HttpServletResponse response,Page page) {
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		name = HtmlUtils.htmlEscape(name);
		System.out.println(name);
		
		boolean exist = userDAO.isExist(name);
		if(exist) {
			request.setAttribute("msg", "用户名已经被使用,不能使用!!");
			return "register.jsp";
		}
		
		User user = new User();
		user.setName(name);
		user.setPassword(password);
		System.out.println(user.getName());
		System.out.println(user.getPassword());
		userDAO.add(user);
		return "@registerSuccess.jsp";
	}
	
	public String login(HttpServletRequest request,HttpServletResponse response,Page page) {
		String name = request.getParameter("name");
		name = HtmlUtils.htmlEscape(name);
		String password = request.getParameter("password");
		User user = userDAO.get(name, password);
		
		if(null==user) {
			request.setAttribute("msg", "账户密码错误");
			return "login.jsp";
		}
		
		request.getSession().setAttribute("user", user);
		return "@forehome";
	}
	
	public String loginAdmin(HttpServletRequest request,HttpServletResponse response,Page page) {
		//此处可继续添加管理员账号
		HashMap<String, String> map = new HashMap<>();
		map.put("路飞", "Sheng2018.");
		
		String name = request.getParameter("name");
		name = HtmlUtils.htmlEscape(name);
		String password = request.getParameter("password");
		User admin = userDAO.get(name, password);
		
		if(null==admin || map.get(name)==null || !map.get(name).equals(password)) {
			request.setAttribute("msg", "管理员账号密码错误");
			return "login_admin.jsp";
		}
		request.getSession().setAttribute("admin", admin);
		return "@admin_category_list";
	}
		
	public String checkLogin(HttpServletRequest request,HttpServletResponse response,Page page) {
		User user =  (User) request.getSession().getAttribute("user");
		if(null!=user) 
			return "%success";
		return "%fail";
	}
	
	public String loginAjax(HttpServletRequest request,HttpServletResponse response,Page page) {
		
		String name = request.getParameter("name");
	    String password = request.getParameter("password");    
	    User user = userDAO.get(name,password);
	     
	    if(null==user){
	        return "%fail";
	    }
	    request.getSession().setAttribute("user", user);
	    
	    return "%success";  
	}
	

	public String logout(HttpServletRequest request,HttpServletResponse response,Page page) {
		request.getSession().removeAttribute("user");
		
		return "@forehome";
	}
	
	
	
	//	2: 需要登录的功能----------------------------------------------------------------------------------------------------	
	public String addCart(HttpServletRequest request,HttpServletResponse response,Page page) {
		int pid = Integer.parseInt(request.getParameter("pid"));
		int num = Integer.parseInt(request.getParameter("num"));
		Product p = productDAO.get(pid);
		
		boolean found = false;
		User user = (User) request.getSession().getAttribute("user");
		
		List<OrderItem> ois = orderItemDAO.listByUser(user.getId());
		for (OrderItem oi : ois) {
			if(oi.getProduct().getId()==p.getId()) {
				oi.setNumber(oi.getNumber()+num);
				orderItemDAO.update(oi);
				found = true;
				break;
			}
		}
		if(!found) {
			OrderItem oi = new OrderItem();
			oi.setNumber(num);
			oi.setProduct(p);
			oi.setUser(user);
			orderItemDAO.add(oi);
		}
		
		return "%success";
	}

	public String cart(HttpServletRequest request,HttpServletResponse response,Page page) {
		User user = (User) request.getSession().getAttribute("user");
		List<OrderItem> ois = orderItemDAO.listByUser(user.getId());
		request.setAttribute("ois", ois);
		
		return "cart.jsp";
	}	
	
	public String deleteOrderItem(HttpServletRequest request,HttpServletResponse response,Page page) {
		User user = (User) request.getSession().getAttribute("user");
		if(null==user) {
			return "%fail";
		}
		
		int oiid = Integer.parseInt(request.getParameter("oiid"));
		orderItemDAO.delete(oiid);
		return "%success";
	}
	
	public String buyone(HttpServletRequest request,HttpServletResponse response,Page page) {
		int pid = Integer.parseInt(request.getParameter("pid"));
		int num = Integer.parseInt(request.getParameter("num"));
		Product p = productDAO.get(pid);
		int oiid = 0;
		
		User user = (User) request.getSession().getAttribute("user");
		boolean found = false;
		List<OrderItem> ois = orderItemDAO.listByUser(user.getId());
		for (OrderItem oi : ois) {
			if(oi.getProduct().getId()==p.getId()) {
				oi.setNumber(oi.getNumber()+num);
				orderItemDAO.update(oi);
				found = true;
				oiid = oi.getId();
				break;
			}
		}
		
		if(!found) {
			OrderItem oi= new OrderItem();
			oi.setNumber(num);
			oi.setProduct(p);
			oi.setUser(user);
			orderItemDAO.add(oi);
			oiid = oi.getId();
		}
		
		return "@forebuy?oiid="+oiid;
	}
		
	public String buy(HttpServletRequest request,HttpServletResponse response,Page page) {
		String[] oiids = request.getParameterValues("oiid");
		List<OrderItem> ois = new ArrayList<>();
		float total = 0;
		
		for (String strid : oiids) {
			int oiid = Integer.parseInt(strid);
			OrderItem oi = orderItemDAO.get(oiid);
			total += oi.getProduct().getPromotePrice()*oi.getNumber();
			ois.add(oi);
		}
		
		request.getSession().setAttribute("ois", ois);
		request.setAttribute("total", total);
		return "buy.jsp";
	}
	
	public String changeOrderItem(HttpServletRequest request,HttpServletResponse response,Page page) {
		User user = (User) request.getSession().getAttribute("user");
		if(null==user) {
			return "%fail";
		}
		
		int oiid = Integer.parseInt(request.getParameter("oiid"));
		int number = Integer.parseInt(request.getParameter("number"));
		OrderItem oi = orderItemDAO.get(oiid);
		oi.setNumber(number);
		orderItemDAO.update(oi);
		
		return "%success";
	}
	
	public String createOrder(HttpServletRequest request,HttpServletResponse response,Page page) {
		User user = (User) request.getSession().getAttribute("user");
		
		List<OrderItem> ois = (List<OrderItem>) request.getSession().getAttribute("ois");
		if(null==ois)
			return "@login.jsp";
		
		String address = request.getParameter("address");
		String post = request.getParameter("post");
		String receiver = request.getParameter("receiver");
		String mobile = request.getParameter("mobile");
		String userMessage = request.getParameter("userMessage");
		
		Order order = new Order();
		String orderCode = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())+RandomUtils.nextInt(10000);
		
		order.setOrderCode(orderCode);
		order.setAddress(address);
		order.setPost(post);
		order.setReceiver(receiver);
		order.setMobile(mobile);
		order.setUserMessage(userMessage);
		
		order.setCreateDate(new Date());
		order.setUser(user);
		order.setStatus(orderDAO.waitPay);
		
		orderDAO.add(order);
		float total = 0;
		for (OrderItem oi : ois) {
			oi.setOrder(order);
			orderItemDAO.update(oi);
			total += oi.getNumber()*oi.getProduct().getPromotePrice();
		}
		
		
		return "@forealipay?oid="+order.getId()+"&total="+total;
	}
	
	public String alipay(HttpServletRequest request,HttpServletResponse response,Page page) {
		
		return "alipay.jsp";
	}
	
	public String payed(HttpServletRequest request,HttpServletResponse response,Page page) {
		int oid = Integer.parseInt(request.getParameter("oid"));
		//int total = Integer.parseInt(request.getParameter("total"));
		Order order = orderDAO.get(oid);
		order.setPayDate(new Date());
		order.setStatus(orderDAO.waitDelivery);
		
		String deliveryDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
		int month = Integer.parseInt(deliveryDate.substring(4, 6));
		int day = Integer.parseInt(deliveryDate.substring(6, 8));
		if(month==1||month==3||month==5||month==7||month==8||month==10||month==12) {
			if(day==31) {
				if(month!=12) {month++;}else{month=1;}
				day = 1;
			}
			else {day++;}
		}else if(month==2) {
			if(day==28||day==29) {month++;day = 1;}else{day++;}
		}else {
			if(day==30) {month++;day = 1;}else{day++;}
		}
		
		orderDAO.update(order);
		
		request.setAttribute("month", month);
		request.setAttribute("day", day);
		request.setAttribute("o", order);
		return "payed.jsp";
	}
	
	public String bought(HttpServletRequest request,HttpServletResponse response,Page page) {
		User user = (User) request.getSession().getAttribute("user");
		List<Order> os = orderDAO.list(user.getId(), orderDAO.delete);
		
		orderItemDAO.fill(os);
		
		request.setAttribute("os", os);
		return "bought.jsp";
	}
	
	public String deleteOrder(HttpServletRequest request,HttpServletResponse response,Page page) {
		int oid = Integer.parseInt(request.getParameter("oid"));
		Order order = orderDAO.get(oid);
		order.setStatus(orderDAO.delete);
		orderDAO.update(order);
		
		return "%success";
	}
	
	public String confirmPay(HttpServletRequest request,HttpServletResponse response,Page page) {
		int oid = Integer.parseInt(request.getParameter("oid"));
		Order order = orderDAO.get(oid);
		orderItemDAO.fill(order);
		
		request.setAttribute("o", order);
		return "confirmPay.jsp";
	}
	
	public String orderConfirmed(HttpServletRequest request,HttpServletResponse response,Page page) {
		int oid = Integer.parseInt(request.getParameter("oid"));
		Order order = orderDAO.get(oid);
		
		order.setConfirmDate(new Date());
		order.setStatus(orderDAO.waitReview);
		orderDAO.update(order);
		
		return "orderConfirmed.jsp";
	}
	
	public String review(HttpServletRequest request,HttpServletResponse response,Page page) {
		int oid = Integer.parseInt(request.getParameter("oid"));
		Order o = orderDAO.get(oid);
		orderItemDAO.fill(o);
		Product p = o.getOrderItems().get(0).getProduct();
		productDAO.setSaleAndReviewNumber(p);
		List<Review> reviews = reviewDAO.list(p.getId());
		
		request.setAttribute("p", p);
		request.setAttribute("o", o);
		request.setAttribute("reviews", reviews);
		
		return "review.jsp";
	}
	
	public String doreview(HttpServletRequest request,HttpServletResponse response,Page page) {
		int oid = Integer.parseInt(request.getParameter("oid"));
		Order o = orderDAO.get(oid);
		o.setStatus(orderDAO.finish);
		orderDAO.update(o);
		
		int pid = Integer.parseInt(request.getParameter("pid"));
		Product p = productDAO.get(pid);
		String content = request.getParameter("content");
		content = HtmlUtils.htmlEscape(content);
		
		User user = (User) request.getSession().getAttribute("user");
		
		Review review = new Review();
		review.setContent(content);
		review.setCreateDate(new Date());
		review.setProduct(p);
		review.setUser(user);
		reviewDAO.add(review);
		
		return "@forereview?oid="+oid+"&showonly=true";
	}
	
}
