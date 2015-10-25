<html>
<head>
	<style type="text/css">
		tr td:first-child {
			text-align: right;
		}
		td {
			padding: 0 20px;
			margin: 0;
			border: 0;
			border-bottom: thin #CCC solid;
		}
		tr {
			border-bottom: thin #000 solid;
		}
		tr:hover {
			background-color: #AFA;
		}
	</style>
</head>
<body style="font-family: 'Lucida Console', Monaco, monospace">
<table>
	<thead><tr><td>ID</td><td>Comment</td><td>Actions.</td></tr></thead>
	<tbody>
<?php
	function numberToDirection($num) {
		switch ($num) {
			case 1: return 'WEST';
			case 2: return 'EAST';
			case 3: return 'NORT';
			case 4: return 'SOUT';
		}
	}
	
	$db = new mysqli('localhost', 'bender0', 'B4pHUX3fC8x5RfXK', 'bender0');
	$result = $db->query('SELECT gStateId, gStateComment FROM `SarsaState` s LEFT JOIN `SarsaStateAction` a ON s.gStateId = a.state GROUP BY gStateId, gStateComment ORDER BY MAX(ABS(a.qValue)) DESC ');
	while ($row = $result->fetch_array(MYSQLI_NUM)) {
		if (is_null($row[1]))
			continue;
		$inResult = $db->query('SELECT action, qValue FROM SarsaStateAction WHERE state = ' . $row[0]);
		echo '<tr><td>' . $row[0] . '</td><td>' . nl2br(str_replace(' ', '&nbsp;', $row[1])) . '</td><td>';
		while ($inRow = $inResult->fetch_array(MYSQLI_NUM)) {
			echo numberToDirection($inRow[0]) . ' ' . $inRow[1] . '<br />';
		}
		echo '</td></tr>' . "\n";
	}
	$result->free();
	$db->close();
?>
	</tbody>
</table>
</body>
</html>
