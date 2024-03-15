RuneCraftory 1.9.1
================
- Fix taking small damage triggering level up sound
- Minimum damage to gain defence xp is > 1
- Remove skill datapack syncing. All level stuff is now done completely on the server
- Decrease default gate hp gain

RuneCraftory 1.9.0
================
- Added armor effects system:
  You can specify additional effects for armor pieces.  
  These effects can also be transfered to other pieces during crafting similar to adding spells to staffs
- Rewrote NPC conversation type to use a registry based system making it more expandable
- Implementing romancing npcs. Giving a npc a love letter with 7 >= friend points has a chance of advancing relationship.  
  Giving a npc an engagement ring allows you to marry it if you are dating and all their quests requirements are met.
  These npcs cannot die.
- Make npc combat system use the same as the players which fixes e.g. rune abilities not working right on them
- Rewrite the npc look feature system to use a registry based one. Also added a size feature.
- Added plant sword/shield:
  These items can be grown as crops and will be applied random item upgrades on harvest
- Added zoning config for distance based level increase. Previously that was hardcoded.
- Added player predicate option to gate and boss spawning
- Boss spawner now have requirements to meet before being able to enter
- Updated lots of advancement and introduced some progression system
- Changed requirement of farmland needing to be wet to grow crops. Now if its not wet the growth speed gets a 0.5 penalty
- Renamed saved data file
- Adjusted monster stats
- Updated the guide book
- Fix weagle idle animation not working
- Fix upgrade tooltip on items sometimes not shown
- Fix item stats using tags not working
- "Fixed" skelefang particle with sodium/iris

RuneCraftory 1.8.4
================
- Add a screen for spawn eggs instead of renaming it to set a level
- Add sounds to various things
- Split datapack packets into multiple ones
- Change naive blade skill to not get triggered by non entity attacks
- Tweak hp of monsters
- Fix crash with thunderbolt
- Fix negative multiplier with monster stats
- Fix some animations

RuneCraftory 1.8.3
================
- Sync max skill level to client. 
  Fixes bug where skill level was not updated correctly there
- Fix food buffs not reset when using reset command
- Change stats to round to nearest 0.5 instead of full integer
- Removed `disableDefence` and `vanillaIgnoreDefence` and replaced it with
  `defenceSystem` config which allows a bit more possibilities

RuneCraftory 1.8.2
================
- Changing some default config values
- Fix raccoon attack

RuneCraftory 1.8.1
================
- Increase herb generation rate
- Add missing effect icon for blitz and steel heart
- Minor tweaks to boss stats
- Fix rafflesia summons not being same level as it
- Fix npc spawning on leaves

RuneCraftory 1.8.0
================
- Breaking Change!!!
- Added fruit trees:  
  Fruit trees behave similarly to crops.  
  You need to water them everyday and they take a long time to grow.  
  When mature they will drop fruits every day
- Added giant crops:
  Plant crops in a 2x2 and apply giantizer to it.
  After a while they will merge together into a giant version
- Renamed lots of items/blocks (crops, seeds) to reflect the translated name better.
  **This breaks every previous world** (or rather all affected items/blocks in world will vanish)!
- Add npc spawning mechanism:
  NPC will spawn in villages if there are only few nearby
- Add some more npcs and quest
- Add all rune abilities
- Add random npc names and (some) textures
- Taking damage during sleep effect wakes you up
- Config added to allow movement during weapon attacks.
  - Animation will look out of place though as its not made for that
- Add spells and rune abilities to various vanilla chest loots
- Added Jade and TheOneProbe compat
- Mineral blocks mine slower with regular pickaxes
- Increase weapon range and aoe
- Tweaked weapon stats
- Change rafflesia attack and fix it not hitting when too close
- Increase cooldown of boss attacks
- Tweak monster knockback
- Water spells now can be held to cast it longer
- Fix npc data not persistent on load
- Fix player animation sync
- Fix "furniture" attack texture from marionetta
- Fix parent quest for npc quests
- Fix shop item on reload gone

