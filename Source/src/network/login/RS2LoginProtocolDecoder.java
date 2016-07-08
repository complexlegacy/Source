package network.login;

import ionic.player.Client;
import ionic.player.PlayerHandler;
import ionic.player.PlayerSave;
import network.connection.Connection;
import network.packet.Packet;
import network.packet.StaticPacketBuilder;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoFuture;
import org.apache.mina.common.IoFutureListener;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import utility.ISAACRandomGen;
import utility.Misc;
import core.Configuration;
import core.Server;

/**
 * Login protocol decoder.
 * @author Graham
 * @author Ryan / Lmctruck30 <- login Protocol fixes
 *
 */
public class RS2LoginProtocolDecoder extends CumulativeProtocolDecoder
{

	/**
	 * True, if the player's client is outdated.
	 */
     private boolean uidOutdated;



		/**
         * Parses the data in the provided byte buffer and writes it to
         * <code>out</code> as a <code>Packet</code>.
         *
         * @param session The IoSession the data was read from
         * @param in	  The buffer
         * @param out	 The decoder output stream to which to write the <code>Packet</code>
         * @return Whether enough data was available to create a packet
         */
        @
        Override
        public boolean doDecode(IoSession session, ByteBuffer in , ProtocolDecoderOutput out)
        {
                synchronized(session)
                {
                        Object loginStageObj = session.getAttribute("LOGIN_STAGE");
                        int loginStage = 0;
                        if (loginStageObj != null)
                        {
                                loginStage = (Integer) loginStageObj;
                        }
                        //Logger.log("recv login packet, stage: "+loginStage);
                        switch (loginStage)
                        {
                        case 0:
                                if (2 <= in .remaining())
                                {
                                        int protocol = in .get() & 0xff;@
                                        SuppressWarnings("unused")
                                        int nameHash = in .get() & 0xff;
                                        if (protocol == 14)
                                        {
                                                long serverSessionKey = ((long)(java.lang.Math.random() * 99999999D) << 32) + (long)(java.lang.Math.random() * 99999999D);
                                                StaticPacketBuilder s1Response = new StaticPacketBuilder();
                                                s1Response.setBare(true).addBytes(new byte[]
                                                {
                                                        0, 0, 0, 0, 0, 0, 0, 0
                                                }).addByte((byte) 0).addLong(serverSessionKey);
                                                session.setAttribute("SERVER_SESSION_KEY", serverSessionKey);
                                                session.write(s1Response.toPacket());
                                                session.setAttribute("LOGIN_STAGE", 1);
                                        }
                                        return true;
                                }
                                else
                                { in .rewind();
                                        return false;
                                }
                        case 1:
                                @SuppressWarnings("unused")
                                int loginType = -1, loginPacketSize = -1, loginEncryptPacketSize = -1;
                                if (2 <= in .remaining())
                                {
                                        loginType = in .get() & 0xff; //should be 16 or 18
                                        loginPacketSize = in .get() & 0xff;
                                        loginEncryptPacketSize = loginPacketSize - (36 + 1 + 1 + 2);
                                        if (loginPacketSize <= 0 || loginEncryptPacketSize <= 0)
                                        {
                                                System.out.println("Zero or negative login size.");
                                                session.close();
                                                return false;
                                        }
                                }
                                else
                                { in .rewind();
                                        return false;
                                }
                                if (loginPacketSize <= in .remaining())
                                {
                                        int magic = in .get() & 0xff;
                                        int version = in .getUnsignedShort();
                                        if (magic != 255)
                                        {
                                                System.out.println("Wrong magic id.");
                                                session.close();
                                                return false;
                                        }
                                        @SuppressWarnings("unused")
                                        int lowMem = in .get() & 0xff;
                                        for (int i = 0; i < 9; i++)
                                        { in .getInt();
                                        }
                                        loginEncryptPacketSize--;
                                        if (loginEncryptPacketSize != ( in .get() & 0xff))
                                        {
                                                System.out.println("Encrypted size mismatch.");
                                                session.close();
                                                return false;
                                        }
                                        if (( in .get() & 0xff) != 10)
                                        {
                                                System.out.println("Encrypted id != 10.");
                                                session.close();
                                                return false;
                                        }
                                        long clientSessionKey = in .getLong();
                                        long serverSessionKey = in .getLong();
                                        int uid = in .getInt();
                                        if (uid != 4566)
                                        {
                                        		uidOutdated = !uidOutdated;
                                        }

                                        String name = readRS2String( in );
                                        String pass = readRS2String( in );
                                        int sessionKey[] = new int[4];
                                        sessionKey[0] = (int)(clientSessionKey >> 32);
                                        sessionKey[1] = (int) clientSessionKey;
                                        sessionKey[2] = (int)(serverSessionKey >> 32);
                                        sessionKey[3] = (int) serverSessionKey;
                                        ISAACRandomGen inC = new ISAACRandomGen(sessionKey);
                                        for (int i = 0; i < 4; i++) sessionKey[i] += 50;
                                        ISAACRandomGen outC = new ISAACRandomGen(sessionKey);
                                        load(session, uid, name, pass, inC, outC, version, uidOutdated);
                                        session.getFilterChain().remove("protocolFilter");
                                        session.getFilterChain().addLast("protocolFilter", new ProtocolCodecFilter(new GameCodecFactory(inC)));
                                        return true;
                                }
                                else
                                { in .rewind();
                                        return false;
                                }
                        }
                }
                return false;
        }

