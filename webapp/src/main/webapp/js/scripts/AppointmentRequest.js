var AppointmentRequest = function() {
    let staffId = null;

    let bindElements = function () {
        let modal = Modal.create(
            'Turnos', // TODO I18N
            $('#appointment-select-modal').text()
        );
        modal.modal('show');
    };

    let getStaffTimeslots = function () {
        return App.get('/timeslots/' + staffId, {
            from_day: 29,
            from_month: 4,
            from_year: 2020,
            to_day: 1,
            to_month: 5,
            to_year: 2020
        });
    };

    return {
        init: function (_staffId) {
            staffId = _staffId;
            getStaffTimeslots().then(timeslots => {
                for (let timeslot of timeslots) {
                    console.log(timeslot);
                }
                bindElements();
            });
        }
    }
}();
