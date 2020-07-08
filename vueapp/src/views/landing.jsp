<html>
<head>
    <%@ include file="../partials/head.jsp" %>
    <link rel="stylesheet" type="text/css" href='<c:url value="/css/landing.css"/>'>
    <script type="text/javascript" src="https://cdn.jsdelivr.net/jquery.jssocials/1.4.0/jssocials.min.js"></script>
    <link type="text/css" rel="stylesheet" href="https://cdn.jsdelivr.net/jquery.jssocials/1.4.0/jssocials.css" />
    <link type="text/css" rel="stylesheet" href="https://cdn.jsdelivr.net/jquery.jssocials/1.4.0/jssocials-theme-flat.css" />
    <link type="text/css" rel="stylesheet" href="https://cdn.jsdelivr.net/jquery.jssocials/1.4.0/jssocials-theme-classic.css" />
    <link type="text/css" rel="stylesheet" href="https://cdn.jsdelivr.net/jquery.jssocials/1.4.0/jssocials-theme-minima.css" />
    <link type="text/css" rel="stylesheet" href="https://cdn.jsdelivr.net/jquery.jssocials/1.4.0/jssocials-theme-plain.css" />
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.6.0/css/all.css" integrity="sha384-aOkxzJ5uQz7WBObEZcHvV5JvRW3TUc2rNPA7pe3AwnsUohiw1Vj2Rgx2KSOkF5+h" crossorigin="anonymous">
</head>
<body>
<%@ include file="navbar/navbar.jsp" %>
<div class="container w-100 ml-4 mb-5">
    <h2 class="display-5 mt-5 green-text"><spring:message code="FindingMedicQuickAndEasy"/></h2>
</div>
<div class="container h-50 justify-content-center">
    <form class="filter-form p-3" action="<c:url value="/mediclist/1"/>">
        <div class="form-row">
            <div class="col">
                <h2 class="ml-5 mt-3 form-title"><spring:message code="SearchMedics"/></h2>
            </div>
        </div>
        <div class="form-row justify-content-center justify-content-around mt-3">
            <div class="col-5 pl-5">
                <label for="name"></label><input class="w-100 form-control" type="text" name="name" id="name"
                                                 placeholder="<spring:message code="NameAndOrSurname"/>">
            </div>
            <div class="col">
                <label for="specialties">
                </label>
                <select name="specialties" class="select-css form-control" id="specialties">
                    <option value="-1" disabled selected><spring:message code="Specialty"/></option>
                    <option value="-1"><spring:message code="Any"/></option>
                    <c:forEach var="specialty" items="${specialties}">
                        <option value="<c:out value="${specialty.id}"/>"><c:out value="${specialty.name}"/></option>
                    </c:forEach>
                </select>

            </div>
            <div class="col pr-5">
                <label for="localities">
                </label>
                <select name="localities" class="select-css form-control" id="localities">
                    <option value="-1" disabled selected><spring:message code="Locality"/></option>
                    <option value="-1"><spring:message code="Any"/></option>
                    <c:forEach var="locality" items="${localities}">
                        <option value="<c:out value="${locality.id}"/>"><c:out value="${locality.name}"/></option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="form-row px-5 mt-4 mb-3">
            <button class="w-100 btn rounded-pill btn-light header-btn-element"><spring:message
                    code="SearchMedics"/></button>
        </div>
    </form>
    <br>
    <div class="container"><hr></div>
    <div class="shareDiv">
        <h1 class="shareTitle">¡Compartí MediCare con tus amigos!</h1>
        <div class="shareSocial"></div>
        <br>
    </div>
    <br>
    <div class="container"><hr></div>
    <br>
    <br>
</div>
<script>
    logoUrls = [];
    logoUrls["facebook"] = "<c:url value="/img/logos/facebooklogo.svg"/>";
    logoUrls["twitter"] = "<c:url value="/img/logos/twitterlogo.svg"/>";
    logoUrls["whatsapp"] = "<c:url value="/img/logos/whatsapplogo.svg"/>";
    logoUrls["linkedin"] = "<c:url value="/img/logos/linkedinlogo.svg"/>";
    logoUrls["pinterest"] = "<c:url value="/img/logos/pinterestlogo.svg"/>";
</script>
<script src="<c:url value="/js/scripts/Landing.js"/>"></script>
</body>
</html>