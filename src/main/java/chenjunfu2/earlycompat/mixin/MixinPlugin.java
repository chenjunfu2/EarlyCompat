package chenjunfu2.earlycompat.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.api.VersionParsingException;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class MixinPlugin implements IMixinConfigPlugin
{
	
	private boolean isPluslsCarpetAdditionAvailable;
	private boolean isDecoratedPotEarlyAvailable;
	
	@Override
	public void onLoad(String mixinPackage)
	{
		var loaderInstance = FabricLoader.getInstance();
		isPluslsCarpetAdditionAvailable = loaderInstance.getModContainer("pca-1_20_1")
			.map
			(
				modContainer ->
				{
					try
					{
						SemanticVersion installed = SemanticVersion.parse
						(
							modContainer.getMetadata().getVersion().getFriendlyString()
						);
						SemanticVersion required = SemanticVersion.parse("0.3.190");
						return installed.compareTo(required) >= 0;
					}
					catch (VersionParsingException e)
					{
						return false;
					}
				}
			).orElse(false);
		
		isDecoratedPotEarlyAvailable = loaderInstance.getModContainer("decoratedpotearly")
			.map
				(
					modContainer ->
					{
						try
						{
							SemanticVersion installed = SemanticVersion.parse
							(
								modContainer.getMetadata().getVersion().getFriendlyString()
							);
							SemanticVersion required = SemanticVersion.parse("1.0.1");
							return installed.compareTo(required) >= 0;
						}
						catch (VersionParsingException e)
						{
							return false;
						}
					}
				).orElse(false);
	}
	
	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName)
	{
		// 只有带 "PcaDecoratedPotEarlyCompat" 的 mixin 才受 PCA 和 DecoratedPotEarly 影响
		if (mixinClassName.contains("PcaDecoratedPotEarlyCompat"))
		{
			return isPluslsCarpetAdditionAvailable && isDecoratedPotEarlyAvailable;
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