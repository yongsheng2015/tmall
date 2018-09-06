<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<script>

var deleteOrderItem = false;
var deleteOrderItemid = 0;

$(function(){
	
	$("a.deleteOrderItem").click(function(){
		deleteOrderItem = false;
		var oiid = $(this).attr("oiid");
		deleteOrderItemid = oiid;
		$("#deleteConfirmModal").modal('show');
	});
	$("button.deleteConfirmButton").click(function(){
		deleteOrderItem = true;
		$("#deleteConfirmModal").modal('hide');
	});
	$("#deleteConfirmModal").on('hidden.bs.modal',function(){
		
		if(deleteOrderItem){
			var page = "foredeleteOrderItem";
			$.post(
					page,
					{"oiid":deleteOrderItemid},
					function(result){
						if("success"==result){
							//$("tr.cartProductItemTR[oiid="+deleteOrderItemid+"]").hide();
							location.reload();
						}else{
							location.href="login.jsp";
						}
						
					}
			);
		}
	});
	
	$("img.cartProductItemIfSelected").click(function(){
		var selectit = $(this).attr("selectit");
		if("false"==selectit){
			$(this).attr("src","img/site/cartSelected.png");
			$(this).attr("selectit","selectit");
			$(this).parents("tr.cartProductItemTR").css("background-color","#FFF8E1");
		}else{
			$(this).attr("src","img/site/cartNotSelected.png");
			$(this).attr("selectit","false");
			$(this).parents("tr.cartProductItemTR").css("background-color","#fff");
		}
		syncSelect();
		syncCreateOrderButton();
		calcCartSumPriceAndNumber();
	});
	
	$("img.selectAllItem").click(function(){
		var selectit = $(this).attr("selectit");
		if("false"==selectit){
			$("img.selectAllItem").attr("src","img/site/cartSelected.png");
			$("img.selectAllItem").attr("selectit","selectit");
			$(".cartProductItemIfSelected").each(function(){
				$(this).attr("src","img/site/cartSelected.png");
				$(this).attr("selectit","selectit");
				$(this).parents("tr.cartProductItemTR").css("background-color","#FFF8E1");
			});
		}else{
			$("img.selectAllItem").attr("src","img/site/cartNotSelected.png");
			$("img.selectAllItem").attr("selectit","false");
			$(".cartProductItemIfSelected").each(function(){
				$(this).attr("src","img/site/cartNotSelected.png");
				$(this).attr("selectit","false");
				$(this).parents("tr.cartProductItemTR").css("background-color","#fff");
			});
		}
		syncCreateOrderButton();
		calcCartSumPriceAndNumber();
	});
	
	$(".orderItemNumberSetting").keyup(function(){
		var pid = $(this).attr("pid");
		var stock = $("span.orderItemStock[pid="+pid+"]").text();
		var price = $("span.orderItemPromotePrice[pid="+pid+"]").text();
		var num = $(this).val();
		num = parseInt(num);
		if(isNaN(num))
			num = 1;
		if(num>stock)
			num = stock;
		if(num<=0)
			num = 1;
		syncPrice(pid,num,price);
		
		var page = "forechangeOrderItem";
		var oiid = $(this).attr("oiid");
		$.post(
				page,
				{"oiid":oiid,"number":num},
				function(result){
					if("success"!=result){
						location.href = "login.jsp";
					}
				}
		);
	});
	
	$(".numberPlus").click(function(){
		var pid = $(this).attr("pid");
		var stock = $("span.orderItemStock[pid="+pid+"]").text();
		var price = $("span.orderItemPromotePrice[pid="+pid+"]").text();
		var num = $(".orderItemNumberSetting[pid="+pid+"]").val();
		num++;
		if(num>stock)
			num = stock;
		syncPrice(pid,num,price);
		
		var page = "forechangeOrderItem";
		var oiid = $(this).attr("oiid");
		$.post(
				page,
				{"oiid":oiid,"number":num},
				function(result){
					if("success"!=result){
						location.href = "login.jsp";
					}
				}
		);
	});
	
	$(".numberMinus").click(function(){
		var pid = $(this).attr("pid");
		var stock = $("span.orderItemStock[pid="+pid+"]").text();
		var price = $("span.orderItemPromotePrice[pid="+pid+"]").text();
		var num = $(".orderItemNumberSetting[pid="+pid+"]").val();
		num--;
		if(num<=0)
			num = 1;
		syncPrice(pid,num,price);
		
		var page = "forechangeOrderItem";
		var oiid = $(this).attr("oiid");
		$.post(
				page,
				{"oiid":oiid,"number":num},
				function(result){
					if("success"!=result){
						location.href = "login.jsp";
					}
				}
		);
	});
	
	$(".createOrderButton").click(function(){
		var params = "";
		$(".cartProductItemIfSelected").each(function(){
			if("selectit"==$(this).attr("selectit")){
				var oiid = $(this).attr("oiid");
				params += "&oiid="+oiid;
			}
		});
		params = params.substring(1);
		location.href = "forebuy?"+params;
	});
	
	
	
	function formatMoney(num){
	    num = num.toString().replace(/\$|\,/g,'');  
	    if(isNaN(num))  
	        num = "0";  
	    sign = (num == (num = Math.abs(num)));  
	    num = Math.floor(num*100+0.50000000001);  
	    cents = num%100;  
	    num = Math.floor(num/100).toString();  
	    if(cents<10)  
	    cents = "0" + cents;  
	    for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)  
	    num = num.substring(0,num.length-(4*i+3))+','+  
	    num.substring(num.length-(4*i+3));  
	    return (((sign)?'':'-') + num + '.' + cents);  
	}
	
	function syncCreateOrderButton(){
		var selectAny = false;
		$(".cartProductItemIfSelected").each(function(){
			if("selectit"==$(this).attr("selectit"))
				selectAny = true;
		});
		if(selectAny){
			$("button.createOrderButton").css("background-color","#C40000");
			$("button.createOrderButton").removeAttr("disabled");
		}else{
			$("button.createOrderButton").css("background-color","#AAAAAA");
			$("button.createOrderButton").attr("disabled","disabled");
		}
	}
	
	function syncSelect(){
		var selectAll = true;
		$(".cartProductItemIfSelected").each(function(){
			if("false"==$(this).attr("selectit"))
				selectAll = false;	
		});
		
		if(selectAll){
			$("img.selectAllItem").attr("src","img/site/cartSelected.png");
			$("img.selectAllItem").attr("selectit","selectit");
		}else{
			$("img.selectAllItem").attr("src","img/site/cartNotSelected.png");
			$("img.selectAllItem").attr("selectit","false");
		}
	}
	
	function calcCartSumPriceAndNumber(){
		var sumPrice = 0;
		var totalNumber = 0;
		$("img.cartProductItemIfSelected[selectit='selectit']").each(function(){
			var oiid = $(this).attr("oiid");
			
			var num = $(".orderItemNumberSetting[oiid="+oiid+"]").val();
			num = new Number(num);
			totalNumber += num;
			
			var price = $(".cartProductItemSmallSumPrice[oiid="+oiid+"]").text();
			price = price.replace(/,/g,"");
			price = price.replace(/￥/g,"");
			price = new Number(price);
			sumPrice += price;
		});
		$("span.cartTitlePrice").html("￥"+formatMoney(sumPrice));
		$("span.cartSumPrice").html("￥"+formatMoney(sumPrice));
		$("span.cartSumNumber").html(totalNumber);
	}
	
	function syncPrice(pid,num,price){
		$(".orderItemNumberSetting[pid="+pid+"]").val(num);
		var cartProductItemSmallSumPrice = formatMoney(num*price);
		$(".cartProductItemSmallSumPrice[pid="+pid+"]").html("￥"+cartProductItemSmallSumPrice);
		calcCartSumPriceAndNumber();
	}
	
});
</script>

