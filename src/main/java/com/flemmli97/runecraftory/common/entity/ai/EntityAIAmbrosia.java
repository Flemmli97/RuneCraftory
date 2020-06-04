package com.flemmli97.runecraftory.common.entity.ai;

import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.entity.monster.boss.EntityAmbrosia;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class EntityAIAmbrosia extends EntityAIAttackBase<EntityAmbrosia> {

    private int moveDelay;
    private boolean moveFlag,iddleFlag,clockwise, pollen;

    public EntityAIAmbrosia(EntityAmbrosia entity) {
        super(entity);
    }

    @Override
    public AnimatedAction randomAttack() {
        AnimatedAction anim = this.attacker.chainAnim(this.prevAnim);
        if(anim==null || (anim.getID().equals("pollen") && !pollen))
            anim = this.attacker.getRandomAnimation(EntityMobBase.AnimationType.GENERICATTACK);
        if ((!anim.getID().equals(this.prevAnim) || pollen) && !(this.prevAnim.equals("kick_3") && anim.getID().equals("kick_1"))) {
            if(anim.getID().equals("pollen"))
                pollen = !pollen;
            return anim;
        }
        return this.randomAttack();
    }

    @Override
    public int coolDown(AnimatedAction anim) {
        return !pollen && anim.getID().equals("pollen")?this.attacker.animationCooldown(null):this.attacker.animationCooldown(this.next);
    }

    @Override
    public void resetTask() {
        super.resetTask();
        this.moveDelay=0;
        this.moveFlag=false;
        this.pollen=false;
    }
    
    @Override
    public void handlePreAttack() {
        this.iddleFlag = false;
        switch (this.next.getID()) {
            case "butterfly":
                if (!this.moveFlag) {
                    if (this.distanceToTargetSq < 20.0) {
                        BlockPos pos = this.randomPosAwayFrom(this.target, 5.0f);
                        this.attacker.getNavigator().tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), 1);
                    }
                    this.moveDelay = 44 + this.attacker.getRNG().nextInt(10);;
                    this.moveFlag = true;
                } else if (this.moveDelay-- <= 0 || this.attacker.getNavigator().noPath()) {
                    this.attacker.setAiVarHelper(new double[] {this.target.posX, this.target.posY-2, this.target.posZ});
                    this.movementDone = true;
                    this.moveFlag=false;
                }
                break;
            case "sleep":
            case "kick_1":
                if (!this.moveFlag) {
                    this.attacker.getNavigator().tryMoveToEntityLiving(this.target, 1.2);
                    this.moveDelay = 30 + this.attacker.getRNG().nextInt(10);
                    this.moveFlag = true;
                } else if (this.moveDelay-- <= 0 || this.attacker.getNavigator().noPath()) {
                    this.movementDone = true;
                    this.moveFlag = false;
                }
                break;
            case "pollen":
                if(!this.pollen) {
                    this.movementDone = true;
                    int length = this.next.getLength();
                    Vec3d vec = this.target.getPositionVector().subtract(this.attacker.getPositionVector()).normalize().scale(6);
                    this.attacker.setAiVarHelper(new double[] {vec.x/length, 0, vec.z/length});
                }
                else {
                    if (!this.moveFlag) {
                        int length = this.next.getLength();
                        this.attacker.getNavigator().tryMoveToEntityLiving(this.target, 1.2);
                        this.moveDelay = 30 + this.attacker.getRNG().nextInt(10);
                        this.moveFlag = true;
                        Vec3d vec = this.target.getPositionVector().subtract(this.attacker.getPositionVector()).normalize().scale(4);
                        this.attacker.setAiVarHelper(new double[] {vec.x/length, 0, vec.z/length});
                    } else if (this.moveDelay-- <= 0 || this.attacker.getNavigator().noPath() || this.distanceToTargetSq < 4) {
                        this.movementDone = true;
                        this.moveFlag = false;
                    }
                }
                break;
            case "wave":
                if(!this.moveFlag){
                    if(this.distanceToTargetSq > 16) {
                        this.attacker.getNavigator().tryMoveToEntityLiving(this.target, 1.2);
                        this.moveDelay = 10 + this.attacker.getRNG().nextInt(10);
                    }
                    this.moveDelay+=10;
                    this.moveFlag=true;
                }
                else if (this.moveDelay-- <= 0 || this.attacker.getNavigator().noPath()) {
                    this.movementDone = true;
                    this.moveFlag=false;
                }
                break;
            case "kick_2":
            case "kick_3": this.movementDone = true;
                break;
        }
    }

    @Override
    public void handleIddle() {
        if(!this.iddleFlag) {
            this.clockwise=this.attacker.getRNG().nextBoolean();
            this.iddleFlag=true;
        }
        this.circleAroundTargetFacing(7, this.clockwise, 1);
    }

}
