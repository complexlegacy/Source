package ionic.player.content.combat.vsplayer.melee;

import ionic.player.Player;
import core.Constants;

/**
 * Handle Melee animations etc..
 * @author MGT Madness, created on 20-11-2013.
 */
public class MeleeData 
{
	
	public static int getWeaponAnimation(Player player, String weaponName)
    {
        if (player.playerEquipment[Constants.WEAPON_SLOT] <= 0)
        {
            if (player.combatType(player.ACCURATE) || player.combatType(player.BLOCK))
            {
            	return 422;
            }
            if (player.combatType(player.AGGRESSIVE)) 
            {
            	return 423;
            }
        }
        if (weaponName.contains("halberd"))
        {
            return 440;
        }
        if (weaponName.startsWith("dragon dagger"))
        {
            return 402;
        }
        if (weaponName.endsWith("dagger"))
        {
            return 412;
        }
        if (weaponName.contains("2h sword") || weaponName.contains("godsword") || weaponName.contains("aradomin sword"))
        {
            if (player.combatType(player.AGGRESSIVE) || player.combatType(player.ACCURATE)) 
            {
            	return 7041;
            }
            if (player.combatType(player.CONTROLLED)) 
            {
            	return 7048;
            }
            if (player.combatType(player.DEFENSIVE)) 
            {
            	return 7049;
            }
        }
        if (weaponName.contains("sword")) 
        	return 390;
        if (weaponName.contains("battleaxe")) 
        {
        	return 395;
        }
        if (weaponName.contains("scimitar") || weaponName.contains("longsword") || weaponName.contains("korasi"))
        {
            switch (player.fightMode)
            {
            case 0:
                return 390;
            case 1:
                return 390;
            case 2:
                return 390;
            case 3:
                return 390;
            }
        }
        if (weaponName.contains("pickaxe"))
        {
            return 13035;
        }
        if (weaponName.contains("rapier"))
        {
            switch (player.fightMode)
            {
            case 0:
                return 12028;
            case 1:
                return 12028;
            case 2:
                return 12028;
            case 3:
                return 12028;
            }
        }
        switch (player.playerEquipment[Constants.WEAPON_SLOT])
        {
        case 6522:
            // Toktz-xil-ul (tzhaar range weapon, looks like a hoolahoop)
            return 2614;
        case 1434:
            // Dragon mace
            return 13035;
        case 4153:
            // Granite maul
            return 1665;
        case 4726:
            // Guthan's spear 
            return 2080;
        case 4747:
            // Torags hammers
            return 0x814;
        case 13905:
        case 11716:
            // Vesta's spear and Zamorokian spear
            if (player.combatType(player.ACCURATE))
                return 12006;
            if (player.combatType(player.AGGRESSIVE))
                return 13041;
            return 13041;
        case 4718:
            // Dharok's greataxe
            if (player.combatType(player.AGGRESSIVE))
                return 2066;
            if (player.combatType(player.ACCURATE))
                return 2066;
            return 2067;
        case 4710:
            // Ahrim's staff
            return 406;
            // Boxing gloves
        case 7671:
        case 7673:
            return 3678;
        case 14484:
            // Dragon claws
            return 393;
        case 4755:
            // Verac's flail
            return 2062;
        case 13902:
            // Statius warhammer
            return 13035;
        case 10887: // Barrelchest anchor
            return 5865;
        case 4151:
        case 15445:
        case 15444:
        case 15443:
        case 15442:
        case 15441:
        case 21371:
        case 21372:
        case 21373:
        case 21374:
        case 21375:
            return 1658;
        case 1215:
        case 5698:
            return 402;
        case 6528:
            // Obby maul
        case 18353:
            return 2661;
        default:
            return 451;
        }
    }

}
