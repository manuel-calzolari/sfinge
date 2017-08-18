<%@ page language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Sfinge - Question details</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.css">
<style type="text/css">
body {
	padding-top: 60px;
}
</style>
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
      <h1>Question details</h1>
      <p>
        <strong>Id:</strong> ${question.id}
      </p>
      <p>
        <strong>Question:</strong> ${question.question}
      </p>
      <p>
        <strong>Question type:</strong> ${question.question_type}
      </p>
      <c:if test="${not empty question.instructions}">
        <p>
          <strong>Instructions:</strong> ${question.instructions}
        </p>
      </c:if>
      <c:if test="${not empty question.options}">
        <p>
          <strong>Options:</strong> ${question.options}
        </p>
      </c:if>
      <c:if test="${not empty question.default_value}">
        <p>
          <strong>Default value:</strong> ${question.default_value}
        </p>
      </c:if>
      <c:if test="${(not empty question.max_length) && (question.max_length > 0)}">
        <p>
          <strong>Max length:</strong> ${question.max_length}
        </p>
      </c:if>
      <p>
        <strong>Created at:</strong> ${question.cdatetime}
      </p>
      <p>
        <strong>Modified at:</strong> ${question.mdatetime}
      </p>
      <c:if test="${(not empty question.rows) && (question.rows > 0)}">
        <p>
          <strong>Rows:</strong> ${question.rows}
        </p>
      </c:if>
      <c:if test="${not empty question.size}">
        <p>
          <strong>Size:</strong> ${question.size}
        </p>
      </c:if>
    </div>
  </div>
</body>
</html>