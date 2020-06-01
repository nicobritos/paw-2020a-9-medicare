const AppointmentRequest = function () {
    let staffId = null;

    let getStaffTimeslots = function () {
        return App.post('/timeslots/' + staffId, {
            fromDay: 3,
            fromMonth: 5,
            fromYear: 2020,
            toDay: 8,
            toMonth: 5,
            toYear: 2020
        });
    };

    let requestAppointment = function (year, month, day, hour, minute) {
        App.post('/patient/appointment', {
            staffId: staffId,
            year: year,
            month: month,
            day: day,
            minute: minute,
            hour: hour
        }).then(() => {
            App.showOk(['Su turno fue agendado exitosamente'], 'Agendado');
        });
    };

    let bindElements = function () {
        let modal = Modal.create(
            'Turnos', // TODO I18N
            $($('#appointment-select-modal').html())
        );
        modal.modal('show');

        getStaffTimeslots().then(timeslots => {
            let weekContainer = modal.find('#week-container');
            weekContainer.empty();

            for (let dateTimeslot of timeslots) {
                let date = dateTimeslot.date;
                let dayContainer = $($('#appointment-select-modal-day').html().format(
                    date.year + '/' + date.month + '/' + date.day
                ));
                weekContainer.append(dayContainer);

                let buttonContainer = dayContainer.find('#button-container');
                for (let timeslot of dateTimeslot.timeslots) {
                    let button = $(
                        '<button ' +
                        'type="button" ' +
                        'class="btn btn-info m-1" ' +
                        'data-hour="' + timeslot.hour + '" ' +
                        'data-minute="' + timeslot.minute + '" ' +
                        'data-year="' + date.year + '" ' +
                        'data-month="' + date.month + '" ' +
                        'data-day="' + date.day + '" ' +
                        '>' +
                        timeslot.hour + ':' + timeslot.minute +
                        '</button>'
                    );
                    button.click(function () {
                        let $this = $(this);
                        requestAppointment(
                            $this.data('year'),
                            $this.data('month'),
                            $this.data('day'),
                            $this.data('hour'),
                            $this.data('minute')
                        )
                    });
                    buttonContainer.append(button);
                }
            }
        });
    };

    return {
        init: function (_staffId) {
            staffId = _staffId;
            bindElements();
        }
    }
}();
