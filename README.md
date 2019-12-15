# asiant-ratong

代理服务工具: 使用多种方式实现

## netty实现

通过提供对nett-http的request和response的拦截器接口，可以实现对netty-proxy进行扩展，以实现想要的HTTP代理功能

下面对netty实现模块进行说明：
实现项目目录netty-http-proxy，以下为项目目录：

- :netty-http-proxy:http-interceptor            nett-http的Request和Response拦截器接口
- :netty-http-proxy:kerberos-extension          基于kerberos认证的扩展实现
- :netty-http-proxy:proxy-application           netty代理核心实现

在proxy-application项目中主方法CliApplication中已经对netty-proxy进行了实现的封装及示例，我们通过对HttpProxyServer进行Request或者Response进行添加Interceptor即可，主方法中对返回http-response添加了proxy-host头

## spring实现

spring实现主要使用spring-cloud-gateway进行实现通过添加filter对路由进行处理即可

KerberosAuthenticateGlobalFilter中添加kerberos认证实现即可，代码中以添加X-User请求头为样例，进行kerberos认证时，将此部分替换即可

生成kerberos认证token可以参看[:netty-http-proxy:kerberos-extension](netty-http-proxy/kerberos-extension)


## http-client实现
