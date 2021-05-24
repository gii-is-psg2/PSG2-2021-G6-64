<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%-- Placed at the end of the document so the pages load faster --%>
<spring:url value="/webjars/jquery/2.2.4/jquery.min.js" var="jQuery"/>
<script src="${jQuery}"></script>

<%-- jquery-ui.js file is really big so we only load what we need instead of loading everything --%>
<spring:url value="/webjars/jquery-ui/1.11.4/jquery-ui.min.js" var="jQueryUiCore"/>
<script src="${jQueryUiCore}"></script>

<%-- Bootstrap --%>
<spring:url value="/webjars/bootstrap/3.3.6/js/bootstrap.min.js" var="bootstrapJs"/>
<script src="${bootstrapJs}"></script>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<footer class="navbar navbar-default text-center text-white">
  <div class="container pt-4">
    <section class="mb-4">
    <a
        class="btn btn-link btn-floating btn-lg text-dark m-1"
        href="/?lang=es"
        role="button"
        data-mdb-ripple-color="dark"
        ><img alt="ES" height="32" width="36" align="middle" src="/resources/images/es.svg">
	</a>
    <a
        class="btn btn-link btn-floating btn-lg text-dark m-1"
        href="/?lang=en"
        role="button"
        data-mdb-ripple-color="dark"
        ><img alt="ES" height="32" width="36" align="middle" src="/resources/images/gb.svg">
        
    </a>
    </section>
  </div>
</footer>