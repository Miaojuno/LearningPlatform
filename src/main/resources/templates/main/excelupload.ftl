<#import "/main/macros.ftl" as macros>
<#assign importCss=[]>
<#assign importJs=[]>
<@macros.navhead importJs=importJs importCss=importCss titleName="上传"></@macros.navhead>

<div class="main-contain" style="margin-top: 2rem">
    <div class="row">
        <label class="control-label col-12" for="">数据导入:</label>
        <div class="col-10">
            <input type="file" id="file" hidden >
            <input type="text" id="fileInput" class="form-control" placeholder="请选择文件" readonly>
        </div>
        <button id="submitUpload" class="btn btn-primary" style="">确定</button>
    </div>

    <div class="progress" style="margin-top: 4rem">
        <div class="progress-bar progress-bar-striped progress-bar-animated" style="width:0;"></div>
    </div>

</div>

<script>
    // $("#file").change(function () {
    //     alert(1)
    //     UpladFile();
    // });
    if (/(iPhone|iPad|iPod|iOS|Android)/i.test(navigator.userAgent)) { //移动端
        $(".main-contain").css("height",$(window).height()*0.9)
        // $(".main-contain").css("width","90%")
    }

    $(".progress").css('background-color','rgba(0, 0, 0, 0)');
    var allWidth=parseFloat($(".progress").css("width").substring(0,$(".progress").css("width").length-2))
    var n=0;
    var dt;

    $('#submitUpload').on('click', function () {
        layui.use('layer',function() {
            if (document.getElementById("file").files[0] == null) {
                layer.msg("请选择文件", {icon: 2});
                return
            }
        $(".progress").css('background-color','#e9ecef');
        dt=setInterval(function () {
            if(n<400){
                console.log(n)
                n=n+1
                $(".progress-bar").css("width",allWidth/500*n)
            }

        },"100");
        UpladFile();
        })
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
            var url =  "/neo/excelupload"; // 接收上传文件的后台地址
            var form = new FormData(); // FormData 对象
            form.append("file", fileObj); // 文件对象

            var name=fileObj.name.split(".")
            if(name[name.length-1] != "xlsx"){
                layer.confirm("导入失败,只支持xlsx格式文件",{icon:2}, function (index) {
                    layer.close(index);
                });
            }
            else {
                $.ajax({
                    type: "post",
                    url: url,
                    data: form,
                    processData: false,
                    contentType:false,
                    success: function (res) {
                        layer.confirm(res.msg, function (index) {
                            layer.close(index);
                        });
                        $("#file").val("");
                        $("#fileInput").val("");
                        $(".progress-bar").css("width","0")
                        n=0
                        clearInterval(dt);
                        $(".progress").css('background-color','rgba(0, 0, 0, 0)');
                    },
                    error: function (err) {
                        layer.msg("导入失败",{icon:2});
                        $("#file").val("");
                        $("#fileInput").val("");
                        $(".progress-bar").css("width","0")
                        n=0
                        clearInterval(dt);
                        $(".progress").css('background-color','rgba(0, 0, 0, 0)');
                    }
                });
            }
        })

    }
</script>