package com.yundagalaxy.management.controller;

import com.yundagalaxy.management.service.CommonToolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 一段话简述功能。
 * <p>
 * Created by MiaoYuanMeng on 2019/10/29.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/common")
@Api(value = "公共接口调用", tags = "公共接口调用")
public class CommonToolController extends BladeController {

    private CommonToolService commonToolService;

    /**
     * 查询省
     */
    @GetMapping("/getProvinces")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "查询省", notes = "查询省")
    public R<List<Map<String,Object>>> getProvinces() {
        List<Map<String,Object>> list = commonToolService.getProvinces();
        return R.data(list);
    }

    /**
     * 查询市
     */
    @GetMapping("/getCitys")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "查询市", notes = "查询市")
    public R<List<Map<String,Object>>> getCitys(@RequestParam("ProvinceID") String ProvinceID) {
        List<Map<String,Object>> list = commonToolService.getCitys(ProvinceID);
        return R.data(list);
    }

    /**
     * 查询区
     */
    @GetMapping("/getCountys")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "查询市", notes = "查询市")
    public R<List<Map<String,Object>>> getCountys(@RequestParam("CityID") String CityID) {
        List<Map<String,Object>> list = commonToolService.getCountys(CityID);
        return R.data(list);
    }

    /**
     * 查询下级网点
     */
    @GetMapping("/getLowerOrgCode")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "查询下级网点", notes = "查询下级网点")
    public R<List<Map<String,Object>>> getLowerOrgCode(BladeUser user) {
        List<Map<String,Object>> list = commonToolService.getLowerOrgCode(user.getDeptId());
        return R.data(list);
    }
}
