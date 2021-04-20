<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
	
<petclinic:layout pageName="adoptions">
    <h2><fmt:message key="pet.adoption"/></h2>

    <table id="adoptionsTable" class="table table-striped">
        <thead>
	        <tr>
	            <th><fmt:message key="name"/></th>
	            <th><fmt:message key="type"/></th>
	            <th><fmt:message key="birth"/></th>
	            <th><fmt:message key="owner.title"/></th>
	            <th></th>
	        </tr>
        </thead>
        <tbody>
	        <c:forEach items="${listaPets}" var="pet">
	            <tr>
	                <td>
	                    <c:out value="${pet.name}"/>
	                </td>                         
	               	<td>
	                 	<c:out value="${pet.type}"/>
	                </td>
	                <td>
	                 	<c:out value="${pet.birthDate}"/>
	                </td>
	                <td>
	                 	<c:out value="${pet.owner.firstName}"/>
	                </td>
	               	<td>
		               	<sec:authorize access="hasAuthority('owner')">
		                    <spring:url value="/adoptions/{petId}/apply" var="applyAdoptUrl">
		                        <spring:param name="petId" value="${pet.id}"/>
		                    </spring:url>
		                    <c:if test="${(pet.owner.id != currentOwnerId) && !currentApplications.contains(pet)}">
                            <a href="${fn:escapeXml(applyAdoptUrl)}" class="btn btn-default"><fmt:message key="pet.adoption"/></a>
                            </c:if>
                            <c:if test="${pet.owner.id == currentOwnerId}">
                            	<b><fmt:message key="owned"/></b>
                            </c:if>
                            <c:if test="${currentApplications.contains(pet)}">
                            	<b><fmt:message key="adoption.applied"/></b>
                            </c:if>
		                </sec:authorize>
	               	</td>
	            </tr>    
	        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>