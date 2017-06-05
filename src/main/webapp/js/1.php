<?php
$fp=file_get_contents('result.json','rb');
print_r(json_decode($fp));