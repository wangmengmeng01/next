<?php
/**
 * oms数据库连接
 */
function connectDb()
{
	try {
		$pdo = new PDO("mysql:host=" . DB_EM_HOST . ";port=3306;dbname=oms;", DB_EM_USER, DB_EM_PASS);

		$pdo->query('set names utf8');
		$pdo->setAttribute(PDO::MYSQL_ATTR_USE_BUFFERED_QUERY,true);
		$pdo->setAttribute(PDO::ATTR_ERRMODE,PDO::ERRMODE_EXCEPTION);
		$pdo->setAttribute(PDO::ATTR_CASE,PDO::CASE_LOWER);
	} catch (PDOException $e) {
		print "Error!: " . $e->getMessage() . "<br/>";
		die();
	}
	return $pdo;
}

/**
 * oms_jd数据库连接
 */
function connectJdDb(){
    try {
        $pdo = new PDO("mysql:host=" . DB_JD_HOST . ";port=3306;dbname=oms;", DB_JD_USER, DB_JD_PASS);
        $pdo->query('set names utf8');
        $pdo->setAttribute(PDO::MYSQL_ATTR_USE_BUFFERED_QUERY,true);
        $pdo->setAttribute(PDO::ATTR_ERRMODE,PDO::ERRMODE_EXCEPTION);
        $pdo->setAttribute(PDO::ATTR_CASE,PDO::CASE_LOWER);
    } catch (PDOException $e) {
        print "Error!: " . $e->getMessage() . "<br/>";
        die();
    }
    return $pdo;
}

