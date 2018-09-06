package tmall.servlet;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tmall.bean.Product;
import tmall.bean.ProductImage;
import tmall.util.ImageUtil;
import tmall.util.Page;

public class ProductImageServlet extends BaseBackServlet{

	@Override
	public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
		// TODO Auto-generated method stub
		Map<String, String> params = new HashMap<>();
		InputStream is = super.parseUpload(request, params);
		
		
		String type = params.get("type");
		int pid = Integer.parseInt(params.get("pid"));
		Product p = productDAO.get(pid);
		
		
		ProductImage pi = new ProductImage();
		pi.setProduct(p);
		pi.setType(type);
		productImageDAO.add(pi);
		
		String fileName = pi.getId()+".jpg";
		String imageFolder = null;
		String imageFolder_small = null;
		String imageFolder_middle = null;
		
		if(productImageDAO.type_single.equals(pi.getType())) {
			imageFolder = request.getSession().getServletContext().getRealPath("img/productSingle");
			imageFolder_small = request.getSession().getServletContext().getRealPath("img/productSingle_small");
			imageFolder_middle = request.getSession().getServletContext().getRealPath("img/productSingle_middle");
		}else
			imageFolder = request.getSession().getServletContext().getRealPath("img/productDetail");
		
		File f = new File(imageFolder, fileName);
		f.getParentFile().mkdirs();
		
		try {
			if(null!=is&&0!=is.available()) {
				try(FileOutputStream fos = new FileOutputStream(f)){
					int length=0;
					byte b[] = new byte[1024*1024];
					while(-1!=(length=is.read(b))) {
						fos.write(b,0,length);
					}
					fos.flush();
					BufferedImage img = ImageUtil.change2jpg(f);
					ImageIO.write(img, "jpg", f);
					
					if(productImageDAO.type_single.equals(pi.getType())) {
						File f_small = new File(imageFolder_small, fileName);
						File f_middle = new File(imageFolder_middle, fileName);
						
						ImageUtil.resizeImage(f,56 , 56, f_small);
						ImageUtil.resizeImage(f, 217, 190, f_middle);
					}
				}catch (Exception e) {
					// TODO: handle exception
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "@admin_productImage_list?pid="+p.getId();
	}

	@Override
	public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
		// TODO Auto-generated method stub
		int id = Integer.parseInt(request.getParameter("id"));
		ProductImage pi = productImageDAO.get(id);
		productImageDAO.delete(id);
		
		String fileName = pi.getId()+".jpg";
		if(productImageDAO.type_single.equals(pi.getType())) {
			String imageFolder_single = request.getSession().getServletContext().getRealPath("img/productSingle");
			String imageFolder_small  = request.getSession().getServletContext().getRealPath("img/productSingle_small");
			String imageFolder_middle = request.getSession().getServletContext().getRealPath("img/productSingle_middle");
			
			File f_single = new File(imageFolder_single, fileName);
			f_single.delete();
			File f_small = new File(imageFolder_small, fileName);
			f_small.delete();
			File f_middle = new File(imageFolder_middle, fileName);
			f_middle.delete();
		}else {
			String imageFolder_detail = request.getSession().getServletContext().getRealPath("img/productDetail");
			File f_detail = new File(imageFolder_detail, fileName);
			f_detail.delete();
		}
		return "@admin_productImage_list?pid="+pi.getProduct().getId();
	}

	@Override
	public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
		// TODO Auto-generated method stub
		int pid = Integer.parseInt(request.getParameter("pid"));
		Product p = productDAO.get(pid);
		
		List<ProductImage> pisSingle = productImageDAO.list(p, productImageDAO.type_single);
		List<ProductImage> pisDetail = productImageDAO.list(p, productImageDAO.type_detail);
		
		request.setAttribute("p", p);
		request.setAttribute("pisSingle", pisSingle);
		request.setAttribute("pisDetail", pisDetail);
		return "admin/listProductImage.jsp";
	}

}
