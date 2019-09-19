<?php
/**
 * 公共方法类
 * 使用较多的公共方法可以放入此类中
 * @auth 	DengYun
 * @version 1.0
 */

class util
{

    /**
     * $string： 明文 或 密文
     * $operation：DECODE表示解密,其它表示加密
     * $key： 密匙
     * $expiry：密文有效期
     */
    public static function rc4($string, $operation = 'DECODE', $key = '', $expiry = 0) {
        // 动态密匙长度，相同的明文会生成不同密文就是依靠动态密匙
        $ckey_length = 4;

        // 密匙
        $key = md5($key);

        // 密匙a会参与加解密
        $keya = md5(substr($key, 0, 16));
        // 密匙b会用来做数据完整性验证
        $keyb = md5(substr($key, 16, 16));
        // 密匙c用于变化生成的密文
        $keyc = $ckey_length ? ($operation == 'DECODE' ? substr($string, 0, $ckey_length): substr(md5(microtime()), -$ckey_length)) : '';
        // 参与运算的密匙
        $cryptkey = $keya.md5($keya.$keyc);
        $key_length = strlen($cryptkey);
        // 明文，前10位用来保存时间戳，解密时验证数据有效性，10到26位用来保存$keyb(密匙b)，解密时会通过这个密匙验证数据完整性
        // 如果是解码的话，会从第$ckey_length位开始，因为密文前$ckey_length位保存 动态密匙，以保证解密正确
        $string = $operation == 'DECODE' ? base64_decode(substr($string, $ckey_length)) : sprintf('%010d', $expiry ? $expiry + time() : 0).substr(md5($string.$keyb), 0, 16).$string;
        $string_length = strlen($string);
        $result = '';
        $box = range(0, 255);
        $rndkey = array();
        // 产生密匙簿
        for($i = 0; $i <= 255; $i++) {
            $rndkey[$i] = ord($cryptkey[$i % $key_length]);
        }
        // 用固定的算法，打乱密匙簿，增加随机性，好像很复杂，实际上对并不会增加密文的强度
        for($j = $i = 0; $i < 256; $i++) {
            $j = ($j + $box[$i] + $rndkey[$i]) % 256;
            $tmp = $box[$i];
            $box[$i] = $box[$j];
            $box[$j] = $tmp;
        }
        // 核心加解密部分
        for($a = $j = $i = 0; $i < $string_length; $i++) {
            $a = ($a + 1) % 256;
            $j = ($j + $box[$a]) % 256;
            $tmp = $box[$a];
            $box[$a] = $box[$j];
            $box[$j] = $tmp;
            // 从密匙簿得出密匙进行异或，再转成字符
            $result .= chr(ord($string[$i]) ^ ($box[($box[$a] + $box[$j]) % 256]));
        }
        if($operation == 'DECODE') {
            // substr($result, 0, 10) == 0 验证数据有效性
            // substr($result, 0, 10) - time() > 0 验证数据有效性
            // substr($result, 10, 16) == substr(md5(substr($result, 26).$keyb), 0, 16) 验证数据完整性
            // 验证数据有效性，请看未加密明文的格式
            if((substr($result, 0, 10) == 0 || substr($result, 0, 10) - time() > 0) && substr($result, 10, 16) == substr(md5(substr($result, 26).$keyb), 0, 16)) {
                return substr($result, 26);
            } else {
                return '';
            }
        } else {
            // 把动态密匙保存在密文里，这也是为什么同样的明文，生产不同密文后能解密的原因
            // 因为加密后的密文可能是一些特殊字符，复制过程可能会丢失，所以用base64编码
            return $keyc.str_replace('=', '', base64_encode($result));
        }
    }

