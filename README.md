# Cloaker

Cloaker是一个使用Java编写的可以本地运行的文章查重开发工具，若您有论文或其他文档查重的需求，就可以使用本工具进行简单的查重自测。使用本工具进行查重或开发，您可以获得您的论文与文献库中文档的总体相似度、论文中与文献库相似度较高的语句、被您的文章所引用的文章和语句等信息，它们会被显示在一个HTML页面中。

使用者可以自定义查重文章和文献库目录、相似度阈值、输出文件路径，也可以编写自定义的算法/过滤器对文章进行检测从而获得自己想要的特定数据和输出样式。

要注意的是，所有文章（待查重文章和文献库文章）都需要保存为txt文件用文章题目作为文件名，并且要将文件通过无BOM的UTF-8编码保存才能正常使用。所有文献库中的文章必须保存在启动参数指定的、或者代码中指定的参数文献库目录下。

## 名词解释

1. **Cloaker控制器** —— 位于cn.eviadc.cloaker.Cloaker，控制整个Cloaker程序流程的类，其中的analysis方法实现了程序的主要功能。运行analysis方法相当于运行整个程序。
1. **PaperAnalyzer接口/模块接口** —— 位于cn.eviadc.cloaker.analyzer.PaperAnalyzer，所有模块和过滤器都需要添加的接口，主要用于模块对文章或分句的操作和模块之间的相互调用，其中的Analyze方法用于模块和过滤器实现它们的主要功能。
1. **PaperFilter类/过滤器父类** —— 位于cn.eviadc.cloaker.analyzer.filter.PaperFilter，过滤器的父类，添加了PaperAnalyzer接口，自定义的过滤器类一定要继承此类。
1. **模块** —— 实例化后传入Article或Entry对象analyze方法、对Article或Entry对象内容进行操作的添加了PaperAnalyzer接口的类。其主要功能被实现在接口的Analyze方法中。
1. **Article/文章/文章对象** —— 位于cn.eviadc.cloaker.Article，用于存储一篇文章的类，实例化后在程序中相当于一篇文章。其analyze方法用于接受模块并调用模块的Analyze方法来实现模块对其的操作。
1. **Entry/分句/分句对象** —— 位于cn.eviadc.cloaker.Entry，类似于Article，但它代表的是文章中的一条分句。也具有analyze方法，功能类似于Article的analyze方法。
1. **过滤器** —— 实例化后用于对文章进行分析、实现主要查重算法的类，它继承了PaperFilter类。

*注：Article的analyze方法和cn.eviadc.cloaker.analyzer.PaperAnalyzer接口的Analyze方法大小写有区别。*

## 运行示例

API使用示例可以参考cn.eviadc.cloaker.TestEntrance，示例中包含了使用启动参数配置待检测文章、文献库目录、相似度阈值、HTML输出文件和调试模式的代码。

主要流程为：

1. 预定义待检测文章文件的路径。
2. 预定义文献库目录路径。
3. 预定义相似度阈值。
4. 预定义HTML输出文件路径。
5. 预定义是否为调试模式（调试模式会在控制台打印处额外信息）。
6. 从启动参数设定以上值。
7. 实例化一个Cloaker控制器。
8. 实例化并配置所有的过滤器。
9. 将所有的过滤器加入Cloaker控制器中。
10. 以上述的1、2、5、4为参数调用Cloaker控制器的analysis方法执行整个程序流程。
11. 打开HTML输出文件查看查重报告。

## 简要结构

![结构图](https://cloakerdoc.maphical.cn/Cloaker%E7%AE%80%E8%A6%81%E7%BB%93%E6%9E%84%E5%9B%BE.png)

* TestEntrance —— 使用示例，程序的入口。
* Cloaker —— 程序的控制器。
* PaperFilter —— 过滤器的父类。
* CloakerFilter —— 在PaperFilter的基础上编写的过滤器。
* PaperAnalyzer —— 模块共用的接口。
* FileReader —— 文件读取模块。
* EntryGenerator —— 分句模块。
* WordDivider_IK —— 分词模块。
* SynonymsReplacer —— 同义词替换模块。
* EntryContrast —— 用于分析相似度的工具类。
* ContrastResult —— EntryContrast返回的结果。
* WrongArgumentException —— 自定义的“参数错误异常”。
* Article —— 文章类。
* Entry —— 分句类。

## 使用工具模块对文章（Article）和分词（Entry）进行预处理

Article和Entry可以通过它们的analyze方法接受带有PaperAnalyzer接口的模块，判断模块需要的参数类型并调用模块的Analyze方法对其自身进行操作。所以要使用工具模块对文章和分句进行操作只需要：

* 拥有一个“需要参数为Article或者Entry的Analyze方法”的模块类。
* 实例化这个模块类。
* 以这个模块对象为参数调用Article或者Entry的analyze方法。

## 编写自定义的过滤器（PaperFilter）

1. **创建自定义的过滤器类。** 在cn.eviadc.cloaker.analyzer.filter中新建一个类作为你的自定义过滤器类（类名任意），注意新的过滤器类要继承PaperFilter。
2. **编写自定义的过滤器需要的工具模块。** 在cn.eviadc.cloaker.analyzer中创建你的自定义过滤器所需要的工具模块，每个类代表一个模块。若工具模块需要对文章或者分句对象进行操作，那么就要为这个工具模块添加PaperAnalyzer接口，并且把主要的操作实现在接口的Analyze方法中，把所要操作的对象类型名通过接口的targetClassName方法返回。这是因为当Article或者Entry对象的analyze方法收到所传入的模块时，会调用模块的targetClassName方法来获得模块所需要的参数类型名，以此来向模块传入不同的参数。如下图：  
![接收模块流程](https://cloakerdoc.maphical.cn/Article%E6%8E%A5%E5%8F%97%E6%A8%A1%E5%9D%97.png)
3. **为自定义的过滤器设定参数。** 重新实现父类（PaperFilter）的setParam方法用来为自定义的过滤器设置参数。
4. **编写自定义的过滤器的主要功能。** 重新实现父类（PaperFilter）的Analyze方法来实现自定义过滤器的主要功能。
5. **让自定义的过滤器生成HTML文档。** 可以按需重新实现父类（PaperFilter）的genHTML方法用来生成HTML文档。

*注：除了实现过滤器的主要功能的Analyze方法之外，过滤器父类中其它的方法可以看作对过滤器功能的一点提示，您可以任意实现或重新实现您想要的过滤器的方法。*

## APIs

要查看Cloaker的API文档，点击[这里](https://cloakerdoc.maphical.cn)。 要下载文档（HTML）点击[这里](https://cloakerdoc.maphical.cn/CloakerDoc.zip)。
