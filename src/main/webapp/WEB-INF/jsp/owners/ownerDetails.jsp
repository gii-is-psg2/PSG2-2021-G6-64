<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="owners">

    <h2>Informaci√≥n del Propietario</h2>  <%-- Owner Information --%>


    <table class="table table-striped">
        <tr>
            <th>Nombre</th>
            <td><b><c:out value="${owner.firstName} ${owner.lastName}"/></b></td>
        </tr>
        <tr>
            <th>Direcci√≥n</th>
            <td><c:out value="${owner.address}"/></td>
        </tr>
        <tr>
            <th>Ciudad</th>
            <td><c:out value="${owner.city}"/></td>
        </tr>
        <tr>
            <th>Tel√©fono</th>
            <td><c:out value="${owner.telephone}"/></td>
        </tr>
    </table>
    
    <c:if test="${isCurrentOwner}">
    <spring:url value="{ownerId}/edit" var="editUrl">
        <spring:param name="ownerId" value="${owner.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Editar Propietario</a>  <%-- Edit Owner --%>

    <spring:url value="{ownerId}/pets/new" var="addUrl">
        <spring:param name="ownerId" value="${owner.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(addUrl)}" class="btn btn-default">AÒadir Nueva Mascota</a>
    </c:if>
    
	<security:authorize access="hasAnyRole('admin')">
    <spring:url value="{ownerId}/delete" var="deleteUrl">
        <spring:param name="ownerId" value="${owner.id}"/>
    </spring:url>
	<a href="${fn:escapeXml(deleteUrl)}" class="btn btn-default">Borrar Propietario</a>
	</security:authorize>

    <c:if test="${isCurrentOwner || isAdmin}">
    <br/>
    <br/>
    <br/>
    </c:if>
    <security:authorize access="hasAnyRole('admin')">
    <br/>
    <br/>
    <br/>
    </security:authorize>
    <h2>Mascotas y Visitas</h2>  <%-- Pets and Visits --%>

    <table class="table table-striped">
        <c:forEach var="pet" items="${owner.pets}">

            <tr>
                <td valign="top">
                    <dl class="dl-horizontal">
                        <dt>Nombre</dt>
                        <dd><c:out value="${pet.name}"/></dd>
                        <dt>Fecha de nacimiento</dt>
                        <dd><petclinic:localDate date="${pet.birthDate}" pattern="yyyy-MM-dd"/></dd>
                        <dt>Tipo</dt>
                        <dd><c:out value="${pet.type.name}"/></dd>
                    </dl>
                </td>
                <td valign="top">
                    <table class="table-condensed">
                        <thead>
                        <tr>
                            <th>Fecha de visita</th>
                            <th>DescripciÛn</th>
                        </tr>
                        </thead>
                        <c:forEach var="visit" items="${pet.visits}">
                            <tr>
                                <td><petclinic:localDate date="${visit.date}" pattern="yyyy-MM-dd"/></td>
                                <td><c:out value="${visit.description}"/></td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td>
                                <spring:url value="/owners/{ownerId}/pets/{petId}/edit" var="petUrl">
                                    <spring:param name="ownerId" value="${owner.id}"/>
                                    <spring:param name="petId" value="${pet.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(petUrl)}">Editar Mascota</a>
                            </td>
                            <td>
                                <spring:url value="/owners/{ownerId}/pets/{petId}/visits/new" var="visitUrl">
                                    <spring:param name="ownerId" value="${owner.id}"/>
                                    <spring:param name="petId" value="${pet.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(visitUrl)}">AÒadir Visita</a>
                            </td>
                        </tr>
                        <c:if test="${isCurrentOwner}">
                        <tr>
							<th>Habitaciones Reservadas</th>
							<th>Borrar Mascota</th>
                        </tr>
                        <tr>
							<td>
                                <spring:url value="/owners/{ownerId}/pets/{petId}/hotel-rooms/new" var="hotelRoomUrl">
                                    <spring:param name="ownerId" value="${owner.id}"/>
                                    <spring:param name="petId" value="${pet.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(hotelRoomUrl)}">Reservar HabitaciÛn</a>
                            </td>
                            <td>
                                <spring:url value="/owners/{ownerId}/pets/{petId}/delete" var="deletePetUrl">
                                    <spring:param name="ownerId" value="${owner.id}"/>
                                    <spring:param name="petId" value="${pet.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(deletePetUrl)}">Eliminar Mascota</a>
                            </td>
                        </tr>
                        </c:if>
                    </table>
                </td>
            </tr>

        </c:forEach>
    </table>

</petclinic:layout>
