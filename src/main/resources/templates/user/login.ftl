<!DOCTYPE html>
<html lang="en">
<head>
    <title>登录-学习平台</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <script src="/webjars/jquery/3.1.1/jquery.min.js"></script>
    <script src="/webjars/bootstrap/4.3.1/js/bootstrap.min.js"></script>
    <script src="/webjars/layui/2.5.5/layui.js"></script>
    <link rel="stylesheet" type="text/css" href="/webjars/bootstrap/4.3.1/css/bootstrap.min.css" />
    <link rel="stylesheet" type="text/css" href="/css/user/login.css" />
    <meta name="viewport" content="width=device-width, initial-scale=1">
</head>
<body>
    <div class="container">
        <form id="form-login" action="/user/login" method="post">
            <div class="row">
                <h3 class="text-center">学习平台</h3>
            </div>
            <div class="row form-group">
                <label class="control-label col-2" for="">账户</label>
                <div class="col-10">
                    <input type="text" class="form-control" name="userAccount" placeholder="输入你的账户...">
                </div>
            </div>
            <div class="row form-group">
                <label class="control-label col-2" for="">密码</label>
                <div class="col-10">
                    <input type="password" class="form-control" name="userPwd" placeholder="输入你的密码...">
                </div>
            </div>
            <div class="row">
                <span style="font-size: 15px;color: red;">${(msg)!}</span>
            </div>
            <div class="row">
                <button type="submit" id="btn-register" class="btn btn-success">登陆</button>
            </div>
            <div class="row">
                <button id="go-register" type="button" class="btn btn-success">去注册</button>
            </div>
        </form>


    </div>

<#--<p>当前时间：${.now?string("yyyy-MM-dd HH:mm:ss.sss")}</p>-->
</body>
</html>

<script src="/js/user/login.js"></script>
