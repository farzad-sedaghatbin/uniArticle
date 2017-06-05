//Copyright © 2015 SEPIDARSOFT | JS Writen BY SepidarSoft API Department V 4.0.3.100 Stable Beta 7.5.6
var arrSignatures = ["MSXML2.XMLHTTP.5.0", "MSXML2.XMLHTTP.4.0", "MSXML2.XMLHTTP.3.0", "MSXML2.XMLHTTP", "Microsoft.XMLHTTP"];

function getInternetExplorerVersion()
// Returns the version of Internet Explorer or a -1
// (indicating the use of another browser).
{
  var rv = -1; // Return value assumes failure.
  if (navigator.appName == 'Microsoft Internet Explorer') {
    var ua = navigator.userAgent;
    var re = new RegExp("MSIE ([0-9]{1,}[\.0-9]{0,})");
    if (re.exec(ua) != null)
      rv = parseFloat(RegExp.$1);
  };
  return rv;
};

function checkVersion() {
  var msg = true;
  var ver = getInternetExplorerVersion();
  if (ver > -1) {
    if (ver >= 8.0)
      msg = true;
    else
      msg = false;
  };
  return (msg);
};

function loadXMLPostDocf(url, method, posData, uploadProgress, uploadComplete, uploadFailed, uploadCanceled, statechange, CallBackOnSend) {
  // branch for native XMLHttpRequest object
  //console.log("Request:"+url+'--'+method);
  if (window.XMLHttpRequest && checkVersion()) {
    pos = new XMLHttpRequest();
    pos.open(method, url, true);
    //pos.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    if (statechange != '') {
      pos.onreadystatechange = statechange;
    };
    try {
      if (uploadProgress != '')
        if (pos.upload) {
          pos.upload.addEventListener("progress", uploadProgress, true);
        } else {
          pos.addEventListener("progress", uploadProgress, true);
        }
      if (uploadComplete != '') pos.addEventListener("load", uploadComplete, false);
      if (uploadFailed != '') pos.addEventListener("error", uploadFailed, false);
      if (uploadCanceled != '') pos.addEventListener("abort", uploadCanceled, false);
    } catch (e) {}
    pos.send(posData);
    if (CallBackOnSend) CallBackOnSend(pos);
    //console.log(pos.abort());
    // branch for IE/Windows ActiveX version
  } else if (window.ActiveXObject) {
    for (var i = 0; i < arrSignatures.length; i++) {
      try {
        pos = new ActiveXObject(arrSignatures[i]);
      } catch (oError) {
        //ignore
      };
    };
    if (pos) {
      pos.open(method, url, false);
      //pos.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
      if (!statechange) {
        pos.onreadystatechange = statechange;
      };
      if (!uploadComplete) {
        pos.onreadystatechange = uploadComplete;
      };
      // if(uploadProgress!='')pos.upload.addEventListener("progress", uploadProgress, true);
      //if(uploadComplete!='')pos.addEventListener("load", uploadComplete, false);
      //if(uploadFailed!='')pos.addEventListener("error", uploadFailed, false);
      //if(uploadCanceled!='')pos.addEventListener("abort", uploadCanceled, false);
      pos.send(posData);
      if (CallBackOnSend) CallBackOnSend(pos);
    };
  };
  return (pos);
};
var responseText = function (e, doing) {
  if (e.readyState == 4) {
    if (doing) doing(e);
    return (e.responseText);
  };
  if (e.readyState == 200) {
    if (doing) doing(e);
    return (e.responseText);
  };
  return false;
};

