////////////////////////////// Usage : onkeyup="farsiNumberConverter(this,event);" //////////////////////////////////

var buf = new StringBuffer();

function StringBuffer() {
    this.buffer = [];
}

StringBuffer.prototype.append = function append(string) {
    this.buffer.push(string);
    return this;
}

StringBuffer.prototype.removeLast = function removeLast() {
    this.buffer.pop();
    return this;
}

StringBuffer.prototype.removeFirst = function removeFirst() {
    this.buffer.shift();
    return this;
}

StringBuffer.prototype.toString = function toString() {
    return this.buffer.join("");
}

function farsiNumberConverter(componentName, e) {

    var char;
    var sText = componentName.value;
    var KeyID;

    if (window.event) {
        KeyID = window.event.keyCode;
    } else {
        KeyID = e.which;
    }

    if (KeyID == 46) {
        componentName.focus();
    }
    if (KeyID == 8) {
        buf.removeLast();
    }
    if (KeyID == 48) {
        buf.append(String.fromCharCode(1632));
    }
    if (KeyID == 49) {
        buf.append(String.fromCharCode(1633));
    }
    if (KeyID == 50) {
        buf.append(String.fromCharCode(1634));
    }
    if (KeyID == 51) {
        buf.append(String.fromCharCode(1635));
    }
    if (KeyID == 52) {
        buf.append(String.fromCharCode(1636));
    }
    if (KeyID == 53) {
        buf.append(String.fromCharCode(1637));
    }
    if (KeyID == 54) {
        buf.append(String.fromCharCode(1638));
    }
    if (KeyID == 55) {
        buf.append(String.fromCharCode(1639));
    }
    if (KeyID == 56) {
        buf.append(String.fromCharCode(1640));
    }
    if (KeyID == 57) {
        buf.append(String.fromCharCode(1641));
    }

    componentName.value = buf.toString();
}