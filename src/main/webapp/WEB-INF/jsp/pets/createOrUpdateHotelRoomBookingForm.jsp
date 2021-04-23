<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<petclinic:layout pageName="owners">
    <jsp:attribute name="customScript">
        <script>
            $(function () {
                $("#startDate").datepicker({dateFormat: 'yy/mm/dd'});
            });
        </script>
        <script>
            $(function () {
                $("#finishDate").datepicker({dateFormat: 'yy/mm/dd'});
            });
        </script>
    </jsp:attribute>
    <jsp:body>
        <h2><c:if test="${hotelRoom['new']}"><fmt:message key="new-female"/><b> </b></c:if><fmt:message key="hotel.room.booking"/></h2>

        <b><fmt:message key="pet.pet"/></b>
        <table class="table table-striped">
            <thead>
            <tr>
                <th><fmt:message key="name"/></th>
                <th><fmt:message key="birth"/></th>
                <th><fmt:message key="type"/></th>
                <th><fmt:message key="owner.owner"/></th>
            </tr>
            </thead>
            <tr>
                <td><c:out value="${hotelRoomBooking.pet.name}"/></td>
                <td><petclinic:localDate date="${hotelRoomBooking.pet.birthDate}" pattern="yyyy/MM/dd"/></td>
                <td><c:out value="${hotelRoomBooking.pet.type.name}"/></td>
                <td><c:out value="${hotelRoomBooking.pet.owner.firstName} ${hotelRoomBooking.pet.owner.lastName}"/></td>
            </tr>
        </table>

        <form:form modelAttribute="hotelRoomBooking" class="form-horizontal">
            <div class="form-group has-feedback">
            	<petclinic:selectFieldWithCustomName label="Habitación" name="hotelRoom" names="${hotelRooms}" selected="${hotelRoomBooking.hotelRoom.id}">
            	</petclinic:selectFieldWithCustomName>
    	        <fmt:message var="start" key="hotel.start"/>
            	<fmt:message var="finish" key="hotel.finish"/>				
				<petclinic:inputField label="${start}" name="startDate"/>
				<petclinic:inputField label="${finish}" name="finishDate"/>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <input type="hidden" name="pet" value="${hotelRoomBooking.pet.id}"/>
                    <button class="btn btn-default" type="submit"><fmt:message key="hotel.book"/></button>
                </div>
            </div>
        </form:form>

        <br/>
        <b><fmt:message key="hotel.booked"/></b>
        <table class="table table-striped">
            <tr>
				<th><fmt:message key="hotel.room.name"/></th>
				<th><fmt:message key="number"/></th>
	            <th><fmt:message key="hotel.start"/></th>
                <th><fmt:message key="hotel.finish"/></th>
            </tr>
            <c:forEach var="hotelRoomBooked" items="${hotelRoomBookings}">
                <c:if test="${!hotelRoom['new']}">
                    <tr>
						<td><c:out value="${hotelRoomBooked.hotelRoom.name}"/></td>
						<td><c:out value="${hotelRoomBooked.hotelRoom.number}"/></td>
                        <td><petclinic:localDate date="${hotelRoomBooked.startDate}" pattern="yyyy/MM/dd"/></td>
                        <td><petclinic:localDate date="${hotelRoomBooked.finishDate}" pattern="yyyy/MM/dd"/></td>
                    </tr>
                </c:if>
            </c:forEach>
        </table>
    </jsp:body>

</petclinic:layout>
