<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

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

    <title>Create Organization</title>

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
        <h1>Create Organization</h1>
      </div>
      <!-- Begin form -->
      <spring:url value="/organizations" var="organizationsEndpointUrl" />
      <form:form method="post" modelAttribute="command" role="form" action="${organizationsEndpointUrl}">
        <div class="row">
          <div class="form-group col-sm-6">
            <label for="name">Organization Name</label>
            <form:input path="name" type="text" cssClass="form-control" id="name" placeholder="Organization Name" autocomplete="disable" />
          </div>
          <div class="form-group col-sm-6">
            <label for="email">Email Address</label>
            <form:input path="email" type="email" cssClass="form-control" id="email" placeholder="Email Address" autocomplete="disable" />
          </div>
        </div>
        <div class="row">
          <div class="form-group col-sm-6">
            <label for="peopleNumber">People Number</label>
            <form:input path="peopleNumber" type="text" cssClass="form-control" id="peopleNumber" placeholder="Organization Name" autocomplete="disable" />
          </div>
        </div>
        <div class="row">
          <div class="form-group col-sm-6">
            <label for="manager">Organization Manager</label>
            <form:select path="manager.id" id="manager" cssClass="form-control" items="${people}" itemLabel="fullName" itemValue="id" />
          </div>
          <div class="form-group col-sm-6">
            <label for="contact">Contact Person</label>
            <form:select path="contact.id" id="contact" cssClass="form-control" items="${people}" itemLabel="fullName" itemValue="id" />
          </div>
        </div>
        <div class="row">
          <div class="form-group col-sm-6">
            <label for="usedCurrency">Used Currency</label>
            <form:select path="usedCurrency.code" id="usedCurrency" cssClass="form-control" items="${currencies}" itemValue="code" itemLabel="name" />
          </div>
        </div>
        <a href="${organizationsEndpointUrl}" class="btn btn-warning">Cancel</a>
        <button type="submit" class="btn btn-primary">Create</button>
      </form:form>
      <!-- End form -->
    </div>

    <div id="footer">
      <div class="container">
        <p class="text-muted">&copy; 2014 - Innovez One - Search Spring Data Samples</p>
      </div>
    </div>


    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
    <script src="${staticAssetsUrl}/js/bootstrap.min.js"></script>
  </body>
</html>
