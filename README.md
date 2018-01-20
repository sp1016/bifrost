# bifrost

#### 简介  
基于[Guava](https://github.com/google/guava)中令牌筒算法`com.google.common.util.concurrent.RateLimiter`实现的*方法级别*限流器工具, 项目以来`guava`和`spring`, 通过`spring`注解的方式来使用  

#### Guava令牌筒简介  

#### 使用方式  
bifrost依赖spring, 在本地`classpath：props`目录下添加`limiter.properties`配置:  

````
default.isLimit=true  # 默认是否限流, 可以把default改成方法的key, 精确控制每个方法
default.premitsPreSecond=1024  # 默认发送速度, 可以把default改成方法的key, 精确控制每个方法
````

然后在需要限流方法上添加bifrost注解@Limiter, 注解参数：  

|param|description|
|:---:|:---------:|
|key|可选参数, 方法限流器key, 可以通过key在配置文件中配置是否限流和限流器令牌发送速度|
|permits|可选参数,方法需要的令牌个数,默认值为1|
|limitType|可选参数, 限流方式, 可选项为`WAIT`和`RETURN`, `WAIT`表示如果筒中没足够的令牌则阻塞等待, `RETURN`表示如果筒中没足够的令牌则直接返回, 默认限流方式为`RETURN`|
|code|必输参数, 被限流的请求的返回码|
|codeField|可选参数, 设置返回码的字段, 默认为`code`|
|message|必输参数, 被限流的请求的返回信息|
|messageField|可选参数, 设置返回信息的字段, 默认为`message`|

#### 注意事项  