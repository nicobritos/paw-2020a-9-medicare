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

        let profilePictureInput = $('#profile-picture-input');
        $('.picture-overlay i').click(function() {
            profilePictureInput.trigger('click');
        });
        profilePictureInput.change(function(e){
            let file = e.target.files[0];
            let formData = new FormData();
            formData.append("pic",file);
            if(file.type!=="image/jpeg"){
                App.showError();
                return;
            }
            fetch($("#baseUrl").attr("href") + "profilePics/set",{
                    method:"POST",
                    body:formData
            }).then((r)=>{
                if(r.statusCode === 200){
                    //TODO:show better message
                    App.showOk();
                }else{
                    App.showError();
                }
            }).catch((e)=>{
                App.showError();
            });
        })
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

