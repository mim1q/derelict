package com.github.mim1q.derelict.config;

import io.wispforest.owo.config.annotation.Config;

@Config(name = "derelict", wrapperName = "DerelictConfig")
public class DerelictConfigModel {
  public boolean fancyFlickeringLights = true;
}