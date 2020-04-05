<#--导航栏-->
<#macro navhead importJs=[] importCss=[] titleName="首页">
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <title>${titleName}-学习平台</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <script src="/webjars/jquery/3.1.1/jquery.min.js"></script>
        <script src="/webjars/popper.js/1.14.4/umd/popper.js"></script>
        <script src="/webjars/bootstrap/4.3.1/js/bootstrap.min.js"></script>
        <script src="/webjars/bootstrap-table/1.9.1-1/bootstrap-table.js"></script>
        <script src="/webjars/echarts/4.6.0/echarts.min.js"></script>
        <script src="/webjars/bootstrap-table/1.9.1-1/locale/bootstrap-table-zh-CN.js"></script>
        <script src="/webjars/layui/2.5.5/layui.js"></script>
        <link rel="stylesheet" type="text/css" href="/webjars/bootstrap/4.3.1/css/bootstrap.min.css"/>
        <link rel="stylesheet" type="text/css" href="/webjars/bootstrap-table/1.9.1-1/bootstrap-table.css">
        <link rel="stylesheet" href="/css/main/main.css">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <#if importJs??>
            <#list importJs as js>
        <script src="${js}"></script>
            </#list>
        </#if>
        <#if importCss??>
            <#list importCss as css>
                <link rel="stylesheet" href="${css}">
            </#list>
        </#if>
    </head>
    <body>

    <div>
        <nav class="navbar navbar-expand-md bg-dark navbar-dark">
            <a class="navbar-brand" href="/index" style="font-size: 1.8rem;">网上学习系统</a>

            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#collapsibleNavbar">
                <span class="navbar-toggler-icon"></span>
            </button>

            <div class="collapse navbar-collapse justify-content-end" id="collapsibleNavbar">
                <ul class="navbar-nav">
                    <li class="nav-item dropdown">
                        <#if Session["loginUserRole"] != "学生">
                        <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown">
                            管理
                        </a>
                        </#if>
                        <div class="dropdown-menu">
                        <#--教师、领导、管理员-->
                            <a class="dropdown-item" target="_blank" href="/user/list">用户管理</a>
                        <#--管理员-->
                            <#if Session["loginUserRole"] == "管理员">
                                <a class="dropdown-item" target="_blank" href="/role/list">角色管理</a>
                            </#if>
                        </div>

                    </li>



                <#--教师、领导-->
                    <#if Session["loginUserRole"] != "管理员" && Session["loginUserRole"] != "学生">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown">
                            审核
                        </a>
                        <div class="dropdown-menu">
                            <a class="dropdown-item" target="_blank" href="/apply/superiorApplyReview">上级变更审核</a>
                        </div>
                    </li>
                    </#if>

                <#--学生-->
                    <#if Session["loginUserRole"] == "学生">
                    <li class="nav-item">
                        <a class="nav-link" target="_blank" href="/record/doQuestion">去做题</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" target="_blank" href="/record/list">做题记录</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" target="_blank" href="/questionSet/list">我的题集</a>
                    </li>
                    </#if>

                    <li class="nav-item">
                        <a class="nav-link" target="_blank" href="/neo4jShow/pointShow">知识点查看</a>
                    </li>

                <#--教师-->
                    <#if Session["loginUserRole"] == "教师">
                    <li class="nav-item">
                        <a class="nav-link" target="_blank" href="/record/questionReview">去审阅</a>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown">
                            题目题集
                        </a>
                        <div class="dropdown-menu">
                            <a class="dropdown-item" target="_blank" href="/neo/addQuestion">添加题目</a>
                            <a class="dropdown-item" target="_blank" href="/questionSet/addQuestionSet">添加题集</a>
                        </div>
                    </li>
                    </#if>

                    <li class="nav-item">
                        <a class="nav-link" target="_blank" href="/neo/excelupload">数据导入</a>
                    </li>
                <#--<li class="nav-item">-->
                <#--<a class="nav-link" href="/neo/questionupload">题目导入</a>-->
                <#--</li>-->
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown"
                           style="padding-top: 0;padding-bottom: 0;">
                            <img class="img-fluid rounded" id="userPicInNavHead" style="height: 2.2rem;width: 2.2rem;">
                            <span4msg class="msgTip spanInPic">NEW</span4msg>
                        </a>
                        <div class="dropdown-menu" style="left: -4.8rem;">
                        <#--学生、教师、领导-->
                            <#if Session["loginUserRole"] != "管理员">
                            <a class="dropdown-item" href="#" id="roleModifyApplyBtn">角色变更申请</a>
                            <a class="dropdown-item" href="#" id="supeiorModifyApplyBtn">上级变更申请</a>
                            </#if>
                            <a class="dropdown-item" href="#" id="userPicModifyBtn">修改头像</a>
                            <a class="dropdown-item" target="_blank" href="/friendShip/main">我的消息<span4msg class="msgTip spanInTab">NEW</span4msg></a>
                            <a class="dropdown-item" href="/user/logout">注销</a>
                        </div>
                    </li>
                </ul>
            </div>
        </nav>
    </div>

    <#--当前登录用户账户及角色-->
    <input id="loginUserAccount" value="${Session ["loginUserAccount"]}" hidden>
    <input id="loginUserRole" value="${Session ["loginUserRole"]}" hidden>
    </body>

    <#--头像修改遮罩层-->
    <div id="modify-userPic-div" class="maskLayer" style="display: none;">
        <div class="modal-body">
            <div class="row">
                <input type="file" id="user-pic-modify-file" hidden>
            </div>
        </div>
    </div>


    <#if Session["loginUserRole"] != "管理员">
    <#--角色修改申请遮罩层-->
        <div id="role-modify-apply-div" class="maskLayer" style="display: none;">
            <div class="modal-body">
                <form id="role-modify-apply-form">
                    <div class="row">
                        <label class="control-label col-3 text-right" for="">角色</label>
                        <div class="col-8">
                            <select id="role-modify-apply-select" name="roleId" class=" form-control">
                                <option value="">---请选择---</option>
                            </select>
                        </div>
                    </div>
                    <div class="row">
                        <label class="control-label col-3 text-right" for="">备注</label>
                        <div class="col-8">
                            <textarea rows="3" name="reason" class="form-control reason" autocomplete="off"></textarea>
                        </div>
                    </div>
                </form>
            </div>
        </div>



    <#--上级修改申请遮罩层-->
        <div id="superior-modify-apply-div" class="maskLayer" style="display: none;">
            <div class="modal-body">
                <div class="row">
                    <label class="control-label col-3 text-right" for="">上级</label>
                    <div class="col-8" hidden>
                        <input name="newId" class="form-control newId" autocomplete="off">
                    </div>
                    <div class="col-8">
                        <input name="newId" class="form-control newName" autocomplete="off">
                    </div>
                </div>
                <div class="row">
                    <label class="control-label col-3 text-right" for="">备注</label>
                    <div class="col-8">
                        <textarea rows="3" name="reason" class="form-control reason" autocomplete="off"></textarea>
                    </div>
                </div>
            </div>
        </div>

    <#--上级选择遮罩层-->
        <div id="choose-superior-div" class="maskLayer" style="display: none;">
            <div class="modal-body">
                <form id="choose-superior-form">
                    <div class="row" hidden>
                        <label class="control-label col-3 text-right" for="">id</label>
                        <div class="col-8">
                            <input type="text" name="userId" class="subordinateId form-control userId"
                                   autocomplete="off">
                        </div>
                    </div>
                    <div class="row">
                        <label class="control-label col-3 text-right" for="">姓名</label>
                        <div class="col-8">
                            <input type="text" name="userName" class="searchSuperiorName form-control">
                        </div>
                    </div>
                </form>
                <div class="tablediv m-auto">
                <#--内容表格-->
                    <table id="choose-superior-table"></table>
                </div>
            </div>
        </div>
    </#if>


    </html>
    <script src="/js/main/main.js"></script>
