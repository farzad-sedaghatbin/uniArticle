<?php
$_Year = $_POST['year'];
if(empty($_Year))$_Year=date("Y");
$DAta=array(
   'Status'=>true,
   "$_Year"=>array(
      'selectBox'=>array(
	        '1'=>array(
			    '_id'=>'1',
				'Color'=>'#ff00ff',
				'Title'=>'تست',
				'timestamp'=>'4523456236-4343456236'
			),
			'2'=>array(
			    '_id'=>'2',
				'Color'=>'#860fff',
				'Title'=>'1تست',
				'timestamp'=>'4523456236-4343456236'
			),
			'3'=>array(
			    '_id'=>'3',
				'Color'=>'#5f2f7f',
				'Title'=>'2تست',
				'timestamp'=>'4523456236-4343456236'
			),
			'4'=>array(
			    '_id'=>'4',
				'Color'=>'#9f6883',
				'Title'=>'تست3',
				'timestamp'=>'4523456236-4343456236'
			)
			
			
	  
	   ),
	   'Initialize'=>array(
	        
	  
	  
	   ),
   ),


);
for($M=1;$M<13;$M++){
	for($D=1;$D<45;$D++){
		$DAta['2014']['Initialize'][$M][$D]=array('_id'=>rand(1,4),'timestamp'=>(time()+rand(1,10000)).'-'.time()+rand(10000,10000));
	}
	
}
$data=json_encode($DAta,JSON_HEX_TAG | JSON_HEX_APOS | JSON_HEX_QUOT | JSON_HEX_AMP);
$fp=fopen('init.json','w+');
fwrite($fp,$data);
echo $data;
