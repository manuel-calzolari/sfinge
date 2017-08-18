<%@ page language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<h1>Create radio</h1>
<c:if test="${not empty error_required}">
  <div class="alert-message block-message error">
    <p>
      <strong>Error!</strong>
    </p>
    <ul>
      <c:if test="${not empty error_required}">
        <li>Fields in red are required!</li>
      </c:if>
    </ul>
  </div>
</c:if>
<form method="post">
  <fieldset>
    <input type="hidden" name="referer" value="create_radio"> <input type="hidden" name="question_type" value="radio">
    <div class="clearfix">
      <label class="highlight">Question</label>
      <div class="input">
        <input name="question" type="text" value="${question.question}">
      </div>
      <br>
    </div>
    <div class="clearfix">
      <label>Instructions</label>
      <div class="input">
        <textarea cols="20" name="instructions" rows="5">${question.instructions}</textarea>
      </div>
      <br>
    </div>
    <div class="clearfix">
      <label class="highlight">Options</label>
      <div class="input">
        <textarea cols="20" name="options" rows="5">${options}</textarea>
        <span class="help-block">New line for every option.</span>
      </div>
      <br>
    </div>
    <div class="clearfix">
      <label>Answer required</label>
      <div class="input">
        <ul class="inputs-list">
          <li><label> <input name="answer_required" type="checkbox" value="checked"<c:if test="${question.answer_required == 1}"> checked</c:if>></label></li>
        </ul>
      </div>
      <br>
    </div>
    <div class="clearfix">
      <label>Default value</label>
      <div class="input">
        <input name="default_value" type="text" value="${question.default_value}">
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