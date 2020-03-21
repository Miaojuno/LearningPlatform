<#import "/main/macros.ftl" as macros>
<#assign importCss=["/css/friendShip/fsMain.css"]>
<#assign importJs=["/js/friendShip/fsMain.js"]>
<@macros.navhead importJs=importJs importCss=importCss titleName="我的好友"></@macros.navhead>

<div class="main-contain" style="margin-top: 1rem">
    <div class="row friend-div">
        <div class="friendList col-3">

        </div>

        <div class="content-div col-9">
            <div class="content-top">

            </div>
            <div class="content-bottom row">
                <div class="col-10">
                    <input class="in-msg form-control">
                </div>
                <div class="col-2">
                    <button class="sendBtn btn btn-success">发送</button>
                </div>
            </div>
        </div>
    </div>

</div>

