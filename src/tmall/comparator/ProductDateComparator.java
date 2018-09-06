package tmall.comparator;

import java.util.Comparator;

import tmall.bean.Product;

public class ProductDateComparator implements Comparator<Product>{

	@Override
	public int compare(Product p1, Product p2) {
		// TODO Auto-generated method stub
		return p2.getCreateDate().compareTo(p1.getCreateDate());
	}

}
