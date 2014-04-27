<?php

class DB_User {
	private $db;

	//constructor
	function __construct() {
		require_once 'db_connect.php';

		$this->db = new DB_CONNECT();
		$this->db->connect();
	}

	//destructor
	function __destruct() {

	}

	/**
	 * stores new user
	 * returns user details
	 */
	public function storeUser($name, $email, $password) {
		$uuid = uniqid('', true);
		$hash = $this->hashSSHA($password);
		$encrypted_password = $hash["encrypted"];
		$salt = $hash["salt"];
		$result = mysql_query("INSERT INTO users(unique_id, name, email, encrypted_password, salt, created_at)
								VALUES ('$uuid', '$name', '$email', '$encrypted_password', '$salt', NOW())");

		//check if stored successfully
		if ($result) {
			$uid = mysql_insert_id();
			$result = mysql_query("SELECT * FROM users WHERE uid = $uid");
			return mysql_fetch_array($result);
		} else {
			return false;
		}
	}

	/**
	 * Get user by email and password
	 */
	public function getUserByEmailPassword($email, $password) {
		$result = mysql_query("select * from users where email='$email'")
			or die(mysql_error());

		$number_of_rows = mysql_num_rows($result);

		if ($number_of_rows > 0) {
			$result = mysql_fetch_array($result);

			$salt = $result['salt'];
			$encrypted_password = $result['encrypted_password'];
			$hash = $this->checkhashSSHA($salt, $password);

			$passwords_match = ($encrypted_password == $hash);
			if ($passwords_match) {
				return $result;
			}
		} else {
			return false;
		}
	}

	/**
	 * check if user exists
	 */
	public function userExists($email) {
		$result = mysql_query("select email from users where email = '$email'");

		$number_of_rows = mysql_num_rows($result);
		if ($number_of_rows > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	* Encrypt password
	*/
	public function hashSSHA($password) {
		$salt = sha1(rand());
		$salt = substr($salt, 0 , 10);
		$encrypted = base64_encode(sha1($password.$salt, true).$salt);
		$hash = array("salt" => $salt, "encrypted" => $encrypted);
		return $hash;
	}

	/**
	* Decrypt password
	*/
	public function checkhashSSHA($salt, $password) {
		$hash = base64_encode(sha1($password.$salt, true).$salt);
		return $hash;

	}
}

?>