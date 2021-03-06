<#import "/main/macros.ftl" as macros>
<#assign importCss=[]>
<#assign importJs=[]>
<@macros.navhead importJs=importJs importCss=importCss titleName="判题"></@macros.navhead>

<div class="main-contain" style="margin-top: 1rem">

    <div class="card" id="questionReviewDiv">
        <input class="recordId" hidden>
        <div class="card-header">
            <div class="userShow" style="position: absolute;right: 1rem;top: 0.5rem;"></div>
            <p class="date" style="position: absolute;right: 1rem;top: 1.5rem;"></p>
            <h5>原题：</h5>
            <img class="questionPic img-thumbnail">
            <p class="questionDetail"></p>
        </div>
        <div class="card-body">
            <h5>参考答案：</h5>
            <p class="solution"></p>
            <img class="sulutionPic img-thumbnail">
            <h5>用户输入答案：</h5>
            <textarea rows="5" class="userSolution" style="width: 100%" disabled></textarea>
            <img class="recordUserPic img-thumbnail">
        </div>
        <div class="card-footer">
            <input class="score form-control" placeholder="在这里输入你的打分">
            <button class="submitScore btn btn-primary" style="margin-top: 1rem; margin-left: 1rem;">确认
            </button>
        </div>
    </div>
</div>


<script>
    $(function () {

        getOneUnreviewed()

        $('#questionReviewDiv .submitScore').on('click', function () {
            layui.use('layer', function () {
                layer = layui.layer;
                var regx = /(^[0-9]+$)/
                if ($(".score").val().trim() == "") {
                    layer.msg("请打分", {icon: 2});
                    return
                }
                if (!regx.test($(".score").val())) {
                    layer.msg("请输入整数分值", {icon: 2});
                    return
                }

                $.ajax({
                    type: "post",
                    url: "/record/updateScore",
                    data: {
                        "recordId": $("#questionReviewDiv .recordId").val(),
                        "score": $("#questionReviewDiv .score").val()
                    },
                    dataType: "json",
                    success: function (res) {
                        layer.msg("ok", {icon: 1});
                        $("#questionReviewDiv .score").val("");
                        getOneUnreviewed()

                    },
                    error: function (err) {
                        layer.msg("失败", {icon: 2});
                        $("#questionReviewDiv .score").val("");
                    }
                });

            })
        });

        // $("#questionReviewDiv .questionPic").on("click",function () {
        //     $("#qrUserShow .userId").val("123");
        // })

        //加载一题待批改题目
        function getOneUnreviewed() {
            layui.use('layer', function () {
                layer = layui.layer;
                $.ajax({
                    url: "/record/getOneUnreviewed",
                    data: {"userAccount": $("#loginUserAccount").val()},
                    async: false,
                    dataType: "json",
                    type: "post",
                    success: function (result) {
                        if (result.success == true) {
                            $("#questionReviewDiv").show();
                            $("#questionReviewDiv .recordId").val(result.data.recordId);
                            $("#questionReviewDiv .date").val(result.data.date);
                            $.ajax({
                                url: "/user/findbyid",
                                dataType: "json",
                                data: {
                                    "userId": result.data.userId
                                },
                                type: "post",
                                success: function (result2) {
                                    if (result2.success == true) {
                                        $("#questionReviewDiv .userShow").append("<p>来自："+result2.data.userName+"</p>")
                                    }
                                },
                                error: function (result) {
                                }
                            })
                            if (result.data.questionDetail == null || result.data.questionDetail == "") {
                                $("#questionReviewDiv .questionDetail").hide()
                            }
                            else {
                                $("#questionReviewDiv .questionDetail").show()
                                $("#questionReviewDiv .questionDetail").html(result.data.questionDetail);
                            }

                            if (result.data.questionPic == null) {
                                $("#questionReviewDiv .questionPic").hide()
                            }
                            else {
                                $("#questionReviewDiv .questionPic").show()
                                $("#questionReviewDiv .questionPic").attr("src", "data:image/jpeg;base64," + result.data.questionPic);
                            }

                            if (result.data.solution == null || result.data.solution == "") {
                                $("#questionReviewDiv .solution").hide()
                            }
                            else {
                                $("#questionReviewDiv .solution").show()
                                $("#questionReviewDiv .solution").html(result.data.solution);
                            }

                            if (result.data.sulutionPic == null) {
                                $("#questionReviewDiv .sulutionPic").hide()
                            }
                            else {
                                $("#questionReviewDiv .sulutionPic").show()
                                $("#questionReviewDiv .sulutionPic").attr("src", "data:image/jpeg;base64," + result.data.sulutionPic);
                            }

                            if (result.data.userSolution == null || result.data.userSolution == "") {
                                $("#questionReviewDiv .userSolution").hide()
                            }
                            else {
                                $("#questionReviewDiv .userSolution").show()
                                $("#questionReviewDiv .userSolution").html(result.data.userSolution);
                            }

                            if (result.data.recordUserPic == null) {
                                $("#questionReviewDiv .recordUserPic").hide()
                            }
                            else {
                                $("#questionReviewDiv .recordUserPic").show()
                                $("#questionReviewDiv .recordUserPic").attr("src", "data:image/jpeg;base64," + result.data.recordUserPic);
                            }


                        } else {
                            layer.msg("没有待审批题目啦！", {icon: 2});
                            $("#questionReviewDiv").hide()
                        }
                    },
                    error: function (result) {
                    }
                })
            })
        }
    })

</script>

