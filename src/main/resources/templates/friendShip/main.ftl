<#import "/main/macros.ftl" as macros>
<#assign importCss=[]>
<#assign importJs=["/js/user/user.js","/js/user/superior.js"]>
<@macros.navhead importJs=importJs importCss=importCss titleName="我的好友"></@macros.navhead>

<div class="main-contain" style="margin-top: 1rem">
    <div class="row">
        <div class="friendList col-3">

        </div>

        <div class="content-div col-9">
            <div class="content-top">

            </div>
            <div class="content-bottom row">
                <input class="in-msg form-control col-11">
                <span class="oi location" title="icon location"></span>
            </div>
        </div>
    </div>

</div>

<style>
    .friendList {
        overflow-y: auto;
        overflow-x: hidden;
    }

    .friend-tab {
        margin-top: 0;
        margin-bottom: 0;
        border-bottom: 1px solid #d9dde1;
        padding-top: 1rem;
        padding-bottom: 0.5rem;
    }

    .lastTime {
        position: absolute;
        top: 0;
        right: 1rem;
        color: #61a1a1;
    }

    .unreadNum {
        position: absolute;
        right: 1rem;
        bottom: -1rem;
        width: 1.2rem;
        height: 1.2rem;
        border-radius: 0.8rem;
        background-color: rgba(236, 4, 7, 0.61);
        font-size: 0.2rem;
        color: white;
    }

    .content-div {
        background-color: #f6f6f6;
    }

    .content-bottom {
        position: absolute;
        bottom: 0;
        width: 100%;
    }

    .content-bottom input {
        margin-left: 1rem;
    }

    .choosed-friend {
        background-color: #f6f6f6;
    }

    .msg-tab .msg {
        border-radius: 0.5rem;
        padding: 0.5rem 1rem;
    }

    .msg-l .msg {
        float: left;
        background-color: #c8c9ca;
    }

    .msg-r .msg {
        float: right;
        color: white;
        background-color: #1882c6;
    }
</style>

