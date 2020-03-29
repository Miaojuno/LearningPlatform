$(function () {

    if (/(iPhone|iPad|iPod|iOS|Android)/i.test(navigator.userAgent)) { //移动端
        $(".main-contain").css("height", $(window).height() * 0.9)
        $(".friendList").css("width", "100%")
    }

    $(".main-contain").height($(window).height() * 0.8)
    $(".friendList").height($(window).height() * 0.8)
    $(".content-div").height($(window).height() * 0.8)
    $(".content-top").height($(".content-div").height() - $(".content-bottom").height() - 30)

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
                    "                    <div class='col-4'></div>\n" +
                    "                    <div class='col-8'>" +
                    "                       <div class='msg'><span>" + data[i].msgContent + "</span></div>" +
                    "                    </div>" +
                    "                </div>")
            } else {//对方的消息
                $(".content-top").append("<div class='row msg-tab msg-l'>\n" +
                    "                    <div class='col-8'> " +
                    "                       <div class='msg'><span>" + data[i].msgContent + "</span></div>" +
                    "                    </div>" +
                    "                    <div class='col-4'></div>\n" +
                    "                </div>")
            }
        }
        $('.content-top')[0].scrollTop = $('.content-top')[0].scrollHeight;
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

    //点击右侧 清除以选择框的未读
    $(document).on("click", ".content-div", function () {
        $(".choosed-friend .unreadNum").remove()
    })

    // 回车发送消息
    $(".in-msg").keyup(function (event) {
        if (event.keyCode == 13) {
            sendMessage();
        }
    });

    // 回车发送消息
    $(".sendBtn").on("click", function (event) {
        sendMessage();
    });

    function sendMessage() {
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
                        "                    <div class='col-8'></div>\n" +
                        "                    <div class='col-4'>" +
                        "                       <div class='msg'><span>" + $(".in-msg").val() + "</span></div>" +
                        "                    </div>" +
                        "                </div>")
                    $(".in-msg").val("")
                    $('.content-top')[0].scrollTop = $('.content-top')[0].scrollHeight;
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


})