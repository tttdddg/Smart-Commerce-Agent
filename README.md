## Smart Commerce Agent
### 项目简介
Smart Commerce Agent是基于**开源电商项目 mall（https://github.com/macrozheng/mall）**
深度改造的AI电商客服与导购Agent系统，在原有电商业务基础上，接入大模型与Agent能力，实现智能客服、商品咨询、订单查询、多轮导购等功能，打造更智能的电商交互体验。
### 项目背景
原mall项目提供了完整的电商后台管理 + 前台商城体系，包含商品、订单、会员、营销等核心模块。本项目在其基础上：
保留mall原有商品、订单、用户、库存等业务数据与接口
新增AI Agent对话中枢，实现自然语言交互
基于LangChain4j+Tool Calling动态调用电商业务能力
支持多轮对话、上下文管理、结构化输出、异常兜底
### 技术栈
#### 后端核心
Spring Boot
Spring Security
MyBatis
MySQL
Redis
#### AI Agent 增强
LangChain4j
DeepSeek API
Tool Calling
Prompt Engineering
上下文管理/Session缓存
结构化输出与结果校验
#### 基础架构
Docker
Maven
JWT
Apifox
### 功能模块
1. 基础电商能力（继承mall）
商品管理、商品搜索、SKU管理
订单管理、订单查询、物流状态
会员管理、权限控制、促销管理
后台管理接口mall-admin
前台商城接口mall-portal
2. AI Agent增强功能（本项目新增）
自然语言商品咨询、价格/库存/规格查询
自然语言订单查询：订单号、手机号、状态查询
多轮对话导购：上下文理解、偏好推荐、引导转化
Tool Calling：动态调用商品/订单/用户接口
会话管理：Redis缓存多轮对话状态
结构化输出：稳定可解析，便于前端渲染
异常兜底：模型异常/接口失败友好处理
### 改造亮点
低侵入：不破坏mall原有架构，以新增模块接入AI
强复用：直接使用mall成熟商品、订单、用户接口
易扩展：新增Tool即可快速支持售后、退款、物流查询
高稳定：上下文管理 + 校验兜底，适合生产环境
可演示：开箱即用，适合面试、毕设、产品演示
