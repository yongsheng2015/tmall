<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div class="payedDiv">
    <div class="payedTextDiv">
        <img src="img/site/paySuccess.png">
        <span class="payedText">您已成功付款</span>
    </div>
    <div class="payedAddressDiv">
        <ul>
            <li>收货地址信息： ${o.address } ${o.receiver } ${o.mobile }</li>
            <li>实付款：<span class="payedPrice"> ￥<fmt:formatNumber type="number" value="${param.total }" minFractionDigits="2" /></span></li>
            <li>预计${month }月${day }日送达</li>
        </ul>
        <div class="payedAddressLinkDiv">
            您可以
            <a class="payedCheckLink" href="forebought">查看已买到的宝贝</a>
            <a class="payedCheckLink" href="forebought">查看交易详情</a>
        </div>
    </div>
    <div class="payedSeperatedLine"></div>
    <div class="warningDiv">
        <img src="img/site/warning.png">
        <b>安全提醒：</b>下单后，<span class="redColor boldWorld">用QQ给您发送链接办理退款的都是骗子！</span>天猫不存在系统升级，订单异常等问题，谨防假冒客服电话诈骗！
    </div>
</div>