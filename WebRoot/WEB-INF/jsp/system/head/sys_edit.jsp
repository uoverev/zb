<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
	<head>
		<base href="<%=basePath%>">
		<meta charset="utf-8" />
		<title></title>
		<meta name="description" content="overview & stats" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />
		<link href="static/css/bootstrap.min.css" rel="stylesheet" />
		<link href="static/css/bootstrap-responsive.min.css" rel="stylesheet" />
		<link rel="stylesheet" href="static/css/font-awesome.min.css" />
		<link rel="stylesheet" href="static/css/ace.min.css" />
		<link rel="stylesheet" href="static/css/ace-responsive.min.css" />
		<link rel="stylesheet" href="static/css/ace-skins.min.css" />
		<script type="text/javascript" src="static/js/jquery-1.7.2.js"></script>
		<!--提示框-->
		<script type="text/javascript" src="static/js/jquery.tips.js"></script>
		
<script type="text/javascript">
	
	$(top.hangge());
	//保存
	function save(){
		
		if($("#YSYNAME").val()==""){
			$("#YSYNAME").tips({
				side:3,
	            msg:'输入系统名称',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#YSYNAME").focus();
			return false;
		}

		if($("#COUNTPAGE").val()==""){
			$("#COUNTPAGE").tips({
				side:3,
	            msg:'输入每页条数',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#COUNTPAGE").focus();
			return false;
		}
		
		if($("#SMTP").val()==""){
			$("#SMTP").tips({
				side:1,
	            msg:'输入SMTP',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#SMTP").focus();
			return false;
		}
		
		if($("#PORT").val()==""){
			$("#PORT").tips({
				side:1,
	            msg:'输入端口',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#PORT").focus();
			return false;
		}
		
		if($("#EMAIL").val()==""){
			
			$("#EMAIL").tips({
				side:3,
	            msg:'输入邮箱',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#EMAIL").focus();
			return false;
		}else if(!ismail($("#EMAIL").val())){
			$("#EMAIL").tips({
				side:3,
	            msg:'邮箱格式不正确',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#EMAIL").focus();
			return false;
		}
	
		if($("#PAW").val()==""){
			$("#PAW").tips({
				side:1,
	            msg:'输入密码',
	            bg:'#AE81FF',
	            time:3
	        });
			$("#PAW").focus();
			return false;
		}
		
		$("#Form").submit();
		$("#zhongxin").hide();
		$("#zhongxin2").show();
	}
	
	function ismail(mail){
		return(new RegExp(/^(?:[a-zA-Z0-9]+[_\-\+\.]?)*[a-zA-Z0-9]+@(?:([a-zA-Z0-9]+[_\-]?)*[a-zA-Z0-9]+\.)+([a-zA-Z]{2,})+$/).test(mail));
		}
</script>
	</head>
<body>


<form action="head/saveSys.do" name="Form" id="Form" method="post">
<div id="zhongxin">
 <div class="span6">
	<div class="tabbable">
            <ul class="nav nav-tabs" id="myTab">
              <li class="active"><a data-toggle="tab" href="#home"><i class="green icon-home bigger-110"></i> 配置 NO1</a></li>
              <li><a data-toggle="tab" href="#profile"><i class="green icon-cog bigger-110"></i>配置 NO2</a></li>
            </ul>
            <div class="tab-content">
			  <div id="home" class="tab-pane in active">
				
				<table id="table_report" class="table table-striped table-bordered table-hover">
					<tr>
						<td style="width:15%">系统名称:</td>
						<td><input type="text" name="YSYNAME" id="YSYNAME" value="${pd.YSYNAME }" placeholder="这里输入系统名称" style="width:90%" title="系统名称"/></td>
					
						<td style="width:15%">每页条数:</td>
						<td><input type="number" name="COUNTPAGE" id="COUNTPAGE" value="${pd.COUNTPAGE }" placeholder="这里输入每页条数" style="width:90%" title="每页条数"/></td>
					</tr>
				</table>
				
				<table id="table_report" class="table table-striped table-bordered table-hover">
					<tr>
						<td style="text-align: center;" colspan="100">
							邮件服务器配置
						</td>
					</tr>
					<tr>
						<td style="width:15%">SMTP:</td>
						<td><input type="text" name="SMTP" id="SMTP" value="${pd.SMTP }" placeholder="例如:smtp.qq.com" style="width:90%" title="SMTP"/></td>
					
						<td style="width:15%">端口:</td>
						<td><input type="number" name="PORT" id="PORT" value="${pd.PORT }" placeholder="一般为：25" style="width:90%" title="端口"/></td>
					</tr>
					<tr>
						<td style="width:15%">邮箱:</td>
						<td><input type="email" name="EMAIL" id="EMAIL" value="${pd.EMAIL }" placeholder="请输入邮件服务器邮箱" style="width:90%" title="邮箱"/></td>
					
						<td style="width:15%">密码:</td>
						<td><input type="password" name="PAW" id="PAW" value="${pd.PAW }" placeholder="请输入邮箱密码" style="width:90%" title="密码"/></td>
					</tr>
				</table>
				
				<table id="table_report" class="table table-striped table-bordered table-hover">
					<tr>
						<td style="text-align: center;" colspan="100">
							短信接口&nbsp;(短信商一&nbsp;<a href="http://www.dxton.com/" target="_blank">官网</a>)
						</td>
					</tr>
					<tr>
						<td style="width:15%">账号:</td>
						<td><input type="email" name="SMSU1" id="SMSU1" value="${pd.SMSU1 }" placeholder="请输入账号" style="width:90%" title="邮箱"/></td>
					
						<td style="width:15%">密码:</td>
						<td><input type="password" name="SMSPAW1" id="SMSPAW1" value="${pd.SMSPAW1 }" placeholder="请输入密码" style="width:90%" title="密码"/></td>
					</tr>
					<tr>
						<td style="text-align: center;" colspan="100">
							短信接口&nbsp;(短信商二&nbsp;<a href="http://www.ihuyi.com/" target="_blank">官网</a>)
						</td>
					</tr>
					<tr>
						<td style="width:15%">账号:</td>
						<td><input type="email" name="SMSU2" id="SMSU2" value="${pd.SMSU2 }" placeholder="请输入账号" style="width:90%" title="邮箱"/></td>
					
						<td style="width:15%">密码:</td>
						<td><input type="password" name="SMSPAW2" id="SMSPAW2" value="${pd.SMSPAW2 }" placeholder="请输入密码" style="width:90%" title="密码"/></td>
					</tr>
				</table>
				
				<table id="table_report" class="table table-striped table-bordered table-hover">
					<tr>
						<td style="text-align: center;" colspan="100">
							图片前路径
						</td>
					</tr>
					<tr>
						<td style="width:15%">路径:</td>
						<td><input type="text" name="IMAGESURL" id="SMTP" value="${pd.IMAGESURL }" placeholder="例如:smtp.qq.com" style="width:90%" title="SMTP"/></td>
					
					</tr>
				</table>
		

			  </div>
			  <div id="profile" class="tab-pane">
				<p>待开发……</p>
			  </div>
            </div>
	</div>
 </div><!--/span-->


		<table id="table_report" class="table table-striped table-bordered table-hover">
			<tr>
				<td style="text-align: center;" colspan="100">
					<a class="btn btn-mini btn-primary" onclick="save();">保存</a>
					<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
				</td>
			</tr>
		</table>

</div>
		
<div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><img src="static/images/jiazai.gif" /><br/><h4 class="lighter block green"></h4></div>
		
</form>
	
	
		<!-- 引入 -->
		<script type="text/javascript">window.jQuery || document.write("<script src='static/js/jquery-1.9.1.min.js'>\x3C/script>");</script>
		<script src="static/js/bootstrap.min.js"></script>
		<script src="static/js/ace-elements.min.js"></script>
		<script src="static/js/ace.min.js"></script>
		
</body>
</html>