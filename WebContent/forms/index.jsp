<%@ page language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Sfinge - Enter passphrase</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.css">
<style type="text/css">
  body {
    padding-top: 60px;
  }
</style>
</head>
<body>
<div class="container-fluid">
      <div class="content">
        <h1>Enter passphrase</h1>
        <form name="input" action="${pageContext.request.contextPath}/Questions" method="get">
        <fieldset>
          <div class="clearfix">
            <label>Passphrase</label>
            <div class="input">
              <input name="p" type="text">
            </div>
            <br>
          </div>
        <div class="actions">
          <input type="submit" class="btn primary" value="Submit">&nbsp;<button type="reset" class="btn">Reset</button>
        </div>
        </fieldset>
        </form>
      </div>
</div>
</body>
</html>