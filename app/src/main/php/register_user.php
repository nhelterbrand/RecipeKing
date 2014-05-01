<?php

/**
* Code to register a new user to the users table
*
*/

$response = array();

$validName = isset($_POST["name"]) && !empty($_POST["name"]);
$validEmail = isset($_POST["email"]) && !empty($_POST["email"]);
$validPassword = isset($_POST["password"]) && !empty($_POST["password"]);

if ($validName && $validEmail && $validPassword) {
	//assign values
	$name = mysql_escape_string($_POST["name"]);
	$email = mysql_escape_string($_POST["email"]);
	$password = mysql_escape_string($_POST["password"]);

	//connect to the database
	require_once "db_connect.php";
	$db = new DB_CONNECT();

	//try inserting
	$result = mysql_query("INSERT into users(name, email, password) 
		values('$name', '$email', '$password')");

	if ($result) {
		//insert went well
		$response["success"] = 1;
		$response["message"] = "User successfully registered";
	} else {
		//insert failed
		$response["success"] = 0;
		$response["message"] = "Error in registration";
	}

	echo json_encode($response);
} else {
	//something is messed up with the fields
	$response["success"] = 0;
	$response["message"] = "Fields are not entered correctly";

	echo json_encode($response);
}