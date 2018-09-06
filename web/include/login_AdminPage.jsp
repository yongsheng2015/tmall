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
    <form class="loginForm" action="foreloginAdmin" method="post">
    	<div id="loginSmallDiv" class="loginSmallDiv">
    		
    		<div class="loginErrorMessageDiv">
    			<div class="alert alert-danger">
    				<button type="button" class="close" data-dismiss="alert" aria-label="Close"></button>
    				<span class="errorMessage"></span>
    			</div>
    		</div>
    	
	        <div><span class="text-danger">管理员账号登录</span></div>
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
	        
	        <div style="margin-top: 20px">
	            <button type="submit" class="btn btn-block redButton">登录</button>
	        </div>
	    </div>
    </form>
</div>