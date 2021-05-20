<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<petclinic:layout pageName="rooms">
    <jsp:body>
        <h2><c:if test="${hotelRoom['new']}"><fmt:message key="new-female"/><b> </b></c:if><fmt:message key="hotel.room"/></h2>

        <form:form modelAttribute="hotelRoom" class="form-horizontal">
            <div class="form-group has-feedback">
            	<fmt:message var="name" key="hotel.room.name"/>
            	<fmt:message var="number" key="number"/>
                <petclinic:inputField label="${name}" name="name"/>
                <petclinic:inputField label="${number}" name="number"/>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <button class="btn btn-default" type="submit"> <fmt:message key="hotel.room.add"/></button>
                </div>
            </div>
        </form:form>
    </jsp:body>
</petclinic:layout>
