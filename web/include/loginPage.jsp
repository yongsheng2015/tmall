<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
	
<script>
$(function(){
	<c:if test="${!empty msg}">
		$("span.errorMessage").html("${msg}");
		$("div.loginErrorMessageDiv").show();
	</c:if>
	
	$(".loginForm").submit(function(){
		if(0==$("#name").val().length||0==$("#password").val().length){
			$("span.errorMessage").html("请输入账号密码");
			$("div.loginErrorMessageDiv").show();
			return false;
		}
		
		return true;
	});
	
	$("form.loginForm input").keyup(function(){
		$("div.loginErrorMessageDiv").hide();
	});
	
	var left = window.innerWidth/2 + 162;
	$("div.loginSmallDiv").css("left",left);
});
</script>	

<div class="loginDiv" style="position:relative">
    <div class="simpleLogo">
        <img src="img/site/simpleLogo.png">
    </div>
    <img class="loginBackgroundImg" id="loginBackgroundImg" src="img/site/loginBackground.png">
    <form class="loginForm" action="forelogin" method="post">
    	<div id="loginSmallDiv" class="loginSmallDiv">
    		
    		<div class="loginErrorMessageDiv">
    			<div class="alert alert-danger">
    				<button type="button" class="close" data-dismiss="alert" aria-label="Close"></button>
    				<span class="errorMessage"></span>
    			</div>
    		</div>
    	
	        <div class="login_acount_test">账户登录</div>
	        <div class="loginInput">
	            <span class="loginInputIcon">
	                <span class="glyphicon glyphicon-user"></span>
	            </span>
	            <input type="text" placeholder="手机/会员名/邮箱" name="name" id="name">
	        </div>
	        <div class="loginInput">
	            <span class="loginInputIcon">
	                <span class="glyphicon glyphicon-lock"></span>
	            </span>
	            <input type="password" placeholder="密码" name="password" id="password">
	        </div>
	        
	        <span class="text-danger">不要输入真实的天猫账号密码！！</span>
	        
	        <div>
	            <a href="#nowhere" class="notImplementLink">忘记登录密码</a>
	            <a href="register.jsp" class="pull-right">免费注册</a>
	        </div>
	        <div style="margin-top: 20px">
	            <button type="submit" class="btn btn-block redButton">登录</button>
	        </div>
	    </div>
    </form>
</div>