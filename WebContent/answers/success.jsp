<%@ page language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Sfinge - Success</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.css">
<style type="text/css">
body {
	padding-top: 60px;
}
</style>
</head>
<body>
  <div class="container-fluid">
    <div class="alert-message block-message success">
      <p>
        <strong>Success!</strong> Your answers have been correctly sent.
      </p>
      <c:if test="${not empty admin}">
        <div class="alert-actions">
          <a class="btn small" href="${pageContext.request.contextPath}/Forms">Go to the administration</a>
        </div>
      </c:if>
    </div>
  </div>
</body>
</html>