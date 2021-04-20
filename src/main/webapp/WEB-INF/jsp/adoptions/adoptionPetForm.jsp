<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<petclinic:layout pageName="adoptionApply">
	<jsp:body>
		 <h2>
		 	<fmt:message key="pet.adoption"/>
		 </h2>
		 <form:form modelAttribute="adoptionApply" class="form-horizontal">
		 	<input type="hidden" name="id" value="${pet.id}"/>
			 <div class="form-group has-feedback">
			 	<fmt:message key="adoptions.solicitud" var="applyAdoption"/>
			    <petclinic:inputField label="${applyAdoption}" name="solicitud"/>
			  	<button class="btn btn-default" type="submit"><fmt:message key= "adoption.send"/></button>                             
			</div>
		</form:form>
	</jsp:body>
</petclinic:layout>