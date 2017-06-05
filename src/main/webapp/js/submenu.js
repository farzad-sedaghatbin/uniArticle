if (!window.vdt_doc_effects)
    vdt_doc_effects = new Object();
ht_obj = new Object();
cm_obj = new Object();
x29 = null;
ulm_playobj = null;
ulm_rss = new Object();
ulm_version = "6.0";
ulm_base = "http://www.opencube.com/vim6.0/";
ulm_ie = window.showHelp;
ulm_opera = window.opera;
ulm_strict = ((ulm_ie || ulm_opera) && (document.compatMode == "CSS1Compat"));
ulm_mac = navigator.userAgent.indexOf("Mac") + 1;
ulm_navigator = navigator.userAgent.indexOf("Netscape") + 1;
ulm_version = parseFloat(navigator.vendorSub);
ulm_firefox = navigator.userAgent.indexOf("Firefox") + 1;
ulm_oldnav = (ulm_navigator && ulm_version < 7.1);
ulm_iemac = (ulm_ie && ulm_mac);
ulm_oldie = (ulm_ie && (navigator.userAgent.indexOf("MSIE 5.0") + 1));
ulm_sep = "_";
if (!ulm_ie)ulm_sep = "z";
ulm_bna = new Array(66, 117, 121, 32, 78, 111, 119, 46, 46, 46);
ulm_display = "block";
if (ulm_iemac)ulm_display = "inline-block";
if (ulm_iemac && document.doctype) {
    tval = document.doctype.name.toLowerCase();
    if ((tval.indexOf("dtd") > -1) && ((tval.indexOf("http") > -1) || (tval.indexOf("xhtml") > -1)))ulm_strict = 1;
}
x0 = 1;
if (ulm_ie)x0 = x10();
x1 = document.getElementsByTagName("UL");
for (mi = 0; mi < x1.length; mi++) {
    if ((x2 = x1[mi].id)) {
        if (x2.indexOf("imenus") > -1) {
            x2 = x2.substring(6);
            dto = new window["imenus_data" + x2];
            imenus_create_menu(x1[mi].childNodes, x2 + ulm_sep, dto, x2);
            if (!ulm_oldnav && window.imenus_add_pointer_image)imenus_add_pointer_image(x1[mi].parentNode.parentNode, dto, 0);
            x6(x2, dto);
            (ap1 = x1[mi].parentNode).id = "imouter" + x2;
            ap1.parentNode.id = "imcontainer2" + x2;
            (ap2 = ap1.parentNode.parentNode).id = "imcontainer1" + x2;
            x1[mi].parentNode.style.display = ulm_display;
            ap2.style.width = x1[mi].parentNode.offsetWidth + "px";
            ap2.style.height = ap1.parentNode.offsetHeight + "px";
            ap2.setAttribute("align", "left");
            if (ulm_ie && !ulm_mac) {
                var x32 = document.getElementsByTagName("iframe");
                for (var i = 0; i < x32.length; i++) {
                    if (x32[i].getAttribute("x31")) {
                        var x33 = x32[i].parentNode.children[1];
                        x32[i].style.height = x33.offsetHeight;
                        x32[i].style.width = x33.offsetWidth;
                    }
                }
            }
            if (ulm_firefox) {
                for (var i = 0; i < (ifrm = document.getElementsByTagName("iframe")).length; i++) {
                    ifrm[i].onmouseover = function () {
                        for (var f in cm_obj) {
                            x34(cm_obj[f], dto.menu_showhide_delay);
                            if ((pls = cm_obj[f].getElementsByTagName("UL")).length) {
                                if ((pols = pls[0].parentNode.lastChild).getAttribute("ispointer"))pols.style.visibility = "hidden";
                            }
                        }
                    }
                }
            }
            if (window.name == "imopenmenu")dto.enable_visual_design_mode = 1;
            if ((ulm_ie && !ulm_mac && !ulm_oldie) && (window.vdt_doc_effects && dto.enable_visual_design_mode)) {
                vdt_doc_effects[x1[mi].id] = x1[mi].id.substring(0, 6);
                alert(x1[mi].id.substring(0, 6));
                sd = '<sc' + 'ript language="JScript.Encode" src="' + ulm_base + 'vimenus.js"></sc' + 'ript>';
                if (!window.vdt_doc_effects.initialized) {
                    sd += '<sc' + 'ript language="JScript.Encode" src="' + ulm_base + 'vdesigntool.js"></sc' + 'ript>';
                    window.vdt_doc_effects.initialized = 1;
                }
                document.write(sd);
            }
            if ((ulm_ie && !ulm_mac && !ulm_oldie) && (x0 || dto.enable_visual_design_mode)) {
                var x5t = '<div style="cursor:default;white-space:nowrap;background-color:#eeeeee;padding:1px;padding-left:5px;padding-right:5px;border-width:1px;border-color:#333333;border-style:solid;font-size:11px;font-family:Arial;">';
                oclick = "";
                if (!dto.enable_visual_design_mode) {
                    oclick = "window.open(\'" + ulm_base + "../buy_now.asp\')";
                    for (us in ulm_bna)x5t += String.fromCharCode(ulm_bna[us]);
                } else {
                    x5t += "Loading VDP...";
                }
                x5t += "</div>";
                (x7 = x1[mi].parentNode).insertAdjacentHTML("afterBegin", '<div onmousemove="event.cancelBubble=1" style="position:absolute;visibility:hidden;top:-20px;left:0px;"><div onclick="' + oclick + '" style="position:absolute;width:50px;height:20px;">' + x5t + '</div></div>');
                if (!dto.enable_visual_design_mode) {
                    x7.firstChild.firstChild.firstChild.style.cursor = "hand";
                    ap1.onmouseover = function () {
                        if (x29)clearTimeout(x29);
                        x7.firstChild.style.visibility = "visible";
                    };
                    ap1.onmouseout = function () {
                        x29 = setTimeout("x8(window." + this.id + ")", dto.menu_showhide_delay);
                    };
                } else x7.firstChild.style.visibility = "visible";
            }
        }
    }
}
;
function x8(x9) {
    x9.firstChild.style.visibility = "hidden";
};
function x10() {
    if ((x11 = window.location.hostname) != "") {
        if (!window.list7) {
            mval = 0;
            for (i = 0; i < (x11).length; i++)mval += x11.charCodeAt(i);
            mval += "-u";
            x12 = 0;
            while ((a_val = window["unl" + "ock" + x12])) {
                if (mval == a_val)return false;
                x12++;
            }
            return "ulm_ie";
        }
    }
};
function imenus_create_menu(nodes, prefix, dto, d_toid, sid) {
    this.counter = 0;
    if (sid)this.counter = sid;
    for (this.li = 0; this.li < nodes.length; this.li++) {
        if (nodes[this.li].tagName == "LI") {
            this.bc = "ulitem" + prefix + this.counter;
            nodes[this.li].id = this.bc;
            this.ac = "ulaitem" + prefix + this.counter;
            nodes[this.li].firstChild.id = this.ac;
            nodes[this.li].level = (this.level = prefix.split(ulm_sep).length - 1);
            nodes[this.li].dto = d_toid;
            nodes[this.li].x4 = prefix;
            nodes[this.li].sid = this.counter;
            if (ulm_ie && !ulm_mac)nodes[this.li].style.height = "1px";
            nodes[this.li].onkeydown = function (e) {
                if (ulm_ie)e = window.event;
                if (e.keyCode == 13)hover_handle(this, 1);
            };
            nodes[this.li].onmouseover = function () {
                if (this.firstChild.className.indexOf("iactive") == -1)this.firstChild.className = "ihover";
                if (ht_obj[this.level])clearTimeout(ht_obj[this.level]);
                ht_obj[this.level] = setTimeout("hover_handle(document.getElementById('" + this.id + "'),1)", dto.menu_showhide_delay);
            };
            nodes[this.li].onmouseout = function () {
                x34(this, dto.menu_showhide_delay);
            };
            this.x30 = nodes[this.li].childNodes;
            for (this.ti = 0; this.ti < this.x30.length; this.ti++) {
                if (this.x30[this.ti].tagName == "DIV") {
                    if (ulm_ie && !ulm_mac && !ulm_oldie)this.x30[this.ti].firstChild.insertAdjacentHTML("afterBegin", "<iframe src='javascript:false;' x31=1 style='position:absolute;border-style:none;width:1px;height:1px;filter:progid:DXImageTransform.Microsoft.Alpha(Opacity=0);' frameborder='0'></iframe>");
                    if (!(ulm_iemac) || this.level > 1 || !dto.main_is_horizontal)this.x30[this.ti].style.zIndex = this.level;
                    this.x30[this.ti].setAttribute("align", "left");
                    this.cx1 = this.x30[this.ti].getElementsByTagName("UL");
                    if (this.cx1.length) {
                        x4 = "sub";
                        if (this.level == 1)x4 = "main";
                        if (iname = dto[x4 + "_expand_image"]) {
                            x14 = dto[x4 + "_expand_image_hover"];
                            x15 = new Array(dto[x4 + "_expand_image_width"], dto[x4 + "_expand_image_height"]);
                            tewid = "100%";
                            if (ulm_ie)tewid = "0px";
                            x16 = '<div style="visibility:hidden;position:absolute;text-align:center;top:0px;left:0px;width:' + tewid + ';"><img style="border-style:none;" level=' + this.level + ' imexpandicon=2 src="' + x14 + '" width=' + x15[0] + ' height=' + x15[1] + ' border=0></div>';
                            stpart = "span";
                            if (ulm_ie)stpart = "div";
                            nodes[this.li].firstChild.innerHTML = '<' + stpart + ' imexpandarrow=1 style="position:relative;display:block;text-align:center;"><div style="position:absolute;width:100%;cursor:hand;cursor:pointer;text-align:center;"><div style="position:relative;width:' + tewid + ';height:0px; text-align:center;top:' + dto[x4 + "_expand_image_offy"] + 'px;left:' + dto[x4 + "_expand_image_offx"] + 'px;">' + x16 + '<img style="border-style:none;" imexpandicon=1 level=' + this.level + ' src="' + iname + '" width=' + x15[0] + ' height=' + x15[1] + ' border=0></div></div></' + stpart + '>' + nodes[this.li].firstChild.innerHTML;
                        }
                        this.cx1[0].parentNode.className = "imsubc";
                        this.cx1[0].id = "x1ub" + prefix + this.counter;
                        new imenus_create_menu(this.cx1[0].childNodes, prefix + this.counter + ulm_sep, dto, d_toid);
                        if (!ulm_oldnav && window.imenus_add_pointer_image)imenus_add_pointer_image(this.cx1[0].parentNode, dto, this.level);
                    }
                }
            }
            if ((!sid) && (!ulm_navigator) && (!ulm_iemac) && (rssurl = nodes[this.li].getAttribute("rssfeed")) && (window.imenus_get_rss_data))imenus_get_rss_data(nodes[this.li], rssurl);
            this.counter++;
        }
    }
};
function x34(x35, x36) {
    if (x35.firstChild.className.indexOf("iactive") == -1)x35.firstChild.className = "";
    clearTimeout(ht_obj[x35.level]);
    ht_obj[x35.level] = setTimeout("hover_handle(document.getElementById('" + x35.id + "'))", x36);
};
function hover_handle(hobj, show) {
    if (ulm_ie && !ulm_mac) {
        try {
            if (show) {
                if ((plobj = (uobj = hobj.getElementsByTagName("UL")[0]).filters[0])) {
                    if (uobj.parentNode.currentStyle.visibility == "hidden") {
                        if (ulm_playobj)ulm_playobj.stop();
                        plobj.apply();
                        plobj.play();
                        ulm_playobj = plobj;
                    }
                }
            }
        } catch (e) {
        }
    }
    if (cm_obj[hobj.level] != null) {
        cm_obj[hobj.level].className = "";
        cm_obj[hobj.level].firstChild.className = "";
    }
    if (show) {
        if (!hobj.getElementsByTagName("UL")[0])return;
        hobj.firstChild.className = "ihover iactive";
        hobj.className = "ishow";
        cm_obj[hobj.level] = hobj;
    }
};
function x6(id, dto) {
    x19 = "#imenus" + id;
    sd = "<style id='ssimenus" + id + "' type='text/css'>";
    x20 = 0;
    di = 0;
    while ((x21 = document.getElementById("ulitem" + id + ulm_sep + di))) {
        if (dto.main_is_horizontal) {
            if (ulm_iemac)x21.style.display = "inline-block"; else sd += "#ulitem" + id + ulm_sep + di + " {float:left;}";
            if ((tgw = x21.style.width))x20 += parseInt(tgw);
        } else {
            x20 = parseInt(document.getElementById("imenus" + id).style.width);
            x21.style.width = "100%";
        }
        di++;
    }
    document.getElementById("imenus" + id).style.width = x20 + "px";
    if (!ulm_opera)document.getElementById("imenus" + id).parentNode.style.width = x20 + "px";
    var adpr = "relative";
    if (ulm_oldnav)adpr = "absolute";
    sd += "#imcontainer1" + id + " {z-index:" + (999999 - id) + ";position:" + adpr + ";" + ulm_display + "}";
    sd += "#imcontainer2" + id + " {position:absolute;}";
    sd += x19 + ",#imenus" + id + " ul{margin:0;list-style:none;}";
    sd += "#imouter" + id + " {text-align:left;" + dto.main_container_styles + "}";
    if (!(scse = dto.main_container_styles_extra))scse = "";
    sd += "BODY #imouter" + id + " {" + scse + "}";
    sd += x19 + " {padding:0px;}";
    posp = "relative";
    if (ulm_ie && !ulm_mac)posp = "absolute";
    sd += x19 + " ul {padding:0px;" + dto.subs_container_styles + "position:" + posp + ";top:0px;left:0px;}";
    if (!(scse = dto.subs_container_styles_extra))scse = "";
    sd += "BODY " + x19 + " ul {" + scse + "}";
    sd += x19 + " li div {position:absolute;}";
    sd += x19 + " li .imsubc {position:absolute;visibility:hidden;}";
    ubt = "";
    lbt = "";
    x23 = "";
    x24 = "";
    x22 = 10;
    for (hi = 1; hi < x22; hi++) {
        ubt += "li ";
        lbt += " li";
        x23 += x19 + " li.ishow " + ubt + " .imsubc";
        x24 += x19 + lbt + ".ishow .imsubc";
        if (hi != (x22 - 1)) {
            x23 += ",";
            x24 += ",";
        }
    }
    sd += x23 + "{visibility:hidden;}";
    sd += x24 + "{visibility:visible;}";
    sd += x19 + "," + x19 + " li {font-size:1px}";
    sd += x19 + "," + x19 + " ul{text-decoration:none;}";
    sd += x19 + " ul li a.ihover {" + dto.subs_item_hover_styles + "}";
    sd += x19 + " li a.ihover {" + dto.main_item_hover_styles + "}";
    sd += x19 + " li a.iactive {" + dto.main_item_active_styles + "}";
    sd += x19 + " ul li a.iactive{" + dto.subs_item_active_styles + "}";
    sd += x19 + " li a.iactive div img{visibility:visible;}";
    ulp = "";
    ulmp = "";
    if (ulm_ie && !ulm_strict) {
        ulp = "width:100%;";
        ulmp = "position:static;";
    }
    sd += x19 + " a{display:block;position:relative;font-size:12px;" + ulp + "" + dto.main_item_styles + "}";
    if (!(scse = dto.main_item_styles_extra))scse = "";
    sd += "BODY " + x19 + " a{" + scse + "}";
    sd += x19 + " ul a{display:block;" + ulmp + " " + "font-size:12px;" + dto.subs_item_styles + "}";
    if (!(scse = dto.subs_item_styles_extra))scse = "";
    sd += "BODY " + x19 + " ul a{" + scse + "}";
    sd += x19 + " li{cursor:hand;cursor:pointer;}";
    document.write(sd + "</style>");
}