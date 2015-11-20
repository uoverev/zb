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
		if($("#AD_DESCRITE").val()==""){
			$("#AD_DESCRITE").tips({
				side:3,
	            msg:'请输入广告描述',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#AD_DESCRITE").focus();
			return false;
		}
		if($("#STARTTIME").val()==""){
			$("#STARTTIME").tips({
				side:3,
	            msg:'请输入开始投放时间',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#STARTTIME").focus();
			return false;
		}
		if($("#ENDTIME").val()==""){
			$("#ENDTIME").tips({
				side:3,
	            msg:'请输入结束投放时间',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#ENDTIME").focus();
			return false;
		}
		if($("#MONEY").val()==""){
			$("#MONEY").tips({
				side:3,
	            msg:'请输入投放金额',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#MONEY").focus();
			return false;
		}
		if($("#ADPICTURE").val()==""){
			$("#ADPICTURE").tips({
				side:3,
	            msg:'请输入广告图片',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#ADPICTURE").focus();
			return false;
		}
		if($("#LEFT_ACTION").val()==""){
			$("#LEFT_ACTION").tips({
				side:3,
	            msg:'请输入左滑动作',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#LEFT_ACTION").focus();
			return false;
		}
		if($("#LEFT_VALUE").val()==""){
			$("#LEFT_VALUE").tips({
				side:3,
	            msg:'请输入左滑值',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#LEFT_VALUE").focus();
			return false;
		}
		if($("#STATIC").val()==""){
			$("#STATIC").tips({
				side:3,
	            msg:'请输入状态',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#STATIC").focus();
			return false;
		}
		if($("#CREATETIME").val()==""){
			$("#CREATETIME").tips({
				side:3,
	            msg:'请输入创建时间',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#CREATETIME").focus();
			return false;
		}
		if($("#AUDITPERSON").val()==""){
			$("#AUDITPERSON").tips({
				side:3,
	            msg:'请输入审核用户',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#AUDITPERSON").focus();
			return false;
		}
		if($("#AUDITDESCRITE").val()==""){
			$("#AUDITDESCRITE").tips({
				side:3,
	            msg:'请输入审核描述',
	            bg:'#AE81FF',
	            time:2
	        });
			$("#AUDITDESCRITE").focus();
			return false;
		}
		$("#Form").submit();
		$("#zhongxin").hide();
		$("#zhongxin2").show();
	}
	
</script>
	</head>
<body>
	<form action="ad/${msg }.do" name="Form" id="Form" method="post">
		<input type="hidden" name="AD_ID" id="AD_ID" value="${pd.AD_ID}"/>
		<div id="zhongxin">
		<table>
			<tr>
				<td><input type="text" name="ADVEID" id="ADVEID" value="${pd.ADVEID}" maxlength="32" placeholder="这里输入广告商ID" title="广告商ID"/></td>
			</tr>
			<tr>
				<td><input type="text" name="AD_DESCRITE" id="AD_DESCRITE" value="${pd.AD_DESCRITE}" maxlength="200" placeholder="这里输入广告描述" title="广告描述"/></td>
			</tr>
			<tr>
				<td><input class="span10 date-picker" name="STARTTIME" id="STARTTIME" value="${pd.STARTTIME}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" placeholder="开始投放时间" title="开始投放时间"/></td>
			</tr>
			<tr>
				<td><input class="span10 date-picker" name="ENDTIME" id="ENDTIME" value="${pd.ENDTIME}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" placeholder="结束投放时间" title="结束投放时间"/></td>
			</tr>
			<tr>
				<td><input type="text" name="MONEY" id="MONEY" value="${pd.MONEY}" maxlength="32" placeholder="这里输入投放金额" title="投放金额"/></td>
			</tr>
			<tr>
				<td><input type="text" name="ADPICTURE" id="ADPICTURE" value="${pd.ADPICTURE}" maxlength="200" placeholder="这里输入广告图片" title="广告图片"/></td>
			</tr>
			<tr>
				<td><input type="text" name="LEFT_ACTION" id="LEFT_ACTION" value="${pd.LEFT_ACTION}" maxlength="32" placeholder="这里输入左滑动作" title="左滑动作"/></td>
			</tr>
			<tr>
				<td><input type="text" name="LEFT_VALUE" id="LEFT_VALUE" value="${pd.LEFT_VALUE}" maxlength="32" placeholder="这里输入左滑值" title="左滑值"/></td>
			</tr>
			<tr>
				<td><input type="text" name="STATIC" id="STATIC" value="${pd.STATIC}" maxlength="32" placeholder="这里输入状态" title="状态"/></td>
			</tr>
			<tr>
				<td><input class="span10 date-picker" name="CREATETIME" id="CREATETIME" value="${pd.CREATETIME}" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" placeholder="创建时间" title="创建时间"/></td>
			</tr>
			<tr>
				<td><input type="text" name="AUDITPERSON" id="AUDITPERSON" value="${pd.AUDITPERSON}" maxlength="32" placeholder="这里输入审核用户" title="审核用户"/></td>
			</tr>
			<tr>
				<td><input type="text" name="AUDITDESCRITE" id="AUDITDESCRITE" value="${pd.AUDITDESCRITE}" maxlength="32" placeholder="这里输入审核描述" title="审核描述"/></td>
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