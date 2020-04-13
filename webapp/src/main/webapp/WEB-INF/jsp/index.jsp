<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<html>
    <head>
        <%@ include file = "head.jsp" %>
    </head>
    <body>
        <%@ include file = "header.jsp" %>
        <%@ include file = "navbar.jsp" %>
        <br>
        <h2>Hello ${greeting}!</h2>
        <br>
        <div style="overflow-x:auto;">
            <table>
                <tr>
                    <th>Nombre</th>
                    <th>Especialidad</th>
                    <th>Correo electrónico</th>
                    <th>Teléfono</th>
                </tr>
                <tr>
                    <td>Lorem ipsum</td>
                    <td>Lorem ipsum</td>
                    <td>Lorem ipsum</td>
                    <td>Lorem ipsum</td>
                </tr>
                <tr>
                    <td>Lorem ipsum</td>
                    <td>Lorem ipsum</td>
                    <td>Lorem ipsum</td>
                    <td>Lorem ipsum</td>
                </tr>
                <tr>
                    <td>Lorem ipsum</td>
                    <td>Lorem ipsum</td>
                    <td>Lorem ipsum</td>
                    <td>Lorem ipsum</td>
                </tr>
            </table>
        </div>
    </body>
</html>