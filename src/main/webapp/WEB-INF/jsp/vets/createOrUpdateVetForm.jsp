<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


<petclinic:layout pageName="vets">
	<h2>
        <c:if test="${vet['new']}"><fmt:message key="new"/> </c:if><b> </b><fmt:message key="vet"/>
    </h2>
    <form:form modelAttribute="vet" class="form-horizontal" id="add-vet-form">
        <div class="form-group has-feedback">
   		    <fmt:message var="name" key="name"/>
            <fmt:message var="surname" key="surname"/>
            <fmt:message var="specialtys" key="specialtys"/>
            <petclinic:inputField label="${name}" name="firstName"/>
            <petclinic:inputField label="${surname}" name="lastName"/>
            <div class="control-group">
                  <petclinic:selectField name="specialties" label="${specialtys}" names="${specialities}" size="3" multiple="true"/>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${vet['new']}">
                        <button class="btn btn-default" type="submit">Add Vet</button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit">Update Vet</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form> 
</petclinic:layout>