<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="causes">

    <h2>Información de Causas</h2>

    <table class="table table-striped">
        <tr>
            <th>Descripción</th>
            <td><b><c:out value="${cause.description}"/></b></td>
        </tr>
        <tr>
            <th>Objetivo del presupuesto</th>
            <td><c:out value="${cause.budgetTarget}"/></td>
        </tr>
        <tr>
            <th>Organización</th>
            <td><c:out value="${cause.organization}"/></td>
        </tr>
        <tr>
            <th>Estado de la causa</th>
            <td><c:out value="${cause.closed}"/></td>
        </tr>
    </table>

     <spring:url value="/causes/{causeId}/donations" var="donationsUrl">
        <spring:param name="causeId" value="${cause.id}"/>
    </spring:url>
    
    <a href="${fn:escapeXml(donationsUrl)}" class="btn btn-default">Ver donaciones</a> 

</petclinic:layout>