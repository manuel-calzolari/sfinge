<%@ page language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Sfinge - Update question</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.css">
<style type="text/css">
body {
	padding-top: 60px;
}

label.highlight {
	color: #FF0000;
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
      <c:if test="${question.question_type == 'checkbox'}"><jsp:include page="_update_checkbox.jsp" /></c:if>
      <c:if test="${question.question_type == 'password'}"><jsp:include page="_update_password.jsp" /></c:if>
      <c:if test="${question.question_type == 'radio'}"><jsp:include page="_update_radio.jsp" /></c:if>
      <c:if test="${question.question_type == 'select'}"><jsp:include page="_update_select.jsp" /></c:if>
      <c:if test="${question.question_type == 'text'}"><jsp:include page="_update_text.jsp" /></c:if>
      <c:if test="${question.question_type == 'textarea'}"><jsp:include page="_update_textarea.jsp" /></c:if>
    </div>
  </div>
</body>
</html>