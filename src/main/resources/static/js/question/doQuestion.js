$(function () {

    $(".diff-div").hide()
    $(".type-div").hide()


    //选择随机
    $("#btn-random").on("click", function () {
        $("#pointId").val("")
        $("#diff").val("")
        $("#type").val("")
        $("#doq-title").text("随机做题")
        $(".diff-div").hide()
        $(".type-div").hide()
        getRandomQuestion()
    })

    //选择难度
    $("#btn-diff").on("click", function () {
        $(".diff-div").show()
        $(".type-div").hide()
    })
    //选择具体难度
    $("#btn-diff1").on("click", function () {
        $("#pointId").val("")
        $("#type").val("")
        $("#diff").val("1")
        $("#doq-title").text("简单难度")
        getRandomQuestion()
    })
    $("#btn-diff2").on("click", function () {
        $("#pointId").val("")
        $("#type").val("")
        $("#diff").val("2")
        $("#doq-title").text("普通难度")
        getRandomQuestion()
    })
    $("#btn-diff3").on("click", function () {
        $("#pointId").val("")
        $("#type").val("")
        $("#diff").val("3")
        $("#doq-title").text("困难难度")
        getRandomQuestion()
    })

    //选择类型
    $("#btn-type").on("click", function () {
        console.log(1)
        $(".diff-div").hide()
        $(".type-div").show()
    })
    //选择具体类型
    $("#btn-type1").on("click", function () {
        $("#pointId").val("")
        $("#type").val("主观题")
        $("#diff").val("")
        $("#doq-title").text("主观题")
        getRandomQuestion()
    })
    $("#btn-type2").on("click", function () {
        $("#pointId").val("")
        $("#type").val("客观题")
        $("#diff").val("")
        $("#doq-title").text("客观题")
        getRandomQuestion()
    })
    $("#btn-type3").on("click", function () {
        $("#pointId").val("")
        $("#type").val("选择题")
        $("#diff").val("")
        $("#doq-title").text("选择题")
        getRandomQuestion()
    })

    //按照知识点做题时，隐藏tab栏
    var url = location.search;
    var nodeId;
    if (url.indexOf("?") != -1) {
        //知识点
        if(url.split("=")[0]=="?nodeId"){
            $(".choose-div").hide()
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
                    $("#pointDetail").text("知识点：" + res.data.pointDetail)
                    $("#pointId").val(res.data.pointId)
                    getRandomQuestion()
                },
                error: function (err) {
                    layer.msg("失败", {icon: 2});
                }
            });
        }
        //按照难度
        else if(url.split("=")[0]=="?diff"){
            var diff=unescape(url.split("=")[1])
            $("#btn-diff").click()
            $("#btn-diff"+diff.substring(diff.length-1)).click()
        }
        //按照类型
        else if(url.split("=")[0]=="?type"){
            $("#btn-type").click()
            var type=unescape(url.split("=")[1])
            if(type=="主观题"){
                $("#btn-type1").click()
            }
            else if(type=="客观题"){
                $("#btn-type2").click()
            }
            else if(type=="选择题"){
                $("#btn-type3").click()
            }
        }

    }


    // //默认显示第一个tab
    // $('#doQuetsion-tab li:eq(0) a').tab('show');
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
            else{
                if($("#random .userSolution").val() == undefined || $("#random .userSolution").val() == null
                    || $("#random .userSolution").val().trim()==""){
                    layer.msg("请输入答案", {icon: 2});
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
                    "pointId": $("#pointId").val(),
                    "diff": $("#diff").val(),
                    "type": $("#type").val()
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