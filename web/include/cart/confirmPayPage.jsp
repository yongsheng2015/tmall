<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>   
    
<div class="confirmPayPageDiv">
            
    <!--26otherDiv_03confirmPayPage--------------------------------------------------------------------------------------->
    <div class="confirmPayImageDiv">
        <img src="img/site/comformPayFlow.png">
        <div class="confirmPayTime1">
        	<fmt:formatDate value="${o.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
        </div>
        <div class="confirmPayTime2">
            <fmt:formatDate value="${o.payDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
        </div>
        <div class="confirmPayTime3">
            <fmt:formatDate value="${o.deliveryDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
        </div>
    </div>
    <div class="confirmPayOrderInfoDiv">
        <div class="confirmPayOrderInfoText">我已收到货，同意支付宝付款</div>
    </div>
    <div class="confirmPayOrderItemDiv">
        <div class="confirmPayOrderItemText">订单信息</div>
        <table class="confirmPayOrderItemTable">
            <thead>
                <tr>
                    <th colspan="2">宝贝</th>
                    <th width="120px">单价</th>
                    <th width="120px">数量</th>
                    <th width="120px">商品总价</th>
                    <th width="120px">运费</th>
                </tr>
            </thead>
            
            <tbody>
            	<c:forEach items="${o.orderItems }" var="oi">
            		<tr class="confirmOrderItemTR">
	                    <td>
	                        <img width="50" src="img/productSingle_middle/${oi.product.firstProductImage.id }.jpg">
	                    </td>
	                    <td class="confirmPayOrderItemProductLink">
	                        <a href="#nowhere">${oi.product.name }</a>
	                    </td>
	                    <td>￥<fmt:formatNumber type="number" value="${oi.product.promotePrice }" minFractionDigits="2" maxFractionDigits="2"/></td>
	                    <td>${oi.number }</td>
	                    <td><span class="conformPayProductPrice">￥<fmt:formatNumber type="number" value="${oi.number*oi.product.promotePrice }" minFractionDigits="2" maxFractionDigits="2"/></span></td>
	                    <td>快递 ： 0.00</td>
	                </tr>
            	</c:forEach>
            </tbody>
        </table>
        <div class="confirmPayOrderItemText pull-right">
        实付款：<span class="confirmPayOrderItemSumPrice">￥<fmt:formatNumber type="number" value="${o.total }" minFractionDigits="2" maxFractionDigits="2"/></span>
        </div>
    </div>
            
            
    <!--27otherDiv_04confirmPayPage2--------------------------------------------------------------------------------------->
    <div class="confirmPayOrderDetailDiv">
        <table class="confirmPayOrderDetailTable">
            <tr>
                <td>订单编号：</td>
                <td>${o.orderCode }<img src="img/site/confirmOrderTmall.png"></td>
            </tr>
            <tr>
                <td>卖家昵称：</td>
                <td>天猫商铺<span class="confirmPayOrderDetailWangWangGif"></span></td>
            </tr>
            <tr>
                <td>收货信息：</td>
                <td>${o.address }，${o.receiver}， ${o.mobile }， ${o.post }</td>
            </tr>
            <tr>
                <td>成交时间：</td>
                <td><fmt:formatDate value="${o.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            </tr>
        </table>
    </div>
    <div class="confirmPayButtonDiv">
        <div class="confirmPayWarning">请收到货后，再确认收货！否则您可能钱货两空！</div>
        <a href="foreorderConfirmed?oid=${o.id }"><button class="confirmPayButton">确认支付</button></a>
    </div>
    
</div>