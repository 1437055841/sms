## :school::mortar_board: Student Information Management System


### 项目进度 (:speech_balloon: `pause update`)
- *第一阶段 : 信息管理功能 :white_check_mark:*
- *第二阶段 : 成绩管理功能 :x:*


### 用户权限介绍
- *`管理员` : 具有所有信息管理模块的权限*
- *`教师` : 具有学生信息管理模块的所有权限，但在教师信息管理模块中只具有查询及修改个人信息的权限*
- *`学生` : 只具有查询并修改个人信息的权限*

*设置权限的核心示例代码如下( 没有使用任何安全框架 ) :*
```java
// 用户权限设置: 如果当前用户类型为教师，则将其权限设置为仅能查询个人信息
if (userType == 3) {
    TeacherInfo currentTeacherInfo = (TeacherInfo) request.getSession().getAttribute("userInfo");
	teacherInfo.setId(currentTeacherInfo.getId());
}

// 获取分页后的教师列表信息
List<TeacherInfo> teacherList = teacherDao.getTeacherList(teacherInfo, new Paging(currentPage, pageSize));
```


### 开发环境
| 工具     | 版本或描述                   |    
| -------  | --------------------------- |    
| `OS`     | Windows 10                  | 
| `JDK`    | 11.0.2                      |    
| `IDE`    | Eclipse EE 2018-12 (4.10.0) |    
| `Server` | Apache Tomcat v9.0          |    
| `MySQL`  | 8.0.11                      |

> 本项目的数据库版本为`8.0.11`，请广大版本为`5.0.0+`的同学注意咯：可通过逐个复制表结构来创建该数据库哟 ~


### 图片预览
- *用户登录页*

![](https://raw.githubusercontent.com/YUbuntu0109/Student-Information-Management-System/master/demonstration_picture/Student_Information_Management_System01-LoginInterface.PNG)

- *系统主页*

![](https://raw.githubusercontent.com/YUbuntu0109/Student-Information-Management-System/master/demonstration_picture/Student_Information_Management_System01-MainInterface.PNG)

- *学生信息管理页*

![](https://raw.githubusercontent.com/YUbuntu0109/Student-Information-Management-System/master/demonstration_picture/Student_Information_Management_System01-StudentInfoInterface.PNG)

- *数据库 ER 图*

![](https://raw.githubusercontent.com/YUbuntu0109/Student-Information-Management-System/master/demonstration_picture/sms_er.png)


### 项目结构
```
│
└─student_information_management_system
    │       
    │                                                                                       
    ├─database
    │      SMS.sql
    │
    ├─src
    │  │  databaseConfig.properties
    │  │
    │  └─pers
    │      └─huangyuhui
    │          └─sms
    │              ├─dao
    │              │      AdminDao.java
    │              │      BasicDao.java
    │              │      ClazzDao.java
    │              │      StudentDao.java
    │              │      TeacherDao.java
    │              │
    │              ├─filter
    │              │      LoginFilter.java
    │              │
    │              ├─model
    │              │      AdminInfo.java
    │              │      ClazzInfo.java
    │              │      Paging.java
    │              │      StudentInfo.java
    │              │      TeacherInfo.java
    │              │
    │              ├─servlet
    │              │      ClazzManagementServlet.java
    │              │      LoginServlet.java
    │              │      OutVerifiCodeServlet.java
    │              │      PersonalManagementServlet.java
    │              │      PhotoServlet.java
    │              │      StuManagementServlet.java
    │              │      SysMainInterfaceServlet.java
    │              │      TeacherManagementServlet.java
    │              │
    │              └─util
    │                      CreateVerifiCodeImage.java
    │                      DbConfig.java
    │                      DbUtil.java
    │                      StringUtil.java
    │
    └─WebContent
        │  index.jsp
        │  refresh.jsp
        │
        ├─easyui
        │  │
        │  ├─css       
        │  │
        │  ├─js
        │  │     
        │  └─themes
        │      
        │
        ├─h-ui
        │  │
        │  ├─css
        │  │      
        │  ├─images
        │  │
        │  ├─js
        │  │       
        │  ├─lib
        │  │
        │  └─skin
        │     
        │
        ├─META-INF
        │      MANIFEST.MF
        │
        ├─resource
        │  └─image
        │          default_portrait.jpg
        │
        └─WEB-INF
            │  web.xml
            │
            ├─lib
            │      commons-beanutils-1.8.3.jar
            │      commons-collections-3.2.1.jar
            │      commons-fileupload-1.2.1.jar
            │      commons-io-1.4.jar
            │      commons-lang-2.5.jar
            │      commons-logging-1.1.1.jar
            │      ezmorph-1.0.6.jar
            │      FilelLoad.jar
            │      json-lib-2.3-jdk15.jar
            │      jsonplugin-0.34.jar
            │      jstl.jar
            │      mysql-connector-java-8.0.11.jar
            │      standard.jar
            │
            └─view
                │  login.jsp
                │
                ├─class
                │      classList.jsp
                │
                ├─error
                │      404.jsp
                │      500.jsp
                │
                ├─management
                │      personalView.jsp
                │
                ├─student
                │      studentList.jsp
                │
                ├─system
                │      main.jsp
                │      welcome.jsp
                │
                └─teacher
                        teacherList.jsp
```


### 文件说明
1. *数据库文件*
```
SMS.sql
```

2. *数据库配置文件*
```
databaseConfig.properties
```

3. *`H-ui` 前端框架*
```
h-ui/
```

4. *`EasyUI` 前端框架*
```
easyui/
```


:clock8: *回首仔细阅读并认真思索该项目的源码，惊喜地发现该项目中的代码有许多需要优化的地方，这毕竟是我第一个`Java web`小项目，所以暂请原谅吧~ 为了让你写出更加优美的代码及更加具有可扩张性的项目，这里我给出一个非常适合初学设计模式的同学用于学习与参考的项目 ：https://github.com/YUbuntu0109/design-patterns-in-java*



*:books:更多有趣项目及详细学习笔记请前往我的个人博客哟（づ￣3￣）づ╭❤～ : https://yubuntu0109.github.io/*

*👩‍💻学习笔记已全部开源 : https://github.com/YUbuntu0109/YUbuntu0109.github.io*
 
*:coffee: Look forward to your contribution, if you need any help, please contact me~ QQ : 3083968068*
