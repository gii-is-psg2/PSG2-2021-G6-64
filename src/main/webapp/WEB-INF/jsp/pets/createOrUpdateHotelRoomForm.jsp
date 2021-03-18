<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


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
        <h2><c:if test="${hotelRoom['new']}">New </c:if>Hotel Room Booking</h2>

        <b>Pet</b>
        <table class="table table-striped">
            <thead>
            <tr>
                <th>Name</th>
                <th>Birth Date</th>
                <th>Type</th>
                <th>Owner</th>
            </tr>
            </thead>
            <tr>
                <td><c:out value="${hotelRoom.pet.name}"/></td>
                <td><petclinic:localDate date="${hotelRoom.pet.birthDate}" pattern="yyyy/MM/dd"/></td>
                <td><c:out value="${hotelRoom.pet.type.name}"/></td>
                <td><c:out value="${hotelRoom.pet.owner.firstName} ${hotelRoom.pet.owner.lastName}"/></td>
            </tr>
        </table>

        <form:form modelAttribute="hotelRoom" class="form-horizontal">
            <div class="form-group has-feedback">
                <petclinic:inputField label="Room name" name="name"/>
                <petclinic:inputField label="Start date" name="startDate"/>
                <petclinic:inputField label="Finish date" name="finishDate"/>
            </div>

            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <input type="hidden" name="petId" value="${hotelRoom.pet.id}"/>
                    <button class="btn btn-default" type="submit">Book Room</button>
                </div>
            </div>
        </form:form>

        <br/>
        <b>Previous Hotel Rooms Booked</b>
        <table class="table table-striped">
            <tr>
                <th>Room Name</th>
                <th>Start Date</th>
                <th>Finish Date</th>
            </tr>
            <c:forEach var="hotelRoom" items="${hotelRooms}">
                <c:if test="${!hotelRoom['new']}">
                    <tr>
						<td><c:out value="${hotelRoom.name}"/></td>
                        <td><petclinic:localDate date="${hotelRoom.startDate}" pattern="yyyy/MM/dd"/></td>
                        <td><petclinic:localDate date="${hotelRoom.finishDate}" pattern="yyyy/MM/dd"/></td>
                    </tr>
                </c:if>
            </c:forEach>
        </table>
    </jsp:body>

</petclinic:layout>
