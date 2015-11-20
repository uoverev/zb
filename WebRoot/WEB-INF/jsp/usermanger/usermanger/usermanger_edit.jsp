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
		<!-- 下拉框 -->
		<link rel="stylesheet" href="static/css/chosen.css" />
		
		<link rel="stylesheet" href="static/css/ace.min.css" />
		<link rel="stylesheet" href="static/css/ace-responsive.min.css" />
		<link rel="stylesheet" href="static/css/ace-skins.min.css" />
		
		<link rel="stylesheet" href="static/css/datepicker.css" /><!-- 日期框 -->
		<script type="text/javascript" src="static/js/jquery-1.7.2.js"></script>
		<script type="text/javascript" src="static/js/jquery.tips.js"></script>
		
<script type="text/javascript">
	
	
	//保存
	function save(){
			if($("#USERID").val()==""){
			$("#USERID").tips({
				side:3,
	            msg:'请输入用户ID',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#USERID").focus();
			return false;
		}
		if($("#ADVEID").val()==""){
			$("#ADVEID").tips({
				side:3,
	            msg:'请输入广告商ID',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#ADVEID").focus();
			return false;
		}
		if($("#NICKNAME").val()==""){
			$("#NICKNAME").tips({
				side:3,
	            msg:'请输入昵称',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#NICKNAME").focus();
			return false;
		}
		if($("#ACCOUNT").val()==""){
			$("#ACCOUNT").tips({
				side:3,
	            msg:'请输入帐号',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#ACCOUNT").focus();
			return false;
		}
		if($("#PHONE").val()==""){
			$("#PHONE").tips({
				side:3,
	            msg:'请输入手机号',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#PHONE").focus();
			return false;
		}
		if($("#HEAD").val()==""){
			$("#HEAD").tips({
				side:3,
	            msg:'请输入头像',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#HEAD").focus();
			return false;
		}
		if($("#BALANCE").val()==""){
			$("#BALANCE").tips({
				side:3,
	            msg:'请输入余额',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#BALANCE").focus();
			return false;
		}
		if($("#ALIPAY_NAME").val()==""){
			$("#ALIPAY_NAME").tips({
				side:3,
	            msg:'请输入支付宝姓名',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#ALIPAY_NAME").focus();
			return false;
		}
		if($("#ALIPAY_ACCOUNT").val()==""){
			$("#ALIPAY_ACCOUNT").tips({
				side:3,
	            msg:'请输入支付宝帐号',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#ALIPAY_ACCOUNT").focus();
			return false;
		}
		if($("#STATE").val()==""){
			$("#STATE").tips({
				side:3,
	            msg:'请输入状态',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#STATE").focus();
			return false;
		}
		if($("#REGTIME").val()==""){
			$("#REGTIME").tips({
				side:3,
	            msg:'请输入注册时间',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#REGTIME").focus();
			return false;
		}
		if($("#INVITER").val()==""){
			$("#INVITER").tips({
				side:3,
	            msg:'请输入邀请人ID',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#INVITER").focus();
			return false;
		}
		$("#Form").submit();
		$("#zhongxin").hide();
		$("#zhongxin2").show();
	}
	
</script>
	</head>
<body>
	<form action="usermanger/${msg }.do" name="Form" id="Form" method="post">
		<input type="hidden" name="USERMANGER_ID" id="USERMANGER_ID" value="${pd.USERMANGER_ID}"/>
		<div id="zhongxin">
		<table>
			<tr>
				<td><input type="text" name="USERID" id="USERID" value="${pd.USERID}" maxlength="32" placeholder="这里输入用户ID" title="用户ID"/></td>
			</tr>
			<tr>
				<td><input type="text" name="ADVEID" id="ADVEID" value="${pd.ADVEID}" maxlength="32" placeholder="这里输入广告商ID" title="广告商ID"/></td>
			</tr>
			<tr>
				<td><input type="text" name="NICKNAME" id="NICKNAME" value="${pd.NICKNAME}" maxlength="32" placeholder="这里输入昵称" title="昵称"/></td>
			</tr>
			<tr>
				<td><input type="text" name="ACCOUNT" id="ACCOUNT" value="${pd.ACCOUNT}" maxlength="32" placeholder="这里输入帐号" title="帐号"/></td>
			</tr>
			<tr>
				<td><input type="number" name="PHONE" id="PHONE" value="${pd.PHONE}" maxlength="32" placeholder="这里输入手机号" title="手机号"/></td>
			</tr>
			<tr>
				<td><input type="text" name="HEAD" id="HEAD" value="${pd.HEAD}" maxlength="200" placeholder="这里输入头像" title="头像"/></td>
			</tr>
			<tr>
				<td><input type="text" name="BALANCE" id="BALANCE" value="${pd.BALANCE}" maxlength="32" placeholder="这里输入余额" title="余额"/></td>
			</tr>
			<tr>
				<td><input type="text" name="ALIPAY_NAME" id="ALIPAY_NAME" value="${pd.ALIPAY_NAME}" maxlength="32" placeholder="这里输入支付宝姓名" title="支付宝姓名"/></td>
			</tr>
			<tr>
				<td><input type="text" name="ALIPAY_ACCOUNT" id="ALIPAY_ACCOUNT" value="${pd.ALIPAY_ACCOUNT}" maxlength="32" placeholder="这里输入支付宝帐号" title="支付宝帐号"/></td>
			</tr>
			<tr>
				<td><input type="text" name="STATE" id="STATE" value="${pd.STATE}" maxlength="32" placeholder="这里输入状态" title="状态"/></td>
			</tr>
			<tr>
				<td><input class="span10 date-picker" name="REGTIME" id="REGTIME" value="${pd.REGTIME}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" placeholder="注册时间" title="注册时间"/></td>
			</tr>
			<tr>
				<td><input type="text" name="INVITER" id="INVITER" value="${pd.INVITER}" maxlength="32" placeholder="这里输入邀请人ID" title="邀请人ID"/></td>
			</tr>
			<tr>
				<td style="text-align: center;">
					<a class="btn btn-mini btn-primary" onclick="save();">保存</a>
					<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
				</td>
			</tr>
		</table>
		</div>
		
		<div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><br/><img src="static/images/jiazai.gif" /><br/><h4 class="lighter block green">提交中...</h4></div>
		
	</form>
	
	
		<!-- 引入 -->
		<script type="text/javascript">window.jQuery || document.write("<script src='static/js/jquery-1.9.1.min.js'>\x3C/script>");</script>
		<script src="static/js/bootstrap.min.js"></script>
		<script src="static/js/ace-elements.min.js"></script>
		<script src="static/js/ace.min.js"></script>
		<script type="text/javascript" src="static/js/chosen.jquery.min.js"></script><!-- 下拉框 -->
		<script type="text/javascript" src="static/js/bootstrap-datepicker.min.js"></script><!-- 日期框 -->
		<script type="text/javascript">
		$(top.hangge());
		$(function() {
			
			//单选框
			$(".chzn-select").chosen(); 
			$(".chzn-select-deselect").chosen({allow_single_deselect:true}); 
			
			//日期框
			$('.date-picker').datepicker();
			
		});
		
		</script>
</body>
</html>