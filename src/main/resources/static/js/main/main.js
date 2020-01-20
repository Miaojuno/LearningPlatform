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
                            console.log(result)
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


})