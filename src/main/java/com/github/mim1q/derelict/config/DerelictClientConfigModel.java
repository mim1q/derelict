package com.github.mim1q.derelict.config;

import blue.endless.jankson.Comment;
import io.wispforest.owo.config.annotation.Config;

@SuppressWarnings("unused")
@Config(name = "derelict_client", wrapperName = "DerelictClientConfig")
public class DerelictClientConfigModel {
  @Comment("Opacity of the little honeycomb icon next to waxed blocks in your inventory. 0.0 to disable.")
  public float waxedIndicatorOpacity = 0.5F;

  @Comment("Scale of the honeycomb icon next to waxed blocks in your inventory. 1.0 is full item slot size.")
  public float waxedIndicatorScale = 0.5F;

  @Comment("Whether the block highlights should be shown when holding a Waxing Staff or Honeycomb")
  public boolean waxableHighlights = true;

  @Comment("Whether the block highlights should be shown when holding an Aging Staff")
  public boolean ageableHighlights = true;

  @Comment("The maximum distance of the block highlights when holding a Waxing Staff, Honeycomb or Aging Staff. 0 to disable")
  public int waxableAndAgeableHightlightRange = 16;

  @Comment("Whether the crosshair tip should be shown when holding a Waxing Staff or Honeycomb")
  public boolean waxableCrosshairTip = true;

  @Comment("Whether the crosshair tip should be shown when holding an Aging Staff")
  public boolean ageableCrosshairTip = true;
}
