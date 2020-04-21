let toggler = document.getElementById("toggle-visibility");
let eyes = toggler.getElementsByTagName("img");
let password = document.getElementById("password");
toggler.onclick = function() {
  if(password.type == "password"){
    password.type = "text";
    eyes[0].style.display = "none";
    eyes[1].style.display = "inline";
  }else{
    password.type = "password";
    eyes[0].style.display = "inline";
    eyes[1].style.display = "none";
  }
}
