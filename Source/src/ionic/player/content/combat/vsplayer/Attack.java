package ionic.player.content.combat.vsplayer;

import ionic.item.ItemAssistant;
import ionic.item.ItemDegrade;
import ionic.player.Client;
import ionic.player.Player;
import ionic.player.PlayerHandler;
import ionic.player.content.combat.SpecialAttack;
import ionic.player.content.combat.vsplayer.magic.MagicFormula;
import ionic.player.content.combat.vsplayer.melee.Melee;
import ionic.player.content.combat.vsplayer.range.RangeFormula;
import ionic.player.content.gambling.GamblingHandler;
import ionic.player.content.miscellaneous.Skull;
import ionic.player.movement.Movement;
import core.Constants;
import utility.Misc;

/**
 * Attack player.
 * @author MGT Madness, created on 20-11-2013
 */
public class Attack
{

        public static void attackPlayer(final Player attacker, Player victim)
        {
                if (victim == null)
                {
                        return;
                }

                if (victim.isTeleporting())
                {
                        return;
                }

                if (victim.isDead)
                {
                        attacker.getCombat().resetPlayerAttack();
                        return;
                }

                if (attacker.isDead || victim.isDead)
                {
                        attacker.getCombat().resetPlayerAttack();
                        return;
                }

                if (!requirementsToAttack(attacker))
                {
                        return;
                }

                if (victim.teleporting || attacker.teleporting)
                {
                        attacker.getCombat().resetPlayerAttack();
                        return;
                }

                boolean sameSpot = attacker.absX == victim.getX() && attacker.absY == victim.getY();

                /* Player is more than 25 tiles away from target. */
                if (!attacker.goodDistance(victim.getX(), victim.getY(), attacker.getX(), attacker.getY(), 25) && !sameSpot)
                {
                        attacker.getCombat().resetPlayerAttack();
                        return;
                }

                if (victim.heightLevel != attacker.heightLevel)
                {
                        attacker.getCombat().resetPlayerAttack();
                        return;
                }

                attacker.getCombat().lowerBoosterCombatLevels();
                attacker.followId = victim.playerId;
                attacker.followId2 = 0;

                if (attacker.attackTimer > 0)
                {
                        return;
                }
                ItemDegrade.doDegrade(attacker, 1);
                attacker.usingBow = false;
                attacker.specEffect = 0;
                attacker.usingRangeWeapon = false;
                attacker.rangeItemUsed = 0;
                boolean usingBow = false;
                boolean usingArrows = false;
                boolean usingOtherRangeWeapons = false;
                boolean usingCross = attacker.playerEquipment[Constants.WEAPON_SLOT] == 9185 || attacker.playerEquipment[Constants.WEAPON_SLOT] == 18357;
                attacker.projectileStage = 0;

                /* Player is on same tile as target. */
                if (attacker.absX == victim.absX && attacker.absY == victim.absY)
                {
                        if (attacker.freezeTimer > 0)
                        {
                                attacker.getCombat().resetPlayerAttack();
                                return;
                        }
                        attacker.followId = victim.playerId;
                        attacker.attackTimer = 0;
                        //return;
                }
                if (!attacker.usingMagic)
                {
                        for (int bowId: attacker.BOWS)
                        {
                                if (attacker.playerEquipment[Constants.WEAPON_SLOT] == bowId)
                                {
                                        usingBow = true;
                                        attacker.setUsingRange(true);
                                        for (int arrowId: attacker.ARROWS)
                                        {
                                                if (attacker.playerEquipment[Constants.ARROW_SLOT] == arrowId)
                                                {
                                                        usingArrows = true;
                                                }
                                        }
                                }
                        }
                        for (int otherRangeId: attacker.OTHER_RANGE_WEAPONS)
                        {
                                if (attacker.playerEquipment[Constants.WEAPON_SLOT] == otherRangeId)
                                {
                                        usingOtherRangeWeapons = true;
                                }
                        }
                }
                if (attacker.autocasting)
                {
                        attacker.spellId = attacker.autocastId;
                        attacker.usingMagic = true;

                        // Stops player from autocasting Ancient magicks spells while not on Ancient magicks spellbook
                        if (attacker.spellId >= 33 && attacker.spellId <= 47 && attacker.playerMagicBook != 1)
                        {
                                return;
                        }

                }
                if (attacker.spellId > 0)
                {
                        attacker.usingMagic = true;

                        // Stops player from casting Ancient magicks spells while not on Ancient magicks spellbook
                        if (attacker.spellId >= 33 && attacker.spellId <= 47 && attacker.playerMagicBook != 1)
                        {
                                return;
                        }
                }
                attacker.attackTimer = attacker.getCombat().getAttackDelay(ItemAssistant.getItemName(attacker.playerEquipment[Constants.WEAPON_SLOT]).toLowerCase());
                if (attacker.duelRule[2] && (usingBow || usingOtherRangeWeapons))
                {
                        attacker.sendMessage("Range has been disabled in this duel!");
                        return;
                }
                if (attacker.duelRule[3] && (!usingBow && !usingOtherRangeWeapons && !attacker.usingMagic))
                {
                        attacker.sendMessage("Melee has been disabled in this duel!");
                        return;
                }
                if (attacker.duelRule[4] && attacker.usingMagic)
                {
                        attacker.sendMessage("Magic has been disabled in this duel!");
                        attacker.getCombat().resetPlayerAttack();
                        return;
                }

                /* Player using knifes, darts etc.. */
                if ((!attacker.goodDistance(attacker.getX(), attacker.getY(), victim.getX(), victim.getY(), 4) &&
                        (usingOtherRangeWeapons && !usingBow && !attacker.usingMagic)))
                {
                        attacker.attackTimer = 1;
                        if (!usingBow && !attacker.usingMagic && !usingOtherRangeWeapons && attacker.freezeTimer > 0)
                        {
                                attacker.getCombat().resetPlayerAttack();
                        }
                        return;
                }

                /* Player using Halberd. */
                if ((!attacker.goodDistance(attacker.getX(), attacker.getY(), victim.getX(), victim.getY(), 2) && (!usingOtherRangeWeapons && attacker.getCombat().usingHally() && !usingBow && !attacker.usingMagic)))
                {
                        attacker.attackTimer = 1;
                        if (!usingBow && !attacker.usingMagic && !usingOtherRangeWeapons && attacker.freezeTimer > 0)
                        {
                                attacker.getCombat().resetPlayerAttack();
                        }
                        return;
                }

                /* Player using Melee */
                if (!attacker.goodDistance(attacker.getX(), attacker.getY(), victim.getX(), victim.getY(),
                        attacker.getCombat().getRequiredDistance()) && (!usingOtherRangeWeapons && !attacker.getCombat().usingHally() && !usingBow && !attacker.usingMagic))
                {
                        attacker.attackTimer = 1;
                        if (!usingBow && !attacker.usingMagic && !usingOtherRangeWeapons && attacker.freezeTimer > 0)
                        {
                                attacker.getCombat().resetPlayerAttack();
                        }
                        return;
                }

                /* Player is using Melee and is diagonal from the target. */
                if (attacker.goodDistance(attacker.getX(), attacker.getY(), victim.getX(), victim.getY(),
                    attacker.getCombat().getRequiredDistance()) && (!usingOtherRangeWeapons && !attacker.getCombat().usingHally() && !usingBow && !attacker.usingMagic &&
                    attacker.getX() != victim.getX() && attacker.getY() != victim.getY()))
                {
                        attacker.getPA().movePlayerDiagonal(victim);
                        if (!usingBow && !attacker.usingMagic && !usingOtherRangeWeapons && attacker.freezeTimer > 0)
                        {
                                attacker.getCombat().resetPlayerAttack();
                        }
                }

                /* Player using Bow or Magic */
                if ((!attacker.goodDistance(attacker.getX(), attacker.getY(), victim.getX(), victim.getY(), 10) && (usingBow || attacker.usingMagic)))
                {
                        attacker.attackTimer = 1;
                        if (!usingBow && !attacker.usingMagic && !usingOtherRangeWeapons && attacker.freezeTimer > 0)
                        {
                                attacker.getCombat().resetPlayerAttack();
                        }
                        return;
                }
                if (!usingCross && !usingArrows && usingBow && !attacker.usingMagic && attacker.playerEquipment[Constants.WEAPON_SLOT] != 15241 && attacker.playerEquipment[Constants.WEAPON_SLOT] != 4734)
                {
                        attacker.sendMessage("You have run out of arrows!");
                        Movement.stopMovement(attacker);
                        attacker.getCombat().resetPlayerAttack();
                        return;
                }
                if (attacker.playerEquipment[Constants.WEAPON_SLOT] == 15241 && attacker.playerEquipment[Constants.ARROW_SLOT] != 15243)
                {
                	attacker.sendMessage("You have run out of Hand cannon shots.");
                    Movement.stopMovement(attacker);
                    attacker.getCombat().resetPlayerAttack();
                	return;
                }
                if (attacker.playerEquipment[Constants.WEAPON_SLOT] == 4734 && attacker.playerEquipment[Constants.ARROW_SLOT] != 4740)
                {
                	attacker.sendMessage("You have run out of Bolt racks.");
                    Movement.stopMovement(attacker);
                    attacker.getCombat().resetPlayerAttack();
                	return;
                }
                if (attacker.playerEquipment[Constants.WEAPON_SLOT] != 15241 && attacker.playerEquipment[Constants.WEAPON_SLOT] != 4734 && attacker.getCombat().correctBowAndArrows() < attacker.playerEquipment[Constants.ARROW_SLOT] && usingBow && attacker.playerEquipment[Constants.WEAPON_SLOT] != 9185 && attacker.playerEquipment[Constants.WEAPON_SLOT] != 18357 && !attacker.usingMagic)
                {
                        attacker.sendMessage("You can't use " + ItemAssistant.getItemName(attacker.playerEquipment[Constants.ARROW_SLOT]).toLowerCase() + "s with a " + ItemAssistant.getItemName(attacker.playerEquipment[Constants.WEAPON_SLOT]).toLowerCase() + ".");
                       Movement.stopMovement(attacker);
                        attacker.getCombat().resetPlayerAttack();
                        return;
                }
                if ((attacker.playerEquipment[Constants.WEAPON_SLOT] == 9185 || attacker.playerEquipment[Constants.WEAPON_SLOT] == 18357) && !attacker.getCombat().properBolts() && !attacker.usingMagic)
                {
                        attacker.sendMessage("You must use bolts with a crossbow.");
                       Movement.stopMovement(attacker);
                        attacker.getCombat().resetPlayerAttack();
                        return;
                }
                if (usingBow || attacker.usingMagic || usingOtherRangeWeapons || attacker.getCombat().usingHally())
                {
                       Movement.stopMovement(attacker);
                }
                if (!attacker.getCombat().checkMagicReqs(attacker.spellId))
                {
                       Movement.stopMovement(attacker);
                        attacker.getCombat().resetPlayerAttack();
                        return;
                }
                attacker.lastTimeEngagedPlayer = System.currentTimeMillis();
                victim.lastTimeEngagedPlayer = System.currentTimeMillis();
                attacker.faceUpdate(victim.playerId + 32768);
                if (attacker.inWilderness() && !attacker.inSafePkArea())
                {
                        if (!attacker.attackedPlayers.contains(attacker.playerIndex) && !PlayerHandler.players[attacker.playerIndex].attackedPlayers.contains(attacker.playerId))
                        {
                                attacker.attackedPlayers.add(attacker.playerIndex);
                                Skull.whiteSkull(attacker);
                        }
                }

                attacker.getCombat().resetEffects();

                if (attacker.usingSpecial && !attacker.usingMagic)
                {
                        if (attacker.duelRule[10] && attacker.duelStatus == 5)
                        {
                                attacker.sendMessage("Special attacks have been disabled during this duel!");
                                attacker.usingSpecial = false;
                                ItemAssistant.updateSpecialBar(attacker);
                                attacker.getCombat().resetPlayerAttack();
                                return;
                        }
                        if (attacker.getCombat().checkSpecAmount(attacker.playerEquipment[Constants.WEAPON_SLOT]))
                        {

                                SpecialAttack.activateSpecial(attacker, attacker.playerEquipment[Constants.WEAPON_SLOT], victim.playerId);
                                attacker.followId = attacker.playerIndex;
                                if (attacker.getUsingRange())
                                {
                                        RangeFormula.calculateFirstDamage(attacker, victim);
                                        RangeFormula.Effects(attacker, victim, 1);
                                        if (attacker.magicBowSpecialAttack || attacker.usingDarkBowSpecialAttack)
                                        {
                                                attacker.lastArrowUsed = attacker.playerEquipment[Constants.ARROW_SLOT];
                                                RangeFormula.calculateSecondDamage(attacker, victim);
                                                RangeFormula.Effects(attacker, victim, 2);
                                        }
                                        attacker.addAttacksGiven(1);
                                        victim.addAttacksReceived(1);
                                }
                                else
                                {
                                        Melee.meleeSpecialAttack(attacker, victim);
                                }
                                return;
                        }
                        else
                        {
                                attacker.sendMessage("You don't have the required special energy to use this attack.");
                                attacker.usingSpecial = false;
                                ItemAssistant.updateSpecialBar(attacker);
                                attacker.playerIndex = 0;
                                return;
                        }
                }
                if (!attacker.usingMagic)
                {
                        if (attacker.getUsingRange())
                        {
                                attacker.startAnimation(attacker.getCombat().weaponAttackAnimation(ItemAssistant.getItemName(attacker.playerEquipment[Constants.WEAPON_SLOT]).toLowerCase()));
                                attacker.mageFollow = false;
                                attacker.lastWeaponUsed = attacker.playerEquipment[Constants.WEAPON_SLOT];
                                attacker.lastArrowUsed = attacker.playerEquipment[Constants.ARROW_SLOT];
                                RangeFormula.calculateFirstDamage(attacker, victim);
                                RangeFormula.Effects(attacker, victim, 1);
                                if (attacker.playerEquipment[Constants.WEAPON_SLOT] == 11235)
                                {
                                        attacker.usingDarkBowNormalAttack = true;
                                        RangeFormula.calculateSecondDamage(attacker, victim);
                                }
                                attacker.addAttacksGiven(1);
                                victim.addAttacksReceived(1);
                        }
                        else
                        {
                                Melee.normalMeleeAttack(attacker, victim);
                        }
                }
                else
                {
                        attacker.setUsingRange(false);
                        attacker.startAnimation(attacker.MAGIC_SPELLS[attacker.spellId][2]);
                        attacker.mageFollow = true;
                        attacker.followId = attacker.playerIndex;
                        attacker.magicSplash = MagicFormula.isSplash(attacker, victim);
                        MagicFormula.calculateMagicDamage(attacker, victim);
                        MagicFormula.Effects(attacker, victim);
                        attacker.getCombat().addCombatXP(2, attacker.magicDamage);
                        attacker.addAttacksGiven(1);
                        victim.addAttacksReceived(1);
                }
                victim.underAttackBy = attacker.playerId;
                victim.logoutDelay = System.currentTimeMillis();
                victim.lastAttackedBy = attacker.playerId;
                attacker.lastArrowUsed = 0;
                attacker.rangeItemUsed = 0;
                if (!usingBow && !attacker.usingMagic && !usingOtherRangeWeapons)
                { // melee hit delay
                        attacker.followId = PlayerHandler.players[attacker.playerIndex].playerId;
                        attacker.getPA().followPlayer();
                        attacker.hitDelay = attacker.getCombat().getHitDelay(ItemAssistant.getItemName(attacker.playerEquipment[Constants.WEAPON_SLOT]).toLowerCase());
                        attacker.delayedDamage = Misc.random(attacker.getCombat().calculateMeleeMaxHit());
                        attacker.projectileStage = 0;
                        attacker.oldPlayerIndex = victim.playerId;
                }
                if (usingBow && !usingOtherRangeWeapons && !attacker.usingMagic || usingCross)
                { // range hit delay
                        if (attacker.playerEquipment[Constants.WEAPON_SLOT] >= 4212 && attacker.playerEquipment[Constants.WEAPON_SLOT] <= 4223)
                        {
                                attacker.rangeItemUsed = attacker.playerEquipment[Constants.WEAPON_SLOT];
                        }
                        else
                        {
                                attacker.rangeItemUsed = attacker.playerEquipment[Constants.ARROW_SLOT];
                                ItemAssistant.deleteArrow(attacker);
                        }
                        if (attacker.fightMode == attacker.RAPID) attacker.attackTimer--;
                        if (usingCross) attacker.usingBow = true;
                        attacker.usingBow = true;
                        attacker.followId = PlayerHandler.players[attacker.playerIndex].playerId;
                        attacker.getPA().followPlayer();
                        attacker.lastWeaponUsed = attacker.playerEquipment[Constants.WEAPON_SLOT];
                        attacker.lastArrowUsed = attacker.playerEquipment[Constants.ARROW_SLOT];
                        attacker.gfx100(attacker.getCombat().getRangeStartGFX());
                        attacker.hitDelay = attacker.getCombat().getHitDelay(ItemAssistant.getItemName(attacker.playerEquipment[Constants.WEAPON_SLOT]).toLowerCase());
                        attacker.projectileStage = 1;
                        attacker.oldPlayerIndex = victim.playerId;
                        attacker.getCombat().fireProjectilePlayer();
                }
                if (usingOtherRangeWeapons)
                { // knives, darts, etc hit delay
                        attacker.setUsingRange(true);
                        attacker.rangeItemUsed = attacker.playerEquipment[Constants.WEAPON_SLOT];
                        ItemAssistant.deleteEquipment(attacker);
                        attacker.usingRangeWeapon = true;
                        attacker.followId = PlayerHandler.players[attacker.playerIndex].playerId;
                        attacker.getPA().followPlayer();
                        attacker.gfx100(attacker.getCombat().getRangeStartGFX());
                        if (attacker.fightMode == attacker.RAPID) attacker.attackTimer--;
                        attacker.hitDelay = attacker.getCombat().getHitDelay(ItemAssistant.getItemName(attacker.playerEquipment[Constants.WEAPON_SLOT]).toLowerCase());
                        attacker.projectileStage = 1;
                        attacker.oldPlayerIndex = victim.playerId;
                        attacker.getCombat().fireProjectilePlayer();
                }
                if (attacker.usingMagic)
                { // magic hit delay
                        int pX = attacker.getX();
                        int pY = attacker.getY();
                        int nX = victim.getX();
                        int nY = victim.getY();
                        int offX = (pY - nY) * -1;
                        int offY = (pX - nX) * -1;
                        attacker.castingMagic = true;
                        attacker.projectileStage = 2;
                        if (attacker.MAGIC_SPELLS[attacker.spellId][3] > 0)
                        {
                                if (attacker.getCombat().getStartGfxHeight() == 100)
                                {
                                        attacker.gfx100(attacker.MAGIC_SPELLS[attacker.spellId][3]);
                                }
                                else
                                {
                                        attacker.gfx0(attacker.MAGIC_SPELLS[attacker.spellId][3]);
                                }
                        }
                        if (attacker.MAGIC_SPELLS[attacker.spellId][4] > 0)
                        {
                                attacker.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 78, attacker.MAGIC_SPELLS[attacker.spellId][4], attacker.getCombat().getStartHeight(), attacker.getCombat().getEndHeight(), -victim.playerId - 1, attacker.getCombat().getStartDelay());
                        }
                        if (attacker.autocastId > 0)
                        {
                                attacker.followId = attacker.playerIndex;
                                attacker.followDistance = 5;
                        }
                        attacker.hitDelay = attacker.getCombat().getHitDelay(ItemAssistant.getItemName(attacker.playerEquipment[Constants.WEAPON_SLOT]).toLowerCase());
                        attacker.oldPlayerIndex = victim.playerId;
                        attacker.oldSpellId = attacker.spellId;
                        attacker.spellId = 0;
                        if (attacker.MAGIC_SPELLS[attacker.oldSpellId][0] == 12891 && victim.isMoving)
                        {
                                attacker.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 85, 368, 25, 25, -victim.playerId - 1, attacker.getCombat().getStartDelay());
                        }
                        int freezeDelay = attacker.getCombat().getFreezeTime(); //freeze time
                        if (freezeDelay > 0 && victim.freezeTimer <= -3 && !attacker.magicSplash)
                        {
                                victim.freezeTimer = freezeDelay;
                                Movement.stopMovement(victim);
                                victim.getCombat().resetPlayerAttack();
                                victim.sendMessage("You have been frozen!");
                                attacker.sendMessage("You have frozen your target.");
                                victim.orb = false;
                                victim.frozenBy = attacker.playerId;
                        }
                        if (!attacker.autocasting && attacker.spellId <= 0) attacker.playerIndex = 0;
                }
        }

        /**
         * Check if the player can attack the target.
         *
         * @param attacker The player attacking.
         * @return True, if the player has all the requirements to attack the target.
         */
        public static boolean requirementsToAttack(Player attacker)
        {

                Client victim = (Client) PlayerHandler.players[attacker.playerIndex];
                if (victim == null)
                {
                        return false;
                }
                
                if (attacker.playerIndex == attacker.playerId) return false;
                if (victim.inDuelArena() && attacker.duelStatus != 5 && !attacker.usingMagic)
                {
                        if (attacker.arenas() || attacker.duelStatus == 5)
                        {
                                attacker.sendMessage("You can't challenge inside the arena!");
                                return false;
                        }
                        attacker.getDuelArena().requestDuel(attacker.playerIndex);
                        return false;
                }
                if (attacker.duelStatus == 5 && victim.duelStatus == 5)
                {
                        if (victim.duelingWith == attacker.getId())
                        {
                                return true;
                        }
                        else
                        {
                                attacker.sendMessage("This isn't your opponent!");
                                return false;
                        }
                }
                if (!victim.inWilderness() && victim.safeTimer <= 0)
                {
                        attacker.sendMessage("That player is not in a Pvp area.");
                        Movement.stopMovement(attacker);
                        attacker.getCombat().resetPlayerAttack();
                        return false;
                }
                if (!attacker.inWilderness() && attacker.safeTimer <= 0)
                {
                        attacker.sendMessage("You are not in a Pvp area.");
                        Movement.stopMovement(attacker);
                        attacker.getCombat().resetPlayerAttack();
                        return false;
                }
                if (!attacker.inSafePkArea())
                {
                                int combatDif1 = attacker.getCombat().getCombatDifference(attacker.combatLevel, victim.combatLevel);
                                if (combatDif1 > attacker.wildLevel || combatDif1 > victim.wildLevel)
                                {
                                        attacker.sendMessage("Your combat level difference is too great to attack that player here.");
                                        Movement.stopMovement(attacker);
                                        attacker.getCombat().resetPlayerAttack();
                                        return false;
                                }
                }
                if (!victim.inMulti())
                { // single combat zones
                        if (victim.underAttackBy != attacker.playerId && victim.underAttackBy != 0)
                        {
                                attacker.sendMessage("That player is already in combat.");
                                Movement.stopMovement(attacker);
                                attacker.getCombat().resetPlayerAttack();
                                return false;
                        }
                        if (victim.playerId != attacker.underAttackBy && attacker.underAttackBy != 0 || attacker.underAttackBy2 > 0)
                        {
                                attacker.sendMessage("You are already in combat.");
                                Movement.stopMovement(attacker);
                                attacker.getCombat().resetPlayerAttack();
                                return false;
                        }
                }
                return true;
        }

}