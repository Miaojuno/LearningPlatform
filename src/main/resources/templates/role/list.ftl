<#import "/main/macros.ftl" as macros>
<#assign importCss=[]>
<#assign importJs=[]>
<@macros.navhead importJs=importJs importCss=importCss></@macros.navhead>

<div class="main-contain" style="margin-top: 1rem">
    <div class="searchdiv m-auto row">
        <#--<div class="row">-->
            <#--<label class="control-label col-2" for="">用户名:</label>-->
            <#--<div class="col-10 ">-->
                <#--<input name="userName" class="form-control input-sm" type="text" placeholder="请输入">-->
            <#--</div>-->
        <#--</div>-->
        <#--<div class="row">-->
            <#--<button id="data-query" class="btn btn-sm btn-submit bt-query">查询</button>-->
            <#--<button type="reset" id="data-reset" class="btn btn-sm btn-reset">重置</button>-->
        <#--</div>-->
    </div>
    <div class="tablediv m-auto">
    <#--数据操作-->
        <div class="query-operation">
            <button type="button" id="user-add" class="btn btn-outline-primary btn-sm">新增</button>
        </div>

        <table id="role-table"></table>
    </div>
</div>

<script>
    $('#role-table').bootstrapTable({
        url: '/role/pageList.json',
        pagination: true,           //分页
        // search: true,               //显示搜索框
        sidePagination: "server",   //服务端处理分页
        pageNumber: 1,              //初始化加载第一页，默认第一页
        pageSize: 20,               //每页的记录行数（*）
        pageList: [10, 20, 30, 50],        //可供选择的每页的行数（*）
        uniqueId: "roleId",         //隐藏的id
        // queryParamsType:'',
        queryParams : function(params){
            var temp={
                pageNumber: (params.offset / params.limit) + 1,     //页数
                pageSize: params.limit,                             //每页的记录行数
            };
            return temp;
        },
        responseHandler:function(res) {
            return {
                "total":res.total,//总条目数
                "rows": res.data //数据
            }
        },
        columns: [{
            field: 'roleName',
            title: '账户'
        }, {
            field: 'roleDesc',
            title: '姓名'
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
        result += "<a href='javascript:;'  title='查看'><span class=''>查看</span></a>&nbsp;&nbsp;&nbsp;";
        result += "<a href='javascript:;'  title='编辑'><span class=''>编辑</span></a>&nbsp;&nbsp;&nbsp;";
        result += "<a href='javascript:;'  title='删除'><span class=''>删除</span></a>";

        return result;
    }

</script>