<nav class="navbar navbar-expand header">
    <div class="container w-100 justify-content-start">
        <img class="navbar-brand" src='<c:url value="/img/whiteLogo.svg"/>' alt="logo" id="navbar-logo">
        <a class="navbar-brand header-brand header-a-element " href='<c:url value="/"/> '>MediCare</a>
    </div>
    <div class="container w-100 justify-content-end">
        <a href='<c:url value="/signup/staff"/>' class="header-a-element nav-link mx-3"><spring:message code="AreYouMedic"/></a>
        <a href='<c:url value="/signup"/>' class="header-a-element nav-link mx-3"><spring:message code="SignUp"/></a>
        <a href='<c:url value="/login"/>' class="header-btn-element btn btn-light rounded mx-3"><spring:message code="Login"/></a>
    </div>
</nav>