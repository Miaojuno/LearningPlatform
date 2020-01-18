<#import "/main/macros.ftl" as macros>
<#assign importCss=[]>
<#assign importJs=["/js/user/user.js","/js/user/superior.js"]>
<@macros.navhead importJs=importJs importCss=importCss></@macros.navhead>

<div class="main-contain" style="margin-top: 1rem">
    <div class="searchdiv m-auto row">
        <div class="row form-group col-5">
            <label class="control-label col-4 text-right" for="">姓名:</label>
            <div class="col-8">
                <input type="text" id="userName" class="form-control">
            </div>
        </div>

        <div class="row form-group col-5">
            <label class="control-label col-4 text-right" for="">角色:</label>
            <div class="col-8">
                <@macros.roleSelect id="roleSelect" ></@macros.roleSelect>
            </div>
        </div>

        <div class="row buttons">
            <button id="data-query" class="btn btn-primary btn-sm">查询</button>
            <#--<button id="data-reset" class="btn btn-primary btn-sm">重置</button>-->
        </div>

    </div>
    <div class="tablediv m-auto">
        <#--数据操作-->
        <div class="query-operation">
            <button type="button" id="user-add" class="btn btn-outline-primary btn-sm">新增</button>
        </div>

        <#--内容表格-->
        <table id="user-table"></table>
    </div>
</div>

<#--add遮罩层-->
<div id="user-add-div" class="maskLayer" style="display: none;">
    <div class="modal-body">
        <form id="user-add-form">
            <div class="row">
                <label class="control-label col-3 text-right" for="">账户</label>
                <div class="col-8">
                    <input type="text" name="userAccount" class="form-control account" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')" placeholder="输入账户..." autocomplete="off">
                </div>
            </div>
            <div class="row">
                <label class="control-label col-3 text-right" for="">密码</label>
                <div class="col-8">
                    <input type="text" name="userPwd" class="form-control pwd1" placeholder="输入密码..." autocomplete="off">
                </div>
            </div>
            <div class="row">
                <label class="control-label col-3 text-right" for="">姓名</label>
                <div class="col-8">
                    <input type="text" name="userName" class="form-control name" placeholder="输入姓名..." autocomplete="off">
                </div>
            </div>
            <div class="row">
                <label class="control-label col-3 text-right" for="">角色</label>
                <div class="col-8">
                    <@macros.roleSelect id="user-add-roleSelect" name="roleId"></@macros.roleSelect>
                </div>
            </div>
            <div class="row">
                <label class="control-label col-3 text-right" for="">简介</label>
                <div class="col-8">
                    <textarea rows="3" name="userDesc" class="form-control desc"  autocomplete="off"></textarea>
                </div>
            </div>
        </form>
    </div>
</div>


<#--modify遮罩层-->
<div id="user-modify-div" class="maskLayer" style="display: none;">
    <div class="modal-body">
        <form id="user-modify-form">
            <div class="row" hidden>
                <label class="control-label col-3 text-right" for="">id</label>
                <div class="col-8">
                    <input type="text" name="userId" class="form-control userId"  autocomplete="off">
                </div>
            </div>
            <div class="row">
                <label class="control-label col-3 text-right" for="">账户</label>
                <div class="col-8">
                    <input type="text" name="userAccount" class="form-control account" disabled>
                </div>
            </div>
            <div class="row">
                <label class="control-label col-3 text-right" for="">姓名</label>
                <div class="col-8">
                    <input type="text" name="userName" class="form-control name" placeholder="输入姓名..." autocomplete="off">
                </div>
            </div>
            <div class="row">
                <label class="control-label col-3 text-right" for="">简介</label>
                <div class="col-8">
                    <textarea rows="3" name="userDesc" class="form-control desc"  autocomplete="off"></textarea>
                </div>
            </div>
            <div class="row">
                <label class="control-label col-3 text-right" for="">角色</label>
                <div class="col-8">
                    <@macros.roleSelect id="user-modify-roleSelect" name="roleId"></@macros.roleSelect>
                </div>
            </div>
        </form>
    </div>
</div>

<#--配置上级遮罩层-->
<div id="user-superior-div" class="maskLayer" style="display: none;">
    <div class="modal-body">
        <form id="superior-modify-form">
            <div class="row" hidden>
                <label class="control-label col-3 text-right" for="">id</label>
                <div class="col-8">
                    <input type="text" name="userId" id="subordinateId" class="form-control userId"  autocomplete="off">
                </div>
            </div>
            <div class="row">
                <label class="control-label col-3 text-right" for="">姓名</label>
                <div class="col-8">
                    <input type="text" name="userName" id='searchSuperiorName'  class="form-control">
                </div>
            </div>
        </form>
        <div class="tablediv m-auto">
        <#--内容表格-->
            <table id="superior-table"></table>
        </div>
    </div>
</div>