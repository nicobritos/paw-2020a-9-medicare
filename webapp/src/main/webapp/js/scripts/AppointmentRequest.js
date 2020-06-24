const AppointmentRequest = function () {
    let appointmentRequestForm = $('#appointment-request-form');
    let appointmentRequestButton = $('#appointment-request-button');

    let bindElements = function () {
        appointmentRequestButton.click(function () {
            Swal.fire({
                title: strings['title'],
                text: strings['body'],
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                cancelButtonText: strings['cancel'],
                confirmButtonText: strings['accept'],
            }).then((result) => {
                if (result.value) {
                    appointmentRequestForm.submit();
                }
            })
        });
    };

    return {
        init: function () {
            bindElements();
        }
    }
}();
