package com.noahhusby.sledgehammer.communication;

import com.noahhusby.sledgehammer.handlers.TaskHandler;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CommunicationMessage implements IMessage {

    public String text;

    public CommunicationMessage() { }

    public CommunicationMessage(String text) {
        this.text = text;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        text = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, text);
    }

    public static class Handler implements IMessageHandler<CommunicationMessage, IMessage> {

        @Override
        public IMessage onMessage(CommunicationMessage message, MessageContext ctx) {
            TaskHandler.getInstance().newMessage(message);
            return null;
        }
    }
}