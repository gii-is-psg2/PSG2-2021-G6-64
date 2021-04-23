<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
	
<petclinic:layout pageName= "donations">

    <h2><fmt:message key="donation.of"/> <b> </b> ${cause.name}</h2>

        <table id= "causesTable" class= "table table-striped">
        <thead>
        <tr>
           	<th><fmt:message key="desc"/></th> 
            <th><fmt:message key="amount"/></th>
            <th><fmt:message key="owner.title"/></th>  
        </tr>
        </thead>
        <tbody>
        <c:forEach items= "${donations}" var= "donation">
            <tr>
                <td>
                	<c:out value= "${donation.description}"/>
                </td>               
                <td>
                    <c:out value= "${donation.amount}"/>
                </td>
                <td>
                    <c:out value= "${donation.owner.firstName}"/>
                </td>               
            </tr>
        </c:forEach>
        </tbody>
        <tfoot>
        	<tr>
                <td>
                	<b><fmt:message key="total"/></b>
                </td>               
                <td>
                   	<b><c:out value= "${cause.budgetAchieved}"/></b>
                </td>
                <td>
                </td>                                
            </tr>
        </tfoot>
    </table>
  	<c:if test= "${!cause.closed}">
		<sec:authorize access="hasAuthority('owner')">
		    <spring:url value= "/causes/{causeId}/donations/new" var= "donateUrl">
		    	<spring:param name= "causeId" value= "${cause.id}"/>
	   		</spring:url>
		    <a class= "btn btn-default" href= "${fn:escapeXml(donateUrl)}"><fmt:message key="donation.donate"/></a> 
	    </sec:authorize> 
    </c:if>			    

</petclinic:layout>