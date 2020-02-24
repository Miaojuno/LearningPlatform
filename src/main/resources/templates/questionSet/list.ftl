<#import "/main/macros.ftl" as macros>
<#assign importCss=[]>
<#assign importJs=["/js/user/user.js","/js/user/superior.js"]>
<@macros.navhead importJs=importJs importCss=importCss></@macros.navhead>

<div class="main-contain" style="margin-top: 1rem">
    <ul id="qs-tab" class="nav nav-tabs" role="tablist">
        <li class="active nav-item"><a href="#qss" data-toggle="tab" class="nav-link">我的题集</a></li>
        <li class="nav-item"><a href="#qss2" data-toggle="tab" class="nav-link">所有题集</a></li>
    </ul>


    <div id="myTabContent" class="tab-content" style="margin-top: 2rem;">
        <div class="tab-pane fade in active" id="qss">

        </div>

        <div class="tab-pane fade" id="qss2">

        </div>

    </div>


</div>

<script>
    $(function () {
        //默认显示第一个tab
        $('#qs-tab li:eq(0) a').tab('show');

        loadTab1()
        loadTab2()


        function loadTab1() {
            $.ajax({
                url: "/questionSet/findByUserAccount",
                data: {
                    "userAccount": $("#loginUserAccount").val()
                },
                dataType: "json",
                type: "post",
                async: false,
                success: function (result) {
                    if (result.success == true) {
                        $("#qss").empty()
                        for (var i in result.data) {
                            var qsItem = result.data[i]
                            $("#qss").append("<div class='row qs-div' id='my-" + qsItem.qsId + "'>" +
                                    "            <div class='qsPic col-3'>" +
                                    "                <img class='qsPic img-thumbnail'>" +
                                    "            </div>" +
                                    "            <div class='qs-right col-9'>\n" +
                                    "                <h4>" + qsItem.qsName + "</h4>" +
                                    "                <div class='qs-info'>" +
                                    "                    <span>创建者：<span2>" + qsItem.qsOwnerName + "</span2></span>" +
                                    "                    <span>已完成：<span2>" + qsItem.finishedNumber + "/" + qsItem.questionNumber + "</span2></span>" +
                                    "                </div>" +
                                    "                <button class='go-do-qs btn btn-primary'>去做题</button>" +
                                    "            </div>" +
                                    "        </div>")
                            if (qsItem.qsPic) {
                                $("#my-" + qsItem.qsId + " .qsPic").attr("src", "data:image/jpeg;base64," + qsItem.qsPic);
                            }
                        }


                    } else {
                        layer.msg(result.msg, {icon: 2});
                    }
                },
                error: function (result) {
                }
            })
        }

        function loadTab2() {
            $.ajax({
                url: "/questionSet/findAll",
                dataType: "json",
                type: "post",
                success: function (result) {
                    if (result.success == true) {
                        $("#qss2").empty()
                        for (var i in result.data) {
                            var qsItem = result.data[i]
                            $("#qss2").append("<div class='row qs-div' id='all-" + qsItem.qsId + "'>" +
                                    "            <div class='qsPic col-3'>" +
                                    "                <img class='qsPic img-thumbnail'>" +
                                    "            </div>" +
                                    "            <div class='qs-right col-9'>\n" +
                                    "                <h4>" + qsItem.qsName + "</h4>" +
                                    "                <div class='qs-info'>" +
                                    "                    <span>创建者：<span2>" + qsItem.qsOwnerName + "</span2></span>" +
                                    "                    <span>题目数：<span2>" + qsItem.questionNumber + "</span2></span>" +
                                    "            </div>" +
                                    "        </div>")
                            if ($("#my-" + qsItem.qsId).length > 0) {
                                $("#all-" + qsItem.qsId + " .qs-right").append("<button class='added-to-my-qs btn btn-primary' disabled>已在题集中</button>")
                            }
                            else {
                                $("#all-" + qsItem.qsId + " .qs-right").append("<button class='add-to-my-qs btn btn-primary'>添加到我的题集</button>")
                            }
                            if (qsItem.qsPic) {
                                $("#all-" + qsItem.qsId + " .qsPic").attr("src", "data:image/jpeg;base64," + qsItem.qsPic);
                            }
                        }


                    } else {
                        layer.msg(result.msg, {icon: 2});
                    }
                },
                error: function (result) {
                }
            })
        }

        //添加题集至我的题集
        $(document).on("click", ".add-to-my-qs", function () {
            var qsId = $(this).parent().parent().attr("id").substring(4);
            $.ajax({
                url: "/questionSet/addUser",
                dataType: "json",
                data: {
                    "userAccount": $("#loginUserAccount").val(),
                    "qsId": qsId
                },
                type: "post",
                success: function (result) {
                    if (result.success == true) {
                        $("#all-" + qsId + " .add-to-my-qs").hide()
                        $("#all-" + qsId + " .qs-right").append("<button class='added-to-my-qs btn btn-primary' disabled>已在题集中</button>")
                        loadTab1()
                    } else {
                        layer.msg(result.msg, {icon: 2});
                    }
                },
                error: function (result) {
                }
            })
        })

        $(document).on("click", ".go-do-qs", function () {
            var qsId = $(this).parent().parent().attr("id").substring(3);
            window.location.href = "/questionSet/doQuestionByQs?qsId=" + qsId;
        })

    });
</script>
<style>
    h4 {
        margin-top: 0.5rem;
    }

    span {
        font-size: 1.05rem;
        margin-right: 1.5rem;
    }

    span2 {
        font-size: 1rem;
        color: #4d555d;
    }

    .qs-info {
        margin-top: 1rem;
    }

    .go-do-qs {
        float: right;
        padding: 0.3rem 2rem;
        border-radius: 1rem;
        border: 0;
        background: rgba(240, 20, 20, .6);
        margin-bottom: 1rem;
    }

    .add-to-my-qs {
        float: right;
        padding: 0.3rem 1.5rem;
        border-radius: 1rem;
        color: #545c63;
        border-color: #9199a1;
        background: white;
        margin-bottom: 1rem;
    }

    .add-to-my-qs:hover {
        background: rgba(240, 20, 20, .6);
        border: 0;
    }

    .added-to-my-qs {
        float: right;
        padding: 0.3rem 1.5rem;
        border-radius: 1rem;
        color: #545c63;
        border: 0;
        background: white;
        margin-bottom: 1rem;
    }

    .qs-right {
        border-bottom: 1px solid #d9dde1;
    }

    .qs-div {
        margin-top: 1rem;
        padding-bottom: 1rem;
    }
</style>