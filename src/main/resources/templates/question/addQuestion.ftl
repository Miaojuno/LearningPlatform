<#import "/main/macros.ftl" as macros>
<#assign importCss=[]>
<#assign importJs=[]>
<@macros.navhead importJs=importJs importCss=importCss></@macros.navhead>

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
                <input type="text" class="fileInput1 form-control" placeholder="题目图片 ，可选 ，jpg/png">
            </div>
        </div>

    <#--答案部分-非选择-->
        <div class="row type1">
            <label class="control-label col-2 text-right" for="">答案*</label>
            <div class="col-10">
            <textarea rows="5" id="defaultSolution" class="solution"
                      placeholder="输入答案。。。"></textarea>
                <input type="file" class="file2" id="file2" hidden>
                <input type="text" class="fileInput2 form-control" placeholder="答案图片 ，可选 ，jpg/png">
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




<script>
    $(function () {
        //为题集添加题目时，修改最上方标题,添加完成按钮（跳转至首页）
        var url = location.search;
        var qsId;
        if (url.indexOf("?") != -1) {
            qsId = url.split("=")[1];
            $.ajax({
                type: "post",
                url: "/questionSet/findById",
                data: {
                    "qsId": qsId
                },
                success: function (res) {
                    $(".add-question-title").text("新增题目至题集：" + res.data.qsName)
                    $(".btns").append("<button class=\"submitQuestionSet btn btn-primary\" style=\"margin-top: 1rem; margin-left: 1rem;\">完成</button>")
                },
                error: function (err) {
                    layer.msg("失败", {icon: 2});
                }
            });

        }
        $(document).on("click", ".submitQuestionSet", function () {
            layui.use('layer', function () {
                layer = layui.layer;
                layer.msg("题集创建完成", {icon: 1, time: 1000});
                window.location.href = "/index";
            })
        })



        $("#questionType").bind("change", function () {
            if ($("#questionType option:selected").val() == "选择题") {
                $(".type1").hide()
                $(".type2").show()
            }
            else {
                $(".type1").show()
                $(".type2").hide()
            }
        });

        $('.fileInput1').on('click', function () {
            $(".addDiv .file1").click()
        });

        $(".file1").bind("input propertychange", function () {
            $('.addDiv .fileInput1').val($('.addDiv .file1').val())
        });

        $('.fileInput2').on('click', function () {
            $(".addDiv .file2").click()
        });

        $(".file2").bind("input propertychange", function () {
            $('.addDiv .fileInput2').val($('.addDiv .file2').val())
        });

        //提交
        $('.submitQuestion').on('click', function () {
            layui.use('layer', function () {
                layer = layui.layer;
                var data = new FormData();
                data.append("type", $("#questionType option:selected").val());
                data.append("questionDetail", $(".questionDetail").val());
                data.append("typeDistribution", $(".typeDistribution").val());
                data.append("difficultyDistribution", $(".difficultyDistribution").val());
                data.append("pointId", $(".pointId").val());
                data.append("score", $(".score").val());
                var fileObj1 = document.getElementById("file1").files[0];
                var fileObj2 = document.getElementById("file2").files[0];
                data.append("file1", fileObj1);
                data.append("file2", fileObj2);
                if ($("#questionType option:selected").val() == "选择题") {
                    data.append("solution", $("#solutionChoose").val() + "/" + $("#solution4choose").val())
                }
                else {
                    data.append("solution", $("#defaultSolution").val())
                }

                $.ajax({
                    url: "/neo/addQuestion",
                    data: data,
                    dataType: "json",
                    type: "post",
                    processData: false,
                    contentType: false,
                    success: function (result) {
                        if (result.success == true) {
                            //添加至公共题库
                            if (url.indexOf("?") == -1) {
                                layer.msg("添加成功", {icon: 1, time: 2000});
                                $(".questionDetail").val("");
                                $(".typeDistribution").val("");
                                $(".difficultyDistribution").val("");
                                $(".pointId").val("");
                                $(".score").val("");
                                $("#defaultSolution").val("");
                                $("#solutionChoose").val("");
                                $("#solution4choose").val("");
                                $(".file1").val("")
                                $(".file2").val("")
                                $(".fileInput1").val("")
                                $(".fileInput2").val("")
                            }
                            //添加至题集
                            else {
                                $.ajax({
                                    url: "/questionSet/addQuestion",
                                    data: {
                                        "qsId": qsId,
                                        "questionId": result.questionId
                                    },
                                    dataType: "json",
                                    type: "post",
                                    success: function (result) {
                                        if (result.success == true) {
                                            layer.msg("添加成功", {icon: 1, time: 2000});
                                        } else {
                                            layer.msg(result.msg, {icon: 2});
                                        }
                                        $(".questionDetail").val("");
                                        $(".typeDistribution").val("");
                                        $(".difficultyDistribution").val("");
                                        $(".pointId").val("");
                                        $(".score").val("");
                                        $("#defaultSolution").val("");
                                        $("#solutionChoose").val("");
                                        $("#solution4choose").val("");
                                        $(".file1").val("")
                                        $(".file2").val("")
                                        $(".fileInput1").val("")
                                        $(".fileInput2").val("")
                                    },
                                    error: function (result) {
                                    }
                                })
                            }

                        } else {
                            layer.msg(result.msg, {icon: 2});
                        }
                    },
                    error: function (result) {
                    }
                })
            })
        });


    });
</script>