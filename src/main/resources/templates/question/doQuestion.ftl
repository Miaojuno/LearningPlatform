<#import "/main/macros.ftl" as macros>
<#assign importCss=[]>
<#assign importJs=["/js/question/doQuestion.js"]>
<@macros.navhead importJs=importJs importCss=importCss titleName="做题"></@macros.navhead>

<div class="main-contain" style="margin-top: 1rem">
    <#--<ul id="doQuetsion-tab" class="nav nav-tabs" role="tablist">-->
        <#--<li class="active nav-item"><a href="#random" data-toggle="tab" class="nav-link">随机做题</a></li>-->
        <#--<li class="nav-item"><a href="#recommend" data-toggle="tab" class="nav-link">为你推荐</a></li>-->
    <#--</ul>-->

    <h5 id="pointDetail"></h5>
    <input hidden id="pointId">
    <input hidden id="diff">
    <input hidden id="type">

    <div class="choose-div">
        <div class="container" style="margin-bottom: 0.5rem;padding: 0;">
            <div class="btn-group btn-group-justified" style="width: 100%">
                <button type="button" id="btn-random"  class="btn btn-primary">随机做题</button>
                <button type="button" id="btn-diff"  class="btn btn-primary">选择难度</button>
                <button type="button" id="btn-type"  class="btn btn-primary">选择类型</button>
                <button type="button" id="btn-foru"  class="btn btn-primary">为你推荐</button>
            </div>
        </div>

        <div class="container diff-div" style="margin-bottom: 1rem;padding: 0;">
            <div class="btn-group btn-group-justified" style="width: 50%">
                <button type="button" id="btn-diff1"  class="btn btn-primary">简单</button>
                <button type="button" id="btn-diff2"  class="btn btn-primary">普通</button>
                <button type="button" id="btn-diff3"  class="btn btn-primary">困难</button>
            </div>
        </div>

        <div class="container type-div" style="margin-bottom: 1rem;padding: 0;">
            <div class="btn-group btn-group-justified" style="width: 50%">
                <button type="button" id="btn-type1"  class="btn btn-primary">主观题</button>
                <button type="button" id="btn-type2"  class="btn btn-primary">客观题</button>
                <button type="button" id="btn-type3"  class="btn btn-primary">选择题</button>
            </div>
        </div>
        <div class="row" >
            <div class="row col-6" style="border-bottom: solid 1px rgba(152, 152, 152, 0.69);margin-left: 1rem;padding: 0;">
                <h4 style="margin-right: 1rem">已选择:</h4>
                <h4 id="doq-title" >随机做题</h4>
            </div>

        </div>
    </div>


    <#--<div id="myTabContent" class="tab-content" style="margin-top: 2rem;">-->
        <#--<div class="tab-pane fade in active" id="random">-->
        <div class="" id="random">
            <div class="card">
                <div class="card-header">
                    <input class="questionId" hidden>
                    <img class="questionPic img-thumbnail">
                    <p class="questionDetail"></p>
                </div>
                <div class="card-body">
                    <div class="col-10">
                        <div class="btn-group optionsDiv"></div>
                        <textarea rows="5" class="userSolution"
                                  placeholder="在这里输入你的答案。。。"></textarea>
                        <input type="file" class="file" id="randomFile" hidden>
                        <input type="text" class="fileInput form-control" placeholder="上传图片(jpg/png)">
                    </div>
                </div>
                <div class="card-footer">
                    <button class="submitRecord btn btn-primary" style="margin-top: 1rem; margin-left: 1rem;">提交
                    </button>
                    <button class="changeRandom btn btn-primary" style="margin-top: 1rem; margin-left: 1rem;">换一题
                    </button>
                </div>
            </div>
        </div>


        <#--<div class="tab-pane fade" id="recommend">-->
            <#--<p>aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa</p>-->
        <#--</div>-->

    <#--</div>-->
</div>

<style>
    .btn-group button{
        background-color: #337ab7;
        border-color: #296092;
    }
    .btn-group button :hover{
        background-color: #224e75;
    }
</style>