var AppointmentRequest = function() {
    let staffId = null;

    let bindElements = function () {
        getStaffTimeslots().then(timeslots => {
            let modal = Modal.create(
                'Turnos', // TODO I18N
                $($('#appointment-select-modal').text())
            );

            let weekContainer = modal.find('#week-container');
            weekContainer.empty();

            for (let dateTimeslot of timeslots) {
                let dayContainer = $($('#appointment-select-modal-day').text().format(
                    dateTimeslot.year + '/' + dateTimeslot.month + '/' + dateTimeslot.day
                ));
                weekContainer.append(dayContainer);

                let buttonContainer = dayContainer.find('#button-container');
                for (let timeslot of dateTimeslot.timeslots) {
                    let button = $(
                        '<button ' +
                        'type="button" ' +
                        'class="btn" ' +
                        'data-hour="' + timeslot.hour + '" ' +
                        'data-minute="' + timeslot.minute + '" ' +
                        'data-year="' + dateTimeslot.year + '" ' +
                        'data-month="' + dateTimeslot.month + '" ' +
                        'data-day="' + dateTimeslot.day + '" ' +
                        '>' +
                        '</button>'
                    );
                    button.click(function() {
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

            modal.modal('show');
        });
    };

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

    let requestAppointment = function(year, month, day, hour, minute) {
        console.log(year);
        console.log(month);
        console.log(day);
        console.log(hour);
        console.log(minute);
    };

    return {
        init: function (_staffId) {
            staffId = _staffId;
            bindElements();
        }
    }
}();
