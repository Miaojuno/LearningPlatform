$(function () {
    $('#user-table').bootstrapTable({
        url: '/user/pageList.json',
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
                userName : $("#userName").val(),
                roleId: $("#roleSelect").val()
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
            visible: true                  //是否显示复选框
        }, {
            field: 'userAccount',
            title: '账户'
        }, {
            field: 'userName',
            title: '姓名'
        }, {
            field: 'roleName',
            title: '角色' ,
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
        result += "<a href='javascript:;'  class='re-pwd' title='密码重置'><span>密码重置</span></a>&nbsp;&nbsp;&nbsp;";
        result += "<a href='javascript:;'  class='edit-user' title='编辑'><span>编辑</span></a>&nbsp;&nbsp;&nbsp;";
        result += "<a href='javascript:;'  class='edit-role' title='分配角色'><span>角色</span></a>&nbsp;&nbsp;&nbsp;";
        result += "<a href='javascript:;'  class='del-user' title='删除'><span>删除</span></a>";

        return result;
    }


    $("#data-query").on("click",function () {
        $('#user-table').bootstrapTable('refresh');
    })

    $("#data-reset").on("click",function () {
        location.reload()
    })

    $(document).on("click",".re-pwd",function () {
        var userId=$(this).parent().parent().attr("data-uniqueid");
        layui.use('layer',function() {
            layer = layui.layer;
            layer.confirm("确认重置密码？",function () {
                $.ajax({
                    url: "/user/rePwd",
                    data : {
                        "userId": userId
                    },
                    dataType : "json",
                    type : "post",
                    success : function(result){
                        if (result.success==true){
                            layer.msg("密码已重置为123456",{icon:1,time:2000});
                        }else {
                            layer.msg(result.msg,{icon:2});
                        }
                    },
                    error : function(result){
                    }
                })
            })
        })
    })

    $(document).on("click",".del-user",function () {
        var userId=$(this).parent().parent().attr("data-uniqueid");
        layui.use('layer',function() {
            layer = layui.layer;
            layer.confirm("确认删除？",function () {
                $.ajax({
                    url: "/user/delete",
                    data : {
                        "userId": userId
                    },
                    dataType : "json",
                    type : "post",
                    success : function(result){
                        if (result.success==true){
                            layer.msg("用户已删除",{icon:1,time:2000});
                        }else {
                            layer.msg(result.msg,{icon:2});
                        }
                    },
                    error : function(result){
                    }
                })
            })
        })
    })

    $("#user-add").on("click",function (){
        layui.use('layer',function(){
            layer=layui.layer;

            layer.open({
                type: 1,
                zIndex:"1",
                title: '新增用户',
                area: ['800px'],
                content: $('#user-add-div'),
                btn: ['确定','关闭'],
                yes: function (index) {

                    //验证输入的信息
                    var regx=/(^[A-Za-z0-9]+$)/
                    if($("#user-add-form .account").val()==""){
                        layer.msg("账户不能为空",{icon:2});
                    }
                    else if(!regx.test($(".account").val())){
                        layer.msg("账户只能由数字和密码组成",{icon:2});
                    }
                    else if($(".account").val().length<5 || $(".account").val().length>15){
                        layer.msg("账户长度应为5-15位",{icon:2});
                    }
                    else if($(".pwd1").val()==""){
                        layer.msg("密码不能为空",{icon:2});
                    }
                    else if($(".name").val()==""){
                        layer.msg("姓名不能为空",{icon:2});
                    }
                    else if(!regx.test($(".pwd1").val())){
                        layer.msg("密码只能由数字和密码组成",{icon:2});
                    }
                    else if($(".pwd1").val().length<5 || $(".pwd1").val().length>15){
                        layer.msg("密码长度应为5-15位",{icon:2});
                    }
                    else {
                        $.ajax({
                            url: "/user/register",
                            data : $("#user-add-form").serialize(),
                            dataType : "json",
                            type : "post",
                            success : function(result){
                                console.log(result)
                                if (result.success==true){
                                    layer.msg("新增成功",{icon:1});
                                    layer.close(index);
                                }else {
                                    layer.msg(result.msg,{icon:2});
                                }
                            },
                            error : function(result){
                            }
                        })
                    }
                },
                btn2: function (index) {
                    layer.close(index);
                },
                cancel: function(index){
                    layer.close(index);
                }
            })
        })

    });
})


function checkUserInfo() {
    
}

