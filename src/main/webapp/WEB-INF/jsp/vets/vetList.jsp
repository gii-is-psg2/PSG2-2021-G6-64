<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
	
<petclinic:layout pageName="vets">
    <h2><fmt:message key="vet.vets"/></h2>

    <table id="vetsTable" class="table table-striped">
        <thead>
        <tr>
            <th><fmt:message key="name"/></th>
            <th><fmt:message key="specialtys"/></th>
    		<sec:authorize access="hasAuthority('admin')">
            <th><fmt:message key="edit"/></th>
            <th><fmt:message key="delete"/></th>
            </sec:authorize>
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
                    <c:if test="${vet.nrOfSpecialties == 0}"><fmt:message key="none"/></c:if>
                </td>
                <sec:authorize access="hasAuthority('admin')">
                <td style="padding: 8px 20px;">
	                <spring:url value="/vet/{vetId}/edit" var="editVetUrl">
	                 	<spring:param name="vetId" value="${vet.id}"/>
	                </spring:url>
	                <a href="${fn:escapeXml(editVetUrl)}">
	                	<span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
	               	</a>
                </td>
                
                <td style="padding: 8px 30px;">
                <spring:url value="vets/{vetId}/delete" var="deleteVetUrl">
        		<spring:param name="vetId" value="${vet.id}"/>
    			</spring:url>
   				 <a href="${fn:escapeXml(deleteVetUrl)}">
   				 	<span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
   				 </a>
                 </td>
                 </sec:authorize> 
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <table class="table-buttons">
        <tr >

        	<td style="display:block;margin:20px 0px">
        		<spring:url value="/vet/new" var="newVetUrl">
                </spring:url>
                <sec:authorize access="hasAuthority('admin')">
                	<a href="${fn:escapeXml(newVetUrl)}" class="btn btn-default"><fmt:message key="vet.add"/></a>
				</sec:authorize>           
        	</td>         
                       

        </tr>
        <tr>
         	<td style="display:block;margin:20px 0px">
                <a href="<spring:url value="/vets.xml" htmlEscape="true" />"><fmt:message key="xml"/></a>
            </td>
        </tr>
    </table>
</petclinic:layout>