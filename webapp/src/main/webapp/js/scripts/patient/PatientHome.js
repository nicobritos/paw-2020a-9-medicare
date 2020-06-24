const PatientHome = function () {
    let bindElements = function () {
        $( ".cancel-appt-form" ).each( function( index, element ){
            $(element).children('.cancel-appt-btn').click(function () {
                Swal.fire({
                    title: strings['title'],
                    text: strings['body'],
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33',
                    confirmButtonText: strings['accept'],
                    cancelButtonText: strings['cancel'],
                }).then((result) => {
                    if (result.value) {
                        element.submit();
                        Swal.fire(
                            strings['deleted'],
                            strings['deleted_body'],
                            'success'
                        )
                    } else {
                        element.submit();
                    }
                })
            });
        })
    }

    return {
        init: bindElements
    }
}();
