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
新的灾厄村民挖掘者现在加入游戏,他生成在第四波和第七波,这将是一场噩梦  
他能投掷挖掘蛋
挖掘蛋能破坏地形,这意味着你如果你不能快速解决他,那你的防御工事很可能被他破坏掉  
挖掘蛋仅对铁傀儡,雪傀儡,凋灵有伤害,伤害为困难时100点(意味着一击杀死满血铁傀儡),其他难度90点,对其他生物无伤,所以不要妄想铁傀儡会来救你  
  
------
这是一个fabric模组(不是forge),安装方法见fabric相关文档  
Minecraft版本要求1.14+  
fabric loader+fabric api是运行fabric模组所需的最小环境,你需要至少安装这两个mod  
[fabric loader下载地址](https://fabricmc.net/use/)  
[fabric api下载地址](https://minecraft.curseforge.com/projects/fabric/files)  
fabric api的下载页面由于引用了谷歌的页面在国内访问很慢,你可以点击[这里](https://minecraft.curseforge.com/projects/fabric/files/latest),下载fabric api的jar文件,放进Minecraft运行目录下的mods文件夹中即可完成fabric api的安装