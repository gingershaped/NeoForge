/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.neoforged.neoforge.oldtest.misc;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.gui.ModMismatchDisconnectedScreen;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

/**
 * This test mod provides a way to register a {@link CustomPacketPayload} with a different protocol version on the client and the server to cause a mod channel mismatch.
 * With this test mod and at least one of its features enabled, a {@link ModMismatchDisconnectedScreen} should appear when trying to join a test server,
 * displaying detailed information about why the handshake failed.
 * In case of a mismatch, the two displayed mod versions will be the same due to not being able to specify a different client and server mod version of this test mod.
 * This test mod is disabled by default to ensure that users can join test servers without needing to specifically disable this test mod.
 * <p>
 * In the past this test mod also registered a {@link SoundEvent} to cause a registry mismatch, but this is no longer the case,
 * as the network negotiation does not care for registry mismatches anymore.
 * </p>
 */
@Mod(ModMismatchTest.MOD_ID)
public class ModMismatchTest implements IPayloadHandler<ModMismatchTest.ModMismatchPayload> {
    public static final String MOD_ID = "mod_mismatch_test";

    private static final boolean ENABLED = false;
    // Enable these fields to register the channel for either the server, the client, or both.
    // If the channel is enabled for both dists, this test mod will be identified as mismatching between server and client.
    // If the channel is enabled for one dist only, this test mod will be identified as missing from the dist the channel hasn't been registered for.
    // Additionally, if the channel is missing for the client, a S2CModMismatchData packet will be sent to the client, containing all the information about the channel mismatch detected on the server.
    private static final boolean REGISTER_FOR_SERVER = true;
    private static final boolean REGISTER_FOR_CLIENT = true;

    private static final String CHANNEL_PROTOCOL_VERSION = FMLEnvironment.dist == Dist.CLIENT ? "V1" : "V2";

    public ModMismatchTest(IEventBus modBus) {
        if (ENABLED) {
            modBus.addListener(RegisterPayloadHandlersEvent.class, this::onRegisterPacketHandler);
        }
    }

    private void onRegisterPacketHandler(RegisterPayloadHandlersEvent event) {
        if ((FMLEnvironment.dist == Dist.DEDICATED_SERVER && REGISTER_FOR_SERVER) || (FMLEnvironment.dist == Dist.CLIENT && REGISTER_FOR_CLIENT)) {
            event
                    .registrar(CHANNEL_PROTOCOL_VERSION)
                    .configurationBidirectional(
                            ModMismatchPayload.TYPE,
                            ModMismatchPayload.STREAM_CODEC,
                            this);
        }
    }

    @Override
    public void handle(ModMismatchPayload payload, IPayloadContext context) {
        //Noop
    }

    public record ModMismatchPayload() implements CustomPacketPayload {
        private static final CustomPacketPayload.Type<ModMismatchPayload> TYPE = new Type<>(new ResourceLocation(MOD_ID, "mod_mismatch"));
        private static final StreamCodec<FriendlyByteBuf, ModMismatchPayload> STREAM_CODEC = StreamCodec.unit(new ModMismatchTest.ModMismatchPayload());

        @Override
        public CustomPacketPayload.Type<ModMismatchPayload> type() {
            return TYPE;
        }
    }
}
