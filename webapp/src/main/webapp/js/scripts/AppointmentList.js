var AppointmentList = function () {
    let bindElements = function () {
        let buttons = $('.available-appointments-button');
        buttons.each(function () {
            let $this = $(this);
            $this.click(function () {
                AppointmentRequest.init($this.data('id'));
            });
        });
    };

    return {
        init: bindElements
    }
}();