</#macro>




















<#--role选择组件-->
<#macro roleSelect id name>
    <select id="${id}" name="${name}" class=" form-control">
        <option value="">---请选择---</option>
    </select>

    <script>
        $.ajax({
            url: "/role/listActive",
            dataType: "json",
            type: "post",
            success: function (result) {
                for (i in result) {
                    $("#${id}").append("<option value='" + result[i].roleId + "' >" + result[i].roleName + "</option>");
                }
            },
            error: function (result) {
            }
        })
    </script>
</#macro>




















<#--状态选择组件-->
<#macro statusSelect id name>
    <select id="${id}" name="${name}" class=" form-control">
        <option value="">---请选择---</option>
        <option value="申请中">申请中</option>
        <option value="通过">通过</option>
        <option value="拒绝">拒绝</option>
    </select>
</#macro>













<#--用户选择组件
type:single代表单选,multiple代表多选,默认单选-->
<#macro userChoose id class name type="single">
    <input id="${id}" name="${name}" class="${class} macroUserIds" hidden>
    <#if type=="single">
        <input type="text" class="form-control userNames" placeholder="">
    <#elseif type=="multiple">
        <textarea rows="5" class="userNames" placeholder=""></textarea>
    </#if>

<#--用户选择遮罩层-->
    <div id="userChoose-div" class="maskLayer" style="display: none;">
        <div class="modal-body">
            <form id="userChoose-form">
                <div class="row">
                    <label class="control-label col-4 text-right" for="">用户名</label>
                    <div class="col-8">
                        <input type="text" class="userName form-control">
                    </div>
                </div>
            </form>
            <div class="tablediv m-auto">
            <#--内容表格-->
                <table id="userChoose-table"></table>
            </div>
            <#if type=="multiple">
                <div class="choosed-users">
                    已选择用户：
                </div>
            </#if>
        </div>
    </div>

    <script>
        $(function () {
            if (!/(iPhone|iPad|iPod|iOS|Android)/i.test(navigator.userAgent)) { //非移动端
                $("#userChoose-div").css("max-height", $(window).height() * 0.8)
            }

            //表格
            $('#userChoose-table').bootstrapTable({
                url: '/user/pageList.json',
                pagination: true,           //分页
                sidePagination: "server",   //服务端处理分页
                pageNumber: 1,              //初始化加载第一页，默认第一页
                pageSize: 5,               //每页的记录行数（*）
                pageList: [5],        //可供选择的每页的行数（*）
                uniqueId: "userId",         //隐藏的id
                // queryParamsType:'',
                queryParams: function (params) {
                    var temp = {
                        pageNumber: (params.offset / params.limit) + 1,     //页数
                        pageSize: params.limit,                             //每页的记录行数
                        userName: $(".userName").val(),
                        roleId: "8a8181816e209f02016e209f20330001",//学生的id
                        superiorAccount: $("#loginUserAccount").val()
                    };
                    if ($("#loginUserRole").val() == "管理员") {
                        temp.superiorAccount = ""
                    }
                    else {
                        if ($("#loginUserRole").val() == "领导") {
                            temp.roleName = "教师"
                        }
                        else if ($("#loginUserRole").val() == "教师") {
                            temp.roleName = "学生"
                        }
                    }
                    return temp;
                },
                responseHandler: function (res) {
                    return {
                        "total": res.total,//总条目数
                        "rows": res.data //数据
                    }
                },
                columns: [
                    {
                        field: 'userAccount',
                        title: '账户'
                    }, {
                        field: 'userName',
                        title: '姓名'
                    }, {
                        field: 'userDesc',
                        title: '介绍'
                    }, {
                        field: 'action',
                        title: '操作',
                        formatter: actionFormatter
                    },]
            });

            //操作栏的格式化
            function actionFormatter(value, row, index) {
                var id = value;
                var result = "";
                result += "<a href='javascript:;'  class='chooseUser' title='选择'><span>选择</span></a>";
                return result;
            }

            var layerPointChoose;

            //打开上级设置遮罩层
            $(document).on("click", ".userNames", function () {
                $("#userChoose-table").bootstrapTable('refresh');
                layui.use('layer', function () {
                    layer = layui.layer;

                    layerUserChoose = layer.open({
                        type: 1,
                        zIndex: "1",
                        title: '选择用户',
                        // area: ['800px'],
                        content: $('#userChoose-div'),
                        <#if type=="multiple">
                            btn: ['全部','确定'],
                            yes: function (index) {
                                $.ajax({
                                    url: "/user/pageList.json",
                                    data: {
                                        pageNumber: 1,
                                        pageSize: 999,
                                        userName: $(".userName").val(),
                                        roleId: "8a8181816e209f02016e209f20330001",//学生的id
                                        superiorAccount: $("#loginUserAccount").val()
                                    },
                                    dataType: "json",
                                    type: "post",
                                    success: function (result) {
                                        var ids = result.data[0].userId;
                                        var names = result.data[0].userName;
                                        for (i = 1; i < result.data.length; i++) {
                                            ids = ids + "、" + result.data[i].userId;
                                            names = names + "、" + result.data[i].userName;
                                        }
                                        $(".macroUserIds").val(ids);
                                        $(".userNames").val(names);
                                        layer.close(index);
                                    },
                                    error: function (result) {
                                    }
                                })
                            },
                            btn2: function (index) {
                                var allUser = $(".choosed-users").children();
                                if (allUser.length == 0) {
                                    layer.msg("请选择至少一个用户", {icon: 2, time: 2000});
                                    return;
                                }
                                var ids = allUser.eq(0).attr("id").substring(5);
                                var names = allUser.eq(0).text().replace(" ✖", "");
                                for (i = 1; i < allUser.length; i++) {
                                    ids = ids + "、" + allUser.eq(i).attr("id").substring(5);
                                    names = names + "、" + allUser.eq(i).text().replace(" ✖", "");
                                }
                                $(".macroUserIds").val(ids);
                                $(".userNames").val(names);
                                layer.close(index);
                            },
                        </#if>
                        cancel: function (index) {
                            layer.close(index);
                            $(".userName").val("")
                        }
                    })
                })
            })

            $(document).on("click", ".chooseUser", function () {
                var id = $(this).parent().parent().attr("data-uniqueid");
                var name = $(this).closest("tr").find("td").eq(1).text();
                    <#if type=="single">
                        $(".macroUserIds").val(id);
                        $(".userNames").val(name);
                        layer.close(layerUserChoose);
                    <#elseif type=="multiple">
                        layui.use('layer', function () {
                            layer = layui.layer;
                            if ($("#user-" + id).length > 0) {
                                layer.msg("已添加过", {icon: 2, time: 500});
                            }
                            else {
                                $(".choosed-users").append("<span class=\"choosed-user badge badge-info\" " +
                                        "id=\"user-" + id + "\">" + name + " ✖</span>")
                                layer.msg("添加成功", {icon: 1, time: 500});
                            }
                        })
                    </#if>
            })

            //输入时刷新表格
            $(document).on("input", "#userChoose-div .userName", function () {
                $("#userChoose-table").bootstrapTable('refresh');
            })

            //点击删除已经选择的用户
            $(document).on("click", ".choosed-users .choosed-user", function () {
                $(this).remove()
                layui.use('layer', function () {
                    layer.msg("删除成功", {icon: 1, time: 500});

                })
            })


        })
    </script>

    <style>
        .choosed-users {
            border-radius: .25rem;
            border: 1px solid #ced4da;
            min-height: 5rem;
        }

        .choosed-users span {
            margin: 0.3rem;
        }

        .choosed-users span:hover {
            cursor: pointer
        }
    </style>