RuneCraftory 1.7.0
================
- Reworked combat: All weapons from this mod now have attack animations
- Added ultimate attack for weapons. Unlocked with level 20 of their respective skill
- Add `levelIncreaseOnKill` mob properties: When killed the base level of gates increase by this value
- Vanilla hoes use up RP now
- Make sleeping cure fatigue
- Add party based loot & luck system
- Rework npc dialogues:
  - Custom gui
  - Allows answer selection
- Add dynamic configurable npc attack patterns
- Implement Questsystem fully:
  - Allows for Questboard based quests
  - Allows for NPC based quests
- Make standing still able to slowly regen RP
- Reduced walking xp gain
- Add configurable system for riding actions to consume RP.
- Change treasure chests to use loottables instead of tags
- Add sand fishing (currently only fishes up sand though)
- Rework some weapon textures
- Fix gates not spawning underwater
- Fix knocked down mobs still getting targeted
- Fix dead monster not resetting barn capacity
- Fix right click harvesting arm swing

RuneCraftory 1.6.2
================
- Update jei version and compat
- Reduce poison needle poison chance
- Spawning min level is now ignored if gate level config is set to CONSTANT
- Add missing effect icons
- Fix and improve various mob ai and attacks

RuneCraftory 1.6.1
================
- Fix targeting owned entities being incorrect
- Hide locked recipes in jei and rei
- Fix rp bar not aligned in crafting gui
- Tweak max monster around gate
- Tweak gate spawnrate. Now defined via MaxSpawnDelay and MinSpawnDelay
- Add indicator for when monster can't be hurt
- Add random add tick for movement animation so not all mobs have the same
- Moved skill and weapon config to datapack

RuneCraftory 1.6.0
================
- Dragon scales providing bonus shield stats (only up to 50%) when used in upgrades
- Added lots of monsters
- Added bosses:
  - Chimera, Dead Tree, Raccoon, Skelefang, Rafflesia
- Bosses heal rapidly if no nearby players
- Bosses heal to full if they manage to kill all nearby players
- Mob stats etc. are now datapack and not config based except a few things
- New drop chance calculation that incorporates looting and luck. Looting is now multiplicative while luck is additive
- Add missing eathspike spell
- Spell stuff (like cooldown etc) are now configurable via datapack
- Fixes:
  - Redone a few textures
  - Fix monster doing nothing when ridden by non players
  - Make ridden mobs model fit to animations
  - Fix untaming entities in parties not removing them
  - Make untaming entities remove from parties
  - Tweak damage calculations
  - Gate spawning config moved to datapack
  - Fix (non player) ridden monsters not attacking
  - Fix random move ai sometimes crashing
  - Improve targeting for mobs with owner. Check owner directly instead of just uuid

RuneCraftory 1.5.1
================
- Removed some unused textures
- Fix multiblock highlight being wrong when aiming at replaceable blocks like grass
- Farmland check sky for rain
- Doctors can cure negative effects

RuneCraftory 1.5.0
================
- Due to renaming of attributes you need to delete your mob config and let it regenerate. Else all attributes of the mobs will not get applied
- Added cash register block serving as a generic shop POI
- Added barn block entity. Monster now need a barn block assigned. Existing tamed monster will not do anything till they get a barn assigned
- Add bath effect: Gotten by talking to a bathhouse attendant. Spawns in bathhouses.
  If you have the effect and go into water with campfires below you regen health + rp.
- Cooking now accepts additional items
- Add weapon changing mechanic
- Invis stone makes weapon invisible when used in upgrades
- Add a party system: Only a max amount of entities can follow you. default 3
- Add quest board. Needs simplequests mod
- Add bath house and magic/runeskill npc house
- Can define npc schedule per datapack. if not defined creates a random one
- Change npc shop types from enum to a registry
- Added tooltips for some special upgrades
- Fix various monster attacks not working when ridden
- Attack speed and range is now an attribute instead of config based
- NPC now select 2-3 lines only
- Tweaked various stats
- Cook npc now provide recipe breads
- Fix spam using bed increasing xp
- Custom farmland block removed: Now uses a global system for farmland.
  Unloaded farmland now tick etc.
