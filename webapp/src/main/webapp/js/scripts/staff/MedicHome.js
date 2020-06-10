const MedicHome = function () {
    let getParams = function () {
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
    };

    let changeWeek = function (i) {
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
    };

    let changeToday = function (today) {
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

    let bindElements = function () {
        $("#nextWeekBtn").click(function () {
            changeWeek(1);
        });
        $("#prevWeekBtn").click(function () {
            changeWeek(-1);
        });

        $(".medicare-day-span").each(function (s) {
            let $this = $(this);
            $this.click(function () {
                changeToday($this.data("day"));
            });
            $this.addClass('pointer');
        });

        $( ".cancel-appt-form" ).each( function( index, element ){
            $(element).children('.cancel-appt-btn').click(function () {
                Modal.confirm({
                    title: strings['title'],
                    body: strings['body'],
                    callbacks: {
                        confirm: function () {
                            element.submit();
                        }
                    }
                });
            });
        })
    }

    return {
        init: bindElements
    }
}();
