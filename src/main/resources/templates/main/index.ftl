<#import "/main/macros.ftl" as macros>
<#assign importCss=[]>
<#assign importJs=[]>
<@macros.navhead importJs=importJs importCss=importCss></@macros.navhead>

<div class="main-contain" style="margin-top: 1rem">
    <#--学生最近15天概况（做题数、正确率）（柱形图、折线图）-->
    <div id="studentHistoryGraphics" style="width: 600px;height:400px;"></div>
</div>

<script>

    var chart1 = echarts.init(document.getElementById('studentHistoryGraphics'));
    // 显示标题，图例和空的坐标轴
    chart1.setOption({
        title: {
            text: '统计图表异步数据加载示例'
        },
        tooltip: {
            trigger: 'axis', //坐标轴触发提示框，多用于柱状、折线图中
        },
        legend: {
            data: ['销量']
        },
        toolbox: {
            feature: {
                dataView: {show: true, readOnly: false},
                magicType: {show: true, type: ['line', 'bar']},
                restore: {show: true},
                saveAsImage: {show: true}
            }
        },
        xAxis: {
            type: 'category',
            data: []
        },
        yAxis: {},
        series: [{
            name: '销量',
            type: 'bar',
            data: []
        }]
    });

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
                        text: '最近概况'
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

    // chart1.setOption(option);

</script>