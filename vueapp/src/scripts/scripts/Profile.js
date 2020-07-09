const Profile = function () {
    let bindElements = function () {
        let togglers = document.getElementsByClassName("toggle-readonly");
        for (let t of togglers) {
            let input = document.getElementById(t.htmlFor);
            t.onclick = function () {
                input.readOnly = false;
                if (t.htmlFor === 'password') {
                    let repeat = document.getElementById('repeatPassword');
                    repeat.readOnly = false;
                    toggleVisibility();
                }
            }
        }

        //get profile input
        let profilePictureInput = $('#profile-picture-input');
        $('.picture-overlay i').click(function () {
            profilePictureInput.trigger('click');
        });
        //append to onchange event
        profilePictureInput.change(function (e) {
            //get profile pic file and check type
            let file = e.target.files[0];
            if (!file.type.includes("image")) {
                App.showError(error_message['error']);
                return;
            }
            //append it to form
            let formData = new FormData();
            formData.append("pic", file);
            //post to baseurl/profilePics/set
            fetch($("#baseUrl").attr("href") + "profilePics/set", {
                method: "POST",
                body: formData
            }).then((r) => {
                if (r.ok) {
                    //TODO:show better message
                    App.showOk();
                    location.reload();
                } else {
                    //TODO:show better message
                    App.showError(error_message['error']);
                }
            }).catch((e) => {
                //TODO:show better message
                App.showError(error_message['error']);
            });
        })

        $( ".cancel-workday-form" ).each( function( index, element ){
            $(element).children('.cancel-workday-btn').click(function () {
                var check = false;
                Swal.fire({
                    title: workday_strings['title'],
                    text: workday_strings['body'],
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33',
                    confirmButtonText: workday_strings['accept'],
                    cancelButtonText: workday_strings['cancel'],
                    onAfterClose: () => {
                        if(check) {
                            Swal.fire({
                                title: workday_strings['title2'],
                                text: workday_strings['body2'] + ' ' + element.dataset.appts,
                                icon: 'warning',
                                showCancelButton: true,
                                confirmButtonColor: '#3085d6',
                                cancelButtonColor: '#d33',
                                confirmButtonText: workday_strings['accept2'],
                                cancelButtonText: workday_strings['cancel2'],
                            }).then((result) => {
                                if (result.value) {
                                    $.post(element.dataset.appointment_url).done(function (){
                                        Swal.fire(
                                            workday_strings['deleted'],
                                            workday_strings['deleted_body'],
                                            'success'
                                        )
                                        location.reload();
                                    });
                                } else {
                                    element.submit();
                                    Swal.fire(
                                        workday_strings['deleted'],
                                        workday_strings['deleted_body'],
                                        'success'
                                    )
                                }
                            })
                        }
                    }
                }).then((result) => {
                    if (result.value) {
                        check = true;
                    }
                })
            });
        })

        $( ".cancel-specialty-form" ).each( function( index, element ){
            $(element).children('.cancel-specialty-btn').click(function () {
                Swal.fire({
                    title: specialty_strings['title'],
                    text: specialty_strings['body'],
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33',
                    confirmButtonText: specialty_strings['accept'],
                    cancelButtonText: specialty_strings['cancel'],
                }).then((result) => {
                    if (result.value) {
                        element.submit();
                        Swal.fire(
                            specialty_strings['deleted'],
                            specialty_strings['deleted_body'],
                            'success'
                        )
                    }
                })
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

    return {
        init: function () {
            bindElements();
        }
    }
}();

