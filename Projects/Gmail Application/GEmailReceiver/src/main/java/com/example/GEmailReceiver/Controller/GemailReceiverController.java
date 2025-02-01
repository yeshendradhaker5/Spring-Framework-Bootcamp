package com.example.GEmailReceiver.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.example.GEmailReceiver.Entity.Email;
import com.example.GEmailReceiver.Entity.EmailRequests;
import com.example.GEmailReceiver.Service.GEmailReceiverService;

@Controller
public class GemailReceiverController {

	@Autowired
	private GEmailReceiverService gEmailReceiverService;

	private RestTemplate restTemplate;

	public GemailReceiverController(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}

	@GetMapping("/")
	public String inbox(Model model) {
		List<Email> emailsList = gEmailReceiverService.getEmails();
		model.addAttribute("emailslist", emailsList);
		return "inbox";
	}

	@PostMapping("/sendmail")
	@ResponseBody
	public String sendMail(@ModelAttribute EmailRequests emailRequests, Model model) {

		try {
			
			String url = "http://localhost:8080/sendmail";
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			
			HttpEntity<EmailRequests> httpEntity = new HttpEntity<EmailRequests>(emailRequests, headers);
			
			ResponseEntity<String> response = restTemplate.postForEntity(url, httpEntity, String.class);
			
			if(response.getStatusCode() == HttpStatus.OK) {
				model.addAttribute("success", "Email sent successfully");
			}
			else
				model.addAttribute("failed", "Failed to send the email");
			
		} catch (Exception e) {
				model.addAttribute("ErrorMessage", "Process Failed with the message "+e.getMessage());
		}
		
		return "Mail Sent";
	}
}






