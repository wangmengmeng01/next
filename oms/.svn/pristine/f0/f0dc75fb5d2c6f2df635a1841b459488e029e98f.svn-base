<?php
class Ry extends CActiveRecord
{
	public static function model($className=__CLASS__)
	{
		return parent::model($className);
	}

	public function tableName()
	{
		return 'ydserver.ry';
	}
	
    public function relations()
	{
		return array(
				'company' => array(self::BELONGS_TO, 'Gs', 'gs','select' => 'bm,mc,lb')
		);
	}
	
	public function getRy()
	{
		return self::model()->with('company')->find('gh_oa=:gh_oa',array(':gh_oa'=>Yii::app()->user->name));
	}
	
	public static function setQx()
	{
		try {
			$row = self::model()->getRy();
			$qx = $row -> qx;
			$qx = explode(',',$qx);
			Yii::app()->user->setState('user_title', $row->name);
			Yii::app()->user->setState('gsbm',$row->company->bm);
			Yii::app()->user->setState('gsmc',$row->company->mc);
			Yii::app()->user->setState('gslb',$row->company->lb);
			return true;			
		} catch (Exception $e) {
			exit(0);
		}
	}
	
	public function login()
	{
		$result = array('status'=>false, 'msg'=>'');
		if(isset($_POST['Vcode']) && Vcode::getVcode()->verifyCode($_POST['Vcode'],true)) {
			$query = Yii::app()->db->createCommand();
			$query->select('A.USERPASS,A.EMPID,A.EMPSTATUS,B.gh_oa,B.name,C.bm,C.mc,C.lb');
			$query->from('ydserver.yd_cas_emp A');
			$query->leftjoin('ydserver.ry B','A.EMPID=B.gh_oa');
			$query->leftjoin('ydserver.gs C','B.gs=C.bm');
			$query->where('A.EMPID=:id',array(':id'=>$_POST['userName']));
			$query->limit(1);
			$rows = $query->queryRow();
			if(is_array($rows)) {
				if(isset($_POST['passWord']) && strtolower($rows['USERPASS']) == strtolower(base64_encode(md5($_POST['passWord'], true))) && $rows['EMPSTATUS'] == '11'){
					$identity = new CUserIdentity($rows['gh_oa'], date('Y-m-d H:i:s'));
					Yii::app()->user->login($identity, 0);
					Yii::app()->user->setState('soa_id', $rows['EMPID']);
					Yii::app()->user->setState('user_title', $rows['name']);
					Yii::app()->user->setState('gsbm',$rows['bm']);
					Yii::app()->user->setState('gsmc',$rows['mc']);
					Yii::app()->user->setState('gslb',$rows['lb']);
					//获取登陆者的角色
					Yii::app()->user->setState('user_role', $this->getUserRole());
					//获取登陆者的权限位
					Yii::app()->user->setState('user_all_pri', $this->getUserAllPri());
					session_regenerate_id();
					$result = array('status'=>true,'msg'=>'登录成功！');					
				} else {
					$result['msg'] = '用户名或密码错误！';
				}
			}else {
				$result['msg'] = '用户名或密码错误！';
			}
	    }else {
			$result['msg'] = '验证码错误！';		    
        }
        return $result;
	}
	
	/**
	 * 获取登陆用户的角色
	 */
	public function getUserRole()
	{
		$resultStr = '';
		$url = sprintf(AUTH_ROLE_URL, AUTH_APPID, Yii::app()->user->soa_id);
		$reponse = util::curl($url);
		if ($reponse) {
			$resultArr = json_decode($reponse,true);
			if($resultArr['retcode'] == '0' && $resultArr['result']){
				$resultStr = $resultArr['userRole'];
			}
		}
		return $resultStr;
	}
	
	/**
	 * 获取登陆用户的所有权限
	 */
	public function getUserAllPri()
	{
		$resultStr = '';
		$url = sprintf(AUTH_ALL_PRI_URL, AUTH_APPID, Yii::app()->user->soa_id, AUTH_OBJECTID);
		$reponse = util::curl($url);
		if ($reponse) {
			$resultArr = json_decode($reponse,true);
			if ($resultArr['retcode'] == '0' && !empty($resultArr['attrValues'])) {
				foreach ($resultArr['attrValues'] as $v)
				{
					if ($resultStr == '') {
						$resultStr = $v['memuid'];
					} else {
						$resultStr .= ',' . $v['memuid'];
					}
				}
			}
		}
        //获取由菜单所维护的权限
        $user_menus_access_str = $this->tranMenusToAccess($this->getUserMenus());
		return $resultStr.$user_menus_access_str;
	}
	
	/**
	 * 获取登陆者是否有访问某个权限位的权限
	 */
	public function hasPower($pos)
	{
		$url = sprintf(AUTH_SINGLE_PRI_URL,Yii::app()->user->soa_id,AUTH_APPID,AUTH_OBJECTID,AUTH_LOCKID,AUTH_ATTRID,$pos);
		$reponse = util::curl($url);
		if($reponse){
			$rpsArr = json_decode($reponse,true);
			if($rpsArr['retcode']==0 && $rpsArr['result']){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

    /**
     * @return mixed
     * 获取登陆用户的所有菜单
     */
    public function getUserMenus()
    {
        $url = sprintf(AUTH_MENU_URL, AUTH_APPID, Yii::app()->user->soa_id);
        $response = json_decode(util::curl($url));
        if($response->retcode == 0) {
            return $response->menus;
        } else {
            return '';
        }

    }

    public function tranMenusToAccess($menus){
        $access = '';
        if(!empty($menus)){
            foreach ($menus as $sub_menus){
                if(!empty($sub_menus->menus)){
                    $access .= $this->tranMenusToAccess($sub_menus->menus);
                }else{
                    if($sub_menus->status == 'A'){
                        $access .= ','.$sub_menus->icon;
                    }
                }
            }
        }
        return $access;
    }
	
}
