/**
 * 名称: TestController.java
 * 描述: 
 * 类型: JAVA
 * 最近修改时间:2014年8月6日 下午7:58:14
 * @since  2014年8月6日
 * @author malb
 */ 
package com.shiyi.fileservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author malb
 *
 */

@Controller
public class TestController {
	
	
	@RequestMapping(value="test", method=RequestMethod.GET)
	public ModelAndView test() {
		return new ModelAndView("index");
	}
	
}
