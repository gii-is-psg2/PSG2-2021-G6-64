<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="vets">
    <h2>Veterinarios</h2>

    <table id="vetsTable" class="table table-striped">
        <thead>
        <tr>
            <th>Nombre</th>
            <th>Especialidades</th>
            <th>Editar</th>
            <th>Eliminar</th>
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
                    <c:if test="${vet.nrOfSpecialties == 0}">ninguno</c:if>
                </td>
                <td>
	                <spring:url value="/vet/{vetId}/edit" var="editVetUrl">
	                 	<spring:param name="vetId" value="${vet.id}"/>
	                </spring:url>
	                <a href="${fn:escapeXml(editVetUrl)}">
	                	<span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
                </td>
                <td>
                <spring:url value="vets/{vetId}/delete" var="deleteVetUrl">
        		<spring:param name="vetId" value="${vet.id}"/>
    			</spring:url>
   				 <a href="${fn:escapeXml(deleteVetUrl)}">
   				 	<span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
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
                <a href="${fn:escapeXml(newVetUrl)}" class="btn btn-default">Nuevo Veterinario</a>
        	</td>          

            <td>
                <a href="<spring:url value="/vets.xml" htmlEscape="true" />">Ver como XML</a>
            </td>            

        </tr>
    </table>
</petclinic:layout>