package ionic.npc;


import ionic.npc.drops.Drop;
import ionic.object.clip.Region;
import ionic.player.Client;
import ionic.player.Player;
import ionic.player.content.combat.MultiWay;
import utility.Misc;
import network.packet.Stream;

public class NPC {

	/**
	 * True, if the NPC has been summoned by a player.
	 */
	public boolean summoned;

	/**
	 * The identity of the player that summoned this NPC.
	 */
	public int summonedBy;

	public int npcId;
	public int npcType;
	public int absX, absY;
	public int heightLevel;
	public int makeX, makeY, maxHit, defence, attack, moveX, moveY, direction,
			walkingType, combatLevel;
	public int spawnX, spawnY;
	public int viewX, viewY;

	/**
	 * attackType: 0 = melee, 1 = range, 2 = mage
	 */
	public int attackType;
	public int projectileId, endGfx, spawnedBy, summonedFor, hitDelayTimer, HP,
			MaxHP, hitDiff, animNumber, actionTimer, enemyX, enemyY;
	public boolean applyDead, isDead, needRespawn, respawns;
	public boolean walkingHome, underAttack;
	public int freezeTimer, attackTimer, killerId, killedBy, oldIndex,
			underAttackBy;
	public long lastDamageTaken;
	public boolean randomWalk;
	public boolean dirUpdateRequired;
	public boolean animUpdateRequired;
	public boolean hitUpdateRequired;
	public boolean updateRequired;
	public boolean forcedChatRequired;
	public boolean faceToUpdateRequired;
	public int firstAttacker;
	public String forcedText;
	public int disappearTime;
	public int mole = 0, moleX, moleY;
	public long lastBlock = 0;
	public boolean glacytesSpawned = false;
	public NPC[] glacytes = new NPC[3];
	public Drop drops = null;
	
	public Direction defaultRotation = Direction.SOUTH;
	
	public boolean isFamiliar = false;
	public Player owner = null;
	
	

	public NPC(int _npcId, int _npcType) {
		npcId = _npcId;
		npcType = _npcType;
		direction = -1;
		isDead = false;
		applyDead = false;
		actionTimer = 0;
		randomWalk = true;
		drops = Drop.npcDrops[_npcType];
	}

	public int getCombatLevel() {
		return NPCData.data[npcType].combatLvl;
	}

	public Client projectile = null;

	public void updateNPCMovement(Stream str) {
		if (direction == -1) {
			if (updateRequired) {
				str.writeBits(1, 1);
				str.writeBits(2, 0);
			} else {
				str.writeBits(1, 0);
			}
		} else {
			str.writeBits(1, 1);
			str.writeBits(2, 1);
			str.writeBits(3, Misc.xlateDirectionToClient[direction]);
			if (updateRequired) {
				str.writeBits(1, 1);
			} else {
				str.writeBits(1, 0);
			}
		}
	}

	/**
	 * Text update
	 **/

	public void forceChat(String text) {
		forcedText = text;
		forcedChatRequired = true;
		updateRequired = true;
	}

	/**
	 * Graphics
	 **/

	public int mask80var1 = 0;
	public int mask80var2 = 0;
	protected boolean mask80update = false;

	public void appendMask80Update(Stream str) {
		str.writeWord(mask80var1);
		str.writeDWord(mask80var2);
	}

	public void gfx100(int gfx) {
		mask80var1 = gfx;
		mask80var2 = 6553600;
		mask80update = true;
		updateRequired = true;
	}

	public int teleportDelay = -1, teleX, teleY, teleHeight;

	public void npcTeleport(int x, int y, int h) {
		needRespawn = true;
		teleX = x;
		teleY = y;
		teleHeight = h;
		updateRequired = true;
		teleportDelay = 0;
	}

	public void gfx0(int gfx) {
		mask80var1 = gfx;
		mask80var2 = 65536;
		mask80update = true;
		updateRequired = true;
	}

	public void appendAnimUpdate(Stream str) {
		str.writeWordBigEndian(animNumber);
		str.writeByte(1);
	}

	public void requestAnimation(int animId) {
		animNumber = animId;
		animUpdateRequired = true;
		updateRequired = true;
	}

	/**
	 * 
	 Face
	 * 
	 **/

	public int FocusPointX = -1, FocusPointY = -1;
	public int face = 0;

	private void appendSetFocusDestination(Stream str) {
		str.writeWordBigEndian(FocusPointX);
		str.writeWordBigEndian(FocusPointY);
	}

