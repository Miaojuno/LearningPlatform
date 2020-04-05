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
                    $("#tab-" + result.data[i].fsId ).addClass("tab-user-"+friendId)
                    $("#tab-" + result.data[i].fsId + " .friendAccount").val(result.data[i].userVo.userAccount)
                    $("#tab-" + result.data[i].fsId + " .rowData").val(result.data[i].fsMsgRecord)
                }

                var url = location.search;
                var chooseId;
                if (url.indexOf("?") != -1) {
                    chooseId = url.split("=")[1];
                    $(".tab-user-"+chooseId).addClass("choosed-friend")
                    $(".tab-user-"+chooseId).click()
                }

                else{
                    $(".friendList").find(".friend-tab").eq(0).addClass("choosed-friend")
                    $(".friendList").find(".friend-tab").eq(0).click()
                }

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
            if(data[i].msgType=="img"){//图片类型消息
                if (friendId != data[i].msgUser) {//我的消息
                    $(".content-top").append("<div class='row msg-tab msg-r'>\n" +
                        "                    <div class='col-4'></div>\n" +
                        "                    <div class='col-8'>\n" +
                        "                       <div class='msg msg-pic-div' id='pic-"+data[i].msgContent+"'>" +
                        "                       </div>" +
                        "                    </div>" +
                        "                </div>")
                } else {//对方的消息
                    $(".content-top").append("<div class='row msg-tab msg-l'>\n" +
                        "                    <div class='col-8'>\n" +
                        "                       <div class='msg msg-pic-div' id='pic-"+data[i].msgContent+"'>" +
                        "                       </div>" +
                        "                    </div>" +
                        "                    <div class='col-4'></div>\n" +
                        "                </div>")
                }
                //异步加载图片
                $.ajax({
                    url: "/friendShip/getPic",
                    data: {
                        "imgId": data[i].msgContent
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
            else{//文字类型消息
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

    //选择图片 立即发送
    $('#pic-send-icon').on('click', function () {
        $("#picFile").click()
    });
    $("#picFile").bind("input propertychange", function () {
        if($("#picFile").val()!=""){
            sendPicMessage()
        }
    });

    // 发送消息
    $(".sendBtn").on("click", function (event) {
        sendMessage();
    });

    // 回车发送消息
    $(".in-msg").keyup(function (event) {
        if (event.keyCode == 13) {
            sendMessage();
        }
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
                        "                    <div class='col-4'></div>\n" +
                        "                    <div class='col-8'>" +
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

    var picDivId=0

    function sendPicMessage() {
        var fileObj = document.getElementById('picFile').files[0]; // js 获取文件对象
        var form = new FormData();  // FormData 对象
        form.append("userAccount", $("#loginUserAccount").val())
        form.append("id", $(".choosed-friend").attr("id").substring(4))
        form.append("msgContent", "")
        form.append("file", fileObj); // 文件对象
        $.ajax({
            url: "/friendShip/addMsg",
            data: form,
            dataType: "json",
            type: "post",
            processData: false,
            contentType: false,
            success: function (result) {
                if (result.success == true) {
                    var reads= new FileReader();
                    reads.readAsDataURL(fileObj);

                    reads.onload=function (e) {
                        $(".content-top").append("<div class='row msg-tab msg-r'>\n" +
                            "                    <div class='col-4'></div>\n" +
                            "                    <div class='col-8'>\n" +
                            "                       <div id='picId-"+picDivId+"' class='msg msg-pic-div' " +
                            "                       </div>" +
                            "                    </div>" +
                            "                </div>")
                        $("#picId-"+picDivId).append("<img class='friendPic rounded' style='max-width: 20rem' " +
                            "src='" + this.result + "'>")
                        picDivId=picDivId+1
                        $('.content-top')[0].scrollTop = $('.content-top')[0].scrollHeight;
                    };

                    $(".in-msg").val("")
                    $('.content-top')[0].scrollTop = $('.content-top')[0].scrollHeight;
                    $("#picFile").val("")
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
    function getObjectURL(file) {
        var url = null;
        /* window.URL = window.URL || window.webkitURL;*/

        if (window.createObjcectURL != undefined) {
            url = window.createOjcectURL(file);
        } else if (window.URL != undefined) {
            url = window.URL.createObjectURL(file);
        } else if (window.webkitURL != undefined) {
            url = window.webkitURL.createObjectURL(file);
        }
        return url;
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