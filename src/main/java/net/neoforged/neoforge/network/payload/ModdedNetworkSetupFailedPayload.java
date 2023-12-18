/*
 * Copyright (c) NeoForged and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.neoforged.neoforge.network.payload;

import java.util.Map;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.internal.versions.neoforge.NeoForgeVersion;

public record ModdedNetworkSetupFailedPayload(Map<ResourceLocation, Component> failureReasons) implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(NeoForgeVersion.MOD_ID, "modded_network_setup_failed");
    public static final FriendlyByteBuf.Reader<? extends CustomPacketPayload> READER = ModdedNetworkSetupFailedPayload::new;

    public ModdedNetworkSetupFailedPayload(FriendlyByteBuf buf) {
        this(buf.readMap(FriendlyByteBuf::readResourceLocation, FriendlyByteBuf::readComponent));
    }

    @Override
    public void write(FriendlyByteBuf p_294947_) {
        p_294947_.writeMap(failureReasons, FriendlyByteBuf::writeResourceLocation, FriendlyByteBuf::writeComponent);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
