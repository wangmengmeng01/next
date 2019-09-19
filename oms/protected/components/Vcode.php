<?php
/**
 * 验证码
 */
class vcode {
	// 验证码默认配置
	protected $params = array (
			'key' => 'captchaCode', // 存储验证码的key
			'height' => 35, //
			'width' => 85,
			'minLength' => 4,
			'maxLength' => 6 
	);
	// 字体文件路径
	protected $fontPath = '/protected/config/font.ttf';
	protected static $objVcode = null;
	
	private function __construct(){
		
	}
	
	/**
	 * 获取当前类的实例化对象
	 *
	 * @return Obj
	 */
	public static function getVcode() {
		if (self::$objVcode == null)
			self::$objVcode = new self ();
		return self::$objVcode;
	}
	
	/**
	 * 生成图形验证码
	 *
	 * @param
	 * array 验证码配置
	 */
	public function renderImage($params = array()) {
		if (extension_loaded ( 'gd' )) {
			$params = array_merge ( $this->params, $params );
			// 新建真色彩图像
			$image = imagecreatetruecolor ( $params ['width'], $params ['height'] );
			// 设置图像背景颜色
			$background = imagecolorallocate ( $image, 255, 255, 255 );
			imagecolortransparent ( $image, $background );
			// 给图像背景填充颜色
			imagefill ( $image, 0, 0, $background );
			$font = APP_ROOT . '/' . $this->fontPath; // 自定义字体
			$strs = array (
				2,3,4,5,6,7,8,9,'q','w','e','r','t','y','u','p','a','s','d','f','g','h','j','k','z','x','c','v','b','n','m'
			);
			$vcode = '';
			// 验证码长度
			$lenght = rand ( $params ['maxLength'], $params ['minLength'] );
			$offset = $params ['width'] / $lenght;
			$x = $offset / 2; // 水平位置
			if (file_exists ( $font ) && function_exists ( 'imagettftext' )) {
				for($i = 0; $i < 4; $i ++) {
					$str = $strs [rand ( 0, count ( $strs ) - 1 )];
					$vcode .= $str;
					if (rand ( 0, 1 )) {
						$str = strtoupper ( $str );
					}
					$textcolor = imagecolorallocate ( $image, mt_rand ( 0, 200 ), mt_rand ( 0, 200 ), mt_rand ( 0, 200 ) ); // 设置字体颜色
					imagettftext ( $image, 20, rand ( - 40, 40 ), rand ( $x, $x + 5 ), rand ( 20, 25 ), $textcolor, $font, $str );
					$x += $offset;
				}
			} else {
				for($i = 0; $i < 4; $i ++) {
					$str = $strs [rand ( 0, count ( $strs ) - 1 )];
					$vcode .= $str;
					if (rand ( 0, 1 )) {
						$str = strtoupper ( $str );
					}
					$textcolor = imagecolorallocate ( $image, mt_rand ( 0, 200 ), mt_rand ( 0, 200 ), mt_rand ( 0, 200 ) ); // 设置字体颜色
					imagestring ( $image, 5, rand ( $x, $x + intval ( $offset / 5 ) ), rand ( 0, $params ['height'] / 2 ), $str, $textcolor ); // //给图像水平写人字符
					$x += $offset;
				}
			}
			// 加入干扰像素
			for($i = 0; $i < 200; $i ++) {
				$randcolor = ImageColorallocate ( $image, rand ( 0, 255 ), rand ( 0, 255 ), rand ( 0, 255 ) );
				imagesetpixel ( $image, rand ( 0, $params ['width'] ), rand ( 0, $params ['height'] ), $randcolor );
			}
			
			Yii::app ()->session [$params ['key']] = strtolower ( $vcode );
			header ( "content-type:image/png" ); // 声明输出内容格式
			imagepng ( $image ); // 输入图像
			imagedestroy ( $image ); // 释放内存
			Yii::app ()->end ();
		} else {
			throw new CException ( Yii::t ( 'yii', 'GD库未开启，无法生成验证码！' ) );
		}
	}
	/**
	 * 验证值是否与验证码的值一致
	 *
	 * @param $value string
	 *        	用户输入的验证码
	 * @param $key string
	 *        	存储验证码的key
	 * @param $destory boolean
	 *        	验证后是否销毁验证码
	 * @return boolean 成功返回 true 否则返回 false;
	 *        
	 */
	public function verifyCode($value, $destory = false, $key = null) {
		$value = strtolower ( trim ( $value ) );
		$key = $key == null ? $this->params ['key'] : $key;
		$result = false;
		$session = Yii::app ()->session;
        
		if ($value != '' && $session [$key] == $value)
			$result = true;
		if ($destory == true)
			unset ($session[$key]);
		return $result;
	}
}