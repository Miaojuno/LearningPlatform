$(function () {


    $.ajax({
        url: "/role/listActive",
        dataType : "json",
        type : "post",
        success : function(result){
            for(i in result){
                $("#role-modify-apply-select").append("<option value='"+result[i].roleId+"' >"+result[i].roleName+"</option>");
            }
        },
        error : function(result){9
        }
    })

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
                        data:  {
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
                        data:  {
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
        queryParams : function(params){
            var temp={
                pageNumber: (params.offset / params.limit) + 1,     //页数
                pageSize: params.limit,                             //每页的记录行数
                userName : $("#choose-superior-div .searchSuperiorName").val(),
                subordinateAccount : $("#loginUserAccount").val()
            };
            return temp;
        },
        responseHandler:function(res) {
            return {
                "total":res.total,//总条目数
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
            }, ]
    });

    //操作栏的格式化
    function actionFormatter(value, row, index) {
        var id = value;
        var result = "";
        result += "<a href='javascript:;'  class='chooseSuperior' title='选择'><span>选择</span></a>";
        return result;
    }

    var layerSuperiorChoose;

    //打开上级选择遮罩层
    $(document).on("click", "#superior-modify-apply-div .newName", function (){
        var userId=$(this).parent().parent().attr("data-uniqueid");
        $("#choose-superior-div .subordinateId").val(userId);
        $("#superior-table").bootstrapTable('refresh');
        layui.use('layer',function(){
            layer=layui.layer;

            layerSuperiorChoose = layer.open({
                type: 1,
                zIndex:"1",
                title: '选择上级',
                // area: ['800px'],
                content: $('#choose-superior-div'),
                cancel: function(index){
                    layer.close(index);
                }
            })
        })
    });

    $(document).on("click", ".chooseSuperior", function (){
        var superiorId=$(this).parent().parent().attr("data-uniqueid");
        $("#superior-modify-apply-div .newName").val($(this).closest("tr").find("td").eq(1).text())
        $("#superior-modify-apply-div .newId").val(superiorId)
        layer.close(layerSuperiorChoose)
    });

    $(document).on("input","#choose-superior-div .searchSuperiorName",function () {
        $("#choose-superior-table").bootstrapTable('refresh');
    })

})