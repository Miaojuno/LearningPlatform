<#import "/main/macros.ftl" as macros>
<#assign importCss=[]>
<#assign importJs=[]>
<@macros.navhead importJs=importJs importCss=importCss></@macros.navhead>

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


<script>
    $(function () {
        var url = location.search;
        var nodeId;
        if (url.indexOf("?") != -1) {
            nodeId = url.split("=")[1];
            $("#doQuetsion-tab").hide();
            $("#pointDetail").show();
            $.ajax({
                type: "post",
                url: "/neo/findPointById",
                data: {
                    "id": nodeId
                },
                success: function (res) {
                    $("#pointDetail").text("知识点："+res.data.pointDetail)
                    $("#pointId").val(res.data.pointId)
                },
                error: function (err) {
                    layer.msg("失败", {icon: 2});
                }
            });
        }


        //默认显示第一个tab
        $('#doQuetsion-tab li:eq(0) a').tab('show');
        //随机加载题目
        getRandomQuestion()

        $('#random .fileInput').on('click', function () {
            $("#random .file").click()
        });

        $("#random .file").bind("input propertychange", function () {
            $('#random .fileInput').val($('#random .file').val())
        });

        //换一题
        $('#random .changeRandom').on('click', function () {
            var qid = $('#random .questionId').val();
            var number = 0;
            getRandomQuestion()
            while (qid == $('#random .questionId').val() && number < 10) {
                getRandomQuestion();
                number++;
            }
            if (number == 10) {
                layer.msg("没有题目啦！", {icon: 2});
            }
        });

        //提交
        $('#random .submitRecord').on('click', function () {
            submitRecord()
        });

        //选择题选择答案
        $(document).on('click', '#random .optionsDiv .btn', function () {
            $("#random .userSolution").val($(this).text())
            submitRecord()
        })

        // 提交方法
        function submitRecord() {
            layui.use('layer', function () {
                layer = layui.layer;
                var fileObj = document.getElementById('randomFile').files[0]; // js 获取文件对象
                var url = "/record/add"; // 接收上传文件的后台地址
                var form = new FormData();  // FormData 对象
                form.append("questionId", $("#random .questionId").val())
                form.append("userSolution", $("#random .userSolution").val())
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
                        $("#random .file").val("");
                        $("#random .fileInput").val("");
                        $("#random .userSolution").val("");
                        $("#random .optionsDiv").empty();
                        getRandomQuestion()

                    },
                    error: function (err) {
                        layer.msg("失败", {icon: 2});
                        $("#random .file").val("");
                        $("#random .fileInput").val("");
                        $("#random .userSolution").val("");
                        $("#random .optionsDiv").empty();
                    }
                });

            })
        }


        //加载随机题目
        function getRandomQuestion() {
            layui.use('layer', function () {
                layer = layui.layer;
                $.ajax({
                    url: "/neo/getRandomQuestion",
                    data: {
                        "pointId":$("#pointId").val()
                    },
                    async: false,
                    dataType: "json",
                    type: "post",
                    success: function (result) {
                        if (result.success == true) {
                            $("#random .questionId").val(result.data.questionId);
                            if (result.data.questionDetail == null || result.data.questionDetail == "") {
                                $("#random .questionDetail").hide()
                            }
                            else {
                                $("#random .questionDetail").show()
                                $("#random .questionDetail").html(result.data.questionDetail);
                            }

                            if (result.data.pic == null) {
                                $("#random .questionPic").hide()
                            }
                            else {
                                $("#random .questionPic").show()
                                $("#random .questionPic").attr("src", "data:image/jpeg;base64," + result.data.pic);
                            }

                            if (result.data.type == "主观题") {
                                $("#random .fileInput").show();
                                $("#random .userSolution").show();
                                $("#random .userSolution").attr("rows", "5");
                                $("#random .optionsDiv").empty();
                                $("#random .submitRecord").show()
                            }
                            else if (result.data.type == "客观题") {
                                $("#random .fileInput").hide();
                                $("#random .userSolution").show();
                                $("#random .userSolution").attr("rows", "1");
                                $("#random .optionsDiv").empty();
                                $("#random .submitRecord").show()

                            }
                            else {
                                $("#random .fileInput").hide();
                                $("#random .optionsDiv").empty()
                                $("#random .userSolution").hide();
                                var options = result.data.solution.split("/")[1];
                                for (i = 0; i < options.length; i++) {
                                    $("#random .optionsDiv").append("    <button type=\"button\" class=\"btn btn-primary\" " +
                                            // "onclick=\"optionSubmitRecord(" + options.charAt(i) + ");\">" + options.charAt(i) + "</button>\n")
                                            "\">" + options.charAt(i) + "</button>\n")

                                }
                                $("#random .submitRecord").hide()

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