<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String	topId=(String)request.getParameter("topId");

%>
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
   <title>足布注册</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
 	<script src="js/jquery-1.9.1.min.js"></script>
<link rel="stylesheet" href="css/bootstrap.css" />
<script src="js/bootstrap.js"></script>
<script src="js/jquery.md5.js"></script>
	<style type="text/css">
      body {
        padding-top: 25px;
        padding-bottom: 30px;
        background:url('background1242x2208.png') ;
		background-size: 100% 100%;
		-o-background-size: 100% 100%;
		-moz-background-size: 100% 100%;
		-webkit-background-size: 100% 100%;
		
      }
	p{
		padding-top: 10px;
	    margin-left:auto; 
		margin-right:auto;
		font-size:12px;
		color:#FFFFFF;
	}
	div[name=cover] {
    position:fixed; top: 0px; right:0px; filter: alpha(opacity=60);
   
    z-index: 1002; left: 0px; display:none;
    }
    </style>
	<script type="text/javascript">
function changePass(){
	var type=$("#password").attr("type");
	if(type=="text"){
 		$("#password").attr("type","password");
	}else if(type=="password"){
 		$("#password").attr("type","text");
	}
}

    function CheckIsMobile(mobile){ 
         
        if(isNaN(mobile)||(mobile.length!=11)){ 
            return false; 
        } 
         
        var reg = /(^13\d{9}$)|(^14)[5,7]\d{8}$|(^15[0,1,2,3,5,6,7,8,9]\d{8}$)|(^17)[6,7,8]\d{8}$|(^18\d{9}$)/g ;  
        if(!reg.test(mobile)) 
        {             
            return false; 
        } 
        return true; 
    } 
    var msgCode="";
    var wait=60;  
    var click=true;
    var register=true;
	function time(o) {  
        if (wait == 0) {  
           // $(o).removeAttribute("disabled");            
            $(o).html("获取验证码");  
            wait = 60; 
            click=true; 
        } else {  
            //$(o).setAttribute("disabled", true);
            if(click){
            	click=false;
            }  
            $(o).html("已发送("+ wait+")");  
            wait--;  
            setTimeout(function() {  
                time(o) ;
            },  
            1000) ; 
        }  
    }  
function getYZ(o){
	if(click){
		click=false;
	var phone=$("#mobilePhone").val();
	if(phone==null || phone.replace(/(^\s*)|(\s*$)/g,'')=="" ){ 
			click=true;
			alert("请输入手机号码");
            return false; 
     }    
	if(!CheckIsMobile(phone)){
		click=true;
		alert("请输入正确的手机号码");
		return false;	
	}
	var ajaxCallUrl="<%=path%>/appKu/query.do?STYPE=1003";
	var subStr='DATA={"mobilePhone":"'+$("#mobilePhone").val()+'","register":"1"}';
	$.ajax({
                type: "POST",
                url:ajaxCallUrl,
                data:subStr,// 你的formid
                dataType:"json",
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                	click=true;
                    alert("获取验证码出错!");
                },
                success: function(data) {
                	click=true;
                    if(data.code=="100"){
                    	msgCode=data.msgCode;
                    	 time(o);
                    } else{
                    	alert(data.msg);
                    }                  
                }
            });
            	
	}

}

function submit1(){
	if(register){
		register=false;
	var phone=$("#mobilePhone").val();
	if(!CheckIsMobile(phone)){
		register=true;
		alert("请输入正确的手机号码!");
		return false;	
	}
	
	var password=$("#password").val();
	if(password==null || password.replace(/(^\s*)|(\s*$)/g,'')=="" ){ 
			register=true;
        	alert("请输入密码!");
            return false; 
      } 
     var yzcode=$("#msgCode").val();
	if(yzcode==null || yzcode.replace(/(^\s*)|(\s*$)/g,'')=="" ){ 
			register=true;
        	alert("请输入验证码!");
            return false; 
       }    
     if(msgCode != yzcode){
    	 register=true;
    	 alert("验证码错误!");
         return false; 
      } 
	var ajaxCallUrl="<%=path%>/appKu/write.do?STYPE=200001";
	var subStr='DATA={"mobilePhone":"'+$("#mobilePhone").val()+'","password":"'+$.md5(password)+'","msgCode":"'+yzcode+'","topId":"'+<%=topId%>+'"}';
	
	$.ajax({
                type: "POST",
                url:ajaxCallUrl,
                data:subStr,// 你的formid
                dataType:"json",
                error: function(request) {
                	register=true;
                    alert("提交注册信息出错!");
                },
                success: function(data) {
                	register=true;
                    if(data.code=="100"){
                    	alert("注册成功！");
                    	window.location.href="http://a.app.qq.com/o/simple.jsp?pkgname=com.zubu";
                    }else {
                    	alert(data.msg)
                    } 
                }
            });
		}
}
function showMask(){
	$('body').css("overflow","hidden")
	$("#cover").show();
	}
</script>
</head>
<div  class="cover"></div>
<body>
<div name="cover" class="alert alert-warning fade in">  
  <a class="close" data-dismiss="alert">×</a>
  <strong>Error!</strong>This is a fatal error.
</div>
    <div class="container">

<form style="max-width:250px;text-align:center;margin: 0 auto;">
	<h1 style="font-family:Arial, Helvetica, sans-serif;color:#FFFFFF;font-size:23px; padding-bottom: 10px;">足布注册</h1>
  <div class="form-group">
  <label class="sr-only" >1</label>
  <div class="input-group"> 
  <div class="input-group-addon"><span class="glyphicon glyphicon-phone"></span></div>
    <input type="text"  maxlength="11"  class="form-control" id="mobilePhone" name="mobilePhone" placeholder="请输入手机号" >
 </div>
  </div>
  
   <div class="form-group">
   <label class="sr-only" >2</label>
   <div class="input-group">
    <div class="input-group-addon"> <span class="glyphicon glyphicon-lock"></span></div>
    <input type="password" class="form-control"  maxlength="30" id="password" name="password" style="background-color:#FFF;border-right:hidden;" placeholder="请输入密码">
	 <div class="input-group-addon" onclick="changePass();" style="background-color:#FFF;"><span class="glyphicon glyphicon-eye-open"  ></span></div>
	</div>
  </div>
  
 
   <div class="form-group" >
 <label class="sr-only" >3</label>
      <div class="input-group">
    <div class="input-group-addon"><span class="glyphicon glyphicon-list-alt"></span></div>
    <input type="text"  maxlength="4" class="form-control" id="msgCode" name="msgCode" placeholder="验证码">
	<div class="input-group-addon" onclick="getYZ(this);">获取验证码</div>
  </div>
  </div>

  
   <div class="form-group">
   <label class="sr-only" >4</label>
      <div class="input-group">
    <div class="input-group-addon"> <span class="glyphicon glyphicon-send"></span></div>
    <input type="text" disabled class="form-control" value="<%=topId%>" maxlength="11" id="topId" name="topId" placeholder="推荐好友ID(选填)">
	 </div>
 </div>
    <input class="btn btn-lg btn-primary btn-block" type="button" onclick="submit1();" value="注册"/>
	 <p>点击上面的"注册"按钮即表示你同意<a style="color:#FF5AFF;" href="<%=path %>/register/content.jsp">《足布用户协议》</a></p>
<!-- <a href="javascript:;" onclick="showMask()" >点我显示遮罩层</a><br /> -->
</form>
    </div> <!-- /container -->

  </body>
  
 
  	<script src="js/alert.js"></script>
</html>
