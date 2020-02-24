<#import "/main/macros.ftl" as macros>
<#assign importCss=[]>
<#assign importJs=[]>
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
            <div class="col-10">
                <div class="btn-group optionsDiv"></div>
                <textarea rows="5" class="userSolution"
                          placeholder="在这里输入你的答案。。。"></textarea>
                <input type="file" class="file" id="upFile" hidden>
                <input type="text" class="fileInput form-control" placeholder="上传图片(jpg/png)">
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


<script>
    $(function () {
        var url = location.search;
        var qsId;
        if (url.indexOf("?") != -1) {
            qsId = url.split("=")[1];

            $.ajax({
                type: "post",
                url: "/questionSet/findQsRecordByUser",
                data: {
                    "userAccount": $("#loginUserAccount").val(),
                    "qsId": qsId
                },
                success: function (res) {
                    $(".qsName").text(res.data.qsName);
                    var unfinishedList = res.data.unfinishedQuestionIds.split("、");
                    var allList = res.data.questionIds.split("、");
                    var num = 1;
                    for (i = 0, len = allList.length; i < len; i++) {
                        //已完成的题目
                        if (!jquery.inArray(allList[i], unfinishedList)) {
                            $(".badges-div").append("<span class='badge badge-success finished' id='question-'" + allList[i] + ">" + num + "</span>")
                        }
                        //未完成的题目
                        else {
                            $(".badges-div").append("<span class='badge badge-secondary unfinished' id='question-'" + allList[i] + ">" + num + "</span>")
                        }
                        num++;
                    }
                    getQuestion($('.badges-div .badge').eq(0).attr('id').substring(9))
                },
                error: function (err) {
                    layer.msg("失败", {icon: 2});
                }
            });
        }

        $('#do-qs-question-div .fileInput').on('click', function () {
            $("#do-qs-question-div .file").click()
        });

        $("#do-qs-question-div .file").bind("input propertychange", function () {
            $('#do-qs-question-div .fileInput').val($('#do-qs-question-div .file').val())
        });

        // //换一题
        // $('#do-qs-question-div .changeRandom').on('click', function () {
        //     var qid = $('#do-qs-question-div .questionId').val();
        //     var number = 0;
        //     getRandomQuestion()
        //     console.log(qid)
        //     console.log($('#do-qs-question-div .questionId').val())
        //     while (qid == $('#do-qs-question-div .questionId').val() && number < 10) {
        //         console.log(qid)
        //         console.log($('#do-qs-question-div .questionId').val())
        //         getRandomQuestion();
        //         number++;
        //     }
        //     if (number == 10) {
        //         layer.msg("没有题目啦！", {icon: 2});
        //     }
        // });

        //提交
        $('#do-qs-question-div .submitRecord').on('click', function () {
            submitRecord()
        });

        //选择题选择答案
        $(document).on('click', '#do-qs-question-div .optionsDiv .btn', function () {
            $("#do-qs-question-div .userSolution").val($(this).text())
            submitRecord()
        })

        // 提交方法
        function submitRecord() {
            layui.use('layer', function () {
                layer = layui.layer;
                var fileObj = document.getElementById('upFile').files[0]; // js 获取文件对象
                var url = "/record/add"; // 接收上传文件的后台地址
                var form = new FormData();  // FormData 对象
                form.append("questionId", $("#do-qs-question-div .questionId").val())
                form.append("userSolution", $("#do-qs-question-div .userSolution").val())
                form.append("file", fileObj); // 文件对象
                if (fileObj != null) {
                    var name = fileObj.name.split(".")
                    if (name[name.length - 1] != "jpg" && name[name.length - 1] != "png") {
                        layer.confirm("导入失败,只支持jpg、png格式文件", {icon: 2}, function (index) {
                            layer.close(index);
                        });
                        return
                    }
                }

                $.ajax({
                    type: "post",
                    url: url,
                    data: form,
                    processData: false,
                    contentType: false,
                    success: function (res) {
                        layer.msg("ok", {icon: 1});
                        $("#do-qs-question-div .file").val("");
                        $("#do-qs-question-div .fileInput").val("");
                        $("#do-qs-question-div .userSolution").val("");
                        $("#do-qs-question-div .optionsDiv").empty();
                        // getRandomQuestion()

                    },
                    error: function (err) {
                        layer.msg("失败", {icon: 2});
                        $("#do-qs-question-div .file").val("");
                        $("#do-qs-question-div .fileInput").val("");
                        $("#do-qs-question-div .userSolution").val("");
                        $("#do-qs-question-div .optionsDiv").empty();
                    }
                });

            })
        }


        function getQuestion(questionId) {
            layui.use('layer', function () {
                layer = layui.layer;
                $.ajax({
                    url: "/neo/findByQuestionId",
                    async: false,
                    data{
                        "questionId": questionId
                    },
                    dataType: "json",
                    type: "post",
                    success: function (result) {
                        if (result.success == true) {
                            $("#do-qs-question-div .questionId").val(result.data.questionId);
                            if (result.data.questionDetail == null || result.data.questionDetail == "") {
                                $("#do-qs-question-div .questionDetail").hide()
                            }
                            else {
                                $("#do-qs-question-div .questionDetail").show()
                                $("#do-qs-question-div .questionDetail").html(result.data.questionDetail);
                            }

                            if (result.data.pic == null) {
                                $("#do-qs-question-div .questionPic").hide()
                            }
                            else {
                                $("#do-qs-question-div .questionPic").show()
                                $("#do-qs-question-div .questionPic").attr("src", "data:image/jpeg;base64," + result.data.pic);
                            }

                            if (result.data.type == "主观题") {
                                $("#do-qs-question-div .fileInput").show();
                                $("#do-qs-question-div .userSolution").show();
                                $("#do-qs-question-div .userSolution").attr("rows", "5");
                                $("#do-qs-question-div .optionsDiv").empty();
                                $("#do-qs-question-div .submitRecord").show()
                            }
                            else if (result.data.type == "客观题") {
                                $("#do-qs-question-div .fileInput").hide();
                                $("#do-qs-question-div .userSolution").show();
                                $("#do-qs-question-div .userSolution").attr("rows", "1");
                                $("#do-qs-question-div .optionsDiv").empty();
                                $("#do-qs-question-div .submitRecord").show()

                            }
                            else {
                                $("#do-qs-question-div .fileInput").hide();
                                $("#do-qs-question-div .userSolution").hide();
                                var options = result.data.solution.split("/")[1];
                                for (i = 0; i < options.length; i++) {
                                    $("#do-qs-question-div .optionsDiv").append("    <button type=\"button\" class=\"btn btn-primary\" " +
                                            // "onclick=\"optionSubmitRecord(" + options.charAt(i) + ");\">" + options.charAt(i) + "</button>\n")
                                            "\">" + options.charAt(i) + "</button>\n")

                                }
                                $("#do-qs-question-div .submitRecord").hide()

                            }

                        } else {
                            layer.msg("没有题目啦！", {icon: 2});
                        }
                    },
                    error: function (result) {
                    }
                })
            })
        }
    });
</script>