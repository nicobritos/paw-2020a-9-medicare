const SelectAppointment = function () {
    let week = 0;
    let id = 0;

    let refreshPage = function () {
        App.goto('/appointment/' + id + '/' + week, true);
    };

    let bindElements = function () {
        $("#day-left").click(() => {
            week -= 1;
            refreshPage();
        });
        $("#day-right").click(() => {
            week += 1;
            refreshPage();
        });
    };

    return {
        init: function (_id, _week) {
            id = _id;
            week = _week;
            bindElements();
        }
    }
}();