    /*********************************************************************
    函数名称:encrypt
    函数作用:加密解密字符串
    使用方法:
    加密     :encrypt('str','E','nowamagic');
    解密     :encrypt('被加密过的字符串','D','nowamagic');
    参数说明:
    $string   :需要加密解密的字符串
    $operation:判断是加密还是解密:E:加密   D:解密
    $key      :加密的钥匙(密匙);
     *********************************************************************/
    public static function encrypt($string,$operation,$key='')
    {
        $key=md5($key);
        $key_length=strlen($key);
        $string=$operation=='D'?base64_decode($string):substr(md5($string.$key),0,8).$string;
        $string_length=strlen($string);
        $rndkey=$box=array();
        $result='';
        for($i=0;$i<=255;$i++)
        {
            $rndkey[$i]=ord($key[$i%$key_length]);
            $box[$i]=$i;
        }
        for($j=$i=0;$i<256;$i++)
        {
            $j=($j+$box[$i]+$rndkey[$i])%256;
            $tmp=$box[$i];
            $box[$i]=$box[$j];
            $box[$j]=$tmp;
        }
        for($a=$j=$i=0;$i<$string_length;$i++)
        {
            $a=($a+1)%256;
            $j=($j+$box[$a])%256;
            $tmp=$box[$a];
            $box[$a]=$box[$j];
            $box[$j]=$tmp;
            $result.=chr(ord($string[$i])^($box[($box[$a]+$box[$j])%256]));
        }
        if($operation=='D')
        {
            if(substr($result,0,8)==substr(md5(substr($result,8).$key),0,8))
            {
                return substr($result,8);
            }
            else
            {
                return'';
            }
        }
        else
        {
            return str_replace('=','',base64_encode($result));
        }
    }

    public static function gzdecode($data) {
        $len = strlen($data);
        if ($len < 18 || strcmp(substr($data,0,2),"\x1f\x8b")) {
            return null; // Not GZIP format (See RFC 1952)
        }
        $method = ord(substr($data,2,1)); // Compression method
        $flags = ord(substr($data,3,1)); // Flags
        if ($flags & 31 != $flags) {
            // Reserved bits are set -- NOT ALLOWED by RFC 1952
            return null;
        }
        // NOTE: $mtime may be negative (PHP integer limitations)
        $mtime = unpack("V", substr($data,4,4));
        $mtime = $mtime[1];
        $xfl = substr($data,8,1);
        $os    = substr($data,8,1);
        $headerlen = 10;
        $extralen = 0;
        $extra    = "";
        if ($flags & 4) {
            // 2-byte length prefixed EXTRA data in header
            if ($len - $headerlen - 2 < 8) {
                return false;    // Invalid format
            }
            $extralen = unpack("v",substr($data,8,2));
            $extralen = $extralen[1];
            if ($len - $headerlen - 2 - $extralen < 8) {
                return false;    // Invalid format
            }
            $extra = substr($data,10,$extralen);
            $headerlen += 2 + $extralen;
        }

        $filenamelen = 0;
        $filename = "";
        if ($flags & 8) {
            // C-style string file NAME data in header
            if ($len - $headerlen - 1 < 8) {
                return false;    // Invalid format
            }
            $filenamelen = strpos(substr($data,8+$extralen),chr(0));
            if ($filenamelen === false || $len - $headerlen - $filenamelen - 1 < 8) {
                return false;    // Invalid format
            }
            $filename = substr($data,$headerlen,$filenamelen);
            $headerlen += $filenamelen + 1;
        }

        $commentlen = 0;
        $comment = "";
        if ($flags & 16) {
            // C-style string COMMENT data in header
            if ($len - $headerlen - 1 < 8) {
                return false;    // Invalid format
            }
            $commentlen = strpos(substr($data,8+$extralen+$filenamelen),chr(0));
            if ($commentlen === false || $len - $headerlen - $commentlen - 1 < 8) {
                return false;    // Invalid header format
            }
            $comment = substr($data,$headerlen,$commentlen);
            $headerlen += $commentlen + 1;
        }

        $headercrc = "";
        if ($flags & 1) {
            // 2-bytes (lowest order) of CRC32 on header present
            if ($len - $headerlen - 2 < 8) {
                return false;    // Invalid format
            }
            $calccrc = crc32(substr($data,0,$headerlen)) & 0xffff;
            $headercrc = unpack("v", substr($data,$headerlen,2));
            $headercrc = $headercrc[1];
            if ($headercrc != $calccrc) {
                return false;    // Bad header CRC
            }
            $headerlen += 2;
        }

        // GZIP FOOTER - These be negative due to PHP's limitations
        $datacrc = unpack("V",substr($data,-8,4));
        $datacrc = $datacrc[1];
        $isize = unpack("V",substr($data,-4));
        $isize = $isize[1];

        // Perform the decompression:
        $bodylen = $len-$headerlen-8;
        if ($bodylen < 1) {
            // This should never happen - IMPLEMENTATION BUG!
            return null;
        }
        $body = substr($data,$headerlen,$bodylen);
        $data = "";
        if ($bodylen > 0) {
            switch ($method) {
                case 8:
                    // Currently the only supported compression method:
                    $data = gzinflate($body);
                    break;
                default:
                    // Unknown compression method
                    return false;
            }
        } else {
            // I'm not sure if zero-byte body content is allowed.
            // Allow it for now... Do nothing...
        }

        // Verifiy decompressed size and CRC32:
        // NOTE: This may fail with large data sizes depending on how
        //      PHP's integer limitations affect strlen() since $isize
        //      may be negative for large sizes.
        if ($isize != strlen($data) || crc32($data) != $datacrc) {
            // Bad format! Length or CRC doesn't match!
            return false;
        }
        return $data;
    }

