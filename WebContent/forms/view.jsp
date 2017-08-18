<%@ page language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Sfinge - Form details</title>
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
      </div>
    </div>
    <div class="content">
      <h1>Form details</h1>
      <p>
        <strong>Id:</strong> ${form.id}
      </p>
      <p>
        <strong>Title:</strong> ${form.title}
      </p>
      <p>
        <c:if test="${not empty form.desc}"><strong>Description:</strong> ${form.desc}</c:if>
      </p>
      <p>
        <strong>Created at:</strong> ${form.cdatetime}
      </p>
      <p>
        <strong>Modified at:</strong> ${form.mdatetime}
      </p>
      <p>
        <strong>Created by:</strong> ${user.name} (${user.username})
      </p>
      <p>
        <strong>Passphrase:</strong> ${form.passphrase}
      </p>
    </div>
  </div>
</body>
</html>