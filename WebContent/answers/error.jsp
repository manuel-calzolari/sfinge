<%@ page language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Sfinge - Error</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.css">
<style type="text/css">
body {
	padding-top: 60px;
}
</style>
</head>
<body>
  <div class="container-fluid">
      <div class="alert-message block-message error">
        <p>
          <strong>Error!</strong> Fields in red are required!
        </p>
        <div class="alert-actions">
          <a class="btn small" href="javascript:history.go(-1)">Go back</a>
        </div>
      </div>
  </div>
</body>
</html>