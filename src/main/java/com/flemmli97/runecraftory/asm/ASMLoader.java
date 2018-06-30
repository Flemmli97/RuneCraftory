package com.flemmli97.runecraftory.asm;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.Name;

@MCVersion("1.12.2")
@Name("RuneCraftory/ASMLoader")
public class ASMLoader implements IFMLLoadingPlugin
{
    public String[] getASMTransformerClass() {
        return new String[] { ASMTransformer.class.getName() };
    }
    
    public String getModContainerClass() {
        return null;
    }
    
    public String getSetupClass() {
        return null;
    }
    
    public void injectData(Map<String, Object> data) {
    }
    
    public String getAccessTransformerClass() {
        return null;
    }
}
