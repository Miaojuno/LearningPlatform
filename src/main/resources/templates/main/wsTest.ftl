<#import "/main/macros.ftl" as macros>
<#assign importCss=[]>
<#assign importJs=[]>
<@macros.navhead importJs=importJs importCss=importCss></@macros.navhead>

<div class="main-contain" style="margin-top: 1rem">
    <h3>WebSocket测试，在<span style="color:red">控制台</span>查看测试信息输出！</h3>
    <h4>
        [url=/api/ws/sendOne?message=单发消息内容&id=none]单发消息链接[/url]
        [url=/api/ws/sendAll?message=群发消息内容]群发消息链接[/url]
    </h4>


</div>
 <script type="text/javascript">
     var socket;
     if (typeof (WebSocket) == "undefined") {
         console.log("浏览器不支持WebSocket");
     } else {

         socket = new WebSocket("ws://localhost:8080/ws/asset?userAccount=" + $("#loginUserAccount").val());
         //连接打开事件
         socket.onopen = function () {
             console.log($("#loginUserAccount").val() + "已连接");
             socket.send($("#loginUserAccount").val() + "已连接");
         };
         //收到消息事件
         socket.onmessage = function (msg) {
             console.log(msg.data);
         };
         //连接关闭事件
         socket.onclose = function () {
             console.log("Socket已关闭");
         };
         //发生了错误事件
         socket.onerror = function () {
             alert("Socket发生了错误");
         }

         //窗口关闭时，关闭连接
         window.unload = function () {
             socket.close();
         };
     }
 </script>