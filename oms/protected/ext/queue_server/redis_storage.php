<?php
/**
 * redis类
 * User: 独孤羽<123517746@qq.com>
 * Date: 15-5-11 下午12:34
 */
class redis_storage
{
    private $_connect = null;
    private $_link = null;

    public function __construct($config)
    {
        if (!isset($this->_connect) || !$this->_connect) {
            try {
                $this->_link = new Redis();
                $this->_link->connect($config['host'], $config['port']);

                $auth = $config['auth'];
                if ($auth) {
                    if ($this->_link->auth($auth) == false) {
                        throw new Exception('redis服务器登陆认证失败', 601);
                    }
                }
            } catch(Exception $e) {
                throw new Exception('redis服务器连接失败', 601);
            }
        }
    }

    public function ping()
    {
        return $this->_link->ping();
    }

    /**
     * 入队列
     * @param $queueName
     * @param $queueVal
     * @return mixed
     */
    public function push($queueName, $queueVal)
    {
        return $this->_link->lpush($queueName, $queueVal);
    }

    /**
     * 出队列
     * @param $queueName
     * @return mixed
     */
    public function pop($queueName)
    {
        return $this->_link->lpop($queueName);
    }

}