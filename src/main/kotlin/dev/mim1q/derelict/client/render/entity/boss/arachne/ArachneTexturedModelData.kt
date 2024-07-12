package dev.mim1q.derelict.client.render.entity.boss.arachne

import net.minecraft.client.model.*

object ArachneTexturedModelData {
    fun create(): TexturedModelData = ModelData().let {
        it.root.apply {
            addChild("body", ModelPartBuilder.create(), ModelTransform.pivot(0F, 14F, 6F)).apply {
                addChild("sternum", ModelPartBuilder.create().uv(0, 46).cuboid(-8F, -4.75F, -0.5F, 16F, 10F, 24F), ModelTransform.of(0F, 0F, -25F, 0.2618F, 0F, 0F)).apply {
                    addChild("sternumHair1_r1", ModelPartBuilder.create().uv(-8, 88).cuboid(-8F, -18.25F, -14F, 16F, 0F, 8F), ModelTransform.of(0F, 2.25F, 35.5F, 0.6109F, 0F, 0F))
                    addChild("sternumHair0_r1", ModelPartBuilder.create().uv(-8, 80).cuboid(-8F, -18.25F, -14F, 16F, 0F, 8F), ModelTransform.of(0F, 2.25F, 26.5F, 0.6109F, 0F, 0F))
                    addChild("abdomen", ModelPartBuilder.create().uv(0, 0).cuboid(-10F, -8.7547F, -0.6287F, 20F, 18F, 28F), ModelTransform.of(0F, -0.7453F, 22.8787F, -0.3491F, 0F, 0F)).apply {
                        addChild("egg_base", ModelPartBuilder.create().uv(0, 101).cuboid(-7F, -2.4288F, -11.012F, 14F, 5F, 22F), ModelTransform.pivot(0F, -11.326F, 13.3833F))
                        addChild("eggs", ModelPartBuilder.create(), ModelTransform.pivot(0F, -2.826F, 22.3833F)).apply {
                            addChild("egg16", ModelPartBuilder.create().uv(0, 58).cuboid(-3F, -2.9288F, -3.012F, 6F, 6F, 6F), ModelTransform.pivot(0F, -12F, -5F))
                            addChild("egg15", ModelPartBuilder.create().uv(0, 58).cuboid(-3F, -2.9288F, -3.012F, 6F, 6F, 6F), ModelTransform.pivot(8F, -6F, 1F))
                            addChild("egg14", ModelPartBuilder.create().uv(0, 58).cuboid(-3F, -2.9288F, -3.012F, 6F, 6F, 6F), ModelTransform.pivot(-9F, -6F, -2F))
                            addChild("egg13", ModelPartBuilder.create().uv(0, 58).cuboid(-3F, -2.9288F, -3.012F, 6F, 6F, 6F), ModelTransform.pivot(-5F, -11F, -19F))
                            addChild("egg12", ModelPartBuilder.create().uv(8, 50).cuboid(-2F, -1.9288F, -2.012F, 4F, 4F, 4F), ModelTransform.pivot(-1F, -5F, -22F))
                            addChild("egg11", ModelPartBuilder.create().uv(8, 50).cuboid(-2F, -1.9288F, -2.012F, 4F, 4F, 4F), ModelTransform.pivot(-7F, -5F, 4F))
                            addChild("egg10", ModelPartBuilder.create().uv(8, 50).cuboid(-2F, -1.9288F, -2.012F, 4F, 4F, 4F), ModelTransform.pivot(0F, -5F, 5F))
                            addChild("egg9", ModelPartBuilder.create().uv(8, 50).cuboid(-2F, -1.9288F, -2.012F, 4F, 4F, 4F), ModelTransform.pivot(6F, -11F, -3F))
                            addChild("egg8", ModelPartBuilder.create().uv(8, 50).cuboid(-2F, -1.9288F, -2.012F, 4F, 4F, 4F), ModelTransform.pivot(10F, -7F, -6F))
                            addChild("egg7", ModelPartBuilder.create().uv(8, 50).cuboid(-2F, -1.9288F, -2.012F, 4F, 4F, 4F), ModelTransform.pivot(-1F, -11F, 1F))
                            addChild("egg6", ModelPartBuilder.create().uv(8, 50).cuboid(-2F, -1.9288F, -2.012F, 4F, 4F, 4F), ModelTransform.pivot(7F, -6F, -13F))
                            addChild("egg5", ModelPartBuilder.create().uv(8, 50).cuboid(-2F, -1.9288F, -2.012F, 4F, 4F, 4F), ModelTransform.pivot(-9F, -6F, -14F))
                            addChild("egg4", ModelPartBuilder.create().uv(8, 50).cuboid(-2F, -1.9288F, -2.012F, 4F, 4F, 4F), ModelTransform.pivot(-7F, -10F, -7F))
                            addChild("egg3", ModelPartBuilder.create().uv(8, 50).cuboid(-2F, -1.9288F, -2.012F, 4F, 4F, 4F), ModelTransform.pivot(-1F, -11F, -13F))
                            addChild("egg2", ModelPartBuilder.create().uv(0, 58).cuboid(-3F, -2.9288F, -3.012F, 6F, 6F, 6F), ModelTransform.pivot(8F, -6F, -21F))
                            addChild("egg1", ModelPartBuilder.create().uv(8, 50).cuboid(-2F, -1.9288F, -2.012F, 4F, 4F, 4F), ModelTransform.pivot(4F, -12F, -19F))
                            addChild("egg0", ModelPartBuilder.create().uv(0, 58).cuboid(-3F, -2.9288F, -3.012F, 6F, 6F, 6F), ModelTransform.pivot(4F, -14F, -11F))
                        }
                    }
                }
                addChild("head", ModelPartBuilder.create().uv(56, 46).cuboid(-6F, -4.3905F, -11.8595F, 12F, 8F, 12F), ModelTransform.pivot(0F, -2.6095F, -23.1405F)).apply {
                    addChild("headHair1_r1", ModelPartBuilder.create().uv(0, 11).cuboid(-6F, -5F, 0F, 12F, 5F, 0F), ModelTransform.of(0F, -4.3905F, -4.8595F, -0.7854F, 0F, 0F))
                    addChild("headHair0_r1", ModelPartBuilder.create().uv(0, 16).cuboid(-6F, -3F, 0F, 12F, 3F, 0F), ModelTransform.of(0F, -4.3905F, -7.8595F, -0.7854F, 0F, 0F))
                    addChild("right_fang", ModelPartBuilder.create().uv(14, 19).cuboid(-2F, 0F, -2F, 4F, 6F, 3F), ModelTransform.of(-3F, 0.6095F, -11.8595F, 0F, 0.4363F, 0F)).apply {
                        addChild("rightFangEnd_r1", ModelPartBuilder.create().uv(8, 43).cuboid(0F, -1.5F, -1.5F, 0F, 3F, 3F), ModelTransform.of(0F, 7.5F, -0.5F, 0F, 1.0472F, 0F))
                    }
                    addChild("left_fang", ModelPartBuilder.create().uv(0, 19).cuboid(-2F, 0F, -2F, 4F, 6F, 3F), ModelTransform.of(3F, 0.6095F, -11.8595F, 0F, -0.4363F, 0F)).apply {
                        addChild("leftFangEnd_r1", ModelPartBuilder.create().uv(11, 16).cuboid(0F, -1.5F, -1.5F, 0F, 3F, 3F), ModelTransform.of(0F, 7.5F, -0.5F, 0F, -1.0472F, 0F))
                    }
                }
                addChild("left_leg_joint0", ModelPartBuilder.create(), ModelTransform.pivot(8F, -3F, -21F)).apply {
                    addChild("left_leg0", ModelPartBuilder.create().uv(68, 8).cuboid(-2F, -2F, -2F, 24F, 4F, 4F, Dilation(0.1F)).uv(56, 66).cuboid(-2F, -5F, 0F, 24F, 3F, 0F).uv(68, 18).cuboid(-2F, 2F, 0F, 24F, 1F, 0F).uv(0, 0).cuboid(15F, -2.5F, -2.5F, 7F, 6F, 5F, Dilation(0.25F)).uv(0, 46).cuboid(22F, -3.5F, 0F, 4F, 8F, 0F), ModelTransform.NONE).apply {
                        addChild("left_leg_front0", ModelPartBuilder.create().uv(68, 0).cuboid(0F, -2F, -2F, 24F, 4F, 4F).uv(68, 16).cuboid(0F, -4F, -0.1F, 24F, 2F, 0F).uv(68, 19).cuboid(0F, 2F, 0F, 24F, 1F, 0F).uv(19, 0).cuboid(24F, -2F, 0F, 4F, 4F, 0F), ModelTransform.pivot(20F, 0F, 0F))
                    }
                }
                addChild("left_leg_joint1", ModelPartBuilder.create(), ModelTransform.pivot(8F, -5F, -15F)).apply {
                    addChild("left_leg1", ModelPartBuilder.create().uv(68, 8).cuboid(-2F, -2F, -2F, 24F, 4F, 4F, Dilation(0.1F)).uv(56, 66).cuboid(-2F, -5F, 0F, 24F, 3F, 0F).uv(68, 18).cuboid(-2F, 2F, 0F, 24F, 1F, 0F).uv(0, 0).cuboid(15F, -2.5F, -2.5F, 7F, 6F, 5F, Dilation(0.25F)).uv(0, 46).cuboid(22F, -3.5F, 0F, 4F, 8F, 0F), ModelTransform.NONE).apply {
                        addChild("left_leg_front1", ModelPartBuilder.create().uv(68, 0).cuboid(0F, -2F, -2F, 24F, 4F, 4F).uv(68, 16).cuboid(0F, -4F, -0.1F, 24F, 2F, 0F).uv(68, 19).cuboid(0F, 2F, 0F, 24F, 1F, 0F).uv(19, 0).cuboid(24F, -2F, 0F, 4F, 4F, 0F), ModelTransform.pivot(20F, 0F, 0F))
                    }
                }
                addChild("left_leg_joint2", ModelPartBuilder.create(), ModelTransform.pivot(8F, -7F, -9F)).apply {
                    addChild("left_leg2", ModelPartBuilder.create().uv(68, 8).cuboid(-2F, -2F, -2F, 24F, 4F, 4F, Dilation(0.1F)).uv(56, 66).cuboid(-2F, -5F, 0F, 24F, 3F, 0F).uv(68, 18).cuboid(-2F, 2F, 0F, 24F, 1F, 0F).uv(0, 0).cuboid(15F, -2.5F, -2.5F, 7F, 6F, 5F, Dilation(0.25F)).uv(0, 46).cuboid(22F, -3.5F, -0.25F, 4F, 8F, 0F), ModelTransform.NONE).apply {
                        addChild("left_leg_front2", ModelPartBuilder.create().uv(68, 0).cuboid(0F, -2F, -2F, 24F, 4F, 4F).uv(68, 16).cuboid(0F, -4F, -0.1F, 24F, 2F, 0F).uv(68, 19).cuboid(0F, 2F, 0F, 24F, 1F, 0F).uv(19, 0).cuboid(24F, -2F, 0F, 4F, 4F, 0F), ModelTransform.pivot(20F, 0F, 0F))
                    }
                }
                addChild("left_leg_joint3", ModelPartBuilder.create(), ModelTransform.pivot(8F, -9F, -3F)).apply {
                    addChild("left_leg3", ModelPartBuilder.create().uv(68, 8).cuboid(-2F, -2F, -2F, 24F, 4F, 4F, Dilation(0.1F)).uv(56, 66).cuboid(-2F, -5F, 0F, 24F, 3F, 0F).uv(68, 18).cuboid(-2F, 2F, 0F, 24F, 1F, 0F).uv(0, 0).cuboid(15F, -2.5F, -2.5F, 7F, 6F, 5F, Dilation(0.25F)).uv(0, 46).cuboid(22F, -3.5F, 0F, 4F, 8F, 0F), ModelTransform.NONE).apply {
                        addChild("left_leg_front3", ModelPartBuilder.create().uv(68, 0).cuboid(0F, -2F, -2F, 24F, 4F, 4F).uv(68, 16).cuboid(0F, -4F, -0.1F, 24F, 2F, 0F).uv(68, 19).cuboid(0F, 2F, 0F, 24F, 1F, 0F).uv(19, 0).cuboid(24F, -2F, 0F, 4F, 4F, 0F), ModelTransform.pivot(20F, 0F, 0F))
                    }
                }
                addChild("right_leg_joint0", ModelPartBuilder.create(), ModelTransform.pivot(-8F, -3F, -21F)).apply {
                    addChild("right_leg0", ModelPartBuilder.create().uv(68, 8).mirrored().cuboid(-22F, -2F, -2F, 24F, 4F, 4F, Dilation(0.1F)).uv(56, 66).mirrored().cuboid(-22F, -5F, 0F, 24F, 3F, 0F).uv(68, 18).mirrored().cuboid(-22F, 2F, 0F, 24F, 1F, 0F).uv(0, 0).mirrored().cuboid(-22F, -2.5F, -2.5F, 7F, 6F, 5F, Dilation(0.25F)).uv(0, 46).mirrored().cuboid(-26F, -3.5F, 0F, 4F, 8F, 0F), ModelTransform.NONE).apply {
                        addChild("right_leg_front0", ModelPartBuilder.create().uv(68, 0).mirrored().cuboid(-24F, -2F, -2F, 24F, 4F, 4F).uv(68, 16).mirrored().cuboid(-24F, -4F, -0.1F, 24F, 2F, 0F).uv(68, 19).mirrored().cuboid(-24F, 2F, 0F, 24F, 1F, 0F).uv(19, 0).mirrored().cuboid(-28F, -2F, 0F, 4F, 4F, 0F), ModelTransform.pivot(-20F, 0F, 0F))
                    }
                }
                addChild("right_leg_joint1", ModelPartBuilder.create(), ModelTransform.pivot(-8F, -5F, -15F)).apply {
                    addChild("right_leg1", ModelPartBuilder.create().uv(68, 8).mirrored().cuboid(-22F, -2F, -2F, 24F, 4F, 4F, Dilation(0.1F)).uv(56, 66).mirrored().cuboid(-22F, -5F, 0F, 24F, 3F, 0F).uv(68, 18).mirrored().cuboid(-22F, 2F, 0F, 24F, 1F, 0F).uv(0, 0).mirrored().cuboid(-22F, -2.5F, -2.5F, 7F, 6F, 5F, Dilation(0.25F)).uv(0, 46).mirrored().cuboid(-26F, -3.5F, 0F, 4F, 8F, 0F), ModelTransform.NONE).apply {
                        addChild("right_leg_front1", ModelPartBuilder.create().uv(68, 0).mirrored().cuboid(-24F, -2F, -2F, 24F, 4F, 4F).uv(68, 16).mirrored().cuboid(-24F, -4F, -0.1F, 24F, 2F, 0F).uv(68, 19).mirrored().cuboid(-24F, 2F, 0F, 24F, 1F, 0F).uv(19, 0).mirrored().cuboid(-28F, -2F, 0F, 4F, 4F, 0F), ModelTransform.pivot(-20F, 0F, 0F))
                    }
                }
                addChild("right_leg_joint2", ModelPartBuilder.create(), ModelTransform.pivot(-8F, -7F, -9F)).apply {
                    addChild("right_leg2", ModelPartBuilder.create().uv(68, 8).mirrored().cuboid(-22F, -2F, -2F, 24F, 4F, 4F, Dilation(0.1F)).uv(56, 66).mirrored().cuboid(-22F, -5F, 0F, 24F, 3F, 0F).uv(68, 18).mirrored().cuboid(-22F, 2F, 0F, 24F, 1F, 0F).uv(0, 0).mirrored().cuboid(-22F, -2.5F, -2.5F, 7F, 6F, 5F, Dilation(0.25F)).uv(0, 46).mirrored().cuboid(-26F, -3.5F, -0.25F, 4F, 8F, 0F), ModelTransform.NONE).apply {
                        addChild("right_leg_front2", ModelPartBuilder.create().uv(68, 0).mirrored().cuboid(-24F, -2F, -2F, 24F, 4F, 4F).uv(68, 16).mirrored().cuboid(-24F, -4F, -0.1F, 24F, 2F, 0F).uv(68, 19).mirrored().cuboid(-24F, 2F, 0F, 24F, 1F, 0F).uv(19, 0).mirrored().cuboid(-28F, -2F, 0F, 4F, 4F, 0F), ModelTransform.pivot(-20F, 0F, 0F))
                    }
                }
                addChild("right_leg_joint3", ModelPartBuilder.create(), ModelTransform.pivot(-8F, -9F, -3F)).apply {
                    addChild("right_leg3", ModelPartBuilder.create().uv(68, 8).mirrored().cuboid(-22F, -2F, -2F, 24F, 4F, 4F, Dilation(0.1F)).uv(56, 66).mirrored().cuboid(-22F, -5F, 0F, 24F, 3F, 0F).uv(68, 18).mirrored().cuboid(-22F, 2F, 0F, 24F, 1F, 0F).uv(0, 0).mirrored().cuboid(-22F, -2.5F, -2.5F, 7F, 6F, 5F, Dilation(0.25F)).uv(0, 46).mirrored().cuboid(-26F, -3.5F, 0F, 4F, 8F, 0F), ModelTransform.NONE).apply {
                        addChild("right_leg_front3", ModelPartBuilder.create().uv(68, 0).mirrored().cuboid(-24F, -2F, -2F, 24F, 4F, 4F).uv(68, 16).mirrored().cuboid(-24F, -4F, -0.1F, 24F, 2F, 0F).uv(68, 19).mirrored().cuboid(-24F, 2F, 0F, 24F, 1F, 0F).uv(19, 0).mirrored().cuboid(-28F, -2F, 0F, 4F, 4F, 0F), ModelTransform.pivot(-20F, 0F, 0F))
                    }
                }
            }
        }
        TexturedModelData.of(it, 128, 128)
    }
}