package ionic.player;

import network.packet.Stream;

import org.apache.mina.common.IoSession;

import core.Constants;

public class Client extends Player
{

		public Client(IoSession s, int _playerId)
        {

                super(_playerId);
                this.session = s;
                synchronized(this)

                {
                        outStream = new Stream(new byte[Constants.BUFFER_SIZE]);
                        outStream.currentOffset = 0;
                }

                inStream = new Stream(new byte[Constants.BUFFER_SIZE]);
                inStream.currentOffset = 0;
                buffer = new byte[Constants.BUFFER_SIZE];

        }
}