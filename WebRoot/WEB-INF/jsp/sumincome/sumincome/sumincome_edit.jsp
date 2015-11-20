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
		if($("#ITEM").val()==""){
			$("#ITEM").tips({
				side:3,
	            msg:'请输入累计项目',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#ITEM").focus();
			return false;
		}
		if($("#ITEMVAL").val()==""){
			$("#ITEMVAL").tips({
				side:3,
	            msg:'请输入累计值',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#ITEMVAL").focus();
			return false;
		}
		if($("#TOTALREVENUE").val()==""){
			$("#TOTALREVENUE").tips({
				side:3,
	            msg:'请输入总收益',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#TOTALREVENUE").focus();
			return false;
		}
		$("#Form").submit();
		$("#zhongxin").hide();
		$("#zhongxin2").show();
	}
	
</script>
	</head>
<body>
	<form action="sumincome/${msg }.do" name="Form" id="Form" method="post">
		<input type="hidden" name="SUMINCOME_ID" id="SUMINCOME_ID" value="${pd.SUMINCOME_ID}"/>
		<div id="zhongxin">
		<table>
			<tr>
				<td><input type="text" name="USERID" id="USERID" value="${pd.USERID}" maxlength="32" placeholder="这里输入用户ID" title="用户ID"/></td>
			</tr>
			<tr>
				<td><input type="text" name="ITEM" id="ITEM" value="${pd.ITEM}" maxlength="32" placeholder="这里输入累计项目" title="累计项目"/></td>
			</tr>
			<tr>
				<td><input type="text" name="ITEMVAL" id="ITEMVAL" value="${pd.ITEMVAL}" maxlength="32" placeholder="这里输入累计值" title="累计值"/></td>
			</tr>
			<tr>
				<td><input type="text" name="TOTALREVENUE" id="TOTALREVENUE" value="${pd.TOTALREVENUE}" maxlength="32" placeholder="这里输入总收益" title="总收益"/></td>
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