function formdata() {
  this.init = function () {
    var Res = new Object();
    if (window.FormData != 'undefined' && window.FormData) {
      Res.data = new FormData();
    } else {
      var iframe = document.createElement("iframe");
      iframe.height = '0px';
      iframe.width = '0px';
      iframe.style.display = 'none';
      iframe.id = "";
      document.body.insertBefore(iframe, document.body.firstChild);
      if (iframe.contentDocument) {
        if (iframe.contentDocument.body == null) {
          Res.data = document.createElement("form");
          iframe.contentDocument.body = Res.data;
        }
        $(iframe.contentDocument)
          .find('body')
          .each(function (index, element) {
            Res.data = document.createElement("form");
            element.appendChild(Res.data);
          });
      } else if (iframe.contentWindow) { // IE
        $(iframe.contentWindow.document)
          .find('body')
          .each(function (index, element) {
            Res.data = document.createElement("form");
            element.appendChild(Res.data);
          });
      };
    };
    //
    return (Res.data);
  };
  this.appendEle = function (handel, element) {
    if (!handel) {
      return false;
    };
    var value = new Array();
    var Res = new Object();
    Res.File = false;
    Res.handel = handel;
    Res.element = element;
    if (Res.handel.WaitForCallBack == null) {
      Res.handel.WaitForCallBack = 0;
    };
    if (element.type == "file") {
      Res.File = true;
      if (element.files[0]) {
        value = element.files[0];
      } else {
        value = element.cloneNode(true);
      }
    } else if (element.nodeName == "TEXTAREA") {
      value = element.value;
    } else if (element.nodeName == "SELECT" && element.selectedIndex != -1) {
      var multiple = element.getAttribute('multiple');
      if (multiple && multiple != '') {
        var Stack = new Array();
        while (element.selectedIndex != -1) {
          value.push(element.options[element.selectedIndex].value);
          Stack.push(element.selectedIndex);
          element.options[element.selectedIndex].selected = false;
        };
        for (var i = 0; i < Stack.length; i++) {
          element.options[Stack[i]].selected = true;
        };
      } else {
        value = element.options[element.selectedIndex].value;
      };
    } else {
      if (element.type == "checkbox") {
        element.value = (element.value != null) ? element.value : '1';
		var notCheckedValue = (element.getAttribute('notcheckedvalue') != null) ? element.getAttribute('notcheckedvalue') : '0';
        value = (element.checked == true) ? element.value : notCheckedValue;
      } else
        value = element.value;
    };
    if (window.FormData != 'undefined' && window.FormData) {
      if ($.isArray(value)) {
        for (var i = 0; i < value.length; i++) {
          handel.append(element.name, value[i]);
        };
      } else {
        handel.append(element.name, value);
      };
    } else {
      if ($.isArray(value)) {
        for (var i = 0; i < value.length; i++) {
          var input = document.createElement("input");
          input.name = element.name;
          input.type = "hidden";
          input.setAttribute('value', value[i]);
          var node = input.cloneNode(true);
          //alert(handel.parentNode.nodeName);
          handel.appendChild(node);
        };
      } else {
        if (Res.File) {
          var node = value.cloneNode(true);
          handel.appendChild(node);
        } else {
          var input = document.createElement("input");
          input.name = element.name;
          input.type = "hidden";
          input.setAttribute('value', value);
          var node = input.cloneNode(true);
          //alert(handel.parentNode.nodeName);
          handel.appendChild(node);
        }
      };
    };
  };
  this.appendData = function (handel, name, value) {
    if (handel.WaitForCallBack == null) {
      handel.WaitForCallBack = 0;
    };
    if (window.FormData != 'undefined' && window.FormData) {
      handel.append(name, value);
    } else {
      var input = document.createElement("input");
      input.name = name;
      input.type = "hidden";
      input.setAttribute('value', value);
      //alert(handel.parentNode.nodeName);
      handel.appendChild(input);
    };
  };
  this.send = function (handel, URL, Method, UploadProgress, UploadComplete, UploadFailed, UploadCanceled, FU, CallBackOnSend) {
    if (window.FormData != 'undefined' && window.FormData) {
      var res = new Object();
      res.handel = handel;
      res.URL = URL;
      res.Method = Method;
      res.UploadProgress = UploadProgress;
      res.UploadComplete = UploadComplete;
      res.UploadFailed = UploadFailed;
      res.UploadCanceled = UploadCanceled;
      res.CallBackOnSend = CallBackOnSend;
      res.FU = FU;
      res.Interval = self.setInterval(function () {
        if (res.handel.WaitForCallBack <= 0) {
          loadXMLPostDocf(res.URL, res.Method, res.handel, res.UploadProgress, res.UploadComplete, res.UploadFailed, res.UploadCanceled, res.FU, res.CallBackOnSend);
          window.clearInterval(res.Interval);
        };
      }, 100);
    } else {
      handel.method = Method;
      handel.action = URL;
      handel.submit();
      alert('ارسال شد');
      UploadComplete();
    };
  };
};

