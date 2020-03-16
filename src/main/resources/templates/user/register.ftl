<#import "/main/macros.ftl" as macros>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>注册-学习平台</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <script src="/webjars/jquery/3.1.1/jquery.min.js"></script>
    <script src="/webjars/bootstrap/4.3.1/js/bootstrap.min.js"></script>
    <script src="/webjars/layui/2.5.5/layui.js"></script>
    <link rel="stylesheet" type="text/css" href="/webjars/bootstrap/4.3.1/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="/css/user/login.css"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
</head>
<body>
<div class="container">
    <form id="form-register" method="post">
        <div class="row">
            <h3 class="text-center">账户注册</h3>
        </div>
        <div class="row">
            <label class="control-label col-2 text-right" for="">账户</label>
            <div class="col-10">
                <input type="text" name="userAccount" class="form-control account"
                       onkeyup="value=value.replace(/[^\w\.\/]/ig,'')" placeholder="输入你的账户...">
            </div>
        </div>
        <div class="row">
            <label class="control-label col-2 text-right" for="">密码</label>
            <div class="col-10">
                <input type="password" name="userPwd" class="form-control pwd1" placeholder="输入你的密码...">
            </div>
        </div>
        <div class="row">
            <label class="control-label col-2 text-right" for="">确认密码</label>
            <div class="col-10">
                <input type="password" class="form-control pwd2" placeholder="再次输入你的密码...">
            </div>
        </div>
        <div class="row">
            <label class="control-label col-2 text-right" for="">姓名</label>
            <div class="col-10">
                <input type="text" name="userName" class="form-control name" placeholder="输入你的姓名...">
            </div>
        </div>
        <#--<div class="row">-->
            <#--<label class="control-label col-2 text-right" for="">角色</label>-->
            <#--<div class="col-10">-->
                    <#--<@macros.roleSelect id="user-register-roleSelect" name="roleId"></@macros.roleSelect>-->
            <#--</div>-->
        <#--</div>-->
        <div class="row">
            <span style="font-size: 15px;color: red;">${(msg)!}</span>
        </div>
        <div class="row">
            <button type="button" id="btn-register" class="btn btn-success">注册</button>
        </div>
        <div class="row">
            <button id="go-login" type="button" class="btn btn-success">返回</button>
        </div>
    </form>

</div>

</body>
</html>

<script src="/js/user/login.js"></script>