<#import "/main/macros.ftl" as macros>
<#assign importCss=[]>
<#assign importJs=["/js/role/role.js"]>
<@macros.navhead importJs=importJs importCss=importCss></@macros.navhead>

<div class="main-contain" style="margin-top: 1rem">
    <div class="searchdiv m-auto row">
        <#--<div class="row">-->
            <#--<label class="control-label col-2" for="">用户名:</label>-->
            <#--<div class="col-10 ">-->
                <#--<input name="userName" class="form-control input-sm" type="text" placeholder="请输入">-->
            <#--</div>-->
        <#--</div>-->
        <#--<div class="row">-->
            <#--<button id="data-query" class="btn btn-sm btn-submit bt-query">查询</button>-->
            <#--<button type="reset" id="data-reset" class="btn btn-sm btn-reset">重置</button>-->
        <#--</div>-->
    </div>
    <div class="tablediv m-auto">
    <#--数据操作-->
        <div class="query-operation">
            <button type="button" id="role-add" class="btn btn-outline-primary btn-sm">新增</button>
        </div>

        <table id="role-table"></table>
    </div>
</div>

<#--add遮罩层-->
<div id="role-add-div" style="display: none;">
    <div class="modal-body">
        <form id="role-add-form">
            <div class="row">
                <label class="control-label col-2 text-right" for="">角色名</label>
                <div class="col-8">
                    <input type="text" name="roleName" class="form-control roleName"  placeholder="输入角色名..." autocomplete="off">
                </div>
            </div>
            <div class="row">
                <label class="control-label col-2 text-right" for="">描述</label>
                <div class="col-8">
                    <input type="text" name="roleDesc" class="form-control roleDesc" placeholder="输入描述..." autocomplete="off">
                </div>
            </div>
        </form>
    </div>
</div>

<#--modify遮罩层-->
<div id="role-modify-div" style="display: none;">
    <div class="modal-body">
        <form id="role-modify-form">
            <div class="row" hidden>
                <label class="control-label col-2 text-right" for="">id</label>
                <div class="col-8">
                    <input type="text" name="roleId" class="form-control roleId"  autocomplete="off">
                </div>
            </div>
            <div class="row">
                <label class="control-label col-2 text-right" for="">角色名</label>
                <div class="col-8">
                    <input type="text" name="roleName" class="form-control roleName"  placeholder="输入角色名..." autocomplete="off">
                </div>
            </div>
            <div class="row">
                <label class="control-label col-2 text-right" for="">描述</label>
                <div class="col-8">
                    <input type="text" name="roleDesc" class="form-control roleDesc" placeholder="输入描述..." autocomplete="off">
                </div>
            </div>
            <div class="row">
                <label class="control-label col-2 text-right" for="">激活状态</label>
                <div class="col-8">
                    <select id="modify-isActive" name="isActive" class="form-control">
                        <option value="1" >激活</option>
                        <option value="0" >冻结</option>
                    </select>
                </div>
            </div>

        </form>
    </div>
</div>