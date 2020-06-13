const AppointmentRequest = function () {
    let appointmentRequestForm = $('#appointment-request-form');
    let appointmentRequestButton = $('#appointment-request-button');

    let bindElements = function () {
        appointmentRequestButton.click(function () {
            Modal.confirm({
                title: strings['title'],
                body: strings['body'],
                callbacks: {
                    confirm: function () {
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
