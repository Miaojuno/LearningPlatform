<#import "/main/macros.ftl" as macros>
<#assign importCss=[]>
<#assign importJs=["/js/question/doQuestion.js"]>
<@macros.navhead importJs=importJs importCss=importCss titleName="做题"></@macros.navhead>

<div class="main-contain" style="margin-top: 1rem">
    <ul id="doQuetsion-tab" class="nav nav-tabs" role="tablist">
        <li class="active nav-item"><a href="#random" data-toggle="tab" class="nav-link">随机做题</a></li>
        <li class="nav-item"><a href="#recommend" data-toggle="tab" class="nav-link">为你推荐</a></li>
    </ul>

    <h5 id="pointDetail"></h5>
    <input hidden id="pointId">

    <div id="myTabContent" class="tab-content" style="margin-top: 2rem;">
        <div class="tab-pane fade in active" id="random">
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


        <div class="tab-pane fade" id="recommend">
            <p>aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa</p>
        </div>

    </div>
</div>

