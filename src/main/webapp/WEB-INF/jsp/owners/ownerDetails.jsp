<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
	
<petclinic:layout pageName="owners">

    <h2><fmt:message key="owner.ownerDetails"/></h2>  <%-- Owner Information --%>


    <table class="table table-striped">
        <tr>
            <th><fmt:message key="name"/></th>
            <td><b><c:out value="${owner.firstName} ${owner.lastName}"/></b></td>
        </tr>
        <tr>
            <th><fmt:message key="addres"/></th>
            <td><c:out value="${owner.address}"/></td>
        </tr>
        <tr>
            <th><fmt:message key="city"/></th>
            <td><c:out value="${owner.city}"/></td>
        </tr>
        <tr>
            <th><fmt:message key="phone"/></th>
            <td><c:out value="${owner.telephone}"/></td>
        </tr>
    </table>
    
    <c:if test="${isCurrentOwner}">
    <spring:url value="{ownerId}/edit" var="editUrl">
        <spring:param name="ownerId" value="${owner.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default"><fmt:message key="owner.update"/></a>  <%-- Edit Owner --%>

    <spring:url value="{ownerId}/pets/new" var="addUrl">
        <spring:param name="ownerId" value="${owner.id}"/>
    </spring:url>
  	<a href="${fn:escapeXml(addUrl)}" class="btn btn-default"><fmt:message key="pet.add"/></a>
    </c:if>
    
	<sec:authorize access="hasAuthority('admin')">
    <spring:url value="{ownerId}/delete" var="deleteUrl">
        <spring:param name="ownerId" value="${owner.id}"/>
    </spring:url>
	<a href="${fn:escapeXml(deleteUrl)}" class="btn btn-default"><fmt:message key="owner.delete"/></a>
	</sec:authorize>

    <c:if test="${isCurrentOwner || isAdmin}">
    <br/>
    <br/>
    <br/>
    </c:if>
    <sec:authorize access="hasAuthority('admin')">
    <br/>
    <br/>
    <br/>
    </sec:authorize>
	<h2><fmt:message key="pet.title"/></h2>  <%-- Pets and Visits --%>

    <table class="table table-striped">
        <c:forEach var="pet" items="${owner.pets}">

            <tr>
                <td valign="top">
                    <dl class="dl-horizontal">
						<dt><fmt:message key="name"/></dt>
                        <dd><c:out value="${pet.name}"/></dd>
                        <dt><fmt:message key="birth"/></dt>
                        <dd><petclinic:localDate date="${pet.birthDate}" pattern="yyyy-MM-dd"/></dd>
                        <dt><fmt:message key="type"/></dt>
                        <dd><c:out value="${pet.type.name}"/></dd>
                    </dl>
                </td>
                <td valign="top">
                    <table class="table-condensed">
                        <thead>
                        <tr>
                            <th><fmt:message key="pet.visitdate"/></th>
                            <th><fmt:message key="pet.visitdesc"/></th>
                            <c:if test="${!pet.visits.isEmpty() && isCurrentOwner && visitsCanBeDeleted.containsValue(true)}">
                            <th><fmt:message key="pet.visitdelete"/></th>
                            </c:if>
                        </tr>
                        </thead>
                        <c:forEach var="visit" items="${pet.visits}">
                            <tr>
                                <td><petclinic:localDate date="${visit.date}" pattern="yyyy-MM-dd"/></td>
                                <td><c:out value="${visit.description}"/></td>
                                <td style="text-align: center;">
                                <spring:url value="/owners/{ownerId}/pets/{petId}/visit/{visitId}/delete" var="deleteVisitUrl">
                                    <spring:param name="ownerId" value="${owner.id}"/>
                                    <spring:param name="petId" value="${pet.id}"/>
                                    <spring:param name="visitId" value="${visit.id}"/>
                                </spring:url>
                                <c:if test="${!pet.visits.isEmpty() && isCurrentOwner && visitsCanBeDeleted.get(visit.id)}">
                                <a href="${fn:escapeXml(deleteVisitUrl)}">
			   				 		<span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
			   				 	</a>
			   				 	</c:if>
                            	</td>
                            </tr>
                        </c:forEach>
                        <tr>
                        
                            <td>
                            	<c:if test="${isCurrentOwner}">
	                                <spring:url value="/owners/{ownerId}/pets/{petId}/edit" var="petUrl">
	                                    <spring:param name="ownerId" value="${owner.id}"/>
	                                    <spring:param name="petId" value="${pet.id}"/>
	                                </spring:url>
	                                <a href="${fn:escapeXml(petUrl)}"><fmt:message key="pet.update"/></a>
                             	</c:if>
                            </td>
                            <td>
                            	<c:if test="${isCurrentOwner}">
	                                <spring:url value="/owners/{ownerId}/pets/{petId}/visits/new" var="visitUrl">
	                                    <spring:param name="ownerId" value="${owner.id}"/>
	                                    <spring:param name="petId" value="${pet.id}"/>
	                                </spring:url>
	                                <a href="${fn:escapeXml(visitUrl)}"><fmt:message key="visit.add"/></a>
                                </c:if>
                            </td>
                                                                                    
                            <c:if test="${pet.enAdopcion==false && isCurrentOwner}">
                            <c:if test="${!petCanBeAdopted.get(pet.id)}">
                            <td>
                                <spring:url value="/owners/{ownerId}/pets/{petId}/adopt" var="adoptUrl">
                                    <spring:param name="ownerId" value="${owner.id}"/>
                                    <spring:param name="petId" value="${pet.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(adoptUrl)}"><fmt:message key="pet.adopt"/></a>
                            </td>
                            </c:if>
                            </c:if>
                            
                            <c:if test="${pet.enAdopcion== true && isCurrentOwner}">
                            <c:if test="${!petCanBeAdopted.get(pet.id)}">
                            <td>
                                <spring:url value="/owners/{ownerId}/adoptions/{petId}" var="adoptSetUrl">
                                    <spring:param name="ownerId" value="${owner.id}"/>
                                    <spring:param name="petId" value="${pet.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(adoptSetUrl)}"><fmt:message key="adoption.show"/></a>
                            </td>
                            </c:if>
                            </c:if>
                            
                        </tr>
                        <c:if test="${isCurrentOwner}">
                        <tr>
							<th><fmt:message key="hotel.booked"/></th>
							<th><fmt:message key="pet.delete"/></th>
                        </tr>
                        <tr>
							<td>
                                <spring:url value="/owners/{ownerId}/pets/{petId}/hotel-rooms/new" var="hotelRoomUrl">
                                    <spring:param name="ownerId" value="${owner.id}"/>
                                    <spring:param name="petId" value="${pet.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(hotelRoomUrl)}"><fmt:message key="hotel.book"/></a>
                            </td>
                            <td>
                                <spring:url value="/owners/{ownerId}/pets/{petId}/delete" var="deletePetUrl">
                                    <spring:param name="ownerId" value="${owner.id}"/>
                                    <spring:param name="petId" value="${pet.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(deletePetUrl)}"><fmt:message key="pet.delete"/></a>
                            </td>
                        </tr>
                        </c:if>
                    </table>
                </td>
            </tr>

        </c:forEach>
    </table>

</petclinic:layout>
