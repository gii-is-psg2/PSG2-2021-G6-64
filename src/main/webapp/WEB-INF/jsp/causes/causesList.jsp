<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
	
<petclinic:layout pageName= "causes">

    <h2>Causas</h2>

        <table id= "causesTable" class= "table table-striped">
        <thead>
        <tr>
            <th><fmt:message key="name"/></th>
            <th><fmt:message key="org"/></th>
            <th><fmt:message key="cause.objetive"/></th>
            <th><fmt:message key="cause.reach"/></th>
            <th><fmt:message key="cause.info"/></th>
            <th><fmt:message key="donation.donate"/></th> 
        </tr>
        </thead>
        <tbody>
        <c:forEach items= "${causes}" var= "cause">
            <tr>
            	 <td>
                	<spring:url value= "/causes/{causeId}" var= "causeUrl">
                	    <spring:param name= "causeId" value= "${cause.id}"/>
                    </spring:url>
                    <a href= "${fn:escapeXml(causeUrl)}"><c:out value= "${cause.name}"/></a>
                </td> 
                <td>
                	<c:out value= "${cause.organization}"/>
                </td>               
                <td>
                    <c:out value= "${cause.budgetTarget}"/> $
                </td>
                <td>
                	<c:out value="${cause.budgetAchieved}"/> $ (<fmt:formatNumber type="number" maxFractionDigits="2" value="${(cause.budgetAchieved / cause.budgetTarget)*100}" />%)
                </td>
                <td>
  				    <spring:url value= "/causes/{causeId}/" var= "editUrl">
  				    <spring:param name= "causeId" value= "${cause.id}"/>
				    </spring:url>
 				    <a href= "${fn:escapeXml(editUrl)}"><fmt:message key="cause.infoShow"/></a> 
                </td>
                <td>
                <c:if test= "${!cause.closed}">
                	<sec:authorize access="hasAuthority('owner')">
	   				    <spring:url value= "/causes/{causeId}/donations/new" var= "donateUrl">
	   				    	<spring:param name= "causeId" value= "${cause.id}"/>
	 				    </spring:url>
	  				    <a href= "${fn:escapeXml(donateUrl)}"><img src="../resources/images/money.png" width = 30px></a>
	  				    
	  				    <!-- <fmt:message key="donation.donate"/> -->
  				    </sec:authorize>
    			</c:if>
                </td>                
            </tr>
        </c:forEach>
        </tbody>
    </table>
  
  <button class= "btn btn-default" onclick= "window.location.href='/causes/new'"><fmt:message key="cause.add"/></button>

</petclinic:layout>