- Removed rp cost for weapon charge attack
- Add min level config for monsters and npc
- Max skill level now configurable for individual skills
- NPC stats now definable for individual npcs

RuneCraftory 1.4.4
================
- Changed how some stuff is updated. (e.g. Weather)
- Removed bonus rolls (aka luck attribute bonuses) from loot tables
- Make milk conversion recipe require bucket
- Fix monster interaction particles not always shown
- Monsters can now clear farmland of herbs growing on them
- Fix monster retaliating when attacked by owner on accident
- Fix npc follow issue
- Make npc reset POI when too far away or can't reach
- Make mobs that hold weapons able to use them
- Make skill leveling when hitting entities tag based instead of item based
- All fertilizer apply in a 2x2 now

RuneCraftory 1.4.3
================
- Fix bonus item increasing the item level
- Money from defeating monsters now not scaled on level anymore
- Fix attribute tooltip
- Some attributes will not get used when upgrading items:  
  Weapon/Tools: Will not get resistance (e.g. sleep resistance) increased  
  Armor: Will not get status effect (e.g. sleep effect chance) chance increased
- Reduced storm chances in summer/winter
- Fix spell inventory dropping things if keepInventory is true
- Make the attack in the stats screen red if the player doesn't have a weapon equipped
- Tweaked mob loot tables
- Sheared woolys dont drop wool

RuneCraftory 1.4.2
================
- Tags
- Fix mixin crash on fabric
- Monster daily drops changed to loot tables

RuneCraftory 1.4.1
================
- Fix crash with thrown item
- Fix crash with picking up hp runeys
- Fix meltable snow not having correct tags
- Hide the tooltip from apotheosis and quark for weapons/armor from this mod
- Fix a nullpointer with loading old npcs
- Fix wrong default npc lang key 
- Fix default items not added to shop
- Add condition to npc conversations
- Fix snow in biomes with no downfall
- Add npc birthday
- Base xp config for skill removed. Instead replaced with skill xp multiplier
- Added more tags

RuneCraftory 1.4.0
================
- Add seasonal snow. If it gets cold enough it will snow in biomes where it will normally not.
- Various mob attack rewritten to use spell system. So they can be used with staff upgrades
- Fix default element not saved when upgrading item
- Player get a notification when their tamed monster gets knocked out
- Lots of damaging stuff that didnt apply several status effects now do it properly
- Made bosses resistant against a lot of status effects
- Crafting system got rewritten:  
  You can now craft items you havent unlocked at a higher rp cost (You can disable it in the config if you want).  
  During crafting you can add up to 3 additional items. These items are applied the same as if you upgrade
  the item directly but without an duplicate item penalty.  
  Gui for crafting got redesigned to make choosing recipes easier.  
  Crafting an item without a valid recipe now yields something different
- Fix biome tags
- Disable spell keys when gui is open
- Runeys and rune orbs can now spawn when harvesting fully grown crops. They will refresh your runepoints and level a random skill
  or a random stat
- Config value for "Recipe System" overwritten. 
- NPC now seek shelter when its raining
- Add some advancements
- Decreased xp for level up
- Made bosses immune against heal reduction from food
- Fix taming/untaming monster not resetting target properly and attacking things they shouldn't
- Scrap metal + upgrade now does fixed 1 damage
- Tweaked monster stats
- Taming item not config but tag based now. The tag its checking is "runecraftory:taming/entity_id"
- Add more stats and shop items
- Add datapack based NPC interaction. Can define dialogues, gifts, looks etc.
- Stat system now use vanilla attribute system instead of a separate one.  
  Makes it so stats are now applied to mobs too instead of only the player
