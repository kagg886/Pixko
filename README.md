# Pixko

Pixco是一个适用于pixiv-android的库

## 引入(未实现)

```kotlin
implementation("top.kagg886:pixko:1.0")
```

## 功能

- [x] 登录类
    - [x] 基于pixiv scheme 回调url登录
    - [x] 基于access_token的登录
- [x] 插画类
    - [x] 获取推荐插画
    - [x] 查看插画榜单
    - [x] 搜索插画
    - [x] 查看插画详情
    - [x] 收藏插画
    - [x] 评论插画
      - [x] 发布新评论
      - [x] 回复别人的评论
      - [x] 删除自己的评论
- [ ] 用户类
    - [x] 获取用户的id,昵称等个人信息
    - [x] 获取用户填写的性别等个人信息
    - [ ] 获取用户的作业环境
    - [ ] 获取用户的收藏插画