</#macro>
















<#--知识点选择组件，展示知识点内容，隐藏框内容为id-->
<#macro pointChoose id class name>
    <input type="text" class="macroPointChoseInput form-control" placeholder="">
    <input type="text" id="${id}" name="${name}" class="${class} macroPointChoseId form-control" placeholder="" hidden>

<#--选择知识点遮罩层-->
    <div id="pointChoose-div" class="maskLayer" style="display: none;">
        <div class="modal-body">
            <form id="pointChoose-form">
                <div class="row">
                    <label class="control-label col-4 text-right" for="">知识点</label>
                    <div class="col-8">
                        <input type="text" class="pointDetail form-control">
                    </div>
                </div>
            </form>
            <div class="tablediv m-auto">
            <#--内容表格-->
                <table id="pointChoose-table"></table>
            </div>
        </div>
    </div>

    <script>
        $(function () {
            if (!/(iPhone|iPad|iPod|iOS|Android)/i.test(navigator.userAgent)) { //非移动端
                $("#pointChoose-div").css("max-height", $(window).height() * 0.6)
            }

            //表格
            $('#pointChoose-table').bootstrapTable({
                url: '/neo/findPointByDetail.json',
                pagination: false,           //分页
                uniqueId: "pointId",         //隐藏的id
                // queryParamsType:'',
                queryParams: function (params) {
                    var temp = {
                        pointDetail: $("#pointChoose-div .pointDetail").val(),
                    };
                    return temp;
                },
                columns: [
                    {
                        field: 'pointDetail',
                        title: '知识点'
                    }, {
                        field: 'action',
                        title: '操作',
                        formatter: actionFormatter
                    },]
            });

            //操作栏的格式化
            function actionFormatter(value, row, index) {
                var id = value;
                var result = "";
                result += "<a href='javascript:;'  class='choosePoint' title='选择'><span>选择</span></a>";
                return result;
            }

            var layerPointChoose;

            //打开上级设置遮罩层
            $(document).on("click", ".macroPointChoseInput", function () {
                $("#pointChoose-table").bootstrapTable('refresh');
                layui.use('layer', function () {
                    layer = layui.layer;

                    layerPointChoose = layer.open({
                        type: 1,
                        zIndex: "1",
                        title: '选择知识点',
                        // area: ['800px'],
                        content: $('#pointChoose-div'),
                        cancel: function (index) {
                            layer.close(index);
                            $("#superior-modify-form")[0].reset()
                        }
                    })
                })
            })

            $(document).on("click", ".choosePoint", function () {
                var id = $(this).parent().parent().attr("data-uniqueid");
                $(".macroPointChoseId").val(id)
                $(".macroPointChoseInput").val($(this).closest("tr").find("td").eq(0).text())
                layer.close(layerPointChoose)
            })

            //输入时刷新表格
            $(document).on("input", "#pointChoose-div .pointDetail", function () {
                $("#pointChoose-table").bootstrapTable('refresh');
            })

        })
    </script>
</#macro>
