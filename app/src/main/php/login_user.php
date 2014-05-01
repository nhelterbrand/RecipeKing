<?php

/**
* Logs a user in based on their email and password
*
*/

define WRONG_PASSWORD = 2;
define NO_EMAIL_FOUND = 3;
define BAD_INPUT = 4;
define SUCCESS = 1;
$response = array();
$validEmail = $_POST["email"] && !empty($_POST["email"]);
$validPassword = $_POST["password"] && !empty($_POST["password"]);

if ($validEmail && $validPassword) {
	//assign values
	$email = mysql_escape_string($_POST["email"]);
	$password = mysql_escape_string($_POST["password"]);

	//connect to the database
	require_once "db_connect.php";
	$db = new DB_CONNECT();

	//query for user
	$result = mysql_query("select password from users where email = '$email'");

	if (mysql_num_rows($result) == 1) {
		//user found check password
		if ($password == mysql_fetch_array($result)) {
			$response["success"] = SUCCESS;
			$response["message"] = "User Login Successful";
		} else {
			$response["success"] = WRONG_PASSWORD;
			$response["message"] = "Incorrect Password";
		}
		
	} else {
		//user not found
		$resposne["success"] = NO_EMAIL_FOUND;
		$response["message"] = "User Email Not Found";
	}

	echo json_encode($response);
} else {
	//bad input
	$response["success"] = BAD_INPUT;
	$response["message"] = "Error in login input";
}