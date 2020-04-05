<#import "/main/macros.ftl" as macros>
<#assign importCss=[]>
<#assign importJs=[]>
<@macros.navhead importJs=importJs importCss=importCss titleName="申请"></@macros.navhead>

<div class="main-contain" style="margin-top: 1rem">
    <div class="searchdiv m-auto row">

        <div class="row form-group col-5">
            <label class="control-label col-4 text-right" for="">状态:</label>
            <div class="col-8">
                <@macros.statusSelect id="statusSelect" ></@macros.statusSelect>
            </div>
        </div>

        <div class="row buttons">
            <button id="data-query" class="btn btn-primary btn-sm">查询</button>
        <#--<button id="data-reset" class="btn btn-primary btn-sm">重置</button>-->
        </div>

    </div>
    <div class="tablediv m-auto">

    <#--内容表格-->
        <table id="superiorApply-table"></table>
    </div>

</div>

<script>
    //表格
    $('#superiorApply-table').bootstrapTable({
        url: '/apply/superiorApplyPageList.json',
        pagination: true,           //分页
        sidePagination: "server",   //服务端处理分页
        pageNumber: 1,              //初始化加载第一页，默认第一页
        pageSize: 20,               //每页的记录行数（*）
        pageList: [10, 20, 30, 50],        //可供选择的每页的行数（*）
        uniqueId: "applyId",         //隐藏的id
        // queryParamsType:'',
        queryParams: function (params) {
            var temp = {
                pageNumber: (params.offset / params.limit) + 1,     //页数
                pageSize: params.limit,                             //每页的记录行数
                status: $("#statusSelect").val(),
                userAccount: $("#loginUserAccount").val(),
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
                field: 'type',
                title: '类型'
            }, {
                field: 'oldName',
                title: '原'
            }, {
                field: 'newName',
                title: '新'
            }, {
                field: 'status',
                title: '状态'
            }, {
                field: 'userName',
                title: '申请人'
            }, {
                field: 'reason',
                title: '备注'
            }, {
                field: 'reviewContent',
                title: '审核意见'
            }, {
                field: 'action',
                title: '操作',
                formatter: actionFormatter
            },]
    });

    //操作栏的格式化
    function actionFormatter(value, row, index) {
        var id = value;
        var result = "";
        result += "<a href='javascript:;'  class='re-pwd' title='pass-apply'><span>同意</span></a>&nbsp;";
        result += "<a href='javascript:;'  class='re-pwd' title='un-pass-apply'><span>拒绝</span></a>&nbsp;";

        return result;
    }

    $(document).on("click", ".pass-apply", function () {
        var applyId = $(this).parent().parent().attr("data-uniqueid");
        $.ajax({
            url: "/user/updateSuperior",
            data: {
                "userId": userId
            },
            dataType: "json",
            type: "post",
            success: function (result) {
                if (result.success == true) {
                    layer.msg("用户已删除", {icon: 1, time: 2000});
                    $("#user-table").bootstrapTable('refresh');
                } else {
                    layer.msg(result.msg, {icon: 2});
                }
            },
            error: function (result) {
            }
        })

    })
</script>