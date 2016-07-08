package network.connection;

import ionic.player.Client;
import ionic.player.Player;
import network.login.CodecFactory;
import network.packet.Packet;

import org.apache.mina.common.IdleStatus;
import org.apache.mina.common.IoHandler;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;

public class ConnectionHandler implements IoHandler
{

        @
        Override
        public void exceptionCaught(IoSession arg0, Throwable arg1)
        throws Exception
        {}

        @
        Override
        public void messageReceived(IoSession arg0, Object arg1) throws Exception
        {
                if(arg0.getAttachment() != null) 
			{
						Player plr = (Player) arg0.getAttachment();
						plr.queueMessage((Packet) arg1);
			}
        	/*
                if (arg0.getAttachment() != null)
                {
                        Packet packet = (Packet) arg1;
                        Player player = (Player) arg0.getAttachment();
                        if (packet.getId() == 41)
                        {
                                player.timeOutCounter = 0;
                                player.wearId = packet.readUnsignedWord();
                                player.wearSlot = packet.readUnsignedWordA();
                                player.interfaceId = packet.readUnsignedWordA();
                                if (player.doingAction(false) || player.getDoingAgility() || player.isTeleporting() || player.isDead)
                                {
                                        return;
                                }
                                if (!player.getItems().playerHasItem(player.wearId, 1, player.wearSlot))
                                {
                                        return;
                                }
                                if (player.wearId >= 5509 && player.wearId <= 5515)
                                {
                                    int pouch = -1;
                                    int a = player.wearId;
                                    if (a == 5509)
                                        pouch = 0;
                                    if (a == 5510)
                                        pouch = 1;
                                    if (a == 5512)
                                        pouch = 2;
                                    if (a == 5514)
                                        pouch = 3;
                                    player.getPA().emptyPouch(pouch);
                                    player.sendMessage("You have emptied your pouch.");
                                    return;
                                }
                                if ((player.playerIndex > 0 || player.npcIndex > 0) && player.wearId != 4153)
                                {
                                        player.getCombat().resetPlayerAttack();
                                }
                                if (player.wearId == 4153)
                                {
                                        player.usedGmaul = true;
                                }
                                if (player.resting)
                                {
                                        AgilityAssistant.stopResting((Client) player);
                                }
                                player.getItems().wearItem(player.wearId, player.wearSlot);
                        }
                        else
                        {
                                player.queueMessage((Packet) arg1);
                        }
                }
                */
        }



        @
        Override
        public void messageSent(IoSession arg0, Object arg1) throws Exception
        {}

        @
        Override
        public void sessionClosed(IoSession arg0) throws Exception
        {
                if (arg0.getAttachment() != null)
                {
                        Client plr = (Client) arg0.getAttachment();
                        plr.disconnected = true;
                }
                HostList.getHostList().remove(arg0);
        }

        @
        Override
        public void sessionCreated(IoSession arg0) throws Exception
        {
                if (!HostList.getHostList().add(arg0))
                {
                        arg0.close();
                }
                else
                {
                        arg0.setAttribute("inList", Boolean.TRUE);
                }
        }

        @
        Override
        public void sessionIdle(IoSession arg0, IdleStatus arg1) throws Exception
        {
                arg0.close();
        }

        @
        Override
        public void sessionOpened(IoSession arg0) throws Exception
        {
                arg0.setIdleTime(IdleStatus.BOTH_IDLE, 60);
                arg0.getFilterChain().addLast("protocolFilter", new ProtocolCodecFilter(new CodecFactory()));
        }

}