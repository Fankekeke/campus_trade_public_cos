### 基于SpringBoot + Vue的校园二手商品交易平台.

###### 管理员：
公告管理 、用户收藏 、商品管理 、评价管理 、订单管理 、商品类型 、消息回复 、用户管理 、数据统计 、商家商品 、商家管理 、商家订单 、商家库存 、出入库详情。

###### 用户：
账户注册登录、密码修改、个人信息 、校内商铺 、订单管理 、评价管理 、我的商品 、卖出订单 、我的收藏 、在线支付 、在线联系 、商铺订单。

###### 商家：
账户注册登录、密码修改、商家信息 、我的商品 、订单管理 、商品库存 、出入库详情 、数据统计。

##### 商家入驻与商品管控
###### 商家管理/信息： 审核校园商家入驻资质，维护店铺基础档案，确保经营主体真实可靠。

###### 商品管理/类型： 规范商品分类标准，支持全校商品上架审核，实现校园物资有序管理。

###### 商家商品/我的： 商家与个人用户管理自营货架，实时编辑商品详情，展示校园特色好物。

##### 库存调度与仓储物流
###### 商品库存/详情： 实时监控商品在库数量，详细记录出入库流水，避免校园交易超卖风险。

###### 出入库详情： 追踪每一件商品的流转记录，为库存对账与物资溯源提供准确的数据支撑。

##### 交易流转与支付结算
###### 订单管理/商铺： 全程监控下单、发货及收货状态，确保校园买卖双方交易流程合规透明。

###### 卖出/商铺订单： 针对学生卖家与专业商家提供专属订单视图，提升校园二手与新货交易效率。

###### 在线支付/记录： 集成便捷的校园支付通道，实时生成财务流水，保障资金结算安全与便捷。

##### 互动评价与售后联络
###### 评价管理： 记录用户对商品与服务的真实反馈，构建校园诚信体系，导引高质量消费。

###### 在线联系/回复： 搭建即时沟通桥梁，支持买卖双方咨询议价，快速解决交易过程中的各类疑问。

###### 收藏/消息管理： 用户收藏心仪好物，系统精准推送订单状态与回复提醒，提升校园交互体验。

##### 运营决策与基础支撑
###### 数据统计： 聚合校园交易额、活跃度及热销榜单，通过可视化看板辅助平台科学运营。

###### 用户管理/公告： 维护全校师生账户安全，实时发布集市动态与交易规则，营造良好校园环境。

#### 安装环境

JAVA 环境 

