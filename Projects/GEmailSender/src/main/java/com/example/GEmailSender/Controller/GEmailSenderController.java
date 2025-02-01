package com.example.GEmailSender.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.GEmailSender.Model.GEmailSender;
import com.example.GEmailSender.Service.GEmailSenderService;

@Controller
public class GEmailSenderController {
	
	@Autowired
	GEmailSenderService gEmailSenderService;
	
	@GetMapping("/")
	public String emailform() {
		return "GEmailSenderForm";
	}
	
	@PostMapping("/sendmail")
	@ResponseBody
	public String sendMail(@RequestBody GEmailSender emailSender) {
		gEmailSenderService.sendMail(emailSender);
		return "sent";
	}
	
}
