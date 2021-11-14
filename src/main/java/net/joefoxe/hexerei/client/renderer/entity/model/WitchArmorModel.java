
package net.joefoxe.hexerei.client.renderer.entity.model;


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.ZombieVillagerEntity;
import net.minecraft.inventory.EquipmentSlotType;

import java.util.HashMap;
import java.util.Map;

public class WitchArmorModel <T extends LivingEntity> extends ArmorModel<T> {

	private static final Map<Integer, net.joefoxe.hexerei.client.renderer.entity.model.WitchArmorModel<? extends LivingEntity>> CACHE = new HashMap<>();

	private final ModelRenderer Head;
	private final ModelRenderer head_r1;
	private final ModelRenderer head_r2;
	private final ModelRenderer head_r3;
	private final ModelRenderer head_r4;
	private final ModelRenderer head_r5;
	private final ModelRenderer head_r6;
	private final ModelRenderer head_r7;
	private final ModelRenderer head_r8;
	private final ModelRenderer head_r9;
	private final ModelRenderer head_r10;
	private final ModelRenderer head_r11;
	private final ModelRenderer head_r12;
	private final ModelRenderer Body;
	private final ModelRenderer body_r1;
	private final ModelRenderer body_r2;
	private final ModelRenderer body_r3;
	private final ModelRenderer body_r4;
	private final ModelRenderer body_r5;
	private final ModelRenderer body_r6;
	private final ModelRenderer RightArm;
	private final ModelRenderer rightarm_r1;
	private final ModelRenderer rightarm_r2;
	private final ModelRenderer rightarm_r3;
	private final ModelRenderer rightarm_r4;
	private final ModelRenderer rightarm_r5;
	private final ModelRenderer LeftArm;
	private final ModelRenderer leftarm_r1;
	private final ModelRenderer leftarm_r2;
	private final ModelRenderer leftarm_r3;
	private final ModelRenderer leftarm_r4;
	private final ModelRenderer feathers;
	private final ModelRenderer feather_r1;
	private final ModelRenderer feather_r2;
	private final ModelRenderer feather_r3;
	private final ModelRenderer feather_r4;
	private final ModelRenderer feather_r5;
	private final ModelRenderer feather_r6;
	private final ModelRenderer feather_r7;
	private final ModelRenderer feather_r8;
	private final ModelRenderer feather_r9;
	private final ModelRenderer feather_r10;
	private final ModelRenderer feather_r11;
	private final ModelRenderer feather_r12;
	private final ModelRenderer feather_r13;
	private final ModelRenderer feather_r14;
	private final ModelRenderer feather_r15;
	private final ModelRenderer feather_r16;
	private final ModelRenderer feather_r17;
	private final ModelRenderer feather_r18;
	private final ModelRenderer feather_r19;
	private final ModelRenderer feather_r20;
	private final ModelRenderer feather_r21;
	private final ModelRenderer feather_r22;
	private final ModelRenderer feather_r23;
	private final ModelRenderer feather_r24;
	private final ModelRenderer Belt;
	private final ModelRenderer RightLeg;
	private final ModelRenderer LeftLeg;
	private final ModelRenderer RightBoot;
	private final ModelRenderer feather_r25;
	private final ModelRenderer feather_r26;
	private final ModelRenderer feather_r27;
	private final ModelRenderer feather_r28;
	private final ModelRenderer feather_r29;
	private final ModelRenderer feather_r30;
	private final ModelRenderer rightboot_r1;
	private final ModelRenderer rightboot_r2;
	private final ModelRenderer LeftBoot;
	private final ModelRenderer feather_r31;
	private final ModelRenderer feather_r32;
	private final ModelRenderer feather_r33;
	private final ModelRenderer feather_r34;
	private final ModelRenderer feather_r35;
	private final ModelRenderer feather_r36;
	private final ModelRenderer feather_r37;
	private final ModelRenderer feather_r38;
	private final ModelRenderer leftboot_r1;
	private final ModelRenderer leftboot_r2;
	private final EquipmentSlotType slot;
	private final byte entityFlag;

