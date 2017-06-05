var Methods;
var temporaryYear;
//var Jalali = require('index.js');
function strtotime(text, now) {
    // Convert string representation of date and time to a timestamp
    //
    // version: 1109.2015
    // discuss at: http://phpjs.org/functions/strtotime
    // +   original by: Caio Ariede (http://caioariede.com)
    // +   improved by: Kevin van Zonneveld (http://kevin.vanzonneveld.net)
    // +      input by: David
    // +   improved by: Caio Ariede (http://caioariede.com)
    // +   bugfixed by: Wagner B. Soares
    // +   bugfixed by: Artur Tchernychev
    // +   improved by: A. Matías Quezada (http://amatiasq.com)
    // +   improved by: preuter
    // +   improved by: Brett Zamir (http://brett-zamir.me)
    // %        note 1: Examples all have a fixed timestamp to prevent tests to fail because of variable time(zones)
    // *     example 1: strtotime('+1 day', 1129633200);
    // *     returns 1: 1129719600
    // *     example 2: strtotime('+1 week 2 days 4 hours 2 seconds', 1129633200);
    // *     returns 2: 1130425202
    // *     example 3: strtotime('last month', 1129633200);
    // *     returns 3: 1127041200
    // *     example 4: strtotime('2009-05-04 08:30:00');
    // *     returns 4: 1241418600
    var parsed, match, year, date, days, ranges, len, times, regex, i;
    if (!text) {
        return null;
    }
    // Unecessary spaces
    text = text.trim()
        .replace(/\s{2,}/g, ' ')
        .replace(/[\t\r\n]/g, '')
        .toLowerCase();
    if (text === 'now') {
        return now === null || isNaN(now) ? new Date()
            .getTime() / 1000 | 0 : now | 0;
    }
    if (!isNaN(parsed = Date.parse(text))) {
        return parsed / 1000 | 0;
    }
    if (text === 'now') {
        return new Date()
            .getTime() / 1000; // Return seconds, not milli-seconds
    }
    if (!isNaN(parsed = Date.parse(text))) {
        return parsed / 1000;
    }
    match = text.match(/^(\d{2,4})-(\d{2})-(\d{2})(?:\s(\d{1,2}):(\d{2})(?::\d{2})?)?(?:\.(\d+)?)?$/);
    if (match) {
        year = match[1] >= 0 && match[1] <= 69 ? +match[1] + 2000 : match[1];
        return new Date(year, parseInt(match[2], 10) - 1, match[3],
            match[4] || 0, match[5] || 0, match[6] || 0, match[7] || 0) / 1000;
    }
    date = now ? new Date(now * 1000) : new Date();
    days = {
        'sun': 0,
        'mon': 1,
        'tue': 2,
        'wed': 3,
        'thu': 4,
        'fri': 5,
        'sat': 6
    };
    ranges = {
        'yea': 'FullYear',
        'mon': 'Month',
        'day': 'Date',
        'hou': 'Hours',
        'min': 'Minutes',
        'sec': 'Seconds'
    };

    function lastNext(type, range, modifier) {
        var diff, day = days[range];
        if (typeof day !== 'undefined') {
            diff = day - date.getDay();
            if (diff === 0) {
                diff = 7 * modifier;
            } else if (diff > 0 && type === 'last') {
                diff -= 7;
            } else if (diff < 0 && type === 'next') {
                diff += 7;
            }
            date.setDate(date.getDate() + diff);
        }
    }

    function process(val) {
        var splt = val.split(' '), // Todo: Reconcile this with regex using \s, taking into account browser issues with split and regexes
            type = splt[0],
            range = splt[1].substring(0, 3),
            typeIsNumber = /\d+/.test(type),
            ago = splt[2] === 'ago',
            num = (type === 'last' ? -1 : 1) * (ago ? -1 : 1);
        if (typeIsNumber) {
            num *= parseInt(type, 10);
        }
        if (ranges.hasOwnProperty(range) && !splt[1].match(/^mon(day|\.)?$/i)) {
            return date['set' + ranges[range]](date['get' + ranges[range]]() + num);
        }
        if (range === 'wee') {
            return date.setDate(date.getDate() + (num * 7));
        }
        if (type === 'next' || type === 'last') {
            lastNext(type, range, num);
        } else if (!typeIsNumber) {
            return false;
        }
        return true;
    }

    times = '(years?|months?|weeks?|days?|hours?|minutes?|min|seconds?|sec' +
        '|sunday|sun\\.?|monday|mon\\.?|tuesday|tue\\.?|wednesday|wed\\.?' +
        '|thursday|thu\\.?|friday|fri\\.?|saturday|sat\\.?)';
    regex = '([+-]?\\d+\\s' + times + '|' + '(last|next)\\s' + times + ')(\\sago)?';
    match = text.match(new RegExp(regex, 'gi'));
    if (!match) {
        return false;
    }
    for (i = 0, len = match.length; i < len; i++) {
        if (!process(match[i])) {
            return false;
        }
    }
    // ECMAScript 5 only
    //if (!match.every(process))
    //    return false;
    return (date.getTime() / 1000);
}
(function (o) {
    o.fn.CalendarSelectable = function () {
        var Document = this;
        Methods = {
            this: Document,
            Document: Document,
            SelectBox: {},
            InitJson: {},
            CurrentSelectBox: {},
            SelectBoxRollBack: {},
            SelectBoxPoint: '.SelectBox',
            CurrentYear: '',
            DayPointer: 0,
            $DAta: Array(),
            $TempDAta: Array(),
            AfterInit: null,
            Jalali: false,
            Console: {
                log: function (Error) {
                    console.log(Error);
                }
            },
            test: function (url, params) {

                Methods.CurrentYear = '2017';

                var InitJson = {};
                $.ajax({
                    type: 'POST',
                    url: url,
                    data: params,
                    success: function (r) {
                        if (r.Status == true) {
                            Methods.Console.log(JSON.stringify(r));

                            InitJson = r;
                        } else {
                            Methods.Console.log(r.Error);
                            InitJson = false;
                        }
                        ;
                    },
                    contentType: "text/plain",
                    dataType: "json",
                    async: false,
                    error: function (r) {
                        alert(r);
                    }
                });
                Methods.InitJson = InitJson;
                return Methods;
            },
            Ajax: function (ServerURL, Year, Parameter) {
                if (!Year) {
                    Year = 2017;
                }
                ;
                Methods.CurrentYear = String(Year);
                var InitJson = {};
                $.ajax({
                    type: 'POST',
                    url: ServerURL,
                    data: {
                        "operation": "Get_Year_Data_Calendar",
                        "year": Year,
                    },
                    //contentType:"application/json;charset=utf-8",
                    success: function (r) {
                        if (r.Status == true) {
                            InitJson = r;
                        } else {
                            Methods.Console.log(r.Error);
                            InitJson = false;
                        }
                        ;
                    },
                    dataType: "json",
                    async: false,
                    error: function (r) {
                    },
                });
                Methods.InitJson = InitJson;
                return Methods;
            },
            Ajax2: function (url, Year, Parameter) {
                Methods.CurrentYear = '2017';

                var InitJson = {};
                $.ajax({
                    type: 'POST',
                    url: url,
                    data: Methods.CurrentYear,
                    success: function (r) {
                        if (r.Status == true) {
                            Methods.Console.log(JSON.stringify(r));

                            InitJson = r;
                        } else {
                            Methods.Console.log(r.Error);
                            InitJson = false;
                        }
                        ;
                    },
                    contentType: "text/plain",
                    dataType: "json",
                    async: false,
                    error: function (r) {
                        alert(r);
                    }
                });
                Methods.InitJson = InitJson;
                return Methods;
            },
            Clear: function () {
            },
            SelectBoxInit: function (Point) {
                if (!Point) {
                    Point = Methods.SelectBoxPoint;
                } else {
                    Methods.SelectBoxPoint = Point;
                }
                ;
                Point = $(Point);
                console.log('SelectBoxInit');
                for (i in Methods.SelectBox) {
                    var SelectBox = Methods.SelectBox[i];
                    Point.append('<option value="' + SelectBox['_id'] + '" style=" background:' + SelectBox['Color'] + '; ">' + SelectBox['Title'] + '</option>');
                }
                ;
                return Methods;
            },
            GetSelectBox: function () {
                Point = Methods.SelectBoxPoint;
                Methods.CurrentSelectBox = Methods.SelectBox[$(Point)[0].options[$(Point)[0].selectedIndex].value];
                return Methods;
            },
            GetDayData: function (Data, Month, Reset) {
                if (Reset == true) {
                    Methods.DayPointer = 0;
                } else {
                    try {
                        var $DayData = Data[Month][++Methods.DayPointer];
                        $DayData['Color'] = Methods.SelectBox[$DayData['_id']]['Color'];
                        return $DayData;
                    } catch ($e) {
                        return {
                            '_id': '',
                            'Color': '',
                            'timestamp': ''
                        };
                    }
                    ;
                }
                ;
            },
            SubmitEvent: function (Pointer, ServerURL, Parameter) {
                $(Pointer)
                    .click(function (e) {
                        Methods.SubmitData(ServerURL, Parameter);
                    });
                return Methods;
            },
            SubmitData: function (ServerURL, Parameter) {
                Methods.$DAta = new Array();
                //Methods.$DAta[Methods.CurrentYear]=new Object();
                //Methods.$DAta[Methods.CurrentYear]['Initialize']=new Array();
                Methods.Document.find('tr')
                    .each(function (indexMonth, element) {
                        if (indexMonth == 0) {
                            return;
                        }
                        ;
                        Methods.$TempDAta = new Array();
                        $(element)
                            .find('td')
                            .each(function (indexDay, elementDay) {
                                if (indexDay == 0) return;
                                /*if(typeof  Methods.$DAta[Methods.CurrentYear]['Initialize'][indexMonth]=="undefined")
                                 {
                                 Methods.$DAta[Methods.CurrentYear]['Initialize'][indexMonth]=new Array();
                                 };*/
                                var myobject = new Object();
                                //Methods.Console.log($(elementDay).attr('_id'));
                                myobject._id = $(elementDay)
                                    .attr('_id');
                                myobject.timestamp = $(elementDay)
                                    .attr('timestamp');
                                myobject.day = elementDay.innerHTML;
                                myobject.Month = indexMonth;
                                myobject.Year = Methods.CurrentYear;
                                myobject.indexDay = indexDay;
                                Methods.$TempDAta.push(myobject);
                            });
                        Methods.$DAta.push(Methods.$TempDAta);
                    });
                Buffer = Methods.InitJson;
                Buffer[Methods.CurrentYear]['Initialize'] = Methods.$DAta;
                //Methods.Console.log(Methods.$DAta);
                //Methods.Console.log(Methods.InitJson[Methods.CurrentYear]);
                $Temp = Buffer[Methods.CurrentYear]['SelectBox'];
                $TempRep = new Array();
                for (i in $Temp) {
                    $TempRep.push($Temp[i]);
                }
                Buffer[Methods.CurrentYear]['SelectBox'] = $TempRep;
                $.ajax({
                    type: 'POST',
                    url: ServerURL,
                    data: {
                        operation: "Save_Year_Data_Calendar",
                        year: Methods.CurrentYear,
                        Data: JSON.stringify(Buffer)
                    },
                    //contentType:"application/json;charset=utf-8",
                    success: function (r) {
                        if (r.Status == true) {
                            Methods.Console.log(r.Message);
                        } else {
                            Methods.Console.log(r.Error);
                        }
                        ;
                    },
                    //dataType: "json",
                    async: false
                });
                return Methods;
            }, getData: function (ServerURL, Parameter) {
                Methods.$DAta = new Array();
                //Methods.$DAta[Methods.CurrentYear]=new Object();
                //Methods.$DAta[Methods.CurrentYear]['Initialize']=new Array();
                Methods.Document.find('tr')
                    .each(function (indexMonth, element) {
                        if (indexMonth == 0) {
                            return;
                        }
                        ;
                        Methods.$TempDAta = new Array();
                        $(element)
                            .find('td')
                            .each(function (indexDay, elementDay) {
                                if (indexDay == 0) return;
                                /*if(typeof  Methods.$DAta[Methods.CurrentYear]['Initialize'][indexMonth]=="undefined")
                                 {
                                 Methods.$DAta[Methods.CurrentYear]['Initialize'][indexMonth]=new Array();
                                 };*/
                                var myobject = new Object();
                                //Methods.Console.log($(elementDay).attr('_id'));
                                myobject._id = $(elementDay)
                                    .attr('_id');
                                myobject.timestamp = $(elementDay)
                                    .attr('timestamp');
                                myobject.day = elementDay.innerHTML;
                                myobject.Month = indexMonth;
                                myobject.Year = Methods.CurrentYear;
                                myobject.indexDay = indexDay;
                                Methods.$TempDAta.push(myobject);
                            });
                        Methods.$DAta.push(Methods.$TempDAta);
                    });
                Buffer = Methods.InitJson;
                Buffer[Methods.CurrentYear]['Initialize'] = Methods.$DAta;
                //Methods.Console.log(Methods.$DAta);
                //Methods.Console.log(Methods.InitJson[Methods.CurrentYear]);
                $Temp = Buffer[Methods.CurrentYear]['SelectBox'];
                $TempRep = new Array();
                for (i in $Temp) {
                    $TempRep.push($Temp[i]);
                }
                Buffer[Methods.CurrentYear]['SelectBox'] = $TempRep;
                return JSON.stringify(Buffer);
                ;
            },
            Initialize: function (Year, Data, Header, Body, Footer) {
                console.log('Initialize');
                if (!Year) {
                    Year = 2017;
                }
                ;
                if (!Data) {
                    Data = Methods.InitJson;
                }
                ;
                if (Data == false) {
                    Methods.Console.log("Con't Initialize Data");
                    return;
                }
                ;
                $Temp = Data[Year]['SelectBox'];
                $TempRep = new Object();
                for (i in $Temp) {

                    if (!$TempRep[$Temp[i]._id]) {
                        $TempRep[$Temp[i]._id] = new Object();
                    }
                    $TempRep[$Temp[i]._id] = $Temp[i];

                }
                Data[Year]['SelectBox'] = $TempRep;
                if (Year in Data) {
                    if ('SelectBox' in Data[Year]) {
                        Methods.SelectBox = Data[Year]['SelectBox'];
                        Methods.SelectBoxInit();
                    }
                    ;
                }
                ;
                if (Methods.Jalali == true) {
                    var JalaliYear = Jalali.convert(Year, 5, 0)['y'];
                } else {
                    var JalaliYear = Year;
                }
                ;
                //console.log(JalaliYear);
                var Buffer = '';
                var Maxweekday = 0,
                    MaxweekdayMonth = 1;
                //for()
                $Temp = Data[Year]['Initialize'];
                $TempRep = new Object();
                for (i in $Temp) {
                    $TempRepInner = new Object();
                    var $TempMonth;
                    for (j in $Temp[i]) {
                        $TempRepInner[$Temp[i][j].indexDay] = $Temp[i][j];
                        $TempMonth = $Temp[i][j].Month;
                    }
                    if (!$TempRep[$TempMonth]) {
                        $TempRep[$TempMonth] = new Object();
                    }
                    $TempRep[$TempMonth] = $TempRepInner;

                }
                Data[Year]['Initialize'] = $TempRep;

                for (var Month = 1; Month < 13; Month++) {
                    Methods.GetDayData(Data[Year]['Initialize'], Month, true);
                    if (Methods.Jalali == true) {
                        var MonthAlphabetic = Jalali.alphabetic(Month);
                    } else {
                        var MonthAlphabetic = Gregorian.alphabetic(Month);
                    }
                    ;
                    if (Methods.Jalali == true) {
                        if (Month < 7) {
                            LastDay = 31;
                        } else if (Month < 12) {
                            LastDay = 30;
                        } else {
                            var Kabise = JalaliYear / 4;
                            if (Kabise - Math.floor(Kabise) > 0) {
                                LastDay = 30;
                            } else {
                                LastDay = 29;
                            }
                        }
                        ;
                    } else {
                        if (Month == 1 || Month == 3 || Month == 5 || Month == 7 || Month == 8 || Month == 10 || Month == 12) {
                            LastDay = 31;
                        } else if (Month == 2) {
                            var Kabise = JalaliYear / 4;
                            if (Kabise - Math.floor(Kabise) > 0) {
                                LastDay = 28;
                            } else {
                                LastDay = 29;
                            }
                        } else if (Month == 4 || Month == 6 || Month == 9 || Month == 11) {
                            LastDay = 30;
                        }
                    }
                    ;
                    if (Methods.Jalali == true) {
                        var LastDayAlphabetic = Jalali.persianDay(LastDay);
                    } else {
                        var LastDayAlphabetic = Gregorian.GregorianDay(LastDay);
                    }
                    if (Header) {
                        Buffer += Header({
                            'Year': Year,
                            'Month': Month,
                            'MonthAlphabetic': MonthAlphabetic,
                            'LastDay': LastDay,
                            'LastDayAlphabetic': LastDayAlphabetic
                        });
                    } else {
                        Buffer += '<tr><td class="UnSelectable">' + MonthAlphabetic + '</td>';
                    }
                    ;
                    if (Methods.Jalali == true) {
                        var j = persiana_to_jd(JalaliYear, Month, 1 + 0.5);
                    } else {
                        var j = gregorian_to_jd(JalaliYear, Month, 1);
                    }
                    weekday = jwday(j);
                    if (weekday > Maxweekday) {
                        Maxweekday = weekday;
                        MaxweekdayMonth = Month;
                    }
                    for (var NonDay = 0; NonDay < weekday + 1 && weekday + 1 < 7; NonDay++) {
                        $DayData = Methods.GetDayData(Data[Year]['Initialize'], Month);
                        Buffer += '<td class="UnSelectable"  ></td>';
                    }
                    for (var Day = 1; Day <= LastDay; Day++) {
                        //console.log(Day);
                        //console.log(weekday);
                        $DayData = Methods.GetDayData(Data[Year]['Initialize'], Month);
                        if (Body) {
                            Buffer += Body({
                                'Year': Year,
                                'Month': Month,
                                'MonthAlphabetic': MonthAlphabetic,
                                'LastDay': LastDay,
                                'LastDayAlphabetic': LastDayAlphabetic,
                                'Day': Day,
                                'DayAlphabetic': (Methods.Jalali == true) ? Jalali.persianDay(Day) : Gregorian.GregorianDay(Day),
                                'DayData': $DayData
                            });
                        } else {
                            Buffer += '<td _id="' + $DayData['_id'] + '" timestamp="' + $DayData['timestamp'] + '" style="background-color:' + $DayData['Color'] + '">' + Day + '</td>';
                        }
                        ;
                    }
                    ;
                    for (var NonDay = LastDay + weekday + 1; NonDay < 37; NonDay++) {
                        $DayData = Methods.GetDayData(Data[Year]['Initialize'], Month);
                        Buffer += '<td class="UnSelectable" ></td>';
                    }
                    if (weekday + 1 > 6 && LastDay > 30) {
                        for (var NonDay = 1; NonDay < weekday + 1; NonDay++) {
                            $DayData = Methods.GetDayData(Data[Year]['Initialize'], Month);
                            Buffer += '<td class="UnSelectable" ></td>';
                        }
                    } else if (weekday + 1 > 6) {
                        for (var NonDay = 1; NonDay < weekday + 2; NonDay++) {
                            $DayData = Methods.GetDayData(Data[Year]['Initialize'], Month);
                            Buffer += '<td class="UnSelectable" ></td>';
                        }
                    }
                    if (Footer) {
                        Buffer += Footer({
                            'Year': Year,
                            'Month': Month,
                            'MonthAlphabetic': MonthAlphabetic,
                            'LastDay': LastDay,
                            'LastDayAlphabetic': LastDayAlphabetic
                        });
                    } else {
                        Buffer += '</tr>';
                    }
                    ;
                }
                ;
                var Buffer1 = '<tr ><td class="UnSelectable YearChanger">&nbsp;&nbsp;<span class="UnSelectable prev" prevyear="' + (JalaliYear - 1) + '">&lt;</span>&nbsp;&nbsp;<span class="UnSelectable Year">' + JalaliYear + '</span>&nbsp;&nbsp;<span class="UnSelectable Next" nextyear="' + (parseInt(JalaliYear) + 1) + '">&gt;</span>&nbsp;&nbsp;</td>';
                for ($WeekDAy = -(Maxweekday); $WeekDAy < 31; $WeekDAy++) {
                    if (Methods.Jalali == true) {
                        var j = persiana_to_jd(JalaliYear, MaxweekdayMonth, $WeekDAy + 0.5);
                    } else {
                        var j = gregorian_to_jd(JalaliYear, MaxweekdayMonth, $WeekDAy);
                    }
                    $weekday = jwday(j);
                    Buffer1 += '<td class="UnSelectable">' + ((Methods.Jalali == true) ? PERSIAN_WEEKDAYS_MINI[$weekday] : Gregorian.jdays_Mini[$weekday]) + '</td>';
                }
                Buffer1 += '</tr>';
                Buffer = Buffer1 + Buffer;
                Methods.Document[0].innerHTML = Buffer;
                Methods.Document.find('td')
                    .click(function (e) {
                        if ($(e.target)
                            .hasClass('UnSelectable')) {
                            $(e.target)
                                .removeClass('ui-selected');
                            return;
                        }
                        ;
                        Methods.GetSelectBox();
                        $SelectedData = Methods.CurrentSelectBox;
                        $(e.target)
                            .css("background", $SelectedData['Color'])
                            .attr("_id", $SelectedData['_id'])
                            .attr("timestamp", $SelectedData['timestamp']);
                    });
                if (Methods.AfterInit) {
                    Methods.AfterInit(Methods);
                }
                ;
                return Methods;
            },
            UnSelectAll: function () {
                Methods.Document.find('td').removeAttr("style").removeAttr("_id").removeAttr("timestamp");
            },
            selectable: function (Data) {
                Methods.Document.selectable({
                    filter: Data['filter'],
                    delay: Data['delay'],
                    start: function (event, ui) {
                        Data['start'](event, ui, Methods);
                    },
                    selecting: function (event, ui) {
                        Data['selecting'](event, ui, Methods);
                        if ($(ui.selecting)
                            .hasClass('UnSelectable')) {
                            $(ui.selected)
                                .removeClass('ui-selected');
                            return;
                        }
                        ;
                        Methods.GetSelectBox();
                        $SelectedData = Methods.CurrentSelectBox;
                        $(ui.selecting)
                            .attr("selectboxrollback_id", $(ui.selecting)
                                .attr("_id"));
                        $(ui.selecting)
                            .attr("selectboxrollbacktimestamp", $(ui.selecting)
                                .attr("timestamp"));
                        $(ui.selecting)
                            .css("background", $SelectedData['Color'])
                            .attr("_id", $SelectedData['_id'])
                            .attr("timestamp", $SelectedData['timestamp']);
                    },
                    unselecting: function (event, ui) {
                        Data['unselecting'](event, ui, Methods);
                        //$(ui.unselecting).removeAttr("style").removeAttr("_id").removeAttr("timestamp");
                        try {
                            var $color = Methods.SelectBox[$(ui.unselecting)
                                .attr("selectboxrollback_id")]['Color'];
                        } catch ($e) {
                            var $color = '';
                        }
                        $(ui.unselecting)
                            .css("background", $color)
                            .attr("_id", $(ui.unselecting)
                                .attr("selectboxrollback_id"))
                            .attr("timestamp",
                                $(ui.unselecting)
                                    .attr("selectboxrollbacktimestamp"));
                    },
                    selected: function (event, ui) {
                        Data['selected'](event, ui, Methods);
                        $(ui.selected)
                            .removeClass('ui-selected');
                    },
                    unselected: function (event, ui) {
                        Data['unselected'](event, ui, Methods);
                        //$(ui.unselecting).removeAttr("style").removeAttr("_id").removeAttr("timestamp");
                        try {
                            var $color = Methods.SelectBox[$(ui.unselecting)
                                .attr("selectboxrollback_id")]['Color'];
                        } catch ($e) {
                            var $color = '';
                        }
                        $(ui.unselecting)
                            .css("background", $color)
                            .attr("_id", $(ui.unselecting)
                                .attr("selectboxrollback_id"))
                            .attr("timestamp",
                                $(ui.unselecting)
                                    .attr("selectboxrollbacktimestamp"));
                    },
                    stop: function (event, ui) {
                        Data['stop'](event, ui, Methods);
                        $(ui.selected)
                            .removeClass('ui-selected');
                    }
                });
                return Methods;
            }
        };
        return Methods;
    };
}($));