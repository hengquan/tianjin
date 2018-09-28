$(function () { 
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('main'));

    // 指定图表的配置项和数据
    var option = {
        tooltip: {},
        xAxis: {
            data: ["法规类","政策类","收费类","放行类","理货类","出口类","进口类"]
        },
        legend: {
            data: ['总数量', '近期访问数']
        },
        yAxis: {},
        series: [
            {
                name: '总数量',
                type: 'bar',
                data: [10, 20, 30, 20, 10, 10, 5]
            },
            {
                name: '近期访问数',
                type: 'bar',
                data: [11, 22, 33, 44, 55, 66, 77]
            }
        ]
    };

    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);

    // 右侧柱状图
    var myChart2 = echarts.init(document.getElementById('main2'));

    // 指定图表的配置项和数据
    var option2 = {
        tooltip: {},
        xAxis: {
            data: ["法规类","政策类","收费类","放行类","理货类","出口类","进口类"]
        },
        yAxis: {},
        legend: {
            data: ['总数量', '近期访问数']
        },
        series: [
            {
                name: '总数量',
                type: 'bar',
                data: [10, 20, 30, 20, 10, 10, 5]
            },
            {
                name: '近期访问数',
                type: 'bar',
                data: [11, 22, 33, 44, 55, 66, 77]
            }
        ]
    };

    // 使用刚指定的配置项和数据显示图表。
    myChart2.setOption(option2);
    window.onresize = function(){
        myChart.resize();
        myChart2.resize();
    
    }
 });