//auxiliar functions
function getParams() {
    let params = location.search.substring(1).split("&");
    let paramMap = {};
    for (let i = 0; i < params.length; i++) {
        if (params !== "") {
            let p = params[i].split("=");
            if (p.length === 2) {
                paramMap[p[0]] = p[1];
            }
        }
    }
    return paramMap;
}

function changeWeek(i) {
    let url = $(location).attr("origin") + $(location).attr("pathname") + "?";
    let params = getParams();
    let weekAdded = false;
    for (let k in params) {
        if (k !== "week") {
            url += k + "=" + params[k] + "&";
        } else {
            let week = parseInt(params[k]);
            url += k + "=" + (isNaN(week) ? i : week + i) + "&";
            weekAdded = true;
        }
    }
    if (!weekAdded) {
        url += "week=" + i + "&";
    }
    url = url.substring(0, url.length - 1);
    location.replace(url);
}

//change week buttons
document.getElementById("nextWeekBtn").onclick = function () {
    changeWeek(1);
};
document.getElementById("prevWeekBtn").onclick = function () {
    changeWeek(-1);
};

//change today auxiliar functions
function changeToday(today) {
    let url = location.origin + location.pathname + "?";
    let params = getParams();
    let todayAdded = false;
    for (let k in params) {
        if (k !== "today") {
            url += k + "=" + params[k] + "&";
        } else {
            url += k + "=" + today + "&";
            todayAdded = true;
        }
    }
    if (!todayAdded) {
        url += "today=" + today + "&";
    }
    url = url.substring(0, url.length - 1);
    location.replace(url);
}

for (let s of document.querySelectorAll(".medicare-day-span")) {
    s.onclick = function () {
        let newToday = s.getAttribute("data-day");
        changeToday(newToday);
    }
    //TODO: use classes for better practice
    s.style.cursor = "pointer";
}

for (let cb of document.querySelectorAll(".cancelAppointmentBtn")) {
    cb.onclick = function (e) {
        e.preventDefault();
        cancelAppointment(cb.href);
    }
}