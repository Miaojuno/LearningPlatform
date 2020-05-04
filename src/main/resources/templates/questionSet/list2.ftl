<#import "/main/macros.ftl" as macros>
<#assign importCss=[]>
<#assign importJs=["/js/user/user.js","/js/user/superior.js"]>
<@macros.navhead importJs=importJs importCss=importCss titleName="题集"></@macros.navhead>

<div class="main-contain" style="margin-top: 1rem">

    <ul id="qs-tab" class="nav nav-tabs" role="tablist">
        <li class="active nav-item"><a href="#qss" data-toggle="tab" class="nav-link">我创建的题集</a></li>
    </ul>


    <div id="myTabContent" class="tab-content" style="margin-top: 2rem;">
        <div class="tab-pane fade in active" id="qss">

        </div>


    </div>

</div>

<div class="student-status-div" >

</div>


<script>
    $(function () {
        //默认显示第一个tab
        $('#qs-tab li:eq(0) a').tab('show');

        $(".student-status-div").hide()
        loadTab1()


        function loadTab1() {
            $.ajax({
                url: "/questionSet/findByOwner",
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
                            $("#qss").append("<div class='row qs-div' id='my-" + qsItem.questionSet.qsId + "'>" +
                                    "            <div class='qsPic col-3'>" +
                                    "                <img class='qsPic img-thumbnail'>" +
                                    "            </div>" +
                                    "            <div class='qs-right col-9'>\n" +
                                    "                <h4>" + qsItem.questionSet.qsName + "</h4>" +
                                    "                <div class='qs-info'>" +
                                    "                    <span>总题数：<span2>" + qsItem.questionSet.questionNumber + "</span2></span>" +
                                    "                </div>" +
                                    "            </div>" +
                                    "        </div>")
                            if (qsItem.questionSet.qsPic) {
                                $("#my-" + qsItem.questionSet.qsId + " .qsPic").attr("src", "data:image/jpeg;base64," + qsItem.questionSet.qsPic);
                            }
                            var size=qsItem.usersInfo.length
                            for (var j in qsItem.usersInfo){
                                var stuItem = qsItem.usersInfo[j]
                                $(".student-status-div").append("<div class=\"student-div row qsid-stu-"+qsItem.questionSet.qsId+"\" id=\"stuId-"+stuItem.userVo.userId+"\">" +
                                        "<p class=\"col-6\">"+stuItem.userVo.userName+"</p>\n" +
                                        "<p class=\"col-6\">完成数："+stuItem.count+"</p></div>")
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
        function mousePos(e) {
            //获取提示展示的坐标
            var e = e || window.event;
            return {
                x: e.clientX + document.body.scrollLeft + document.documentElement.scrollLeft,
                y: e.clientY + document.body.scrollTop + document.documentElement.scrollTop
            };
        }

        $(".qs-div").on("click",function () {
            $(".student-status-div").show()
            var mouse = mousePos();
            $(".student-status-div").css("left", mouse.x + 10 + 'px');
            $(".student-status-div").css("top", mouse.y + 10 + 'px');
            $(".student-status-div").css("display", "block");
            $(".student-status-div .student-div").hide()
            $(".qsid-stu-"+$(this).attr("id").substring(3)).show()
        })

        $(document).click(function(e){
            var _con = $('.qs-div');
            if(!_con.is(e.target) && _con.has(e.target).length === 0){
                $(".student-status-div").hide()
            };
        });

        $(".student-div").on("click",function () {
            window.open("/friendShip/main?userId=" + $(this).attr("id").substring(6))
        })
    });
</script>
<style>
    .student-div{
        cursor: pointer;
        width: 20rem;
        padding: 1rem;
        border-bottom: 1px solid #d9dde1;
    }
    .student-status-div{
        position: absolute;
        z-index: 99999;
        background-color: #f2f2f2;
        padding: 1rem;
        border-radius: 1rem;
        /*border: .3rem solid gray;*/
    }

    .qs-div {
        cursor: pointer;
    }
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