var ws = null;

function connect() {
    ws = new WebSocket("ws://localhost:8080/sutms/device-data");
    ws.onopen = function () {
        console.log("Connected");
    };
    ws.onmessage = function (evt) {
        console.log(evt.data);
        updateUI(JSON.parse(evt.data));
    };
    ws.onclose = function () {
        console.log("Closed");
    };
}

function send() {
    if (ws != null) {
        let value = document.getElementById("txt").value;
        ws.send(value);
    }
}

function updateUI(data) {
    let tlStatus = data.trafficLightStatus;
    let location = data.gpsCoordinates.location;
    let speed = data.vehicleSpeed;

    let trafficLight;

    switch (location) {
        case "KANDY":
            trafficLight = document.getElementById("tlKandy");
            break;
        case "KATUGASTOTA":
            trafficLight = document.getElementById("tlKatugastota");
            break;
        case "PERADENIYA":
            trafficLight = document.getElementById("tlPeradeniya");
            break;
        case "PILIMATHALAWA":
            trafficLight = document.getElementById("tlPilimathalawa");
            break;
    }

    let lights = trafficLight.querySelectorAll("div");

    switch (tlStatus) {
        case "RED":
            changeColor(lights, 0)
            break;
        case "YELLOW":
            changeColor(lights, 1)
            calculateAvgSpeed(location, speed);
            break;
        case "GREEN":
            changeColor(lights, 2)
            calculateAvgSpeed(location, speed);
            break;
    }

}

function changeColor(lights, number) {
    lights[number].classList.remove("light-down");
    lights[number].classList.add("light-up");

    switch (number) {
        case 0:
            lights[1].classList.remove("light-up");
            lights[1].classList.add("light-down");
            lights[2].classList.remove("light-up");
            lights[2].classList.add("light-down");
            break;
        case 1:
            lights[0].classList.remove("light-up");
            lights[0].classList.add("light-down");
            lights[2].classList.remove("light-up");
            lights[2].classList.add("light-down");
            break;
        case 2:
            lights[0].classList.remove("light-up");
            lights[0].classList.add("light-down");
            lights[1].classList.remove("light-up");
            lights[1].classList.add("light-down");
            break;
    }

}

function calculateAvgSpeed(location, speed) {
    let avg;

    switch (location) {
        case "KANDY":
            avg = document.getElementById("avgKandy");
            break;
        case "KATUGASTOTA":
            avg = document.getElementById("avgKatugastota");
            break;
        case "PERADENIYA":
            avg = document.getElementById("avgPeradeniya");
            break;
        case "PILIMATHALAWA":
            avg = document.getElementById("avgPilimathalawa");
            break;
    }

    let req = new XMLHttpRequest();
    req.open("GET", "http://localhost:8080/sutms/vehicle-data/speed/" + location + "/" + speed, true);
    req.onreadystatechange = () => {
        if (req.readyState == 4 && req.status == 200) {
            let resp = JSON.parse(req.responseText);
            avg.innerHTML = resp.avgSpeed + " km/h";
        }
    };
    req.send();

}