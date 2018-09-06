<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false" %>
	
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>



<div class="homepageCategoryProducts">

	<c:forEach items="${cs }" var="c" varStatus="stc">
		<div class="eachHomepageCategoryProducts">
	        <div class="left-mark"></div>
	        <span class="categoryTitle">${c.name }</span>
	        <br>
	        
	        <c:forEach items="${c.products }" var="p" varStatus="st">
		        <c:if test="${st.count<=5 }">
		        	<div class="productItem">
			            <a href="foreproduct?pid=${p.id }"><img width="100px" 
			            		src="img/productSingle_middle/${p.firstProductImage.id }.jpg">
			            </a>
			            <a href="foreproduct?pid=${p.id }" class="productItemDescLink">
			                <span class="productItemDesc">
			                [热销]${fn:substring(p.name,0,20)}
			                </span>
			            </a>
			            <span class="productPrice">
			            	<fmt:formatNumber type="number" value="${p.promotePrice }" minFractionDigits="2"/>
			            </span>
			        </div>
		        </c:if>
	        </c:forEach>
	        <div style="clear:both"></div>
	    </div>
	
	</c:forEach>
    <img id="endpng" class="endpng" src="img/site/end.png">

</div>