	public WitchArmorModel(float modelSize, EquipmentSlotType slotType) {
		super(modelSize, slotType);
		this.slot = slotType;
		this.entityFlag = (byte) (0 >> 4);
		textureWidth = 128;
		textureHeight = 128;

		Head = new ModelRenderer(this);
		Head.setRotationPoint(0.0F, -2.0F, 0.0F);
		setRotationAngle(Head, 0.0436F, 0.0F, 0.0F);


		head_r1 = new ModelRenderer(this);
		head_r1.setRotationPoint(-2.0F, -5.0F, 0.0F);
		Head.addChild(head_r1);
		setRotationAngle(head_r1, -0.3048F, -0.0186F, -0.1276F);
		head_r1.setTextureOffset(0, 14).addBox(-2.75F, -1.95F, -5.25F, 10.0F, 1.0F, 10.0F, 0.0F, false);

		head_r2 = new ModelRenderer(this);
		head_r2.setRotationPoint(4.704F, -14.2025F, 5.7117F);
		Head.addChild(head_r2);
		setRotationAngle(head_r2, -0.5968F, 0.6696F, 1.2934F);
		head_r2.setTextureOffset(0, 55).addBox(1.35F, 0.05F, -1.0F, 3.0F, 3.0F, 3.0F, 0.0F, false);

		head_r3 = new ModelRenderer(this);
		head_r3.setRotationPoint(6.4009F, -12.7447F, 6.4586F);
		Head.addChild(head_r3);
		setRotationAngle(head_r3, -0.6173F, 0.8249F, 1.5822F);
		head_r3.setTextureOffset(0, 30).addBox(1.5F, 0.0F, -0.75F, 2.0F, 2.0F, 2.0F, 0.0F, false);

		head_r4 = new ModelRenderer(this);
		head_r4.setRotationPoint(0.6618F, 25.413F, -5.3821F);
		Head.addChild(head_r4);
		setRotationAngle(head_r4, -0.3048F, -0.0186F, -0.1276F);
		head_r4.setTextureOffset(75, 74).addBox(6.5F, -31.75F, -11.25F, 4.0F, 2.0F, 2.0F, 0.0F, false);
		head_r4.setTextureOffset(43, 28).addBox(-1.5F, -31.75F, -11.25F, 5.0F, 2.0F, 2.0F, 0.0F, false);
		head_r4.setTextureOffset(34, 67).addBox(-3.5F, -31.75F, -11.25F, 2.0F, 2.0F, 3.0F, 0.0F, false);
		head_r4.setTextureOffset(70, 65).addBox(-3.5F, -31.75F, -4.25F, 2.0F, 2.0F, 7.0F, 0.0F, false);
		head_r4.setTextureOffset(0, 0).addBox(-1.5F, -31.75F, -9.25F, 12.0F, 2.0F, 12.0F, 0.0F, false);

		head_r5 = new ModelRenderer(this);
		head_r5.setRotationPoint(2.6707F, -7.2899F, -5.1F);
		Head.addChild(head_r5);
		setRotationAngle(head_r5, -0.3521F, -0.5156F, 0.0458F);
		head_r5.setTextureOffset(30, 20).addBox(-3.175F, -0.9999F, -0.175F, 3.0F, 2.0F, 2.0F, 0.0F, false);

		head_r6 = new ModelRenderer(this);
		head_r6.setRotationPoint(2.0033F, -7.4583F, -5.8296F);
		Head.addChild(head_r6);
		setRotationAngle(head_r6, -0.4114F, 0.7224F, -0.4143F);
		head_r6.setTextureOffset(16, 78).addBox(-1.575F, -0.9746F, 0.0F, 3.0F, 2.0F, 2.0F, 0.0F, false);

		head_r7 = new ModelRenderer(this);
		head_r7.setRotationPoint(-5.2544F, -4.9491F, -1.0762F);
		Head.addChild(head_r7);
		setRotationAngle(head_r7, -0.4441F, -0.7977F, 0.1948F);
		head_r7.setTextureOffset(34, 51).addBox(-0.425F, -1.0F, -1.25F, 2.0F, 2.0F, 3.0F, 0.0F, false);

		head_r8 = new ModelRenderer(this);
		head_r8.setRotationPoint(-5.879F, -5.6909F, -3.5945F);
		Head.addChild(head_r8);
		setRotationAngle(head_r8, -0.353F, 0.5198F, -0.3144F);
		head_r8.setTextureOffset(54, 29).addBox(-0.575F, -0.9748F, -0.975F, 2.0F, 2.0F, 3.0F, 0.0F, false);

		head_r9 = new ModelRenderer(this);
		head_r9.setRotationPoint(-1.9901F, -8.6229F, 0.9681F);
		Head.addChild(head_r9);
		setRotationAngle(head_r9, -0.3481F, 0.0316F, 0.0844F);
		head_r9.setTextureOffset(0, 25).addBox(-2.5F, 0.0F, -4.5F, 9.0F, 3.0F, 9.0F, 0.0F, false);

		head_r10 = new ModelRenderer(this);
		head_r10.setRotationPoint(-1.9868F, -9.5805F, 1.2908F);
		Head.addChild(head_r10);
		setRotationAngle(head_r10, -0.4582F, 0.1451F, 0.2752F);
		head_r10.setTextureOffset(36, 0).addBox(-1.5F, -1.5F, -3.75F, 8.0F, 4.0F, 8.0F, 0.0F, false);

		head_r11 = new ModelRenderer(this);
		head_r11.setRotationPoint(-1.5121F, -13.9451F, 5.148F);
		Head.addChild(head_r11);
		setRotationAngle(head_r11, -0.4867F, 0.5183F, 0.7968F);
		head_r11.setTextureOffset(72, 19).addBox(3.0F, -2.55F, -2.25F, 4.0F, 4.0F, 4.0F, 0.0F, false);

		head_r12 = new ModelRenderer(this);
		head_r12.setRotationPoint(-1.3545F, -11.8213F, 2.9487F);
		Head.addChild(head_r12);
		setRotationAngle(head_r12, -0.3675F, 0.2629F, 0.4639F);
		head_r12.setTextureOffset(59, 28).addBox(0.0F, -1.5F, -3.0F, 6.0F, 4.0F, 6.0F, 0.0F, false);

		Body = new ModelRenderer(this);
		Body.setRotationPoint(0.0F, 0.0F, 0.0F);
		Body.setTextureOffset(40, 14).addBox(-5.0F, -0.5F, -3.0F, 10.0F, 8.0F, 6.0F, 0.0F, false);
		Body.setTextureOffset(31, 32).addBox(-4.5F, 7.501F, -2.501F, 9.0F, 6.0F, 5.0F, 0.0F, false);

		body_r1 = new ModelRenderer(this);
		body_r1.setRotationPoint(-2.9102F, 24.2703F, 0.0F);
		Body.addChild(body_r1);
		setRotationAngle(body_r1, 0.0F, 0.0F, 0.1745F);
		body_r1.setTextureOffset(72, 38).addBox(-5.0F, -16.5F, 0.01F, 2.0F, 9.0F, 3.0F, 0.0F, false);
		body_r1.setTextureOffset(57, 72).addBox(-5.0F, -16.5F, -2.99F, 2.0F, 7.0F, 3.0F, 0.0F, false);

		body_r2 = new ModelRenderer(this);
		body_r2.setRotationPoint(5.2869F, 14.7125F, -1.49F);
		Body.addChild(body_r2);
		setRotationAngle(body_r2, -0.5236F, 0.0F, -0.1745F);
		body_r2.setTextureOffset(67, 74).addBox(-1.025F, -2.7F, -1.5F, 2.0F, 3.0F, 4.0F, 0.0F, false);

		body_r3 = new ModelRenderer(this);
		body_r3.setRotationPoint(-5.2869F, 14.7125F, -1.49F);
		Body.addChild(body_r3);
		setRotationAngle(body_r3, -0.5236F, 0.0F, 0.1745F);
		body_r3.setTextureOffset(59, 38).addBox(-0.975F, -2.7F, -1.5F, 2.0F, 3.0F, 4.0F, 0.0F, false);

		body_r4 = new ModelRenderer(this);
		body_r4.setRotationPoint(2.9102F, 24.2703F, 0.0F);
		Body.addChild(body_r4);
		setRotationAngle(body_r4, 0.0F, 0.0F, -0.1745F);
		body_r4.setTextureOffset(0, 14).addBox(3.0F, -16.5F, -2.99F, 2.0F, 7.0F, 3.0F, 0.0F, false);

		body_r5 = new ModelRenderer(this);
		body_r5.setRotationPoint(4.7661F, 11.758F, 1.51F);
		Body.addChild(body_r5);
		setRotationAngle(body_r5, 0.0F, 0.0F, -0.1745F);
		body_r5.setTextureOffset(0, 0).addBox(-1.0F, -4.5F, -1.5F, 2.0F, 9.0F, 3.0F, 0.0F, false);

		body_r6 = new ModelRenderer(this);
		body_r6.setRotationPoint(-0.01F, 11.2618F, 2.7227F);
		Body.addChild(body_r6);
		setRotationAngle(body_r6, 0.1317F, 0.0F, 0.0F);
		body_r6.setTextureOffset(0, 37).addBox(-4.975F, -10.5F, -0.75F, 10.0F, 16.0F, 2.0F, 0.0F, false);

		RightArm = new ModelRenderer(this);
		RightArm.setRotationPoint(-5.0F, 1.0F, 0.0F);
		setRotationAngle(RightArm, -0.0133F, -0.0125F, -0.0853F);


		rightarm_r1 = new ModelRenderer(this);
		rightarm_r1.setRotationPoint(-1.6951F, 5.1954F, 0.2218F);
		RightArm.addChild(rightarm_r1);
		setRotationAngle(rightarm_r1, -0.014F, 0.0821F, 0.1306F);
		rightarm_r1.setTextureOffset(0, 61).addBox(-2.2814F, 1.8917F, -2.9187F, 6.0F, 2.0F, 6.0F, 0.0F, false);

		rightarm_r2 = new ModelRenderer(this);
		rightarm_r2.setRotationPoint(-1.6951F, 5.9454F, 0.2218F);
		RightArm.addChild(rightarm_r2);
		setRotationAngle(rightarm_r2, -0.0135F, -0.0064F, 0.0445F);
		rightarm_r2.setTextureOffset(39, 51).addBox(-1.969F, -8.4881F, -2.5253F, 5.0F, 11.0F, 5.0F, 0.0F, false);

		rightarm_r3 = new ModelRenderer(this);
		rightarm_r3.setRotationPoint(-1.1622F, 7.9253F, 3.2088F);
		RightArm.addChild(rightarm_r3);
		setRotationAngle(rightarm_r3, -0.2886F, 0.8566F, -0.0735F);
		rightarm_r3.setTextureOffset(30, 14).addBox(-1.75F, -0.5F, -2.25F, 4.0F, 2.0F, 4.0F, 0.0F, false);

		rightarm_r4 = new ModelRenderer(this);
		rightarm_r4.setRotationPoint(0.0983F, 3.2608F, -0.7944F);
		RightArm.addChild(rightarm_r4);
		setRotationAngle(rightarm_r4, -0.0041F, 0.7798F, 0.0409F);
		rightarm_r4.setTextureOffset(36, 77).addBox(-4.75F, 1.5F, 0.25F, 3.0F, 4.0F, 3.0F, 0.0F, false);
		rightarm_r4.setTextureOffset(36, 0).addBox(-4.25F, -1.5F, 0.75F, 2.0F, 3.0F, 2.0F, 0.0F, false);

		rightarm_r5 = new ModelRenderer(this);
		rightarm_r5.setRotationPoint(-0.5781F, 0.1539F, 2.0367F);
		RightArm.addChild(rightarm_r5);
		setRotationAngle(rightarm_r5, -0.0041F, -0.791F, 0.0467F);
		rightarm_r5.setTextureOffset(36, 0).addBox(-1.25F, -1.25F, -0.75F, 2.0F, 3.0F, 2.0F, 0.0F, false);

		LeftArm = new ModelRenderer(this);
		LeftArm.setRotationPoint(5.0F, 1.0F, 0.0F);
		setRotationAngle(LeftArm, 0.0F, 0.0F, 0.0436F);


		leftarm_r1 = new ModelRenderer(this);
		leftarm_r1.setRotationPoint(1.7068F, 5.1989F, 0.1468F);
		LeftArm.addChild(leftarm_r1);
		setRotationAngle(leftarm_r1, -0.0217F, -0.0703F, -0.1311F);
		leftarm_r1.setTextureOffset(60, 0).addBox(-3.7186F, 1.8917F, -2.9187F, 6.0F, 2.0F, 6.0F, 0.0F, false);

		leftarm_r2 = new ModelRenderer(this);
		leftarm_r2.setRotationPoint(1.7068F, 5.9489F, 0.1468F);
		LeftArm.addChild(leftarm_r2);
		setRotationAngle(leftarm_r2, -0.0201F, 0.0188F, -0.0457F);
		leftarm_r2.setTextureOffset(19, 51).addBox(-3.031F, -8.4881F, -2.5253F, 5.0F, 11.0F, 5.0F, 0.0F, false);
		leftarm_r2.setTextureOffset(54, 45).addBox(-3.531F, -9.2381F, -3.0253F, 6.0F, 5.0F, 6.0F, 0.0F, false);

		leftarm_r3 = new ModelRenderer(this);
		leftarm_r3.setRotationPoint(1.1739F, 7.9288F, 3.1338F);
		LeftArm.addChild(leftarm_r3);
		setRotationAngle(leftarm_r3, -0.2964F, -0.8435F, 0.0782F);
		leftarm_r3.setTextureOffset(27, 25).addBox(-2.25F, -0.5F, -2.25F, 4.0F, 2.0F, 4.0F, 0.0F, false);

		leftarm_r4 = new ModelRenderer(this);
		leftarm_r4.setRotationPoint(-0.5866F, 3.2643F, -0.8694F);
		LeftArm.addChild(leftarm_r4);
		setRotationAngle(leftarm_r4, -0.0132F, -0.7673F, -0.0356F);
		leftarm_r4.setTextureOffset(77, 27).addBox(2.0F, 1.5F, 0.0F, 3.0F, 4.0F, 3.0F, 0.0F, false);
		leftarm_r4.setTextureOffset(0, 25).addBox(2.5F, -1.5F, 0.5F, 2.0F, 3.0F, 2.0F, 0.0F, false);

		feathers = new ModelRenderer(this);
		feathers.setRotationPoint(-1.4287F, 0.229F, 0.15F);
		LeftArm.addChild(feathers);


		feather_r1 = new ModelRenderer(this);
		feather_r1.setRotationPoint(3.3075F, -3.5968F, 2.3999F);
		feathers.addChild(feather_r1);
		setRotationAngle(feather_r1, -0.5107F, -0.5313F, -0.6482F);
		feather_r1.setTextureOffset(23, 46).addBox(0.25F, 0.5F, 0.0F, 3.0F, 0.0F, 1.0F, 0.0F, false);

		feather_r2 = new ModelRenderer(this);
		feather_r2.setRotationPoint(4.4492F, -1.1555F, 3.3527F);
		feathers.addChild(feather_r2);
		setRotationAngle(feather_r2, -1.7342F, -0.5874F, 0.389F);
		feather_r2.setTextureOffset(23, 45).addBox(-1.5F, -0.2F, -0.5F, 3.0F, 0.0F, 1.0F, 0.0F, false);

		feather_r3 = new ModelRenderer(this);
		feather_r3.setRotationPoint(3.2999F, 0.6819F, 3.9032F);
		feathers.addChild(feather_r3);
		setRotationAngle(feather_r3, -2.0813F, -0.3443F, 1.1283F);
		feather_r3.setTextureOffset(23, 44).addBox(-0.5F, 1.25F, -0.5F, 3.0F, 0.0F, 1.0F, 0.0F, false);

		feather_r4 = new ModelRenderer(this);
		feather_r4.setRotationPoint(3.3075F, -3.5968F, -2.3999F);
		feathers.addChild(feather_r4);
		setRotationAngle(feather_r4, 0.5107F, 0.5313F, -0.6482F);
		feather_r4.setTextureOffset(53, 12).addBox(-1.0F, 0.25F, -1.0F, 3.0F, 0.0F, 1.0F, 0.0F, false);

		feather_r5 = new ModelRenderer(this);
		feather_r5.setRotationPoint(3.2999F, 0.6819F, -3.9032F);
		feathers.addChild(feather_r5);
		setRotationAngle(feather_r5, 2.0813F, 0.3443F, 1.1283F);
		feather_r5.setTextureOffset(47, 12).addBox(-0.5F, 1.25F, -0.5F, 3.0F, 0.0F, 1.0F, 0.0F, false);

		feather_r6 = new ModelRenderer(this);
		feather_r6.setRotationPoint(3.1114F, -1.5281F, 3.6198F);
		feathers.addChild(feather_r6);
		setRotationAngle(feather_r6, -2.0357F, -0.4057F, 1.0041F);
		feather_r6.setTextureOffset(23, 42).addBox(-0.5F, 0.1F, 1.0F, 3.0F, 0.0F, 1.0F, 0.0F, false);

		feather_r7 = new ModelRenderer(this);
		feather_r7.setRotationPoint(3.1114F, -1.5281F, 3.6198F);
		feathers.addChild(feather_r7);
		setRotationAngle(feather_r7, -1.4383F, -0.5375F, 0.5115F);
		feather_r7.setTextureOffset(23, 43).addBox(-2.0F, -0.4F, -0.5F, 3.0F, 0.0F, 1.0F, 0.0F, false);

		feather_r8 = new ModelRenderer(this);
		feather_r8.setRotationPoint(3.1114F, -1.5281F, -3.6198F);
		feathers.addChild(feather_r8);
		setRotationAngle(feather_r8, 1.4383F, 0.5375F, 0.5115F);
		feather_r8.setTextureOffset(23, 43).addBox(-2.0F, -0.15F, -0.5F, 3.0F, 0.0F, 1.0F, 0.0F, false);

		feather_r9 = new ModelRenderer(this);
		feather_r9.setRotationPoint(3.1114F, -1.5281F, -3.6198F);
		feathers.addChild(feather_r9);
		setRotationAngle(feather_r9, 2.0357F, 0.4057F, 1.0041F);
		feather_r9.setTextureOffset(23, 43).addBox(-0.5F, 0.1F, -2.0F, 3.0F, 0.0F, 1.0F, 0.0F, false);

		feather_r10 = new ModelRenderer(this);
		feather_r10.setRotationPoint(4.4492F, -1.1555F, -3.3527F);
		feathers.addChild(feather_r10);
		setRotationAngle(feather_r10, 1.7342F, 0.5874F, 0.389F);
		feather_r10.setTextureOffset(47, 13).addBox(-1.5F, 0.0F, -0.5F, 3.0F, 0.0F, 1.0F, 0.0F, false);

		feather_r11 = new ModelRenderer(this);
		feather_r11.setRotationPoint(4.7935F, -3.8956F, -1.885F);
		feathers.addChild(feather_r11);
		setRotationAngle(feather_r11, 0.2882F, 0.5236F, -0.4894F);
		feather_r11.setTextureOffset(58, 57).addBox(-1.5F, 0.25F, -0.25F, 3.0F, 0.0F, 1.0F, 0.0F, false);

		feather_r12 = new ModelRenderer(this);
		feather_r12.setRotationPoint(2.909F, -3.7249F, -0.1261F);
		feathers.addChild(feather_r12);
		setRotationAngle(feather_r12, -0.2465F, -0.2182F, -0.4841F);
		feather_r12.setTextureOffset(58, 56).addBox(-1.3959F, -0.2137F, 0.6724F, 3.0F, 0.0F, 1.0F, 0.0F, false);

		feather_r13 = new ModelRenderer(this);
		feather_r13.setRotationPoint(2.909F, -3.7249F, 0.1261F);
		feathers.addChild(feather_r13);
		setRotationAngle(feather_r13, 0.0243F, 0.436F, -0.3914F);
		feather_r13.setTextureOffset(58, 56).addBox(-1.4228F, -0.2341F, -1.4583F, 3.0F, 0.0F, 1.0F, 0.0F, false);

		feather_r14 = new ModelRenderer(this);
		feather_r14.setRotationPoint(4.8214F, -3.843F, 2.1146F);
		feathers.addChild(feather_r14);
		setRotationAngle(feather_r14, -0.4088F, -0.3926F, -0.4746F);
		feather_r14.setTextureOffset(11, 58).addBox(-0.75F, 0.25F, -0.5F, 3.0F, 0.0F, 1.0F, 0.0F, false);

		feather_r15 = new ModelRenderer(this);
		feather_r15.setRotationPoint(5.4017F, 3.3284F, 0.0985F);
		feathers.addChild(feather_r15);
		setRotationAngle(feather_r15, -0.5627F, -0.3357F, 1.3216F);
		feather_r15.setTextureOffset(23, 41).addBox(-0.8894F, -0.9203F, 1.1842F, 3.0F, 0.0F, 1.0F, 0.0F, false);

		feather_r16 = new ModelRenderer(this);
		feather_r16.setRotationPoint(5.4017F, 3.3284F, 0.0985F);
		feathers.addChild(feather_r16);
		setRotationAngle(feather_r16, 0.6259F, 0.2169F, 1.1857F);
		feather_r16.setTextureOffset(23, 40).addBox(-1.0927F, -1.0827F, -2.3443F, 3.0F, 0.0F, 1.0F, 0.0F, false);

		feather_r17 = new ModelRenderer(this);
		feather_r17.setRotationPoint(5.4017F, 3.3284F, 0.0985F);
		feathers.addChild(feather_r17);
		setRotationAngle(feather_r17, -0.008F, -0.1183F, 1.2219F);
		feather_r17.setTextureOffset(23, 39).addBox(-0.3885F, 0.5048F, -0.772F, 3.0F, 0.0F, 1.0F, 0.0F, false);

		feather_r18 = new ModelRenderer(this);
		feather_r18.setRotationPoint(5.9017F, 0.3284F, 0.0985F);
		feathers.addChild(feather_r18);
		setRotationAngle(feather_r18, 0.6259F, 0.2169F, 1.1857F);
		feather_r18.setTextureOffset(23, 49).addBox(-1.0927F, -0.8327F, -2.0443F, 3.0F, 0.0F, 1.0F, 0.0F, false);

		feather_r19 = new ModelRenderer(this);
		feather_r19.setRotationPoint(5.9017F, 0.3284F, 0.0985F);
		feathers.addChild(feather_r19);
		setRotationAngle(feather_r19, -0.0081F, 0.2308F, 1.2191F);
		feather_r19.setTextureOffset(23, 48).addBox(-0.3885F, 0.5048F, -0.022F, 3.0F, 0.0F, 1.0F, 0.0F, false);

		feather_r20 = new ModelRenderer(this);
		feather_r20.setRotationPoint(5.9017F, 0.3284F, 0.0985F);
		feathers.addChild(feather_r20);
		setRotationAngle(feather_r20, -0.5627F, -0.3357F, 1.3216F);
		feather_r20.setTextureOffset(23, 47).addBox(-0.8894F, -0.6703F, 1.0842F, 3.0F, 0.0F, 1.0F, 0.0F, false);

		feather_r21 = new ModelRenderer(this);
		feather_r21.setRotationPoint(6.401F, -2.0952F, 2.0806F);
		feathers.addChild(feather_r21);
		setRotationAngle(feather_r21, -0.5627F, -0.3357F, 0.6235F);
		feather_r21.setTextureOffset(17, 55).addBox(-1.5F, 0.75F, -0.25F, 3.0F, 0.0F, 1.0F, 0.0F, false);

		feather_r22 = new ModelRenderer(this);
		feather_r22.setRotationPoint(6.3731F, -2.1478F, -1.919F);
		feathers.addChild(feather_r22);
		setRotationAngle(feather_r22, 0.6259F, 0.2169F, 0.4876F);
		feather_r22.setTextureOffset(54, 28).addBox(-1.5F, 0.75F, -0.75F, 3.0F, 0.0F, 1.0F, 0.0F, false);

		feather_r23 = new ModelRenderer(this);
		feather_r23.setRotationPoint(5.679F, -3.3455F, 0.1018F);
		feathers.addChild(feather_r23);
		setRotationAngle(feather_r23, -0.0109F, 0.0101F, 0.2609F);
		feather_r23.setTextureOffset(53, 13).addBox(-0.5F, 1.0F, -0.5F, 3.0F, 0.0F, 1.0F, 0.0F, false);

		feather_r24 = new ModelRenderer(this);
		feather_r24.setRotationPoint(5.679F, -3.3455F, 0.1018F);
		feathers.addChild(feather_r24);
		setRotationAngle(feather_r24, 0.4006F, -0.1312F, -0.2155F);
		feather_r24.setTextureOffset(58, 58).addBox(-0.75F, 0.0F, -0.5F, 3.0F, 0.0F, 1.0F, 0.0F, false);

		feather_r25 = new ModelRenderer(this);
		feather_r25.setRotationPoint(6.0712F, -2.7099F, 2.5799F);
		feathers.addChild(feather_r25);
		setRotationAngle(feather_r25, -0.9739F, -0.6044F, 0.6491F);
		feather_r25.setTextureOffset(23, 50).addBox(-1.5F, 0.0F, 0.25F, 3.0F, 0.0F, 1.0F, 0.0F, false);

		feather_r26 = new ModelRenderer(this);
		feather_r26.setRotationPoint(6.0712F, -2.7099F, -2.5799F);
		feathers.addChild(feather_r26);
		setRotationAngle(feather_r26, 0.9739F, 0.6044F, 0.6491F);
		feather_r26.setTextureOffset(59, 0).addBox(-1.5F, 0.0F, -1.25F, 3.0F, 0.0F, 1.0F, 0.0F, false);

		Belt = new ModelRenderer(this);
		Belt.setRotationPoint(0.0F, 0.0F, 0.0F);


		RightLeg = new ModelRenderer(this);
		RightLeg.setRotationPoint(0.0F, 0.0F, 0.0F);


		LeftLeg = new ModelRenderer(this);
		LeftLeg.setRotationPoint(0.0F, 0.0F, 0.0F);


		RightBoot = new ModelRenderer(this);
		RightBoot.setRotationPoint(-2.0F, 12.0F, 0.0F);


		feather_r27 = new ModelRenderer(this);
		feather_r27.setRotationPoint(-8.4017F, -8.6716F, 0.0985F);
		RightBoot.addChild(feather_r27);
		setRotationAngle(feather_r27, 0.6259F, -0.2169F, -1.1857F);
		feather_r27.setTextureOffset(35, 5).addBox(-14.4073F, 8.6673F, -6.5443F, 3.0F, 0.0F, 1.0F, 0.0F, false);

		feather_r28 = new ModelRenderer(this);
		feather_r28.setRotationPoint(-4.0794F, 6.8242F, -0.7251F);
		RightBoot.addChild(feather_r28);
		setRotationAngle(feather_r28, 0.652F, 0.1706F, -1.1512F);
		feather_r28.setTextureOffset(26, 33).addBox(-1.25F, 0.25F, -1.5F, 3.0F, 0.0F, 1.0F, 0.0F, false);

		feather_r29 = new ModelRenderer(this);
		feather_r29.setRotationPoint(-2.4226F, 6.7928F, 3.351F);
		RightBoot.addChild(feather_r29);
		setRotationAngle(feather_r29, -1.0817F, 0.55F, -1.552F);
		feather_r29.setTextureOffset(35, 7).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 0.0F, 1.0F, 0.0F, true);

