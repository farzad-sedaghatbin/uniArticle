var time = null;
var outOfWindow = false;
var bAltF4AsLast = false;
var pageRefreshed = false;
var modalVisibility = false;
var charactersRestricted = new Array(48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 1776, 1777, 1778, 1779, 1780, 1781, 1782, 1783, 1784, 1785, 8, 0);
var msgWindow;



function loginToSystem(e) {

    var key;
    if (window.event) {
        key = window.event.keyCode;
    } else {
        key = e.which;
    }
    if (key == 13) {
        document.getElementById('loginForm').submit();
    }
    else {
        return false;
    }
}


function newWindow(file, window, width, height, scroll, toolbar, menubar, directory, location, resizable, status) {
    msgWindow = open(file, window, 'copyhistory=no,directories=' + directory + ',location=' + location + ',menubar=' + menubar + ',resizable=' + resizable + ',toolbar=' + toolbar + ',status=' + status + ',width=' + width + ',height=' + height + ',scrollbars=' + scroll + ',left=200,top=150');
    if (msgWindow.opener == null)
        msgWindow.opener = self;
}

function closeWindow() {
    if (msgWindow != null) {
        msgWindow.close();
    }
}

function mouseOver(Obj) {
    Obj.style.color = "#cc0033";
}

function removeRow(id) {
    document.getElementById(id).deleteRow(0);
}

function mouseOut(Obj) {
    Obj.style.color = "#000066";
}

function setFocus() {
}

function cacheValue(param1, param2) {
    document.getElementById(param1).value = param2;
}

function assignmentToAccountTextBox(responsibleId, responsibleFieldId) {
    document.getElementById(responsibleFieldId).value = responsibleId;
}
function invokeWorkgroup() {
}
function invokeRole() {
}
function invokeMember() {
}
function invokeUser() {
}
function invokeRing() {
}
function invokeOrganization() {
}
function invokeBaseInformationParent() {
}
function invokeBaseInformationChild() {
}
function invokeExit() {
}

function fillFamilyHandlePageFields(textBoxNationalCode, textBoxname, textBoxlastname, textBoxBirthDate, textBoxfatherName, textBoxRelationship, textBoxjob) {
    document.getElementById('FamilyInformation-lov-subview:FamilyInformationFRM:textBoxNationalCode').value = textBoxNationalCode;
    document.getElementById('FamilyInformation-lov-subview:FamilyInformationFRM:textBoxname').value = textBoxname;
    document.getElementById('FamilyInformation-lov-subview:FamilyInformationFRM:textBoxlastname').value = textBoxlastname;
    document.getElementById('FamilyInformation-lov-subview:FamilyInformationFRM:textBoxBirthDate').value = textBoxBirthDate;
    document.getElementById('FamilyInformation-lov-subview:FamilyInformationFRM:textBoxfatherName').value = textBoxfatherName;
    document.getElementById('FamilyInformation-lov-subview:FamilyInformationFRM:textBoxRelationship').value = textBoxRelationship;
    document.getElementById('FamilyInformation-lov-subview:FamilyInformationFRM:textBoxjob').value = textBoxjob;
}

function timerForReload() {
    setTimeout("submitByTimer()", 1000);
}

function submitByTimer() {
    document.forms['progressForm'].submit();
}
function submitByTimer2() {
}

function checkShowLockScreen() {
    if (document.getElementById('lockValue').value == 'true') {
        Richfaces.showModalPanel('lockscreenPanel');
    }
}

function checkHideLockScreen() {
    if (document.getElementById('lockValue').value == 'false') {
        Richfaces.hideModalPanel('lockscreenPanel');
    }
}

function showLockScreen() {
    Richfaces.showModalPanel('lockscreenPanel');
}

function hideLockScreen() {
    Richfaces.hideModalPanel('lockscreenPanel');
}


function showModal() {
    Richfaces.showModalPanel('ajaxLoadingModalBox');
    modalVisibility = true;
}

function hideModal() {
    Richfaces.hideModalPanel('ajaxLoadingModalBox');
    modalVisibility = false;
}

function backDisabled() {
    if (window.history.forward(1) != null) {
        window.history.forward(1);
    }
    if (window.event && window.event.keyCode == 8) {
        window.event.keyCode = 123;
    }
}


function mouseOver(Obj) {
    Obj.style.color = "#cc0033";
}

function mouseOut(Obj) {
    Obj.style.color = "#000066";
}

function topPage() {
    window.scrollTo(0, 0);
}

function moveText() {
    //self.moveBy(x,y);
}

function refreshText() {
    window.setTimeout('moveText()', 100);
}

