# KitSigns #

A simple plugin used to create signs that give out predefined items to players. 

## Configuration ##
Kits are defined branching off from the root of the YAML configuration. The key defined for the kit is the name that must be entered in the sign when creating kit signs. Each defined kit must have the following format:
```yaml
kit_name:
  helmet: <ItemStack>
  chest: <ItemStack>
  legs: <ItemStack>
  boots: <ItemStack>
  inv:
    <index>: <ItemStack>
    ...
```
`<index>` in the `inv` node corresponds to an inventory slot number (`Integer`) inside a players inventory. 

##### ItemStack #####
ItemStack definitions in the configuration must have the following format:
```yaml
node:
  type: <Material>
  amount: <Integer>
  durability: <Integer>
  enchantments:
    <Enchantment>: <Integer>
    ...
``` 
Valid materials and enchantments can be found [here](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html) and [here](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/enchantments/Enchantment.html).

## Creation ##
Once kits are defined in the configuration file and loaded onto the server (on server start), server administrators with the `kitmap.createsign` permission may create signs by adding `[Class]` on the first line of a sign, and the name of the kit on the second. The plugin will output a message and modify the sign appropriately.

## License ##
This software is available under the following licenses:

* MIT