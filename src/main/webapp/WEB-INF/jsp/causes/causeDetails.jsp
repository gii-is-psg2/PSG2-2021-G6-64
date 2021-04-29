<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>

<petclinic:layout pageName="causes">

    <h2><fmt:message key="cause.info"/></h2>

    <table class="table table-striped">
    	<tr>
            <th><fmt:message key="name"/></th>
            <td><b><c:out value="${cause.name}"/></b></td>
        </tr>
        <tr>
            <th><fmt:message key="desc"/></th>
            <td><b><c:out value="${cause.description}"/></b></td>
        </tr>
        <tr>
            <th><fmt:message key="cause.objetive"/></th>
            <td><c:out value="${cause.budgetTarget}"/> $ </td>
        </tr>
        <tr>
            <th><fmt:message key="cause.reach"/></th>
            <td><c:out value="${cause.budgetAchieved}"/> $ <progress max="${cause.budgetTarget}" value="${cause.budgetAchieved}"> </progress></td>
        </tr>
        <tr>
            <th><fmt:message key="org"/></th>
            <td><c:out value="${cause.organization}"/></td>
        </tr>
        <tr>
            <th><fmt:message key="cause.state"/></th>
            <td>
	            <c:choose>
	            	<c:when test="${cause.closed}">
	                    <b><fmt:message key="close"/></b>
	                </c:when>
	                <c:otherwise>
	                   	<b><fmt:message key="open"/></b>
	                </c:otherwise>
	            </c:choose>
            </td>
        </tr>
    </table>
    <c:if test= "${!cause.closed}">
		<sec:authorize access="hasAuthority('owner')">
		    <spring:url value= "/causes/{causeId}/donations/new" var= "donateUrl">
		    	<spring:param name= "causeId" value= "${cause.id}"/>
	   		</spring:url>
		    <a class= "btn btn-default" href= "${fn:escapeXml(donateUrl)}"><fmt:message key="donation.donate"/></a> 
	    </sec:authorize> 
    </c:if>		
     <spring:url value="/causes/{causeId}/donations" var="donationsUrl">
        <spring:param name="causeId" value="${cause.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(donationsUrl)}" class="btn btn-default"><fmt:message key="donation.show"/></a> 
	
</petclinic:layout>