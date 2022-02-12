package com.theycallmeapp.reddragon.common.entity;

import com.theycallmeapp.reddragon.common.entity.AI.FollowOwnerNoTPGoal;
import com.theycallmeapp.reddragon.common.entity.network.ControlNetwork;
import com.theycallmeapp.reddragon.common.entity.network.message.ControlMessageGoingDown;
import com.theycallmeapp.reddragon.common.entity.network.message.ControlMessageJumping;
import com.theycallmeapp.reddragon.init.EntityInit;
import com.theycallmeapp.reddragon.init.KeyBindsInit;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class RedDragonEntity extends TamableAnimal implements IAnimatable, PlayerRideable {

    private static final EntityDataAccessor<Boolean> IS_FLYING = SynchedEntityData.defineId(RedDragonEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_JUMPING = SynchedEntityData.defineId(RedDragonEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_GOINDOWN = SynchedEntityData.defineId(RedDragonEntity.class, EntityDataSerializers.BOOLEAN);

    AnimationFactory factory = new AnimationFactory(this);
    Player pilot;

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (isFlying()) {
            if (this.xRotO < -20 || isJumping()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.Dragon.flyup", true));
                return PlayState.CONTINUE;
            }
            if (this.xRotO < 0 && this.xRotO > -20 || isJumping()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.Dragon.fly", true));
                return PlayState.CONTINUE;
            }
            if (this.xRotO > 0 && this.xRotO < 20) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.Dragon.glide", true));
                return PlayState.CONTINUE;
            }
            if (this.xRotO > 15 && this.xRotO < 30) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.Dragon.glidedown", true));
                return PlayState.CONTINUE;
            }
            if (this.xRotO > 30) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.Dragon.dive", true));
                return PlayState.CONTINUE;
            }
        }
        if (this.isInSittingPose()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.Dragon.sit", true));
            return PlayState.CONTINUE;
        }
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.Dragon.walk", true));
            return PlayState.CONTINUE;
        }
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.Dragon.idle", true));
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState predicate1(AnimationEvent<E> event) {
        if (isFlying()) {
            if (this.xRotO < 0 && this.xRotO > -20) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.Dragon.rotmidup", true));
                return PlayState.CONTINUE;
            }
            if (this.xRotO > 0 && this.xRotO < 20) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.Dragon.rot0", true));
                return PlayState.CONTINUE;
            }
            if (this.xRotO > 15 && this.xRotO < 30) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.Dragon.rotdive", true));
                return PlayState.CONTINUE;
            }
            if (this.xRotO > 30) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.Dragon.rotdive2", true));
                return PlayState.CONTINUE;
            }
            if (this.xRotO < -20 || isJumping()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.Dragon.rotup", true));
                return PlayState.CONTINUE;
            }
        }
        return PlayState.CONTINUE;
    }

    private static final EntityDataAccessor<Integer> DRAGON_VARIANT = SynchedEntityData.defineId(RedDragonEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DRAGON_OVERLAY = SynchedEntityData.defineId(RedDragonEntity.class, EntityDataSerializers.INT);

    public RedDragonEntity(EntityType<RedDragonEntity> entityType, Level level) {
        super(entityType, level);
        pilot = (Player) getControllingPassenger();
        this.maxUpStep = 1.0F;

    }

    // Entity Goals
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.25D, Ingredient.of(Items.DIAMOND), false));
        this.goalSelector.addGoal(5, new FollowOwnerNoTPGoal(this, 1.1D, 10.0F, 3.0F, false));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0F));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));

    }

    //  Attributes
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 60.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3F)
                .add(Attributes.ATTACK_DAMAGE, 4.5F);

    }

    public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
        return false;
    }

    public int getDragonOverlay() {
        return this.entityData.get(DRAGON_OVERLAY);
    }

    // Variant
    public int getDragonVariant() {
        return this.entityData.get(DRAGON_VARIANT);
    }

    public void setDragonVariant(int pType) {
        this.entityData.set(DRAGON_VARIANT, pType);
    }

    public boolean isFlying() {
        return this.entityData.get(IS_FLYING);
    }

    public void setIsFlying(boolean flying) {
        this.entityData.set(IS_FLYING, flying);
    }

    public boolean isJumping() {
        return this.entityData.get(IS_JUMPING);
    }

    public void setIsJumping(boolean jumping) {
        this.entityData.set(IS_JUMPING, jumping);
    }

    public boolean isGoingDown() {
        return this.entityData.get(IS_GOINDOWN);
    }

    public void setIsGoingDown(boolean goingdown) {
        this.entityData.set(IS_GOINDOWN, goingdown);
    }

    public void setDragonOverlay(int pType) {
        this.entityData.set(DRAGON_OVERLAY, pType);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DRAGON_VARIANT, 0);
        this.entityData.define(DRAGON_OVERLAY, 0);
        this.entityData.define(IS_FLYING, false);
        this.entityData.define(IS_JUMPING, false);
        this.entityData.define(IS_GOINDOWN, false);
    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("variant", this.getDragonVariant());
        pCompound.putInt("overlay", this.getDragonOverlay());
        pCompound.putBoolean("is_flying", this.isFlying());
        pCompound.putBoolean("is_jumping", this.isJumping());
        pCompound.putBoolean("is_goingdown", this.isGoingDown());
    }

    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setDragonVariant(pCompound.getInt("variant"));
        this.setDragonOverlay(pCompound.getInt("overlay"));
        this.setIsFlying(pCompound.getBoolean("is_flying"));
        this.setIsJumping(pCompound.getBoolean("is_jumping"));
        this.setIsGoingDown(pCompound.getBoolean("is_goingdown"));
    }

    //  Taming
    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        Item item = itemstack.getItem();
        if (this.level.isClientSide) {
            boolean flag = this.isOwnedBy(pPlayer) || this.isTame() || itemstack.is(Items.BONE) && !this.isTame();
            return flag ? InteractionResult.CONSUME : InteractionResult.PASS;
        } else {
            if (this.isTame()) {
                if (this.isFood(itemstack) && this.getHealth() < this.getMaxHealth()) {
                    if (!pPlayer.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }

                    this.heal((float) item.getFoodProperties().getNutrition());
                    this.gameEvent(GameEvent.MOB_INTERACT, this.eyeBlockPosition());
                    return InteractionResult.SUCCESS;
                }

                // add another "if" for saddle/armors

                else {
                    InteractionResult interactionresult = super.mobInteract(pPlayer, pHand);
                    if ((!interactionresult.consumesAction() || this.isBaby()) && this.isOwnedBy(pPlayer)) {
                        this.setOrderedToSit(!this.isOrderedToSit());
                        this.jumping = false;
                        this.navigation.stop();
                        this.setTarget(null);
                        return InteractionResult.SUCCESS;
                    }

                    return interactionresult;
                }

            } else if (itemstack.is(Items.DIAMOND)) {
                if (!pPlayer.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                if (this.random.nextInt(3) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, pPlayer)) {
                    this.tame(pPlayer);
                    this.navigation.stop();
                    this.setTarget(null);
                    this.setOrderedToSit(true);
                    this.level.broadcastEntityEvent(this, (byte) 7);
                } else {
                    this.level.broadcastEntityEvent(this, (byte) 6);
                }

                return InteractionResult.SUCCESS;
            }

            if (this.isBaby()) {
                return super.mobInteract(pPlayer, pHand);
            } else {
                this.doPlayerRide(pPlayer);
                return InteractionResult.sidedSuccess(this.level.isClientSide);
            }
        }
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob parent) {
        RedDragonEntity dragon = EntityInit.RED_DRAGON.get().create(level);
        if (parent instanceof RedDragonEntity) {
            if (this.random.nextBoolean()) {
                dragon.setDragonVariant(this.getDragonVariant());
            } else {
                dragon.setDragonVariant(((RedDragonEntity) parent).getDragonVariant());
            }
        }
        return dragon;
    }


    // Variant pt2
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @javax.annotation.Nullable SpawnGroupData pSpawnData, @javax.annotation.Nullable CompoundTag pDataTag) {
        pSpawnData = super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
        this.setDragonVariant(this.random.nextInt(4));
        this.setDragonOverlay(this.random.nextInt(4));
        return pSpawnData;
    }


    // Animation
    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<RedDragonEntity>(this, "controller", 5, this::predicate));
        data.addAnimationController(new AnimationController<RedDragonEntity>(this, "controller1", 15, this::predicate1));

    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    /**
     * Check if the ground 2 blocks below is a solid. Replacement for Vanilla onGound
     *
     * @return solidBlockState
     */
    public boolean isOnGround() {
        BlockPos solidPos = new BlockPos(this.position().x, this.position().y - 3, this.position().z);
        return !level.getBlockState(solidPos).isAir();
    }

    protected void doPlayerRide(Player pPlayer) {
        if (!this.level.isClientSide) {
            this.setPilot(pPlayer);
            pPlayer.setYRot(this.getYRot());
            pPlayer.setXRot(this.getXRot());
            pPlayer.startRiding(this);
        }

    }

    public void positionRider(Entity pPassenger) {
        super.positionRider(pPassenger);
        if (pPassenger == this.getControllingPassenger()) {
            Player mob = (Player) pPassenger;
            this.yBodyRot = mob.yBodyRot;
        }

        pPassenger.setPos(this.getX(), this.getY() + 1.5, this.getZ());
    }


    @Override
    public boolean canBeControlledByRider() {
        return true;
    }

    /**
     * For vehicles, the first passenger is generally considered the controller and "drives" the vehicle. For example,
     * Pigs, Horses, and Boats are generally "steered" by the controlling passenger.
     */
    @javax.annotation.Nullable
    public Entity getControllingPassenger() {
        return this.getFirstPassenger();
    }

    @OnlyIn(Dist.CLIENT)
    public void updateClientControls() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.keyJump.isDown()) {
            ControlNetwork.INSTANCE.sendToServer(new ControlMessageJumping(true));
        } else {
            ControlNetwork.INSTANCE.sendToServer(new ControlMessageJumping(false));
        }
        if (KeyBindsInit.keyDown.isDown()) {
            ControlNetwork.INSTANCE.sendToServer(new ControlMessageGoingDown(true));
        } else {
            ControlNetwork.INSTANCE.sendToServer(new ControlMessageGoingDown(false));
        }
