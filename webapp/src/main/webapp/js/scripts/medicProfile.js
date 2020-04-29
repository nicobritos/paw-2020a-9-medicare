const Profile = function () {
  let bindElements = function () {
    let togglers = document.getElementsByClassName("toggle-readonly");
    for (let t of togglers) {
      let input = document.getElementById(t.htmlFor);
      t.onclick = function() {
        input.readOnly = false;
      }
    }
  };

  return {
    init: function () {
      bindElements();
    }
  }
}();
