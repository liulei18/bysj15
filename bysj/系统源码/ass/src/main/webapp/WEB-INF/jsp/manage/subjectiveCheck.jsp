<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<base href="${pageContext.request.contextPath }/">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>软件工程双语课程</title>
<link href="css/bootstrap.min.css" rel="stylesheet">

<!--[if lt IE 9]>
    <script src="js/html5shiv.js"></script>
    <script src="js/respond.min.js"></script>
    <![endif]-->
</head>

<body>
	<div class="container">
		<div class="panel panel-default">
			<div class="panel-heading" style="margin-bottom:5px">主观题列表</div>
			<!-- <div class="panel-body">
				<p></p>
			</div> -->
			
			<table class="table table-hover table-condensed">
				<thead>
					<tr>
						<th>序号</th>
						<th>试题名称</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>

					<tr class='success'>
						<td>1</td>
						<td><a href="#">${tSubjective.question}</a></td>
						<td><a href="javascript:enterSubjective(${tSubjective.id })">通过</a>||<a
							href="javascript:deleteTSubjective(${tSubjective.id })">未通过</a></td>
					</tr>
					<tr>
						<td>以下是相似的试题：</td>
					</tr>
					<c:forEach var="item" items="${pageInfo.data}" varStatus="status">
						<c:choose>
							<c:when test="${status.index % 2 == 0}">
								<tr class='success'>
							</c:when>
							<c:otherwise>
								<tr>
							</c:otherwise>
						</c:choose>
						<td>${status.index+2}</td>
						<td>${item.question }</td>
						<td>——</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<div class="row">
				<div class="col-md-6">共 ${pageInfo.totalRecords } 条
					${pageInfo.currentPage } / ${pageInfo.totalPages }</div>

				<div class="col-md-6 textRight">
					<c:choose>
						<c:when test="${pageInfo.currentPage == 1}">
							<a class="btn btn-info disabled " role="button">首页</a>
							<a class="btn btn-info disabled " role="button">上一页</a>
						</c:when>
						<c:otherwise>
							<a class="btn btn-info " href="javascript:pageJump(1)"
								role="button">首页</a>
							<a class="btn btn-info"
								href='javascript:pageJump(${pageInfo.currentPage-1 })'
								role="button">上一页</a>
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${pageInfo.currentPage >= pageInfo.totalPages}">
							<a class="btn btn-info disabled" role="button">下一页</a>
							<a class="btn btn-info disabled" role="button">尾页</a>
						</c:when>
						<c:otherwise>
							<a class="btn btn-info"
								href='javascript:pageJump(${pageInfo.currentPage+1 })'
								role="button">下一页</a>
							<a class="btn btn-info"
								href='javascript:pageJump(${pageInfo.totalPages })'
								role="button">尾页</a>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
	</div>
	</div>

	<div class="modal fade" id="myModal">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title">提示</h4>
				</div>
				<div class="modal-body">
					<p>你确定要删除本条记录吗？</p>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" id="sureBtn" class="btn btn-primary">确定</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->

	<script src="js/jquery-1.10.2.min.js"></script>
	<script src="js/bootstrap.min.js"></script>

	<script type="text/javascript">
		function pageJump(page) {
			$('#currentPage').val(page);
			$('#searchForm').submit();
		}
		function deleteUser(id) {
			$('#myModal').modal({
				show : true,
			})
			$('#sureBtn').data('value', id);
		}
		$(function() {
			$('#sureBtn').click(function() {
				location.href = "" + $(this).data('value');
			});
		});
		function deleteTSubjective(id) {
			location.href = "manage/tSubjective/subjectiveCheck/" + id;
		}
		function enterSubjective(id) {
			location.href = "manage/tSubjective/updateTSubjective/" + id;
		}
	</script>
</body>
</html>
