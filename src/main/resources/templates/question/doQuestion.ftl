<#import "/main/macros.ftl" as macros>
<#assign importCss=[]>
<#assign importJs=[]>
<@macros.navhead importJs=importJs importCss=importCss></@macros.navhead>

<div class="main-contain" style="margin-top: 1rem">
    <ul id="doQuetsion-tab" class="nav nav-tabs" role="tablist">
        <li class="active nav-item"><a href="#random" data-toggle="tab" class="nav-link">随机做题</a></li>
        <li class="nav-item"><a href="#recommend" data-toggle="tab" class="nav-link">为你精选</a></li>
    </ul>

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
                        <textarea rows="5" class="userSolution" style="width: 100%"
                                  placeholder="在这里输入你的答案。。。"></textarea>
                        <input type="file" class="file" id="randomFile" hidden>
                        <input type="text" class="fileInput form-control" placeholder="上传jpg图片">
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
            <p>iOS 是一个由苹果公司开发和发布的手机操作系统。最初是于 2007 年首次发布 iPhone、iPod Touch 和 Apple
                TV。iOS 派生自 OS X，它们共享 Darwin 基础。OS X 操作系统是用在苹果电脑上，iOS 是苹果的移动版本。</p>
        </div>

    </div>
</div>


<script>
    $(function () {
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


        $('#random .changeRandom').on('click', function () {
            var qid = $('#random .questionId').val();
            var number = 0;
            getRandomQuestion()
            console.log(qid)
            console.log($('#random .questionId').val())
            while (qid == $('#random .questionId').val() && number < 10) {
                console.log(qid)
                console.log($('#random .questionId').val())
                getRandomQuestion();
                number++;
            }
            if (number == 10) {
                layer.msg("没有题目啦！", {icon: 2});
            }
        });

        $('#random .submitRecord').on('click', function () {
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
                    if (name[name.length - 1] != "jpg") {
                        layer.confirm("导入失败,只支持jpg格式文件", {icon: 2}, function (index) {
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
                        getRandomQuestion()

                    },
                    error: function (err) {
                        layer.msg("失败", {icon: 2});
                        $("#random .file").val("");
                        $("#random .fileInput").val("");
                        $("#random .userSolution").val("");
                    }
                });

            })
        });


        //加载随机题目
        function getRandomQuestion() {
            layui.use('layer', function () {
                layer = layui.layer;
                $.ajax({
                    url: "/neo/getRandomQuestion",
                    data: {},
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
                                $("#random .userSolution").attr("rows","5");
                            }
                            else {
                                $("#random .fileInput").hide();
                                $("#random .userSolution").attr("rows","1");

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