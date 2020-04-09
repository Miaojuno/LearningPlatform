<#import "/main/macros.ftl" as macros>
<#assign importCss=[]>
<#assign importJs=["/js/question/addQuestion.js"]>
<@macros.navhead importJs=importJs importCss=importCss titleName="添加题目"></@macros.navhead>

<div class="main-contain addDiv" style="margin-top: 1rem">

    <h5 class="add-question-title">新增题目至公共题库</h5>

    <form id="questionAddForm">
        <div class="row">
            <label class="control-label col-2 text-right" for="">题目类型*</label>
            <div class="col-10">
                <select id="questionType" name="questionType" class=" form-control">
                    <option value="主观题">主观题</option>
                    <option value="客观题">客观题</option>
                    <option value="选择题">选择题</option>
                </select>
            </div>
        </div>
        <div class="row">
            <label class="control-label col-2 text-right" for="">题目*</label>
            <div class="col-10">
            <textarea rows="5" class="questionDetail" name="questionDetail"
                      placeholder="输入题目。。。"></textarea>
                <input type="file" class="file1" id="file1" hidden>
                <input type="text" class="fileInput1 form-control" placeholder="题目图片 ，可选 ，jpg/png" readonly>
            </div>
        </div>

    <#--答案部分-非选择-->
        <div class="row type1">
            <label class="control-label col-2 text-right" for="">答案*</label>
            <div class="col-10">
            <textarea rows="5" id="defaultSolution" class="solution"
                      placeholder="输入答案。。。"></textarea>
                <input type="file" class="file2" id="file2" hidden>
                <input type="text" class="fileInput2 form-control" placeholder="答案图片 ，可选 ，jpg/png" readonly>
            </div>
        </div>

    <#--答案部分-选择题-->
        <div class="type2" style="display: none">
            <div class="row">
                <label class="control-label col-2 text-right" for="">答案*</label>
                <div class="col-10">
                    <input class="form-control" id="solutionChoose" class="solution" placeholder="输入选择题答案，例：A">
                </div>
            </div>
            <div class="row">
                <label class="control-label col-2 text-right" for="">答案备选*</label>
                <div class="col-10">
                    <input class="form-control" id="solution4choose" class="solution4choose"
                           placeholder="输入选择题答案备选，例：ABCD">
                </div>
            </div>
        </div>

        <div class="row">
            <label class="control-label col-2 text-right" for="">分值*</label>
            <div class="col-10">
                <input type="text" class="score form-control" name="score" placeholder="请输入纯数字（不带小数点）">
            </div>
        </div>

        <div class="row">
            <label class="control-label col-2 text-right" for="">知识点</label>
            <div class="col-10">
            <@macros.pointChoose class="pointId" name="pointId"></@macros.pointChoose>
            </div>
        </div>

        <div class="row">
            <label class="control-label col-2 text-right" for="">类型分布</label>
            <div class="col-10">
                <input type="text" class="typeDistribution form-control" name="typeDistribution" placeholder="">
            </div>
        </div>

        <div class="row">
            <label class="control-label col-2 text-right" for="">难度分布</label>
            <div class="col-10">
                <input type="text" class="difficultyDistribution form-control" name="difficultyDistribution"
                       placeholder="">
            </div>
        </div>


        <div class="col-10 btns">
            <button class="submitQuestion btn btn-primary" style="margin-top: 1rem; margin-left: 1rem;">提交题目
            </button>

        </div>
    </form>


</div>
