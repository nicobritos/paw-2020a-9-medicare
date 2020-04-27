const Register = function () {
    let bindElements = function () {
        let togglers = document.getElementsByClassName("toggle-visibility");
        for (let t of togglers) {
            let eyes = t.getElementsByTagName("img");
            let password = document.getElementById(t.htmlFor);
            t.onclick = function () {
                if (password.type === "password") {
                    password.type = "text";
                    eyes[0].style.display = "none";
                    eyes[1].style.display = "inline";
                } else {
                    password.type = "password";
                    eyes[0].style.display = "inline";
                    eyes[1].style.display = "none";
                }
            }
        }
    };

    let bindStaffElements = function () {
        let togglers = document.getElementsByClassName("toggle-visibility");
        for (let t of togglers) {
            let eyes = t.getElementsByTagName("img");
            let password = document.getElementById(t.htmlFor);
            t.onclick = function () {
                if (password.type === "password") {
                    password.type = "text";
                    eyes[0].style.display = "none";
                    eyes[1].style.display = "inline";
                } else {
                    password.type = "password";
                    eyes[0].style.display = "inline";
                    eyes[1].style.display = "none";
                }
            }
        }
        let countrySelect = $('#country');
        let provinceSelect = $('#province');

        countrySelect.change(() => {
            emptyProvinces();
            getProvinces(countrySelect.val());
        });
        provinceSelect.change(() => {
            emptyLocalities();
            getLocalities(provinceSelect.val());
        });
        if (countrySelect.val() != null) {
            getProvinces(countrySelect.val());
        }
    };

    let getProvinces = function(countryId) {
        App.get('/signup/staff/provinces/' + countryId).then(provinces => {
            let provinceSelect = $('#province');
            if (provinces.length > 0) {
                for (let province of provinces) {
                    provinceSelect.append(
                        $("<option></option>").attr("value", province.id).text(province.name)
                    );
                }
                provinceSelect.show();
                $('#province-container').show();
                getLocalities(provinces[0].id);
            }
        });
    };

    let getLocalities = function(provinceId) {
        App.get('/signup/staff/localities/' + provinceId).then(localities => {
            let localitySelect = $('#locality');
            if (localities.length > 0) {
                for (let locality of localities) {
                    localitySelect.append(
                        $("<option></option>").attr("value", locality.id).text(locality.name)
                    );
                }
                localitySelect.show();
                $('#locality-container').show();
            }
        });
    };

    let emptyProvinces = function() {
        emptyLocalities();

        let provinceSelect = $('#province');
        provinceSelect.hide();
        provinceSelect.children('option').remove();
    };

    let emptyLocalities = function() {
        let localitySelect = $('#locality');
        localitySelect.hide();
        localitySelect.children('option').remove();
    };

    let bindChooserElements = function () {
        $('#signup-patient').click(() => {
            App.goto('/signup/patient');
        });
        $('#signup-staff').click(() => {
            App.goto('/signup/staff');
        });
    };

    return {
        initPatient: function () {
            bindElements();
        },
        initStaff: function () {
            bindStaffElements();
        },
        init: function () {
            bindChooserElements();
        }
    }
}();
