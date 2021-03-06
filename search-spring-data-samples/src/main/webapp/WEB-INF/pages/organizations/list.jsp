<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ taglib prefix="search" uri="http://innovez-one.com/spring-data/custom/search" %>

<spring:url value="/" var="baseUrl" />
<spring:url value="/assets" var="staticAssetsUrl" />
<spring:url value="/organizations" var="organizationsEndpointUrl" />
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    
    <link rel="shortcut icon" href="../../assets/ico/favicon.ico">

    <title>Organization List</title>

    <!-- Bootstrap core CSS -->
    <link href="${staticAssetsUrl}/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="${staticAssetsUrl}/css/style.css" rel="stylesheet">

    <!-- Just for debugging purposes. Don't actually copy this line! -->
    <!--[if lt IE 9]><script src="${staticAssetsUrl}/assets/js/ie8-responsive-file-warning.js"></script><![endif]-->

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>

  <body>
    <!-- Fixed navbar -->
    <div class="navbar navbar-default navbar-fixed-top" role="navigation">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="${baseUrl}">Search Spring Data</a>
        </div>
        <div class="collapse navbar-collapse">
          <ul class="nav navbar-nav">
            <li><a href="${baseUrl}">Home</a></li>
            <li class="active"><a href="${organizationsEndpointUrl}">Organization</a></li>
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </div>

    <!-- Begin page content -->
    <div class="container">
      <div class="page-header">
        <h1>Organization List</h1>
      </div>
      <div class="row">
        <div class="btn-group col-sm-8">
      	  <a href="${organizationsEndpointUrl}?create" class="btn btn-primary">New Organization</a>
        </div>
        <div class="col-md-4">
        	<search:simpleForm formObject="${searchOrgnazitionsForm}" formUrl="${organizationsEndpointUrl}?search" scriptVar="searchScript" />
        </div>
      </div>
      <div class="table-responsive">
        <table class="table table-striped table-hover">
          <thead>
          	<tr>
          	  <th>#</th>
           	  <th>Organization Name</th>
           	  <th>Organization Email</th>
           	  <th>People Number</th>
           	  <th>Manager Name</th>
           	  <th>Manager Email</th>
           	  <th>Contact Person Name</th>
           	  <th>Contact Person Email</th>
           	  <th>Used Currency</th>
           	</tr>
          </thead>
          <tbody>
            <c:forEach items="${pagedDataList.content}" var="organization" varStatus="loop">
              <tr>
                <td>${(pagedDataList.number * pagedDataList.size) + (loop.index + 1)}</td>
                <td><c:out value="${organization.name}" /></td>
                <td><c:out value="${organization.email}" /></td>
                <td><c:out value="${organization.peopleNumber}" /></td>
                <td><c:out value="${organization.manager.fullName}" /></td>
                <td><c:out value="${organization.manager.emailAddress}" /></td>
                <td><c:out value="${organization.contact.fullName}" /></td>
                <td><c:out value="${organization.contact.emailAddress}" /></td>
                <td><c:out value="${organization.usedCurrency.name}" /></td>
              </tr>
            </c:forEach>
          </tbody>
        </table>
      </div>
    </div>

    <div id="footer">
      <div class="container">
        <p class="text-muted">&copy; 2014 - Innovez One - Search Spring Data Samples</p>
      </div>
    </div>


    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="${staticAssetsUrl}/js/jquery-1.11.0.min.js"></script>
    <script src="${staticAssetsUrl}/js/bootstrap.min.js"></script>
    ${searchScript}
  </body>
</html>