- Changed the process of giving items to mobs. You now need to throw the item at them.  
  Also if you are walking or moving faster and drop an item that item will get launched a bit further than normal
- Brushing tamed monster increases a random stat by 1 once a day
- Monster with higher friend points gain a stat boost

RuneCraftory 1.3.5
================
- Fix player data not saved when travelling from the end
- Also fix data not syncing to client when changing dimensions
- Fix default element not saved when upgrading item
- Make carried item not reset when opening the extended inventory menu

RuneCraftory 1.3.4
================
- Fix a crash using cross dimension teleport sometimes
- Also made teleport spell teleport nearby monster/npc that currently follow you
- Fix nether herbs generating too far from restriction
- Monster loose hp when during farming chores. They will stop doing chores if hp drops below 10%.  
  They also gain a tiny bit of xp when farming.

RuneCraftory 1.3.3
================
- Add config if monster should attack npc
- Walking xp reduced
- Textures and models
- Make golems not attack tamed monster
- Improved tamed monster and npc following you
- Improved tamed monster interaction
- Add right click animation system. Gloves and spears missing atm
- Make ghost and spirit attack better
- Fix NPC despawning
- Empty farmland can grow herbs

RuneCraftory 1.3.2
================
- Add "vanilla ignore defence" config
- Make explosion spell hit shake screen
- Tweaked weather chances
- Internal: NBT stuff rewritten. Datapack stuff uses Codecs
- Make goblin and orcs not drop their weapons on death
- Taming chance now considers taming skill too. Also add option to increase it further with love weapons
- Increase monster xp gain by a lot. Since it never got changed when the xp calculation was changed  
  You need to delete the mob config file if you want this change
- Add clinic structure

RuneCraftory 1.3.1
================
- Smoother movement animation
- Add search skill. Leveled by opening loot chests and treasure chests
- Decreased spawnrate of thunderbolt ruins
- Increased chance of minerals breaking
- Decreased rain chance.  
  Also detects if incorrect vanilla weather is running and fixes it
- Updating some default config values
- Fix arms not swinging for mob interaction
- Bells blocking gates radius now configurable
- Add minimum gate level check for spawn config
- Fix knocked out flying mobs staying in midair
- Flying mobs a bit faster when ridden
- Item-, food- and cropstats now show their id with advanced tooltip
- Fix crash on fabric
- Recipes are now sorted according to crafting level on REI and JEI
- Mobs and NPC that follow you now get xp when defeating monsters

RuneCraftory 1.3.0
================
- Enabled crop features now by default cause textures are now (mostly) there
- Brushing non tamed monster increase taming chance with 1.5x chance at 10 brushes.
  Brushing or feeding tamed monster increases friend points once per day
- Add fishing mechanic
- Teleport spell added. Teleports player to their spawn point if they arent in the same dimension or further than 10 blocks away else teleports to world spawn
- Tamed monster now get knocked down instead of killed
- Tamed monster can help with farming
- Add functionality to magnifying glass and inspector
- Added NPC which currently spawn in vanilla villages.
  They can spawn with the possibility of having a shop.  
  NPC need a bed and a workstation not too far away from each other for the 
  player to buy stuff and the shops are only open during certain times of the day.
  Currently no skins (aka they just have the default steve skin)
- Fix penetrating wind blades bounce sometimes not correct
- Ignore height for distance level calculation
- Smooth out some animations
- Seal effect now implemented: Sealed entities cant use magic
- Runey weather now display correctly
- Added command to change weather (uses this mods weather system)
- Added missing recipe for guide book
- Storms now destroy crops
- Make ghost type mobs not phase through walls when ridden else players can suffocate
- Make mimics not solid anymore when they are not hidden

RuneCraftory 1.2.4
================
- Increased some base xp values
- Bosses dont despawn now
- Spear charge dmg increased
- Fix problem with ambrosias wave attack
- Add RP bars to crafting screens 

