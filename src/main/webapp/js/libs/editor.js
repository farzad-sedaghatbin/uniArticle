function updateStuff(event) {
  document.getElementById('posOut').innerHTML = event.worldX.toFixed(4) + ", " +
    event.worldY.toFixed(4) + ", " +
    event.worldZ.toFixed(4) + " | " +
    event.normalX.toFixed(4) + ", " +
    event.normalY.toFixed(4) + ", " +
    event.normalZ.toFixed(4);
  var l = document.getElementById('cpnt');
  var t = document.getElementById('bar');
  var norm = new x3dom.fields.SFVec3f(event.normalX, event.normalY, event.normalZ);
  l.setAttribute('point', event.worldX + ' ' + event.worldY + ' ' + event.worldZ + ', ' +
    (event.worldX + 2.5 * norm.x) + ' ' + (event.worldY + 2.5 * norm.y) + ' ' + (event.worldZ + 2.5 * norm.z));
  var qDir = x3dom.fields.Quaternion.rotateFromTo(new x3dom.fields.SFVec3f(0, 1, 0),
    norm);
  //new x3dom.fields.SFVec3f(0, 0, -1));
  var rot = qDir.toAxisAngle();
  //x3dom.debug.logWarning(rot);  
  t.setAttribute('rotation', rot[0].x + ' ' + rot[0].y + ' ' + rot[0].z + ' ' + rot[1]);
  //t.setAttribute('translation', (event.worldX+norm.x)+' '+(event.worldY+norm.y)+' '+(event.worldZ+norm.z));
  t.setAttribute('translation', event.worldX + ' ' + event.worldY + ' ' + event.worldZ);
}
var funcPtr = function (event) {
  document.getElementById('ausgabe').innerHTML = event.type + " (" +
    event.button + "): " +
    event.worldX.toFixed(2) + ", " +
    event.worldY.toFixed(2) + ", " +
    event.worldZ.toFixed(2);
  document.getElementById('blubbMat').setAttribute('diffuseColor', '1 0.5 0');
};
var funcPtr2 = function (event) {
  document.getElementById('ausgabe').innerHTML = event.hitObject._x3domNode._DEF;
};

function delIt() {
  document.getElementById('blubbMat').setAttribute('diffuseColor', '1 1 0');
  document.getElementById('blubb').removeEventListener('click', funcPtr, false);
};

function start(event) {
  if (rubberBandTest && !drag) {
    drag = true;
    startPosX = event.pageX;
    startPosY = event.pageY;
    res.pos1 = runtime.mousePosition(event);
    rb.style.visibility = "visible";
    rb.style.left = startPosX + "px";
    rb.style.top = startPosY + "px";
    rb.style.width = "1px";
    rb.style.height = "1px";
  }
}
//
// ACHTUNG: Rubber-Band Darstellung ist nur ein HACK und lässt sich 
// derzeit nur von *links oben* nach *rechts unten* aufziehen!!!
//
function move(event) {
  var pos = runtime.mousePosition(event);
  var info = runtime.shootRay(pos[0], pos[1]);
  if (info.pickObject) {
    x3dom.debug.logInfo("pos: " + info.pickPosition + ", norm: " + info.pickNormal +
      ", object: " + info.pickObject.getAttribute("DEF"));
  }
  if (drag) {
    rb.style.left = startPosX + "px";
    rb.style.top = startPosY + "px";
    rb.style.width = Math.abs(event.pageX - startPosX) + "px";
    rb.style.height = Math.abs(event.pageY - startPosY) + "px";
  }
}

function stop(event) {
  if (drag) {
    drag = false;
    res.pos2 = runtime.mousePosition(event);
    rb.style.visibility = "hidden";
    var objs = runtime.pickRect(res.pos1[0], res.pos1[1], res.pos2[0], res.pos2[1]);
    var str = "";
    for (var i = 0; i < objs.length; i++) {
      str += objs[i].getAttribute('DEF') + ", ";
    }
    x3dom.debug.logInfo(str);
  }
}

function addIt() {
  //document.getElementById('ausgabe').innerHTML = "test";
  //document.getElementById('blubb').addEventListener('click', funcPtr, false);
  document.getElementById('kloppe').addEventListener('click', funcPtr2, false);
  document.getElementById('aView').addEventListener('viewpointChanged', viewFunc, false);
  runtime = document.getElementById("boxes").runtime;
  rb = document.getElementById('rubberBand');
}

function setMode() {
  rubberBandTest = !rubberBandTest;
  if (rubberBandTest)
    runtime.noNav();
  else
    runtime.examine();
}
var pos = null,
  rot = null,
  mat = null;
var viewFunc = function (evt) {
  pos = evt.position;
  rot = evt.orientation;
  mat = evt.matrix;
  document.getElementById('anotherView').setAttribute(
    'position', pos.x + ' ' + pos.y + ' ' + pos.z);
  document.getElementById('anotherView').setAttribute(
    'orientation', rot[0].x + ' ' + rot[0].y + ' ' + rot[0].z + ' ' + rot[1]);
  x3dom.debug.logInfo('position: ' + pos.x + ' ' + pos.y + ' ' + pos.z + '\n' +
    'orientation: ' + rot[0].x + ' ' + rot[0].y + ' ' + rot[0].z + ' ' + rot[1]);
};
var setView = function (evt) {
  var e = document.getElementById("boxes");
  e.runtime.resetExamin();
  //e.runtime.canvas.doc._viewarea._scene.getViewpoint().setView(mat);
  //return;
  document.getElementById('aView').setAttribute(
    'position', pos.x + ' ' + pos.y + ' ' + pos.z);
  document.getElementById('aView').setAttribute(
    'orientation', rot[0].x + ' ' + rot[0].y + ' ' + rot[0].z + ' ' + rot[1]);
};
var res = new Object();
var rubberBandTest = false;
var runtime = null;
var startPosX, startPosY;
var rb = null;
var drag = false;
document.onload = addIt;