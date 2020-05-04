function cancelAppointment(url,appointmentElement) {
    fetch(url,{
        method:"DELETE"
    }).then(function (r) {
        if (r.ok) {
            appointmentElement.parentNode.removeChild(appointmentElement);
        } else if (r.status === 500) {
            App.showError();
        }else if(r.status === 400) {
            App.showError();
        }else{
            return Promise.reject();
        }
    }).catch(function () {
        location.reload();
    })
}
for(let cb of document.querySelectorAll(".cancelAppointmentBtn")){
    cb.onclick = function (e) {
        e.preventDefault();
        cancelAppointment(cb.href,cb.closest("li"));
    }
}