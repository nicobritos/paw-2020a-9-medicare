const AppointmentList = function () {
    let page;

    let refreshPage = function () {
        App.goto('/mediclist/' + page, true);
    };

    let bindElements = function () {
        let buttons = $('.available-appointments-button');
        buttons.each(function () {
            let $this = $(this);
            $this.click(function () {
                AppointmentRequest.init($this.data('id'));
            });
        });

        $("#prevButton").click(function () {
            page -= 1;
            refreshPage();
        });
        $("#nextButton").click(function () {
            page += 1;
            refreshPage();
        });
    };

    return {
        init: function (_page) {
            page = _page;
            bindElements();
        }
    }
}();
