<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


<div class="productReviewDiv">
    <div class="productReviewTopPart">
        <a href="#nowhere" class="productReviewTopPartSelectedLink">商品详情</a>
        <a href="#nowhere" class="selected">累计平均 <span class="productReviewTopReviewLinkNumber">${p.reviewCount }</span></a>
    </div>  
    <div class="productReviewContentPart">
    	<c:forEach items="${reviews }" var="rv">
    		<div class="productReviewItem">
		       <div class="productReviewItemDesc">
		           <div class="productReviewItemContent">
		               ${rv.content }
		           </div>
		           <div class="productReviewItemDate">
		           		<fmt:formatDate value="${rv.createDate }" pattern="yyyy-MM-dd"/>
		           </div>
		       </div>
		       <div class="productReviewItemUserInfo">
		           ${rv.user.anonymousName }<span class="userInfoGrayPart">（匿名）</span>
		       </div>
		       <div style="clear:both"></div>
		   	</div>
    	</c:forEach>
	   
	 </div>
    
</div>