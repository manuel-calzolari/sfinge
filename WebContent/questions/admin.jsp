<%@ page language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Sfinge - Manage questions</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.css">
<style type="text/css">
body {
	padding-top: 60px;
}
</style>
<script src="http://code.jquery.com/jquery-1.7.min.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap-modal.js"></script>
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
        <ul>
          <li><a href="${pageContext.request.contextPath}/Questions?r=admin&amp;fid=${param.fid}">Manage questions</a></li>
          <li>Create question
            <ul>
              <li><a href="${pageContext.request.contextPath}/Questions?r=create&amp;fid=${param.fid}&amp;q=checkbox">Create checkbox</a></li>
              <li><a href="${pageContext.request.contextPath}/Questions?r=create&amp;fid=${param.fid}&amp;q=password">Create password</a></li>
              <li><a href="${pageContext.request.contextPath}/Questions?r=create&amp;fid=${param.fid}&amp;q=radio">Create radio</a></li>
              <li><a href="${pageContext.request.contextPath}/Questions?r=create&amp;fid=${param.fid}&amp;q=select">Create select</a></li>
              <li><a href="${pageContext.request.contextPath}/Questions?r=create&amp;fid=${param.fid}&amp;q=text">Create text</a></li>
              <li><a href="${pageContext.request.contextPath}/Questions?r=create&amp;fid=${param.fid}&amp;q=textarea">Create textarea</a></li>
            </ul>
          </li>
        </ul>
      </div>
    </div>
    <div class="content">
      <h1>Manage questions</h1>
      <c:choose>
        <c:when test="${empty questions}">
          <p>No questions.</p>
        </c:when>
        <c:otherwise>
          <c:forEach items="${questions}" var="question">
            <!-- Begin Modal -->
            <div id="${question.id}-modal" class="modal hide fade">
              <div class="modal-header">
                <a href="#" class="close">&times;</a>
                <h3>Delete</h3>
              </div>
              <div class="modal-body">
                <p>Are you sure?</p>
              </div>
              <div class="modal-footer">
                <a href="${pageContext.request.contextPath}/Questions?r=delete&amp;fid=${param.fid}&amp;id=${question.id}" class="btn primary">OK</a>
              </div>
            </div>
            <!-- End Modal -->
          </c:forEach>
          <table class="zebra-striped">
            <thead>
              <tr>
                <th>Question type</th>
                <th>Question</th>
                <th>Answer required</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach items="${questions}" var="question">
                <tr>
                  <td>${question.question_type}</td>
                  <td>${question.question}</td>
                  <c:if test="${question.answer_required == 0}">
                    <td>No</td>
                  </c:if>
                  <c:if test="${question.answer_required == 1}">
                    <td>Yes</td>
                  </c:if>
                  <td><a href="${pageContext.request.contextPath}/Questions?r=view&amp;fid=${param.fid}&amp;id=${question.id}">Details</a> &middot; <a
                    href="${pageContext.request.contextPath}/Questions?r=update&amp;fid=${param.fid}&amp;id=${question.id}">Update</a> &middot; <a href="#"
                    data-controls-modal="${question.id}-modal" data-backdrop="static">Delete</a> | <a
                    href="${pageContext.request.contextPath}/Questions?r=up&amp;fid=${param.fid}&amp;id=${question.id}">Up</a> &middot; <a
                    href="${pageContext.request.contextPath}/Questions?r=down&amp;fid=${param.fid}&amp;id=${question.id}">Down</a></td>
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