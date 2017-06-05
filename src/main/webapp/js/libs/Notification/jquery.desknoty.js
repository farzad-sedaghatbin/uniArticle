;(function($) {
	$.desknotyQueue={};
	$.desknoty = function(options) {
        var desknotyObj=new Object();
		desknotyObj.defaults = {
			icon: null,
			title: "",
			body: "",
			timeout: 5000,
			sticky: false,
			id: null,
			type: 'normal',
			url: '',
			dir: 'rtl',
			onClick: function() {},
			onShow: function() {},
			onClose: function() {},
			onError: function() {}
		};

		desknotyObj.p = this, noti = null;

		desknotyObj.p.set = {};

		desknotyObj.init = function() {
			desknotyObj.p.set = $.extend({}, desknotyObj.defaults, options);
			if(desknotyObj.isSupported()) {
				if(window.webkitNotifications.checkPermission() != 0){
					desknotyObj.getPermissions(desknotyObj.init);
				} else {
					if(desknotyObj.p.set.type === 'normal') desknotyObj.createNoti();
					else if(desknotyObj.p.set.type === 'html') desknotyObj.createNotiHtml();
				};
			} else {
				alert("Desktop notifications are not supported!");
			};
		};

		desknotyObj.createNoti = function() {
			desknotyObj.noti = window.webkitNotifications.createNotification(desknotyObj.p.set.icon, desknotyObj.p.set.title, desknotyObj.p.set.body);
				if(desknotyObj.p.set.dir) desknotyObj.noti.dir = desknotyObj.p.set.dir;
				if(desknotyObj.p.set.onclick) desknotyObj.noti.onclick = desknotyObj.p.set.onclick;
				if(desknotyObj.p.set.onshow) desknotyObj.noti.onshow = desknotyObj.p.set.onshow;
				if(desknotyObj.p.set.onclose) desknotyObj.noti.onclose = desknotyObj.p.set.onclose;
				if(desknotyObj.p.set.onerror) desknotyObj.noti.onerror = desknotyObj.p.set.onerror;
				if(desknotyObj.p.set.id) desknotyObj.noti.replaceId = desknotyObj.p.set.id;
			desknotyObj.noti.show();
			if(!desknotyObj.p.set.sticky) setTimeout(function(){ desknotyObj.noti.cancel(); }, desknotyObj.p.set.timeout);
		};
		desknotyObj.createNotiHtml = function() {
			desknotyObj.noti = window.webkitNotifications.createHTMLNotification(desknotyObj.p.set.url);
				if(desknotyObj.p.set.dir) desknotyObj.noti.dir = desknotyObj.p.set.dir;
				if(desknotyObj.p.set.onclick) desknotyObj.noti.onclick = desknotyObj.p.set.onclick;
				if(desknotyObj.p.set.onshow) desknotyObj.noti.onshow = desknotyObj.p.set.onshow;
				if(desknotyObj.p.set.onclose) desknotyObj.noti.onclose = desknotyObj.p.set.onclose;
				if(desknotyObj.p.set.onerror) desknotyObj.noti.onerror = desknotyObj.p.set.onerror;
				if(desknotyObj.p.set.id) desknotyObj.noti.replaceId = desknotyObj.p.set.id;
			desknotyObj.noti.show();
			if(!desknotyObj.p.set.sticky) setTimeout(function(){ desknotyObj.noti.cancel(); }, desknotyObj.p.set.timeout);
		};

		desknotyObj.isSupported = function() {
			if (window.webkitNotifications)return true;
			else return false;
		};
		desknotyObj.getPermissions = function(callback) {
			window.webkitNotifications.requestPermission(callback);
		};
		desknotyObj.init();
		return desknotyObj;
	};
})(jQuery);