const Profile = function () {
    let bindElements = function () {
        let togglers = document.getElementsByClassName("toggle-readonly");
        for (let t of togglers) {
            let input = document.getElementById(t.htmlFor);
            t.onclick = function() {
                input.readOnly = false;
                if(t.htmlFor === 'password'){
                    let repeat = document.getElementById('repeatPassword');
                    repeat.readOnly = false;
                    toggleVisibility();
                }
            }
        }

        $('#confirm-account-button').click(function () {
            App.post('/patient/profile/confirm').then(value => {
                if (value) {
                    App.showOk("Mail reenviado exitosamente. Chequee su casilla de mail o spam"); // TODO i18n
                } else {
                    App.showError("Su cuenta ya se encuentra verificada"); // TODO i18n
                }
            })
        });
    };

    let toggleVisibility = function () {
        let repeatPasswordContainer = document.getElementById("repeat-password-container");
        repeatPasswordContainer.style.display = "inline"
        document.getElementsByTagName("img").item(0).style.display = "inline";
        let togglers = document.getElementsByClassName("toggle-visibility");
        for (let t of togglers) {
            let eyes = t.getElementsByTagName("img");
            let password = document.getElementById(t.htmlFor);
            t.onclick = function() {
                if(password.type === "password"){
                    password.type = "text";
                    eyes[0].style.display = "none";
                    eyes[1].style.display = "inline";
                }else{
                    password.type = "password";
                    eyes[0].style.display = "inline";
                    eyes[1].style.display = "none";
                }
            }
        }
    };

    return {
        init: function () {
            bindElements();
        }
    }
}();

