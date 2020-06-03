const AppointmentRequest = function () {
    let appointmentRequestForm = $('#appointment-request-form');
    let appointmentRequestButton = $('#appointment-request-button');

    let bindElements = function () {
        appointmentRequestButton.click(function () {
            Modal.confirm({ // TODO: i18n
                title: 'Estas por pedir un turno',
                body: 'Estas seguro que quieres hacerlo?',
                callbacks: {
                    success: function () {
                        appointmentRequestForm.submit();
                    }
                }
            });
        });
    };

    return {
        init: function () {
            bindElements();
        }
    }
}();
