package dev.mim1q.derelict.config;

import io.wispforest.owo.config.annotation.Config;
import blue.endless.jankson.Comment;

@SuppressWarnings("unused")
@Config(name = "derelict", wrapperName = "DerelictConfig")
public class DerelictConfigModel {
  @Comment("""
    Which type of flickering lights to use:
      FAST: Best performance, doesn't have an effect on the emitted light level
      FANCY: Affects emitted light, worse performance. Best option to use with custom texture packs
      FABULOUS: Affects emitted light, looks the best, slightly worse performance than fancy. Best option for vanilla textures
                
    Note: when switching from FAST to FANCY/FABULOUS, all existing flickering light blocks will have to be replaced in order for the change to take place"""
  )
  public FlickeringLightsSetting flickeringLights = FlickeringLightsSetting.FABULOUS;

  public enum FlickeringLightsSetting {
    FAST, FANCY, FABULOUS
  }
}