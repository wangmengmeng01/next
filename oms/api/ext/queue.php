<?php
class queue
{
    private static $_instance = null;
    private static $_storage = '';

    public static function instance($queue, $config=array())
    {
        self::$_storage = empty($queue) ? 'redis_storage' : $queue;
        if (!isset(self::$_instance[self::$_storage]) || !self::$_instance[self::$_storage]) {
            $classFile = ROOT_DIR . '/api/ext/queue_server/' . $queue . '.php';
            if (file_exists($classFile)) {
                include_once $classFile;
                self::$_instance[self::$_storage] = new $queue($config);
            } else {
                throw new Exception('队列对象不存在', 601);
            }
        }
        return self::$_instance[self::$_storage];
    }

    /**
     * 是否运行
     * @return mixed
     */
    public function ping()
    {
        return self::$_instance[self::$_instance]->ping();
    }

    /**
     * 入队列
     * @param $queueName
     * @param $queueVal
     * @return mixed
     */
    public function push($queueName, $queueVal)
    {
        return self::$_instance[self::$_instance]->push($queueName, $queueVal);
    }

    /**
     * 出队列
     * @param $queueName
     * @param $queueVal
     * @return mixed
     */
    public function pop($queueName)
    {
        return self::$_instance[self::$_instance]->pop($queueName);
    }

}