        private synchronized void load(final IoSession session, final int uid, String name, String pass, final ISAACRandomGen inC, ISAACRandomGen outC, int version, boolean uidOutdated)
        {
                session.setAttribute("opcode", -1);
                session.setAttribute("size", -1);
                int loginDelay = 1;
                int returnCode = 2; // 255 is maximum

                name = name.trim();
                name = Misc.capitalize(name);
                pass = pass.toLowerCase();

                if (!name.matches("[A-Za-z0-9 ]+"))
                {
                        returnCode = 4;
                }

                if (name.length() > 12)
                {
                        returnCode = 8;
                }
                if (uidOutdated) 
                {
                	   returnCode = 24;
                }

                Client player = new Client(session, -1);
                player.playerName = name;
                player.playerName2 = player.playerName;
                player.playerPass = pass;
                player.setInStreamDecryption(inC);
                player.setOutStreamDecryption(outC);
                player.outStream.packetEncryption = outC;

                player.saveCharacter = false;
                
                if (Connection.isNamedBanned(player.playerName))
                {
                	returnCode = 1;
                }

                if (PlayerHandler.isPlayerOn(name))
                {
                        returnCode = 5;
                }

                if (PlayerHandler.playerCount >= Configuration.MAX_PLAYERS)
                {
                        returnCode = 7;
                }

                if (Server.UpdateServer)
                {
                        returnCode = 14;
                }

                if (returnCode == 2)
                {
                        int load = PlayerSave.loadGame(player, player.playerName, player.playerPass);
                        if (load == 0)
                                player.addStarter = true;
                        if (load == 3)
                        {
                                returnCode = 3;
                                player.saveFile = false;
                        }
                        else
                        {
                                for (int i = 0; i < player.playerEquipment.length; i++)
                                {
                                        if (player.playerEquipment[i] == 0)
                                        {
                                                player.playerEquipment[i] = -1;
                                                player.playerEquipmentN[i] = 0;
                                        }
                                }
                                if (!Server.playerHandler.newPlayerClient(player))
                                {
                                        returnCode = 7;
                                        player.saveFile = false;
                                }
                                else
                                {
                                        player.saveFile = true;
                                }
                        }
                }

                player.packetType = -1;
                player.packetSize = 0;

                StaticPacketBuilder bldr = new StaticPacketBuilder();
                bldr.setBare(true);
                bldr.addByte((byte) returnCode);
                if (returnCode == 2)
                {
                        player.saveCharacter = true;
                        bldr.addByte((byte) player.playerRights);
                }
                else if (returnCode == 21)
                {
                        bldr.addByte((byte) loginDelay);
                }
                else
                {
                        bldr.addByte((byte) 0);
                }
                player.isActive = true;
                bldr.addByte((byte) 0);
                Packet pkt = bldr.toPacket();
                session.setAttachment(player);
                session.write(pkt).addListener(new IoFutureListener()
                {@
                        Override
                        public void operationComplete(IoFuture arg0)
                        {
                                session.getFilterChain().remove("protocolFilter");
                                session.getFilterChain().addFirst("protocolFilter", new ProtocolCodecFilter(new GameCodecFactory(inC)));
                        }
                });
        }

        private synchronized String readRS2String(ByteBuffer in )
        {
                StringBuilder sb = new StringBuilder();
                byte b;
                while ((b = in .get()) != 10)
                {
                        sb.append((char) b);
                }
                return sb.toString();
        }



        /**
         * Releases the buffer used by the given session.
         *
         * @param session The session for which to release the buffer
         * @throws Exception if failed to dispose all resources
         */
        @
        Override
        public void dispose(IoSession session) throws Exception
        {
                super.dispose(session);
        }

}