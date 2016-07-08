package ionic.player.packets;

import ionic.player.Client;
import ionic.player.PlayerHandler;
import ionic.player.content.combat.vsplayer.Attack;
import ionic.player.content.gambling.GamblingHandler;
import ionic.player.content.miscellaneous.SnowBalls;
import ionic.player.movement.Movement;
import core.Constants;

/**
 * Attack Player
 **/
public class AttackPlayer implements PacketType
{

    public static final int ATTACK_PLAYER = 73, MAGE_PLAYER = 249;@
    Override
    public void processPacket(Client player, int packetType, int packetSize)
    {
    	if (player.isChopping) {
        	player.startAnimation(65535);
        	player.isChopping = false;
        }
    	if (player.doingAction(false) || player.isTeleporting() || player.getDoingAgility())
    	{
    		return;
    	}
        player.playerIndex = 0;
        player.npcIndex = 0;
        switch (packetType)
        {

            /**
             * Attack player
             **/
        case ATTACK_PLAYER:
        	player.setUsingRange(false);
            player.playerIndex = player.getInStream().readSignedWordBigEndian();
            if (PlayerHandler.players[player.playerIndex] == null)
            {
                break;
            }
            
            if (player.inPartyRoom()) {
				GamblingHandler.sendReq(player, PlayerHandler.players[player.playerIndex]);
				player.playerIndex = 0;
				return;
			}
            if (player.playerEquipment[3] == 10501 && !player.inWilderness()) {
            	SnowBalls.throwSnowBall(player, PlayerHandler.players[player.playerIndex]);
            	Movement.stopMovement(player);
                player.getCombat().resetPlayerAttack();
            	return;
            }

            if (player.isDead)
            {
                break;
            }

            if (player.autocastId > 0)
            {
                player.autocasting = true;
            }

            if (!player.autocasting && player.spellId > 0)
            {
                player.spellId = 0;
            }
            player.mageFollow = false;
            player.spellId = 0;
            player.usingMagic = false;
            boolean usingBow = false;
            boolean usingOtherRangeWeapons = false;
            boolean usingArrows = false;
            boolean usingCross = (player.playerEquipment[Constants.WEAPON_SLOT] == 9185 || player.playerEquipment[Constants.WEAPON_SLOT] == 18357);
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
                    player.setUsingRange(true);
                }
            }
            if (player.duelStatus == 5)
            {
                if (player.duelCount > 0)
                {
                    player.sendMessage("The duel hasn't started yet!");
                    player.playerIndex = 0;
                    return;
                }

                if (player.duelRule[2] && (usingBow || usingOtherRangeWeapons))
                {
                    player.sendMessage("Range has been disabled in this duel!");
                    return;
                }
                if (player.duelRule[3] && (!usingBow && !usingOtherRangeWeapons))
                {
                    player.sendMessage("Melee has been disabled in this duel!");
                    return;
                }
            }

            if ((usingBow || player.autocasting) && player.goodDistance(player.getX(), player.getY(), PlayerHandler.players[player.playerIndex].getX(), PlayerHandler.players[player.playerIndex].getY(), 6))
            {
                player.usingBow = true;
                Movement.stopMovement(player);
            }

            if (usingOtherRangeWeapons && player.goodDistance(player.getX(), player.getY(), PlayerHandler.players[player.playerIndex].getX(), PlayerHandler.players[player.playerIndex].getY(), 3))
            {
            	player.setUsingRange(true);
                Movement.stopMovement(player);
            }
            if (!usingBow)
                player.usingBow = false;
            if (!usingOtherRangeWeapons)
                player.usingRangeWeapon = false;

            if (!usingCross && !usingArrows && usingBow && player.playerEquipment[Constants.WEAPON_SLOT] != 15241 && player.playerEquipment[Constants.WEAPON_SLOT] != 4734)
            {
                player.sendMessage("You have run out of arrows!");
                return;
            }
            if ((player.playerEquipment[Constants.WEAPON_SLOT] == 9185 || player.playerEquipment[Constants.WEAPON_SLOT] == 18357) && !player.getCombat().properBolts())
            {
                player.sendMessage("You must use bolts with a crossbow.");
                Movement.stopMovement(player);
                player.getCombat().resetPlayerAttack();
                return;
            }
            if (Attack.requirementsToAttack(player))
            {
                player.followId = player.playerIndex;
                if (!player.usingMagic && !usingBow && !usingOtherRangeWeapons)
                {
                    player.followDistance = 1;
                    player.getPA().followPlayer();
                }
                if (player.attackTimer <= 0)
                {}
            }
            break;


            /**
             * Attack player with magic
             **/
        case MAGE_PLAYER:
        	player.setUsingRange(false);
            if (!player.mageAllowed)
            {
                player.mageAllowed = true;
                break;
            }
            //c.usingSpecial = false;
            //c.getItems().updateSpecialBar();

            player.playerIndex = player.getInStream().readSignedWordA();
            int castingSpellId = player.getInStream().readSignedWordBigEndian();
            player.usingMagic = false;
            if (PlayerHandler.players[player.playerIndex] == null)
            {
                break;
            }

            if (player.isDead)
            {
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

            if (player.autocasting)
                player.autocasting = false;

            if (!Attack.requirementsToAttack(player))
            {
                break;
            }
            if (player.duelStatus == 5)
            {
                if (player.duelCount > 0)
                {
                    player.sendMessage("The duel hasn't started yet!");
                    player.playerIndex = 0;
                    return;
                }
                if (player.duelRule[4])
                {
                    player.sendMessage("Magic has been disabled in this duel!");
                    return;
                }
            }

            for (int r = 0; r < player.REDUCE_SPELLS.length; r++)
            { // reducing spells, confuse etc
                if (PlayerHandler.players[player.playerIndex].REDUCE_SPELLS[r] == player.MAGIC_SPELLS[player.spellId][0])
                {
                    if ((System.currentTimeMillis() - PlayerHandler.players[player.playerIndex].reduceSpellDelay[r]) < PlayerHandler.players[player.playerIndex].REDUCE_SPELL_TIME[r])
                    {
                        player.sendMessage("That player is currently immune to this spell.");
                        player.usingMagic = false;
                        Movement.stopMovement(player);
                        player.getCombat().resetPlayerAttack();
                    }
                    break;
                }
            }


            if (System.currentTimeMillis() - PlayerHandler.players[player.playerIndex].teleBlockDelay < PlayerHandler.players[player.playerIndex].teleBlockLength && player.MAGIC_SPELLS[player.spellId][0] == 12445)
            {
                player.sendMessage("That player is already affected by this spell.");
                player.usingMagic = false;
                Movement.stopMovement(player);
                player.getCombat().resetPlayerAttack();
            }

            if (player.usingMagic)
            {
                if (player.goodDistance(player.getX(), player.getY(), PlayerHandler.players[player.playerIndex].getX(), PlayerHandler.players[player.playerIndex].getY(), 7))
                {
                    Movement.stopMovement(player);
                }
                if (Attack.requirementsToAttack(player))
                {
                    player.followId = player.playerIndex;
                    player.mageFollow = true;
                }
            }
            break;

        }


    }

}