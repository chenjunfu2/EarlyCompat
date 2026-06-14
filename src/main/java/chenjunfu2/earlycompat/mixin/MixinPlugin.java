package chenjunfu2.earlycompat.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.api.VersionParsingException;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import chenjunfu2.earlycompat.EarlyCompat;

import java.util.List;
import java.util.Set;

public class MixinPlugin implements IMixinConfigPlugin
{
	
	private boolean isPluslsCarpetAdditionAvailable;
	private boolean isMasaGadgetAvailable;
	
	private boolean isDecoratedPotEarlyAvailable;
	private boolean isCrafterEarlyAvailable;
	
	private boolean checkVersionConstraint(SemanticVersion installed, String constraint) throws VersionParsingException
	{
		constraint = constraint.trim();
		if (constraint.endsWith("+"))
		{
			String minVerStr = constraint.substring(0, constraint.length() - 1);
			SemanticVersion minVer = SemanticVersion.parse(minVerStr);
			return installed.compareTo(minVer) >= 0;
		}
		else if (constraint.endsWith("-"))
		{
			String maxVerStr = constraint.substring(0, constraint.length() - 1);
			SemanticVersion maxVer = SemanticVersion.parse(maxVerStr);
			return installed.compareTo(maxVer) <= 0;
		}
		else if (constraint.contains(".."))
		{
			String[] parts = constraint.split("\\.\\.");
			if (parts.length != 2)
			{
				EarlyCompat.LOGGER.warn("Invalid range constraint: {}", constraint);
				return false;
			}
			SemanticVersion lower = SemanticVersion.parse(parts[0]);
			SemanticVersion upper = SemanticVersion.parse(parts[1]);
			return installed.compareTo(lower) >= 0 && installed.compareTo(upper) <= 0;
		}
		else
		{
			// 默认精确等于
			SemanticVersion required = SemanticVersion.parse(constraint);
			return installed.compareTo(required) == 0;
		}
	}
	
	private boolean checkModVersion(String modName, String modId, String versionConstraint)
	{
		return FabricLoader.getInstance().getModContainer(modId)
			.map(container ->
			{
				try
				{
					SemanticVersion installed = SemanticVersion.parse
					(
						container.getMetadata().getVersion().getFriendlyString()
					);
					
					boolean satisfied = checkVersionConstraint(installed, versionConstraint);
					if (satisfied)
					{
						EarlyCompat.LOGGER.info("{} version {} satisfies constraint {}",
							modName, installed.getFriendlyString(), versionConstraint);
					}
					else
					{
						EarlyCompat.LOGGER.warn("{} version {} does NOT satisfy constraint {}",
							modName, installed.getFriendlyString(), versionConstraint);
					}
					
					return satisfied;
				}
				catch (VersionParsingException e)
				{
					EarlyCompat.LOGGER.warn("Failed to parse version of {}: {}", modName, e.getMessage());
					return false;
				}
			})
			.orElseGet(() ->
			{
				EarlyCompat.LOGGER.info("{} not found, skipping", modName);
				return false;
			});
	}
	
	@Override
	public void onLoad(String mixinPackage)
	{
		isPluslsCarpetAdditionAvailable = checkModVersion("PluslsCarpetAddition","pca-1_20_1","0.3.190+");
		isMasaGadgetAvailable = checkModVersion("MasaGadget","masa_gadget_mod","4.0.395+");
		isDecoratedPotEarlyAvailable = checkModVersion("DecoratedPotEarly","decoratedpotearly","1.0.0+");
		isCrafterEarlyAvailable = checkModVersion("CrafterEarly","crafter-early","1.0.0+");
	}
	
	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName)
	{
		// 只有带 "PcaVanillaCompat" 的 mixin 才受 PCA 影响
		if(mixinClassName.contains("PcaVanillaCompat"))//原版修复
		{
			return isPluslsCarpetAdditionAvailable;
		}
		
		// 只有带 "PcaDecoratedPotEarlyCompat" 的 mixin 才受 PCA 和 DecoratedPotEarly 影响
		if (mixinClassName.contains("PcaDecoratedPotEarlyCompat"))//陶罐移植修复
		{
			return isPluslsCarpetAdditionAvailable && isDecoratedPotEarlyAvailable;
		}
		
		// 只有带 "PcaCrafterEarlyCompat" 的 mixin 才受 PCA 和 CrafterEarly 影响
		if (mixinClassName.contains("PcaCrafterEarlyCompat"))//合成器移植修复
		{
			return isPluslsCarpetAdditionAvailable && isCrafterEarlyAvailable;
		}
		
		// Masa原版方块同步修复
		if(mixinClassName.contains("MasaVanillaCompat"))
		{
			return isMasaGadgetAvailable;
		}
		
		// 陶罐方块同步修复
		if(mixinClassName.contains("MasaDecoratedPotEarlyCompat"))
		{
			return isMasaGadgetAvailable && isDecoratedPotEarlyAvailable;
		}
		
		// 合成器方块同步修复
		if(mixinClassName.contains("MasaCrafterEarlyCompat"))
		{
			return isMasaGadgetAvailable && isCrafterEarlyAvailable;
		}
		
		// 剩下全部允许通过
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