//        System.out.println(this.isGoingDown() + " Down ");
//        System.out.println(KeyBindsInit.keyDown.isDown() + " key down");

    }

    @Override
    public void tick() {
        if (getControllingPassenger() != null && !isOnGround()) { // !isOnGround()
            this.setIsFlying(true); // temporary, we need to set when an entity isFlying() or not, for now let's set this to true when a pilot is present, due to lack of a liftoff method.
        } else {
            this.setIsFlying(false);
        }
//        System.out.println(this.isOnGround());
        this.setNoGravity(this.isFlying());

        if (level.isClientSide()) {
            updateClientControls();
        }
        super.tick();
    }

    @Override
    public void travel(Vec3 pTravelVector) {
        if (this.isAlive()) {
            if (this.isVehicle() && this.canBeControlledByRider()) {
                LivingEntity pilot = (LivingEntity) this.getControllingPassenger();

                this.setYRot(pilot.getYRot());
                this.yRotO = this.getYRot();
                this.setXRot(pilot.getXRot() * 0.5F);
                if (isFlying()) {
                    this.setXRot(pilot.getXRot() * 0.5F);
                } else {
                    this.setXRot(0);
                }
                this.setRot(this.getYRot(), this.getXRot());
                this.yBodyRot = this.getYRot();
                this.yHeadRot = this.yBodyRot;
                float f = pilot.xxa * 0.5F;
                float f1 = pilot.zza;

                // facing straight up is -10
                double xHeadRot;
                if (this.xRotO > 0) {
                    xHeadRot = this.xRotO / 2;
                } else {
                    xHeadRot = this.xRotO / 3.6;
                }

                // debug head rot; find value, then increment when facing straight down else reduce effect when trying to look on the ground
                // reduce isJumping speed when facing straight up ()

                // facing up:
                // facing down: xHeadRot =
                // facing forward
                double xHeadRotABS = Math.abs(this.xRotO) / 450;
                double y = isFlying() && (!isJumping() || !isGoingDown()) ? xHeadRot * -0.005 : 0;
                // up: y=0.1 down: y=-0.1 neutral: y=-0.01
                // facing positive z: x Head Rot 0.4
                if (f1 <= 0.0F) {
                    f1 *= 0.25F;
                }

                this.xRotO = this.getXRot();

                Vec3 delta = this.getDeltaMovement();
                this.setDeltaMovement(delta.x / 2, delta.y / 2, delta.z / 2);

                if (f1 > 0.0F) {
                    float f2 = Mth.sin(this.getYRot() * ((float) Math.PI / 180F));
                    float f3 = Mth.cos(this.getYRot() * ((float) Math.PI / 180F));

                    this.setDeltaMovement(delta.add(
                            ((-0.2F + xHeadRotABS) * f2),
                            y, // if isFlying( allow y changes if not set to 0);
                            (0.2F - xHeadRotABS) * f3));
                }

                if (this.isJumping()) {
                    this.setDeltaMovement(this.getDeltaMovement().add(0, f1 > 0 ? 0.3 : 0.3, 0));
                } else if (this.isGoingDown()) {
                    this.setDeltaMovement(this.getDeltaMovement().add(0, f1 > 0 ? -0.3 : -0.3, 0));
                }

                if (this.isControlledByLocalInstance()) {
                    this.setSpeed((float) this.getAttributeValue(Attributes.MOVEMENT_SPEED));
                    super.travel(new Vec3(f, y, f1)); // pTravelVector.y
                } else if (pilot instanceof Player) {
                    this.setDeltaMovement(Vec3.ZERO);
                }
                this.tryCheckInsideBlocks();
            } else {
                super.travel(pTravelVector);
            }
        }
    }

    public Player getPilot() {
        return pilot;
    }

    public void setPilot(Player pilot) {
        this.pilot = pilot;
    }

}