		feather_r30 = new ModelRenderer(this);
		feather_r30.setRotationPoint(-4.0794F, 6.8242F, -0.7251F);
		RightBoot.addChild(feather_r30);
		setRotationAngle(feather_r30, -0.1277F, 0.0007F, -1.1415F);
		feather_r30.setTextureOffset(32, 31).addBox(-1.25F, 0.35F, 0.25F, 3.0F, 0.0F, 1.0F, 0.0F, false);

		feather_r31 = new ModelRenderer(this);
		feather_r31.setRotationPoint(-4.0794F, 6.8242F, 0.7251F);
		RightBoot.addChild(feather_r31);
		setRotationAngle(feather_r31, -0.5606F, 0.1156F, -1.1423F);
		feather_r31.setTextureOffset(26, 32).addBox(-1.0F, 0.75F, 0.5F, 3.0F, 0.0F, 1.0F, 0.0F, false);

		feather_r32 = new ModelRenderer(this);
		feather_r32.setRotationPoint(-8.4017F, -9.0716F, -0.0985F);
		RightBoot.addChild(feather_r32);
		setRotationAngle(feather_r32, -0.6259F, 0.2169F, -1.1857F);
		feather_r32.setTextureOffset(26, 31).addBox(-14.4073F, 8.9173F, 5.7943F, 3.0F, 0.0F, 1.0F, 0.0F, false);

