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
                                <a href="${fn:escapeXml(applyAdoptUrl)}" class="btn btn-default"><fmt:message key="pet.adoption"/></a>
                </sec:authorize>
                            </td>
               
               
            </tr>  
              
        </c:forEach>
        </tbody>
  <%--   </table>
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
    </table> --%>
</petclinic:layout>