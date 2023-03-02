package com.minecraft7771.gtnhintergalactic.proxy;

import net.minecraft.util.IIcon;

import com.minecraft7771.gtnhintergalactic.block.BlockSpaceElevatorCable;
import com.minecraft7771.gtnhintergalactic.render.RenderSpaceElevatorCable;
import com.minecraft7771.gtnhintergalactic.tile.TileEntitySpaceElevatorCable;
import com.mitchej123.hodgepodge.textures.IPatchedTextureAtlasSprite;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.DefaultArtifactVersion;
import cpw.mods.fml.common.versioning.InvalidVersionSpecificationException;
import cpw.mods.fml.common.versioning.VersionRange;

/**
 * Proxy used by the client to load stuff
 *
 * @author minecraft7771
 */
public class ClientProxy extends CommonProxy {

    private boolean notifyHodgepodgeTextureUsed = false;

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
        try {
            ArtifactVersion accepted = new DefaultArtifactVersion(
                    "hodgepodge",
                    VersionRange.createFromVersionSpec("[2.0.0,3)"));
            ModContainer mc = Loader.instance().getIndexedModList().get("hodgepodge");
            if (mc != null) notifyHodgepodgeTextureUsed = accepted.containsVersion(mc.getProcessedVersion());
        } catch (InvalidVersionSpecificationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        BlockSpaceElevatorCable.setRenderID(RenderingRegistry.getNextAvailableRenderId());
        RenderingRegistry.registerBlockHandler(BlockSpaceElevatorCable.getRenderID(), new RenderSpaceElevatorCable());
        ClientRegistry
                .bindTileEntitySpecialRenderer(TileEntitySpaceElevatorCable.class, new RenderSpaceElevatorCable());
    }

    /**
     * Mark a texture as used, to prevent hodgepodge from optimizing it
     *
     * @param o Textured to be used
     */
    @Override
    public void markTextureUsed(IIcon o) {
        if (notifyHodgepodgeTextureUsed) {
            if (o instanceof IPatchedTextureAtlasSprite) ((IPatchedTextureAtlasSprite) o).markNeedsAnimationUpdate();
        }
    }
}