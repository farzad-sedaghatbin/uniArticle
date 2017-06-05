<?php
$fp=fopen('result.json','w+');
fwrite($fp,$_POST['Data']);