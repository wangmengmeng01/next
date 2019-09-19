<?php

/**
 * Created by PhpStorm.
 * User: Renee
 * Date: 2018/3/6
 * Time: 15:57
 */
class DbAction extends PDO
{
    private static $_instance = null;
    protected $dbName = '';
    protected $dsn;
    protected $dbh;

//    protected $_trans = 0;
    public function __construct($dbCharset = 'utf8')
    {
        try {
            $this->dsn = 'mysql:host=' . DB_EM_HOST . ';port=3306;dbname=' . DB_EM_NAME;
            $this->dbh = new PDO($this->dsn, DB_EM_USER, DB_EM_PASS);
            $this->dbh->setAttribute(PDO::ATTR_EMULATE_PREPARES, false);
            $this->dbh->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            $this->dbh->exec('SET character_set_connection=' . $dbCharset . ';SET character_set_client=' . $dbCharset . ';SET character_set_results=' . $dbCharset);
        } catch (Exception $e) {
            $this->outputError($e->getMessage());
        }
    }

    public static function getInstance($dbHost, $dbUser, $dbPasswd, $dbName, $dbCharset = 'utf8')
    {
        if (self::$_instance === null) {
            self::$_instance = new self($dbHost, $dbUser, $dbPasswd, $dbName, $dbCharset);
        }
        return self::$_instance;
    }

    public function fetchAll($sql, $params = array())
    {
        try {
            $stm = $this->dbh->prepare($sql);
            if ($stm && $stm->execute($params)) {
                return $stm->fetchAll(PDO::FETCH_ASSOC);
            }
        } catch (Exception $e) {
            $this->outputError($e->getMessage());
        }
    }

    public function fetchOne($sql, $params = array())
    {
        try {
            $result = false;
            $stm = $this->dbh->prepare($sql);
            if ($stm && $stm->execute($params)) {
                $result = $stm->fetch(PDO::FETCH_ASSOC);
            }
            return $result;
        } catch (Exception $e) {
            $this->outputError($e->getMessage());
        }
    }

    public function fetchColumn($sql, $params = array())
    {
        $result = '';
        try {
            $stm = $this->dbh->prepare($sql);
            if ($stm && $stm->execute($params)) {
                $result = $stm->fetchColumn();
            }
            return $result;
        } catch (Exception $e) {
            $this->outputError($e->getMessage());
        }
    }

    public function insert($table, $params = array(), $returnLastId = true)
    {
        $_implode_field = '';
        $fields = array_keys($params);
        $_implode_field = implode(',', $fields);
        $_implode_value = '';

        foreach ($fields as $value) {
            $_implode_value .= ':' . $value . ',';
        }

        $_implode_value = trim($_implode_value, ',');
        $sql = 'INSERT INTO ' . $table . '(' . $_implode_field . ') VALUES (' . $_implode_value . ')';
        try {

            $stm = $this->dbh->prepare($sql);
            $result = $stm->execute($params);
            if ($returnLastId) {
                $result = $this->dbh->lastInsertId();
            }
            return $result;
        } catch (Exception $e) {
            $this->outputError($e->getMessage());
        }
    }

    /**
     * 批量插入处理
     * @param       $table
     * @param array $data
     * @param bool  $resultLastId
     * @return bool|string
     * @throws Exception
     */
    public function insertAll($table, $data = array())
    {
        try {

            $num = count($data);

            # 判断是否为一位数组
            if ($num == count($data, COUNT_RECURSIVE)) {

                return $this->insert($table, $data);
            }

            $tmpField = implode(', ', array_keys($data[0]));
            $tmpValue = '';
            $tmpData = array();

            # 二维数组数据批量占位符处理
            for ($line = 0; $line < $num; $line++) {

                $tmpValue[] = ':' . $line . implode(', :' . $line, array_keys($data[$line]));

                # 预处理数据键重新编写
                foreach ($data[$line] as $k => $value) {

                    $tmpData[$line][$line . $k] = $value;
                }

            }

            $tmpValue = implode('), (', $tmpValue);

            $sql = "INSERT INTO {$table} ({$tmpField}) VALUES ($tmpValue)";

            $stm = $this->dbh->prepare($sql);


            return $stm->execute($tmpData);

        } catch (Exception $e) {

            $this->outputError($e->getMessage());
        }


    }

    public function update($table, $data, $where, $params = array())
    {

        if (empty($where)) {
            return false;
        }

        $sql = 'UPDATE ' . $table . ' SET ';

        foreach ($data as $k => $v) {
            $params[':' . $k] = $v ? $v : null;
            $sql .= $k . ' = :' . $k . ',';
        }

        $sql = substr($sql, 0, strlen($sql) - 1) . ' WHERE ' . $where;

        try {
            $stm = $this->dbh->prepare($sql);
            $result = $stm->execute($params);

            $result = $result === 0 ? 1 : $result;

            return $result;

        } catch (Exception $e) {

            $this->outputError($e->getMessage());
        }
    }

    public function delete($sql, $params = array())
    {
        try {
            $stm = $this->dbh->prepare($sql);
            $result = $stm->execute($params);
            return $result;
        } catch (Exception $e) {
            $this->outputError($e->getMessage());
        }
    }

    public function exec($sql, $params = array())
    {
        try {
            $stm = $this->dbh->prepare($sql);
            $result = $stm->execute($params);
            return $result;
        } catch (Exception $e) {
            $this->outputError($e->getMessage());
        }
    }

    /**
     * 启动事务
     * @return void
     */
    public function startTrans()
    {
        //数据rollback 支持
        if ($this->_trans == 0) $this->dbh->beginTransaction();
        $this->_trans++;
        return;
    }

    /**
     * 用于非自动提交状态下面的查询提交
     * @return boolen
     */
    public function commit()
    {
        $result = true;
        if ($this->_trans > 0) {
            $result = $this->dbh->commit();
            $this->_trans = 0;
        }
        return $result;
    }

    /**
     * 事务回滚
     * @return boolen
     */
    public function rollback()
    {
        $result = true;
        if ($this->_trans > 0) {
            $result = $this->dbh->rollback();
            $this->_trans = 0;
        }
        return $result;
    }

    /**
     * 关闭连接
     * PHP 在脚本结束时会自动关闭连接。
     */
    public function close()
    {
        if (!is_null($this->dbh)) $this->dbh = null;
    }

    private function outputError($strErrMsg)
    {
        throw new Exception("MySQL Error: " . $strErrMsg);
    }

    public function __destruct()
    {
        $this->dbh = null;
    }
}