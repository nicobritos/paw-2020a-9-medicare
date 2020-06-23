const PatientHome = function () {
    let bindElements = function () {
        $( ".cancel-appt-form" ).each( function( index, element ){
            $(element).children('.cancel-appt-btn').click(function () {
                Modal.confirm({
                    title: strings['title'],
                    body: strings['body'],
                    callbacks: {
                        confirm: function () {
                            element.submit();
                        }
                    }
                });
            });
        })
    }

    return {
        init: bindElements
    }
}();