<script>
    $(function () {

        if (/(iPhone|iPad|iPod|iOS|Android)/i.test(navigator.userAgent)) { //移动端
            $(".main-contain").css("height", $(window).height() * 0.9)
            $(".friendList").css("width", "100%")
        }

        $(".friendList").height($(window).height() * 0.8)

        $.ajax({
            url: "/friendShip/getActive",
            data: {
                "userAccount": $("#loginUserAccount").val()
            },
            dataType: "json",
            type: "post",
            success: function (result) {
                if (result.success == true) {
                    for (var i in result.data) {
                        $(".friendList").append("<div class='row friend-tab' id='tab-" + result.data[i].fsId + "'>\n" +
                                "                <div class='col-3 tab-left'>\n" +
                                "                    <img class='friendPic rounded' style='height: 3.3rem;width: 3.3rem'" +
                                "                       src='data:image/jpeg;base64," + result.data[i].userVo.pic + "'>\n" +
                                "                </div>\n" +
                                "                <div class='col-9 tab-right'>\n" +
                                "                    <p class='friendName'>" + result.data[i].userVo.userName + "</p>\n" +
                                "                    <p class='lastTime'>" + getShowTime(result.data[i].fsLastTime) + "</p>\n" +
                                "                    <input class='friendId' hidden>\n" +
                                "                    <input class='friendAccount' hidden>\n" +
                                "                    <input class='rowData' hidden>\n" +
                                "                </div>\n" +
                                "            </div>"
                        )
                        var unreadNum = result.data[i].fsUnread1;
                        var friendId = result.data[i].fsUser2;
                        if (result.data[i].fsUser1 == result.data[i].userVo.userId) {
                            unreadNum = result.data[i].fsUnread2;
                            friendId = result.data[i].fsUser1;
                        }
                        if (unreadNum > 0) {
                            $("#tab-" + result.data[i].fsId + " .tab-right").append("<p class='unreadNum text-center'>" + unreadNum + "</p>\n")
                        }
                        $("#tab-" + result.data[i].fsId + " .friendId").val(friendId)
                        $("#tab-" + result.data[i].fsId + " .friendAccount").val(result.data[i].userVo.userAccount)
                        $("#tab-" + result.data[i].fsId + " .rowData").val(result.data[i].fsMsgRecord)
                    }
                    $(".friendList").find(".friend-tab").eq(0).addClass("choosed-friend")
                    $(".friendList").find(".friend-tab").eq(0).click()

                } else {
                    layer.msg(result.msg, {icon: 2});
                }
            },
            error: function (result) {
            }
        })

        //选择friend
        $(document).on("click", ".friend-tab", function () {
            $(".content-top").empty()
            $(".friend-tab").removeClass("choosed-friend")
            $(this).addClass("choosed-friend")
            var data = eval($(".choosed-friend .rowData").val())
            var friendId = $(".choosed-friend .friendId").val()
            for (i in data) {
                if (friendId != data[i].msgUser) {//我的消息
                    $(".content-top").append("<div class='row msg-tab msg-r'>\n" +
                            "                    <div class='col-6'></div>\n" +
                            "                    <div class='col-6'>" +
                            "                       <div class='msg'><span>" + data[i].msgContent + "</span></div>" +
                            "                    </div>" +
                            "                </div>")
                } else {//对方的消息
                    $(".content-top").append("<div class='row msg-tab msg-l'>\n" +
                            "                    <div class='col-6'> " +
                            "                       <div class='msg'><span>" + data[i].msgContent + "</span></div>" +
                            "                    </div>" +
                            "                    <div class='col-6'></div>\n" +
                            "                </div>")
                }
            }
            //清空未读
            $.ajax({
                url: "/friendShip/readMsg",
                data: {
                    "userAccount": $("#loginUserAccount").val(),
                    "fsId": $(".choosed-friend").attr("id").substring(4)
                },
                dataType: "json",
                type: "post",
                success: function (result) {
                    if (result.success == true) {
                        $(".choosed-friend .unreadNum").remove()
                    } else {
                        layer.msg(result.msg, {icon: 2});
                    }
                },
                error: function (result) {
                }
            })
        })

        // 发送消息
        $(".in-msg").keyup(function (event) {
            if (event.keyCode == 13) {
                $.ajax({
                    url: "/friendShip/addMsg",
                    data: {
                        "userAccount": $("#loginUserAccount").val(),
                        "id": $(".choosed-friend").attr("id").substring(4),
                        "msgContent": $(".in-msg").val()
                    },
                    dataType: "json",
                    type: "post",
                    success: function (result) {
                        if (result.success == true) {
                            $(".content-top").append("<div class='row msg-tab msg-r'>\n" +
                                    "                    <div class='col-6'></div>\n" +
                                    "                    <div class='col-6'>" +
                                    "                       <div class='msg'><span>" + $(".in-msg").val() + "</span></div>" +
                                    "                    </div>" +
                                    "                </div>")
                            $(".in-msg").val("")
                            //通知对方
                            $.ajax({
                                url: "/ws/chat/messageRecordUpload",
                                data: {
                                    "fsId": $(".choosed-friend").attr("id").substring(4),
                                    "toAccount": $(".choosed-friend .friendAccount").val()
                                },
                                dataType: "json",
                                type: "post",
                                success: function (result) {
                                    if (result.success == true) {

                                    } else {
                                        layer.msg(result.msg, {icon: 2});
                                    }
                                },
                                error: function (result) {
                                }
                            })
                        } else {
                            layer.msg(result.msg, {icon: 2});
                        }
                    },
                    error: function (result) {
                    }
                })
            }
        });

        //后端返回时间转换为显示时间
        function getShowTime(time) {
            var now = new Date();
            var year = now.getFullYear().toString();
            var month = (now.getMonth() + 1).toString().padStart(2, "0");
            var day = now.getDate().toString().padStart(2, "0");
            if (year + month + day == time.substring(0, 8)) {
                time = time.substring(8, 10) + ":" + time.substring(10, 12) + ":" + time.substring(12)
            }
            else {
                time = time.substring(0, 4) + "-" + time.substring(4, 6) + "-" + time.substring(6, 8)
            }
            return time;
        }

        // websocket
        var socket;
        if (typeof (WebSocket) == "undefined") {
            console.log("浏览器不支持WebSocket");
        } else {

            socket = new WebSocket("ws://localhost:8080/webSocket/chatModule?userAccount=" + $("#loginUserAccount").val());
            //连接打开事件
            socket.onopen = function () {
                console.log($("#loginUserAccount").val() + "已连接");
                socket.send($("#loginUserAccount").val() + "已连接");
            };
            //收到消息事件
            socket.onmessage = function (msg) {
                console.log(JSON.parse(msg.data))








            };
            //连接关闭事件
            socket.onclose = function () {
                console.log("Socket已关闭");
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
</script>