<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="error">

<h1 style=" color: red;">Se ha producido un error</h1>
	<div class=imagen1>
    <spring:url value="/resources/images/error-404.jpg" var="petsImage"/>
    
    <img src="${petsImage}"/> 
	
    

    <p>${exception.message}</p>

	</div>
</petclinic:layout>
