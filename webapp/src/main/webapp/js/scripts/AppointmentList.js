const AppointmentList = function () {
    let page;
    let final_page;

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

        $(".firstButton").click(function () {
            page = 1;
            refreshPage();
        })
        $(".prevButton").click(function () {
            page -= 1;
            refreshPage();
        });
        $(".nextButton").click(function () {
            page += 1;
            refreshPage();
        });
        $(".lastButton").click(function () {
            page = final_page;
            refreshPage();
        });
    };

    return {
        init: function (_page, _finalpage) {
            page = _page;
            final_page = _finalpage
            bindElements();
        }
    }
}();
