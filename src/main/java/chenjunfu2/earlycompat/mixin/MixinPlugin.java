package chenjunfu2.earlycompat.mixin;

import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class MixinPlugin implements IMixinConfigPlugin
{
	
	private boolean pcaAvailable;
	
	@Override
	public void onLoad(String mixinPackage)
	{
		pcaAvailable = FabricLoader
			.getInstance()
			.isModLoaded("pca-1_20_1");
	}
	
	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName)
	{
		// 只有带 "PcaCompat" 的 mixin 才受 PCA 影响
		if (mixinClassName.contains("PcaCompat"))
		{
			return pcaAvailable;
		}
		return true;
	}
	
	@Override public String getRefMapperConfig() { return null; }
	
	@Override public void acceptTargets(
		Set<String> myTargets, Set<String> otherTargets) {}
	
	@Override public List<String> getMixins() { return null; }
	
	@Override public void preApply(
		String targetClassName, ClassNode targetClass,
		String mixinClassName, IMixinInfo mixinInfo) {}
	
	@Override public void postApply(
		String targetClassName, ClassNode targetClass,
		String mixinClassName, IMixinInfo mixinInfo) {}
}