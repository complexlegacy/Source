package ionic.player.content.consumable;

import ionic.item.ItemAssistant;
import ionic.player.Client;

import java.util.HashMap;

import utility.Misc;

/**
 * @author Sanity
 */

public class Food
{

        public static enum FoodToEat
        {
                PURPLE_SWEETS(10476, Misc.random(2) + 1, "Purple sweets"),
                        SHRIMP(315, 3, "Shrimp"),
                        TROUT(333, 7, "Trout"),
                        SALMON(329, 9, "Salmon"),
                        TUNA(361, 10, "Tuna"),
                        LOBSTER(379, 12, "Lobster"),
                        SWORDFISH(373, 14, "Swordfish"),
                        MONKFISH(7946, 16, "Monkfish"),
                        SHARK(385, 20, "Shark"),
                        SEA_TURTLE(397, 22, "Sea turtle"),
                        TUNA_POTATO(7060, 22, "Tuna potato"),
                        MANTA(391, 22, "Manta ray"),
                        ROCKTAIL(15272, 23, "Rocktail"),
                		BANDAGES(4049, -1, "Bandages");
                private int id;
                private int heal;
                private String name;

                private FoodToEat(int id, int heal, String name)
                {
                        this.id = id;
                        this.heal = heal;
                        this.name = name;
                }

                public int getId()
                {
                        return id;
                }

                public int getHeal(Client c) {
                	int h = heal;
                	if (h == -1)
                		h = 1 + c.calculateMaxLifePoints() / 10;
                    return h;
                }

                public String getName()
                {
                        return name;
                }
                public static HashMap < Integer, FoodToEat > food = new HashMap < Integer, FoodToEat > ();

                public static FoodToEat forId(int id)
                {
                        return food.get(id);
                }

                static
                {
                        for (FoodToEat f: FoodToEat.values())
                                food.put(f.getId(), f);
                }
        }

        public static void eat(Client player, int id, int slot)
        {
                if (player.duelRule[6])
                {
                        player.sendMessage("You may not eat in this duel.");
                        return;
                }
                if (player.skillLevel[3] == 0)
                {
                        player.sendMessage("You are unable to eat whilst dead.");
                        return;
                }
                if (player.isTeleporting() || player.doingAction(false))
                {
                        return;
                }
                if (System.currentTimeMillis() - player.foodDelay >= 1500)
                {
                	if (player.resting) {
            			player.getPA().stopResting();
            		}
                        player.getCombat().resetPlayerAttack();
                        player.attackTimer += 2;
                        player.startAnimation(829);
                        ItemAssistant.deleteItem(player, id, slot, 1);
                        FoodToEat f = FoodToEat.food.get(id);
                        if (player.skillLevel[3] < (f.getId() == 15272 ? (player.maximumHitPoints() * 1.1) : player.maximumHitPoints()))
                        {
                                player.skillLevel[3] += f.getHeal(player);
                                if (player.skillLevel[3] > (f.getId() == 15272 ? (player.maximumHitPoints() * 1.1) : player.maximumHitPoints()))
                                {
                                        player.skillLevel[3] = (int)(f.getId() == 15272 ? (player.maximumHitPoints() * 1.1) : player.maximumHitPoints());
                                }
                                player.getPA().refreshSkill(3);
                        }
                        player.foodDelay = System.currentTimeMillis();
                        String message = "You eat the " + f.getName() + ".";
                        if (f == FoodToEat.BANDAGES)
                        	message = "You use the " + f.getName() + " to heal yourself.";
                        player.sendMessage(message);
                        player.foodEverAte++;
                }
        }


        public static boolean isFood(int id)
        {
                return FoodToEat.food.containsKey(id);
        }


}