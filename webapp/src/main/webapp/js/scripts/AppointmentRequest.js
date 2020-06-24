const AppointmentRequest = function () {
    let appointmentRequestForm = $('#appointment-request-form');
    let appointmentRequestButton = $('#appointment-request-button');

    let bindElements = function () {
        appointmentRequestButton.click(function () {
            /*Modal.confirm({
                title: strings['title'],
                body: strings['body'],
                callbacks: {
                    confirm: function () {
                        appointmentRequestForm.submit();
                    }
                }
            });*/
            Swal.fire({
                title: strings['title'],
                text: strings['body'],
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: strings['yes']
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
