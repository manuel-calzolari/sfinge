<%@ page language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Sfinge - Manage answers</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.css">
<style type="text/css">
body {
	padding-top: 60px;
}

h2 {
	margin-top: 20px;
	margin-bottom: 10px;
}

div#supconf {
    margin-top: 20px;
    margin-bottom: 20px;
}

table#frequency {
	width: 50%;
}

th.secondary,td.secondary {
	width: 10%;
}

div.alert-message {
    margin-top: 30px;
}

div.actions {
    margin-bottom: -19px;
}
</style>
<script src="http://code.jquery.com/jquery-1.7.min.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap-modal.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.tablesorter.js"></script>
<script>
	$(function() {
		$("table#answers").tablesorter({
			sortList : [ [ 1, 0 ] ],
			headers : {
				0 : {
					sorter : false
				},
				4 : {
					sorter : false
				}
			}
		});
	});
	
	function toggle(obja, objb) {
		var ela = document.getElementById(obja);
		var elb = document.getElementById(objb);
		if (ela.style.display != "none") {
			ela.style.display = "none";
			elb.innerHTML = "Show question stats";
		} else {
			ela.style.display = "";
			elb.innerHTML = "Hide question stats";
		}
	};
	</script>
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
      <h1>Manage answers</h1>
      <c:choose>
        <c:when test="${empty answers_map}">
          <p>No answers.</p>
        </c:when>
        <c:otherwise>
        <form name="filter" method="get">
        <c:forEach items="${answers_map}" var="answer_map" varStatus="status_map">
          <c:forEach items="${answer_map.value}" var="answer">
            <!-- Begin Modal -->
            <div id="${answer.id}-modal" class="modal hide fade">
              <div class="modal-header">
                <a href="#" class="close">&times;</a>
                <h3>Delete</h3>
              </div>
              <div class="modal-body">
                <p>Are you sure?</p>
              </div>
              <div class="modal-footer">
                <a href="${pageContext.request.contextPath}/Answers?r=delete&amp;id=${answer.id}" class="btn primary">OK</a>
              </div>
            </div>
            <!-- End Modal -->
          </c:forEach>
          <div class="alert-message warning">
            <p><strong>Question [${answer_map.key.id}]:</strong> ${answer_map.key.question}</p>
          </div>
          <table class="zebra-striped" id="answers">
            <thead>
              <tr>
                <th>Association</th>
                <th>Answer</th>
                <th>Answered by</th>
                <th>Answered at</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach items="${answer_map.value}" var="answer" varStatus="status">
                <c:set var="x" value="false" />
                <c:forEach var="item" items="${association_x_list}">
                  <c:if test="${item eq answer.id}">
                    <c:set var="x" value="true" />
                  </c:if>
                </c:forEach>
                <c:set var="y" value="false" />
                <c:forEach var="item" items="${association_y_list}">
                  <c:if test="${item eq answer.id}">
                    <c:set var="y" value="true" />
                  </c:if>
                </c:forEach>
                <c:set var="contains" value="false" />
                <c:forEach var="item" items="${filters_list}">
                  <c:if test="${item eq answer.id}">
                    <c:set var="contains" value="true" />
                  </c:if>
                </c:forEach>
                <tr>
                  <td><input type="checkbox" name="${answer.id}_x" value="checked" onclick="document.filter.submit()" <c:if test="${x == 'true'}"> checked</c:if>> &nbsp; X &nbsp;&nbsp;&nbsp; 
                      <input type="checkbox" name="${answer.id}_y" value="checked" onclick="document.filter.submit()" <c:if test="${y == 'true'}"> checked</c:if>> &nbsp; Y</td>
                  <td><input type="checkbox" name="${answer.id}_answer" value="checked" onclick="document.filter.submit()" <c:if test="${contains == 'true'}"> checked</c:if>> &nbsp; ${answer.answer}</td>
                  <td>${users[answer.answerer_id].name} (${users[answer.answerer_id].username})</td>
                  <td>${answer.cdatetime}</td>
                  <td><a href="#" data-controls-modal="${answer.id}-modal" data-backdrop="static">Delete</a></td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
          <button type="button" id="${status_map.count-1}-button" class="btn" onclick="toggle('${status_map.count-1}-stats', '${status_map.count-1}-button')">Show question stats</button>
          <div id="${status_map.count-1}-stats" style="display: none">
          <h2>Question statistics</h2>
          <table class="zebra-striped condensed-table bordered-table" id="frequency">
            <thead>
              <tr>
                <th>Answer</th>
                <th class="secondary">Frequency</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="frequency" items="${frequencies[status_map.count-1]}">
                <tr>
                  <td>${frequency.key}</td>
                  <td>${frequency.value}</td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
          <c:if test="${average != 'NaN'}">
            <p>
              <strong>Average:</strong> ${averages[status_map.count-1]}
            </p>
          </c:if>
          <c:if test="${variance != 'NaN'}">
            <p>
              <strong>Variance:</strong> ${variances[status_map.count-1]}
            </p>
          </c:if>
          </div>
        </c:forEach>
        <div id="supconf">
          <p>
            <strong>Support %:</strong> ${support}
          </p>
          <p>
            <strong>Confidence %:</strong> ${confidence}
          </p>
        </div>
        <div class="well">
          <h3>Association rule mining</h3>
          <br>
          <label class="highlight">Min support %</label>
          <div class="input">
            <input name="minsup" type="text" value="${minsup}">
          </div>
          <br>
          <label class="highlight">Min confidence %</label>
          <div class="input">
            <input name="minconf" type="text" value="${minconf}">
          </div>
          <div class="actions">
            <input type="submit" class="btn primary" value="Submit">&nbsp;
            <button type="reset" class="btn">Reset</button>
          </div>
        </div>
        <c:if test="${not empty rules}">
        <pre class="prettyprint">
          <c:forEach var="rule" items="${rules}">
          ${rule}&#13;
          </c:forEach>
        </pre>
        </c:if>
        </form>
        </c:otherwise>
      </c:choose>
    </div>
  </div>
</body>
</html>