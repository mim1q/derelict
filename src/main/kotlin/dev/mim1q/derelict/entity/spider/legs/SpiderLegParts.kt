package dev.mim1q.derelict.entity.spider.legs

import dev.mim1q.derelict.util.extensions.radians
import net.minecraft.client.model.ModelPart

class SpiderLegParts(
    val joint: ModelPart,
    val upper: ModelPart,
    val lower: ModelPart,
) {
    fun applyIk(ik: SpiderLegIKSolver, yaw: Float, deltaTime: Float) {
        joint.yaw = ik.getYaw(deltaTime) - yaw.radians()
        upper.roll = -ik.getUpperRoll(deltaTime)
        lower.roll = -ik.getLowerRoll(deltaTime)
    }

    companion object {
        fun create(root: ModelPart, index: Int, right: Boolean): SpiderLegParts {
            val side = if (right) "right" else "left"
            val joint = root.getChild("${side}_leg_joint$index")
            val upper = joint.getChild("${side}_leg$index")
            val lower = upper.getChild("${side}_leg_front$index")
            return SpiderLegParts(joint, upper, lower)
        }

        fun createArray(root: ModelPart): Array<SpiderLegParts> = arrayOf(
            create(root, 0, false),
            create(root, 4, true),
            create(root, 1, false),
            create(root, 5, true),
            create(root, 2, false),
            create(root, 6, true),
            create(root, 3, false),
            create(root, 7, true)
        )
    }
}