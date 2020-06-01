var AppointmentList = function () {
    let page;

    let bindElements = function () {
        let buttons = $('.available-appointments-button');
        buttons.each(function () {
            let $this = $(this);
            $this.click(function () {
                AppointmentRequest.init($this.data('id'));
            });
        });

        $("#prevButton").click(function () {
            App.goto('/mediclist/' + (page - 1), true);
        });
        $("#nextButton").click(function () {
            App.goto('/mediclist/' + (page + 1), true);
        });
    };

    return {
        init: function (_page) {
            page = _page;
            bindElements();
        }
    }
}();
