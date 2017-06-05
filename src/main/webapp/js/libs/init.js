var VTM3DMap = $('.VTM3DMap').VisualTrafficMonitor();
VTM3DMap = VTM3DMap.InitX3D('VTM3DMap');
var Res = new Object();
Res.VTM3DMap = VTM3DMap;
Res.VTM3DMap.MouseMoveCallBack=function(e,n){
	//console.log(n);
	$('#ShowNodeName').html('');
	
	$('#ShowShapeName').html('');
	if(n.attr('_id')){		
	   var Node = Res.VTM3DMap.ShapeSelectBox[n.attr('_id')];
	   $('#ShowShapeName').html(Node.name);
	};
	
	var Node = $(Res.VTM3DMap.CurrentShapeSelected.hitObject).parent();
	//console.log(Res.VTM3DMap.CurrentShapeSelected);
	if(Node.attr('node_id')){
	  //$('#ShowNodeName').html(Res.VTM3DMap.NodesSelectBox[Node.attr('node_id')].name);
	}
	
};
Res.VTM3DMap.MouseMoveNodeCallBack=function(e,n){
	$('#ShowNodeName').html(n.name);
	
};
window.onresize=function(e){
	VTM3DMap.OnResize(e);
};
Res.VTM3DMap.BeforInsertX3DElement=function(P){
	P.ElementParameter = {
		'OverWrite':{
		      /*'Shape':{
				  'Appearance':{
					  'Material':{
				         'diffuseColor':document.getElementById('diffuseColor').value,
						 'specularColor':document.getElementById('specularColor').value
					  },
					  
				  },
			  },*/
			  //'scale':document.getElementById('scaleX').value + ' ' + document.getElementById('scaleY').value + ' ' + document.getElementById('scaleZ').value
		}
   };
   return P;
};
Res.VTM3DMap.AfterInsertX3DElement=function(P){
	if(P.CurrentNodesSelectBox!=''){
	   $(P.NodesSelectBoxPoint).find('option[value="'+P.CurrentNodesSelectBox['_id']+'"]').attr("hidden","1");
	};
	$(P.ShapeSelectBoxPoint).find('option[value=""]').attr("selected","selected");
	$(P.NodesSelectBoxPoint).find('option[value=""]').attr("selected","selected");
	Res.VTM3DMap.GetNodesSelectBox();	
	Res.VTM3DMap.SelectInsertX3DElement();	
	return P;
};
Res.VTM3DMap.AfterSelectInsertX3DElement=function(P){
	if(P.SelectedInsertX3DElement == '')return P;
	//var scale = P.SelectedInsertX3DElement['Transform']['scale'].split(/[ ]+/g);
	//document.getElementById('scaleX').value = scale[0];
	//document.getElementById('scaleY').value = scale[1];
	//document.getElementById('scaleZ').value = scale[2];
	return P;
};
Res.VTM3DMap.AfterDeleteElement=function(P,Node_id){
	if(P.DeleteElement == true){
		$('#removeElement').css("border","#193BA8 2px solid").css("border-radius","4px");		
	}else{
		if(Node_id){
		   $(P.NodesSelectBoxPoint).find('option[value="'+Node_id+'"]').removeAttr("hidden");
		}
		$('#removeElement').css("border","");
	};
	return P;
};
Res.VTM3DMap.Initialize('/modena-1.0.1/restful/TMSService/initMap',{});
$('#VTMShape').bind("change", function (event) {
	Res.VTM3DMap.SelectInsertX3DElement();	
});
$('#VTMNodes').bind("change", function (event) {
	Res.VTM3DMap.GetNodesSelectBox();	
});
$('#VTMAlert').bind("change", function (event) {
	Res.VTM3DMap.GetAlertSelectBox();	
});
$('#removeElement').bind("click", function (event) {
	Res.VTM3DMap.SetAsRemove();	
	if(Res.VTM3DMap.DeleteElement == true){
		$('#removeElement').css("border","#193BA8 2px solid").css("border-radius","4px");
	}else{
		$('#removeElement').css("border","");
	};
});
$('#saveMap').bind("click", function (event) {
	Res.VTM3DMap.SubmitData();
});
$('#ImageMap').bind("change", function (event) {
  if (!event.srcElement) {
    event.srcElement = event.target;
  }
  var Node = event.srcElement;
  if (Node.files[0] == null) {
    return false;
  }
  Res.Evt = event;
  if (window.FileReader) {
    if (Node.files[0].type == "image/jpeg" || Node.files[0].type == "image/png" || Node.files[0].type == "image/gif" || Node.files[0].type == "image/bmp") {
      var reader = new FileReader();
      reader.onload = function (d) {
        var contents = d.target.result;
        Res.VTM3DMap.removeX3DMap();
        Res.VTM3DMap.InitX3DMap(contents);
      };
      reader.readAsDataURL(Node.files[0]);
    };
  };
});