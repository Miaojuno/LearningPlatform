<#import "/main/macros.ftl" as macros>
<#assign importCss=[]>
<#assign importJs=["/js/user/user.js","/js/user/superior.js"]>
<@macros.navhead importJs=importJs importCss=importCss></@macros.navhead>

<div class="main-contain" style="margin-top: 1rem">
    <div class="friendList">

    </div>

</div>

<style>
    .friendList {
        width: 25%;
        overflow-y:auto;
        overflow-x: hidden;
    }

    .friend-tab{
        border-bottom: 1px solid #d9dde1;
        margin-top: 1rem;
    }
    .lastTime{
        position: absolute;
        top: 0;
        right: 1.5rem;
    }
    .unreadNum{
        position: absolute;
        right: 1.5rem;
        bottom: -0.8rem;
        width: 1.2rem;
        height: 1.2rem;
        border-radius: 0.8rem;
        background-color: rgba(236, 4, 7, 0.61);
        font-size:0.2rem;
        color: white;
    }
</style>

<script>
    $(function () {
        if (/(iPhone|iPad|iPod|iOS|Android)/i.test(navigator.userAgent)) { //移动端
            $(".main-contain").css("height", $(window).height() * 0.9)
            $(".friendList").css("width", "100%")
        }

        $(".friendList").height($(window).height()*0.8)


        $.ajax({
            url: "/friendShip/getActive",
            data: {
                "userAccount": $("#loginUserAccount").val()
            },
            dataType: "json",
            type: "post",
            success: function (result) {
                if (result.success == true) {
                    console.log(result)
                    for (var i in result.data) {
                        $(".friendList").append("<div class='row friend-tab' id='tab-"+result.data[i].userVo.userId+"'>\n" +
                                "                <div class='col-3 tab-left'>\n" +
                                "                    <img class='friendPic rounded' style='height: 3.3rem;width: 3.3rem'" +
                                "                       src='data:image/jpeg;base64," + result.data[i].userVo.pic + "'>\n" +
                                "                </div>\n" +
                                "                <div class='col-9 tab-right'>\n" +
                                "                    <p class='friendName'>" + result.data[i].userVo.userName + "</p>\n" +
                                "                    <p class='lastTime'>" + getShowTime(result.data[i].fsLastTime) + "</p>\n" +
                                "                </div>\n" +
                                "            </div>")
                        var unreadNum = result.data[i].fsUnread1;
                        if (result.data[i].fsUser1 == result.data[i].userVo.userId) {
                            unreadNum = result.data[i].fsUnread2;
                        }
                        if(unreadNum>0){
                            $("#tab-"+result.data[i].userVo.userId+" .tab-right").append("<p class='unreadNum text-center'>" + unreadNum + "</p>\n")
                        }
                    }

                } else {
                    layer.msg(result.msg, {icon: 2});
                }
            },
            error: function (result) {
            }
        })

        //后端返回时间转换为显示时间
        function getShowTime(time) {
            var now = new Date();
            var year = now.getFullYear().toString();
            var month = (now.getMonth() + 1).toString().padStart(2, "0");
            var day = now.getDate().toString().padStart(2, "0");
            if(year+month+day==time.substring(0,8)){
                time=time.substring(8,10)+":"+time.substring(10,12)+":"+time.substring(12)
            }
            else {
                time=time.substring(0,4)+"-"+time.substring(4,6)+"-"+time.substring(6,8)
            }
            return time;
        }
    })
</script>