function AutoLoad(Point, method, posData, uploadProgress, UploadComplete, uploadFailed, uploadCanceled, complete, CallBackOnSend) {
  $(Point)
    .find('[Ajax="true"]')
    .each(function (index, element) {
      var Url = element.getAttribute('Url');
      var uploadComplete = function (evt) {
        if (evt.target.status == 200 || evt.target.status == 304) {
          if (UploadComplete) UploadComplete(evt);
          element.innerHTML = responseText(evt.target);
        };
      };
      return loadXMLPostDocf(Url, method, posData, uploadProgress, uploadComplete, uploadFailed, uploadCanceled, complete, CallBackOnSend);
    });
};

function evalWithVariables(func, vars) {
  return new Function("v", "with (v) { " + func + " return false; }")(vars);
};
/*function evalWithVariables(func, vars) {
 var varString = "";

 for (var i in vars)
     varString += "var " + i + " = " + vars[i] + ";";   
console.log(varString + " var result = (" + func + ")");
 eval(varString + "; var result = (" + func + ")");
 return result;
}*/
function submitForm(ID, URL, Method, append, CheckData, UploadProgress, UploadComplete, UploadFailed, UploadCanceled, FU, CallBackOnSend) {
  var res = new Object();
  if ($(ID)
    .find('form')
    .length == 0) {
    formelement = ID;
    $(formelement)
      .submit(function (event, ui) {
        if (!event.srcElement) {
          event.srcElement = event.target;
        }
        var DoOnSubmit = event.srcElement.getAttribute('Submitallow');
        if (DoOnSubmit && DoOnSubmit == 'false') {
          if (UploadCanceled) {
            UploadCanceled(false);
          };
          return false;
        };
        var DoOnSubmit = event.srcElement.getAttribute('DoOnSubmit');
        if (DoOnSubmit && DoOnSubmit != '') {
          var erun = evalWithVariables(DoOnSubmit, {
            "Point": event.srcElement
          });
          if (erun == false) {
            return false;
          };
        };
        if (URL == null) URL = event.srcElement.action;
        if (Method == null) Method = event.srcElement.method;
        if (CheckData !== 'undefined' && CheckData != "" && CheckData)
          if (!CheckData(event, ID)) {
            return false;
          };
        var form = new formdata();
        $Ah = form.init();
        // alert($Ah.nodeName);
        if (window.FormData == 'undefined' || !window.FormData) {
          alert('در حال ارسال...');
        }
        if (append !== 'undefined' && append != "" && append != null) append(form, $Ah);
        $(formelement)
          .find("input,textarea,select")
          .each(function (index, element) {
            if (!element.getAttribute('block')) {
              form.appendEle($Ah, element);
            };
          });
        form.send($Ah, URL, Method, UploadProgress, UploadComplete, UploadFailed, UploadCanceled, FU, CallBackOnSend);
        //clearInterval(res.id);
        //alert(234);
        var DoAfterSubmit = event.srcElement.getAttribute('DoAfterSubmit');
        if (DoAfterSubmit && DoAfterSubmit != '') {
          var erun = evalWithVariables(DoAfterSubmit, {
            "Point": event.srcElement
          });
          if (erun == false) {
            return false;
          };
        };
        return false;
      });
  } else {
    $(ID)
      .find('form')
      .each(function (formindex, formelement) {
        $(formelement)
          .submit(function (event, ui) {
            if (!event.srcElement) {
              event.srcElement = event.target;
            }
            var DoOnSubmit = event.srcElement.getAttribute('Submitallow');
            if (DoOnSubmit && DoOnSubmit == 'false') {
              if (UploadCanceled) {
                UploadCanceled(false);
              };
              return false;
            };
            var DoOnSubmit = event.srcElement.getAttribute('DoOnSubmit');
            if (DoOnSubmit && DoOnSubmit != '') {
              var erun = evalWithVariables(DoOnSubmit, {
                "Point": event.srcElement
              });
              if (erun == false) {
                return false;
              };
            };
            if (URL == null) URL = event.srcElement.action;
            if (Method == null) Method = event.srcElement.method;
            if (CheckData !== 'undefined' && CheckData != "" && CheckData)
              if (!CheckData(event, ID)) {
                return false;
              };
            var form = new formdata();
            $Ah = form.init();
            // alert($Ah.nodeName);
            if (window.FormData == 'undefined' || !window.FormData) {
              alert('در حال ارسال...');
            }
            if (append !== 'undefined' && append != "" && append != null) append(form, $Ah);
            $(formelement)
              .find("input,textarea,select")
              .each(function (index, element) {
                if (!element.getAttribute('block')) {
                  form.appendEle($Ah, element);
                };
              });
            form.send($Ah, URL, Method, UploadProgress, UploadComplete, UploadFailed, UploadCanceled, FU, CallBackOnSend);
            //clearInterval(res.id);
            //alert(234);
            var DoAfterSubmit = event.srcElement.getAttribute('DoAfterSubmit');
            if (DoAfterSubmit && DoAfterSubmit != '') {
              var erun = evalWithVariables(DoAfterSubmit, {
                "Point": event.srcElement
              });
              if (erun == false) {
                return false;
              };
            };
            return false;
          });
      });
  };
  this.SubmitForm = function () {
    var DoOnSubmit = ID.getAttribute('DoOnSubmit');
    if (DoOnSubmit && DoOnSubmit != '') {
      var erun = evalWithVariables(DoOnSubmit, {
        "Point": ID
      });
      if (erun == false) {
        return false;
      };
    };
    if (URL == null) URL = ID.action;
    if (CheckData !== 'undefined' && CheckData != "" && CheckData)
      if (!CheckData(event, ID)) {
        //UploadFailed(event);
        return false;
      };
    var form = new formdata();
    $Ah = form.init();
    if (window.FormData == 'undefined' || !window.FormData) {
      alert('در حال ارسال...');
    }
    if (append !== 'undefined' && append != "") append(form, $Ah);
    $(ID)
      .find("input,textarea,select")
      .each(function (index, element) {
        form.appendEle($Ah, element);
      });
    form.send($Ah, URL, Method, UploadProgress, UploadComplete, UploadFailed, UploadCanceled, FU, CallBackOnSend);
    var DoAfterSubmit = ID.getAttribute('DoAfterSubmit');
    if (DoAfterSubmit && DoAfterSubmit != '') {
      var erun = evalWithVariables(DoAfterSubmit, {
        "Point": ID
      });
      if (erun == false) {
        return false;
      };
    };
    return false;
  };
};

