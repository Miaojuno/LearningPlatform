<#import "/main/macros.ftl" as macros>
<#assign importCss=[]>
<#assign importJs=[]>
<@macros.navhead importJs=importJs importCss=importCss titleName="我的记录"></@macros.navhead>

<div class="main-contain" style="margin-top: 1rem">
    <div class="searchdiv m-auto row">
        <div class="row form-group col-5">
            <#--<label class="control-label col-4 text-right" for="">状态:</label>-->
            <#--<div class="col-8">-->
                <#--<@macros.roleSelect id="roleSelect" ></@macros.roleSelect>-->
            <#--</div>-->
        </div>


        <div class="row buttons">
            <#--<button id="data-query" class="btn btn-primary btn-sm">查询</button>-->
        <#--<button id="data-reset" class="btn btn-primary btn-sm">重置</button>-->
        </div>

    </div>
    <div class="tablediv m-auto">
    <#--数据操作-->
        <div class="query-operation">

        </div>

    <#--内容表格-->
        <table id="record-table"></table>
    </div>

</div>


<#--遮罩层-->
<div id="record-show-div" class="maskLayer" style="display: none;">
    <div class="modal-body">
        <div class="card">
            <input class="recordId" hidden>
            <div class="card-header">
                <p class="date" style="position: absolute;right: 1rem;top: 1.5rem;"></p>
                <h5>原题：</h5>
                <img class="questionPic img-thumbnail">
                <p class="questionDetail"></p>
            </div>
            <div class="card-body">
                <h5>参考答案：</h5>
                <p class="solution"></p>
                <img class="sulutionPic img-thumbnail">
                <h5>用户输入答案：</h5>
                <textarea rows="5" class="userSolution" style="width: 100%" disabled></textarea>
                <img class="recordUserPic img-thumbnail">
            </div>
            <div class="card-footer">
                <h5>获得分数：</h5>
                <p class="score"></p>
                <h5>总分：</h5>
                <p class="questionScore"></p>
            </div>
        </div>
    </div>
</div>

<style>
    #record-table tbody tr :hover{
        cursor: pointer;
    }
</style>

<script>
$(function () {

    //表格
    $('#record-table').bootstrapTable({
        url: '/record/pageList.json',
        pagination: true,           //分页
        sidePagination: "server",   //服务端处理分页
        pageNumber: 1,              //初始化加载第一页，默认第一页
        pageSize: 20,               //每页的记录行数（*）
        pageList: [10, 20, 30, 50],        //可供选择的每页的行数（*）
        uniqueId: "recordId",         //隐藏的id
        // queryParamsType:'',
        queryParams: function (params) {
            var temp = {
                pageNumber: (params.offset / params.limit) + 1,     //页数
                pageSize: params.limit,                             //每页的记录行数
                userAccount : $("#loginUserAccount").val()
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
                field: 'date',
                title: '做题时间',
                 formatter: timeFormatter
            },{
                field: 'questionDetail',
                title: '题目',
                formatter: detailFormatter
            },{
                field: 'score',
                title: '获得分值'
            },{
                field: 'questionScore',
                title: '总分'
            }
            ]
    });

    //
    // //查询按钮
    // $("#data-query").on("click", function () {
    //     $('#record-table').bootstrapTable('refresh');
    // })


    function detailFormatter(value, row, index) {
        if(value.length<30) return value;
        else return value.substring(0,30)+"  ..."
    }

    function timeFormatter(value, row, index) {
        return getShowTime(value)
    }

    //后端返回时间转换为显示时间
    function getShowTime(time) {
        time = time.substring(0, 4) + "-" + time.substring(4, 6) + "-" + time.substring(6, 8)
            + " " + time.substring(8, 10) + ":" + time.substring(10, 12) + ":" + time.substring(12)
        return time;
    }

    $(document).on("click","tr",function () {
        var recordId = $(this).attr("data-uniqueid")
        layui.use('layer', function () {
            layer = layui.layer;
            layer.open({
                type: 1,
                zIndex: "1",
                title: '记录详情',
                // area: ['800px'],
                content: $('#record-show-div'),
                btn: ['关闭'],
                yes: function (index) {
                    layer.close(index);
                },
                cancel: function (index) {
                    layer.close(index);
                }
            })

            if (!/(iPhone|iPad|iPod|iOS|Android)/i.test(navigator.userAgent)) { //非移动端
                $(".layui-layer-page").css("width", $(window).width() * 0.6)
                $(".layui-layer-page").css("max-width", "60rem")
                $(".layui-layer-page").css("left", $(window).width() * 0.2)
                $(".layui-layer-page").css("top", "1rem")
                $(".layui-layer-page").css("max-height", $(window).height() * 0.7)
                $(".layui-layer-page").css("overflow", "scroll")
            }

            $.ajax({
                url: "/record/findById",
                dataType: "json",
                data: {
                    "id":recordId
                },
                type: "post",
                success: function (result) {
                    if (result.data.questionDetail == null || result.data.questionDetail == "") {
                        $("#record-show-div .questionDetail").hide()
                    }
                    else {
                        $("#record-show-div .questionDetail").show()
                        $("#record-show-div .questionDetail").html(result.data.questionDetail);
                    }

                    if (result.data.questionPic == null) {
                        $("#record-show-div .questionPic").hide()
                    }
                    else {
                        $("#record-show-div .questionPic").show()
                        $("#record-show-div .questionPic").attr("src", "data:image/jpeg;base64," + result.data.questionPic);
                    }

                    if (result.data.solution == null || result.data.solution == "") {
                        $("#record-show-div .solution").hide()
                    }
                    else {
                        $("#record-show-div .solution").show()
                        $("#record-show-div .solution").html(result.data.solution);
                    }

                    if (result.data.sulutionPic == null) {
                        $("#record-show-div .sulutionPic").hide()
                    }
                    else {
                        $("#record-show-div .sulutionPic").show()
                        $("#record-show-div .sulutionPic").attr("src", "data:image/jpeg;base64," + result.data.sulutionPic);
                    }

                    if (result.data.userSolution == null || result.data.userSolution == "") {
                        $("#record-show-div .userSolution").hide()
                    }
                    else {
                        $("#record-show-div .userSolution").show()
                        $("#record-show-div .userSolution").html(result.data.userSolution);
                    }

                    if (result.data.recordUserPic == null) {
                        $("#record-show-div .recordUserPic").hide()
                    }
                    else {
                        $("#record-show-div .recordUserPic").show()
                        $("#record-show-div .recordUserPic").attr("src", "data:image/jpeg;base64," + result.data.recordUserPic);
                    }

                    $("#record-show-div .score").html(result.data.score);
                    $("#record-show-div .questionScore").html(result.data.questionScore);
                },
                error: function (result) {
                }
            })


        })
    })


})




</script>