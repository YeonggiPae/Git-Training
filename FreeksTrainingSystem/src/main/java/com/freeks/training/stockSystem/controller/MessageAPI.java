package com.freeks.training.stockSystem.controller;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.freeks.training.stockSystem.util.MessageEnum;

@RestController
@RequestMapping("/api/messages")
public class MessageAPI {
	
	@GetMapping
	public ResponseEntity<Map<String, String>> getmessages(){
		Map<String, String> messages = Arrays.stream(MessageEnum.values())
				.collect(Collectors.toMap(Enum::name, MessageEnum::getMessage));
		
		return ResponseEntity.ok(messages);
		
	}

}
