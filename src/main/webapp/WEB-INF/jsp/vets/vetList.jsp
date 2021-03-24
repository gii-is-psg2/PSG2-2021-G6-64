<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="vets">
    <h2>Veterinarians</h2>

    <table id="vetsTable" class="table table-striped">
        <thead>
        <tr>
            <th>Name</th>
            <th>Specialties</th>
            <th>Edit</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${vets.vetList}" var="vet">
            <tr>
                <td>
                    <c:out value="${vet.firstName} ${vet.lastName}"/>
                </td>
                <td>
                    <c:forEach var="specialty" items="${vet.specialties}">
                        <c:out value="${specialty.name} "/>
                    </c:forEach>
                    <c:if test="${vet.nrOfSpecialties == 0}">none</c:if>
                </td>
                <td>
	                <spring:url value="/vet/{vetId}/edit" var="editVetUrl">
	                 	<spring:param name="vetId" value="${vet.id}"/>
	                </spring:url>
	                <a href="${fn:escapeXml(editVetUrl)}">
	                	<span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
	                </a>	
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <table class="table-buttons">
        <tr>
        	<td>
        		<spring:url value="/vet/new" var="newVetUrl">
                </spring:url>
                <a href="${fn:escapeXml(newVetUrl)}" class="btn btn-default">New Vet</a>
        	</td>          
        </tr>
    </table>
</petclinic:layout>
