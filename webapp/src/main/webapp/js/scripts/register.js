let togglers = document.getElementsByClassName("toggle-visibility");
for (let t of togglers) {
  let eyes = t.getElementsByTagName("img");
  let password = document.getElementById(t.htmlFor);
  t.onclick = function() {
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
}
