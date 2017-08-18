<%@ page language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>${form.title}</title>
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
    <div class="content">
      <h1>${form.title}</h1>
      <p>${form.desc}</p>
      <c:if test="${not empty answer_missing}">
        <div class="alert-message block-message error">
          <p>
            <strong>Error!</strong>
          </p>
          <ul>
            <c:if test="${not empty answer_missing}">
              <li>Fields in red are required!</li>
            </c:if>
          </ul>
        </div>
      </c:if>
      <form method="post">
        <fieldset>
          <input type="hidden" name="referer" value="questions_index">
          <c:forEach items="${questions}" var="question">
            <c:if test="${question.question_type == 'checkbox'}">
              <div class="clearfix">
                <label <c:if test="${question.answer_required == 1}"> class="highlight"</c:if>>${question.question}</label>
                <div class="input">
                  <ul class="inputs-list">
                    <c:forEach items="${question.options}" var="option">
                      <li><input type="hidden" name="${question.id}" value=""></li>
                      <li><label><input name="${question.id}" type="checkbox" value="${option}" <c:if test="${option == question.default_value}"> checked="checked"</c:if>> <span>${option}</span></label></li>
                    </c:forEach>
                  </ul>
                  <span class="help-block">${question.instructions}</span>
                </div>
                <br>
              </div>
            </c:if>
            <c:if test="${question.question_type == 'password'}">
              <div class="clearfix">
                <label <c:if test="${question.answer_required == 1}"> class="highlight"</c:if>>${question.question}</label>
                <div class="input">
                  <input name="${question.id}" type="password" <c:if test="${(not empty question.max_length) && (question.max_length > 0)}"> maxlength="${question.max_length}"</c:if> <c:if test="${not empty question.size}"> class="${question.size}"</c:if>> <span class="help-block">${question.instructions}</span>
                </div>
                <br>
              </div>
            </c:if>
            <c:if test="${question.question_type == 'radio'}">
              <div class="clearfix">
                <label <c:if test="${question.answer_required == 1}"> class="highlight"</c:if>>${question.question}</label>
                <div class="input">
                  <ul class="inputs-list">
                    <c:forEach items="${question.options}" var="option">
                      <li><input type="hidden" name="${question.id}" value=""></li>
                      <li><label><input name="${question.id}" type="radio" value="${option}" <c:if test="${option == question.default_value}"> checked="checked"</c:if>> <span>${option}</span></label></li>
                    </c:forEach>
                  </ul>
                  <span class="help-block">${question.instructions}</span>
                </div>
                <br>
              </div>
            </c:if>
            <c:if test="${question.question_type == 'select'}">
              <div class="clearfix">
                <label <c:if test="${question.answer_required == 1}"> class="highlight"</c:if>>${question.question}</label>
                <div class="input">
                  <select name="${question.id}">
                    <c:forEach items="${question.options}" var="option">
                      <option <c:if test="${option == question.default_value}"> selected="selected"</c:if>>${option}</option>
                    </c:forEach>
                  </select> <span class="help-block">${question.instructions}</span>
                </div>
                <br>
              </div>
            </c:if>
            <c:if test="${question.question_type == 'text'}">
              <div class="clearfix">
                <label <c:if test="${question.answer_required == 1}"> class="highlight"</c:if>>${question.question}</label>
                <div class="input">
                  <input name="${question.id}" type="text" <c:if test="${(not empty question.max_length) && (question.max_length > 0)}"> maxlength="${question.max_length}"</c:if> <c:if test="${not empty question.size}"> class="${question.size}"</c:if>> <span class="help-block">${question.instructions}</span>
                </div>
                <br>
              </div>
            </c:if>
            <c:if test="${question.question_type == 'textarea'}">
              <div class="clearfix">
                <label <c:if test="${question.answer_required == 1}"> class="highlight"</c:if>>${question.question}</label>
                <div class="input">
                  <textarea cols="20" name="${question.id}" <c:if test="${(not empty question.rows) && (question.rows > 0)}"> rows="${question.rows}"</c:if> <c:if test="${not empty question.size}"> class="${question.size}"</c:if>>${question.default_value}</textarea>
                  <span class="help-block">${question.instructions}</span>
                </div>
                <br>
              </div>
            </c:if>
          </c:forEach>
          <c:if test="${not empty required}">
            <p>Fields in red are required.</p>
          </c:if>
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