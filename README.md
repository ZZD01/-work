# RestaurantOrder
基于SpringBoot与Vue的智能营养分析点餐系统

## QuickStart
### 后端

1. 预先准备mysql，数据库名称为order
- order.sql

2. 修改配置文件数据库账号密码
`src/main/resources/application-template.yaml`

3. 运行RestaurantApplication.java

### 前端

```shell
# npm/yarn安装依赖
npm install
# 运行
npm run serve
# 编译
npm run build
```
### 访问
http://localhost:8080/

默认账号密码
1. 管理员 Peggy : 123456
2. 普通用户 Ikaros : 123456


## 功能说明-后端
### 接口文档
可以采用Swagger2,启动后访问127.0.0.1/swagger-ui.html,默认端口80

## gpt调用说明
1. 系统调用的AI大语言模型是gpt 3.5
2. 调用的是一个免费的API，来源是github上可以搜索chatanywhere，可以使用github账号免费注册
3. 额度限制：同一个ip下每天最多100次调用
4. 若使用，请将项目里面的api key换成自己的
