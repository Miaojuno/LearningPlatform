$(function () {
    var url = location.search;
    var qsId;
    if (url.indexOf("?") != -1) {
        qsId = url.split("=")[1];
        initBadges()
    }

    function initBadges() {
        $.ajax({
            type: "post",
            url: "/questionSet/findQsRecordByUser",
            data: {
                "userAccount": $("#loginUserAccount").val(),
                "qsId": qsId
            },
            success: function (res) {
                $(".qsName").text(res.data.qsName);
                if (res.data.unfinishedQuestionIds != null) {
                    var unfinishedList = res.data.unfinishedQuestionIds.replace(/"/g, "").split("、");
                }
                else {
                    var unfinishedList = [];
                }
                var allList = res.data.questionIds.split("、");
                var num = 1;
                for (i = 0, len = allList.length; i < len; i++) {
                    //已完成的题目
                    if (!unfinishedList.includes(allList[i])) {
                        $(".badges-div").append("<span class='badge finished' id='question-" + allList[i] + "'>" + num + "</span>")
                    }
                    //未完成的题目
                    else {
                        $(".badges-div").append("<span class='badge unfinished' id='question-" + allList[i] + "'>" + num + "</span>")
                    }
                    num++;
                }
                $('.badges-div .badge').eq(0).attr('id').substring(9)
                getQuestion($('.badges-div .badge').eq(0).attr('id').substring(9))
                $('.badges-div .badge').eq(0).addClass("choosed")
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

    //选择上方题号
    $(document).on('click', '.badges-div .badge', function () {
        getQuestion($(this).attr('id').substring(9))
        $(".badges-div .badge").removeClass("choosed")
        $(this).addClass("choosed")
    });

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
                    layer.msg("提交成功", {icon: 1});
                    $("#do-qs-question-div .file").val("");
                    $("#do-qs-question-div .fileInput").val("");
                    $("#do-qs-question-div .userSolution").val("");
                    $("#do-qs-question-div .optionsDiv").empty();



                    $("#question-" + $(".questionId").val()).removeClass("unfinished")
                    $("#question-" + $(".questionId").val()).addClass("finished")

                    //不是最后一题时，自动跳转至下一个节点
                    if($("#question-" + $(".questionId").val()).next().length!=0){
                        $("#question-" + $(".questionId").val()).next().click()
                    }
                    //是最后一题时，刷新页面为已做题
                    else{
                        getAnswer($(".questionId").val())
                    }

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
        $(".questionId").val(questionId)
        layui.use('layer', function () {
            layer = layui.layer;
            $.ajax({
                url: "/neo/findByQuestionId",
                async: false,
                data: {
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
        getAnswer(questionId)
    }

    function getAnswer(questionId) {
        layui.use('layer', function () {
            layer = layui.layer;
            $.ajax({
                url: "/record/findByUserAccountAndQuestionId",
                data: {
                    "questionId": questionId,
                    "userAccount": $("#loginUserAccount").val()
                },
                dataType: "json",
                type: "post",
                success: function (result) {
                    if (result.success == true) {//做过
                        $(".type1").hide()
                        $(".type2").show()
                        if (result.data.userSolution.length > 10) {
                            $(".type2 .userOldSolution").attr("rows", "5")
                        }
                        $(".type2 .userOldSolution").val(result.data.userSolution);
                        if (result.data.recordUserPic != null && result.data.recordUserPic != "") {
                            $(".type2 .recordUserPic").show()
                            $(".type2 .recordUserPic").attr("src", "data:image/jpeg;base64," + result.data.recordUserPic);
                        }
                        else {
                            $(".type2 .recordUserPic").hide()
                        }
                        $(".card-footer").hide()

                    } else {//没做过
                        $(".type1").show()
                        $(".type2").hide()
                        $(".card-footer").show()
                    }
                },
                error: function (result) {
                }
            })
        })
    }

});