		rightboot_r1 = new ModelRenderer(this);
		rightboot_r1.setRotationPoint(0.0F, 24.0F, 0.0F);
		RightBoot.addChild(rightboot_r1);
		setRotationAngle(rightboot_r1, 0.0F, 0.0873F, 0.0F);
		rightboot_r1.setTextureOffset(19, 67).addBox(-2.5F, -17.75F, -2.5F, 5.0F, 6.0F, 5.0F, 0.0F, false);
		rightboot_r1.setTextureOffset(78, 0).addBox(-2.5F, -14.75F, -3.5F, 5.0F, 3.0F, 1.0F, 0.0F, false);
		rightboot_r1.setTextureOffset(13, 69).addBox(-2.0F, -13.75F, -4.5F, 4.0F, 2.0F, 1.0F, 0.0F, false);

		rightboot_r2 = new ModelRenderer(this);
		rightboot_r2.setRotationPoint(0.0F, 5.75F, 0.0F);
		RightBoot.addChild(rightboot_r2);
		setRotationAngle(rightboot_r2, 0.0718F, 0.0936F, -0.175F);
		rightboot_r2.setTextureOffset(59, 56).addBox(-3.0F, -1.0F, -3.0F, 6.0F, 2.0F, 6.0F, 0.0F, false);

