<%@taglib prefix="layout" uri="http://callidora.lk/jsp/template-inheritance" %>

<layout:extends name="base">

    <layout:put block="content" type="REPLACE">
        <div class="container vh-100">
            <div class="row my-5">
                <div class="col-12 ">
                    <h1 class="fw-bold text-center">Smart Urban Traffic Management System</h1>
                </div>
            </div>
            <div class="row my-5">
                <div class="col-12 col-md-6 d-flex align-items-center gap-3">
                    <div id="tlKandy" class="trafficlight">
                        <div class="red light-up"></div>
                        <div class="yellow"></div>
                        <div class="green"></div>
                    </div>
                    <div>
                        <h1 class="fw-bold text-lg-center">Kandy</h1>
                        <h5>Avg Speed: <span id="avgKandy" class="text-warning">0 Km/h</span></h5>
                    </div>
                </div>
                <div class="col-12 col-md-6  d-flex align-items-center gap-3">
                    <div id="tlKatugastota" class="trafficlight">
                        <div class="red light-up"></div>
                        <div class="yellow"></div>
                        <div class="green"1></div>
                    </div>
                    <div>
                        <h1 class="fw-bold text-lg-center">Katugastota</h1>
                        <h5>Avg Speed: <span id="avgKatugastota" class="text-warning">0 Km/h</span></h5>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-12 col-md-6  d-flex align-items-center gap-3">
                    <div id="tlPeradeniya" class="trafficlight">
                        <div class="red light-up"></div>
                        <div class="yellow"></div>
                        <div class="green"></div>
                    </div>
                    <div>
                        <h1 class="fw-bold text-lg-center">Peradeniya</h1>
                        <h5>Avg Speed: <span id="avgPeradeniya" class="text-warning">0 Km/h</span></h5>
                    </div>
                </div>
                <div class="col-12 col-md-6  d-flex align-items-center gap-3">
                    <div id="tlPilimathalawa" class="trafficlight">
                        <div class="red light-up"></div>
                        <div class="yellow"></div>
                        <div class="green"></div>
                    </div>
                    <div>
                        <h1 class="fw-bold text-lg-center">Pilimathalawa</h1>
                        <h5>Avg Speed: <span id="avgPilimathalawa" class="text-warning">0 Km/h</span></h5>
                    </div>
                </div>
            </div>
        </div>

        <script src="${BASE_URL}/js/script.js"></script>
        <script>
            connect();
        </script>
    </layout:put>

</layout:extends>