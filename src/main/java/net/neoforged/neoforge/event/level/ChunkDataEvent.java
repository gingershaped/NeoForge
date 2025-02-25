/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.neoforged.neoforge.event.level;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.status.ChunkType;
import net.minecraft.world.level.chunk.storage.ChunkSerializer;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;

/**
 * ChunkDataEvent is fired when an event involving chunk data occurs.<br>
 * If a method utilizes this {@link Event} as its parameter, the method will
 * receive every child event of this class.<br>
 * <br>
 * {@link #data} contains the NBTTagCompound containing the chunk data for this event.<br>
 * <br>
 * All children of this event are fired on the {@link NeoForge#EVENT_BUS}.<br>
 **/
public abstract class ChunkDataEvent extends ChunkEvent {
    private final CompoundTag data;

    public ChunkDataEvent(ChunkAccess chunk, CompoundTag data) {
        super(chunk);
        this.data = data;
    }

    public ChunkDataEvent(ChunkAccess chunk, LevelAccessor world, CompoundTag data) {
        super(chunk, world);
        this.data = data;
    }

    public CompoundTag getData() {
        return data;
    }

    /**
     * ChunkDataEvent.Load is fired when vanilla Minecraft attempts to load Chunk data.<br>
     * This event is fired during chunk loading in
     * {@link ChunkSerializer#read(ServerLevel, PoiManager, ChunkPos, CompoundTag)} which means it is async, so be careful.<br>
     * <br>
     * This event is not {@link ICancellableEvent}.<br>
     * <br>
     * This event does not have a result. {@link HasResult} <br>
     * <br>
     * This event is fired on the {@link NeoForge#EVENT_BUS}.<br>
     **/
    public static class Load extends ChunkDataEvent {
        private ChunkType status;

        public Load(ChunkAccess chunk, CompoundTag data, ChunkType type) {
            super(chunk, data);
            this.status = type;
        }

        public ChunkType getType() {
            return this.status;
        }
    }

    /**
     * ChunkDataEvent.Save is fired when vanilla Minecraft attempts to save Chunk data.<br>
     * This event is fired during chunk saving in
     * {@code ChunkMap#save(ChunkAccess)}. <br>
     * <br>
     * This event is not {@link ICancellableEvent}.<br>
     * <br>
     * This event does not have a result. {@link HasResult} <br>
     * <br>
     * This event is fired on the {@link NeoForge#EVENT_BUS}.<br>
     **/
    public static class Save extends ChunkDataEvent {
        public Save(ChunkAccess chunk, LevelAccessor world, CompoundTag data) {
            super(chunk, world, data);
        }
    }
}
