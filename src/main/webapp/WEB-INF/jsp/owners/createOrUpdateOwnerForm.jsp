<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="owners">
    <h2>
        <c:if test="${owner['new']}"><fmt:message key="new"/> </c:if><b> </b><fmt:message key="owner.title"/>
    </h2>
    <form:form modelAttribute="owner" class="form-horizontal" id="add-owner-form">
        <div class="form-group has-feedback">
        	<fmt:message var="name" key="name"/>
        	<fmt:message var="surname" key="surname"/>
        	<fmt:message var="addres" key="addres"/>
        	<fmt:message var="city" key="city"/>
        	<fmt:message var="phone" key="phone"/>
        	<fmt:message var="username" key="username"/>
        	<fmt:message var="password" key="password"/>
            <petclinic:inputField label="${name}" name="firstName"/>  <%-- First Name --%>
            <petclinic:inputField label="${surname}" name="lastName"/>  <%-- Last Name --%>
            <petclinic:inputField label="${addres}" name="address"/>  <%-- Address --%>
            <petclinic:inputField label="${city}" name="city"/>  <%-- City --%>
            <petclinic:inputField label="${phone}" name="telephone"/>  <%-- Telephone --%>
            <petclinic:inputField label="${username}" name="user.username"/>  <%-- Username --%>
            <petclinic:inputField label="${password}" name="user.password"/>  <%-- Password --%>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${owner['new']}">
                        <button class="btn btn-default" type="submit"><fmt:message key="owner.add"/></button>  <%-- Add Owner --%>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit"><fmt:message key="owner.update"/></button>  <%-- Update Owner --%>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</petclinic:layout>
