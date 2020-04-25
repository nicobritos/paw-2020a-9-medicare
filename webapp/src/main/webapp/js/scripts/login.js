const Login = function () {
    let bindElements = function () {
        let toggler = $("#toggle-visibility");
        let eyes = toggler.find("img");
        let password = $("#password");
        toggler.click(() => {
            if (password.type === "password") {
                password.type = "text";
                eyes[0].style.display = "none";
                eyes[1].style.display = "inline";
            } else {
                password.type = "password";
                eyes[0].style.display = "inline";
                eyes[1].style.display = "none";
            }
        });

        // $('#login-confirm').click((e) => {
        //     e.preventDefault();
        //     e.stopPropagation();
        //
        //     // let parameter = 'email='
        //     App.post('/login', {
        //         email: $('#email').val(),
        //         password: password.val()
        //     });
        // });
    };

    return {
        init: function () {
            bindElements();
        }
    }
}();
