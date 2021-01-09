<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
	<head>
		<title>Timers</title>
		<link href="webjars/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
	</head>
	<body>
		<div class="container">
			<h1>Timers</h1>
			<table class="table table-striped">
				<caption>Timers created are</caption>
				<thead>
					<tr>
						<th>Id</th>
						<th>Counter Value</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${timers}" var="timer">
						<tr>
							<td>${timer.id}</td>
							<td>${timer.counterValue}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
	    </div>
	    <script src="webjars/jquery/1.9.1/jquery.min.js"></script>
		<script src="webjars/bootstrap/3.3.6/js/bootstrap.min.js"></script>
	</body>
</html>