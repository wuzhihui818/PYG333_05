<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>ECharts</title>
</head>
<body>
<!-- 引入ECharts和JQuery文件 -->
<script src='../js/plugins/echarts/echarts.common.min.js'></script>
<script src="../js/plugins/jquery/jquery.min.js"></script>
<script>
    //将集合中的数据保留两位小数
    function dataToFixed(data) {
        var seriesData = [];
        for (var i = 0; i < data.seriesSaleList.length; i++) {
            //将销量保留两位小数
            var temp = data.seriesSaleList[i].toFixed(2);
            seriesData.push(temp);
        }
        return seriesData;
    }
    //生成图标的方法
    function generateChart(data) {
        //基于准备好的DOM，初始化echarts实例
        var myChart = echarts.init(document.getElementById('main'));
        //指定图表的配置项和数据
        myChart.clear();
        var option = {
            title: {
                text: '销售金额折线图'
            },
            //提示框组件
            tooltip: {
                //坐标轴触发，主要用于柱状图，折线图等
                trigger: 'axis'
            },
            //数据全部显示
            axisLabel: {
                interval: 0
            },
            //图例
            legend: {
                data: ['销售额']
            },
            //横轴
            xAxis: {
                data: data.xAxisList
            },
            //纵轴
            yAxis: {},
            //系列列表。每个系列通过type决定自己的图表类型
            series: [
                {
                    name: '销售额',
                    //折线图
                    type: 'line',
                    data: dataToFixed(data)//处理小数点数据
                }
            ]
        };
        //使用刚指定的配置项和数据显示图表
        myChart.setOption(option);
    }
    //buttion调用的方法
    function getData() {
        var start = $('#start').val();
        var end = $('#end').val();
        //异步请求
        $.post(
            "../ordersale/getData.do",//访问地址
            {start: start, end: end},//携带的参数
            function (data) {
                generateChart(data);
            },
            "json"
        );
    }
</script>
<div align="center">
    <label for="start">开始日期：</label><input id="start" type="date" value="2018-08-13"/>
    <label for="end">结束日期：</label><input id="end" type="date" value="2018-08-16"/>
    <button onclick="getData()">查询</button>
</div>
<hr>
<!-- 为ECharts准备一个具备大小（宽高）的DOM容器-->
<div id='main' style='width: 90%;height:400px;' align="center"></div>
</body>
</html>