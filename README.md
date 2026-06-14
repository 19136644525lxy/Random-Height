# 随机身高模组

一个 Minecraft Forge 模组，随机改变玩家身高。

## 功能特性

- 定期自动随机切换玩家身高
- 可配置的切换间隔时间
- 可配置的身高缩放范围
- 身高变化前的倒计时提醒
- 完整的指令系统配置

## 依赖

- Minecraft 1.20.1
- Forge 47.4.20+
- Pehkui 3.8.2+
- Cloth Config API 11.1.136+

## 指令

| 指令 | 说明 |
|------|------|
| `/rh enable <true/false>` | 启用/禁用随机身高功能 |
| `/rh interval <秒数>` | 设置切换间隔（10-3600秒） |
| `/rh scale <最小值> <最大值>` | 设置缩放范围（0.1-5.0） |
| `/rh scale countdown <true/false>` | 启用/禁用倒计时提醒 |
| `/rh scalecountdown_time <秒数>` | 设置倒计时时间（1-60秒） |
| `/rh scale force` | 强制立即切换身高 |
| `/rh scale config` | 查看当前配置 |

## 安装

1. 安装 Forge 1.20.1
2. 安装 Pehkui 模组
3. 将本模组放入 `mods` 文件夹

## 许可协议

MIT License - 查看 [LICENSE](LICENSE) 文件

## 作者

Yifei