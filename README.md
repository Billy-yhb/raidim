# RaidIm  
增强袭击  
在袭击的第6波和第7波分别增加了1个和2个幻术师  
村民现在有概率会八卦村庄英雄  
加入了四个游戏规则  
* tradeInRaid  
  布尔值,玩家是否能在袭击过程中交易,默认true  
* villagerSpawnGolemInRaid  
  布尔值,村民是否能在袭击中召唤铁傀儡,默认false  
* villagerSpawnGolemCommon  
  布尔值,村民是否能在非袭击时间召唤铁傀儡,默认true  
* raidRadius  
  整数值,袭击半径,默认100  
  
村民现在判断自己是否在袭击中是判断自己离袭击中心的距离是否超过游戏规则raidRadius(单位:格),而不是原版Minecraft中写死的20格  
玩家的计算方式与村民有所不同,玩家仅仅考虑水平距离  
将来会加入新的灾厄村民------挖掘者,他将给袭击带来地形破坏能力  
  
------
这是一个fabric模组(不是forge),安装方法见fabric相关文档  
Minecraft版本要求1.14+
[fabric下载地址](https://fabricmc.net/use/)  
