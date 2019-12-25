/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ProtocolChat.java@author: karlatemp@vip.qq.com: 2019/12/24 下午11:13@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.impl.chat;

import cn.mcres.karlatemp.mxlib.module.chat.BungeeChatAPI;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.google.common.base.Objects;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class ProtocolChat implements BungeeChatAPI {
    public static abstract class AbstractPacket {
        // The packet we will be modifying
        protected PacketContainer handle;

        /**
         * Constructs a new strongly typed wrapper for the given packet.
         *
         * @param handle - handle to the raw packet data.
         * @param type   - the packet type.
         */
        protected AbstractPacket(PacketContainer handle, PacketType type) {
            // Make sure we're given a valid packet
            if (handle == null)
                throw new IllegalArgumentException("Packet handle cannot be NULL.");
            if (!Objects.equal(handle.getType(), type))
                throw new IllegalArgumentException(handle.getHandle()
                        + " is not a packet of type " + type);

            this.handle = handle;
        }

        /**
         * Retrieve a handle to the raw packet data.
         *
         * @return Raw packet data.
         */
        public PacketContainer getHandle() {
            return handle;
        }

        /**
         * Send the current packet to the given receiver.
         *
         * @param receiver - the receiver.
         * @throws RuntimeException If the packet cannot be sent.
         */
        public void sendPacket(Player receiver) {
            try {
                ProtocolLibrary.getProtocolManager().sendServerPacket(receiver,
                        getHandle());
            } catch (InvocationTargetException e) {
                throw new RuntimeException("Cannot send packet.", e);
            }
        }

        /**
         * Send the current packet to all online players.
         */
        public void broadcastPacket() {
            ProtocolLibrary.getProtocolManager().broadcastServerPacket(getHandle());
        }

        /**
         * Simulate receiving the current packet from the given sender.
         *
         * @param sender - the sender.
         * @throws RuntimeException If the packet cannot be received.
         * @see #receivePacket(Player)
         * @deprecated Misspelled. recieve to receive
         */
        @Deprecated
        public void recievePacket(Player sender) {
            try {
                ProtocolLibrary.getProtocolManager().recieveClientPacket(sender,
                        getHandle());
            } catch (Exception e) {
                throw new RuntimeException("Cannot recieve packet.", e);
            }
        }

        /**
         * Simulate receiving the current packet from the given sender.
         *
         * @param sender - the sender.
         * @throws RuntimeException if the packet cannot be received.
         */
        public void receivePacket(Player sender) {
            try {
                ProtocolLibrary.getProtocolManager().recieveClientPacket(sender,
                        getHandle());
            } catch (Exception e) {
                throw new RuntimeException("Cannot receive packet.", e);
            }
        }
    }

    public static class WrapperPlayServerChat extends AbstractPacket {
        public static final PacketType TYPE = PacketType.Play.Server.CHAT;

        public WrapperPlayServerChat() {
            super(new PacketContainer(TYPE), TYPE);
            handle.getModifier().writeDefaults();
        }

        public WrapperPlayServerChat(PacketContainer packet) {
            super(packet, TYPE);
        }

        /**
         * Retrieve the chat message.
         * <p>
         * Limited to 32767 bytes
         *
         * @return The current message
         */
        public WrappedChatComponent getMessage() {
            return handle.getChatComponents().read(0);
        }

        /**
         * Set the message.
         *
         * @param value - new value.
         */
        public void setMessage(WrappedChatComponent value) {
            handle.getChatComponents().write(0, value);
        }

        public EnumWrappers.ChatType getChatType() {
            return handle.getChatTypes().read(0);
        }

        public void setChatType(EnumWrappers.ChatType type) {
            handle.getChatTypes().write(0, type);
        }

        /**
         * Retrieve Position.
         * <p>
         * Notes: 0 - Chat (chat box) ,1 - System Message (chat box), 2 - Above
         * action bar
         *
         * @return The current Position
         * @deprecated Magic values replaced by enum
         */
        @Deprecated
        public byte getPosition() {
            Byte position = handle.getBytes().readSafely(0);
            if (position != null) {
                return position;
            } else {
                return getChatType().getId();
            }
        }

        /**
         * Set Position.
         *
         * @param value - new value.
         * @deprecated Magic values replaced by enum
         */
        @Deprecated
        public void setPosition(byte value) {
            handle.getBytes().writeSafely(0, value);

            if (EnumWrappers.getChatTypeClass() != null) {
                Arrays.stream(EnumWrappers.ChatType.values()).filter(t -> t.getId() == value).findAny()
                        .ifPresent(t -> handle.getChatTypes().writeSafely(0, t));
            }
        }
    }

    @Override
    public void send(Player player, BaseComponent bc) {
        WrapperPlayServerChat chat = new WrapperPlayServerChat();
        final WrappedChatComponent component = WrappedChatComponent.fromJson(ComponentSerializer.toString(bc));
        chat.setChatType(EnumWrappers.ChatType.CHAT);
        chat.setMessage(component);
        chat.sendPacket(player);
    }

    @Override
    public void send(Player player, BaseComponent... bcs) {
        WrapperPlayServerChat chat = new WrapperPlayServerChat();
        final WrappedChatComponent component = WrappedChatComponent.fromJson(ComponentSerializer.toString(bcs));
        chat.setChatType(EnumWrappers.ChatType.CHAT);
        chat.setMessage(component);
        chat.sendPacket(player);
    }
}