Node.js环境 [https://nodejs.org/en/] 选择14.17

Yarn 打开cmd， 输入npm install -g yarn !!!必须安装完毕nodejs

Mysql 数据库 [https://blog.csdn.net/qq_40303031/article/details/88935262] 一定要把账户和密码记住

redis

Idea 编译器 [https://blog.csdn.net/weixin_44505194/article/details/104452880]

WebStorm OR VScode 编译器 [https://www.jianshu.com/p/d63b5bae9dff]

#### 采用技术及功能

后端：SpringBoot、MybatisPlus、MySQL、Redis、
前端：Vue、Apex、Antd、Axios

平台前端：vue(框架) + vuex(全局缓存) + rue-router(路由) + axios(请求插件) + apex(图表)  + antd-ui(ui组件)

平台后台：springboot(框架) + redis(缓存中间件) + shiro(权限中间件) + mybatisplus(orm) + restful风格接口 + mysql(数据库)

开发环境：windows10 or windows7 ， vscode or webstorm ， idea + lambok


#### 前台启动方式
安装所需文件 yarn install 
运行 yarn run dev

#### 默认后台账户密码
[管理员]
admin
1234qwer

#### 项目截图
暂无

|  |  |
|---------------------|---------------------|
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/4f12ebeb-3a7e-45bf-b615-480924674459.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/b0a039be-2c73-4fd7-b93a-d51361dde484.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/02db2884-30a8-40e0-9cae-f740027b0cf3.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/6189002b-bb53-4840-9cf6-9207afcac640.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/0fecb7e7-a283-45df-baf5-2d06435143db.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/94974d84-2913-45af-9535-b85a1392d456.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/0c0a9c58-a9cd-4733-b9b2-6b6c3a1b4652.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/563e7b11-59ab-47d4-8fdc-a93053561b37.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/fc54595e-4869-4d55-8f23-b8c4cdc5abed.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/365baa89-9761-4a6b-8643-22b2475e865b.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/fa0809d3-e812-4b0c-a5ca-efcc17bebdaa.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/90cdcb85-e1f2-4baf-9c27-f2fa97bd06af.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/f9625464-f05e-4b61-8104-839db4be4cbd.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/29f6246b-22b8-4b8f-8fcd-10606075961e.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/eaf9937d-0969-4b93-a50d-284369569666.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/23d99093-3ebe-44f6-b37b-b7e2e91b111d.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/da4c0a3e-ec83-4c94-a88d-a258c0ff63aa.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/8edcb625-73fb-41bd-996f-57a898431805.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/ce863021-b411-4118-a8e9-9ece88ca1bc9.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/7cdf8ec3-d6fc-4fe2-aec7-c71589b69b65.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/bfbda737-93af-4ff5-96fc-b7e91bbd109f.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/5e0d0d78-845d-4a03-87a4-981f811dec2b.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/b0cea8c0-69c3-4fef-a021-b50c8ca67fd1.png) |  |

![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/work/936e9baf53eb9a217af4f89c616dc19.png)

#### 演示视频

暂无

#### 获取方式

Email: fan1ke2ke@gmail.com

WeChat: `Storm_Berserker`

`附带部署与讲解服务，因为要恰饭资源非免费，伸手党勿扰，谢谢理解😭`

> 1.项目纯原创，不做二手贩子 2.一次购买终身有效 3.项目讲解持续到答辩结束 4.非常负责的答辩指导 5.**黑奴价格**

> 项目部署调试不好包退！功能逻辑没讲明白包退！

#### 其它资源

[2025年-答辩顺利通过-客户评价🍜](https://berserker287.github.io/2025/06/18/2025%E5%B9%B4%E7%AD%94%E8%BE%A9%E9%A1%BA%E5%88%A9%E9%80%9A%E8%BF%87/)

[2024年-答辩顺利通过-客户评价👻](https://berserker287.github.io/2024/06/06/2024%E5%B9%B4%E7%AD%94%E8%BE%A9%E9%A1%BA%E5%88%A9%E9%80%9A%E8%BF%87/)

[2023年-答辩顺利通过-客户评价🐢](https://berserker287.github.io/2023/06/14/2023%E5%B9%B4%E7%AD%94%E8%BE%A9%E9%A1%BA%E5%88%A9%E9%80%9A%E8%BF%87/)

[2022年-答辩通过率100%-客户评价🐣](https://berserker287.github.io/2022/05/25/%E9%A1%B9%E7%9B%AE%E4%BA%A4%E6%98%93%E8%AE%B0%E5%BD%95/)

[毕业答辩导师提问的高频问题](https://berserker287.github.io/2023/06/13/%E6%AF%95%E4%B8%9A%E7%AD%94%E8%BE%A9%E5%AF%BC%E5%B8%88%E6%8F%90%E9%97%AE%E7%9A%84%E9%AB%98%E9%A2%91%E9%97%AE%E9%A2%98/)

[50个高频答辩问题-技术篇](https://berserker287.github.io/2023/06/13/50%E4%B8%AA%E9%AB%98%E9%A2%91%E7%AD%94%E8%BE%A9%E9%97%AE%E9%A2%98-%E6%8A%80%E6%9C%AF%E7%AF%87/)

[计算机毕设答辩时都会问到哪些问题？](https://www.zhihu.com/question/31020988)

[计算机专业毕业答辩小tips](https://zhuanlan.zhihu.com/p/145911029)

#### 接JAVAWEB毕设，纯原创，价格公道，诚信第一

`网站建设、小程序、H5、APP、各种系统 选题+开题报告+任务书+程序定制+安装调试+项目讲解+论文+答辩PPT`

More info: [悲伤的橘子树](https://berserker287.github.io/)

<p><img align="center" src="https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/%E5%90%88%E4%BD%9C%E7%89%A9%E6%96%99%E6%A0%B7%E5%BC%8F%20(3).png" alt="fankekeke" /></p>
