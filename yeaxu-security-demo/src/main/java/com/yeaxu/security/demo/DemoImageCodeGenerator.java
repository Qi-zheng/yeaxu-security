package com.yeaxu.security.demo;

import org.springframework.web.context.request.ServletWebRequest;

import com.yeaxu.security.core.validate.code.ImageCode;
import com.yeaxu.security.core.validate.code.ValidateCodeGenerator;

/**
 * 以增量的方式开发代码
 * 主要是在上层包中使用了@ConditionalOnMissingBean(name = "imageCodeGenerator")
 * 配置以达到用户不重写功能时使用默认功能，加入该Bean后则使用用户逻辑来处理
 *   // 当容器中不存在名字叫做imageCodeGenerator的Bean时注册以下的默认的Bean
 * @author pcitc
 *
 */
//@Component("imageCodeGenerator")  
public class DemoImageCodeGenerator implements ValidateCodeGenerator {

	@Override
	public ImageCode generate(ServletWebRequest request) {
		System.out.println("更高级的图形验证码");
		return null;
	}

}
