<?php
 
/*
 * Following code will update a product information
 * A product is identified by product id (recipid)
 */
 
// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['recipeid']) && isset($_POST['name']) && isset($_POST['rating']) && isset($_POST['directions'])) {
 
    $pid = $_POST['recipeid'];
    $name = $_POST['name'];
    $rating = $_POST['rating'];
    $directions = $_POST['directions'];
 
    // include db connect class
    require_once __DIR__ . '/db_connect.php';
 
    // connecting to db
    $db = new DB_CONNECT();
 
    // mysql update row with matched pid
    $result = mysql_query("UPDATE recipes SET name = '$name', rating = '$rating', directions = '$directions' WHERE recipeid = $pid");
 
    // check if row inserted or not
    if ($result) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "Product successfully updated.";
 
        // echoing JSON response
        echo json_encode($response);
    } else {
 
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>