function AddWin32(url, method, Subject, Body, Width, Height, Left, Top, Icon, posData, UploadProgress, UploadComplete, UploadFailed, UploadCanceled, Complete, OnAdd, WinThemp, ErrorPage, DragAble, OnExit, OnRestore, OnMinimize, CallBackOnSend) {
  var complete = function (e) {
    Body += responseText(e.target);
    if (Complete) Complete(e);
    //addwin(Subject,Body,Height,Width,Left,Top,OnAdd,DragAble,OnExit,OnRestore,OnMinimize,Icon);
  };
  var uploadProgress = function (evt) {
    if (UploadProgress) UploadProgress(evt);
  };
  var uploadComplete = function (evt) {
    if (evt.target.status == 200 || evt.target.status == 304) {
      if (UploadComplete) UploadComplete(evt);
      Body = responseText(evt.target);
      //console.log(Body);
    } else {
      loadXMLPostDocf(ErrorPage + "?error=" + evt.target.status, method, posData, uploadProgress, uploadComplete, uploadFailed, uploadCanceled, complete, CallBackOnSend);
      return false;
    };
    addwin(Subject, Body, Width, Height, Left, Top, OnAdd, DragAble, OnExit, OnRestore, OnMinimize, Icon, WinThemp(Body, Subject));
  };
  var uploadFailed = function (evt) {
    if (UploadFailed) UploadFailed(evt);
  };
  var uploadCanceled = function (evt) {
    if (UploadCanceled) UploadCanceled(evt);
  };
  loadXMLPostDocf(url, method, posData, uploadProgress, uploadComplete, uploadFailed, uploadCanceled, complete, CallBackOnSend);
};