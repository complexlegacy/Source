package ionic.player.packets;

import ionic.player.Client;
import core.Configuration;


public class PacketHandler{

	private static PacketType packetId[] = new PacketType[256];
	
	static {
		
		SilentPacket u = new SilentPacket();
		packetId[3] = u;
		packetId[202] = u;
		packetId[77] = u;
		packetId[86] = u;
		packetId[74] = u;
		packetId[78] = u;
		packetId[36] = u;
		packetId[226] = u;
		packetId[234] = u;
		packetId[246] = u;
		packetId[148] = u;
		packetId[183] = u;
		packetId[18] = u;
		packetId[230] = u;
		packetId[136] = u;
		packetId[189] = u;
		packetId[152] = u;
		packetId[200] = u;
		packetId[85] = u;
		packetId[165] = u;
		packetId[238] = u;
		packetId[150] = u;
		packetId[40] = new DialoguePacket();
		ClickObject co = new ClickObject();
		packetId[132] = co;
		packetId[252] = co;
		packetId[70] = co;
		packetId[228] = co;
		packetId[234] = co;
		packetId[57] = new ItemOnNpc();
		ClickNPC cn = new ClickNPC();
		packetId[72] = cn;
		packetId[131] = cn;
		packetId[155] = cn;
		packetId[17] = cn;
		packetId[21] = cn;
		packetId[16] = new ItemClick2();		
		packetId[75] = new ItemClick3();	
		packetId[122] = new ClickItem();
		packetId[241] = new ClickingInGame();
		packetId[4] = new Chat();
		packetId[236] = new PickupItem();
		packetId[87] = new DropItem();
		packetId[185] = new ClickingButtons();
		packetId[130] = new ClickingStuff();
		packetId[103] = new Commands();
		packetId[214] = new MoveItems();
		packetId[237] = new MagicOnItems();
		packetId[181] = new MagicOnFloorItems();
		packetId[202] = new IdleLogout();
		AttackPlayer ap = new AttackPlayer();
		packetId[73] = ap;
		packetId[249] = ap;
		packetId[128] = new ChallengePlayer();
		packetId[39] = new Trade();
		packetId[139] = new FollowPlayer();
		packetId[41] = new WearItem();
		packetId[145] = new RemoveItem();
		packetId[117] = new Remove5();
		packetId[43] = new Remove10();
		packetId[129] = new RemoveAll();
		packetId[101] = new ChangeAppearance();
		packetId[14] = new ItemOnPlayer();
		PrivateMessaging pm = new PrivateMessaging();
		packetId[188] = pm;
		packetId[126] = pm;
		packetId[215] = pm;
		packetId[74] = pm;
		packetId[95] = pm;
		packetId[133] = pm;
		packetId[135] = new RemoveX1();
		packetId[208] = new RemoveX2();
		Walking w = new Walking();
		packetId[98] = w;
		packetId[164] = w;
		packetId[248] = w;
		packetId[53] = new ItemOnItem();
		packetId[192] = new ItemOnObject();
		packetId[25] = new ItemOnGroundItem();
		ChangeRegions cr = new ChangeRegions();
		packetId[121] = cr;
		packetId[210] = cr;
		packetId[60] = new ClanChat();
		packetId[68] = new Operate();
		packetId[66] = new BankDragToTab();
		packetId[67] = new BankDragSwap();
		packetId[195] = new BankWithdraw1();
		packetId[196] = new BankWithdraw2();
		packetId[197] = new BankDeposit1();
		packetId[198] = new BankDeposit2();
		packetId[199] = new SummoningCreations();
		packetId[54] = new Shops();
		packetId[55] = new Shops();
		packetId[63] = new MoneyPouchWithdraw();
		packetId[253] = new GroundItemClick();
		packetId[157] = new MultipleActionButtons();
		packetId[158] = new GrandExchangePacket();
	}


	public static void processPacket(Client c, int packetType, int packetSize) {
        PacketType p = packetId[packetType];
        if(p != null && packetType > 0 && packetType < 257 && packetType == c.packetType && packetSize == c.packetSize) {
            if (Configuration.SERVER_PACKETS) 
            {
                c.sendMessage("PacketType: " + packetType + ". PacketSize: " + packetSize + ".");
            }
            try {
                p.processPacket(c, packetType, packetSize);
            } catch(Exception e) {
                e.printStackTrace();
            }
        } else {
            c.disconnected = true;
            System.out.println(c.playerName + "is sending invalid PacketType: " + packetType + ". PacketSize: " + packetSize);
        }
    }
	

}
