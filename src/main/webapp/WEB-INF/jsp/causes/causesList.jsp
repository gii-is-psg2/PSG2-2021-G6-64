<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName= "causes">

    <h2>Causas</h2>

        <table id= "causesTable" class= "table table-striped">
        <thead>
        <tr>
            <th>Causa</th>
            <th>Organización</th>
            <th>Objetivo del presupuesto</th>
            <th>Total donado</th>
            <th>Info</th>
            <th>Donar</th> 
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
                    <c:out value= "${cause.budgetTarget}"/>
                </td>
                <td>
                	<c:out value= "${cause.budgetAchieved}"/>
                </td>
                <td>
                <c:if test= "${cause.budgetTarget > cause.budgetAchieved}">
   				    <spring:url value= "/causes/{causeId}/" var= "editUrl">
   				    <spring:param name= "causeId" value= "${cause.id}"/>
 				    </spring:url>
  				    <a href= "${fn:escapeXml(editUrl)}">Ver causa</a> 
    			    </c:if>
                </td>
                <td>
                <c:if test= "${cause.budgetTarget > cause.budgetAchieved}">
   				    <spring:url value= "/causes/{causeId}/donations/new" var= "donateUrl">
   				    <spring:param name= "causeId" value= "${cause.id}"/>
 				    </spring:url>
  				    <a href= "${fn:escapeXml(donateUrl)}">Donar</a> 
    			    </c:if>
                </td>                
            </tr>
        </c:forEach>
        </tbody>
    </table>
  
  <button class= "btn btn-default" onclick= "window.location.href='/causes/new'">Crear Causa</button>

</petclinic:layout>