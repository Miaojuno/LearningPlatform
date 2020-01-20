<#import "/main/macros.ftl" as macros>
<#assign importCss=[]>
<#assign importJs=["/js/role/role.js"]>
<@macros.navhead importJs=importJs importCss=importCss></@macros.navhead>

<div class="main-contain" style="margin-top: 1rem">

<#--角色修改申请遮罩层-->
<#--add遮罩层-->
    <div id="role-modify-apply-div" class="maskLayer" style="display: none;">
        <div class="modal-body">
            <form id="role-modify-apply-form">
                <div class="row">
                    <label class="control-label col-3 text-right" for="">角色</label>
                    <div class="col-8">
                    <@macros.roleSelect id="user-add-roleSelect" name="roleId"></@macros.roleSelect>
                    </div>
                </div>
                <div class="row">
                    <label class="control-label col-3 text-right" for="">简介</label>
                    <div class="col-8">
                        <textarea rows="3" name="userDesc" class="form-control desc" autocomplete="off"></textarea>
                    </div>
                </div>
            </form>
        </div>
    </div>

</div>