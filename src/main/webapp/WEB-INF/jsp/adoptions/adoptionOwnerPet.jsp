<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
	
<petclinic:layout pageName="petApplies">
    <h2><fmt:message key="adoption.request"/></h2>
    <table id="adoptionsApplies" class="table table-striped">
        <thead>
        <tr>
            <th><fmt:message key="owner.owner"/></th>
            <th><fmt:message key="adoption.text"/></th>
            <th></th>
        </tr>
        </thead>
        <tbody>
	        <c:forEach items="${listaPets}" var="apply">
	            <tr>
	               	<td>
	                   	<c:out value="${apply.owner.firstName}"/>
	               	</td>                         
	               	<td>
	               		<c:out value="${apply.solicitud}"/>
	               	</td>
	               	<td>
	                  	<spring:url value="/owners/{ownerId}/adoptions/{petId}/apply/{applyId}" var="acceptApply">
	                      	<spring:param name="ownerId" value="${ownerId}"/>
	                    	<spring:param name="applyId" value="${apply.id}"/>
	                      	<spring:param name="petId" value="${apply.pet.id}"/>
	                  	</spring:url>
	                  	<a href="${fn:escapeXml(acceptApply)}" class="btn btn-default"><fmt:message key="adoption.accept"/></a>
	          		</td>      
	            </tr>         
	        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>