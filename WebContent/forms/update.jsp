<%@ page language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Sfinge - Update form</title>
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
      </div>
    </div>
    <div class="content">
      <h1>Update form</h1>
      <c:if test="${(not empty error_required) || (not empty error_passphrase)}">
        <div class="alert-message block-message error">
          <p>
            <strong>Error!</strong>
          </p>
          <ul>
            <c:if test="${not empty error_required}">
              <li>Fields in red are required!</li>
            </c:if>
            <c:if test="${not empty error_passphrase}">
              <li>Passphrase already exists!</li>
            </c:if>
          </ul>
        </div>
      </c:if>
      <form method="post">
        <fieldset>
          <input type="hidden" name="referer" value="update_forms">
          <div class="clearfix">
            <label class="highlight">Title</label>
            <div class="input">
              <input name="title" type="text" value="${form.title}">
            </div>
            <br>
          </div>
          <div class="clearfix">
            <label>Description</label>
            <div class="input">
              <textarea cols="20" name="desc" rows="2">${form.desc}</textarea>
            </div>
            <br>
          </div>
          <div class="clearfix">
            <label class="highlight">Passphrase</label>
            <div class="input">
              <input name="passphrase" type="text" value="${form.passphrase}">
            </div>
            <br>
          </div>
          <p>Fields in red are required.</p>
          <div class="actions">
            <input type="submit" class="btn primary" value="Submit">&nbsp;
            <button type="reset" class="btn">Reset</button>
          </div>
        </fieldset>
      </form>
    </div>
  </div>
</body>
</html>