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
