<%@taglib prefix="layout" uri="http://callidora.lk/jsp/template-inheritance" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<layout:extends name="base">

    <layout:put block="content">


        <div class="container">
            <div class="row">
                <div class="col-12 text-center mt-3">
                    <h1 class="text-info">Smart Urban Traffic Management System</h1>
                </div>

                <div class="col-6 offset-3 py-3 text-center">
                    <h3 class="text-warning">Traffic Pattern Analyzer</h3>
                </div>

                <div class="col-6 offset-3">
                    <c:if test="${it != null}">
                        <jsp:useBean id="it" type="java.util.Map<com.poojithairosha.core.model.TrafficLightLocation, java.util.Map<java.lang.Integer, java.lang.Integer>>"
                                     scope="request"/>

                        <c:forEach items='${it.keySet()}' var="location">
                            <h2 class="mb-2">${location}</h2>
                            <table class="table table-striped">
                                <thead>
                                <tr>
                                    <th scope="col">Hour</th>
                                    <th scope="col">Vehicle Count</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items='${it.get(location).keySet()}' var="hour">
                                    <tr>
                                        <th scope="row">${hour}.00 - ${hour + 1}.00</th>
                                        <td>${it.get(location).get(hour)}</td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                            <hr class="my-3">
                        </c:forEach>

                    </c:if>

                </div>

            </div>
        </div>

        <script src="${BASE_URL}/js/script.js"></script>
    </layout:put>

</layout:extends>
