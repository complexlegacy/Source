package ionic.player.packets;

import ionic.item.ItemAssistant;
import ionic.npc.NPC;
import ionic.npc.NPCEvent;
import ionic.npc.NPCHandler;
import ionic.npc.clicknpc.FirstClickNPC;
import ionic.npc.clicknpc.SecondClickNPC;
import ionic.npc.clicknpc.ThirdClickNPC;
import ionic.player.Client;
import ionic.player.movement.Movement;
import core.Constants;

/**
 * Click NPC
 */
public class ClickNPC implements PacketType
{
    public static final int ATTACK_NPC = 72, MAGE_NPC = 131, FIRST_CLICK = 155, SECOND_CLICK = 17, THIRD_CLICK = 21;@Override
    public void processPacket(Client player, int packetType, int packetSize)
    {
    	if (player.isCrafting) { player.isCrafting = false; }
    	if (player.isHerblore) { player.stopHerblore = true; }
    	if (player.isChopping) {
        	player.startAnimation(65535);
        	player.isChopping = false;
        }
    	if (player.doingAction(false) || player.isTeleporting() || player.getDoingAgility() || player.barrowsDoor)
    	{
    		return;
    	}
        player.npcIndex = 0;
        player.npcClickIndex = 0;
        player.playerIndex = 0;
        player.clickNpcType = 0;
        player.getPA().resetFollow();

        switch (packetType)
        {

            /**
             * Attack npc melee or range
             **/
        case ATTACK_NPC:
            if (!player.mageAllowed)
            {
                player.mageAllowed = true;
                player.sendMessage("I can't reach that.");
                break;
            }
            player.npcIndex = player.getInStream().readUnsignedWordA();
            
            if (NPCHandler.npcs[player.npcIndex] == null)
            {
                player.npcIndex = 0;
                break;
            }
            if (NPCHandler.npcs[player.npcIndex].MaxHP == 0)
            {
                player.npcIndex = 0;
                break;
            }
            if (NPCHandler.npcs[player.npcIndex] == null)
            {
                break;
            }
            
            if (NPCHandler.npcs[player.npcIndex].owner == player) {
    			player.sendMessage("You can't fight your own familiar.");
    			player.getCombat().resetPlayerAttack();
    			break;
    		}
    		if (NPCHandler.npcs[player.npcIndex].isFamiliar && (
    				!NPCHandler.npcs[player.npcIndex].inMulti() || !NPCHandler.npcs[player.npcIndex].inWild())) {
    			player.sendMessage("You can't attack a familiar that's not in a multi-combat danger zone");
    			player.getCombat().resetPlayerAttack();
    			break;
    		}
            
            if (player.autocastId > 0) player.autocasting = true;
            if (!player.autocasting && player.spellId > 0)
            {
                player.spellId = 0;
            }
            player.faceUpdate(player.npcIndex);
            player.usingMagic = false;
            boolean usingBow = false;
            boolean usingOtherRangeWeapons = false;
            boolean usingArrows = false;
            boolean usingCross = player.playerEquipment[Constants.WEAPON_SLOT] == 9185;
            if (player.playerEquipment[Constants.WEAPON_SLOT] >= 4214 && player.playerEquipment[Constants.WEAPON_SLOT] <= 4223) usingBow = true;
            for (int bowId: player.BOWS)
            {
                if (player.playerEquipment[Constants.WEAPON_SLOT] == bowId)
                {
                    usingBow = true;
                    for (int arrowId: player.ARROWS)
                    {
                        if (player.playerEquipment[Constants.ARROW_SLOT] == arrowId)
                        {
                            usingArrows = true;
                        }
                    }
                }
            }
            for (int otherRangeId: player.OTHER_RANGE_WEAPONS)
            {
                if (player.playerEquipment[Constants.WEAPON_SLOT] == otherRangeId)
                {
                    usingOtherRangeWeapons = true;
                }
            }
            if ((usingBow || player.autocasting) && player.goodDistance(player.getX(), player.getY(), NPCHandler.npcs[player.npcIndex].getX(), NPCHandler.npcs[player.npcIndex].getY(), 7))
            {
                Movement.stopMovement(player);
            }
            if (usingOtherRangeWeapons && player.goodDistance(player.getX(), player.getY(), NPCHandler.npcs[player.npcIndex].getX(), NPCHandler.npcs[player.npcIndex].getY(), 4))
            {
                Movement.stopMovement(player);
            }
            if (!usingCross && !usingArrows && usingBow && player.playerEquipment[Constants.WEAPON_SLOT] < 4212 && player.playerEquipment[Constants.WEAPON_SLOT] > 4223 && !usingCross)
            {
                player.sendMessage("You have run out of arrows!");
                break;
            }
            if (player.getCombat().correctBowAndArrows() < player.playerEquipment[Constants.ARROW_SLOT] && usingBow && !player.getCombat().usingCrystalBow() && player.playerEquipment[Constants.WEAPON_SLOT] != 9185 && player.playerEquipment[Constants.WEAPON_SLOT] != 18357)
            {
                player.sendMessage("You can't use " + ItemAssistant.getItemName(player.playerEquipment[Constants.ARROW_SLOT]).toLowerCase() + "s with a " + ItemAssistant.getItemName(player.playerEquipment[Constants.WEAPON_SLOT]).toLowerCase() + ".");
                Movement.stopMovement(player);
                player.getCombat().resetPlayerAttack();
                return;
            }
            if (player.playerEquipment[Constants.WEAPON_SLOT] == 9185 && !player.getCombat().properBolts())
            {
                player.sendMessage("You must use bolts with a crossbow.");
                Movement.stopMovement(player);
                player.getCombat().resetPlayerAttack();
                return;
            }
            
            if (player.followId > 0)
            {
                player.getPA().resetFollow();
            }
            if (player.attackTimer <= 0)
            {
            	NPC NPC = NPCHandler.npcs[player.npcIndex];
                player.getCombat().attackNpc(NPC);
                player.attackTimer++;
            }
            
            break;

            /**
             * Attack npc with magic
             **/
        case MAGE_NPC:
            if (!player.mageAllowed)
            {
                player.mageAllowed = true;
                player.sendMessage("I can't reach that.");
                break;
            }
            player.npcIndex = player.getInStream().readSignedWordBigEndianA();
            int castingSpellId = player.getInStream().readSignedWordA();
            player.usingMagic = false;

            if (NPCHandler.npcs[player.npcIndex] == null)
            {
                break;
            }

            if (NPCHandler.npcs[player.npcIndex].MaxHP == 0 || NPCHandler.npcs[player.npcIndex].npcType == 944)
            {
                player.sendMessage("You can't attack this npc.");
                break;
            }

            for (int i = 0; i < player.MAGIC_SPELLS.length; i++)
            {
                if (castingSpellId == player.MAGIC_SPELLS[i][0])
                {
                    player.spellId = i;
                    player.usingMagic = true;
                    break;
                }
            }
            if (castingSpellId == 1171)
            { // crumble undead
                for (int npc: Constants.UNDEAD_NPCS)
                {
                    if (NPCHandler.npcs[player.npcIndex].npcType != npc)
                    {
                        player.sendMessage("You can only attack undead monsters with this spell.");
                        player.usingMagic = false;
                        Movement.stopMovement(player);
                        break;
                    }
                }
            }
            if (player.autocasting) player.autocasting = false;

            if (player.usingMagic)
            {
                if (player.goodDistance(player.getX(), player.getY(), NPCHandler.npcs[player.npcIndex].getX(), NPCHandler.npcs[player.npcIndex].getY(), 6))
                {
                    Movement.stopMovement(player);
                }
                if (player.attackTimer <= 0)
                {
                	NPC NPC = NPCHandler.npcs[player.npcIndex];
                    player.getCombat().attackNpc(NPC);
                    player.attackTimer++;
                }
            }

            break;

        case FIRST_CLICK:
            player.npcClickIndex = player.inStream.readSignedWordBigEndian();
            player.npcType = NPCHandler.npcs[player.npcClickIndex].npcType;
            if (player.goodDistance(NPCHandler.npcs[player.npcClickIndex].getX(), NPCHandler.npcs[player.npcClickIndex].getY(), player.getX(), player.getY(), 1))
            {
                player.turnPlayerTo(NPCHandler.npcs[player.npcClickIndex].getX(), NPCHandler.npcs[player.npcClickIndex].getY());
                NPCHandler.npcs[player.npcClickIndex].facePlayer(player.playerId);
                FirstClickNPC.firstClickNpc(player, player.npcType);
            }
            else
            {
                player.clickNpcType = 1;
                NPCEvent.clickNpcType1Event(player);
                
            }
            break;

        case SECOND_CLICK:
            player.npcClickIndex = player.inStream.readUnsignedWordBigEndianA();
            player.npcType = NPCHandler.npcs[player.npcClickIndex].npcType;
            if (player.goodDistance(NPCHandler.npcs[player.npcClickIndex].getX(), NPCHandler.npcs[player.npcClickIndex].getY(), player.getX(), player.getY(), 1))
            {
                player.turnPlayerTo(NPCHandler.npcs[player.npcClickIndex].getX(), NPCHandler.npcs[player.npcClickIndex].getY());
                NPCHandler.npcs[player.npcClickIndex].facePlayer(player.playerId);
                SecondClickNPC.secondClickNpc(player, player.npcType);
            }
            else
            {
                player.clickNpcType = 2;
                NPCEvent.clickNpcType2Event(player);
            }
            break;

        case THIRD_CLICK:
            player.npcClickIndex = player.inStream.readSignedWord();
            player.npcType = NPCHandler.npcs[player.npcClickIndex].npcType;
            if (player.goodDistance(NPCHandler.npcs[player.npcClickIndex].getX(), NPCHandler.npcs[player.npcClickIndex].getY(), player.getX(), player.getY(), 1))
            {
                player.turnPlayerTo(NPCHandler.npcs[player.npcClickIndex].getX(), NPCHandler.npcs[player.npcClickIndex].getY());
                NPCHandler.npcs[player.npcClickIndex].facePlayer(player.playerId);
                ThirdClickNPC.thirdClickNpc(player, player.npcType);
            }
            else
            {
                player.clickNpcType = 3;
                NPCEvent.clickNpcType3Event(player);
            }
            break;
        }

    }
}