$(function () {
    if (/(iPhone|iPad|iPod|iOS|Android)/i.test(navigator.userAgent)) { //移动端
        // $(".main-contain").css("height",$(window).height()*0.9)
        $(".main-contain").css("margin-top", "0")
    }
    else {
        $(".main-contain").css("margin-top", "0.5rem")
    }


    getUserPicInTop()

    function getUserPicInTop() {
        //获取右上角头像
        $.ajax({
            url: "/user/findByUserAccount",
            data: {
                "userAccount": $('#loginUserAccount').val()
            },
            dataType: "json",
            type: "post",
            success: function (result) {
                $("#userPicInNavHead").attr("src", "data:image/jpeg;base64," + result.data.pic);
            },
            error: function (result) {
                layer.msg("ERROR", {icon: 2});
            }
        })
    }

    //去掉所有input的autocomplete, 显示指定的除外
    $('input:not([autocomplete]),textarea:not([autocomplete]),select:not([autocomplete])').attr('autocomplete', 'off');

    $.ajax({
        url: "/role/listActive",
        dataType: "json",
        type: "post",
        success: function (result) {
            for (i in result) {
                $("#role-modify-apply-select").append("<option value='" + result[i].roleId + "' >" + result[i].roleName + "</option>");
            }
        },
        error: function (result) {
            9
        }
    })

    //修改头像
    $(document).on("click", "#userPicModifyBtn", function () {
        $("#user-pic-modify-file").click();
    })

    $("#user-pic-modify-file").bind("input propertychange", function () {
        layui.use('layer', function () {
            layer = layui.layer;
            var fileObj = document.getElementById('user-pic-modify-file').files[0]; // js 获取文件对象
            var url = "/user/modifyPic"; // 接收上传文件的后台地址
            var form = new FormData();  // FormData 对象
            form.append("userAccount", $("#loginUserAccount").val())
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
                    layer.msg("头像修改成功", {icon: 1});
                    getUserPicInTop()
                },
                error: function (err) {
                    layer.msg("失败", {icon: 2});
                }
            });

        })
    });


    $(document).on("click", "#roleModifyApplyBtn", function () {
        layui.use('layer', function () {
            layer = layui.layer;
            layer.open({
                type: 1,
                zIndex: "1",
                title: '角色修改申请',
                // area: ['800px'],
                content: $('#role-modify-apply-div'),
                btn: ['确定', '关闭'],
                yes: function (index) {
                    $.ajax({
                        url: "/apply/modifyRoleApply",
                        data: {
                            "userAccount": $('#loginUserAccount').val(),
                            "newId": $("#role-modify-apply-select").val(),
                            "reason": $("#role-modify-apply-div .reason").val()
                        },
                        dataType: "json",
                        type: "post",
                        success: function (result) {
                            if (result.success == true) {
                                layer.msg("申请成功", {icon: 1});
                                layer.close(index);
                            } else {
                                layer.msg(result.msg, {icon: 2});
                            }
                        },
                        error: function (result) {
                            layer.msg("ERROR", {icon: 2});
                        }
                    })

                },
                btn2: function (index) {
                    layer.close(index);
                },
                cancel: function (index) {
                    layer.close(index);
                }
            })
        })

    });


    $(document).on("click", "#supeiorModifyApplyBtn", function () {
        layui.use('layer', function () {
            layer = layui.layer;
            layer.open({
                type: 1,
                zIndex: "1",
                title: '上级修改申请',
                // area: ['800px'],
                content: $('#superior-modify-apply-div'),
                btn: ['确定', '关闭'],
                yes: function (index) {
                    $.ajax({
                        url: "/apply/modifySuperiorApply",
                        data: {
                            "userAccount": $('#loginUserAccount').val(),
                            "newId": $("#superior-modify-apply-div .newId").val(),
                            "reason": $("#superior-modify-apply-div .reason").val()
                        },
                        dataType: "json",
                        type: "post",
                        success: function (result) {
                            if (result.success == true) {
                                layer.msg("申请成功", {icon: 1});
                                layer.close(index);
                            } else {
                                layer.msg(result.msg, {icon: 2});
                            }
                        },
                        error: function (result) {
                            layer.msg("ERROR", {icon: 2});
                        }
                    })

                },
                btn2: function (index) {
                    layer.close(index);
                },
                cancel: function (index) {
                    layer.close(index);
                }
            })
        })

    });


    function initSuperiorModifyTable() {
        //表格
        $('#choose-superior-table').bootstrapTable({
            url: '/user/superiorPageList.json',
            pagination: true,           //分页
            sidePagination: "server",   //服务端处理分页
            pageNumber: 1,              //初始化加载第一页，默认第一页
            pageSize: 20,               //每页的记录行数（*）
            pageList: [10, 20, 30, 50],        //可供选择的每页的行数（*）
            uniqueId: "userId",         //隐藏的id
            // queryParamsType:'',
            queryParams: function (params) {
                var temp = {
                    pageNumber: (params.offset / params.limit) + 1,     //页数
                    pageSize: params.limit,                             //每页的记录行数
                    userName: $("#choose-superior-div .searchSuperiorName").val(),
                    subordinateAccount: $("#loginUserAccount").val()
                };
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
                    checkbox: true,
                    visible: true                  //显示复选框
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
    }

    //操作栏的格式化
    function actionFormatter(value, row, index) {
        var id = value;
        var result = "";
        result += "<a href='javascript:;'  class='chooseSuperior' title='选择'><span>选择</span></a>";
        return result;
    }

    var layerSuperiorChoose;

    //打开上级选择遮罩层
    $(document).on("click", "#superior-modify-apply-div .newName", function () {
        initSuperiorModifyTable()
        var userId = $(this).parent().parent().attr("data-uniqueid");
        $("#choose-superior-div .subordinateId").val(userId);
        $("#superior-table").bootstrapTable('refresh');
        layui.use('layer', function () {
            layer = layui.layer;

            layerSuperiorChoose = layer.open({
                type: 1,
                zIndex: "1",
                title: '选择上级',
                // area: ['800px'],
                content: $('#choose-superior-div'),
                cancel: function (index) {
                    layer.close(index);
                }
            })
        })
    });

    $(document).on("click", ".chooseSuperior", function () {
        var superiorId = $(this).parent().parent().attr("data-uniqueid");
        $("#superior-modify-apply-div .newName").val($(this).closest("tr").find("td").eq(1).text())
        $("#superior-modify-apply-div .newId").val(superiorId)
        layer.close(layerSuperiorChoose)
    });

    $(document).on("input", "#choose-superior-div .searchSuperiorName", function () {
        $("#choose-superior-table").bootstrapTable('refresh');
    })

    $.ajax({
        url: "/friendShip/haveNewMsg",
        data: {
            "userAccount": $('#loginUserAccount').val()
        },
        dataType: "json",
        type: "post",
        success: function (result) {
            if (result.success == true) {
                if (result.data == true) {
                    $(".msgTip").show()
                }
                else {
                    $(".msgTip").hide()
                }
            } else {
                layer.msg("error", {icon: 2});
            }
        },
        error: function (result) {
            layer.msg("ERROR", {icon: 2});
        }
    })

    // websocket
    var socket;
    if (typeof (WebSocket) == "undefined") {
        console.log("浏览器不支持WebSocket");
    } else {

        socket = new WebSocket("ws://localhost:8080/webSocket/chatModule?userAccount=" + $("#loginUserAccount").val());
        //连接打开事件
        socket.onopen = function () {
            // socket.send($("#loginUserAccount").val() + "已连接");
        };
        //收到消息事件
        socket.onmessage = function (msg) {
            var url = location.search;
            if (document.URL.substr(document.URL.length - 16, 16) == "/friendShip/main") {
                var fsData = JSON.parse(msg.data)
                //添加未读
                if ($("#tab-" + fsData.fsId + " .unreadNum").length > 0) {
                    $("#tab-" + fsData.fsId + " .unreadNum").text(eval($("#tab-" + fsData.fsId + " .unreadNum").text()) + 1)
                }
                else {
                    $("#tab-" + fsData.fsId + " .tab-right").append("<p class='unreadNum text-center'>" + 1 + "</p>\n")
                }
                //添加消息
                $("#tab-" + fsData.fsId + " .rowData").val(fsData.fsMsgRecord)
                //当前选中的话，将消息添加至聊天框
                if ($(".choosed-friend").attr("id").substring(4) == fsData.fsId) {
                    var rocordData = eval(fsData.fsMsgRecord).pop();
                    $(".content-top").append("<div class='row msg-tab msg-l'>\n" +
                        "                    <div class='col-6'> " +
                        "                       <div class='msg'><span>" + rocordData.msgContent + "</span></div>" +
                        "                    </div>" +
                        "                    <div class='col-6'></div>\n" +
                        "                </div>")
                    $('.content-top')[0].scrollTop = $('.content-top')[0].scrollHeight;
                }
            }
            else {
                $(".msgTip").show()
            }

        };


        //连接关闭事件
        socket.onclose = function () {
            // console.log("Socket已关闭");
        };
        //发生了错误事件
        socket.onerror = function () {
            alert("Socket发生了错误");
        }

        //窗口关闭时，关闭连接
        window.unload = function () {
            socket.close();
        };
    }
})