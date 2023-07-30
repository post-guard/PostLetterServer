# PostLetterServer

北京邮电大学计算机学院2021级面向对象的程序设计（Java）课程大作业——即时聊天程序服务端。

同[PostLetter](https://github.com/post-guard/PostLetter)配合使用。

## 特点

- 因为过于赶时间而大量使用`magic number/string`反而导致`bug`频出开发效率下降（悲）
- 大量移植隔壁[PostCalendarBackend](https://github.com/post-guard/PostCalendarBackend)的代码

## 开发

使用`Springboot + Maven`开发。

### 开发IDE

- `Jetbrains IDEA`

### 配置数据库

使用`Mysql`作为后端使用的数据库，需要在`src/main/resources/application.yml`中配置数据连接字符串。

在运行程序之前需要使用`src/main/resources/database/schema.sql`初始化数据库。

## 支持

如果您在学习或者是抄袭的过程中发现了问题，我们十分欢迎您提出，您可以通过发起`issue`或者是发送电子邮件的方式联系我们。

~~连README都是复制的隔壁~~