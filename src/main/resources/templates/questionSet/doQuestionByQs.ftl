<#import "/main/macros.ftl" as macros>
<#assign importCss=[]>
<#assign importJs=["/js/question/doQuestionByQs.js"]>
<@macros.navhead importJs=importJs importCss=importCss></@macros.navhead>

<div class="main-contain" style="margin-top: 1rem">

    <h4 class="qsName"></h4>

    <div class="badges-div">

    </div>

    <div class="card" id="do-qs-question-div">
        <div class="card-header">
            <input class="questionId" hidden>
            <img class="questionPic img-thumbnail">
            <p class="questionDetail"></p>
        </div>
        <div class="card-body">
            <div class="col-10 type1">
                <div class="btn-group optionsDiv"></div>
                <textarea rows="5" class="userSolution"
                          placeholder="在这里输入你的答案。。。"></textarea>
                <input type="file" class="file" id="upFile" hidden>
                <input type="text" class="fileInput form-control" placeholder="上传图片(jpg/png)">
            </div>
            <div class="col-10 type2">
                <textarea rows="1" class="form-control userOldSolution " style="margin-bottom: 1rem"
                          disabled></textarea>
                <img class="recordUserPic img-thumbnail">
            </div>
        </div>
        <div class="card-footer">
            <button class="submitRecord btn btn-primary" style="margin-top: 1rem; margin-left: 1rem;">提交
            </button>
        <#--<button class="changeRandom btn btn-primary" style="margin-top: 1rem; margin-left: 1rem;">换一题-->
        <#--</button>-->
        </div>
    </div>

</div>

<style>
    .badges-div {
        margin: 1rem 0;
    }

    .badges-div .badge {
        width: 3.5rem;
        padding: 0.4rem 0;
        margin-right: 0.5rem;
        border-radius: 1rem;
        z-index: 999;
        color: white;
    }

    .badges-div .badge:hover {
        cursor: pointer;
    }

    .finished {
        background-color: #00c758;
    }

    .unfinished {
        background-color: #b4b4b4;
    }

    .choosed {
        font-size: 150%;
        width: 4.5rem !important;
        padding: 0.4rem 0 !important;
    }
</style>


