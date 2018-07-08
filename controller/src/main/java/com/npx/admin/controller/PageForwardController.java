package com.npx.admin.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @ClassName: PageForwardController  
 * @Description: 通用页面跳转的控制器
 * @author NiePengXiang
 * @date 2018年7月7日  
 */
@Controller
public class PageForwardController {
	
	/** 跳转方法 */
	@GetMapping("/{viewName}")
	public String forward(@PathVariable("viewName")String viewName){
		if(StringUtils.isNoneBlank(viewName)){
			return viewName;
		}
		return null;
	}
	
	
	
}