<%@taglib prefix="layout" uri="http://callidora.lk/jsp/template-inheritance" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    request.setAttribute("page", request.getParameter("page"));
%>

<layout:extends name="base">

    <layout:put block="content">
        <div class="container">
            <div class="row">
                <div class="col-12 text-center mt-3">
                    <h1 class="text-info">Smart Urban Traffic Management System</h1>
                </div>
                <div class="col-6 offset-3 mt-3 ">
                    <label class="mb-2" for="dateInput">Select Date to Analyze:</label>
                    <input id="dateInput" type="date" class="form-control"/>
                </div>

                <div class="col-12 mt-5">
                    <table class="table table-striped">
                        <thead>
                        <tr>
                            <th scope="col">#</th>
                            <th scope="col">Traffic Light Status</th>
                            <th scope="col">Vehicle Speed</th>
                            <th scope="col">GPS Coordinates (Lat, Long, Location)</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:if test="${it != null}">
                            <jsp:useBean id="it" type="java.util.HashMap<java.lang.String, java.lang.Object>"
                                         scope="request"/>

                            <c:forEach items='${it.get("deviceDataList")}' var="data">
                                <tr>
                                    <th scope="row">${data.id}</th>
                                    <td>${data.trafficLightStatus}</td>
                                    <td>${data.vehicleSpeed}</td>
                                    <td>${data.gpsCoordinates}</td>
                                </tr>
                            </c:forEach>

                        </c:if>
                        </tbody>
                    </table>
                    <nav class="d-flex justify-content-center">
                        <ul class="pagination">
                            <c:if test='${(it.get("page") - 1) >= 1}'>
                                <li class="page-item">
                                    <a class="page-link"
                                       href="${BASE_URL}/analysis-report?page=${(it.get("page")-1)}&date=${it.get("date")}"
                                       aria-label="Previous">
                                        <span aria-hidden="true">&laquo;</span>
                                    </a>
                                </li>
                            </c:if>

<%--                            <c:forEach begin="1" end='${it.get("count")}' var="num">--%>
<%--                                <li class="page-item">--%>
<%--                                    <a class="page-link"--%>
<%--                                       href="${BASE_URL}/analysis-report?page=${num}&date=${it.get("date")}">${num}</a>--%>
<%--                                </li>--%>
<%--                            </c:forEach>--%>

                            <c:choose>
                                <c:when test="${it.count <= 10}">
                                    <c:forEach begin="1" end="${it.count}" var="num">
                                        <li class="page-item ${num == it.page ? 'active' : ''}">
                                            <a class="page-link" href="${BASE_URL}/analysis-report?page=${num}&date=${it.date}">${num}</a>
                                        </li>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="start" value="${Math.max(1, it.page - 5)}"/>
                                    <c:set var="end" value="${Math.min(Integer.parseInt(String.valueOf(it.page + 5)), Integer.parseInt(String.valueOf(it.count)))}"/>
                                    <c:forEach begin="${start}" end="${end}" var="num">
                                        <li class="page-item ${num == it.page ? 'active' : ''}">
                                            <a class="page-link" href="${BASE_URL}/analysis-report?page=${num}&date=${it.date}">${num}</a>
                                        </li>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>

                            <c:if test='${((it.get("page")+1) <= it.get("count"))}'>
                                <li>
                                    <a class="page-link" href="${BASE_URL}/analysis-report?page=${(it.get("page")+1)}&date=${it.get("date")}" aria-label="Next">
                                        <span aria-hidden="true">&raquo;</span>
                                    </a>
                                </li>
                            </c:if>


                        </ul>
                    </nav>
                </div>
            </div>
        </div>
    </layout:put>

</layout:extends>
