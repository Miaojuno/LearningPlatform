<#import "/main/macros.ftl" as macros>
<#assign importCss=[]>
<#assign importJs=[]>
<@macros.navhead importJs=importJs importCss=importCss></@macros.navhead>

<div class="main-contain" style="margin-top: 2rem">
    <row>
        <label class="control-label col-12" for="">导入测试:</label>
        <div class="col-10">
            <input type="file" id="file" hidden >
            <input type="text" id="fileInput" class="form-control" placeholder="请选择文件">
        </div>
        <button id="submitUpload" class="btn btn-primary" style="margin-top: 1rem; margin-left: 1rem;">确定</button>
    </row>
</div>

<script>
    // $("#file").change(function () {
    //     alert(1)
    //     UpladFile();
    // });
    if (/(iPhone|iPad|iPod|iOS|Android)/i.test(navigator.userAgent)) { //移动端
        $(".main-contain").css("height",$(window).height()*0.9)
        $(".main-contain").css("width","90%")
    }

    $('#submitUpload').on('click', function () {
        UpladFile();
    });

    $('#fileInput').on('click', function () {
        $("#file").click()
    });

    $("#file").bind("input propertychange", function() {
        $('#fileInput').val($('#file').val())
    });


    function UpladFile() {
        layui.use('layer',function(){
            layer=layui.layer;
            var fileObj = document.getElementById("file").files[0]; // js 获取文件对象
            var url =  "/excelupload"; // 接收上传文件的后台地址
            var form = new FormData(); // FormData 对象
            form.append("file", fileObj); // 文件对象

            var name=fileObj.name.split(".")
            if(name[name.length-1] != "xlsx"){
                layer.confirm("导入失败,只支持xlsx格式文件",{icon:2}, function (index) {
                    layer.close(index);
                });
            }
            // else {
                $.ajax({
                    type: "post",
                    url: url,
                    data: form,
                    processData: false,
                    contentType:false,
                    success: function (res) {
                        if ( !res.success ) {
                            layer.confirm("导入失败:"+res.msg,{icon:2}, function (index) {
                                layer.close(index);
                            });
                            $("#file").val("");
                            $("#fileInput").val("");
                        }
                        else {
                            layer.msg("导入成功",{icon:1});
                        }
                    },
                    error: function (err) {
                        layer.msg("导入失败",{icon:2});
                        $("#file").val("");
                        $("#fileInput").val("");
                    }
                });
            // }
        })

    }
</script>