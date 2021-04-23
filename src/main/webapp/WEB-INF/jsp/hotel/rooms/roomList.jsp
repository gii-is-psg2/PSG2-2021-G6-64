<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
	
<petclinic:layout pageName="rooms">
    <h2><fmt:message key="hotel.rooms"/></h2>

    <table id="roomsTable" class="table table-striped">
        <thead>
        <tr>
            <th><fmt:message key="name"/></th>
            <th><fmt:message key="number"/></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${hotelRooms}" var="hotelRoom">
            <tr>
                <td>
                    <c:out value="${hotelRoom.name}"/>
                </td>
                <td>
                    <c:out value="${hotelRoom.number} "/>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <table class="table-buttons">
        <tr>
        	<td style="display:block;margin:20px 0px">
        		<spring:url value="/hotel/rooms/new" var="newHotelRoomUrl">
                </spring:url>
                <sec:authorize access="hasAuthority('admin')">
                	<a href="${fn:escapeXml(newHotelRoomUrl)}" class="btn btn-default"><fmt:message key="hotel.room.add"/></a>
				</sec:authorize>           
        	</td>         
        </tr>
    </table>
</petclinic:layout>