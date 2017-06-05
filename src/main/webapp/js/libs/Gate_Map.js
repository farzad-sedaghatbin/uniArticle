(function (o) {
    o.fn.VisualTrafficMonitor = function () {
        var Document = this;
        var Methods = {
            this: Document,
            version:'1.5.30.102',
            Document: Document,
            Mode:'Editor',
            NumView: 20,
            NumViewInMore: 10,
            NumInView: 0,
            Count: 0,
            JsonEnable:false,
            LastCTimeInView: 0,
            NewerNewsCTimeInView: 0,
            InitJson: {},
            Get_LastInitJson:{},
            Refresh_Duration: 10,
            Refresh_Duration_id: 0,
            UserActive: false,
            NewsAlarmEnable: false,
            NotificationEnable: false,
            Filter_id: {},
            NotificationTimeOut: 7000,
            Refresh_Lock: false,
            CurrentShapeSelectBox: {},
            CurrentShapeSelected:'',
            CurrentShapeOnMap:[],
            ShapeSelectBoxRollBack: {},
            ShapeSelectBoxPoint: '#VTMShape',
            CurrentNodesSelectBox: {},
            NodesSelectBoxRollBack: {},
            NodesSelectBoxPoint: '#VTMNodes',
            CurrentAlertSelectBox: {},
            AlertSelectBoxRollBack: {},
            AlertSelectBoxPoint: '#VTMAlert',
            AfterGetAlertSelectBox:'',
            ElementParameter:{},
            MapEditorDoStack:{},
            BeforInsertX3DElement:'',
            AfterSelectInsertX3DElement:'',
            MouseMoveCallBack:'',
            MouseMoveNodeCallBack:'',
            AfterInsertX3DElement:'',
            DeleteElement:false,
            cpnt: '#cpnt',
            bar: '#bar',
            SelectedInsertX3DElement:'Cylinder',
            rubberBandTest:false,
            AfterDeleteElement:'',
            Stuff:{},
            runtime:'',
            pos:'',
            rot:'',
            mat:'',
            Console: {
                log: function (Error) {
                    console.log(Error);
                }
            },
            SetFilter_id: function (Filter_id) {
                Methods.Filter_id = Filter_id;
                return Methods;
            },
            EnableNewsAlarm: function () {
                Methods.NewsAlarmEnable = true;
                $.cookie(Methods.Document.selector + 'soundAlarm', '1');
                return Methods;
            },
            NewsAlarmState: function () {
                return Methods.NewsAlarmEnable;
            },
            DisableNewsAlarm: function () {
                Methods.NewsAlarmEnable = false;
                $.cookie(Methods.Document.selector + 'soundAlarm', '2');
                return Methods;
            },
            NewsAlarm: function () {
                if (!Methods.UserActive && Methods.NewsAlarmEnable) {
                    document.getElementById('soundAlarm').play();
                };
            },
            Notification: function (title, Body, timeout,icon,body) {
                if (!Methods.UserActive && Methods.NotificationEnable) {
                    if(!icon)icon = "images/alarm_check.png";
                    if(!body)body = "";
                    $.desknoty({
                        icon: icon,
                        title: title,
                        body: body,
                        timeout: timeout
                    });
                };
            },
            EnableNotification: function () {
                $.desknoty({
                    title: 'سیستم اطلاع رسانی',
                    body: 'فعال شد.',
                    icon: "images/notif-big1.png"
                });
                Methods.NotificationEnable = true;
                $.cookie(Methods.Document.selector + 'Notification', '1');
                return Methods;
            },
            NotificationState: function () {
                return Methods.NotificationEnable;
            },
            DisableNotification: function () {
                $.desknoty({
                    title: 'سیستم اطلاع رسانی',
                    body: 'غیر فعال شد.',
                    icon: "images/notif-big2.png"
                });
                Methods.NotificationEnable = false;
                $.cookie(Methods.Document.selector + 'Notification', '2');
                return Methods;
            },
            generateUid : function (separator) {
                /// <summary>
                ///    Creates a unique id for identification purposes.
                /// </summary>
                /// <param name="separator" type="String" optional="true">
                /// The optional separator for grouping the generated segmants: default "-".
                /// </param>

                var delim = separator || "-";
                function S4() {
                    return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
                }
                return (S4() + S4() + delim + S4() + delim + S4() + delim + S4() + delim + S4() + S4() + S4());
            },
            Ajax: function (ServerURL, Operation, Parameter, CallBack) {

                //Get_Last
                //Get_More
                if (!Parameter) {
                    Parameter = {};
                };
                if (Methods.Filter_id) {
                    Parameter["Filter_id"] = Methods.Filter_id;
                };
                if (Operation == 'initialize') {
                    var InitJson = {};
                    Parameter["operation"] = 'initialize';
                    Parameter["Mode"] = Methods.Mode;
                } else if (Operation == 'Get_Last') {
                    var Get_LastInitJson = {};
                    Parameter["operation"] = 'Get_Last';
                    Parameter["NewerNewsCTimeInView"] = Methods.NewerNewsCTimeInView;
                } else if (Operation == 'Get_More') {
                    Parameter["operation"] = 'Get_More';
                    Parameter["NumView"] = Methods.NumViewInMore;
                    Parameter["From"] = Methods.NumInView;
                    Parameter["LastCTimeInView"] = Methods.LastCTimeInView;
                };
                var complete = function (evt, res) {
                    if (evt.target.readyState == 4) {
                        if (evt.target.status == 200 || evt.target.status == 304) {
                            var response = responseText(evt.target);
                            if (response == '') return;
                            var r;
                            try {
                                var REC = responseText(evt.target);
                                if(Methods.JsonEnable)
                                {
                                    r = $.parseJSON(REC);
                                }else
                                {
                                    r = $.xml2json(REC);
                                };
                            } catch (e) {
                                return;
                            }
                            if (r.status == true || r.status == 'true') {

                                if (r.operation == 'Get_More') {
                                    Methods.LastCTimeInView = r.LastCTimeInView - 1;
                                }
                                if (r.operation == 'Get_Last') {
                                    Get_LastInitJson = r;
                                    Methods.NewerNewsCTimeInView = r.NewerNewsCTimeInView + 1;
                                }
                                if (r.operation == 'Initialize') {
                                    InitJson = r;
                                    Methods.NewerNewsCTimeInView = r.NewerNewsCTimeInView + 1;
                                    Methods.LastCTimeInView = r.NewerNewsCTimeInView + 1;
                                }
                                Methods.Filter_id = r.Filter_id;
                                Methods.Count = r.Count;
                            } else {
                                Methods.Console.log(r.error);
                                InitJson = false;
                                Get_LastInitJson = false;
                            };
                            if (r.operation == 'Initialize') {
                                $.cookie(Methods.Document.selector + 'NewerNewsCTimeInView', Methods.NewerNewsCTimeInView);
                                $.cookie(Methods.Document.selector + 'LastCTimeInView', Methods.LastCTimeInView);
                                Methods.InitJson = InitJson;
                            };
                            if (r.operation == 'Get_Last') {
                                Methods.Get_LastInitJson = Get_LastInitJson;
                            };
                            if (CallBack && $.isFunction(CallBack)) {
                                CallBack(false);
                            };
                        } else {
                            if (CallBack && $.isFunction(CallBack)) {
                                CallBack(true);
                            };
                        };
                    };
                };
                var form = new formdata();
                $Ah = form.init();
                for ($K in Parameter) {
                    form.appendData($Ah, $K, Parameter[$K]);
                }
                form.send($Ah, ServerURL, 'POST', null, null, null, null, complete);
                return Methods;
            },
            Clear: function () {
                Methods.Document.empty();
                return Methods;
            },
            Refresh: function (ServerURL, Parameter, Filter_id) {
                if (Methods.Refresh_Duration_id != 0) {
                    clearInterval(Methods.Refresh_Duration_id);
                    console.log(Methods.Refresh_Duration_id);
                };
                if ($(Methods.Document.selector).attr("rdid")) {
                    clearInterval(parseInt($(Methods.Document.selector).attr("rdid")));
                    console.log(parseInt($(Methods.Document.selector).attr("rdid")));
                };
                Methods.Filter_id = Filter_id;

                Methods.Refresh_Duration_id = setInterval(function () {
                    Methods.Get_Last(ServerURL, Parameter);
                }, Methods.Refresh_Duration * 1000);
                $(Methods.Document.selector).attr("rdid", Methods.Refresh_Duration_id);
                return Methods;
            },
            Cancel_Refresh: function () {
                Refresh_Duration_id = clearInterval(Methods.Refresh_Duration_id);
                return Methods;
            },
            SM:function(Editor){
                if(!Editor){
                    Methods.Mode='Viewer';
                }else{
                    Methods.Mode='Editor';
                };
                return Methods;
            },
            InitX3D: function (Stuff,SizeX, SizeY) {
                Methods.Stuff=Stuff;
                if (!Stuff){
                    Stuff ='';
                }else{
                    Stuff =Stuff+'.X3DElementPointer(event);';
                };
                if (!SizeX) SizeX = $(window).width();
                if (!SizeY) SizeY = $(window).height() - 100;
                var mousewheelevt=(/Firefox/i.test(navigator.userAgent))? "DOMMouseScroll" : "mousewheel" //FF doesn't recognize mousewheel as of FF3.x
                if (document.body.attachEvent) //if IE (and Opera depending on user setting)
                    var mousewheel="on"+mousewheelevt+"=\""+Methods.Stuff+".MouseWheel(event);\"";
                else if (document.body.addEventListener) //WC3 browsers
                    var mousewheel=mousewheelevt+"=\""+Methods.Stuff+".MouseWheel(event);\"";
                var OnResize = Methods.Stuff+".OnResize(event);";
                var Buffer = '<X3D xmlns="http://www.web3d.org/specifications/x3d-namespace" id="boxes" ' + '  showStat="false" showLog="false" width="' + SizeX + '" height="' + SizeY + '" style="float:left;"' + '  onmousedown="start(event);" onresize="'+OnResize+'" onmouseup="stop(event);" onmousemove="move(event);" >' + '  <Scene DEF="scene">' + '     <Background skyColor=\'0.7 0.7 0.7\'  />' + '     <Group id=\'kloppe\'' + ' '+mousewheel+'       onmousemove="' +
                    // '        document.getElementById(\'bar\').setAttribute(\'translation\', event.hitPnt);'+
                    //'        document.getElementById(\'pc\').setAttribute(\'set_destination\', event.hitPnt);'+
                    ' '+Stuff+'" ' +
                    'onclick="'+Stuff+'"'+
                    //'        onclick="document.getElementById(\'oc\').setAttribute(\'set_destination\', \'0 1 0 0\');'+
                    //'        document.getElementById(\'cc\').setAttribute(\'set_destination\', \'0 1 0\');"'+
                    '        >' + '     </Group>' + '     <transform id="bar" DEF="foo" scale=".15 .4 .15">' + '        <Shape ispickable="false" DEF="float">' + '           <Appearance>' + '              <Material USE="txtMat" />' + '           </Appearance>' + '           <Cone />' + '        </Shape>' + '     </transform>' + '     <transform Translation="0 0 0">' + '        <Shape ispickable="false" DEF="line">' +
                    /*'           <!--IndexedLineSet coordIndex="0 1 -1 0 2 -1 0 3 -1">'+
                     '              <Coordinate id="cpnt" point="0.0 0.0 0.0, 0.0 0.0 1.0, 1.0 0.0 0.0, 0.0 1.0 0.0"/>'+
                     '              <Color color="1 1 1, 1 1 0, 1 0 1, 0 1 1"/>'+
                     '              </IndexedLineSet-->'+*/
                    '           <IndexedLineSet coordIndex="0 1 -1">' + '              <Coordinate id="cpnt" point="0.0 0.0 0.0, 0.0 1.0 0.0"/>' + '              <Color color="0 1 1, 1 0 1"/>' + '           </IndexedLineSet>' + '        </Shape>' + '     </transform>' + '     <Viewpoint id="aView" centerOfRotation="0 0 0" position="0 0 120" orientation="0 1 0 0" />' +
                    /*'     <!--'+
                     '        <OrientationChaser id="oc" DEF="ochaser" duration="1.5" initialDestination="1 0 0 3.14" initialValue="0 1 0 0" />'+
                     '        <PositionChaser id="pc" DEF="pchaser" duration="1.5" initialDestination="-2 3 2" initialValue="0 0 0" />'+
                     '        <ScalarChaser id="sc" DEF="sdamper" duration="1.5" initialDestination="0" initialValue="1" />'+
                     '        <ColorChaser id="cc" DEF="cdamper" duration="1.5" initialDestination="1 1 0" initialValue="0 1 0" />-->'+*/
                    '     <OrientationDamper id="oc" DEF="ochaser" tau="1.0" order="1" initialDestination="1 0 0 3.14" initialValue="0 1 0 0" />' + '     <PositionDamper id="pc" DEF="pchaser" tau="1.0" order="1" initialDestination="-2 3 2" initialValue="0 0 0" />' + '     <ScalarDamper id="sc" DEF="sdamper" tau="1.0" order="1" initialDestination="0" initialValue="1" />' + '     <ColorDamper id="cc" DEF="cdamper" tau="1.0" order="1" initialDestination="1 1 0" initialValue="0 1 0" />' +
                    //'     <!--ROUTE fromNode=\'pchaser\' fromField=\'value_changed\' toNode=\'foo\' toField=\'translation\'/-->'+
                    '     <ROUTE fromNode=\'ochaser\' fromField=\'value_changed\' toNode=\'coneTrafo\' toField=\'rotation\'/>' + '     <ROUTE fromNode=\'cdamper\' fromField=\'value_changed\' toNode=\'coneMat\' toField=\'diffuseColor\'/>' + '     <ROUTE fromNode=\'sdamper\' fromField=\'value_changed\' toNode=\'coneMat\' toField=\'transparency\'/>' + '  </Scene>' + '</X3D>';

                if (Buffer == 'undefined') return Methods;
                //Methods.Document.append(Buffer);
                $(Buffer).appendTo(Methods.Document.selector);
                var boxes=$(Methods.Document.selector).find(">.boxes");
                //Methods.runtime = boxes[0].runtime;
                boxes.find('#aView').bind('viewpointChanged', Methods.viewFunc, false);
                return Methods;
            },
            viewFunc :function(evt) {
                Methods.pos = evt.position;
                Methods.rot = evt.orientation;
                Methods.mat = evt.matrix;
                document.getElementById('anotherView').setAttribute('position', Methods.pos.x+' '+Methods.pos.y+' '+Methods.pos.z);
                document.getElementById('anotherView').setAttribute('orientation', Methods.rot[0].x+' '+Methods.rot[0].y+' '+Methods.rot[0].z+' '+Methods.rot[1]);

            },
            SelectInsertX3DElement: function () {
                // Methods.SelectedInsertX3DElement = Element;
                Point = Methods.ShapeSelectBoxPoint;
                Methods.SelectedInsertX3DElement = Methods.ShapeSelectBox[$(Point)[0].options[$(Point)[0].selectedIndex].value];
                if(Methods.AfterSelectInsertX3DElement){
                    Methods.AfterSelectInsertX3DElement(Methods);
                }
                return Methods;
            },
            BuildShape: function(Template,ShapeObj,$ElmName,$Parameter){
                //var $Element = Template.find('>'+$ElmName);
                //if($Element.length == 0){
                var $Element = $('<'+$ElmName+'></'+$ElmName+'>');
                $Element.appendTo(Template);
                //};
                //$Element = Template.find('>'+$ElmName);
                if($ElmName == 'transform'){
                    $Element.attr('_id',$Parameter['_id']);
                    if('node_id' in $Parameter){
                        $Element.attr('node_id',$Parameter['node_id']);
                    }
                    if('id' in $Parameter){
                        $Element.attr('id',$Parameter['id']);
                    }
                    if('alerttype' in $Parameter){
                        $Element.attr('alerttype',$Parameter['alerttype']);
                    }

                };
                for(var $Attr in ShapeObj){
                    if(typeof ShapeObj[$Attr] === 'string' ) {

                        if($Attr == 'ispickable' && ShapeObj['ispickable'] == 'true'){
                            if($Parameter['OverWrite'] && ('onclick' in $Parameter['OverWrite'])){
                                $Element.attr('onclick',$Parameter['OverWrite']['onclick']);
                            }else{
                                if(!ShapeObj['onclick']){

                                    $Element.attr('onclick',$Parameter['onclick']);
                                }else{

                                    $Element.attr('onclick',ShapeObj['onclick']);
                                }
                            }
                        }
                        if($Parameter['OverWrite'] && ($Attr in $Parameter['OverWrite'])){
                            if($Parameter['OverWrite'][$Attr] == 'Auto'){
                                $Element.attr($Attr,$Parameter[$Attr]);
                            }else{
                                $Element.attr($Attr,$Parameter['OverWrite'][$Attr]);
                            }
                        }else{

                            if(ShapeObj[$Attr] == 'Auto'){
                                $Element.attr($Attr,$Parameter[$Attr]);
                            }else{
                                //console.log(ShapeObj);
                                //console.log($Attr);
                                var Value = ShapeObj[$Attr].replace(/\{\$(.*?)\}/g,function(m, string){
                                    if($Parameter[string]){
                                        return $Parameter[string];
                                    }else{
                                        return '';
                                    }
                                });
                                $Element.attr($Attr,Value);
                            }
                        }
                    }else{

                        var $Param = $Parameter;
                        if($Parameter['OverWrite'] && $Parameter['OverWrite'][$Attr]){
                            jQuery.extend($Param['OverWrite'] , $Parameter['OverWrite'][$Attr]);
                        }else{
                            $Param['OverWrite'] = null;
                        }
                        $(Methods.BuildShape($Element,ShapeObj[$Attr],ShapeObj[$Attr]['nodename'],$Param)).appendTo($Element);
                    }
                };

                return $Element;
            },
            setView :function(evt) {
                var e = document.getElementById("boxes");
                e.runtime.resetExamin();
                //e.runtime.canvas.doc._viewarea._scene.getViewpoint().setView(mat);
                //return;
                document.getElementById('aView').setAttribute(
                    'position', pos.x+' '+pos.y+' '+pos.z);
                document.getElementById('aView').setAttribute(
                    'orientation', rot[0].x+' '+rot[0].y+' '+rot[0].z+' '+rot[1]);
            },
            setMode :function() {
                Methods.rubberBandTest = !Methods.rubberBandTest;
                if (Methods.rubberBandTest)
                    runtime.noNav();
                else
                    runtime.examine();
            },
            MouseMove:function(e,Node){
                if(this.MouseMoveCallBack){
                    this.MouseMoveCallBack(e,Node);
                };
            },
            MouseMoveNode:function(e,Node){
                if(this.MouseMoveNodeCallBack){
                    this.MouseMoveNodeCallBack(e,Node);
                };
            },
            OnResize:function(event){
                $(Methods.Document.selector + ' X3D').attr("width",$(window).width());
                $(Methods.Document.selector + ' X3D').attr("height",$(window).height() - 100);
            },
            MouseWheel:function(event){

            },
            SetAsRemove:function(event){
                if(Methods.DeleteElement == true){
                    Methods.DeleteElement = false;
                }else{
                    Methods.DeleteElement = true;
                };
            },
            SelectElement:function(event){
                $(Methods.Document.selector + ' Appearance').each(function(index, element) {
                    element.highlight(false,'255 0 0');
                });
                Methods.CurrentShapeSelected = event;
                if(Methods.DeleteElement == true){
                    Methods.DeleteElement = false;
                    var Node = $(Methods.CurrentShapeSelected.hitObject).parent();
                    var Node_id = Node.attr('node_id');
                    Node.attr('render', false);
                    //Node.remove();
                    //Node.remove();
                    //var sys =$(Methods.Document.selector + ' #boxes')[0];
                    //sys.parent.doc.needRender = true;
                    //sys.parent.tick();
                    if(Methods.AfterDeleteElement){
                        Methods = Methods.AfterDeleteElement(Methods,Node_id);
                    }
                }else{
                    try{
                        event.hitObject.highlight(true,'255 0 0');
                    }catch($e){
                    }
                }
                $(Methods.Document.selector + ' #kloppe [render="false"]').each(function(index, element) {
                    element.parentElement.removeChild(element);
                });
                //console.log(Methods.CurrentShapeSelected.hitObject);
            },
            PoPupAlert: function (Node_id,$Data){
                var element = $(Methods.Document.selector + ' #kloppe').find('[node_id="'+Node_id+'"][node="true"]');
                if(element.length == 0)return;
                var AlertType = element.attr('alerttype');
                var AlertObj = Methods.AlertSelectBox[AlertType];

                var Node = AlertObj['node'];
                var Template = AlertObj['pattern'];
                var $Parameter={}
                var ExistTemplate = $(Methods.Document.selector + ' #kloppe').find('[node_id="'+Node_id+'"][pattern="true"][popup="true"]');
                if(ExistTemplate.length == 0){
                    var templ = $('<element></element>');
                    if('id' in Template){
                        $Parameter['id'] = Template['id'];
                    }else{
                        $Parameter['id'] = Methods.generateUid();
                    };

                    for(var $Attr in Template){
                        if(typeof Template[$Attr] !== 'string' ) {
                            Template[$Attr]['popup'] = 'true';
                            Template[$Attr]['node_id'] = Node_id;
                            Buffer = Methods.BuildShape(templ,Template[$Attr],Template[$Attr]['nodename'],$Parameter);
                        }
                    }

                    var translation = element.attr('translation').trim().split(/[ |,]+/g);

                    $(Buffer).attr('translation',(parseInt(translation[0])+parseInt(AlertObj['translationx']))+' '+(parseInt(translation[1])+parseInt(AlertObj['translationy']))+' '+(parseInt(translation[2])+parseInt(AlertObj['translationz'])));
                    var rotation = element.attr('Rotation').trim().split(/[ |,]+/g);
                    //console.log(element.attr('Rotation'));
                    //console.log(rotation[0]+' '+rotation[1]+' '+rotation[2]+' '+rotation[3]);
                    $(Buffer).attr('Rotation',rotation[0]+' '+rotation[1]+' '+rotation[2]+' '+rotation[3]).appendTo(Methods.Document.selector + ' #kloppe');
                }else{
                    if('id' in Template){
                        $Parameter['id'] = Template['id'];
                    }else{
                        $Parameter['id'] = Methods.generateUid();
                    };
                };
                ExistTemplate = $(Methods.Document.selector + ' #kloppe').find('[node_id="'+Node_id+'"][pattern="true"][popup="true"]');

                var child = ExistTemplate.children();
                var NumNode = child.length;
                templ= $('<element></element>');
                $Parameter = $Data;
                for(var $Attr in Node){
                    if(typeof Node[$Attr] !== 'string' ) {
                        if('id' in Node){
                            $Parameter['id'] = Node['id'];
                        }else{
                            $Parameter['id'] = Methods.generateUid();
                        }
                        $Parameter['node_id'] = Node_id;
                        Node[$Attr]['alertnode'] = 'true';
                        var Refresh_Duration_id = setInterval(function () {
                            $(Methods.Document.selector + ' #kloppe').find('[node_id="'+Node_id+'"][id="'+$Parameter['id']+'"][alertnode="true"]').remove();
                        }, parseInt(AlertObj['baseduration']) + parseInt(AlertObj['switchduration']));
                        Node[$Attr]["rdid"] = Refresh_Duration_id;
                        var AlertNode =  Methods.BuildShape(templ,Node[$Attr],Node[$Attr]['nodename'],$Parameter);
                    };
                };
                var Obj = new Object();
                Obj.Nodeheight=AlertObj['nodeheight'];
                Obj.NodeX=AlertObj['nodex'];
                Obj.NodeZ=AlertObj['nodez'];
                child.each(function(e,i){

                    var translation = $(i).attr('translation').trim().split(/[ |,]+/g);
                    //console.log(translation[0]+' '+translation[1]+' '+(parseInt(translation[2]) + Obj.Nodeheight));
                    $(i).attr('translation',(parseInt(translation[0]) + parseInt(Obj.NodeX))+' '+(parseInt(translation[1]) + parseInt(Obj.Nodeheight))+' '+(parseInt(translation[2]) + parseInt(Obj.NodeZ)));

                });
                if(NumNode > AlertObj['num']){
                    ExistTemplate.find('>transform:first').remove();
                };
                AlertNode.appendTo(ExistTemplate);
                return Methods;
            },
            InsertX3DElement: function (translation,rotation) {
                //if(Methods.CurrentNodesSelectBox == '')return Methods;

                if(Methods.BeforInsertX3DElement){
                    Methods = Methods.BeforInsertX3DElement(Methods);
                }

                if(!rotation) rotation ='0.9999892642023637 0 0.004633732838142135 1.5612488335498067';
                if(!translation) translation ='0 0 0';
                var Buffer = '';
                var $Parameter={}
                jQuery.extend($Parameter,Methods.ElementParameter);
                $Parameter['onclick']=Methods.Stuff+'.SelectElement(event);';
                $Parameter['translation']=translation;
                if(!$Parameter['diffusecolor']){
                    $Parameter['diffusecolor']='0 1 0';
                }
                $Parameter['Rotation']=rotation;
                var template = $('<element></element>');
                if(Methods.SelectedInsertX3DElement['type'] == 'Customize'){
                    template = $('<element>'+Methods.SelectedInsertX3DElement['pattern']+'</element>');
                };

                for(var $Attr in Methods.SelectedInsertX3DElement){
                    if(typeof Methods.SelectedInsertX3DElement[$Attr] === 'string' ) {

                    }else{

                        $Parameter['_id'] = Methods.SelectedInsertX3DElement['_id'];
                        //console.log(Methods.SelectedInsertX3DElement);
                        //console.log(Methods.CurrentNodesSelectBox);
                        if(Methods.CurrentNodesSelectBox != ''){
                            $Parameter['node_id'] = Methods.CurrentNodesSelectBox['_id'];
                            $Parameter['alerttype'] = Methods.CurrentAlertSelectBox['_id'];
                            Methods.SelectedInsertX3DElement[$Attr]['node']='true';
                        }
                        if('id' in Methods.SelectedInsertX3DElement){
                            $Parameter['id'] = Methods.SelectedInsertX3DElement['id'];
                        }else{
                            $Parameter['id'] = Methods.generateUid();
                        }
                        //console.log($Parameter);
                        //Methods.SelectedInsertX3DElement[$Attr]['transform']['_id']=$Parameter['_id'];
                        Buffer = Methods.BuildShape(template,Methods.SelectedInsertX3DElement[$Attr],Methods.SelectedInsertX3DElement[$Attr]['nodename'],$Parameter);
                    };
                };
                $(Buffer).find('[xmlns]').each(function(index, element) {
                    $(element).removeAttr('xmlns');
                });
                $(Buffer).appendTo(Methods.Document.selector + ' #kloppe');
                var boxes=$(Methods.Document.selector).find(">#boxes");
                var element=$(Methods.Document.selector + ' #kloppe').find('[id="'+$Parameter['id']+'"]');
                new x3dom.Moveable(boxes[0],element[0], null, 0,'all-switch');
                if(Methods.AfterInsertX3DElement){
                    Methods = Methods.AfterInsertX3DElement(Methods);
                }
                return Methods;
            },
            X3DElementPointer: function (event) {
                /* $(Methods.Document.selector).find('#posOut').innerHTML = event.worldX.toFixed(4) + ", " +
                 event.worldY.toFixed(4) + ", " +
                 event.worldZ.toFixed(4) + " | " +
                 event.normalX.toFixed(4) + ", " +
                 event.normalY.toFixed(4) + ", " +
                 event.normalZ.toFixed(4);
                 */
                var $Node = $(event.hitObject).parent();
                if($Node.prop("tagName") == 'transform'){
                    this.MouseMove(event,$Node);
                };
                if($Node.prop("tagName") == 'transform' && $Node.attr('node_id')){
                    this.MouseMoveNode(event,Methods.NodesSelectBox[$Node.attr('node_id')]);
                };
                var l = $(Methods.Document.selector).find(Methods.cpnt);
                var t = $(Methods.Document.selector).find(Methods.bar);

                var norm = new x3dom.fields.SFVec3f(event.normalX, event.normalY, event.normalZ);

                l.attr('point', event.worldX + ' ' + event.worldY + ' ' + event.worldZ + ', ' + (event.worldX + 2.5 * norm.x) + ' ' + (event.worldY + 2.5 * norm.y) + ' ' + (event.worldZ + 2.5 * norm.z));

                var qDir = x3dom.fields.Quaternion.rotateFromTo(new x3dom.fields.SFVec3f(0, 1, 0), norm);
                //new x3dom.fields.SFVec3f(0, 0, -1));
                var rot = qDir.toAxisAngle();

                //x3dom.debug.logWarning(rot);
                t.attr('Rotation', rot[0].x + ' ' + rot[0].y + ' ' + rot[0].z + ' ' + rot[1]);
                t.attr('translation', (event.worldX)+' '+(event.worldY)+' '+(event.worldZ));

                // t.attr('translation', event.worldX + ' ' + event.worldY + ' ' + event.worldZ);
                if(event.type == "click" && event.button == 2){
                    //**********************************************************************************
                    //Methods.InsertX3DElement((event.worldX) + ' ' + (event.worldY) + ' ' + (event.worldZ),rot[0].x + ' ' + rot[0].y + ' ' + rot[0].z + ' ' + rot[1]);
                    Methods.InsertX3DElement((event.worldX) + ' ' + (event.worldY) + '  0',rot[0].x + ' ' + rot[0].y + ' ' + rot[0].z + ' ' + rot[1]);
                };
                return Methods;
            },
            removeX3DMap: function () {
                $(Methods.Document.selector + ' #kloppe #MapBg').remove();
                return Methods;
            },
            InitX3DMap: function (MapData) {
                //var X3D = $(Methods.Document.selector + ' #boxes');
                // var width = X3D.attr('width')/96;
                //var height = X3D.attr('height')/96;
                //console.log(width);
                var Buffer = '<transform Translation=\'0 0 0\' id="MapBg" > <shape ispickable="true"> <Appearance><imagetexture repeatS=\'false\' id="ImageTest1" repeatT=\'false\' url=\'"'+MapData+'"\'/> </Appearance><box size=\'70 70 0\'  /></shape></transform>';
                $(Buffer).find('[xmlns]').each(function(index, element) {
                    $(element).removeAttr('xmlns');
                });
                $(Buffer).appendTo(Methods.Document.selector + ' #kloppe');

                return Methods;
            },
            Initialize: function (ServerURL, Parameter, BeforInit,OnInit, AfterInit) {
                var CallBack = function () {
                    var Buffer = '';
                    if (Methods.InitJson == false) return Methods;
                    if (BeforInit) {
                        Buffer = Buffer + BeforInit({});
                    };
                    var Count = 1;
                    for (var Key in Methods.InitJson['collections']) {
                        var Collections=Methods.InitJson['collections'][Key];
                        if (OnInit) {
                            Buffer = Buffer + OnInit({
                                "Collections": true,
                                'Count': Count++
                            });
                        };
                        if('shapes' in Collections) {
                            Methods.ShapeSelectBox = Collections['shapes'];
                            if (Methods.Mode=="Editor"){
                                Methods.ShapeSelectBoxInit();
                                if (OnInit) {
                                    Buffer = Buffer + OnInit({
                                        "Shapes": true,
                                    });
                                };
                                Methods.SelectedInsertX3DElement = '';
                                if(Methods.AfterSelectInsertX3DElement){
                                    Methods.AfterSelectInsertX3DElement(Methods);
                                }
                            };
                        };
                        if ('alert' in Collections) {
                            Methods.AlertSelectBox = Collections['alert'];
                            if (Methods.Mode=="Editor"){
                                Methods.AlertSelectBoxInit();
                                if (OnInit) {
                                    Buffer = Buffer + OnInit({
                                        "Shapes": true,
                                    });
                                };
                                //Methods.CurrentAlertSelectBox = '';
                                Methods.GetAlertSelectBox();
                                if(Methods.AfterGetAlertSelectBox){
                                    Methods.AfterGetAlertSelectBox(Methods);
                                }
                            };
                        };
                        if ('nodes' in Collections) {
                            Methods.NodesSelectBox = Collections['nodes'];
                            if (Methods.Mode=="Editor"){
                                Methods.NodesSelectBoxInit();
                                if (OnInit) {
                                    Buffer = Buffer + OnInit({
                                        "Nodes": true,
                                    });
                                };
                                Methods.CurrentNodesSelectBox = '';
                                if(Methods.AfterGetNodesSelectBox){
                                    Methods.AfterGetNodesSelectBox(Methods);
                                }
                            };
                        };
                        if ('initialize' in Collections) {
                            if ('map' in Collections['initialize']) {
                                $(Base64_2.decode(Collections['initialize']['map'])).appendTo(Methods.Document.selector + ' #kloppe');
                            };
                            if ('innerelement' in Collections['initialize']) {
                                for(var initElement in Collections['initialize']['innerelement']){
                                    var Buffer = '';
                                    var $Parameter={}
                                    $Parameter['onclick']=Methods.Stuff+'.SelectElement(event);';
                                    var template = $('<element></element>');
                                    for(var $Attr in Collections['initialize']['innerelement'][initElement]){
                                        if(typeof Collections['initialize']['innerelement'][initElement][$Attr] !== 'string' ) {
                                            if(Collections['initialize']['innerelement'][initElement]['type'] == 'Customize'){
                                                template = $('<element>'+Collections['initialize']['innerelement'][initElement][$Attr]['pattern']+'</element>');
                                            };
                                            if('node_id' in Collections['initialize']['innerelement'][initElement]){
                                                $Parameter['node_id'] = Collections['initialize']['innerelement'][initElement]['node_id'];
                                                Collections['initialize']['innerelement'][initElement]['node']='true';
                                                $(Methods.NodesSelectBoxPoint).find('option[value="'+$Parameter['node_id']+'"]').attr("hidden","1");

                                            }
                                            if('_id' in Collections['initialize']['innerelement'][initElement]){
                                                $Parameter['_id'] = Collections['initialize']['innerelement'][initElement]['_id'];
                                            }
                                            if('id' in Collections['initialize']['innerelement'][initElement]){
                                                $Parameter['id'] = Collections['initialize']['innerelement'][initElement]['id'];

                                            }else{
                                                $Parameter['id'] = Methods.generateUid();
                                            }
                                            Buffer = Methods.BuildShape(template,Collections['initialize']['innerelement'][initElement][$Attr],Collections['initialize']['innerelement'][initElement][$Attr]['nodename'],$Parameter);
                                        };
                                    };
                                    $(Buffer).appendTo(Methods.Document.selector + ' #kloppe');
                                    if (Methods.Mode=="Editor"){
                                        var boxes=$(Methods.Document.selector).find(">#boxes");
                                        var element=$(Methods.Document.selector + ' #kloppe').find('[id="'+$Parameter['id']+'"]');
                                        new x3dom.Moveable(boxes[0],element[0], null, 0,'all-switch');
                                    }
                                };
                            };
                        };
                    };
                    if (AfterInit) {
                        Buffer = Buffer + AfterInit({});
                    };
                    Methods.NumInView += Methods.Count;
                    Methods.Count = 0;
                    if (Buffer == 'undefined') return Methods;
                    //Methods.Document.add(Buffer);
                    //$(Buffer).hide().addTo(Methods.Document.selector).slideDown("slow");
                    Methods.NewsAlarm();
                };
                Methods.Ajax(ServerURL, 'initialize', Parameter, CallBack);
                return Methods;
            },
            Get_More: function (ServerURL, Parameter, Header, Body, Footer) {
                var CallBack = function ($Faild) {
                    if ($Faild) {
                        return;
                    };
                    var Buffer = '';
                    if (Methods.InitJson == false) return Methods;
                    if (Header) {
                        Buffer = Buffer + Header({});
                    };
                    var Count = 1;
                    for (var News in Methods.InitJson['News']) {
                        if (Body) {
                            Buffer = Buffer + Body({
                                "News": Methods.InitJson['News'][News],
                                'Count': Count++
                            });
                        };
                    };
                    /*if (Methods.InitJson['News'].length % 2 != 0) {
                     $(Methods.Document.selector)
                     .find('>li')
                     .each(function (index, element) {
                     $(element)
                     .removeClass("odd")
                     .removeClass("even");
                     var $Even = 'odd';
                     if (index % 2 == 0) {
                     $Even = 'even';
                     };
                     $(element)
                     .addClass($Even);
                     });
                     };*/
                    if (Footer) {
                        Buffer = Buffer + Footer({});
                    };
                    Methods.NumInView += Methods.Count;
                    Methods.Count = 0;
                    if (Buffer == 'undefined') return Methods;
                    //Methods.Document.append(Buffer);
                    $(Buffer).hide().appendTo(Methods.Document.selector).slideDown("slow");
                    Methods.NewsAlarm();
                };
                Methods.Ajax(ServerURL, 'Get_More', Parameter, CallBack);
                return Methods;
            },
            Get_Last: function (ServerURL, Parameter) {
                var CallBackGet_Last = function ($Faild) {
                    Methods.Refresh_Lock = false;
                    if ($Faild) {
                        return;
                    };
                    var Buffer = '';
                    if (Methods.Get_LastInitJson == false) return Methods;
                    var Count = 1;
                    Methods.NotificationTimeOut = 5000;
                    for (var Alert in Methods.Get_LastInitJson) {
                        Methods.PoPupAlert(Methods.Get_LastInitJson[Alert]['node_id'],{'alertname':Methods.Get_LastInitJson[Alert]['alertname'],'alertlastname':Methods.Get_LastInitJson[Alert]['alertlastname'],'alertpersonnum':Methods.Get_LastInitJson[Alert]['alertpersonnum'],'alertdatetime':Methods.Get_LastInitJson[Alert]['alertdatetime'],'alertimageurl':Methods.Get_LastInitJson[Alert]['alertimageurl'],'alertdiffusecolor':Methods.Get_LastInitJson[Alert]['alertdiffusecolor']});
                        Methods.Notification(Methods.Get_LastInitJson[Alert]['alertnotiftitle'], Methods.Get_LastInitJson[Alert]['alertnotifdescription'], Methods.NotificationTimeOut,Methods.Get_LastInitJson[Alert]['alertnotifimageurl']);
                        Methods.NotificationTimeOut = Methods.NotificationTimeOut + 3000;
                    };
                    /*if (Methods.InitJson['News'].length % 2 != 0) {
                     $(Methods.Document.selector)
                     .find('>li')
                     .each(function (index, element) {
                     $(element)
                     .removeClass("odd")
                     .removeClass("even");
                     var $Even = 'odd';
                     if (index % 2 == 0) {
                     $Even = 'even';
                     };
                     $(element)
                     .addClass($Even);
                     });
                     };*/
                    Methods.NumInView += Methods.Count;
                    Methods.Count = 0;
                    Methods.NewsAlarm();
                };
                if (Methods.Refresh_Lock == false) {
                    Methods.Refresh_Lock = true;
                    Methods.Ajax(ServerURL, 'Get_Last', Parameter, CallBackGet_Last);
                };
                return Methods;
            },
            AlertSelectBoxInit: function (Point) {
                if (!Point) {
                    Point = Methods.AlertSelectBoxPoint;
                } else {
                    Methods.AlertSelectBoxPoint = Point;
                };
                Point = $(Point);
                //Point.append('<option value="" style=" background:#fff; ">select Alert</option>');
                for (i in Methods.AlertSelectBox) {
                    var AlertSelectBox = Methods.AlertSelectBox[i];
                    if(!AlertSelectBox['Color']){
                        AlertSelectBox['Color'] = '#fff';
                    };
                    Point.append('<option value="' + AlertSelectBox['_id'] + '" style=" background:' + AlertSelectBox['Color'] + '; ">' + AlertSelectBox['name'] + '</option>');
                };
                return Methods;
            },
            GetAlertSelectBox: function () {
                Point = Methods.AlertSelectBoxPoint;
                Methods.CurrentAlertSelectBox = Methods.AlertSelectBox[$(Point)[0].options[$(Point)[0].selectedIndex].value];
                if(Methods.AfterGetAlertSelectBox){
                    Methods.AfterGetAlertSelectBox(Methods);
                }
                return Methods;
            },
            NodesSelectBoxInit: function (Point) {
                if (!Point) {
                    Point = Methods.NodesSelectBoxPoint;
                } else {
                    Methods.NodesSelectBoxPoint = Point;
                };
                Point = $(Point);
                Point.append('<option value="" style=" background:#fff; ">انتخاب گذرگاه</option>');
                for (i in Methods.NodesSelectBox) {
                    var NodesSelectBox = Methods.NodesSelectBox[i];
                    if(!NodesSelectBox['Color']){
                        NodesSelectBox['Color'] = '#fff';
                    };
                    Point.append('<option value="' + NodesSelectBox['_id'] + '" style=" background:' + NodesSelectBox['Color'] + '; ">' + NodesSelectBox['name'] + '</option>');
                };
                return Methods;
            },
            GetNodesSelectBox: function () {
                Point = Methods.NodesSelectBoxPoint;
                if($(Point)[0].options[$(Point)[0].selectedIndex].value==''){
                    Methods.CurrentNodesSelectBox = '';
                }else{
                    Methods.CurrentNodesSelectBox = Methods.NodesSelectBox[$(Point)[0].options[$(Point)[0].selectedIndex].value];
                }
                if(Methods.AfterGetNodesSelectBox){
                    Methods.AfterGetNodesSelectBox(Methods);
                }
                return Methods;
            },
            ShapeSelectBoxInit: function (Point) {
                if (!Point) {
                    Point = Methods.ShapeSelectBoxPoint;
                } else {
                    Methods.ShapeSelectBoxPoint = Point;
                };
                Point = $(Point);
                Point.append('<option value="" style=" background:#fff; ">انتخاب مدل</option>');
                for (i in Methods.ShapeSelectBox) {
                    var ShapeSelectBox = Methods.ShapeSelectBox[i];
                    if(!ShapeSelectBox['Color']){
                        ShapeSelectBox['Color'] = '#fff';
                    };
                    Point.append('<option value="' + ShapeSelectBox['_id'] + '" style=" background:' + ShapeSelectBox['Color'] + '; ">' + ShapeSelectBox['name'] + '</option>');
                };
                return Methods;
            },
            GetShapeSelectBox: function () {
                Point = Methods.ShapeSelectBoxPoint;
                Methods.CurrentShapeSelectBox = Methods.ShapeSelectBox[$(Point)[0].options[$(Point)[0].selectedIndex].value];
                return Methods;
            },
            GetDayData: function (Data, Month, Reset) {
                if (Reset == true) {
                    Methods.DayPointer = 0;
                } else {
                    try {
                        var $DayData = Data[Month][++Methods.DayPointer];
                        $DayData['Color'] = Methods.ShapeSelectBox[$DayData['_id']]['Color'];
                        return $DayData;
                    } catch ($e) {
                        return {
                            '_id': '',
                            'Color': '',
                            'timestamp': ''
                        };
                    };
                };
            },
            SubmitEvent: function (Pointer, ServerURL, Parameter) {
                $(Pointer).click(function (e) {
                    Methods.SubmitData(ServerURL, Parameter);
                });
                return Methods;
            },
            getDate: function  () {
                var map = $(Methods.Document.selector + ' #kloppe #MapBg').clone().wrap('<p>').parent();
                $(map).find('[xmlns]').each(function(index, element) {
                    $(element).removeAttr('xmlns');
                });
                map = map.html();
                $(Methods.Document.selector + ' #kloppe #MapBg').remove();
                //console.log($(Methods.Document.selector + ' #kloppe').html());
                var InnerElement = $(Methods.Document.selector + ' #kloppe').html().replace(/xmlns=\"(.*?)\"/gi,'');
                $(map).appendTo(Methods.Document.selector + ' #kloppe');
                var JsonInnerElement = $.xml2json('<element>'+InnerElement+'</element>');
                if(JsonInnerElement['transform']){
                    if(!JsonInnerElement['transform'][1]){
                        var tmp = JsonInnerElement['transform'];
                        JsonInnerElement['transform'] = [tmp];
                    }
                }
                //console.log(JsonInnerElement);
                var FinalInnerElement=new Array();
                for (var Key in JsonInnerElement) {
                    for (var K in JsonInnerElement[Key]) {
                        FinalInnerElement.push({'node_id':JsonInnerElement[Key][K].node_id,'type':'Default','id':JsonInnerElement[Key][K].id,0:JsonInnerElement[Key][K]});
                    }
                }
                for (var Key in Methods.InitJson['collections']) {
                    if(!Methods.InitJson['collections'][Key].initialize){
                        Methods.InitJson['collections'][Key].initialize = new Object();
                    }
                    Methods.InitJson['collections'][Key].initialize.map = Base64_2.encode(map);
                    Methods.InitJson['collections'][Key].initialize.innerelement= FinalInnerElement;

                }
                //console.log(Methods.InitJson);
                if(Methods.JsonEnable)
                {
                    var Data = JSON.stringify(Methods.InitJson);
                }else{
                    var $temp = $('<element></element>');

                    $.JsonToXML($temp,Methods.InitJson,'element');
                    $($temp).find('[xmlns]').each(function(index, element) {
                        $(element).removeAttr('xmlns');
                    });
                    Data = $temp.html();
                };
                return Data;
            },
            SubmitData: function (ServerURL, Parameter) {
                var map = $(Methods.Document.selector + ' #kloppe #MapBg').clone().wrap('<p>').parent();
                $(map).find('[xmlns]').each(function(index, element) {
                    $(element).removeAttr('xmlns');
                });
                map = map.html();
                $(Methods.Document.selector + ' #kloppe #MapBg').remove();
                //console.log($(Methods.Document.selector + ' #kloppe').html());
                var InnerElement = $(Methods.Document.selector + ' #kloppe').html().replace(/xmlns=\"(.*?)\"/gi,'');
                $(map).appendTo(Methods.Document.selector + ' #kloppe');
                var JsonInnerElement = $.xml2json('<element>'+InnerElement+'</element>');
                if(JsonInnerElement['transform']){
                    if(!JsonInnerElement['transform'][1]){
                        var tmp = JsonInnerElement['transform'];
                        JsonInnerElement['transform'] = [tmp];
                    }
                }
                //console.log(JsonInnerElement);
                var FinalInnerElement=new Array();
                for (var Key in JsonInnerElement) {
                    for (var K in JsonInnerElement[Key]) {
                        FinalInnerElement.push({'node_id':JsonInnerElement[Key][K].node_id,'type':'Default','id':JsonInnerElement[Key][K].id,0:JsonInnerElement[Key][K]});
                    }
                }
                for (var Key in Methods.InitJson['collections']) {
                    if(!Methods.InitJson['collections'][Key].initialize){
                        Methods.InitJson['collections'][Key].initialize = new Object();
                    }
                    Methods.InitJson['collections'][Key].initialize.map = Base64_2.encode(map);
                    Methods.InitJson['collections'][Key].initialize.innerelement= FinalInnerElement;

                }
                //console.log(Methods.InitJson);
                if(Methods.JsonEnable)
                {
                    var Data = JSON.stringify(Methods.InitJson);
                }else{
                    var $temp = $('<element></element>');

                    $.JsonToXML($temp,Methods.InitJson,'element');
                    $($temp).find('[xmlns]').each(function(index, element) {
                        $(element).removeAttr('xmlns');
                    });
                    Data = $temp.html();
                };
                $.ajax({
                    type: 'POST',
                    url: ServerURL,
                    data: {
                        operation: "Save_Map_Editor",
                        //year: Methods.CurrentYear,
                        Data: Data
                    },
                    //contenttype:"application/json;charset=utf-8",
                    success: function (r) {
                        if (r.status == true) {
                            Methods.Console.log(r.Message);
                        } else {
                            Methods.Console.log(r.error);
                        };
                    },
                    //datatype: "json",
                    async: true
                });
                return Methods;
            },
        };
        var now = new Date();
        var utc_now = new Date(now.getUTCFullYear(), now.getUTCMonth(), now.getUTCDate(), now.getUTCHours(), now.getUTCMinutes(), now.getUTCSeconds(), now.getUTCMilliseconds());
        var NotificationState = $.cookie(Methods.Document.selector + 'Notification');
        if (NotificationState == '1') {
            Methods.NotificationEnable = true;
            $.cookie(Methods.Document.selector + 'Notification', '1');
        } else {
            Methods.NotificationEnable = false;
            $.cookie(Methods.Document.selector + 'Notification', '2');
        };
        var NewsAlarmState = $.cookie(Methods.Document.selector + 'soundAlarm');
        if (NewsAlarmState == '1') {
            Methods.NewsAlarmEnable = true;
            $.cookie(Methods.Document.selector + 'soundAlarm', '1');
        } else {
            Methods.NewsAlarmEnable = false;
            $.cookie(Methods.Document.selector + 'soundAlarm', '2');
        };
        Methods.NewerNewsCTimeInView = $.cookie(Methods.Document.selector + 'NewerNewsCTimeInView');
        if (!Methods.NewerNewsCTimeInView) {
            Methods.NewerNewsCTimeInView = utc_now.getTime() / 1000 - 3600;
            $.cookie(Methods.Document.selector + 'NewerNewsCTimeInView', Methods.NewerNewsCTimeInView);
        };
        Methods.LastCTimeInView = $.cookie(Methods.Document.selector + 'LastCTimeInView');
        if (!Methods.LastCTimeInView) {
            Methods.LastCTimeInView = utc_now.getTime() / 1000;
            $.cookie(Methods.Document.selector + 'LastCTimeInView', Methods.LastCTimeInView);
        };
        $(window).focus(function () {
            Methods.UserActive = true;
        });
        $(window).blur(function () {
            Methods.UserActive = false;
        });
        if (!$.soundAlarm) {
            $('<audio src="data:audio/mp3;base64,SUQzAwAAAAAyKlRQRTEAAAAlAAAB//5NAGUAdAByAG8AbgBvAG0AZQAgAFcAbwBvAGQAZQBuAAAAVENPTgAAAAUAAAAoMCkAVElUMgAAACUAAAH//k0AZQB0AHIAbwBuAG8AbQBlACAAVwBvAG8AZABlAG4AAABQUklWAAAQswAAWE1QADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+Cjx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMy1jMDExIDY2LjE0NTY2MSwgMjAxMi8wMi8wNi0xNDo1NjoyNyAgICAgICAgIj4KIDxyZGY6UkRGIHhtbG5zOnJkZj0iaHR0cDovL3d3dy53My5vcmcvMTk5OS8wMi8yMi1yZGYtc3ludGF4LW5zIyI+CiAgPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIKICAgIHhtbG5zOmRjPSJodHRwOi8vcHVybC5vcmcvZGMvZWxlbWVudHMvMS4xLyIKICAgIHhtbG5zOnhtcERNPSJodHRwOi8vbnMuYWRvYmUuY29tL3htcC8xLjAvRHluYW1pY01lZGlhLyIKICAgIHhtbG5zOnhtcD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLyIKICAgIHhtbG5zOnhtcE1NPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvbW0vIgogICAgeG1sbnM6c3RFdnQ9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZUV2ZW50IyIKICAgIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIgogICBkYzpmb3JtYXQ9ImF1ZGlvL21wZWciCiAgIHhtcERNOmFydGlzdD0iTWV0cm9ub21lIFdvb2RlbiIKICAgeG1wRE06Z2VucmU9IkJsdWVzIgogICB4bXA6TWV0YWRhdGFEYXRlPSIyMDEzLTExLTEwVDExOjE4OjE3KzAzOjMwIgogICB4bXA6TW9kaWZ5RGF0ZT0iMjAxMy0xMS0xMFQxMToxODoxNyswMzozMCIKICAgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDo3RTNBNjQ3NkRDNDlFMzExODc3QUI3M0ZGNzIzNDUyQyIKICAgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDo3RTNBNjQ3NkRDNDlFMzExODc3QUI3M0ZGNzIzNDUyQyIKICAgeG1wTU06T3JpZ2luYWxEb2N1bWVudElEPSJ4bXAuZGlkOjdEM0E2NDc2REM0OUUzMTE4NzdBQjczRkY3MjM0NTJDIj4KICAgPGRjOnRpdGxlPgogICAgPHJkZjpBbHQ+CiAgICAgPHJkZjpsaSB4bWw6bGFuZz0ieC1kZWZhdWx0Ij5NZXRyb25vbWUgV29vZGVuPC9yZGY6bGk+CiAgICA8L3JkZjpBbHQ+CiAgIDwvZGM6dGl0bGU+CiAgIDx4bXBETTpUcmFja3M+CiAgICA8cmRmOkJhZz4KICAgICA8cmRmOmxpCiAgICAgIHhtcERNOnRyYWNrTmFtZT0iQ3VlUG9pbnQgTWFya2VycyIKICAgICAgeG1wRE06dHJhY2tUeXBlPSJDdWUiCiAgICAgIHhtcERNOmZyYW1lUmF0ZT0iZjQ0MTAwIi8+CiAgICAgPHJkZjpsaQogICAgICB4bXBETTp0cmFja05hbWU9IlN1YmNsaXAgTWFya2VycyIKICAgICAgeG1wRE06dHJhY2tUeXBlPSJJbk91dCIKICAgICAgeG1wRE06ZnJhbWVSYXRlPSJmNDQxMDAiLz4KICAgIDwvcmRmOkJhZz4KICAgPC94bXBETTpUcmFja3M+CiAgIDx4bXBNTTpIaXN0b3J5PgogICAgPHJkZjpTZXE+CiAgICAgPHJkZjpsaQogICAgICBzdEV2dDphY3Rpb249InNhdmVkIgogICAgICBzdEV2dDppbnN0YW5jZUlEPSJ4bXAuaWlkOjdEM0E2NDc2REM0OUUzMTE4NzdBQjczRkY3MjM0NTJDIgogICAgICBzdEV2dDp3aGVuPSIyMDEzLTExLTEwVDExOjE4OjE3KzAzOjMwIgogICAgICBzdEV2dDpzb2Z0d2FyZUFnZW50PSJBZG9iZSBBdWRpdGlvbiBDUzYgKFdpbmRvd3MpIgogICAgICBzdEV2dDpjaGFuZ2VkPSIvbWV0YWRhdGEiLz4KICAgICA8cmRmOmxpCiAgICAgIHN0RXZ0OmFjdGlvbj0ic2F2ZWQiCiAgICAgIHN0RXZ0Omluc3RhbmNlSUQ9InhtcC5paWQ6N0UzQTY0NzZEQzQ5RTMxMTg3N0FCNzNGRjcyMzQ1MkMiCiAgICAgIHN0RXZ0OndoZW49IjIwMTMtMTEtMTBUMTE6MTg6MTcrMDM6MzAiCiAgICAgIHN0RXZ0OnNvZnR3YXJlQWdlbnQ9IkFkb2JlIEF1ZGl0aW9uIENTNiAoV2luZG93cykiCiAgICAgIHN0RXZ0OmNoYW5nZWQ9Ii8iLz4KICAgIDwvcmRmOlNlcT4KICAgPC94bXBNTTpIaXN0b3J5PgogICA8eG1wTU06RGVyaXZlZEZyb20KICAgIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6N0QzQTY0NzZEQzQ5RTMxMTg3N0FCNzNGRjcyMzQ1MkMiCiAgICBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOjdEM0E2NDc2REM0OUUzMTE4NzdBQjczRkY3MjM0NTJDIgogICAgc3RSZWY6b3JpZ2luYWxEb2N1bWVudElEPSJ4bXAuZGlkOjdEM0E2NDc2REM0OUUzMTE4NzdBQjczRkY3MjM0NTJDIi8+CiAgPC9yZGY6RGVzY3JpcHRpb24+CiA8L3JkZjpSREY+CjwveDp4bXBtZXRhPgogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgCjw/eHBhY2tldCBlbmQ9InciPz4AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAD/82hwAA7VyvrKDENsA9AF5QAIAAH1rmVZqon+FXguJXOJUJ0VCHdToRjmJ/PIhL6dG79GV8hRCnRlfk14hOhPEJz//sSlTncjOe8SnYgc0Km5pfNKTz9wgwAW5olcq8QnMq8RK7wWEJzhfpv/m/xAYsEIiEHNC8sqfhFAKL+NCwRNRULHCUmETVjwwXB1hDUIQyQnAjpDPzSMtrQEsQAAGQAAAKMT1fzVwgBAJkQP/f//8P09mhRyLqD4gCIPiMcCCw/EA1ojxOHxd4w4JBoHuHbPrYmt1f3/82hwMwA8ARxQAAAAAcACXPwAAAAfSntpcXeGCi6DlLDGr//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////kQQAACUBoJOMFkoqEtKNMppRJq+ClIfCvTReIVMQ9jxAY7NuG+lAHFgQXrlOGf/9ujvsQU9uzWpsOZ5WA4TmFgMX/82hw7RVQARgVoAAAAYgCXPlAAADYbCYNBs3HJVA0RkFuw6kfi4CCZiAIGJxSYLDJhomGXCMtF0UqWArAGP0Yt5hoYAxIFvYutq5rSBGcCmZWWR1cvE1kMPgwwcPDDQVAoYMnmww0AQMeUbk0oQ46k4cp3foIpAhh8fmJiAKi4wOKQsDofCAEXRMHAmLwxT3XhpPoH21LJzP7G+953MFApHFIWaXe+bHW3bE7sWsxGHKnaWxW1Yxq6/89YTmf27liTWoceRZ6ABYZepgcCkIdAQeMQAwKhYz/82hw/yueH1Mvx/AAgSACXK2AAABIBjCYVCgNEQYAIfMMBXWWPvq+qpWEMpkkvpZiesWopIL9ic7hY/8/73//+EAcBwFAADRCUffVIRc7txSRy6rWqQ3fh94IzGGHSyzWu3LVWls/+8s6XfMuf////9UZ///wAADK2aWWk5qrCEKTPIUZGRFJDj2xf0x/v39vmtcWx/qtfX5p/asGJ413urYtuR3GTL1hdmABIqELFrN1sXbOqKXveHPNNFgxmaPq1Xu97tuFvdID6DLHh1hrl1GpJCrFxI7/82hwYBSJuUt24LwAAKgCXK3AAABNquhy1u+t82f4o9n3/bdc68WbUDdfnGcS7+LW+LWze+6Y1v6ti8J9iRHPWltbXiWb07qWkWHv/5xXMb+lRtz0kRKvar+AAAALeHGVS1//+YcMQpk1JoJo6KqSXZB1ospF1qWzpJJIPNkkjIyNlnnos7MUlFouJkmMyDdwG4JGKVHJASBlAPj9TY6JQlP4+t7DpdC1EdEqOCcijds2tPj5cZE6FT20va28ujOZ7Z3P8xvV7rs9s73WhZra1Uy79offatb/82hwfBTVq0N6BixsAKgCXKwAAAC1mZm1Pa7PTHy6scbrTKq6G4kl2kMK1q62Z146oOhr+dCjzy3H1lhKkGiP8AAAAAAIV5EqW////w5hpVOtNlOr6NNSaKDpqZJA2dJMmj5xM2Y8sjSw6Sj5dKBFj5YJ48YEWGVMQ1EDKwGhEajuAoWDoobB9OOFAXSrf2o6MiU00sVMocswzskmmpe7ISFVgqOoooVAPNeb0OWQkBwukSooNwv5J85tI4wVlLN3FNnD3Uo5/DGZSxETXhE2Xi0ITopiwRH/82hwlRU5rzmKBklsAOACXMQAAAD0LJt+bayYpJQuHFCxr5LWp55XEq/5AAAAArNT/1/2S1UnRUl15kRYnj7JGRPOlYyMyqovOktZYIkOSP5oTxkOAG4hMgtqAWQslA0Qt5eK5rHMn3297VzNEKEvjO1IcaTawK9ZVSyT1QF+QpnVvhnK/kgoVD8BXMUzNqDAYnKMxK3Ga7YrbsumOI+ynVS9kVsXD59vNa5gwXtYMz62avVbCa1Dr+CknK1awWFzOl4ooymUTW61ml9YtIzSWzWLYKDw0DP/82hwqhXlqyg6Ai9sARgCXNwAAABW7JbPDf+AAAAACAfvf/7/////+uvn/H8FOxsvV4nRxK8tpWnCLahNnKVkIUYK23OWYBblWsEGZJ64395hPXtH3rWCw2hPhKdDQlIpPDHrCRbPN9HfLHhtbjsJ8tfVIiywj0f/////////////////////////////////////////9qQAAQT/////39ZYt///////////////////////////////////////////////////////////////////////82hwuQ708RgpAC9aAVACXOAAAAD///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////lYYCAADAD/////////////////////////////////////////////////////////////////////////////////82hw/xd0AxYAAAAAAcACXOgAAAD///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////+CELD5BsP/////////////////////////////////////////////////////////////////////////////////82hw/xeAAS4AAAAAAcACXPwAAAD///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////7M4EwFRUj/////////////////////////////////////////////////////////////////////////////////82hw/xeAAS4AAAAAAcACXPwAAAD//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////jwgjChmZI7/////////////////////////////////////////////////////////////////////////////////82hQ/xdgAS4AAAAAAfgCXQAAAAD//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////+MAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAD/82hQ/xfQAS4AAAAAARgCXNwAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABUQUdNZXRyb25vbWUgV29vZGVuAAAAAAAAAAAAAAAAAABNZXRyb25vbWUgV29vZGVuAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA==" autostart="false" width="0" height="0" id="soundAlarm" enablejavascript="true"></audio>').appendTo(document.body);
            $.soundAlarm = true;
        };
        return Methods;
    };
}($));
$.soundAlarm = false;