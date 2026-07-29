package chenjunfu2.earlycompat.util;

import net.minecraft.block.BlockState;

public interface MultiStageBlockProtocolStateAdapter
{
	void earlycompat$setLoopCount(LoopContext ctx);//根据当前客户端和目标状态，决定需要放置的次数
	int earlycompat$toProtocolValueLoop(LoopContext ctx);//根据当前客户端和目标状态以及循环上下文，返回本次放置的协议值

	class LoopContext
	{
		public BlockState stateSchematic = null;
		public BlockState stateClient = null;
		public int loopCount = 0;
		public int loopIndex = 0;
   		public Object data;
	}
}
