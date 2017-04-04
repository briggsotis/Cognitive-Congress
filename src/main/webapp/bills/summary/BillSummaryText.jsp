<!DOCTYPE HTML><%@page import="java.util.List"%>
<%@page import="com.cogcong.stats.BillStats"%>
<%@page language="java"
	contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<html>
<head>
	<% 
	
	int count = 10;
	String url = request.getRequestURL().toString();
	String baseURL = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath() + "/"; 
	BillStats bs = new BillStats();
	List<String> topKeywords = bs.getTopWords();
	List<String> topSubjects = bs.getTopSubjects();
	
	// Partisian words and subjects
	List<String> toprKeywords = bs.getTopRepKeywords();
	List<String> toprSubjects = bs.getTopRepSubjects();
	List<String> topdKeywords = bs.getTopDemKeywords();
	List<String> topdSubjects = bs.getTopDemSubjects();
	List<String> topiKeywords = bs.getTopIndKeywords();
	List<String> topiSubjects = bs.getTopIndSubjects();
	%>
	<!--  Bootstrap -->
	<link href="<%=baseURL%>bootstrap/css/bootstrap.min.css" rel="stylesheet">
<title>BillSummary</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-2"></div>
			<div class="col-md-8">
				<div>
					  <h1 class="display-3">Bill Overview</h1>
					  <p class="lead">A summary of the 6.5 thousand bills proposed during the 113th and 114th Senate.</p>
					  <hr class="my-4">
				</div>
			</div>
			<div class="col-md-2"></div>
		</div>
		<div class="row">
		<div class="col-md-2"></div>
		<div class="col-md-8">
		<div><!-- Keyword table -->
			<h2> Top Watson Concepts </h2>
			<p> The top keywords/concepts across all proposed bills found by IBM Watson. These keywords indicate the central topics a bill is concerned with.</p>
			<table class="table table-striped table-hover table-bordered">
			    <thead>
			      <tr>
			        <th>Key Concept</th>
			        <th>Number of Bills</th>
			      </tr>
			    </thead>
			    <tbody>
			    <%for (int i = 0; i < count; i++){ %>
			      <tr>
			        <td><%= topKeywords.get(i)%></td>
			        <td><%= bs.getWordCount(topKeywords.get(i)) %></td>
			      </tr>
			    <%} %>
			    </tbody>
			</table>
		</div><!-- Keyword table -->
		<hr></hr>
		<div><!-- Subjects table -->
			<h2> Top Bill Subjects </h2>
			<p> The top subjects of bills as assigned by the US Congress</p>
			<table class="table table-striped table-hover table-bordered">
			    <thead>
			      <tr>
			        <th>Subject</th>
			        <th>Number of Bills</th>
			      </tr>
			    </thead>
			    <tbody>
			    <%for (int i = 0; i < count; i++){ %>
			      <tr>
			        <td><%= topSubjects.get(i)%></td>
			        <td><%= bs.getSubjectCount(topSubjects.get(i)) %></td>
			      </tr>
			    <%} %>
			    </tbody>
			</table>
		</div><!-- Subjects table -->
		<hr></hr>
		<div><!-- Partisan Keywords table -->
			<h2> Top Bill Keywords by Party </h2>
			<p> The top keywords/concepts across all proposed bills found by IBM Watson, sorted by political party. </p>
			<table class="table table-striped table-hover table-bordered">
			    <thead>
			      <tr>
					<th>Rank</th>		      
			        <th>Republican</th>
			        <th></th>
			        <th>Democrat</th>
			        <th></th>
			        <th>Independent</th>
			        <th></th>
			      </tr>
			    </thead>
			    <tbody>
			    <%for (int i = 0; i < count; i++){ %>
			      <tr>
			      	<td><%= (i+1) %></td>
			        <td><%= toprKeywords.get(i)%></td>
			        <td><%= bs.getKeywordsRepCount(toprKeywords.get(i)) %></td>
			        <td><%= topdKeywords.get(i)%></td>
			        <td><%= bs.getKeywordsDemCount(topdKeywords.get(i)) %></td>
			        <td><%= topiKeywords.get(i)%></td>
			        <td><%= bs.getKeywordsIndCount(topiKeywords.get(i)) %></td>
			      </tr>
			    <%} %>
			    </tbody>
			</table>
		</div><!-- Partisan Keywords table -->
		<hr></hr>
		<div><!-- Partisan Subjects table -->
			<h2> Top Bill Subjects by Party </h2>
			<p> The top subjects of bills as assigned by the US Congress, sorted by political party.</p>
			<table class="table table-striped table-hover table-bordered">
			    <thead>
			      <tr>
			      	<th>Rank</th>
			        <th>Republican</th>
			        <th></th>
			        <th>Democrat</th>
			        <th></th>
			        <th>Independent</th>
			        <th></th>
			      </tr>
			    </thead>
			    <tbody>
			    <%for (int i = 0; i < count; i++){ %>
			      <tr>
			      	<td><%= (i+1) %></td>
			        <td><%= toprSubjects.get(i)%></td>
			        <td><%= bs.getSubjectRepCount(toprSubjects.get(i)) %></td>
			        <td><%= topdSubjects.get(i)%></td>
			        <td><%= bs.getSubjectDemCount(topdSubjects.get(i)) %></td>
			        <td><%= topiSubjects.get(i)%></td>
			        <td><%= bs.getSubjectIndCount(topiSubjects.get(i)) %></td>
			      </tr>
			    <%} %>
			    </tbody>
			</table>
		</div><!-- Partisan Subjects table -->
		</div>
		<div class="col-md-2"></div>
		</div>
		<div class="row">
			<div class="col-md-2"></div>
			<div class="col-md-8">
				<footer>
				<p>&copy; 2017, Phillip Lopez - pgl5711@rit.edu</p>
				</footer>
			</div>
			<div class="col-md-2"></div>
		</div>
	</div>
	
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="<%=baseURL%>bootstrap/js/bootstrap.min.js"></script>
</body>


</html>