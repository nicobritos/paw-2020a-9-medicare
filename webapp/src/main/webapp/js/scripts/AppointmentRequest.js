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
        return App.post('/timeslots/' + staffId, {
            fromDay: 29,
            fromMonth: 4,
            fromYear: 2020,
            toDay: 1,
            toMonth: 5,
            toYear: 2020
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
