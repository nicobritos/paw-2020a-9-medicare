// <nav class="navbar navbar-expand header">
//     <div class="container w-100 justify-content-start">
//         <img
//                 class="navbar-brand"
//                 src='<c:url value="/img/whiteLogo.svg"/>'
//                 alt="logo"
//                 id="navbar-logo"
//         />
//         <a class="navbar-brand header-brand header-a-element" href='<c:url value="/"/>'
//         >MediCare</a
//         >
//     </div>
//     <div class="container w-100 justify-content-end">
//         <c:if test="${user.get().verified}">
//             <a href="<c:url value="/home"/> " class="header-a-element nav-link mx-3"
//             ><spring:message code="MyAppointments"/></a
//             >
//         </c:if>
//         <c:if test="${user.get().verified && staffs == null}">
//             <a href="<c:url value="/mediclist/1"/>" class="header-a-element nav-link mx-3"
//             ><spring:message code="SearchMedics"/></a
//             >
//         </c:if>

//         <div class="d-inline-flex flex-column align-items-end">
//             <c:choose>
//                 <c:when test="${staffs == null}">
//                     <a href="<c:url value="/patient/profile"/>">
//                         <p style="font-weight: 400;" class="m-0 p-0 text-muted white-text"><c:out
//                                 value="${user.get().firstName} ${user.get().surname}"/></p>
//                     </a>
//                 </c:when>
//                 <c:otherwise>
//                     <a href="<c:url value="/staff/profile"/>">
//                         <p style="font-weight: 400;" class="m-0 p-0 text-muted white-text"><c:out
//                                 value="${user.get().firstName} ${user.get().surname}"/></p>
//                     </a>
//                 </c:otherwise>
//             </c:choose>

//             <a href="<c:url value="/logout"/>" class="m-0 p-0 header-a-element"><small class="m-0 p-0"><spring:message
//                     code="Logout"/></small></a>
//         </div>
//         <c:choose>
//             <c:when test="${!user.get().verified}">
//                 <img id="navbarUnverifiedUserImage" class="ml-2"
//                      src="https://fonts.gstatic.com/s/i/materialicons/account_circle/v4/24px.svg" alt="">
//             </c:when>
//             <c:when test="${staffs == null}">
//                 <a href="<c:url value="/patient/profile"/>">
//                         <%-- TODO:move style to css --%>
//                     <div style="width: 2em;" class="d-flex flex-column justify-content-center">
//                         <div class="profile-picture-container">
//                             <div style="margin-top: 100%;"></div>
//                             <img
//                                     id="navbarPatientUserImage"
//                                     class="profile-picture rounded-circle"
//                                     src="<c:url value="/profilePics/${user.get().profilePicture.id}"/>"
//                                     alt=""
//                             />
//                         </div>
//                     </div>
//                 </a>
//             </c:when>
//             <c:otherwise>
//                 <a href="<c:url value="/staff/profile"/>">
//                         <%-- TODO:move style to css --%>
//                     <div style="width: 2em;" class="d-flex flex-column justify-content-center">
//                         <div class="profile-picture-container">
//                             <div style="margin-top: 100%;"></div>
//                             <img
//                                     id="navbarStaffUserImage"
//                                     class="profile-picture rounded-circle"
//                                     src="<c:url value="/profilePics/${user.get().profilePicture.id}"/>"
//                                     alt=""
//                             />
//                         </div>
//                     </div>
//                 </a>
//             </c:otherwise>
//         </c:choose>
//     </div>
// </nav>
<template>
    
</template>
<script>
export default {
    name:"NavbarLogged"
}
</script>