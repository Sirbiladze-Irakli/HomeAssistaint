package com.sirbiladze.HomeAssistaint.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController extends BaseController {

  @GetMapping
  public String get() {
    return "it's work";
  }

}
