<%@ page language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<h1>Create password</h1>
<c:if test="${(not empty error_required) || (not empty error_max_length)}">
  <div class="alert-message block-message error">
    <p>
      <strong>Error!</strong>
    </p>
    <ul>
      <c:if test="${not empty error_required}">
        <li>Fields in red are required!</li>
      </c:if>
      <c:if test="${not empty error_max_length}">
        <li>Max length must be numeric!</li>
      </c:if>
    </ul>
  </div>
</c:if>
<form method="post">
  <fieldset>
    <input type="hidden" name="referer" value="create_password"> <input type="hidden" name="question_type" value="password">
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
    <div class="clearfix">
      <label>Max length</label>
      <div class="input">
        <input name="max_length" type="text" value="<c:if test="${question.max_length > 0}">${question.max_length}</c:if>">
      </div>
      <br>
    </div>
    <div class="clearfix">
      <label>Size</label>
      <div class="input">
        <select name="size">
          <option></option>
          <option <c:if test="${question.size == 'mini'}"> selected="selected"</c:if>>mini</option>
          <option <c:if test="${question.size == 'small'}"> selected="selected"</c:if>>small</option>
          <option <c:if test="${question.size == 'medium'}"> selected="selected"</c:if>>medium</option>
          <option <c:if test="${question.size == 'large'}"> selected="selected"</c:if>>large</option>
          <option <c:if test="${question.size == 'xlarge'}"> selected="selected"</c:if>>xlarge</option>
          <option <c:if test="${question.size == 'xxlarge'}"> selected="selected"</c:if>>xxlarge</option>
        </select>
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