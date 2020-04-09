$(function () {



    $('input:not([autocomplete]),textarea:not([autocomplete]),select:not([autocomplete])').attr('autocomplete', 'off');

    $('#go-register').on('click', function () {
        location.href = "/user/register"
    });

    $('#go-login').on('click', function () {
        location.href = "/"
    });

    $('#btn-register').on('click', function () {
        layui.use('layer', function () {
            layer = layui.layer;

            //验证输入的信息
            var regx = /(^[A-Za-z0-9]+$)/
            if ($(".account").val().trim() == "") {
                layer.msg("账户不能为空", {icon: 2});
                return;
            }
            if (!regx.test($(".account").val())) {
                layer.msg("账户只能由数字和密码组成", {icon: 2});
                return;
            }
            if ($(".account").val().trim().length < 5 || $(".account").val().trim().length > 15) {
                layer.msg("账户长度应为5-15位", {icon: 2});
                return;
            }
            if ($(".pwd1").val().trim() == "") {
                layer.msg("密码不能为空", {icon: 2});
                return;
            }
            if ($(".name").val().trim() == "") {
                layer.msg("姓名不能为空", {icon: 2});
                return;
            }
            if (!regx.test($(".pwd1").val())) {
                layer.msg("密码只能由数字和密码组成", {icon: 2});
                return;
            }
            if ($(".pwd1").val().trim().length < 5 || $(".pwd1").val().trim().length > 15) {
                layer.msg("密码长度应为5-15位", {icon: 2});
                return;
            }
            if ($(".pwd1").val() != $(".pwd2").val()) {
                layer.msg("两次密码不一致", {icon: 2});
                return;
            }
            $.ajax({
                url: "/user/register",
                data: $("#form-register").serialize(),
                dataType: "json",
                type: "post",
                success: function (result) {
                    if (result.success == true) {
                        layer.msg("注册成功，跳转中。。。", {icon: 1});
                        setTimeout(function () {
                            location.href = "/";
                        }, 1500)
                    } else {
                        layer.msg(result.msg, {icon: 2});
                    }
                },
                error: function (result) {
                }
            })
        })
    });




})