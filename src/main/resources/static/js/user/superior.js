$(function () {
    //表格
    $('#superior-table').bootstrapTable({
        url: '/user/superiorPageList.json',
        pagination: true,           //分页
        sidePagination: "server",   //服务端处理分页
        pageNumber: 1,              //初始化加载第一页，默认第一页
        pageSize: 20,               //每页的记录行数（*）
        pageList: [10, 20, 30, 50],        //可供选择的每页的行数（*）
        uniqueId: "userId",         //隐藏的id
        // queryParamsType:'',
        queryParams : function(params){
            var temp={
                pageNumber: (params.offset / params.limit) + 1,     //页数
                pageSize: params.limit,                             //每页的记录行数
                userName : $("#superiorName").val(),
                subordinateId : $("#subordinateId").val()
            };
            return temp;
        },
        responseHandler:function(res) {
            return {
                "total":res.total,//总条目数
                "rows": res.data //数据
            }
        },
        columns: [
        {
            checkbox: true,
            visible: true                  //显示复选框
        }, {
            field: 'userAccount',
            title: '账户'
        }, {
            field: 'userName',
            title: '姓名'
        }, {
            field: 'roleName',
            title: '角色' ,
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
        result += "<a href='javascript:;'  class='changeSuperior' title='选择'><span>选择</span></a>";
        return result;
    }

    //打开上级设置遮罩层
    $(document).on("click",".edit-superior",function (){
        var userId=$(this).parent().parent().attr("data-uniqueid");
        $("#subordinateId").val(userId);
        $("#superior-table").bootstrapTable('refresh');
        layui.use('layer',function(){
            layer=layui.layer;

            layer.open({
                type: 1,
                zIndex:"1",
                title: '配置上级',
                area: ['800px'],
                content: $('#user-superior-div'),
                btn: ['确定','关闭'],
                yes: function (index) {
                    layer.close(index);
                    $("#superior-modify-form")[0].reset()
                },
                btn2: function (index) {
                    layer.close(index);
                    $("#superior-modify-form")[0].reset()
                },
                cancel: function(index){
                    layer.close(index);
                    $("#superior-modify-form")[0].reset()
                }
            })
        })
    })

    $(document).on("click",".changeSuperior",function (){
        console.log(1)
    })


})