<div class="cartDiv">
    <div class="cartTitle pull-right">
        <span>已选商品 (不含运费) </span>
        <span class="cartTitlePrice">￥0.00</span>
        <button class="createOrderButton" disabled="disabled">结 算</button>
    </div>
	<div class="cartProductList">
	    <table class="cartProductTable">
	        <thead>
	            <tr>
	                <th class="selectAndImage">
	                    <img class="selectAllItem" selectit="false" src="img/site/cartNotSelected.png">
	                                    全选
	                </th>
	                <th>商品信息</th>
	                <th>单价</th>
	                <th>数量</th>
	                <th width="120px">金额</th>
	                <th class="operation">操作</th>
	            </tr>
	        </thead>
	        
	        <tbody>
	        <c:forEach items="${ois }" var="oi">
        		<tr class="cartProductItemTR" oiid="${oi.id }">
	                <td>
	                    <img class="cartProductItemIfSelected" selectit="false" oiid="${oi.id }" src="img/site/cartNotSelected.png">
	                    <a href="#nowhere" style="display: none"><img src="img/site/cartSelected.png"></a>
	                   <img width="40px" class="cartProductImg" src="img/productSingle_middle/${oi.product.firstProductImage.id }.jpg">
	               </td>
	               <td>
	                   <div class="cartProductLinkOutDiv">
	                       <a class="cartProductLink" href="foreproduct?pid=${oi.product.id }">${oi.product.name }</a>
	                       <div class="cartProductLinkInnerDiv">
	                           <img title="支持信用卡支付" src="img/site/creditcard.png">
	                           <img title="消费者保障服务,承诺7天退货" src="img/site/7day.png">
	                           <img title="消费者保障服务,承诺如实描述" src="img/site/promise.png">
	                       </div>
	                   </div>
	               </td>
	               <td>
	                   <span class="cartProductItemOringalPrice">￥${oi.product.orignalPrice }</span>
	                   <span class="cartProductItemPromotionPrice">￥${oi.product.promotePrice }</span>
	               </td>
	               <td>
	                   <div class="cartProductChangeNumberDiv">
	                       <span pid="${oi.product.id }" class="hidden orderItemStock">${oi.product.stock }</span>
	                       <span pid="${oi.product.id }" class="hidden orderItemPromotePrice">${oi.product.promotePrice }</span>
	                       <a class="numberMinus" href="#nowhere" pid="${oi.product.id }"  oiid="${oi.id }">-</a>
	                       <input value="${oi.number }" autocomplete="off" class="orderItemNumberSetting" pid="${oi.product.id }" oiid="${oi.id }">
	                       <a class="numberPlus" href="#nowhere" pid="${oi.product.id }" oiid="${oi.id }" stock="${oi.product.stock }">+</a>
	                   </div>
	               </td>
	               <td>
	                   <span pid="${oi.product.id }" oiid="${oi.id }" class="cartProductItemSmallSumPrice">
	                   	     ￥<fmt:formatNumber type="number" value="${oi.product.promotePrice*oi.number }" minFractionDigits="2"/>
	                   </span>
	               </td>
	               <td>
	                   <a class="deleteOrderItem" href="#nowhere" oiid="${oi.id }">删除</a>
	               </td>
	           </tr>
	        </c:forEach>
	            
	        </tbody>
	    </table>
	</div>
	
	<div class="cartFoot">
	    <img class="selectAllItem" selectit="false" src="img/site/cartNotSelected.png">
	    <span>全选</span>
	    <div class="pull-right">
	        <span>已选商品<span class="cartSumNumber">0</span>件</span>
	        <span>合计 (不含运费): </span>
	        <span class="cartSumPrice">￥0.00</span>
	        <button class="createOrderButton" disabled="disabled">结 算</button>
	    </div>
	</div>
	
</div>
    