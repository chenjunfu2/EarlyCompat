package chenjunfu2.earlycompat.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class EasyPlaceExtraProtocolHelper
{
	public static boolean isExtraProtocol(int protocolValue)
	{
		return (protocolValue & 0b0000_1000) == 0b0000_1000;//判断bit3是否为1，是的话则为扩展协议
	}
	
	public static double getRelativeHitX(Vec3d hitPos, BlockPos blockPos)
	{
		return hitPos.x - (double)blockPos.getX();
	}
	public static double getRelativeHitZ(Vec3d hitPos, BlockPos blockPos)
	{
		return hitPos.z - (double)blockPos.getZ();
	}
	
	public static int decodeProtocolValueFromHitDim(double relativeHitDim)
	{
		return ((int)relativeHitDim - 2) >>> 1;
	}
	
	public static double encodeProtocolValueToHitDim(double relativeHitDim, int protocolValue)
	{
		return relativeHitDim + (double)((protocolValue << 1) + 2);
	}
	
	public static int extraProtocolValueToRawProtocolValue(int protocolValue)
	{
		return (((protocolValue & 0b1111_0000) >>> 1) | (protocolValue & 0b0000_0111));//摘除bit3，拼接剩余位，一共剩余6bit可用，最大7bit，因为bit3作为协议判断所以少一位
	}
	
	public static int rawProtocolValueToExtraProtocolValue(int protocolValue)
	{
		return ((protocolValue & 0b0111_1000) << 1) | (protocolValue & 0b0000_0111) | 0b0000_1000;//把值从bit3中间分开，设置bit3为1
	}
	
	public static int addExtraProtocolBit(int protocolValue)
	{
		return protocolValue | 0b0000_1000;
	}
	
	public static int removeExtraProtocolBit(int protocolValue)
	{
		return protocolValue & ~((int)0b0000_1000);
	}
	
	
	public static Vec3d encodeProtocolValueToHitVecX(int protocolValue, Vec3d hitVec)
	{
		return new Vec3d(encodeProtocolValueToHitDim(hitVec.x, protocolValue), hitVec.y, hitVec.z);
	}
	
	public static Vec3d encodeExtraProtocolValueToHitVecX(int protocolValue, Vec3d hitVec)//值最多7bit
	{
		int extraProtocolValue = rawProtocolValueToExtraProtocolValue(protocolValue);
		return encodeProtocolValueToHitVecX(extraProtocolValue, hitVec);
	}
}