//============================================= usage :  onkeypress="return convert(this,event);" ===========================
var lastkey = 0;
var farsi = true;
var s = new Array(32, 33, 34, 35, 36, 37, 1548, 1711, 41, 40, 215, 43, 1608, 45, 46, 47, 1632, 1633, 1634, 1635, 1636, 1637, 1638, 1639, 1640, 1641, 58, 1603, 44, 61, 46, 1567, 64, 1616, 1584, 125, 1609, 1615, 1609, 1604, 1570, 247, 1600, 1548, 47, 8217, 1583, 215, 1563, 1614, 1569, 1613, 1601, 8216, 123, 1611, 1618, 1573, 126, 1580, 1688, 1670, 94, 95, 1662, 1588, 1584, 1586, 1610, 1579, 1576, 1604, 1575, 1607, 1578, 1606, 1605, 1574, 1583, 1582, 1581, 1590, 1602, 1587, 1601, 1593, 1585, 1589, 1591, 1594, 1592, 60, 124, 62, 1617);
var b = navigator.userAgent.toLowerCase();
var msie = (b.indexOf('msie') > -1) ? true : false;
var gecko = (b.indexOf('gecko') > -1) ? true : false;
var opera = (b.indexOf('opera') > -1) ? true : false;
if (opera) {
    msie = false;
}


function changelang(fld) {
    if (farsi) {
        farsi = false;
    } else {
        farsi = true;
    }
    if (fld) {
        document.getElementsByName(fld)[0].focus();
    }
}


function PersianOnly(fld, e) {

    var key;
    if (window.event) {
        key = window.event.keyCode;
    } else {
        key = e.which;
    }

    if (((key >= 33) && (key <= 38)) || ((key >= 40) && (key <= 43))
        || (key == 94) || (key == 95) || (key == 1567)) {
        return false;
    }

    if (msie) {
        k = event.keyCode;
        if (farsi && k > 32 && k < 128) event.keyCode = s[k - 32];
    }

    if (gecko) {
        k = e.which;
        if (farsi && (k > 32 && k < 128) && !e.ctrlKey && !e.altKey && !e.metaKey) {
            fld.value = fld.value + String.fromCharCode(s[k - 32]);
            return false;
        }
    }

    if (opera) {
        k = event.keyCode;
        if (k > 32 && k < 128 && !event.ctrlKey && !event.altKey && !event.metaKey) {
            fld.value = fld.value + String.fromCharCode(s[k - 32]);
            return false;
        }
    }
}

function englishOnly(e) {
    var key;
    if (window.event) {
        key = window.event.keyCode;
    } else {
        key = e.which;
    }

    if (((key >= 48) && (key <= 57)) || ((key >= 97) && (key <= 122)) || key == 13 || key == 8 || key == 0 || ((key >= 65) && (key <= 90)))
        return true;
    else
        return false;
}

function numbersOnly(e) {
    var KeyID;

    if (window.event) {

        KeyID = window.event.keyCode;

    } else {

        KeyID = e.which;
    }

    if (KeyID == 118 && e.ctrlKey) {
        return true;
    }
    if (KeyID == 99 && e.ctrlKey) {
        return true;
    }

    for (counter = 0; counter < charactersRestricted.length; counter++) {

        if (charactersRestricted[counter] == KeyID) {
            return true;
        }
    }

    return false;
}

function isUserFriendlyChar(val) {
    if (val == 8 || val == 9 || val == 13 || val == 45 || val == 46)
        return true;

    if ((val > 16 && val < 21) || (val > 34 && val < 41))
        return true;
    return false;
}

function autoTab(current, to) {
    if (current.value.length == current.getAttribute("maxlength")) {
        to.select();
        to.focus();
    }
}

function confirmAndWait(message) {
    var answer = window.confirm(message);
    if (answer) {
        showModal();
    }
    return answer;
}
function highlight(element) {
    var latestRow = $('.selectedInDataTable');
    if (latestRow != undefined) {
        latestRow.removeClass("selectedInDataTable");
    }
    var row = jQuery(element).parent().parent().children();
    row.addClass("selectedInDataTable");
    element.click();
}

function initColorPicker() {
    document.getElementById('colorpickerbuttonid0').style.backgroundImage = 'none';
    document.getElementById('colorpickerbuttonid0').style.backgroundColor = document.getElementById('frm:color_picker').style.backgroundColor;
}

function removeLastUploadedFiles(element) {
    var listDiv = element.getElementsByClassName("rf-fu-lst")[0];
    var count = listDiv.getElementsByClassName("rf-fu-itm").length;
    if (count > 1) {
        listDiv.removeChild(listDiv.childNodes[0])
    }
}