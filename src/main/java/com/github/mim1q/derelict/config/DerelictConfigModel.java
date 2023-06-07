package com.github.mim1q.derelict.config;

import io.wispforest.owo.config.annotation.Config;
import blue.endless.jankson.Comment;

@Config(name = "derelict", wrapperName = "DerelictConfig")
public class DerelictConfigModel {
  @Comment("""
    Which type of flickering lights to use:
      FAST: best performance, doesn't have an effect on the emitted light level
      FANCY: affects emitted light, worse performance, best option to use with custom texture packs
      FABULOUS: affects emitted light, looks the best, worst performance, best option for vanilla textures"""
  )
  public FlickeringLightsSetting flickeringLights = FlickeringLightsSetting.FABULOUS;

  public enum FlickeringLightsSetting {
    FAST, FANCY, FABULOUS
  }
}