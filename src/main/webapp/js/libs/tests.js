// Tests loader
// adds menu as well as stylesheet to tests
// the only file to include in the HTML for tests is this one
var _init = function () {
  
  //	<nav><a href="index.html">back to test index</a></nav>
  // TODO set the config for the flash object
  // x3dom.config.XYZ = ../../src/swfobject/
};
// dom loaded shorthand http://www.kryogenix.org/days/2007/09/26/shortloaded
(function (i) {
  var u = navigator.userAgent;
  var e = /*@cc_on!@*/ false;
  var st =
    setTimeout;
  if (/webkit/i.test(u)) {
    st(function () {
      var dr = document.readyState;
      if (dr == "loaded" || dr == "complete") {
        i()
      } else {
        st(arguments.callee, 10);
      }
    }, 10);
  } else if ((/mozilla/i.test(u) && !/(compati)/.test(u)) || (/opera/i.test(u))) {
    document.addEventListener("DOMContentLoaded", i, false);
  } else if (e) {
    (
      function () {
        var t = document.createElement('doc:rdy');
        try {
          t.doScroll('left');
          i();
          t = null;
        } catch (e) {
          st(arguments.callee, 0);
        }
      })();
  } else {
    window.onload = i;
  }
})(_init);