<#import "/main/macros.ftl" as macros>
<#assign importCss=[]>
<#assign importJs=[]>
<@macros.navhead importJs=importJs importCss=importCss></@macros.navhead>

<div class="main-contain" style="margin-top: 1rem">
    <#--轮播-->
    <div id="carousel-qs" class="carousel slide" data-ride="carousel">

        <!-- 指示符 -->
        <ul class="carousel-indicators">
            <li data-target="#carousel-qs" data-slide-to="0" class="active"></li>
            <li data-target="#carousel-qs" data-slide-to="1"></li>
            <li data-target="#carousel-qs" data-slide-to="2"></li>
        </ul>

        <!-- 轮播图片 -->
        <div class="carousel-inner">

        </div>

        <!-- 左右切换按钮 -->
        <a class="carousel-control-prev" href="#carousel-qs" data-slide="prev">
            <span class="carousel-control-prev-icon"></span>
        </a>
        <a class="carousel-control-next" href="#carousel-qs" data-slide="next">
            <span class="carousel-control-next-icon"></span>
        </a>
    </div>

    <div class="row my-title-div">
        <div class="col-6 my-title">
            <h3>
                我的做题统计
            </h3>
        </div>
    </div>

<#--学生最近15天概况（做题数、正确率）（柱形图、折线图）-->
    <div id="studentHistoryGraphics" class="index-graphics" style="width: 30rem;height:20rem;"></div>

<#--错误率按照题目类型-->
    <div id="studentErrorByKindGraphics" class="index-graphics" style="width: 30rem;height:20rem;"></div>

<#--正确率按照题目难度-->
    <div id="studentRecordByDiffGraphics" class="index-graphics" style="width: 30rem;height:20rem;"></div>
</div>

<style>
    .index-graphics{
        float: left;
    }
    .index-graphics {
        background-color: rgba(0, 0, 0, 0.02);
        margin: 1rem;
        border-radius: 1rem;
    }
    .carousel-img{
        width: 66rem;
        height: 38rem;
    }
    .my-title{
        margin-left: 1rem;
        margin-top: 1rem;
        padding: 0.5rem;
        border-bottom: solid 1px rgba(152, 152, 152, 0.69);
    }
    .my-title-div{
        /*border-bottom: solid 1px #5f5f5f;*/
    }
</style>