RuneCraftory 1.2.3
================
- Readjusted and rewritten rp usages
- Mining minerals now cost rp
- Add "Heal On Wake" config. Previously that logic was together with "Modify Bed"
- Tools now shift the center of the action to the block the player is looking at instead of just using the players position
- Fix non food item not getting used up during taming
- Fix tamed monster sometimes going after other tamed monsters
- Changed how the amount of xp per mob is calculated
- Fabric: Fix some packets not properly send
- Sleep effect now causes entities to lay down
- Disable sprinting if paralysed
- Added paralysis and lightning particles
- JEI: can browse recipes from inside crafting devices now
- REI: Support for quick crafting (that + button thingy)
- Now shows the crafting level of recipes in both JEI and REI
- Material show upgrade difficulty now
- Fix when dying with shift mass crafting not taking account the death
- Fix crafting tables not properly saving their inventory
- Added stats to currently dropped items
- Tweaked food stats

RuneCraftory 1.2.2
================
- Make bronze mineral drop copper instead of bronze cause more logical and balanced  
- Make sleep effect now actually affect players by freezing them
- Grass and foliage now change colors according to current season.
- Adjusting loot tables and recipes
- Fix fireballs not spawnable
- Rewritten commands
- Fix monster interaction being broken
- Add daily drops for mobs
- Gates now dont spawn near meeting POI (e.g. bell blocks) in a 48 radius
- Fix dying mobs still attacking
- Make owned monster immune to dmg from their owners unless the player is sneaking
- Make treasure chest only drop equipment the player can craft with their current level + 3
- Fix reset player data command corrupting player
- Split render overlay config into calender, hp bar and food bar
- (Nearly) all skills are now properly tracked. Fishing, bathing and leadership still WIP
- Gate level now adjustable and not just on level 1. See mob config
- Lots of other bugfixes and balancing changes

RuneCraftory 1.2.1
================
- Better structure placement
- Fix mobs trying to attack creative players
- Fix sound playing during attacks when it shouldn't
- Fix chest trap from marionetta doing no dmg
- Treasure chest interaction fix
- Make marionetta jump around more
- Fix crash with butterflys
- Fix mobs floating when they playing defeated animation

RuneCraftory 1.2.0
================
- Added: spider, shadow panther, monster box, gobble box
- All magic skills are now functional
- Boss: Marionetta. Spawns in theater ruins
- Added treasure chest. Has a chance of spawning when a gate spawns.  
  Right clicking it opens it while left clicking/attacking just destroys it
  Drops a pletora of loot when opened including currently only this way obtainable items
- Fix stat boosting item giving +10 instead of +1
- Fix stat boost from items not getting saved
- Balancing Changes: tweaked mob stats, magic dmg etc.

RuneCraftory 1.1.0
================
- Added: duck, fairy, ghost, spirit, ghost ray
- Added spells: cure, cureall, master cure, light barrier, shine, prism dark ball, dark snake
- Fixed mobs getting equipment when they shouldn't have

RuneCraftory 1.0.5
================
- Fix shipping bin being broken
- Forge: Fixing problems with capability loading on player clone
- Crops from this mod can now wilt if not watered.  
  After wilting there is a chance the crop will, unless watered, turn into withered grass

RuneCraftory 1.0.4
================
- Fix crafting block models gone missing
- Decrease saturation of food

RuneCraftory 1.0.3
================
- Add more configs for gate spawning
- Make not tamed monster able to despawn
- Add lots of missing textures
- Fabric: Fix modmenu saving not getting applied
- Implement crop loot modifier on fabric
- Fix vanilla crops not plantable on custom farmland
- Item rarity
- Add recipes for some items
- Some more features are now enabled by default

RuneCraftory 1.0.2
================
- Add missing crop data
- Split disableDatack into disable(Food/ItemStat/Crop)
- Fix no generation in the end

RuneCraftory 1.0.1
================
- Add a patchouli guidebook (WIP)
- Fix a nullpointer with thunderbolt
- Move dmg randomizer to a later point

RuneCraftory 1.0.0
================
- 1.18 and release