	public void turnNpc(int i, int j) {
		FocusPointX = 2 * i + 1;
		FocusPointY = 2 * j + 1;
		updateRequired = true;

	}

	public void appendFaceEntity(Stream str) {
		str.writeWord(face);
	}

	public void facePlayer(int player) {
		face = player + 32768;
		dirUpdateRequired = true;
		updateRequired = true;
	}

	public void appendFaceToUpdate(Stream str) {
		str.writeWordBigEndian(viewX);
		str.writeWordBigEndian(viewY);
	}

	public void appendNPCUpdateBlock(Stream str, Client c) {
		if (!updateRequired) {
			return;
		}
		int updateMask = 0;
		if (animUpdateRequired)
			updateMask |= 0x10;
		if (hitUpdateRequired2)
			updateMask |= 8;
		if (mask80update)
			updateMask |= 0x80;
		if (dirUpdateRequired)
			updateMask |= 0x20;
		if (forcedChatRequired)
			updateMask |= 1;
		if (hitUpdateRequired)
			updateMask |= 0x40;
		if (FocusPointX != -1)
			updateMask |= 4;

		str.writeByte(updateMask);

		if (animUpdateRequired)
			appendAnimUpdate(str);
		if (hitUpdateRequired2)
			appendHitUpdate2(str, c);
		if (mask80update)
			appendMask80Update(str);
		if (dirUpdateRequired)
			appendFaceEntity(str);
		if (forcedChatRequired) {
			str.writeString(forcedText);
		}
		if (hitUpdateRequired)
			appendHitUpdate(str, c);
		if (FocusPointX != -1)
			appendSetFocusDestination(str);

	}

	public void clearUpdateFlags() {
		updateRequired = false;
		forcedChatRequired = false;
		hitUpdateRequired = false;
		hitUpdateRequired2 = false;
		animUpdateRequired = false;
		dirUpdateRequired = false;
		mask80update = false;
		forcedText = null;
		moveX = 0;
		moveY = 0;
		direction = -1;
		FocusPointX = -1;
		FocusPointY = -1;
	}

	public int getNextWalkingDirection() {
		int dir;
		dir = Misc.direction(absX, absY, (absX + moveX), (absY + moveY));
		if (!Region.canMove(absX, absY, (absX + moveX), (absY + moveY),
				heightLevel, NPCSize.getSize()[npcType], 1))
			return -1;
		if (dir == -1)
			return -1;
		dir >>= 1;
		absX += moveX;
		absY += moveY;
		return dir;
	}

	public void getNextNPCMovement(int i) {
		direction = -1;
		if (NPCHandler.npcs[i].freezeTimer == 0) {
			direction = getNextWalkingDirection();
		}
	}

	public void appendHitUpdate(Stream str, Client c) {
		if (HP <= 0) {
			isDead = true;
		}
		str.writeWordA(hitDiff);
		str.writeByteS(hitMask);
		str.writeByte(hitIcon);
		str.writeWordA(HP);
		str.writeWordA(MaxHP);
	}

	public int hitDiff2 = 0;
	public boolean hitUpdateRequired2 = false;
	public int hitIcon, hitMask, hitIcon2, hitMask2;

	public boolean isPestControlNPC = false;
	public boolean isPest = false;
	public int knightHit = 0;

	public void appendHitUpdate2(Stream str, Client c) {
		if (HP <= 0) {
			isDead = true;
		}
		str.writeWordA(hitDiff2);
		str.writeByteC(hitMask2);
		str.writeByte(hitIcon2);
		str.writeWordA(HP);
		str.writeWordA(MaxHP);
	}

	public void handleHitMask(int damage) {
		if (!hitUpdateRequired) {
			hitUpdateRequired = true;
			hitDiff = damage;
		} else if (!hitUpdateRequired2) {
			hitUpdateRequired2 = true;
			hitDiff2 = damage;
		}
		updateRequired = true;
	}

	public int getX() {
		return absX;
	}

	public int getY() {
		return absY;
	}

	public boolean inMulti() {
		return MultiWay.inMulti(absX, absY);
	}

	public boolean inBarbDef() {
		return (coordsCheck(3147, 3193, 9737, 9778));
	}

	public boolean coordsCheck(int X1, int X2, int Y1, int Y2) {
		return absX >= X1 && absX <= X2 && absY >= Y1 && absY <= Y2;
	}

	public boolean inWild() {
		if (absX > 2941 && absX < 3392 && absY > 3518 && absY < 3966
				|| absX > 2941 && absX < 3392 && absY > 9918 && absY < 10366) {
			return true;
		}
		return false;
	}


}