		LeftBoot = new ModelRenderer(this);
		LeftBoot.setRotationPoint(2.0F, 12.0F, 0.0F);


		feather_r33 = new ModelRenderer(this);
		feather_r33.setRotationPoint(4.0794F, 6.8242F, -0.7251F);
		LeftBoot.addChild(feather_r33);
		setRotationAngle(feather_r33, -0.1277F, -0.0007F, 1.1415F);
		feather_r33.setTextureOffset(21, 37).addBox(-1.75F, 0.35F, 0.25F, 3.0F, 0.0F, 1.0F, 0.0F, false);

		feather_r34 = new ModelRenderer(this);
		feather_r34.setRotationPoint(2.4226F, 6.7928F, 3.351F);
		LeftBoot.addChild(feather_r34);
		setRotationAngle(feather_r34, -1.0817F, -0.55F, 1.552F);
		feather_r34.setTextureOffset(35, 7).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 0.0F, 1.0F, 0.0F, false);

		feather_r35 = new ModelRenderer(this);
		feather_r35.setRotationPoint(8.4017F, -9.0716F, -0.0985F);
		LeftBoot.addChild(feather_r35);
		setRotationAngle(feather_r35, -0.6259F, -0.2169F, 1.1857F);
		feather_r35.setTextureOffset(35, 7).addBox(11.4073F, 8.9173F, 5.7943F, 3.0F, 0.0F, 1.0F, 0.0F, false);

		feather_r36 = new ModelRenderer(this);
		feather_r36.setRotationPoint(4.0794F, 6.8242F, 0.7251F);
		LeftBoot.addChild(feather_r36);
		setRotationAngle(feather_r36, -0.5606F, -0.1156F, 1.1423F);
		feather_r36.setTextureOffset(35, 6).addBox(-2.0F, 0.75F, 0.5F, 3.0F, 0.0F, 1.0F, 0.0F, false);

		feather_r37 = new ModelRenderer(this);
		feather_r37.setRotationPoint(4.0794F, 6.8242F, -0.7251F);
		LeftBoot.addChild(feather_r37);
		setRotationAngle(feather_r37, 0.652F, -0.1706F, 1.1512F);
		feather_r37.setTextureOffset(21, 38).addBox(-1.75F, 0.25F, -1.5F, 3.0F, 0.0F, 1.0F, 0.0F, false);

		feather_r38 = new ModelRenderer(this);
		feather_r38.setRotationPoint(8.4017F, -8.6716F, 0.0985F);
		LeftBoot.addChild(feather_r38);
		setRotationAngle(feather_r38, 0.6259F, 0.2169F, 1.1857F);
		feather_r38.setTextureOffset(38, 28).addBox(11.4073F, 8.6673F, -6.5443F, 3.0F, 0.0F, 1.0F, 0.0F, false);

		leftboot_r1 = new ModelRenderer(this);
		leftboot_r1.setRotationPoint(0.0F, 24.0F, 0.0F);
		LeftBoot.addChild(leftboot_r1);
		setRotationAngle(leftboot_r1, 0.0F, -0.0873F, 0.0F);
		leftboot_r1.setTextureOffset(66, 8).addBox(-2.5F, -17.75F, -2.5F, 5.0F, 6.0F, 5.0F, 0.0F, false);
		leftboot_r1.setTextureOffset(77, 55).addBox(-2.5F, -14.75F, -3.5F, 5.0F, 3.0F, 1.0F, 0.0F, false);
		leftboot_r1.setTextureOffset(9, 55).addBox(-2.0F, -13.75F, -4.5F, 4.0F, 2.0F, 1.0F, 0.0F, false);

		leftboot_r2 = new ModelRenderer(this);
		leftboot_r2.setRotationPoint(0.0F, 5.75F, 0.0F);
		LeftBoot.addChild(leftboot_r2);
		setRotationAngle(leftboot_r2, 0.0718F, -0.0936F, 0.175F);
		leftboot_r2.setTextureOffset(53, 64).addBox(-3.0F, -1.0F, -3.0F, 6.0F, 2.0F, 6.0F, 0.0F, false);
	}

	// THIS IS THE RENDERING OF THE ARMOR FOR SLOTS THIS IS NEEDED IN EACH JAVA FILE
	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {

		matrixStack.push();
		if (this.slot == EquipmentSlotType.HEAD) {
			this.Head.copyModelAngles(this.bipedHead);
			this.Head.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		}
		else if (this.slot == EquipmentSlotType.CHEST) {
			this.Body.copyModelAngles(this.bipedBody);
			this.Body.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
			this.RightArm.copyModelAngles(this.bipedRightArm);
			this.RightArm.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
			this.LeftArm.copyModelAngles(this.bipedLeftArm);
			this.LeftArm.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);

		} else if (this.slot == EquipmentSlotType.LEGS) {
			this.Belt.copyModelAngles(this.bipedBody);
			this.Belt.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
			this.RightLeg.copyModelAngles(this.bipedRightLeg);
			this.RightLeg.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
			this.LeftLeg.copyModelAngles(this.bipedLeftLeg);
			this.LeftLeg.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);

		} else if (this.slot == EquipmentSlotType.FEET) {
			this.RightBoot.copyModelAngles(this.bipedRightLeg);
			this.RightBoot.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
			this.LeftBoot.copyModelAngles(this.bipedLeftLeg);
			this.LeftBoot.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		}
		matrixStack.pop();
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}


	// This is also needed
	@SuppressWarnings("unchecked")
	public static <A extends BipedModel<?>> A getModel(EquipmentSlotType slot, LivingEntity entity) {
		boolean illager = entity instanceof AbstractIllagerEntity ||
				entity instanceof ZombieVillagerEntity ||
				entity instanceof AbstractVillagerEntity;
		int entityFlag = (slot.ordinal() & 15) | (illager ? 1 : 0) << 4 | (entity.isChild() ? 1 : 0) << 6;
		return (A) CACHE.computeIfAbsent(entityFlag, k -> new WitchArmorModel(1, slot));
	}

}
