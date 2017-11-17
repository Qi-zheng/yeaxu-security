/**
 * 
 */
package com.yeaxu.security.core.validate.code;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import com.yeaxu.security.core.validate.code.sms.SmsCodeSender;

/**
 * @author zhailiang
 *
 */
@RestController
public class ValidateCodeController {

//	@Autowired
//	private ValidateCodeProcessorHolder validateCodeProcessorHolder;
	
	public static final String SESSION_KEY = "SESSION_KEY_IMAGE_CODE";

	private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
	
	@Autowired
	ValidateCodeGenerator imageCodeGenerator;
	
	@Autowired
	ValidateCodeGenerator smsCodeGenerator;
	
	@Autowired
	SmsCodeSender smsCodeSender;
	
	/**
	 * 创建验证码，根据验证码类型不同，调用不同的 {@link ValidateCodeProcessor}接口实现
	 * 
	 * @param request
	 * @param response
	 * @param type
	 * @throws Exception
	 */
	@GetMapping("/code/image")
	public void createCode(HttpServletRequest request, HttpServletResponse response/*, @PathVariable String type*/)
			throws Exception {
		//validateCodeProcessorHolder.findValidateCodeProcessor(type).create(new ServletWebRequest(request, response));
		ImageCode imageCode = (ImageCode)imageCodeGenerator.generate(new ServletWebRequest(request));
		
		sessionStrategy.setAttribute(new ServletWebRequest(request), SESSION_KEY, imageCode);
		ImageIO.write(imageCode.getImage(), "JPEG", response.getOutputStream());
	}
	
	@GetMapping("/code/sms")
	public void createSmsCode(HttpServletRequest request, HttpServletResponse response/*, @PathVariable String type*/)
			throws Exception {
		//validateCodeProcessorHolder.findValidateCodeProcessor(type).create(new ServletWebRequest(request, response));
		ValidateCode smsCode = smsCodeGenerator.generate(new ServletWebRequest(request));
		
		sessionStrategy.setAttribute(new ServletWebRequest(request), SESSION_KEY, smsCode);
		String mobile = ServletRequestUtils.getRequiredStringParameter(request, "mobile");
		smsCodeSender.send(mobile, smsCode.getCode());
	}
	

}
