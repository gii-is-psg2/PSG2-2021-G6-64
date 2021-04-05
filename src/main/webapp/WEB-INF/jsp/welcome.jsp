<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<!-- %@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %-->  

<petclinic:layout pageName="home">
    <h2><fmt:message key="welcome"/></h2> 
    <h3> PSG2-2021-G6-64 </h3>

    <div class="row">
        <div class="col-md-12">
        	<div class = imagen1>
            <spring:url value="/resources/images/medical-personnel.jpg" htmlEscape="true" var="petsImage"/>
            <img class="img-responsive" src="${petsImage}"/>
            </div>
        </div>
    </div>
</petclinic:layout>
