# 开始使用

还在用VexView自带的那套难以编辑的系统吗?
我们提供了一套更好的配置系统.
你可以把您自己的GUI写一个文件里面
(提前说明: 因为VexView按钮判断机制所以按钮的操作得写在另外的文件)

以下是我们的示例文件

```text
# karlatemp:example-gui
size 400 200;
background "[local]login.png";
```
这样我们就能得到一个最简单的GUI了!

## 文件路径
我们使用了与MC原版路径差不多的方式: 命名空间路径.
```text
karlatemp:example-gui

[namespace]:[path]
```
对于 \[namespace\], 我们有以下要求: 全小写或者下滑符

对于 \[path\], 我们有以下要求
- 全小写
- 用 / 作为文件目录分割
- 不能包含连续的两个点号 `..`
- 没有文件名后缀

对于 `karlatemp:example-gui`, 他的实际路径是
`plugins/Vit/karlatemp/example-gui.txt`

对于 `karlatemp:gui/example`, 他的实际路径是
`plugins/Vit/karlatemp/gui/example.txt`

我们使用此机制的原因是为了更好的编辑和存储.

## 文件语法
啊哈, 严格来说这个并没有什么语法, 也就只是一些格式,
写入每一行命令以告诉解释器应该做什么, 对于Vit文件,
我们做了一下规定

```text
# Commit
对于以 # 开头的行, 实际运行中会忽略, 但是请不要把它塞在命令中间
    比如 command # Commit
                Argument;
因为这可能导致解释器把这个注释也读进去了

{}
这是一个空的命令块, 通常用于参数传值使用.
 Eg: {
    size 233 233;
    background "[local]button.png" "[local]button_.png";
 };

printf "Hello? Can you hear me?" {
    color red;
    scale 50；
};
这是一句命令, 以分号结束. 参数可以是命令快.

注意!
关于连续命令:
    除了最后一条命令, 命令的末尾必须以 ; 结束, 否则我们是不认的
    比如
        cmd1 {}
        cmd2 {}
    她和
        cmd1 {} cmd2 {}
    是一样的! 没有分号我们会把她们当做一行处理

不要在文件开头/结尾加上 {、}, 因为一个命令文件本身就是一个块!
```

## Gui
```text
# Type of GUI

# The size of gui. Must integer.
# GUI 的大小. width 和 height 必须是整数
size <width> <height>;

# GUI background address.
# GUI 背景地址.
# Eg: background "[local]login.png";
background <image address>;

# Insert a image, can be duplicate.
# 插入一个图片, 可以重复
# Eg: image {
#   size 20 20;
#   background "[local]login.png";
#   image_size 20 20;
#   size 20 20;
#   move 0 60;
# };
image [ImageType];

# Insert a text, can be duplicate.
# 插入一个图片, 可以重复
# Eg: text {text "FAQ"; move 60 0; scale 5};
text [TextType];


```
@see [Image Type](#Image)

@see [Text Type](#Text)

@see [Slot Type](#Slot)

@see [Button Type](#Button)

## Image
```text
# Type of Image.

# The address of this image.
# 图片位置
# Eg: background "[local]login.png";
background <address>;

# Image Rending size.
# 图片显示大小
# Eg: size 20 20;
size <width> <height>;

# Image Source Size
# 图片原始大小
# Eg: image_size 233 233;
image_size <width> <height>;

# Move this image
# 移动
move <x> <y>;

# The showing text when point move to image
# 当鼠标移动到图片上后显示的文字
# Eg: hover {line "§cWho are you?"};
hover [TextType];
```
@see [Text Type](#Text)

## Text
```text
# The type of text

# Insert line(s) to TextObject
# 追加更多行
# Eg: line
#        "Line1"
#        "Line2"
#        "Line3"
#        <OrMore>;
line [line] ...;

# Move this text
# 移动
# Eg: move 10 0;
move <x> <y>;

# The level of scale.
# 缩放系数
# Eg: scale -1;
scale <scale>;

# The showing text when point move to this text
# 当鼠标移动到文本上后显示的文字(禁止套娃)
# Eg: hover {line "Nya? Nya?"};
hover [TextType];
```

## Slot
```text
# The type of slot.

# ...
move <x> <y>;

# The item of this slot.
# 这个slot的物品
# Eg: item {material DIAMOND;};
item [Item Type];
```
@see [ItemType](#Item);

## Item
```text
# Type of item.

# The material of item
# 物品材质
# @see https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html
# Eg: material DIAMOND;
material <material>;

# The amount of item
# 物品数量
amount <amount>;

# Item Durability
# 物品耐久
# @Deprecated 已过时
durability <durability>;

# Enchantment
# 附魔
# @see https://hub.spigotmc.org/javadocs/spigot/org/bukkit/enchantments/Enchantment.html
# Eg: enchant DAMAGE_ALL 50;
enchant <name> <level>;
```
@see [Materials](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html)

@see [Enchantment](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/enchantments/Enchantment.html)

## Button
```text
# The type of button.

# Button size
# 按钮大小
size <width> <height>;

# ?
move <x> <y>;

# The text of button.
# 按钮文字
# Eg: text "What are you doing?";
text <text>;

# The unique id of this button.
# 按钮ID, 不要重复, 否则可能出大问题
# Eg: id "vit:karlatemp:gui/example/button-1";
id <unique id>;

# The handle of this button.
# 按钮点击回调
# Is PATH not COMMAND!
# 是路径而不是命令!
# Eg: handle "karlatemp/gui/example/button-handle-1";
handle <path>;
```

## Button Handle
```text
# The type of handle.

# The message send back when click don't have permission.
# 点击者没有权限时显示的信息
no-perm-msg <message>;

# The permission for perform handle.
# 运行此Handle所需要的权限
perm <permission>;

# Clicking button will close GUI when this option exists.
# It will make `open` invalid.
# 当此选择存在时, 点击会关闭gui并使得open无效
close;

# Resolve path
# 重新打开的gui路径
open <target gui path>;

# The permission given when invoking commands.
# Will cancel temp permission in end.
# 在运行handle时给予的临时权限
# 会在执行完毕后取消
# Eg: add_perm "*";
add_perm <permission>;

# The commands invoking
# 执行的一系列命令
# Eg:
#   cmd "say 已经没有什么好怕的了?"
#       "suicide";
#
#   cmd "say Hello World!";
cmd <command1> <command2> ...;
```