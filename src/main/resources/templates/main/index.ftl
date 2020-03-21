<#import "/main/macros.ftl" as macros>
<#assign importCss=[]>
<#assign importJs=[]>
<@macros.navhead importJs=importJs importCss=importCss></@macros.navhead>

<div class="main-contain" style="margin-top: 1rem">
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

    $(".main-contain").css("min-height", $(window).height() * 0.9)


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
                        top: 20,
                        textStyle: {
                            color: '#898989'
                        }
                    },

                    tooltip: {
                        trigger: 'item',
                        formatter: '{b} : {c} ({d}%)'
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
        console.log(params.name);
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
                        top: 20,
                        textStyle: {
                            color: '#898989'
                        }
                    },

                    tooltip: {},
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
                        center: ['27%', '50%'],
                        encode: {
                            itemName: '难度',
                            value: '做题比例'
                        }
                    }, {
                        type: 'pie',
                        radius: 60,
                        center: ['73%', '50%'],
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


</script>