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
	            msg:'请输入用户Id',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#USERID").focus();
			return false;
		}
		if($("#PROD_ID").val()==""){
			$("#PROD_ID").tips({
				side:3,
	            msg:'请输入商品Id',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#PROD_ID").focus();
			return false;
		}
		if($("#BUY_TIME").val()==""){
			$("#BUY_TIME").tips({
				side:3,
	            msg:'请输入购买时间',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#BUY_TIME").focus();
			return false;
		}
		if($("#NUMBERS").val()==""){
			$("#NUMBERS").tips({
				side:3,
	            msg:'请输入数量',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#NUMBERS").focus();
			return false;
		}
		if($("#UNIT_PRICE").val()==""){
			$("#UNIT_PRICE").tips({
				side:3,
	            msg:'请输入单价',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#UNIT_PRICE").focus();
			return false;
		}
		if($("#TOTAL_PRICE").val()==""){
			$("#TOTAL_PRICE").tips({
				side:3,
	            msg:'请输入总价',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#TOTAL_PRICE").focus();
			return false;
		}
		if($("#DESCRITE").val()==""){
			$("#DESCRITE").tips({
				side:3,
	            msg:'请输入描述',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#DESCRITE").focus();
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
		$("#Form").submit();
		$("#zhongxin").hide();
		$("#zhongxin2").show();
	}
	
</script>
	</head>
<body>
	<form action="exchangerecord/${msg }.do" name="Form" id="Form" method="post">
		<input type="hidden" name="EXCHANGERECORD_ID" id="EXCHANGERECORD_ID" value="${pd.EXCHANGERECORD_ID}"/>
		<div id="zhongxin">
		<table>
			<tr>
				<td><input type="text" name="USERID" id="USERID" value="${pd.USERID}" maxlength="32" placeholder="这里输入用户Id" title="用户Id"/></td>
			</tr>
			<tr>
				<td><input type="text" name="PROD_ID" id="PROD_ID" value="${pd.PROD_ID}" maxlength="32" placeholder="这里输入商品Id" title="商品Id"/></td>
			</tr>
			<tr>
				<td><input class="span10 date-picker" name="BUY_TIME" id="BUY_TIME" value="${pd.BUY_TIME}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" placeholder="购买时间" title="购买时间"/></td>
			</tr>
			<tr>
				<td><input type="number" name="NUMBERS" id="NUMBERS" value="${pd.NUMBERS}" maxlength="32" placeholder="这里输入数量" title="数量"/></td>
			</tr>
			<tr>
				<td><input type="text" name="UNIT_PRICE" id="UNIT_PRICE" value="${pd.UNIT_PRICE}" maxlength="32" placeholder="这里输入单价" title="单价"/></td>
			</tr>
			<tr>
				<td><input type="text" name="TOTAL_PRICE" id="TOTAL_PRICE" value="${pd.TOTAL_PRICE}" maxlength="32" placeholder="这里输入总价" title="总价"/></td>
			</tr>
			<tr>
				<td><input type="text" name="DESCRITE" id="DESCRITE" value="${pd.DESCRITE}" maxlength="32" placeholder="这里输入描述" title="描述"/></td>
			</tr>
			<tr>
				<td><input type="text" name="STATE" id="STATE" value="${pd.STATE}" maxlength="32" placeholder="这里输入状态" title="状态"/></td>
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