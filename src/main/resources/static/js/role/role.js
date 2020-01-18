$(function () {

    $('#role-table').bootstrapTable({
        url: '/role/pageList.json',
        pagination: true,           //分页
        // search: true,               //显示搜索框
        sidePagination: "server",   //服务端处理分页
        pageNumber: 1,              //初始化加载第一页，默认第一页
        pageSize: 20,               //每页的记录行数（*）
        pageList: [10, 20, 30, 50],        //可供选择的每页的行数（*）
        uniqueId: "roleId",         //隐藏的id
        // queryParamsType:'',
        queryParams : function(params){
            var temp={
                pageNumber: (params.offset / params.limit) + 1,     //页数
                pageSize: params.limit,                             //每页的记录行数
            };
            return temp;
        },
        responseHandler:function(res) {
            return {
                "total":res.total,//总条目数
                "rows": res.data //数据
            }
        },
        columns: [{
            field: 'roleName',
            title: '角色名'
        }, {
            field: 'roleDesc',
            title: '描述'
        },
            {
                field: 'isActive',
                title: '是否激活',
                formatter: isActiveFormatter
            },{
                field: 'action',
                title: '操作',
                formatter: actionFormatter
            }, ]
    });

    //激活状态格式化
    function isActiveFormatter(value, row, index) {
        if(value=="1")
            return "激活"
        else
            return "冻结"
    }

    //操作栏的格式化
    function actionFormatter(value, row, index) {
        var id = value;
        var result = "";
        // result += "<a href='javascript:;'  title='查看'><span class=''>查看</span></a>&nbsp;&nbsp;&nbsp;";
        result += "<a href='javascript:;' class='edit-role' title='编辑'><span class=''>编辑</span></a>&nbsp;&nbsp;&nbsp;";
        // result += "<a href='javascript:;'  title='删除'><span class=''>删除</span></a>";

        return result;
    }


    //新增role
    $("#role-add").on("click",function (){
        layui.use('layer',function(){
            layer=layui.layer;

            layer.open({
                type: 1,
                zIndex:"1",
                title: '新增角色',
                // area: ['800px'],
                content: $('#role-add-div'),
                btn: ['确定','关闭'],
                yes: function (index) {

                    $.ajax({
                        url: "/role/add",
                        data : $("#role-add-form").serialize(),
                        dataType : "json",
                        type : "post",
                        success : function(result){
                            console.log(result)
                            if (result.success==true){
                                layer.msg("新增成功",{icon:1});
                                layer.close(index);
                                $("#role-table").bootstrapTable('refresh');
                            }else {
                                layer.msg(result.msg,{icon:2});
                            }
                            $("#role-add-form")[0].reset()
                        },
                        error : function(result){
                            layer.msg("ERROR",{icon:2});
                            $("#role-add-form")[0].reset()
                        }
                    })

                },
                btn2: function (index) {
                    layer.close(index);
                    $("#role-add-form")[0].reset()
                },
                cancel: function(index){
                    layer.close(index);
                    $("#role-add-form")[0].reset()
                }
            })
        })

    });


    //修改角色
    $(document).on("click",".edit-role",function (){
        var roleId=$(this).parent().parent().attr("data-uniqueid");
        $("#role-modify-form .roleId").val(roleId);
        $("#role-modify-form .roleName").val($(this).closest("tr").find("td").eq(0).text());
        $("#role-modify-form .roleDesc").val($(this).closest("tr").find("td").eq(1).text());
        if($(this).closest("tr").find("td").eq(2).text()=="激活"){
            $("#modify-isActive").val("1")
        }
        else {
            $("#modify-isActive").val("0")
        }
        layui.use('layer',function(){
            layer=layui.layer;
            layer.open({
                type: 1,
                zIndex:"1",
                title: '修改用户',
                // area: ['800px'],
                content: $('#role-modify-div'),
                btn: ['确定','关闭'],
                yes: function (index) {
                    $.ajax({
                        url: "/role/modify",
                        data : $("#role-modify-form").serialize(),
                        dataType : "json",
                        type : "post",
                        success : function(result){
                            console.log(result)
                            if (result.success==true){
                                layer.msg("修改成功",{icon:1});
                                layer.close(index);
                                $("#role-table").bootstrapTable('refresh');
                            }else {
                                layer.msg(result.msg,{icon:2});
                            }
                            $("#role-modify-form")[0].reset()
                        },
                        error : function(result){
                            layer.msg("ERROR",{icon:2});
                            $("#role-modify-form")[0].reset()
                        }
                    })

                },
                btn2: function (index) {
                    layer.close(index);
                    $("#role-modify-form")[0].reset()
                },
                cancel: function(index){
                    layer.close(index);
                    $("#role-modify-form")[0].reset()
                }
            })
        })

    });



})