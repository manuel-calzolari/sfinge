<%@ page language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Sfinge - Manage forms</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.css">
<style type="text/css">
body {
	padding-top: 60px;
}
</style>
<script src="http://code.jquery.com/jquery-1.7.min.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap-modal.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.tablesorter.js"></script>
<script>
	$(function() {
		$("table#forms").tablesorter({
			sortList : [ [ 1, 0 ] ],
			headers : {
				4 : {
					sorter : false
				}
			}
		});
	});
</script>
</head>
<body>
  <div class="container-fluid">
    <div class="sidebar">
      <div class="well">
        <h5>Operations</h5>
        <ul>
          <li><a href="${pageContext.request.contextPath}/Forms?r=admin">Manage forms</a></li>
          <li><a href="${pageContext.request.contextPath}/Forms?r=create">Create form</a></li>
        </ul>
      </div>
    </div>
    <div class="content">
      <c:if test="${not empty info_login}">
        <div class="alert-message success">
          <p>${info_login}</p>
        </div>
      </c:if>
      <h1>Manage forms</h1>
      <c:choose>
        <c:when test="${empty forms}">
          <p>No forms.</p>
        </c:when>
        <c:otherwise>
          <c:forEach items="${forms}" var="form">
            <!-- Begin Modal -->
            <div id="${form.id}-modal" class="modal hide fade">
              <div class="modal-header">
                <a href="#" class="close">&times;</a>
                <h3>Delete</h3>
              </div>
              <div class="modal-body">
                <p>Are you sure?</p>
              </div>
              <div class="modal-footer">
                <a href="${pageContext.request.contextPath}/Forms?r=delete&amp;id=${form.id}" class="btn primary">OK</a>
              </div>
            </div>
            <!-- End Modal -->
          </c:forEach>
          <table class="zebra-striped" id="forms">
            <thead>
              <tr>
                <th>Title</th>
                <th>Created at</th>
                <th>Created by</th>
                <th>Passphrase</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach items="${forms}" var="form" varStatus="status">
                <tr>
                  <td>${form.title}</td>
                  <td>${form.cdatetime}</td>
                  <td>${users[form.creator_id].name} (${users[form.creator_id].username})</td>
                  <td>${form.passphrase}</td>
                  <td><a href="${pageContext.request.contextPath}/Forms?r=view&amp;id=${form.id}">Details</a> &middot; <a
                    href="${pageContext.request.contextPath}/Forms?r=update&amp;id=${form.id}">Update</a> &middot; <a href="#" data-controls-modal="${form.id}-modal" data-backdrop="static">Delete</a> | <a
                    href="${pageContext.request.contextPath}/Questions?r=admin&amp;fid=${form.id}">Questions</a> &middot; <a href="${pageContext.request.contextPath}/Answers?fid=${form.id}">Answers</a> &middot; <a
                    href="${pageContext.request.contextPath}/Questions?fid=${form.id}">Test</a></td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </c:otherwise>
      </c:choose>
    </div>
  </div>
</body>
</html>