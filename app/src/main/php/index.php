<?php

$post_is_set_not_null = (isset($_POST['tag']) && $_POST['tag'] != '');
if ($post_is_set_not_null) {
	$tag = $_POST['tag'];
	require_once 'include/db_user.php';
	$db = new db_user();

	$response = array("tag" => $tag, "success" => 0, "error" => 0);

	if ($tag == 'login') {
		$email = $_POST['email'];
		$password = $_POST['password'];

		$user = $db->getUserByEmailPassword($email, $password);
		if ($user != false) {
			//user found, all is good
			$response["success"] = 1;
			$response["uid"] = $user["unique_id"];
			$response["user"]["name"] = $user["name"];
            $response["user"]["email"] = $user["email"];
            $response["user"]["created_at"] = $user["created_at"];
            $response["user"]["updated_at"] = $user["updated_at"];
            echo json_encode($response);
		} else {
			//wrong password or email
			$response["error"] = 1; 
			$response["error_msg"] = "Incorrect E-mail or Password";
			echo json_encode($response);
		}
	} else if ($tag == 'register') {
		$name = $_POST['name'];
		$email = $_POST['email'];
		$password = $_POST['password'];

		if ($db->userExists($email)) {
			//user already exists
			$response["error"] = 2;
			$response["error_msg"] = "User already exists";
			echo json_encode($reponse);
		} else {
			$user = $db->storeUser($name, $email, $password);
			if ($user) {
				//user stored successfully
				$response["success"] = 1;
                $response["uid"] = $user["unique_id"];
                $response["user"]["name"] = $user["name"];
                $response["user"]["email"] = $user["email"];
                $response["user"]["created_at"] = $user["created_at"];
                $response["user"]["updated_at"] = $user["updated_at"];
                echo json_encode($response);
			} else {
				//user did not store correctly
				$response["error"] = 1;
                $response["error_msg"] = "Error occured in Registartion";
                echo json_encode($response);
			}
		} 
	} else {
		//tag wasnt 'login' or 'register'
		echo "Invalid Request";
	}
} else {
	//couldn't connect to database
	echo "Access Denied";
}

?>