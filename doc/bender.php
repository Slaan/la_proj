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
	<thead><tr><td>ID</td><td>Comment</td></tr></thead>
	<tbody>
<?php
	$db = new mysqli('localhost', 'bender0', 'B4pHUX3fC8x5RfXK', 'bender0');
	$result = $db->query('SELECT * FROM SarsaState');
	while ($row = $result->fetch_array(MYSQLI_NUM)) {
		if (is_null($row[1]))
			continue;
		echo '<tr><td>' . $row[0] . '</td><td>' . nl2br(str_replace(' ', '&nbsp;', $row[1])) . '</td></tr>' . "\n";
	}
	$result->free();
	$db->close();
?>
	</tbody>
</table>
</body>
</html>
