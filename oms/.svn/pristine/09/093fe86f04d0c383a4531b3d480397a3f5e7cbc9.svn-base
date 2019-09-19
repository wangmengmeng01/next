<?php

/**
 * 数据库操作类
 *
 * @author 1210965963@qq.com
 */
class Database
{
    /**
     * @var \PDO
     */
    private $pdo = null;

    /**
     * @param string $dsn
     * @param string $user
     * @param string $pwd
     *
     * @return void
     */
    public function __construct($dsn, $user, $pwd)
    {
        if (is_null($this->pdo)) {
            $this->pdo = new \PDO($dsn, $user, $pwd);
            $this->pdo->query('set names utf8');
            $this->pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        }
    }

    /**
     * 检索数据
     *
     * @param string $fields 要查询的字段
     * @param string $table  表格
     * @param string $where  检索条件
     * @param array  $params 预处理数据映射关系
     *
     * @return array
     */
    public function fetchAll($fields, $table, $where, $params = array())
    {
        try {

            $sql = 'SELECT ' . $fields . ' FROM ' . $table . ' WHERE ' . $where;
            $statement = $this->pdo->prepare($sql);
            $statement->execute($params);

            return $statement->fetchAll(\PDO::FETCH_ASSOC);

        } catch (PDOException $e) {

            throw new PDOException($e->getMessage());

        }
    }

    /**
     * 检索数据 单条
     *
     * @param string $fields 要查询的字段
     * @param string $table  表格
     * @param string $where  检索条件
     * @param array  $params 预处理数据映射关系
     *
     * @return array
     */
    public function fetchOne($fields, $table, $where, $params = array())
    {
        try {
            $sql = 'SELECT ' . $fields . ' FROM ' . $table . ' WHERE ' . $where;
            $statement = $this->pdo->prepare($sql);
            $statement->execute($params);
            return $statement->fetch(\PDO::FETCH_ASSOC);

        } catch (PDOException $e) {
            throw new PDOException($e->getMessage());

        }

    }

    /**
     * 插入数据
     *
     * @param string $table  数据表
     * @param array  $data   数据集
     * @param bool   $lastId 是否需要获取插入的数据ID
     * @return void
     */
    public function insert($table, $data = array(), $lastId = true)
    {

        try {

            $parameters = array();
            foreach ($data as $k => $v) {
                $parameters[':' . $k] = $v;
            }

            $sql = 'INSERT INTO ' . $table . ' (`' . implode('`,`', array_keys($data)) . '`) VALUES (' . implode(',', array_keys($parameters)) . ')';


            $statement = $this->pdo->prepare($sql);

            $result = $statement->execute($parameters);

            return $lastId ? $this->pdo->lastInsertId() : $result;

        } catch (PDOException $e) {

            throw new PDOException($e->getMessage());
        }


    }

    /**
     * 更新数据
     * @param       $table
     * @param       $data   要更新的字段信息
     * @param       $where
     * @param array $params 预处理需要的信息
     * @return bool|int
     */
    public function update($table, $data, $where, $params = array())
    {

        try {

            if (empty($where)) {
                return false;
            }

            $sql = 'UPDATE ' . $table . ' SET ';

            foreach ($data as $k => $v) {
                $params[':' . $k] = $v || $v === 0 ? $v : null;
                $sql .= $k . ' = :' . $k . ',';
            }

            $sql = substr($sql, 0, strlen($sql) - 1) . ' WHERE ' . $where;
            $statement = $this->pdo->prepare($sql);
            $result = $statement->execute($params);

            $result = $result === 0 ? 1 : $result;

            return $result;


        } catch (PDOException $e) {

            throw new PDOException($e->getMessage());
        }


    }


    /**
     * 批量插入数据
     * @param       $table
     * @param array $data
     * @return bool|string|void
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
            $tmpData = array();

            # 二维数组数据批量占位符处理
            for ($line = 0; $line < $num; $line++) {

                $tmpValue[] = ':' . $line . implode(', :' . $line, array_keys($data[$line]));

                # 预处理数据键重新编写
                foreach ($data[$line] as $k => $value) {

                    $tmpData[$line . $k] = $value;
                }

            }

            $tmpValue = implode('), (', $tmpValue);

            $sql = "INSERT INTO {$table} ({$tmpField}) VALUES ($tmpValue)";

            $stm = $this->pdo->prepare($sql);

            return $stm->execute($tmpData);

        } catch (PDOException $e) {
            throw new PDOException($e->getMessage());
        }

    }

    /**
     * 批量更新
     * @param 表名     $table_name
     * @param 更新数据 $data
     * @param 关键字段 $field
     * @return 影响行数
     */
    public function batch_update($table_name='', $data=array(), $field=''){
        try {
            if(!$table_name||!$data||!$field){
                return false;
            }else{
                $sql='UPDATE '.$table_name;
            }
            $con=array();
            $fields=array();
            $con_sql=array();
            foreach ($data as $key => $value) {
                $x=0;
                foreach ($value as $k => $v) {
                    if($k!=$field&&!$con[$x]&&$x==0){
                        $con[$x]=" SET {$k} = (CASE {$field} ";
                    }elseif($k!=$field&&!$con[$x]&&$x>0){
                        $con[$x]=" {$k} = (CASE {$field} ";
                    }
                    if($k!=$field){
                        $temp=$value[$field];
                        $con_sql[$x].= " WHEN '{$temp}' THEN '{$v}' ";
                        $x++;
                    }
                }
                $temp=$value[$field];
                if(!in_array($temp,$fields)){
                    $fields[]=$temp;
                }
            }
            $num=count($con)-1;
            foreach ($con as $key => $value) {
                foreach ($con_sql as $k => $v) {
                    if($k==$key&&$key<$num){
                        $sql.=$value.$v.' END),';
                    }elseif($k==$key&&$key==$num){
                        $sql.=$value.$v.' END)';
                    }
                }
            }
            $str=implode(',',$fields);
            $sql.=" WHERE {$field} IN({$str})";

            $res = $this->pdo->exec($sql);

            return $res;
        } catch (PDOException $e) {
            throw new PDOException($e->getMessage());
        }
    }


    /**
     * 删除数据
     *
     * @param string $where 条件
     *
     * @return void
     */
    public function delete($table, $where, $parameters = array())
    {

        try {

            $sql = 'DELETE FROM ' . $table . ' WHERE ' . $where;

            $statement = $this->pdo->prepare($sql);

            return $statement->execute($parameters);

        } catch (PDOException $e) {

            throw new PDOException($e->getMessage());

        }

    }


    /**
     * @return \PDO
     */
    public function getPdo()
    {
        return $this->pdo;
    }

    public function __destruct()
    {
        $this->pdo = null;
    }
}
