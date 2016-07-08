  package ionic.player.packets;

import ionic.item.ItemAssistant;
import ionic.player.Client;
import ionic.player.MysteryBox;
import ionic.player.content.combat.dwarfcannon.CannonHandler;
import ionic.player.content.consumable.Food;
import ionic.player.content.consumable.Potion;
import ionic.player.content.miscellaneous.ArmourSets;
import ionic.player.content.miscellaneous.Digging;
import ionic.player.content.miscellaneous.EssencePouch;
import ionic.player.content.miscellaneous.Teleport;
import ionic.player.content.skills.herblore.Herblore;
import ionic.player.content.skills.prayer.Prayer;
import ionic.player.content.skills.woodcutting.BirdNest;

  /**
   * Clicking an item, bury bone, eat food etc
   **/

  public class ClickItem implements PacketType {
	  @Override
      public void processPacket(final Client player, int packetType, int packetSize) {
	  
	  if (player.doingAction(false) || player.getDoingAgility() || player.isTeleporting() || player.barrowsDoor) {
  			return;
  	  }
	  
	  if (player.isSmithing) { player.stopSmithing = true; }
	  if (player.isHerblore) { player.stopHerblore = true; }
	  if (player.isCrafting) { player.isCrafting = false; }
	  
          player.getInStream().readSignedWordBigEndianA();
          int itemSlot = player.getInStream().readUnsignedWordA();
          int itemId = player.getInStream().readUnsignedWordBigEndian();
          
          
          if (itemId != player.playerItems[itemSlot] - 1) {
              return;
          }
          
          if (Prayer.buryBones(player, itemId)) {
        	  return;
          }
          
          if (Potion.drink(player, itemId, itemSlot)) {
        	  return;
          }
          
          if (itemId == 6199) { MysteryBox.openBox(player); }
          if (itemId >= 5070 && itemId <= 5074) { BirdNest.searchNest(player, itemId); }
          
          if (Herblore.cleanHerb(player, itemId)) {
        	  return;
          }
          
          if (ArmourSets.isSet(itemId)) {
  			ArmourSets.handleSet(player, itemId);
  			return;
          }


          if (Food.isFood(itemId)) {
              Food.eat(player, itemId, itemSlot);
              return;
          }
          
          switch(itemId) {
          
          case 6:
        	  CannonHandler.setUpCannon(player);
        	  break;
          
          case 20121:
          case 20122:
          case 20123:
          case 20124:
        	  if (ItemAssistant.playerHasItem(player, 20121)
        			  && ItemAssistant.playerHasItem(player, 20122)
        			  && ItemAssistant.playerHasItem(player, 20123)
        			  && ItemAssistant.playerHasItem(player, 20124)) {
        		  ItemAssistant.deleteItem(player, 20121, 1);
        		  ItemAssistant.deleteItem(player, 20122, 1);
        		  ItemAssistant.deleteItem(player, 20123, 1);
        		  ItemAssistant.deleteItem(player, 20124, 1);
        		  ItemAssistant.addItem(player, 20120, 1);
        		  player.sendMessage("You combine all 4 parts of the key to make a frozen key.");
        	  } else {
        		  player.sendMessage("You must have all 4 parts of the key to assemble this.");
        	  }
        	  break;
          
          case 952:
        	  new Digging(player);
        	  break;
          
          case 6952:
        	  EssencePouch.open(player);
        	  break;
          
          case 8013:
              Teleport.startTeleport(player, 3086, 3494, 0, "tab");
          break;
          
          
          }
      }
  }