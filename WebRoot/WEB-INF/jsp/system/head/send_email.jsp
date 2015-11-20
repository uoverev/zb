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
		
		<style type="text/css">
		#dialog-add,#dialog-message,#dialog-comment{width:100%; height:100%; position:fixed; top:0px; z-index:99999999; display:none;}
		.commitopacity{position:absolute; width:100%; height:700px; background:#7f7f7f; filter:alpha(opacity=50); -moz-opacity:0.5; -khtml-opacity: 0.5; opacity: 0.5; top:0px; z-index:99999;}
		.commitbox{width:100%; margin:0px auto; position:absolute; top:0px; z-index:99999;}
		.commitbox_inner{width:96%; height:255px;  margin:6px auto; background:#efefef; border-radius:5px;}
		.commitbox_top{width:100%; height:250px; margin-bottom:10px; padding-top:10px; background:#FFF; border-radius:5px; box-shadow:1px 1px 3px #e8e8e8;}
		.commitbox_top textarea{width:95%; height:195px; display:block; margin:0px auto; border:0px;}
		.commitbox_cen{width:95%; height:40px; padding-top:10px;}
		.commitbox_cen div.left{float:left;background-size:15px; background-position:0px 3px; padding-left:18px; color:#f77500; font-size:16px; line-height:27px;}
		.commitbox_cen div.left img{width:30px;}
		.commitbox_cen div.right{float:right; margin-top:7px;}
		.commitbox_cen div.right span{cursor:pointer;}
		.commitbox_cen div.right span.save{border:solid 1px #c7c7c7; background:#6FB3E0; border-radius:3px; color:#FFF; padding:5px 10px;}
		.commitbox_cen div.right span.quxiao{border:solid 1px #f77400; background:#f77400; border-radius:3px; color:#FFF; padding:4px 9px;}
		</style>
		
		<script type="text/javascript">
			
			//发送
			function sendEm(){
				
				if($("#TYPE").val()=="1"){
					$("#CONTENT").val(getContentTxt());
				}else{
					$("#CONTENT").val(getContent());
				}
				if($("#EMAIL").val()==""){
					$("#EMAIL").tips({
						side:3,
			            msg:'请输入邮箱',
			            bg:'#AE81FF',
			            time:2
			        });
					$("#EMAIL").focus();
					return false;
				}
				if($("#TITLE").val()==""){
					$("#TITLE").tips({
						side:3,
			            msg:'请输入标题',
			            bg:'#AE81FF',
			            time:2
			        });
					$("#TITLE").focus();
					return false;
				}
				if($("#CONTENT").val()==""){
					
					$("#nr").tips({
						side:1,
			            msg:'请输入内容',
			            bg:'#AE81FF',
			            time:3
			        });
					return false;
				}
				
				$("#zhongxin").hide();
				$("#zhongxin2").show();
				
				var EMAIL = $("#EMAIL").val();
				var TYPE  = $("#TYPE").val();
				var TITLE = $("#TITLE").val();
				var CONTENT = $("#CONTENT").val();
				var isAll = $("#isAll").val();
				
				var fmsg = "${pd.msg}";
				
				$.ajax({
					type: "POST",
					url: '<%=basePath%>/head/sendEmail.do?tm='+new Date().getTime(),
			    	data: {EMAIL:EMAIL,TYPE:TYPE,TITLE:TITLE,CONTENT:CONTENT,isAll:isAll,fmsg:fmsg},
					dataType:'json',
					//beforeSend: validateData,
					cache: false,
					success: function(data){
						 $.each(data.list, function(i, list){
							 if(list.msg == 'ok'){
								 var count = list.count;
								 var ecount = list.ecount;
								 $("#msg").tips({
									side:3,
						            msg:'成功发出'+count+'条,失败'+ecount+'条',
						            bg:'#68B500',
						            time:4
							      });
								 setTimeout("close()",6000);
							 }else{
								 $("#msg").tips({
										side:3,
							            msg:'邮件发送失败,请联系管理员检查邮件服务器配置是否正确!',
							            bg:'#FF0000',
							            time:6
								 });
								 setTimeout("close()",6000);
							 }
						 });
					}
				});
				
			}
			function setType(value){
				$("#TYPE").val(value);
			}
			function close(){
				top.Dialog.close();
			}
			function isAll(){
				if($("#allusers").attr("checked") == 'checked'){
					$("#isAll").val('yes');
					$("#EMAIL").attr("disabled",true);
				}else{
					$("#isAll").val('no');
					$("#EMAIL").attr("disabled",false);
				}
			}
			
			//编辑邮箱(此方式弃用)
			function editEmail(){
			   var EMAIL = $("#EMAIL").val();
			   var result = showModalDialog("<%=basePath%>/head/editEmail.do?EMAIL="+EMAIL,"","dialogWidth=600px;dialogHeight=380px;");
			   if(result==null || ""==result){
				    $("#EMAIL").val("");
			   }else{
					$("#EMAIL").val(result);
			   }
			}
			
			//打开编辑邮箱
			function dialog_open(){
				$("#EMAILs").val($("#EMAIL").val());
				$("#dialog-add").css("display","block");
			}
			//关闭编辑邮箱
			function cancel_pl(){
				$("#dialog-add").css("display","none");
			}
			//保存编辑邮箱
			function saveEmail(){
				$("#EMAIL").val($("#EMAILs").val());
				$("#dialog-add").css("display","none");
			}
		</script>
	</head>
<body>
		<!-- 编辑邮箱  -->
		<div id="dialog-add">
			<div class="commitopacity"></div>
		  	<div class="commitbox">
			  	<div class="commitbox_inner">
			        <div class="commitbox_top">
			        	<textarea name="EMAILs" id="EMAILs" placeholder="请选输入对方邮箱,多个请用(;)分号隔开" title="请选输入对方邮箱,多个请用(;)分号隔开"></textarea>
			            <div class="commitbox_cen">
			                <div class="left" id="cityname"></div>
			                <div class="right"><span class="save" onClick="saveEmail()">保存</span>&nbsp;&nbsp;<span class="quxiao" onClick="cancel_pl()">取消</span></div>
			            </div>
			        </div>
			  	</div>
		  	</div>
		</div>


		<div id="zhongxin">
		<textarea name="CONTENT" id="CONTENT" style="display:none" ></textarea>
		<input type="hidden" name="TYPE" id="TYPE" value="1"/>
		<input type="hidden" name="isAll" id="isAll" value="no"/>
		<table style="width:95%;" >
			<tr>
				<td style="margin-top:0px;">
					 <div style="float: left;" style="width:81%"><textarea name="EMAIL" id="EMAIL" rows="1" cols="50" style="width:540px;height:20px;" placeholder="请选输入对方邮箱,多个请用(;)分号隔开" title="请选输入对方邮箱,多个请用(;)分号隔开">${pd.EMAIL}</textarea></div>
					 <div style="float: right;" style="width:19%"><a class='btn btn-mini btn-info' title="编辑邮箱" onclick="dialog_open();"><i class='icon-edit'></i></a></div>
				</td>
			</tr>
			<tr>
				<td>
					 <input type="text" name="TITLE" id="TITLE" value="" placeholder="请选输入邮件标题" style="width:98%"/>
				</td>
			</tr>
			<tr>
				<td id="nr">
					 <script id="editor" type="text/plain" style="width:590px;height:259px;"></script>
				</td>
			</tr>
			<tr>
				<td style="text-align: center;">
					<a class="btn btn-mini btn-primary" onclick="sendEm();">发送</a>
					<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
					<label style="float:left;padding-left: 32px;"><input name="form-field-radio" id="form-field-radio1" onclick="setType('1');" checked="checked" type="radio" value="icon-edit"><span class="lbl">纯文本</span></label>
					<label style="float:left;padding-left: 5px;"><input name="form-field-radio" id="form-field-radio2" onclick="setType('2');" type="radio" value="icon-edit"><span class="lbl">带标签</span></label>
					<label style="float:left;padding-left: 15px;"><input name="form-field-checkbox" class="ace-checkbox-2" type="checkbox" id="allusers" onclick="isAll();" /><span class="lbl">全体用户</span></label>
				</td>
			</tr>
		</table>
		</div>
		
		<div id="zhongxin2" class="center" style="display:none"><br/><img src="static/images/jzx.gif" id='msg' /><br/><h4 class="lighter block green" id='msg'>正在发送...</h4></div>
		
	
	
		<!-- 引入 -->
		<script type="text/javascript">window.jQuery || document.write("<script src='static/js/jquery-1.9.1.min.js'>\x3C/script>");</script>
		<script src="static/js/bootstrap.min.js"></script>
		<script src="static/js/ace-elements.min.js"></script>
		<script src="static/js/ace.min.js"></script>
		
		<!-- 编辑框-->
		<script type="text/javascript" charset="utf-8">window.UEDITOR_HOME_URL = "<%=path%>/plugins/ueditor/";</script>
		<script type="text/javascript" charset="utf-8" src="plugins/ueditor/ueditor.config.js"></script>
		<script type="text/javascript" charset="utf-8" src="plugins/ueditor/ueditor.all.js"></script>
		<!-- 编辑框-->
		
		<script type="text/javascript">
		$(top.hangge());
		//ueditor纯文本
		function getContentTxt() {
		    var arr = [];
		    arr.push(UE.getEditor('editor').getContentTxt());
		    return arr.join("");
		}
		//ueditor有标签文本
		function getContent() {
		    var arr = [];
		    arr.push(UE.getEditor('editor').getContent());
		    return arr.join("");
		}
		
		setTimeout("ueditor()",500);
		function ueditor(){
			var ue = UE.getEditor('editor');
		}
		
		</script>
	
</body>
</html>