$(function () {
    if (/(iPhone|iPad|iPod|iOS|Android)/i.test(navigator.userAgent)) { //移动端
        // $(".main-contain").css("height",$(window).height()*0.9)
        $(".main-contain").css("margin-top", "0")
    }
    else {
        $(".main-contain").css("margin-top", "0.5rem")
        $(".navbar .navbar-brand").css("margin-left",$(window).width()*0.13)
        $(".navbar .navbar-collapse").css("margin-right",$(window).width()*0.1)
    }


    getUserPicInTop()

    // !function(){function n(n,e,t){return n.getAttribute(e)||t}function e(n){return document.getElementsByTagName(n)}function t(){var t=e("script"),o=t.length,i=t[o-1];return{l:o,z:n(i,"zIndex",-1),o:n(i,"opacity",1.5),c:n(i,"color","0,0,0"),n:n(i,"count",300)}}function o(){a=m.width=window.innerWidth||document.documentElement.clientWidth||document.body.clientWidth,c=m.height=window.innerHeight||document.documentElement.clientHeight||document.body.clientHeight}function i(){r.clearRect(0,0,a,c);var n,e,t,o,m,l;s.forEach(function(i,x){for(i.x+=i.xa,i.y+=i.ya,i.xa*=i.x>a||i.x<0?-1:1,i.ya*=i.y>c||i.y<0?-1:1,r.fillRect(i.x-.5,i.y-.5,1,1),e=x+1;e<u.length;e++)n=u[e],null!==n.x&&null!==n.y&&(o=i.x-n.x,m=i.y-n.y,l=o*o+m*m,l<n.max&&(n===y&&l>=n.max/2&&(i.x-=.03*o,i.y-=.03*m),t=(n.max-l)/n.max,r.beginPath(),r.lineWidth=t/2,r.strokeStyle="rgba("+d.c+","+(t+.2)+")",r.moveTo(i.x,i.y),r.lineTo(n.x,n.y),r.stroke()))}),x(i)}var a,c,u,m=document.createElement("canvas"),d=t(),l="c_n"+d.l,r=m.getContext("2d"),x=window.requestAnimationFrame||window.webkitRequestAnimationFrame||window.mozRequestAnimationFrame||window.oRequestAnimationFrame||window.msRequestAnimationFrame||function(n){window.setTimeout(n,1e3/45)},w=Math.random,y={x:null,y:null,max:2e4};m.id=l,m.style.cssText="position:fixed;top:0;left:0;z-index:"+d.z+";opacity:"+d.o,e("body")[0].appendChild(m),o(),window.onresize=o,window.onmousemove=function(n){n=n||window.event,y.x=n.clientX,y.y=n.clientY},window.onmouseout=function(){y.x=null,y.y=null};for(var s=[],f=0;d.n>f;f++){var h=w()*a,g=w()*c,v=2*w()-1,p=2*w()-1;s.push({x:h,y:g,xa:v,ya:p,max:6e3})}u=s.concat([y]),setTimeout(function(){i()},100)}();

    //背景上的动态线条
    ! function() {
        //封装方法，压缩之后减少文件大小
        function get_attribute(node, attr, default_value) {
            return node.getAttribute(attr) || default_value;
        }
        //封装方法，压缩之后减少文件大小
        function get_by_tagname(name) {
            return document.getElementsByTagName(name);
        }
        //获取配置参数
        function get_config_option() {
            var scripts = get_by_tagname("script"),
                script_len = scripts.length,
                script = scripts[script_len - 1]; //当前加载的script
            return {
                l: script_len, //长度，用于生成id用
                z: get_attribute(script, "zIndex", -1), //z-index
                o: get_attribute(script, "opacity", 0.75), //opacity 线条粗细
                c: get_attribute(script, "color", "0,0,0"), //color
                n: get_attribute(script, "count", 250) //count 线条数
            };
        }
        //设置canvas的高宽
        function set_canvas_size() {
            canvas_width = the_canvas.width = window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth,
                canvas_height = the_canvas.height = window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight;
        }

        //绘制过程
        function draw_canvas() {
            context.clearRect(0, 0, canvas_width, canvas_height);
            //随机的线条和当前位置联合数组
            var all_array = [current_point].concat(random_lines);
            var e, i, d, x_dist, y_dist, dist; //临时节点
            //遍历处理每一个点
            random_lines.forEach(function(r) {
                r.x += r.xa,
                    r.y += r.ya, //移动
                    r.xa *= r.x > canvas_width || r.x < 0 ? -1 : 1,
                    r.ya *= r.y > canvas_height || r.y < 0 ? -1 : 1, //碰到边界，反向反弹
                    context.fillRect(r.x - 0.5, r.y - 0.5, 1, 1); //绘制一个宽高为1的点
                for (i = 0; i < all_array.length; i++) {
                    e = all_array[i];
                    //不是当前点
                    if (r !== e && null !== e.x && null !== e.y) {
                        x_dist = r.x - e.x, //x轴距离 l
                            y_dist = r.y - e.y, //y轴距离 n
                            dist = x_dist * x_dist + y_dist * y_dist; //总距离, m
                        dist < e.max && (e === current_point && dist >= e.max / 2 && (r.x -= 0.03 * x_dist, r.y -= 0.03 * y_dist), //靠近的时候加速
                            d = (e.max - dist) / e.max,
                            context.beginPath(),
                            context.lineWidth = d / 2,
                            context.strokeStyle = "rgba(" + config.c + "," + (d + 0.2) + ")",
                            context.moveTo(r.x, r.y),
                            context.lineTo(e.x, e.y),
                            context.stroke());
                    }
                }
                all_array.splice(all_array.indexOf(r), 1);

            }), frame_func(draw_canvas);
        }
        //创建画布，并添加到body中
        var the_canvas = document.createElement("canvas"), //画布
            config = get_config_option(), //配置
            canvas_id = "c_n" + config.l, //canvas id
            context = the_canvas.getContext("2d"), canvas_width, canvas_height,
            frame_func = window.requestAnimationFrame || window.webkitRequestAnimationFrame || window.mozRequestAnimationFrame || window.oRequestAnimationFrame || window.msRequestAnimationFrame || function(func) {
                window.setTimeout(func, 1000 / 45);
            }, random = Math.random,
            current_point = {
                x: null, //当前鼠标x
                y: null, //当前鼠标y
                max: 20000
            };
        the_canvas.id = canvas_id;
        the_canvas.style.cssText = "position:fixed;top:0;left:0;z-index:" + config.z + ";opacity:" + config.o;
        get_by_tagname("body")[0].appendChild(the_canvas);
        //初始化画布大小

        set_canvas_size(), window.onresize = set_canvas_size;
        //当时鼠标位置存储，离开的时候，释放当前位置信息
        window.onmousemove = function(e) {
            e = e || window.event, current_point.x = e.clientX, current_point.y = e.clientY;
        }, window.onmouseout = function() {
            current_point.x = null, current_point.y = null;
        };
        //随机生成config.n条线位置信息
        for (var random_lines = [], i = 0; config.n > i; i++) {
            var x = random() * canvas_width, //随机位置
                y = random() * canvas_height,
                xa = 2 * random() - 1, //随机运动方向
                ya = 2 * random() - 1;
            random_lines.push({
                x: x,
                y: y,
                xa: xa,
                ya: ya,
                max: 4000 //沾附距离
            });
        }
        //重绘
        setTimeout(function() {
            draw_canvas();
        }, 100);
    }();






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
        console.log(1)
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
                    if(rocordData.msgType=="img"){
                        $(".content-top").append("<div class='row msg-tab msg-l'>\n" +
                            "                    <div class='col-8'>\n" +
                            "                       <div class='msg msg-pic-div' id='pic-"+rocordData.msgContent+"'>" +
                            "                       </div>" +
                            "                    </div>" +
                            "                    <div class='col-4'></div>\n" +
                            "                </div>")
                        //异步加载图片
                        $.ajax({
                            url: "/friendShip/getPic",
                            data: {
                                "imgId": rocordData.msgContent
                            },
                            dataType: "json",
                            type: "post",
                            success: function (result) {
                                if (result.success == true) {
                                    $("#pic-"+result.data.imgId).append("<img class='friendPic rounded' style='max-width: 20rem' " +
                                        "src='data:image/jpeg;base64," + result.data.imgContent + "'>")
                                    $('.content-top')[0].scrollTop = $('.content-top')[0].scrollHeight;
                                } else {
                                    layer.msg(result.msg, {icon: 2});
                                }
                            },
                            error: function (result) {
                            }
                        })
                    }
                    else {
                        $(".content-top").append("<div class='row msg-tab msg-l'>\n" +
                            "                    <div class='col-6'> " +
                            "                       <div class='msg'><span>" + rocordData.msgContent + "</span></div>" +
                            "                    </div>" +
                            "                    <div class='col-6'></div>\n" +
                            "                </div>")
                    }

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