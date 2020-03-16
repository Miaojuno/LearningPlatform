<#import "/main/macros.ftl" as macros>
<#assign importCss=[]>
<#assign importJs=[]>
<@macros.navhead importJs=importJs importCss=importCss titleName="添加题集"></@macros.navhead>

<div class="main-contain addDiv" style="margin-top: 1rem">

    <h5>新增题集</h5>

    <form id="questionSetAddForm">
        <div class="row">
            <label class="control-label col-2 text-right" for="">题集名*</label>
            <div class="col-10">
                <input type="text" class="qsName form-control" name="qsName" placeholder="请输入题集名">
            </div>
        </div>

        <div class="row">
            <label class="control-label col-2 text-right" for="">题集封面*</label>
            <div class="col-10">
                <input type="file" class="file1" id="file1" hidden>
                <input type="text" class="fileInput1 form-control" placeholder="题目图片 ，可选 ，jpg/png">
            </div>
        </div>

        <div class="row">
            <label class="control-label col-2 text-right" for="">用户*</label>
            <div class="col-10">
                    <@macros.userChoose class="userIds" name="userIds" type="multiple"></@macros.userChoose>
            </div>
        </div>


        <div class="col-10">
            <button class="goAddQuestion btn btn-primary" style="margin-top: 1rem; margin-left: 1rem;">添加题目
            </button>
        </div>
    </form>


</div>




<script>
    $(function () {
        $('.fileInput1').on('click', function () {
            $(".addDiv .file1").click()
        });

        $(".file1").bind("input propertychange", function () {
            $('.addDiv .fileInput1').val($('.addDiv .file1').val())
        });


        $(".goAddQuestion").on("click", function () {
            var data = new FormData();
            var fileObj1 = document.getElementById("file1").files[0];
            data.append("file", fileObj1);
            data.append("qsName", $(".qsName").val());
            data.append("userIds", $(".userIds").val());
            data.append("qsOwner", $("#loginUserAccount").val());
            $.ajax({
                url: "/questionSet/addQuestionSet",
                data: data,
                dataType: "json",
                type: "post",
                processData: false,
                contentType: false,
                success: function (result) {
                    if (result.success == true) {
                        window.location.href = "/neo/addQuestion?questionSetId=" + result.qsId;
                    } else {
                        layer.msg(result.msg, {icon: 2});
                    }
                },
                error: function (result) {
                }
            })

        })

    });
</script>