<script>
    if (/(iPhone|iPad|iPod|iOS|Android)/i.test(navigator.userAgent)) { //移动端
        $("#studentHistoryGraphics").css("width", $(window).width() * 0.9)
        $("#studentHistoryGraphics").css("height", $(window).width() * 0.6)
        $("#studentErrorByKindGraphics").css("width", $(window).width() * 0.9)
        $("#studentErrorByKindGraphics").css("height", $(window).width() * 0.6)
        $("#studentRecordByDiffGraphics").css("width", $(window).width() * 0.9)
        $("#studentRecordByDiffGraphics").css("height", $(window).width() * 0.6)
        // $(".main-contain").css("width","90%")
    }
     $(".main-contain").css("min-height", $(window).height() * 1.5)
    $("#carousel-qs").css("width", "66rem")


    $.ajax({
        type: "post",
        async: true,
        url: "questionSet/findAll",
        dataType: "json",
        success: function (result) {
            if (result.success == true) {
                for (var i = 0; i < 3; i++) {
                    $(".carousel-inner").append("            <div class=\"carousel-item\">\n" +
                            "                <img class='carousel-img' src='data:image/jpeg;base64," + result.data[i].qsPic + "'>\n" +
                            "               <div class=\"carousel-caption\">\n" +
                            "                   <h3>"+result.data[i].qsName+"</h3>\n" +
                            "               </div>"+
                            "            </div>")
                }
                $(".carousel-inner").find(".carousel-item").eq(0).addClass("active")

            } else {
                alert("后台数据获取失败!");
            }
        },
        error: function (errorMsg) {
            //请求失败时执行该函数
            alert("图表请求数据失败!");
            chart1.hideLoading();
        }
    })

    $(document).on("click",".carousel-img",function () {
        location.href="/questionSet/list"
    })

    //chart1----------------------------------------------------------------------------------------------------
    var chart1 = echarts.init(document.getElementById('studentHistoryGraphics'));
    chart1.showLoading(); //数据加载完之前先显示一段简单的loading动画
    $.ajax({
        type: "post",
        async: true,
        url: "record/get15daysRecordData",
        dataType: "json",
        success: function (result) {
            if (result.success == true) {

                chart1.hideLoading(); //隐藏加载动画
                chart1.setOption({ //加载数据图表
                    title: {
                        text: '最近概况',
                        textStyle: {
                            color: '#898989'
                        }
                    },
                    dataZoom: [
                        {   // dataZoom组件，默认控制x轴
                            type: 'slider',
                            start: 50,
                            end: 100
                        },
                        {   // 也控制x轴,用于实现滚轮控制
                            type: 'inside',
                            start: 50,
                            end: 100
                        }
                    ],
                    xAxis: {
                        data: result.data.dates
                    },
                    legend: {
                        data: ['做题正确率', '做题数量']
                    },
                    yAxis: [
                        {
                            type: 'value',
                            scale: true,
                            name: '正确率',
                            max: 1,
                            min: 0,
                            boundaryGap: [0.2, 0.2]
                        },
                        {
                            type: 'value',
                            scale: true,
                            name: '做题数',
                            max: Math.ceil(Math.max.apply(null, result.data.nums) / 5) * 5,
                            min: 0,
                            boundaryGap: [0.2, 0.2]
                        }
                    ],
                    series: [{
                        name: '做题正确率',
                        type: 'line',
                        yAxisIndex: 0,
                        data: result.data.rates
                    },
                        {
                            name: '做题数量',
                            type: 'bar',
                            yAxisIndex: 1,
                            data: result.data.nums
                        }]
                });

            } else {
                alert("后台数据获取失败!");
            }
        },
        error: function (errorMsg) {
            //请求失败时执行该函数
            alert("图表请求数据失败!");
            chart1.hideLoading();
        }
    })

    //chart2----------------------------------------------------------------------------------------------------
    var chart2 = echarts.init(document.getElementById('studentErrorByKindGraphics'));
    chart2.showLoading(); //数据加载完之前先显示一段简单的loading动画

    $.ajax({
        type: "post",
        async: true,
        url: "record/getErrorCountGroupByKind",
        dataType: "json",
        success: function (result) {
            if (result.success == true) {
                chart2.hideLoading(); //隐藏加载动画
                chart2.setOption({ //加载数据图表
                    title: {
                        text: '错题分布',
                        left: 'center',
                        top: 5,
                        textStyle: {
                            color: '#898989'
                        }
                    },

                    tooltip: {
                        trigger: 'item',
                        formatter: '{b} : {c} ({d}%)--点击去练习'
                    },

                    series: [
                        {
                            name: '错题',
                            type: 'pie',
                            radius: ['50%', '70%'],
                            avoidLabelOverlap: false,
                            label: {
                                show: false,
                                position: 'center'
                            },
                            emphasis: {
                                label: {
                                    show: true,
                                    fontSize: '30',
                                    fontWeight: 'bold'
                                }
                            },
                            labelLine: {
                                show: false
                            },
                            data: result.data
                        }
                    ]
                });
                chart2.dispatchAction({type: 'highlight', seriesIndex: 0, dataIndex: 0});
            } else {
                alert("后台数据获取失败!");
            }
        },
        error: function (errorMsg) {
            //请求失败时执行该函数
            alert("图表请求数据失败!");
            chart1.hideLoading();
        }
    })

    chart2.on('mouseover',(v) => {
        if(v.dataIndex != 0){
            chart2.dispatchAction({
                type: 'downplay',
                seriesIndex: 0,
                dataIndex: 0
            });
        }
    });

    chart2.on('click', function (params) {
        window.open("/record/doQuestion?type="+escape(params.name))
    });

    chart2.on('mouseout',(v) => {
        chart2.dispatchAction({type: 'highlight',seriesIndex: 0,dataIndex: 0});
    });


    //chart3----------------------------------------------------------------------------------------------------
    var chart3 = echarts.init(document.getElementById('studentRecordByDiffGraphics'));
    chart3.showLoading(); //数据加载完之前先显示一段简单的loading动画

    $.ajax({
        type: "post",
        async: true,
        url: "record/getRecordByDiff",
        dataType: "json",
        success: function (result) {
            if (result.success == true) {
                var data=[['难度', '做题比例', '正确率']]
                if(result.data.diff1!=null) data.push(['难度1', result.data.diff1.correctRate, result.data.diff1.numberRate])
                else data.push(['难度1', 0.1, 0.1])
                if(result.data.diff2!=null) data.push(['难度2', result.data.diff2.correctRate, result.data.diff2.numberRate])
                else data.push(['难度2', 0.1, 0.1])
                if(result.data.diff3!=null) data.push(['难度3', result.data.diff3.correctRate, result.data.diff3.numberRate])
                else data.push(['难度3', 0.1, 0.1])
                chart3.hideLoading(); //隐藏加载动画
                chart3.setOption({ //加载数据图表
                    title: {
                        text: '做题难度分布/各难度正确率',
                        left: 'center',
                        top: 10,
                        textStyle: {
                            color: '#898989'
                        }
                    },

                    tooltip: {
                        formatter: function (params, ticket, callback) {
                            if(params.seriesIndex==0){
                                return params.name+"占比："+params.data[1]+"--点击去练习";
                            }
                            else {
                                return params.name+"正确率："+params.data[1]+"--点击去练习";
                            }
                        }
                    },
                    dataset: {
                        source: data
                    },
                    series: [{
                        title: {
                            text: '各难度',
                            left: 'center',
                            top: 80,
                            textStyle: {
                                color: '#898989'
                            }
                        },
                        type: 'pie',
                        radius: 70,
                        center: ['30%', '60%'],
                        encode: {
                            itemName: '难度',
                            value: '做题比例'
                        }
                    }, {
                        type: 'pie',
                        radius: 60,
                        center: ['70%', '60%'],
                        encode: {
                            itemName: '难度',
                            value: '正确率'
                        }
                    }]
                });
            } else {
                alert("后台数据获取失败!");
            }
        },
        error: function (errorMsg) {
            //请求失败时执行该函数
            alert("图表请求数据失败!");
            chart1.hideLoading();
        }
    })

    chart3.on('click', function (params) {
        window.open("/record/doQuestion?diff="+escape(params.name))
    });


</script>