    public static function array2xml($array, $encoding='UTF-8')
    {
        $xmlObj = new xml();
        return $xmlObj->array2xml($array, $encoding);
    }

    /**
     * CURL方式
     */
    public static function curl($url,$data, $timeout=30)
    {
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL,$url);      			// set url to post to
        curl_setopt($ch, CURLOPT_RETURNTRANSFER,1); 		// return into a variable
        curl_setopt($ch, CURLOPT_TIMEOUT, $timeout);      	// times out
        curl_setopt($ch, CURLOPT_POSTFIELDS, $data);     	// add POST fields
        $result = curl_exec($ch);
        if($error = curl_error($ch))
        {
            return $error;
        }
        return $result;
    }

    /**
     *字段穿特殊字符过滤
     * @param str $str
     */
    public static function stringfilter($str)
    {
        $filterArr=array('\'', '"','<','>','&','(','','\\','/');
        return str_replace($filterArr,'',$str);
    }

    public static function microtime(){
        list($usec, $sec) = explode(" ",microtime());
        return ((float)$usec + (float)$sec);
    }

    /**
     * 过滤数组中值为空数组的参数
     * @param  array
     * @param  array
     */
    public function filter_null($arr)
    {
    	foreach($arr as $key=>$val)
    	{
    		if(empty($arr[$key]) && $val!='0'){
    			$arr[$key]='';
    			continue;
    		}
    		if(is_array($val)){
    			$arr[$key]=$this->filter_null($val);
    		}else{
    			$str=trim(strval($val));
    			$arr[$key]=str_replace(array('<','>','&','"','\'','\\'),'',$str);
    		}
    	}
    	return $arr;
    }
    
    /**
     * 返回错误明细处理，统一处理为二维数组
     * @param  array
     * @param  array
     */
    public function getResultInfo($result_arr)
    {
    	$resultInfo = array();
    	if (!empty($result_arr)) {
    		$resultInfo = $result_arr;
    		$arr = array_pop($resultInfo);
    		if (!is_array($arr)) {
    			$resultInfo = array($result_arr);
    		} else {
    			$resultInfo = $result_arr;
    		}
    	}
    	return $resultInfo;
    }
    
    /**
     * 判断数组是一维数组还是二维数组
     * @param array
     * @return boolean
     */
    public function isArrayMulti($arr)
    {
    	if (!is_array($arr)) {
    		return false;
    	}
    	if (empty($arr)) {
    		return false;
    	} else {
    		if (!empty($arr['xmldata'])) {
    			$arr = $arr['xmldata'];
    		} 
    	}
    	if (!empty($arr['header'])) {
    		$KeyArr = array_keys($arr['header']);
    	} elseif (!empty($arr['data']['orderinfo'])) {
    		$KeyArr = array_keys($arr['data']['orderinfo']);
    	} elseif (!empty($arr['data']['header'])) {
    		$KeyArr = array_keys($arr['data']['header']);
    	} elseif (!empty($arr['data']['ordernos'])) {
    		$KeyArr = array_keys($arr['data']['ordernos']);
    	} else {
    		return false;
    	}

    	foreach ($KeyArr as $val)
    	{
    		if (!is_numeric($val)) {
    			return false;
    		}
    	}
    	return true;
    }

    /**
     * 将数组转换为推送的xml标准格式
     * @param array
     * @param array object
     * @return xml
     */
    public function arrayToXml($params, $xmlObj)
    {
    	$xmlData = '';
        if (empty($params['xmldata'])) {
    		$params = array('xmldata' => $params);
    	} 
    	$multiFlag = $this->isArrayMulti($params);
    	if (!empty($params['xmldata']['header'])) {  //商品资料、客商档案接口、入库单下发、出库单下发、库存查询
    		if (!$multiFlag) {
    			$params['xmldata']['header'] = array($params['xmldata']['header']);
    		}
    		foreach ($params['xmldata']['header'] as $k => $v)
    		{
    			if (!empty($v['detailsItem']) && is_array($v['detailsItem'])) {
    				if (empty($v['detailsItem'][0])) {
    					$v['detailsItem'] = array($v['detailsItem']);
    				}
    				$detailsItemXml = '';
    				foreach ($v['detailsItem'] as $a => $b)
    				{
    					$detailsItemXml .= '<detailsItem>' . $xmlObj->array2xml($b) . '</detailsItem>';
    				}
    				$detailsItemXml = substr($detailsItemXml, 13, -14);
    				$v['detailsItem'] = $detailsItemXml;
    			}
    			if (!empty($v['invoiceItem']) && is_array($v['invoiceItem'])) {
    				if (empty($v['invoiceItem'][0])) {
    					$v['invoiceItem'] = array($v['invoiceItem']);
    				}
    				$invoiceItemXml = '';
    				foreach ($v['invoiceItem'] as $a => $b)
    				{
    					$invoiceItemXml .= '<invoiceItem>' . $xmlObj->array2xml($b) . '</invoiceItem>';
    				}
    				$invoiceItemXml = substr($invoiceItemXml, 13, -14);
    				$v['invoiceItem'] = $invoiceItemXml;
    			}
    			$xmlData .= '<header>' . $xmlObj->array2xml($v) . '</header>';
    		}
    	} elseif (!empty($params['xmldata']['data']['orderinfo'])) {  //入库单状态明细回传、出库单状态回传、出库单明细回传
    		if (!$multiFlag) {
    			$params['xmldata']['data']['orderinfo'] = array($params['xmldata']['data']['orderinfo']);
    		}
    		foreach ($params['xmldata']['data']['orderinfo'] as $k => $v)
    		{
    			if (!empty($v['item']) && is_array($v['item'])) {
    				if (empty($v['item'][0])) {
    					$v['item'] = array($v['item']);
    				}
    				$itemXml = '';
    				foreach ($v['item'] as $a => $b)
    				{
    					$itemXml .= '<item>' . $xmlObj->array2xml($b) . '</item>';
    				}
    				$itemXml = substr($itemXml, 6, -7);
    				$v['item'] = $itemXml;
    			}
    			$xmlData .= '<orderinfo>' . $xmlObj->array2xml($v) . '</orderinfo>';
    		}
    		$xmlData = '<data>' . $xmlData . '</data>';
    	} elseif (!empty($params['xmldata']['data']['header'])) {
    		if (!$multiFlag) {
    			$params['xmldata']['data']['header'] = array($params['xmldata']['data']['header']);
    		}
    		foreach ($params['xmldata']['data']['header'] as $k => $v)
    		{
    			$xmlData .= '<header>' . $xmlObj->array2xml($v) . '</header>';
    		}
    		$xmlData = '<data>' . $xmlData . '</data>';
    	} elseif (!empty($params['xmldata']['data']['ordernos'])) {    //入库单取消、出库单取消
    		if (!$multiFlag) {
    			$params['xmldata']['data']['ordernos'] = array($params['xmldata']['data']['ordernos']);
    		}
    		foreach ($params['xmldata']['data']['ordernos'] as $k => $v)
    		{
    			$xmlData .= '<ordernos>' . $xmlObj->array2xml($v) . '</ordernos>';
    		}
    		$xmlData = '<data>' . $xmlData . '</data>';
    	} else {
    		$xmlData = '';
    	}
    	if ($xmlData != '') {
    		$xmlData = '<xmldata>' . $xmlData . '</xmldata>';
    	}
    	return $xmlData;
    }
}