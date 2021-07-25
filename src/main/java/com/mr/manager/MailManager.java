package com.mr.manager;

import com.mr.config.SystemInitParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * @author Mr
 * @date 2021/7/25
 */
@Component
public class MailManager {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * Spring Boot 提供了一个发送邮件的简单抽象，使用的是下面这个接口，这里直接注入即可使用
	 */
	@Resource
	private JavaMailSender mailSender;

	/**
	 * 配置文件中我的qq邮箱
	 */
	@Resource
	private SystemInitParam systemInitParam;

	/**
	 * 简单文本邮件
	 *
	 * @param to      收件人
	 * @param subject 主题
	 * @param content 内容
	 */
	public boolean sendSimpleMail(String to, String subject, String content, HttpServletRequest request) {
		try {
			//创建SimpleMailMessage对象
			SimpleMailMessage message = new SimpleMailMessage();
			//邮件发送人
			message.setFrom(systemInitParam.getMailFrom());
			//邮件接收人
			message.setTo(to);
			//邮件主题
			message.setSubject(subject);
			//邮件内容
			message.setText(content);
			//发送邮件
			mailSender.send(message);
			return true;
		} catch (MailException e) {
			logger.error("send content mail to:[{}] failed", to, e);
			return false;
		}
	}

	/**
	 * html邮件
	 *
	 * @param to      收件人
	 * @param subject 主题
	 * @param content 内容
	 */
	public boolean sendHtmlMail(String to, String subject, String content, HttpServletRequest request) {
		//获取MimeMessage对象
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper messageHelper;
		try {
			messageHelper = new MimeMessageHelper(message, true);
			//邮件发送人
			messageHelper.setFrom(systemInitParam.getMailFrom());
			//邮件接收人
			messageHelper.setTo(subject);
			//邮件主题
			message.setSubject(subject);
			//邮件内容，html格式
			messageHelper.setText(content, true);
			//发送
			mailSender.send(message);
			//日志信息
			logger.info("send html mail to:[{}] success", to);
			return true;
		} catch (MessagingException e) {
			logger.error("send html mail to:[{}] failed", to, e);
			return false;
		}
	}

	/**
	 * 带附件的邮件
	 *
	 * @param to       收件人
	 * @param subject  主题
	 * @param content  内容
	 * @param filePath 附件
	 */
	public boolean sendAttachmentsMail(String to, String subject, String content, String filePath, HttpServletRequest request) {
		MimeMessage message = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(systemInitParam.getMailFrom());
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(content, true);

			FileSystemResource file = new FileSystemResource(new File(filePath));
			String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
			helper.addAttachment(fileName, file);
			mailSender.send(message);
			logger.info("send Attachment mail to:[{}] success", to);
			return true;
		} catch (MessagingException e) {
			logger.error("send Attachment mail to:[{}]  failed", to, e);
			return false;
		}
	}

}
